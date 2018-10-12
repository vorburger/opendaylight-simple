/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.simple.test;

import static com.google.common.truth.Truth.assertThat;
import static org.opendaylight.infrautils.testutils.TestHttpClient.Method.GET;

import java.io.IOException;
import javax.inject.Inject;
import org.junit.Rule;
import org.junit.Test;
import org.opendaylight.aaa.web.WebServer;
import org.opendaylight.controller.simple.ControllerWiring;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule2;
import org.opendaylight.infrautils.simple.testutils.AbstractSimpleDistributionTest;
import org.opendaylight.infrautils.testutils.TestHttpClient;
import org.opendaylight.infrautils.web.WebWiring;
import org.opendaylight.neutron.simple.NeutronModule;

/**
 * Neutron component test.
 *
 * @author Michael Vorburger.ch
 */
public class NeutronModuleTest extends AbstractSimpleDistributionTest {

    public @Rule GuiceRule2 guice = new GuiceRule2(
            NeutronModule.class, ControllerWiring.class, WebWiring.class, AnnotationsModule.class);

    @Inject WebServer webServer;
    @Inject TestHttpClient http;

    @Test public void testNeutron() throws IOException {
        assertThat(http.responseCode(GET, "/controller/nb/v2/neutron/networks")).isEqualTo(200);
    }
}
