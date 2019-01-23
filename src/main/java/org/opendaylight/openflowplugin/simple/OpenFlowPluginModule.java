/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowplugin.simple;

import com.google.inject.Provides;
import javax.inject.Singleton;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.simple.ConfigReader;
import org.opendaylight.infrautils.inject.guice.AutoWiringModule;
import org.opendaylight.infrautils.inject.guice.GuiceClassPathBinder;
import org.opendaylight.mdsal.binding.api.RpcConsumerRegistry;
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
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.openflow.provider.config.rev160510.OpenflowProviderConfig;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.openflowplugin.app.forwardingrules.manager.config.rev160511.ForwardingRulesManagerConfig;

public class OpenFlowPluginModule extends AutoWiringModule {

    public OpenFlowPluginModule(GuiceClassPathBinder classPathBinder) {
        super(classPathBinder, "org.opendaylight.openflowplugin");
    }

    @Override
    protected void configureMore() {
        // TODO curious that this is needed despite SwitchConnectionProviderFactoryImpl being annotated?!
        bind(SwitchConnectionProviderFactory.class).to(SwitchConnectionProviderFactoryImpl.class);
    }

    @Provides
    @Singleton PacketProcessingService getPacketProcessingService(RpcConsumerRegistry rpcConsumerRegistry) {
        return rpcConsumerRegistry.getRpcService(PacketProcessingService.class);
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
}
