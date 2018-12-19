/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowplugin.simple;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.Provides;
import javax.inject.Singleton;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.simple.ConfigReader;
import org.opendaylight.infrautils.inject.guice.AutoWiringModule;
import org.opendaylight.infrautils.inject.guice.GuiceClassPathBinder;
import org.opendaylight.mdsal.simple.PingPong;
import org.opendaylight.openflowjava.protocol.impl.core.SwitchConnectionProviderFactoryImpl;
import org.opendaylight.openflowjava.protocol.spi.connection.SwitchConnectionProviderFactory;
import org.opendaylight.openflowjava.protocol.spi.connection.SwitchConnectionProviderList;
import org.opendaylight.openflowplugin.api.openflow.configuration.ConfigurationService;
import org.opendaylight.openflowplugin.api.openflow.configuration.ConfigurationServiceFactory;
import org.opendaylight.openflowplugin.impl.ForwardingPingPongDataBroker;
import org.opendaylight.openflowplugin.impl.PingPongDataBroker;
import org.opendaylight.openflowplugin.impl.configuration.ConfigurationServiceFactoryImpl;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.PacketProcessingService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.TransmitPacketInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.packet.service.rev130709.TransmitPacketOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.openflow.provider.config.rev160510.OpenflowProviderConfig;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.openflowplugin.app.forwardingrules.manager.config.rev160511.ForwardingRulesManagerConfig;
import org.opendaylight.yangtools.yang.common.RpcResult;

public class OpenFlowPluginWiring extends AutoWiringModule {

    public OpenFlowPluginWiring(GuiceClassPathBinder classPathBinder) {
        super(classPathBinder, "org.opendaylight.openflowplugin");
    }

    @Override
    protected void configure() {
        super.configure(); // this does the auto-wiring

        // TODO remove NoPacketProcessingService and replace by real PacketProcessingServiceImpl
        bind(PacketProcessingService.class).to(NoPacketProcessingService.class);

        // TODO curious that this is needed despite SwitchConnectionProviderFactoryImpl being annotated?!
        bind(SwitchConnectionProviderFactory.class).to(SwitchConnectionProviderFactoryImpl.class);
    }

    @Provides
    @Singleton PingPongDataBroker getPingPongDataBroker(@PingPong DataBroker pingPongDataBroker) {
        return new ForwardingPingPongDataBroker(pingPongDataBroker);
    }

    @Provides
    @Singleton ConfigurationService getConfigurationService(OpenflowProviderConfig providerConfig) {
        ConfigurationServiceFactory csf = new ConfigurationServiceFactoryImpl();
        return csf.newInstance(providerConfig);
    }

    @Provides
    @Singleton SwitchConnectionProviderList getOpenFlowJavaWiring(OpenFlowJavaWiring openFlowJavaWiring) {
        return openFlowJavaWiring.getSwitchConnectionProviderList();
    }

    @Provides
    @Singleton OpenflowProviderConfig getUpgradeConfig(ConfigReader configReader) {
        return configReader.read("/initial/openflow-provider-config", OpenflowProviderConfig.class);
    }

    @Provides
    @Singleton ForwardingRulesManagerConfig getForwardingRulesManagerConfig(ConfigReader configReader) {
        return configReader.read("/initial/openflow-frm-config", ForwardingRulesManagerConfig.class);
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
