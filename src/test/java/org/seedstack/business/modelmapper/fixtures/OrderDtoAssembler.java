/*
 * Copyright © 2013-2017, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.business.modelmapper.fixtures;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;
import org.seedstack.business.assembler.BaseAssembler;
import org.seedstack.seed.Logging;
import org.slf4j.Logger;

public class OrderDtoAssembler extends BaseAssembler<Order, OrderDto> {
    @Inject
    private MyService myService;
    @Logging
    private Logger logger;

    @Override
    public void mergeAggregateIntoDto(Order sourceAggregate, OrderDto targetDto) {
        assertThat(myService).isNotNull();
        assertThat(logger).isNotNull();
        targetDto.setId(sourceAggregate.getId());
        targetDto.setBillingAddress(sourceAggregate.getBillingAddress());
    }

    @Override
    public void mergeDtoIntoAggregate(OrderDto sourceDto, Order targetAggregate) {
        assertThat(myService).isNotNull();
        assertThat(logger).isNotNull();
        targetAggregate.setBillingAddress(sourceDto.getBillingAddress());
    }
}
