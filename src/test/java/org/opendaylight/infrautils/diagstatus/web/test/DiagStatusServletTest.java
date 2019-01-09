/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.diagstatus.web.test;

import static com.google.common.truth.Truth.assertThat;
import static org.opendaylight.infrautils.diagstatus.web.WebInitializer.DIAGSTATUS_URL;
import static org.opendaylight.infrautils.testutils.web.TestWebClient.Method.GET;
import static org.opendaylight.infrautils.testutils.web.TestWebClient.Method.HEAD;

import com.google.inject.AbstractModule;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import javax.inject.Inject;
import org.junit.Rule;
import org.junit.Test;
import org.opendaylight.aaa.web.WebServer;
import org.opendaylight.infrautils.diagstatus.DiagStatusService;
import org.opendaylight.infrautils.diagstatus.ServiceDescriptor;
import org.opendaylight.infrautils.diagstatus.web.DiagStatusServlet;
import org.opendaylight.infrautils.diagstatus.web.WebInitializer;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule2;
import org.opendaylight.infrautils.testutils.Partials;
import org.opendaylight.infrautils.testutils.TestHttpClient;
import org.opendaylight.infrautils.web.WebModule;

/**
 * Test for {@link DiagStatusServlet}.
 *
 * @author Michael Vorburger.ch
 */
public class DiagStatusServletTest {

    private static final TestDiagStatusService SRVC = Partials.newPartial(TestDiagStatusService.class);

    public static class DiagStatusServletTestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(DiagStatusService.class).toInstance(SRVC);
            bind(WebInitializer.class);
        }
    }

    public @Rule GuiceRule2 guice = new GuiceRule2(
            WebModule.class, DiagStatusServletTestModule.class, AnnotationsModule.class);

    @Inject WebServer webServer;
    @Inject TestHttpClient http;

    @Test
    public void testGetWhenOk() throws IOException {
        SRVC.isOperational = true;
        assertThat(http.responseCode(GET, DIAGSTATUS_URL)).isEqualTo(200);
    }

    @Test
    public void testHeadWhenOk() throws IOException {
        SRVC.isOperational = true;
        assertThat(http.responseCode(HEAD, DIAGSTATUS_URL)).isEqualTo(200);
    }

    @Test
    public void testGetWhenNok() throws IOException {
        SRVC.isOperational = false;
        assertThat(http.responseCode(GET, DIAGSTATUS_URL)).isEqualTo(503);
    }

    @Test
    public void testHeadWhenNok() throws IOException {
        SRVC.isOperational = false;
        assertThat(http.responseCode(HEAD, DIAGSTATUS_URL)).isEqualTo(503);
    }

    private abstract static class TestDiagStatusService implements DiagStatusService {

        Boolean isOperational;

        @Override
        public Collection<ServiceDescriptor> getAllServiceDescriptors() {
            return Collections.emptyList();
        }

        @Override
        public String getAllServiceDescriptorsAsJSON() {
            return "{}";
        }

        @Override
        public boolean isOperational() {
            return this.isOperational;
        }
    }
}
