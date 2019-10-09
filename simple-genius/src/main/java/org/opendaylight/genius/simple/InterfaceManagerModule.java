/*
 * Copyright (c) 2016, 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.genius.simple;

import com.google.inject.AbstractModule;
import javax.annotation.Nullable;
import org.opendaylight.yang.gen.v1.urn.opendaylight.genius.interfacemanager.config.rev160406.IfmConfig;
import org.opendaylight.yangtools.yang.binding.Augmentation;
import org.opendaylight.yangtools.yang.binding.DataContainer;

// TODO unify with org.opendaylight.genius.interfacemanager.test.InterfaceManagerTestModule
public class InterfaceManagerModule extends AbstractModule {

    @Override
    protected void configure() {
        // TODO read from real XML YANG configuration, initial XML and datastore; like BP does
        bind(IfmConfig.class).toInstance(new IfmConfig() {

            @Override
            @Nullable
            public <E extends Augmentation<IfmConfig>> E augmentation(Class<E> augmentationType) {
                return null;
            }

            @Override
            @Nullable
            public Class<? extends DataContainer> getImplementedInterface() {
                return null;
            }

            // as in default genius-ifm-config.xml

            @Override
            public Boolean isItmDirectTunnels() {
                return false;
            }

            @Override
            public Boolean isIfmStatsPollEnabled() {
                return false;
            }

            @Override
            public Integer getIfmStatsDefPollInterval() {
                return 15;
            }
        });
    }
}
