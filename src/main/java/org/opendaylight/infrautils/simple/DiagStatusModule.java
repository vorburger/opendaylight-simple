/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import java.util.Collections;
import java.util.List;
import org.opendaylight.infrautils.diagstatus.ClusterMemberInfo;
import org.opendaylight.infrautils.diagstatus.DiagStatusService;
import org.opendaylight.infrautils.diagstatus.DiagStatusServiceMBean;
import org.opendaylight.infrautils.diagstatus.ServiceStatusProvider;
import org.opendaylight.infrautils.diagstatus.internal.DiagStatusServiceImpl;
import org.opendaylight.infrautils.diagstatus.internal.DiagStatusServiceMBeanImpl;
import org.opendaylight.infrautils.diagstatus.spi.NoClusterMemberInfo;
import org.opendaylight.infrautils.diagstatus.web.WebInitializer;

public class DiagStatusModule extends AbstractModule {

    // TODO get DiagStatusModuleTest to pass without the 2nd bind() below...
    // maybe using https://github.com/google/guice/wiki/Multibindings ..
    // but how to make it AUTOMATICALLY pickup all beans which ServiceStatusProvider?
    // probably possible via https://github.com/google/guice/wiki/CustomInjections ?

    @Override
    protected void configure() {
        bind(DiagStatusService.class).to(DiagStatusServiceImpl.class);
        bind(new TypeLiteral<List<ServiceStatusProvider>>() {}).toInstance(Collections.emptyList());
        bind(WebInitializer.class);
        // TODO when using CDS: bind(ClusterMemberInfo.class).to(ClusterMemberInfoImpl.class);
        bind(ClusterMemberInfo.class).to(NoClusterMemberInfo.class);
        bind(DiagStatusServiceMBean.class).to(DiagStatusServiceMBeanImpl.class);
        // TODO figure out how to use Guice multibindings to inject all real ServiceStatusProvider impls
        bind(new TypeLiteral<List<ServiceStatusProvider>>() {}).toInstance(Collections.emptyList());

        // Commands
        // Multibinder<Action> actionsBinder = Multibinder.newSetBinder(binder(), Action.class);
        // TODO DiagStatusCommand must be made to (again) implement
        // org.apache.karaf.shell.api.action.Action, ideally via org.opendaylight.infrautils.shell.LoggingAction
        // instead of the deprecated legacy Karaf API org.apache.karaf.shell.commands.Action, which
        // we do not want to (can't) anymore depend in opendaylight-simple.
        // This was changed in https://github.com/opendaylight/infrautils/commit/c88543dd8823585bbfd1d3cc72c56b922896df9c
        // actionsBinder.addBinding().to(DiagStatusCommand.class);
    }
}
