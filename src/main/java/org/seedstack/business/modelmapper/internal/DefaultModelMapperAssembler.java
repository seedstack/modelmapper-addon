/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.modelmapper.internal;

import com.google.inject.assistedinject.Assisted;
import org.modelmapper.ModelMapper;
import org.seedstack.business.domain.AggregateRoot;
import org.seedstack.business.modelmapper.ModelMapperAssembler;
import org.seedstack.business.spi.GenericImplementation;

import javax.inject.Inject;

/**
 * This class is a default assembler based on ModelMapper.
 * <p>
 * If an injection point {@code ModelMapperAssembler&lt;A, D&gt;} is defined and any class extending {@code ModelMapperAssembler}
 * for A and D exists, this default assembler will be injected.
 * </p>
 */
@GenericImplementation
@org.seedstack.business.modelmapper.ModelMapper
public class DefaultModelMapperAssembler<A extends AggregateRoot<?>, D> extends ModelMapperAssembler<A, D> {

    @SuppressWarnings("unchecked")
    @Inject
    public DefaultModelMapperAssembler(@Assisted Object[] genericClasses) {
        super((Class) genericClasses[1]);
    }

    @Override
    protected void configureAssembly(ModelMapper modelMapper) {
    }

    @Override
    protected void configureMerge(ModelMapper modelMapper) {
    }
}
