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
import org.javatuples.Tuple;
import org.modelmapper.ModelMapper;
import org.seedstack.business.assembler.BaseTupleAssembler;

/**
 * This assembler automatically assembles aggregates in DTO and vice versa.
 *
 * @param <T> the tuple
 * @param <D> the dto
 */
public abstract class ModelMapperTupleAssembler<T extends Tuple, D> extends BaseTupleAssembler<T, D> {
    private final AtomicBoolean configured = new AtomicBoolean(false);
    @Inject
    private ModelMapper modelMapper;

    public ModelMapperTupleAssembler() {
        super();
    }

    protected ModelMapperTupleAssembler(Class<D> dtoClass) {
        super(dtoClass);
    }

    @Override
    public D createDtoFromAggregate(T sourceAggregate) {
        configureIfNecessary();
        D sourceDto = null;
        for (Object o : sourceAggregate) {
            if (sourceDto == null) {
                sourceDto = modelMapper.map(o, getDtoClass());
            }
            modelMapper.map(o, sourceDto);

        }
        return sourceDto;
    }

    @Override
    public void mergeAggregateIntoDto(T sourceAggregate, D targetDto) {
        configureIfNecessary();
        for (Object o : sourceAggregate) {
            modelMapper.map(o, targetDto);
        }
    }

    @Override
    public void mergeDtoIntoAggregate(D sourceDto, T targetAggregate) {
        configureIfNecessary();
        for (Object o : targetAggregate) {
            modelMapper.map(sourceDto, o);
        }
    }

    private void configureIfNecessary() {
        if (!configured.getAndSet(true)) {
            configure(modelMapper);
        }
    }

    protected abstract void configure(ModelMapper modelMapper);
}
