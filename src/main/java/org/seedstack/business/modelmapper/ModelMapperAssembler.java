/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.business.modelmapper;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;
import org.modelmapper.ModelMapper;
import org.seedstack.business.assembler.BaseAssembler;
import org.seedstack.business.domain.AggregateRoot;

/**
 * This assembler automatically assembles aggregates in DTO and vice versa.
 *
 * @param <A> the aggregate root
 * @param <D> the dto
 */
public abstract class ModelMapperAssembler<A extends AggregateRoot<?>, D> extends BaseAssembler<A, D> {
    private final AtomicBoolean configured = new AtomicBoolean(false);
    @Inject
    private ModelMapper modelMapper;

    public ModelMapperAssembler() {
        super();
    }

    protected ModelMapperAssembler(Class<D> dtoClass) {
        super(dtoClass);
    }

    @Override
    public D createDtoFromAggregate(A sourceAggregate) {
        configureIfNecessary();
        return modelMapper.map(sourceAggregate, getDtoClass());
    }

    @Override
    public void mergeAggregateIntoDto(A sourceAggregate, D targetDto) {
        configureIfNecessary();
        modelMapper.map(sourceAggregate, targetDto);
    }

    @Override
    public void mergeDtoIntoAggregate(D sourceDto, A targetAggregate) {
        configureIfNecessary();
        modelMapper.map(sourceDto, targetAggregate);
    }

    private void configureIfNecessary() {
        if (!configured.getAndSet(true)) {
            configure(modelMapper);
        }
    }

    protected abstract void configure(ModelMapper modelMapper);
}
