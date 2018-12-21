/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.genius.simple;

import com.google.inject.AbstractModule;
import org.opendaylight.controller.simple.InMemoryControllerModule;
import org.opendaylight.daexim.DataImportBootReady;
import org.opendaylight.infrautils.inject.guice.GuiceClassPathBinder;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.simple.InfraUtilsModule;
import org.opendaylight.openflowplugin.simple.OpenFlowPluginModule;
import org.opendaylight.restconf.simple.RestConfModule;
import org.opendaylight.serviceutils.simple.ServiceUtilsModule;

public class GeniusModule extends AbstractModule {

    private final GuiceClassPathBinder classPathBinder;

    public GeniusModule(GuiceClassPathBinder classPathBinder) {
        this.classPathBinder = classPathBinder;
    }

    @Override
    protected void configure() {
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

        // Genius
        install(new MdsalUtilModule());
        install(new LockManagerModule());
        install(new IdManagerModule());
        install(new AlivenessMonitorModule(classPathBinder));
        install(new InterfaceManagerModule());
        install(new ItmModule());
        install(new DatastoreUtilsModule());
        // TODO install(new ResourceManagerWiring());
    }

}
