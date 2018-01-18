/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowplugin.simple;

import com.google.inject.AbstractModule;
import org.opendaylight.openflowplugin.api.openflow.OpenFlowPluginProviderFactory;
import org.opendaylight.openflowplugin.api.openflow.configuration.ConfigurationServiceFactory;
import org.opendaylight.openflowplugin.api.openflow.mastership.MastershipChangeServiceManager;
import org.opendaylight.openflowplugin.impl.OpenFlowPluginProviderFactoryImpl;
import org.opendaylight.openflowplugin.impl.configuration.ConfigurationServiceFactoryImpl;
import org.opendaylight.openflowplugin.impl.mastership.MastershipChangeServiceManagerImpl;
import org.opendaylight.openflowplugin.impl.services.sal.PacketProcessingServiceImpl;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketProcessingService;

public class OpenFlowPluginWiring extends AbstractModule {

    @Override
    protected void configure() {
        // TODO <odl:action-provider interface="org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketProcessingService"/>
        bind(PacketProcessingService.class).to(PacketProcessingServiceImpl.class);

        // TODO need to use newInstance? But then why does class have public no arg constructor..
        bind(OpenFlowPluginProviderFactory.class).to(OpenFlowPluginProviderFactoryImpl.class);
        bind(ConfigurationServiceFactory.class).to(ConfigurationServiceFactoryImpl.class);
        bind(MastershipChangeServiceManager.class).to(MastershipChangeServiceManagerImpl.class);
    }

}
