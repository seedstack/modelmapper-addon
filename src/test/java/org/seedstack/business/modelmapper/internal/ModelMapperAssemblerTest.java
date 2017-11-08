/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.business.modelmapper.internal;

import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.seedstack.business.domain.BaseAggregateRoot;
import org.seedstack.business.modelmapper.ModelMapperAssembler;
import org.seedstack.business.modelmapper.ModelMapperConfig;

public class ModelMapperAssemblerTest {
    private ModelMapperAssembler<Order, OrderDTO> modelMapperAssembler;
    private ModelMapperAssembler<Order, OrderDTO> defaultModelMappedAssembler;

    @Before
    public void before() {
        modelMapperAssembler = configureAssembler(new AutoAssembler());
        defaultModelMappedAssembler = configureAssembler(
                new DefaultModelMapperAssembler<>(new Class[]{Order.class, OrderDTO.class}));
    }

    private <T extends ModelMapperAssembler<?, ?>> T configureAssembler(T assembler) {
        ModelMapperProvider provider = new ModelMapperProvider();
        Whitebox.setInternalState(provider, "modelMapperConfig", new ModelMapperConfig());
        Whitebox.setInternalState(assembler, "modelMapper", provider.get());
        return assembler;
    }

    @Test
    public void testInheritingAssembler() {
        new InheritingAutoAssembler();
    }

    @Test
    public void testAssembleDtoFromAggregate() {
        Order order = new Order(new Customer(new Name("John", "Doe")), new Address("main street", "bevillecity"), null,
                null);

        OrderDTO orderDTO = modelMapperAssembler.createDtoFromAggregate(order);

        Assertions.assertThat(orderDTO.customerFirstName).isEqualTo("John");
        Assertions.assertThat(orderDTO.customerLastName).isEqualTo("Doe");
        Assertions.assertThat(orderDTO.billingCity).isEqualTo("bevillecity");
        Assertions.assertThat(orderDTO.billingStreet).isEqualTo("main street");

        orderDTO = defaultModelMappedAssembler.createDtoFromAggregate(order);

        Assertions.assertThat(orderDTO.customerFirstName).isEqualTo("John");
        Assertions.assertThat(orderDTO.customerLastName).isEqualTo("Doe");
    }

    @Test
    public void testAssembleDtoFromAggregateWithMapAndList() {
        List<String> features = Lists.newArrayList("woow", "such meta");
        Map<String, String> specs = new HashMap<>();
        specs.put("screen", "big but not too much");
        specs.put("price", "cheap");
        Order order = new Order(new Customer(new Name("John", "Doe")), new Address("main street", "bevillecity"),
                features, specs);

        OrderDTO orderDTO = modelMapperAssembler.createDtoFromAggregate(order);

        Assertions.assertThat(orderDTO.customerFirstName).isEqualTo("John");
        Assertions.assertThat(orderDTO.customerLastName).isEqualTo("Doe");
        Assertions.assertThat(orderDTO.billingCity).isEqualTo("bevillecity");
        Assertions.assertThat(orderDTO.billingStreet).isEqualTo("main street");

        orderDTO = defaultModelMappedAssembler.createDtoFromAggregate(order);

        Assertions.assertThat(orderDTO.customerFirstName).isEqualTo("John");
        Assertions.assertThat(orderDTO.customerLastName).isEqualTo("Doe");
    }

    @Test
    public void testUpdateDtoFromAggregate() {
        Order order = new Order(new Customer(new Name("John", "Doe")), new Address("main street", "bevillecity"), null,
                null);
        OrderDTO orderDTO = new OrderDTO("Jane", "Doe", "", "");

        modelMapperAssembler.mergeAggregateIntoDto(order, orderDTO);

        Assertions.assertThat(orderDTO.customerFirstName).isEqualTo("John");
        Assertions.assertThat(orderDTO.customerLastName).isEqualTo("Doe");
        Assertions.assertThat(orderDTO.billingCity).isEqualTo("bevillecity");
        Assertions.assertThat(orderDTO.billingStreet).isEqualTo("main street");
    }

    @Test
    public void testMergeAggregateWithDto() {
        Order order = new Order(new Customer(new Name("Jane", "Doe")), new Address(), null, null);
        order.setIgnoredProp("this should not be deleted");
        OrderDTO orderDTO = new OrderDTO("John", "Doe", "main street", "bevillecity");

        // This custom assembler test a custom mapping for the merge
        // this mapping is necessary because the name are not matching billing != billingAddress

        modelMapperAssembler.mergeDtoIntoAggregate(orderDTO, order);

        Assertions.assertThat(order.getCustomer().getName().getFirstName()).isEqualTo("John");
        Assertions.assertThat(order.getCustomer().getName().getLastName()).isEqualTo("Doe");
        Assertions.assertThat(order.getBillingAddress().getCity()).isEqualTo("bevillecity");
        Assertions.assertThat(order.getBillingAddress().getStreet()).isEqualTo("main street");
        Assertions.assertThat(order.getIgnoredProp()).isEqualTo("this should not be deleted");
    }

    static class AutoAssembler extends ModelMapperAssembler<Order, OrderDTO> {

        @Override
        protected void configure(ModelMapper modelMapper) {
            PropertyMap<OrderDTO, Order> orderMap = new PropertyMap<OrderDTO, Order>() {
                protected void configure() {
                    map().getBillingAddress().setStreet(source.billingStreet);
                    map(source.billingCity, destination.billingAddress.getCity());
                }
            };
            modelMapper.addMappings(orderMap);
        }
    }

    static abstract class AbstractAutoAssembler<T> extends ModelMapperAssembler<Order, T> {

    }

    static class InheritingAutoAssembler extends AbstractAutoAssembler<DummyDTO> {

        @Override
        protected void configure(ModelMapper modelMapper) {
        }
    }

    static class Order extends BaseAggregateRoot<String> {

        String id;
        Customer customer;
        List<String> features;
        Map<String, String> specs;
        String ignoredProp;
        Address billingAddress;

        public Order() {
        }

        public Order(Customer customer, Address billingAddress, List<String> features, Map<String, String> specs) {
            this.customer = customer;
            this.billingAddress = billingAddress;
            this.features = features;
            this.specs = specs;
        }

        public Customer getCustomer() {
            return customer;
        }

        public void setCustomer(Customer customer) {
            this.customer = customer;
        }

        public Address getBillingAddress() {
            return billingAddress;
        }

        public void setBillingAddress(Address billingAddress) {
            this.billingAddress = billingAddress;
        }

        public List<String> getFeatures() {
            return features;
        }

        public void setFeatures(List<String> features) {
            this.features = features;
        }

        public Map<String, String> getSpecs() {
            return specs;
        }

        public void setSpecs(Map<String, String> specs) {
            this.specs = specs;
        }

        public String getIgnoredProp() {
            return ignoredProp;
        }

        public void setIgnoredProp(String ignoredProp) {
            this.ignoredProp = ignoredProp;
        }

    }

    static class Customer {

        Name name;

        public Customer() {
        }

        public Customer(Name name) {

            this.name = name;
        }

        public Name getName() {
            return name;
        }

        public void setName(Name name) {
            this.name = name;
        }

    }

    static class Name {

        String firstName;
        String lastName;

        public Name() {
        }

        public Name(String firstName, String lastName) {

            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

    }

    static class Address {

        String street;
        String city;

        public Address() {
        }

        public Address(String street, String city) {

            this.street = street;
            this.city = city;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

    }

    static class DummyDTO {

    }

    static class OrderDTO {

        String customerFirstName;
        String customerLastName;
        String billingStreet;
        String billingCity;
        List<String> features;
        Map<String, String> specs;

        public OrderDTO() {
        }

        public OrderDTO(String customerFirstName, String customerLastName, String billingStreet, String billingCity) {
            this.customerFirstName = customerFirstName;
            this.customerLastName = customerLastName;
            this.billingStreet = billingStreet;
            this.billingCity = billingCity;
        }

        public String getCustomerFirstName() {
            return customerFirstName;
        }

        public void setCustomerFirstName(String customerFirstName) {
            this.customerFirstName = customerFirstName;
        }

        public String getCustomerLastName() {
            return customerLastName;
        }

        public void setCustomerLastName(String customerLastName) {
            this.customerLastName = customerLastName;
        }

        public String getBillingStreet() {
            return billingStreet;
        }

        public void setBillingStreet(String billingStreet) {
            this.billingStreet = billingStreet;
        }

        public String getBillingCity() {
            return billingCity;
        }

        public void setBillingCity(String billingCity) {
            this.billingCity = billingCity;
        }

        public List<String> getFeatures() {
            return features;
        }

        public void setFeatures(List<String> features) {
            this.features = features;
        }

        public Map<String, String> getSpecs() {
            return specs;
        }

        public void setSpecs(Map<String, String> specs) {
            this.specs = specs;
        }

    }
}
