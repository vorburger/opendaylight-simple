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
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.simple.JobCoordinatorWiring;
import org.opendaylight.infrautils.simple.MetricsWiring;
import org.opendaylight.infrautils.simple.ReadyWiring;
import org.opendaylight.openflowplugin.simple.OpenFlowPluginWiring;
import org.ops4j.pax.cdi.api.OsgiService;

public class GeniusWiring extends AbstractModule {

    @Override
    protected void configure() {
        install(new AnnotationsModule());

        install(new ReadyWiring());
        // TODO install(new DiagStatusWiring());
        install(new MetricsWiring());
        install(new JobCoordinatorWiring());

        // TODO write real DaeximWiring, and replace this line with an install(new DaeximWiring());
        bind(DataImportBootReady.class).annotatedWith(OsgiService.class).toInstance(new DataImportBootReady() {});

        install(new OpenFlowPluginWiring());

        install(new ServiceRecoveryWiring());
        install(new MdsalUtilWiring());
        install(new LockManagerWiring());
        install(new IdManagerWiring());
        install(new AlivenessMonitorWiring());
        install(new InterfaceManagerWiring());
        // TODO install(new ItmWiring());
        // TODO install(new ResourceManagerWiring());
    }

}
