/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowplugin.simple;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.AbstractModule;
import org.opendaylight.openflowplugin.api.openflow.OpenFlowPluginProviderFactory;
import org.opendaylight.openflowplugin.api.openflow.configuration.ConfigurationServiceFactory;
import org.opendaylight.openflowplugin.api.openflow.mastership.MastershipChangeServiceManager;
import org.opendaylight.openflowplugin.impl.OpenFlowPluginProviderFactoryImpl;
import org.opendaylight.openflowplugin.impl.configuration.ConfigurationServiceFactoryImpl;
import org.opendaylight.openflowplugin.impl.mastership.MastershipChangeServiceManagerImpl;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketProcessingService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.TransmitPacketInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.TransmitPacketOutput;
import org.opendaylight.yangtools.yang.common.RpcResult;

public class OpenFlowPluginWiring extends AbstractModule {

    @Override
    protected void configure() {
        bind(PacketProcessingService.class).to(NoPacketProcessingService.class);

        bind(OpenFlowPluginProviderFactory.class).to(OpenFlowPluginProviderFactoryImpl.class);
        bind(ConfigurationServiceFactory.class).to(ConfigurationServiceFactoryImpl.class);
        bind(MastershipChangeServiceManager.class).to(MastershipChangeServiceManagerImpl.class);
    }

    private static class NoPacketProcessingService implements PacketProcessingService {

        // TODO <odl:action-provider
        // interface="org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketProcessingService"/>
        // bind(PacketProcessingService.class).to(PacketProcessingServiceImpl.class);

        @Override
        public ListenableFuture<RpcResult<TransmitPacketOutput>> transmitPacket(TransmitPacketInput input) {
            throw new UnsupportedOperationException("TODO Implement me...");
        }
    }
}
