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
import org.opendaylight.infrautils.diagstatus.DiagStatusService;
import org.opendaylight.infrautils.diagstatus.ServiceStatusProvider;
import org.opendaylight.infrautils.diagstatus.internal.DiagStatusServiceImpl;

public class DiagStatusWiring extends AbstractModule {

    // TODO get DiagStatusWiringTest to pass without the 2nd bind() below...
    // maybe using https://github.com/google/guice/wiki/Multibindings ..
    // but how to make it AUTOMATICALLY pickup all beans which ServiceStatusProvider?
    // probably possible via https://github.com/google/guice/wiki/CustomInjections ?

    @Override
    protected void configure() {
        bind(DiagStatusService.class).to(DiagStatusServiceImpl.class);
        bind(new TypeLiteral<List<ServiceStatusProvider>>() {}).toInstance(Collections.emptyList());
    }

}
