/*
 * Copyright © 2013-2017, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.business.modelmapper.internal;

import com.google.inject.assistedinject.Assisted;
import javax.inject.Inject;
import org.javatuples.Tuple;
import org.modelmapper.ModelMapper;
import org.seedstack.business.modelmapper.ModelMapperTupleAssembler;
import org.seedstack.business.spi.GenericImplementation;

/**
 * This class is a default  tuple assembler based on ModelMapper.
 * <p>
 * This is the same as {@link DefaultModelMapperAssembler} but it supports tuple of aggregates.
 * </p>
 *
 * @see DefaultModelMapperAssembler
 */
@GenericImplementation
@org.seedstack.business.modelmapper.ModelMapper
public class DefaultModelMapperTupleAssembler<T extends Tuple, D> extends ModelMapperTupleAssembler<T, D> {

    @SuppressWarnings("unchecked")
    @Inject
    public DefaultModelMapperTupleAssembler(@Assisted Object[] genericClasses) {
        super((Class) genericClasses[1]);
    }

    @Override
    protected void configure(ModelMapper modelMapper) {
        // no further configuration needed
    }
}
