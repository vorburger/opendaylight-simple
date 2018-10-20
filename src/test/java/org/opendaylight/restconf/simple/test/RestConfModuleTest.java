/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.restconf.simple.test;

import static com.google.common.truth.Truth.assertThat;
import static org.opendaylight.infrautils.testutils.TestHttpClient.Method.GET;

import java.io.IOException;
import javax.inject.Inject;
import org.junit.Rule;
import org.junit.Test;
import org.opendaylight.aaa.web.WebServer;
import org.opendaylight.controller.simple.ControllerWiring;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule2;
import org.opendaylight.infrautils.simple.testutils.AbstractSimpleDistributionTest;
import org.opendaylight.infrautils.testutils.TestHttpClient;
import org.opendaylight.infrautils.web.WebWiring;
import org.opendaylight.restconf.simple.RestConfModule;

/**
 * Tests if the {@link RestConfModule} works.
 *
 * @author Michael Vorburger.ch
 */
public class RestConfModuleTest extends AbstractSimpleDistributionTest {

    public @Rule GuiceRule guice = new GuiceRule2(
            RestConfModule.class, ControllerWiring.class, WebWiring.class, AnnotationsModule.class);

    @Inject WebServer webServer;
    @Inject TestHttpClient http;

    @Test public void testRestConf() throws IOException {
        assertThat(http.responseCode(GET, "/restconf/modules/")).isEqualTo(200);

        // TODO test security; add auth support to TestHttpClient, check that w.o. auth it's 401
    }
}
