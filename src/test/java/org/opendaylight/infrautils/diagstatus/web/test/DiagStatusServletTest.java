/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.diagstatus.web.test;

import static com.google.common.truth.Truth.assertThat;

import com.google.inject.AbstractModule;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
import org.opendaylight.infrautils.web.WebWiring;

/**
 * Test for {@link DiagStatusServlet}.
 *
 * @author Michael Vorburger.ch
 */
public class DiagStatusServletTest {

    private static final TestDiagStatusService SRVC = Partials.newPartial(TestDiagStatusService.class);

    public static class DiagStatusServletTestWiring extends AbstractModule {
        @Override
        protected void configure() {
            bind(DiagStatusService.class).toInstance(SRVC);
            bind(WebInitializer.class);
        }
    }

    public @Rule GuiceRule2 guice = new GuiceRule2(
            WebWiring.class, DiagStatusServletTestWiring.class, AnnotationsModule.class);

    @Inject WebServer webServer;

    @Test
    public void testGetWhenOk() throws IOException {
        SRVC.isOperational = true;
        assertThat(getDiagStatusResponseCode("GET")).isEqualTo(200);
    }

    @Test
    public void testHeadWhenOk() throws IOException {
        SRVC.isOperational = true;
        assertThat(getDiagStatusResponseCode("HEAD")).isEqualTo(200);
    }

    @Test
    public void testGetWhenNok() throws IOException {
        SRVC.isOperational = false;
        assertThat(getDiagStatusResponseCode("GET")).isEqualTo(503);
    }

    @Test
    public void testHeadWhenNok() throws IOException {
        SRVC.isOperational = false;
        assertThat(getDiagStatusResponseCode("HEAD")).isEqualTo(503);
    }

    private int getDiagStatusResponseCode(String httpMethod) throws IOException {
        URL url = new URL(webServer.getBaseURL() + WebInitializer.DIAGSTATUS_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(httpMethod);
        return conn.getResponseCode();
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
