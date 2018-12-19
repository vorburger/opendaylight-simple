/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowplugin.simple.test;

import static com.google.common.truth.Truth.assertThat;

import javax.inject.Inject;
import org.junit.Rule;
import org.junit.Test;
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
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.openflow.provider.config.rev160510.OpenflowProviderConfig;

public class OpenFlowPluginWiringTest extends AbstractSimpleDistributionTest {

    private static final GuiceClassPathBinder CLASS_PATH_BINDER = new GuiceClassPathBinder("org.opendaylight");

    public @Rule GuiceRule guice = new GuiceRule(new OpenFlowPluginWiring(CLASS_PATH_BINDER), new ServiceUtilsWiring(),
            new ControllerWiring(), new DiagStatusWiring(), new WebWiring(), new ReadyWiring(),
            new AnnotationsModule());

    @Inject OpenflowProviderConfig ofpConfig;

    @Test public void testConfig() {
        assertThat(ofpConfig.getGlobalNotificationQuota()).named("globalNotificationQuota").isEqualTo(64000L);
    }

}
