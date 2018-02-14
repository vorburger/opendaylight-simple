/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple.web.test;

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
import org.opendaylight.infrautils.ready.SystemReadyBaseImpl;
import org.opendaylight.infrautils.simple.web.impl.JettyLauncher;
import org.opendaylight.infrautils.testutils.Asserts;
import org.opendaylight.infrautils.web.ServletContextRegistration;
import org.opendaylight.infrautils.web.WebContext;

/**
 * Test of {@link JettyLauncher}.
 * The {@link WebWiringTest} does something similar but in a Guice environment.
 *
 * @author Michael Vorburger.ch
 */
public class JettyLauncherTest {

    // TODO factor out abstract class WebContextTest

    // TODO public static @ClassRule ClasspathHellDuplicatesCheckRule jHades = new ClasspathHellDuplicatesCheckRule();

    @Test
    @SuppressWarnings("checkstyle:IllegalThrows") // start() throws Throwable
    public void testStartWithExplicitMapping() throws Throwable {
        SystemReadyBaseImpl system = new SystemReadyBaseImpl();
        JettyLauncher jetty = new JettyLauncher(system);
        try {
            ServletContextRegistration registration = jetty.newServletContext("/test", false, servletContext -> {
                Dynamic dynServlet = servletContext.addServlet("Test", new TestServlet());
                dynServlet.addMapping("/*");
                dynServlet.setLoadOnStartup(1);
            });

            system.ready();
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
        SystemReadyBaseImpl system = new SystemReadyBaseImpl();
        JettyLauncher jetty = new JettyLauncher(system);
        try {
            jetty.newServletContext("/test1", false, servletContext -> {
                servletContext.addServlet("Test", new TestServlet()).addMapping("/*");
            });
            jetty.newServletContext("/test2", false, servletContext -> {
                servletContext.addServlet("Test", new TestServlet()).addMapping("/*");
            });

            system.ready();
            checkTestServlet("test2");
            checkTestServlet("test1");

        } finally {
            jetty.stop();
        }
    }

    @Test
    @SuppressWarnings("checkstyle:IllegalThrows") // start() throws Throwable
    public void testAddAfterStart() throws Throwable {
        SystemReadyBaseImpl system = new SystemReadyBaseImpl();
        JettyLauncher jetty = new JettyLauncher(system);
        system.ready();

        try {
            WebContext webContext = jetty.newWebContext("/test1", false);
            webContext.registerServlet("/*", "Test", new TestServlet());
            checkTestServlet("test1");

        } finally {
            jetty.stop();
        }
    }

    @Test
    public void testAddFilter() throws Exception {
        SystemReadyBaseImpl system = new SystemReadyBaseImpl();
        JettyLauncher jetty = new JettyLauncher(system);
        system.ready();

        try {
            TestFilter testFilter = new TestFilter();
            WebContext webContext = jetty.newWebContext("/testingFilters", false);
            webContext.addContextParam("testParam1", "avalue").registerFilter("/*", "Test", testFilter);
            assertThat(testFilter.isInitialized).isTrue();

        } finally {
            jetty.stop();
        }
    }

    @Test
    public void testRegisterListener() throws Exception {
        SystemReadyBaseImpl system = new SystemReadyBaseImpl();
        JettyLauncher jetty = new JettyLauncher(system);

        {
            WebContext webContext = jetty.newWebContext("/testingListenerPreBoot", false);
            TestListener testListener = new TestListener();
            webContext.registerListener(testListener);
            assertThat(testListener.isInitialized).isFalse();
            system.ready();
            assertThat(testListener.isInitialized).isTrue();
        }

        try {
            WebContext webContext = jetty.newWebContext("/testingListenerWhenRunning", false);
            TestListener testListener = new TestListener();
            webContext.registerListener(testListener);
            assertThat(testListener.isInitialized).isTrue();

        } finally {
            jetty.stop();
        }
    }

    @Test
    @Ignore // this doesn't work yet because it will read all web.xml and the one from AAA does not yet work
    @SuppressWarnings("checkstyle:IllegalThrows") // start() throws Throwable
    public void testStartWithWebXML() throws Throwable {
        SystemReadyBaseImpl system = new SystemReadyBaseImpl();
        JettyLauncher jetty = new JettyLauncher(system);
        jetty.addWebAppContexts();
        system.ready();
        try {
            checkTestServlet("test1");
        } finally {
            jetty.stop();
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
