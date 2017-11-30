/*
 * Copyright © 2013-2017, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.business.modelmapper.internal;

import javax.inject.Provider;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.seedstack.business.modelmapper.ModelMapperConfig;
import org.seedstack.seed.Configuration;

class ModelMapperProvider implements Provider<ModelMapper> {
    @Configuration
    private ModelMapperConfig modelMapperConfig;

    @Override
    public ModelMapper get() {
        ModelMapper modelMapper = new ModelMapper();
        org.modelmapper.config.Configuration configuration = modelMapper.getConfiguration();

        configuration.setFieldMatchingEnabled(modelMapperConfig.isFieldMatching());
        configuration.setFieldAccessLevel(modelMapperConfig.getFieldAccessLevel());

        configuration.setMethodAccessLevel(modelMapperConfig.getMethodAccessLevel());

        configuration.setAmbiguityIgnored(modelMapperConfig.isAmbiguityIgnored());
        configuration.setFullTypeMatchingRequired(modelMapperConfig.isFullTypeMatchingRequired());
        configuration.setImplicitMappingEnabled(modelMapperConfig.isImplicitMatching());

        configuration.setSourceNameTokenizer(modelMapperConfig.source().getNameTokenizer());
        configuration.setSourceNameTransformer(modelMapperConfig.source().getNameTransformer());
        configuration.setSourceNamingConvention(modelMapperConfig.source().getNamingConvention());

        configuration.setDestinationNameTokenizer(modelMapperConfig.destination().getNameTokenizer());
        configuration.setDestinationNameTransformer(modelMapperConfig.destination().getNameTransformer());
        configuration.setDestinationNamingConvention(modelMapperConfig.destination().getNamingConvention());

        if (modelMapperConfig.isIgnoreNulls()) {
            configuration.setPropertyCondition(Conditions.isNotNull());
        }

        return modelMapper;
    }
}
