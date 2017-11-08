/*
 * Copyright © 2013-2017, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.business.modelmapper;

import javax.inject.Inject;
import org.assertj.core.api.Assertions;
import org.javatuples.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.business.assembler.Assembler;
import org.seedstack.business.modelmapper.fixtures.Address;
import org.seedstack.business.modelmapper.fixtures.Customer;
import org.seedstack.business.modelmapper.fixtures.Name;
import org.seedstack.business.modelmapper.fixtures.Order;
import org.seedstack.business.modelmapper.fixtures.Recipe;
import org.seedstack.business.util.Tuples;
import org.seedstack.seed.it.SeedITRunner;

@RunWith(SeedITRunner.class)
public class ModelMapperTupleAssemblerIT {

    @Inject
    @ModelMapper
    private Assembler<Pair<Order, Customer>, Recipe> defaultTupleAssembler;

    @Test
    public void testAssembleDtoFromAggregate() {
        Customer customer = new Customer(new Name("John", "Doe"));
        Order order = new Order(new Address("main street", "bevillecity"));

        Pair<Order, Customer> tuple = Tuples.create(order, customer);
        Recipe recipe = defaultTupleAssembler.createDtoFromAggregate(tuple);

        Assertions.assertThat(recipe.getCustomerFirstName()).isEqualTo("John");
        Assertions.assertThat(recipe.getCustomerLastName()).isEqualTo("Doe");
        Assertions.assertThat(recipe.getBillingCity()).isEqualTo("bevillecity");
        Assertions.assertThat(recipe.getBillingStreet()).isEqualTo("main street");
    }

    @Test
    public void testUpdateDtoFromAggregate() {
        Customer customer = new Customer(new Name("John", "Doe"));
        Order order = new Order(new Address("main street", "bevillecity"));
        Recipe recipe = new Recipe("Jane", "Doe", "", "");

        Pair<Order, Customer> tuple = Tuples.create(order, customer);
        defaultTupleAssembler.mergeAggregateIntoDto(tuple, recipe);

        Assertions.assertThat(recipe.getCustomerFirstName()).isEqualTo("John");
        Assertions.assertThat(recipe.getCustomerLastName()).isEqualTo("Doe");
        Assertions.assertThat(recipe.getBillingCity()).isEqualTo("bevillecity");
        Assertions.assertThat(recipe.getBillingStreet()).isEqualTo("main street");
    }

    @Test
    public void testMergeAggregateWithDto() {
        Customer customer = new Customer(new Name("John", "Doe"));
        Order order = new Order(null);
        Recipe recipe = new Recipe("John", "Doe", "main street", "bevillecity");

        Pair<Order, Customer> tuple = Tuples.create(order, customer);
        defaultTupleAssembler.mergeDtoIntoAggregate(recipe, tuple);

        Assertions.assertThat(recipe.getCustomerFirstName()).isEqualTo("John");
        Assertions.assertThat(recipe.getCustomerLastName()).isEqualTo("Doe");
        Assertions.assertThat(recipe.getBillingCity()).isEqualTo("bevillecity");
        Assertions.assertThat(recipe.getBillingStreet()).isEqualTo("main street");
    }

}
