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
import org.opendaylight.genius.idmanager.IdManager;
import org.opendaylight.genius.interfacemanager.InterfacemgrProvider;
import org.opendaylight.genius.interfacemanager.interfaces.IInterfaceManager;
import org.opendaylight.genius.interfacemanager.interfaces.InterfaceManagerService;
import org.opendaylight.genius.interfacemanager.listeners.CacheBridgeEntryConfigListener;
import org.opendaylight.genius.interfacemanager.listeners.CacheBridgeRefEntryListener;
import org.opendaylight.genius.interfacemanager.listeners.HwVTEPConfigListener;
import org.opendaylight.genius.interfacemanager.listeners.HwVTEPTunnelsStateListener;
import org.opendaylight.genius.interfacemanager.listeners.InterfaceConfigListener;
import org.opendaylight.genius.interfacemanager.listeners.InterfaceInventoryStateListener;
import org.opendaylight.genius.interfacemanager.listeners.InterfaceStateListener;
import org.opendaylight.genius.interfacemanager.listeners.InterfaceTopologyStateListener;
import org.opendaylight.genius.interfacemanager.listeners.TerminationPointStateListener;
import org.opendaylight.genius.interfacemanager.listeners.VlanMemberConfigListener;
import org.opendaylight.genius.interfacemanager.rpcservice.InterfaceManagerRpcService;
import org.opendaylight.genius.interfacemanager.rpcservice.InterfaceManagerServiceImpl;
import org.opendaylight.genius.interfacemanager.servicebindings.flowbased.listeners.FlowBasedServicesConfigListener;
import org.opendaylight.genius.interfacemanager.servicebindings.flowbased.listeners.FlowBasedServicesInterfaceStateListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.genius.idmanager.rev160406.IdManagerService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.genius.interfacemanager.config.rev160406.IfmConfig;
import org.opendaylight.yang.gen.v1.urn.opendaylight.genius.interfacemanager.rpcs.rev160406.OdlInterfaceRpcService;
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


        bind(IdManagerService.class).to(IdManager.class);
        bind(IInterfaceManager.class).to(InterfacemgrProvider.class);
        bind(InterfaceManagerService.class).to(InterfaceManagerServiceImpl.class);

        bind(OdlInterfaceRpcService.class).to(InterfaceManagerRpcService.class);
        bind(CacheBridgeEntryConfigListener.class);
        bind(CacheBridgeRefEntryListener.class);
        bind(FlowBasedServicesConfigListener.class);
        bind(FlowBasedServicesInterfaceStateListener.class);
        bind(HwVTEPConfigListener.class);
        bind(HwVTEPTunnelsStateListener.class);
        bind(InterfaceConfigListener.class);
        bind(InterfaceInventoryStateListener.class);
        bind(InterfaceTopologyStateListener.class);
        bind(TerminationPointStateListener.class);
        bind(VlanMemberConfigListener.class);
        bind(InterfaceStateListener.class);
    }
}
