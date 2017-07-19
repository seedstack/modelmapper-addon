/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.modelmapper;

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
    private ModelMapper assembleModelMapper;
    private ModelMapper mergeModelMapper;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public ModelMapperTupleAssembler() {
        initModelMappers();
    }

    public ModelMapperTupleAssembler(Class<D> dtoClass) {
        super(dtoClass);
        initModelMappers();
    }

    @Override
    public D createDtoFromAggregate(T sourceAggregate) {
        D sourceDto = null;

        for (Object o : sourceAggregate) {
            if (sourceDto == null) {
                sourceDto = assembleModelMapper.map(o, getDtoClass());
            }
            assembleModelMapper.map(o, sourceDto);

        }
        return sourceDto;
    }

    @Override
    public void mergeAggregateIntoDto(T sourceAggregate, D targetDto) {
        for (Object o : sourceAggregate) {
            assembleModelMapper.map(o, targetDto);
        }
    }

    @Override
    public void mergeDtoIntoAggregate(D sourceDto, T targetAggregate) {
        for (Object o : targetAggregate) {
            mergeModelMapper.map(sourceDto, o);
        }
    }

    private void initModelMappers() {
        this.assembleModelMapper = new ModelMapper();
        configureAssembly(assembleModelMapper);

        this.mergeModelMapper = new ModelMapper();
        configureMerge(mergeModelMapper);
    }

    protected abstract void configureAssembly(ModelMapper modelMapper);

    protected abstract void configureMerge(ModelMapper modelMapper);
}
