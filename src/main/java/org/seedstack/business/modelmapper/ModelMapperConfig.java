/*
 * Copyright © 2013-2017, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.seedstack.business.modelmapper;

import org.modelmapper.config.Configuration;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.convention.NameTransformers;
import org.modelmapper.convention.NamingConventions;
import org.modelmapper.spi.NameTokenizer;
import org.modelmapper.spi.NameTransformer;
import org.modelmapper.spi.NamingConvention;
import org.seedstack.coffig.Config;

@Config("modelMapper")
public class ModelMapperConfig {
    private Configuration.AccessLevel fieldAccessLevel = Configuration.AccessLevel.PRIVATE;
    private Configuration.AccessLevel methodAccessLevel = Configuration.AccessLevel.PUBLIC;
    private boolean fieldMatching = true;
    private boolean ambiguityIgnored = false;
    private boolean fullTypeMatchingRequired = false;
    private boolean implicitMatching = true;
    private boolean ignoreNulls = true;
    private SourceConfig source = new SourceConfig();
    private DestinationConfig destination = new DestinationConfig();

    public Configuration.AccessLevel getFieldAccessLevel() {
        return fieldAccessLevel;
    }

    public ModelMapperConfig setFieldAccessLevel(Configuration.AccessLevel fieldAccessLevel) {
        this.fieldAccessLevel = fieldAccessLevel;
        return this;
    }

    public Configuration.AccessLevel getMethodAccessLevel() {
        return methodAccessLevel;
    }

    public ModelMapperConfig setMethodAccessLevel(Configuration.AccessLevel methodAccessLevel) {
        this.methodAccessLevel = methodAccessLevel;
        return this;
    }

    public boolean isFieldMatching() {
        return fieldMatching;
    }

    public ModelMapperConfig setFieldMatching(boolean fieldMatching) {
        this.fieldMatching = fieldMatching;
        return this;
    }

    public boolean isAmbiguityIgnored() {
        return ambiguityIgnored;
    }

    public ModelMapperConfig setAmbiguityIgnored(boolean ambiguityIgnored) {
        this.ambiguityIgnored = ambiguityIgnored;
        return this;
    }

    public boolean isFullTypeMatchingRequired() {
        return fullTypeMatchingRequired;
    }

    public ModelMapperConfig setFullTypeMatchingRequired(boolean fullTypeMatchingRequired) {
        this.fullTypeMatchingRequired = fullTypeMatchingRequired;
        return this;
    }

    public boolean isImplicitMatching() {
        return implicitMatching;
    }

    public ModelMapperConfig setImplicitMatching(boolean implicitMatching) {
        this.implicitMatching = implicitMatching;
        return this;
    }

    public boolean isIgnoreNulls() {
        return ignoreNulls;
    }

    public ModelMapperConfig setIgnoreNulls(boolean ignoreNulls) {
        this.ignoreNulls = ignoreNulls;
        return this;
    }

    public SourceConfig source() {
        return source;
    }

    public DestinationConfig destination() {
        return destination;
    }

    public static class SourceConfig {
        private NameTokenizer nameTokenizer = NameTokenizers.CAMEL_CASE;
        private NameTransformer nameTransformer = NameTransformers.JAVABEANS_ACCESSOR;
        private NamingConvention namingConvention = NamingConventions.JAVABEANS_ACCESSOR;

        public NameTokenizer getNameTokenizer() {
            return nameTokenizer;
        }

        public SourceConfig setNameTokenizer(NameTokenizer nameTokenizer) {
            this.nameTokenizer = nameTokenizer;
            return this;
        }

        public NameTransformer getNameTransformer() {
            return nameTransformer;
        }

        public SourceConfig setNameTransformer(NameTransformer nameTransformer) {
            this.nameTransformer = nameTransformer;
            return this;
        }

        public NamingConvention getNamingConvention() {
            return namingConvention;
        }

        public SourceConfig setNamingConvention(NamingConvention namingConvention) {
            this.namingConvention = namingConvention;
            return this;
        }
    }

    public static class DestinationConfig {
        private NameTokenizer nameTokenizer = NameTokenizers.CAMEL_CASE;
        private NameTransformer nameTransformer = NameTransformers.JAVABEANS_MUTATOR;
        private NamingConvention namingConvention = NamingConventions.JAVABEANS_MUTATOR;

        public NameTokenizer getNameTokenizer() {
            return nameTokenizer;
        }

        public DestinationConfig setNameTokenizer(NameTokenizer nameTokenizer) {
            this.nameTokenizer = nameTokenizer;
            return this;
        }

        public NameTransformer getNameTransformer() {
            return nameTransformer;
        }

        public DestinationConfig setNameTransformer(NameTransformer nameTransformer) {
            this.nameTransformer = nameTransformer;
            return this;
        }

        public NamingConvention getNamingConvention() {
            return namingConvention;
        }

        public DestinationConfig setNamingConvention(NamingConvention namingConvention) {
            this.namingConvention = namingConvention;
            return this;
        }
    }
}
