/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.genius.simple;

import com.google.inject.Provides;
import javax.inject.Singleton;
import org.opendaylight.controller.simple.ConfigReader;
import org.opendaylight.controller.simple.InMemoryControllerModule;
import org.opendaylight.daexim.DataImportBootReady;
import org.opendaylight.genius.arputil.internal.ArpUtilImpl;
import org.opendaylight.genius.ipv6util.nd.Ipv6NdUtilServiceImpl;
import org.opendaylight.infrautils.inject.guice.AutoWiringModule;
import org.opendaylight.infrautils.inject.guice.GuiceClassPathBinder;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.simple.InfraUtilsModule;
import org.opendaylight.openflowplugin.simple.OpenFlowPluginModule;
import org.opendaylight.restconf.simple.RestConfModule;
import org.opendaylight.serviceutils.simple.ServiceUtilsModule;
import org.opendaylight.yang.gen.v1.urn.opendaylight.genius.arputil.rev160406.OdlArputilService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.genius.ipv6.nd.util.rev170210.Ipv6NdUtilService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.genius.networkutils.config.rev181129.NetworkConfig;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.mdsalutil.rev170830.Config;

public class GeniusModule extends AutoWiringModule {

    public GeniusModule(GuiceClassPathBinder classPathBinder) {
        super(classPathBinder, "org.opendaylight.genius");
    }

    @Provides
    @Singleton NetworkConfig getUpgradeConfig(ConfigReader configReader) {
        return configReader.read("/initial/genius-network-config", NetworkConfig.class);
    }

    @Provides
    @Singleton Config mdsalUtilConfig(ConfigReader configReader) {
        return configReader.read("/initial/genius-mdsalutil-config", Config.class);
    }

    @Override
    protected void configureMore() {
        // Guice
        install(new AnnotationsModule());

        // Infrautils
        install(new InfraUtilsModule());

        // MD SAL
        install(new InMemoryControllerModule());

        // ServiceUtils
        install(new ServiceUtilsModule());

        // RESTCONF
        install(new RestConfModule());

        // Daexim
        // TODO write real DaeximWiring, and replace this line with an install(new DaeximWiring());
        bind(DataImportBootReady.class).toInstance(new DataImportBootReady() {});

        // OpenFlowPlugin
        install(new OpenFlowPluginModule(classPathBinder));

        // TODO remove these, by ... using ConfigReader as above
        install(new InterfaceManagerModule());
        install(new ItmModule());

        // ARP Util
        bind(OdlArputilService.class).to(ArpUtilImpl.class);

        // IPv6
        bind(Ipv6NdUtilService.class).to(Ipv6NdUtilServiceImpl.class);
    }

}
