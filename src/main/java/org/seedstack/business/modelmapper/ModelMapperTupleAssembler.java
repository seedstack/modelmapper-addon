/*
 * Copyright © 2013-2017, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.business.modelmapper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.inject.Inject;
import javax.inject.Provider;
import org.javatuples.Tuple;
import org.modelmapper.ModelMapper;
import org.seedstack.business.assembler.BaseTupleAssembler;

/**
 * This class can be extended to declare an assembler that is able to automatically map a tuple of aggregates to a DTO
 * and back. It delegates the mapping to a {@link ModelMapper} instance.
 *
 * @param <T> the type of the tuple.
 * @param <D> the type of the DTO.
 */
public abstract class ModelMapperTupleAssembler<T extends Tuple, D> extends BaseTupleAssembler<T, D> {
    private static final ConcurrentMap<Class<?>, ModelMapper> modelMappers = new ConcurrentHashMap<>();
    @Inject
    private Provider<ModelMapper> modelMapperProvider;
    private volatile ModelMapper cachedModelMapper;

    public ModelMapperTupleAssembler() {
        super();
    }

    protected ModelMapperTupleAssembler(Class<D> dtoClass) {
        super(dtoClass);
    }

    @Override
    public D createDtoFromAggregate(T sourceAggregate) {
        D sourceDto = null;
        ModelMapper modelMapper = getModelMapper();
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
        ModelMapper modelMapper = getModelMapper();
        for (Object o : sourceAggregate) {
            modelMapper.map(o, targetDto);
        }
    }

    @Override
    public void mergeDtoIntoAggregate(D sourceDto, T targetAggregate) {
        ModelMapper modelMapper = getModelMapper();
        for (Object o : targetAggregate) {
            modelMapper.map(sourceDto, o);
        }
    }

    private ModelMapper getModelMapper() {
        if (cachedModelMapper == null) {
            synchronized (this) {
                if (cachedModelMapper == null) {
                    cachedModelMapper = modelMappers.computeIfAbsent(
                            getClass(),
                            key -> {
                                ModelMapper modelMapper = modelMapperProvider.get();
                                configure(modelMapper);
                                return modelMapper;
                            });
                }
            }
        }
        return cachedModelMapper;
    }

    /**
     * This method is called once when the first mapping of the assembler occurs. It allows to configure the
     * {@link ModelMapper} instance that will be used for mapping. This instance will then be reused for subsequent
     * calls.
     *
     * @param modelMapper the {@link ModelMapper} instance to configure.
     */
    protected abstract void configure(ModelMapper modelMapper);
}
