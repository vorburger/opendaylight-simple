/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowplugin.simple;

import static com.google.common.collect.Lists.newArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.opendaylight.controller.simple.ConfigReader;
import org.opendaylight.openflowjava.protocol.spi.connection.SwitchConnectionProvider;
import org.opendaylight.openflowjava.protocol.spi.connection.SwitchConnectionProviderFactory;
import org.opendaylight.openflowjava.protocol.spi.connection.SwitchConnectionProviderList;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.openflow._switch.connection.config.rev160506.SwitchConnectionConfig;

/**
 * Wiring (non-Guice specific) for openflowjava. Based on
 * openflowplugin/openflowjava/openflowjava-blueprint-config/src/main/resources/OSGI-INF/blueprint/openflowjava.xml.
 *
 * @author Michael Vorburger.ch
 */
@Singleton
public class OpenFlowJavaWiring {

    private final SwitchConnectionProviderList switchConnectionProviderList;

    @Inject
    public OpenFlowJavaWiring(ConfigReader configReader,
            SwitchConnectionProviderFactory switchConnectionProviderFactory) {
        SwitchConnectionConfig defaultSwitchConnConfig = configReader
                .read("/initial/default-openflow-connection-config", SwitchConnectionConfig.class,
                      "openflow-switch-connection-provider-default-impl");
        SwitchConnectionProvider defaultSwitchConnProvider = switchConnectionProviderFactory
                .newInstance(defaultSwitchConnConfig);

        SwitchConnectionConfig legacySwitchConnConfig = configReader
                .read("/initial/legacy-openflow-connection-config",SwitchConnectionConfig.class,
                      "openflow-switch-connection-provider-legacy-impl");
        SwitchConnectionProvider legacySwitchConnProvider = switchConnectionProviderFactory
                .newInstance(legacySwitchConnConfig);

        switchConnectionProviderList = new SwitchConnectionProviderList(
                newArrayList(defaultSwitchConnProvider, legacySwitchConnProvider));
    }

    public SwitchConnectionProviderList getSwitchConnectionProviderList() {
        return switchConnectionProviderList;
    }
}
