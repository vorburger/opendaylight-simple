/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple.test;

import javax.inject.Inject;
import org.junit.Rule;
import org.junit.Test;
import org.opendaylight.infrautils.diagstatus.DiagStatusService;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule;
import org.opendaylight.infrautils.simple.DiagStatusWiring;

/**
 * Unit test for {@link DiagStatusWiring}.
 *
 * @author Michael Vorburger.ch
 */
public class DiagStatusWiringTest {

    public @Rule GuiceRule guice = new GuiceRule(DiagStatusWiring.class, AnnotationsModule.class);

    @Inject DiagStatusService diagStatusService;

    @Test public void testDiagStatusService() { }

    // TODO separate DiagStatusWiring0Test VS DiagStatusWiring1Test where *1* registers a
    // Module that actually does bind a ServiceStatusProvider ...

}
