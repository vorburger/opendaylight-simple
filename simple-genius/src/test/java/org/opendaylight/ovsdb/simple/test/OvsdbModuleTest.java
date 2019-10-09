/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ovsdb.simple.test;

import javax.inject.Inject;
import org.junit.Rule;
import org.opendaylight.aaa.simple.CertModule;
import org.opendaylight.controller.simple.InMemoryControllerModule;
import org.opendaylight.infrautils.inject.guice.GuiceClassPathBinder;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule;
import org.opendaylight.infrautils.ready.guice.ReadyModule;
import org.opendaylight.infrautils.simple.DiagStatusModule;
import org.opendaylight.infrautils.simple.testutils.AbstractSimpleDistributionTest;
import org.opendaylight.infrautils.web.WebModule;
import org.opendaylight.ovsdb.hwvtepsouthbound.HwvtepSouthboundProvider;
import org.opendaylight.ovsdb.lib.OvsdbConnection;
import org.opendaylight.ovsdb.lib.impl.OvsdbConnectionService;
import org.opendaylight.ovsdb.simple.OvsdbModule;
import org.opendaylight.ovsdb.southbound.SouthboundProvider;

public class OvsdbModuleTest extends AbstractSimpleDistributionTest {

    private static final GuiceClassPathBinder CLASS_PATH_BINDER = new GuiceClassPathBinder("org.opendaylight");

    public @Rule GuiceRule guice = new GuiceRule(new OvsdbModule(CLASS_PATH_BINDER), new CertModule(),
            new InMemoryControllerModule(), new DiagStatusModule(), new WebModule(), new ReadyModule(),
            new AnnotationsModule());

    @Inject OvsdbConnection ovsdbConnection;
    @Inject SouthboundProvider southboundProvider;
    @Inject OvsdbConnectionService ovsdbConnectionService;
    @Inject HwvtepSouthboundProvider hwvtepSouthboundProvider;

}
