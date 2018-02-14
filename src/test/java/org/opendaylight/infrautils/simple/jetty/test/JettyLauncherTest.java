/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple.jetty.test;

import static com.google.common.truth.Truth.assertThat;
import static java.nio.charset.StandardCharsets.US_ASCII;

import com.google.common.io.CharStreams;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.ServletRegistration.Dynamic;
import org.junit.Ignore;
import org.junit.Test;
import org.opendaylight.infrautils.simple.jetty.JettyLauncher;
import org.opendaylight.infrautils.testutils.Asserts;
import org.opendaylight.infrautils.web.ServletContextRegistration;

/**
 * Test of {@link JettyLauncher}.
 * The {@link WebWiringTest} does something similar but in a Guice environment.
 *
 * @author Michael Vorburger.ch
 */
public class JettyLauncherTest {

    // TODO public static @ClassRule ClasspathHellDuplicatesCheckRule jHades = new ClasspathHellDuplicatesCheckRule();

    @Test
    @SuppressWarnings("checkstyle:IllegalThrows") // start() throws Throwable
    public void testStartWithExplicitMapping() throws Throwable {
        JettyLauncher jetty = new JettyLauncher();
        try {
            ServletContextRegistration registration = jetty.newServletContext("/test", false, servletContext -> {
                Dynamic dynServlet = servletContext.addServlet("Test", new TestServlet());
                dynServlet.addMapping("/*");
                dynServlet.setLoadOnStartup(1);
            });

            jetty.start();
            checkTestServlet("test");

            registration.unregister();
            Asserts.assertThrows(FileNotFoundException.class, () -> checkTestServlet("test"));

        } finally {
            jetty.stop();
        }
    }

    @Test
    @SuppressWarnings("checkstyle:IllegalThrows") // start() throws Throwable
    public void testTwoServletContexts() throws Throwable {
        JettyLauncher jetty = new JettyLauncher();
        try {
            jetty.newServletContext("/test1", false, servletContext -> {
                servletContext.addServlet("Test", new TestServlet()).addMapping("/*");
            });
            jetty.newServletContext("/test2", false, servletContext -> {
                servletContext.addServlet("Test", new TestServlet()).addMapping("/*");
            });

            jetty.start();
            checkTestServlet("test2");
            checkTestServlet("test1");

        } finally {
            jetty.stop();
        }
    }

    @Test
    @Ignore // this doesn't work yet because it will read all web.xml and the one from AAA does not yet work
    @SuppressWarnings("checkstyle:IllegalThrows") // start() throws Throwable
    public void testStartWithWebXML() throws Throwable {
        JettyLauncher jettyLauncher = new JettyLauncher();
        jettyLauncher.addWebAppContexts();
        jettyLauncher.start();
        try {
            checkTestServlet("test1");
        } finally {
            jettyLauncher.stop();
        }
    }

    static void checkTestServlet(String context) throws IOException {
        URL url = new URL("http://localhost:8080/" + context + "/something");
        URLConnection conn = url.openConnection();
        try (InputStream inputStream = conn.getInputStream()) {
            // The hard-coded ASCII here is strictly speaking wrong of course
            // (should interpret header from reply), but good enough for a test.
            try (InputStreamReader reader = new InputStreamReader(inputStream, US_ASCII)) {
                String result = CharStreams.toString(reader);
                assertThat(result).startsWith("hello, world");
            }
        }
    }

}
