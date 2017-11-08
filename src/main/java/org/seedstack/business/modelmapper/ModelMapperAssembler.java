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
import org.modelmapper.ModelMapper;
import org.seedstack.business.assembler.BaseAssembler;
import org.seedstack.business.domain.AggregateRoot;

/**
 * This class can be extended to declare an assembler that is able to automatically map an aggregate to a DTO and back.
 * It delegates the mapping to a {@link ModelMapper} instance.
 *
 * @param <A> the type of the aggregate root.
 * @param <D> the type of the DTO.
 */
public abstract class ModelMapperAssembler<A extends AggregateRoot<?>, D> extends BaseAssembler<A, D> {
    private static final ConcurrentMap<Class<?>, ModelMapper> modelMappers = new ConcurrentHashMap<>();
    @Inject
    private Provider<ModelMapper> modelMapperProvider;
    private volatile ModelMapper cachedModelMapper;

    public ModelMapperAssembler() {
        super();
    }

    protected ModelMapperAssembler(Class<D> dtoClass) {
        super(dtoClass);
    }

    @Override
    public D createDtoFromAggregate(A sourceAggregate) {
        return getModelMapper().map(sourceAggregate, getDtoClass());
    }

    @Override
    public void mergeAggregateIntoDto(A sourceAggregate, D targetDto) {
        getModelMapper().map(sourceAggregate, targetDto);
    }

    @Override
    public void mergeDtoIntoAggregate(D sourceDto, A targetAggregate) {
        getModelMapper().map(sourceDto, targetAggregate);
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
