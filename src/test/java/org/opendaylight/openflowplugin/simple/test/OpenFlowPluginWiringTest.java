/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowplugin.simple.test;

import org.junit.Rule;
import org.opendaylight.controller.simple.ControllerWiring;
import org.opendaylight.infrautils.inject.guice.GuiceClassPathBinder;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule;
import org.opendaylight.infrautils.simple.DiagStatusWiring;
import org.opendaylight.infrautils.simple.ReadyWiring;
import org.opendaylight.infrautils.simple.testutils.AbstractSimpleDistributionTest;
import org.opendaylight.infrautils.web.WebWiring;
import org.opendaylight.openflowplugin.simple.OpenFlowPluginWiring;
import org.opendaylight.serviceutils.simple.ServiceUtilsWiring;

public class OpenFlowPluginWiringTest extends AbstractSimpleDistributionTest {

    private static final GuiceClassPathBinder CLASS_PATH_BINDER = new GuiceClassPathBinder("org.opendaylight");

    public @Rule GuiceRule guice = new GuiceRule(new OpenFlowPluginWiring(CLASS_PATH_BINDER), new ServiceUtilsWiring(),
            new ControllerWiring(), new DiagStatusWiring(), new WebWiring(), new ReadyWiring(),
            new AnnotationsModule());

}
