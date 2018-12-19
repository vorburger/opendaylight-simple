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
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.openflow.provider.config.rev160510.NonZeroUint16Type;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.openflow.provider.config.rev160510.NonZeroUint32Type;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.openflow.provider.config.rev160510.OpenflowProviderConfig;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.openflow.provider.config.rev160510.OpenflowProviderConfigBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.openflowplugin.app.forwardingrules.manager.config.rev160511.ForwardingRulesManagerConfig;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.openflowplugin.app.forwardingrules.manager.config.rev160511.ForwardingRulesManagerConfigBuilder;
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

        // Configurations:
        // TODO ConfigReader for ForwardingRulesManagerConfig XML - but there is none?  Hard-code defaults, for now:
        // perhaps we can just replace it with an empty XML??
        bind(ForwardingRulesManagerConfig.class).toInstance(
                new ForwardingRulesManagerConfigBuilder().setDisableReconciliation(false).setStaleMarkingEnabled(false)
                        .setReconciliationRetryCount(5).setBundleBasedReconciliationEnabled(false).build());
        // TODO ConfigReader for OpenflowProviderConfig XML - but there is none in OFP?  Hard-code, for now:
        // perhaps we can just replace it with an empty XML??
        bind(OpenflowProviderConfig.class).toInstance(new OpenflowProviderConfigBuilder()
                .setRpcRequestsQuota(new NonZeroUint16Type(20000))
                .setSwitchFeaturesMandatory(false)
                .setGlobalNotificationQuota(64000L)
                .setIsStatisticsPollingOn(true)
                .setIsTableStatisticsPollingOn(true)
                .setIsFlowStatisticsPollingOn(true)
                .setIsGroupStatisticsPollingOn(true)
                .setIsMeterStatisticsPollingOn(true)
                .setIsPortStatisticsPollingOn(true)
                .setIsQueueStatisticsPollingOn(true)
                .setIsStatisticsRpcEnabled(false)
                .setBarrierIntervalTimeoutLimit(new NonZeroUint32Type(500L))
                .setBarrierCountLimit(new NonZeroUint16Type(25600))
                .setEchoReplyTimeout(new NonZeroUint32Type(2000L))
                .setThreadPoolMinThreads(1)
                .setThreadPoolMaxThreads(new NonZeroUint16Type(32000))
                .setThreadPoolTimeout(60L)
                .setEnableFlowRemovedNotification(true)
                .setSkipTableFeatures(true)
                .setBasicTimerDelay(new NonZeroUint32Type(3000L))
                .setMaximumTimerDelay(new NonZeroUint32Type(900000L))
                .setUseSingleLayerSerialization(true)
                .setEnableEqualRole(false)
                .setDeviceConnectionRateLimitPerMin(0)
                .build());
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
