/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.genius.simple;

import com.google.inject.AbstractModule;
import org.opendaylight.daexim.DataImportBootReady;
import org.opendaylight.infrautils.inject.ClassPathScanner;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.simple.InfraUtilsWiring;
import org.opendaylight.mdsal.simple.MdsalWiring;
import org.opendaylight.openflowplugin.simple.OpenFlowPluginWiring;
import org.opendaylight.serviceutils.simple.ServiceUtilsWiring;
import org.ops4j.pax.cdi.api.OsgiService;

public class GeniusWiring extends AbstractModule {

    private final ClassPathScanner scanner;

    public GeniusWiring(ClassPathScanner scanner) {
        this.scanner = scanner;
    }

    @Override
    protected void configure() {
        // Guice
        install(new AnnotationsModule());

        // Infrautils
        install(new InfraUtilsWiring());

        // MD SAL
        install(new MdsalWiring());

        // Daexim
        // TODO write real DaeximWiring, and replace this line with an install(new DaeximWiring());
        bind(DataImportBootReady.class).annotatedWith(OsgiService.class).toInstance(new DataImportBootReady() {});

        // OpenFlowPlugin
        install(new OpenFlowPluginWiring());

        // ServiceUtils
        install(new ServiceUtilsWiring());

        // Genius
        install(new MdsalUtilWiring());
        install(new LockManagerWiring());
        install(new IdManagerWiring());
        install(new AlivenessMonitorWiring(scanner));
        install(new InterfaceManagerWiring());
        install(new ItmWiring());
        install(new DatastoreUtilsWiring());
        // TODO install(new ResourceManagerWiring());
    }

}
