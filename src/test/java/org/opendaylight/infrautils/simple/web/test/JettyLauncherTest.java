/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple.web.test;

import java.io.FileNotFoundException;
import javax.servlet.ServletRegistration.Dynamic;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.opendaylight.infrautils.ready.SystemReadyBaseImpl;
import org.opendaylight.infrautils.simple.web.impl.JettyLauncher;
import org.opendaylight.infrautils.testutils.Asserts;
import org.opendaylight.infrautils.web.ServletContextRegistration;
import org.opendaylight.infrautils.web.WebContextProvider;

/**
 * Test of {@link JettyLauncher} implementation.
 * The {@link WebWiringTest} does something similar but in a Guice environment.
 *
 * @author Michael Vorburger.ch
 */
public class JettyLauncherTest extends WebContextTest {

    // TODO public static @ClassRule ClasspathHellDuplicatesCheckRule jHades = new ClasspathHellDuplicatesCheckRule();

    private final SystemReadyBaseImpl system;
    private final JettyLauncher jetty;

    public JettyLauncherTest() {
        this.system = new SystemReadyBaseImpl();
        this.jetty = new JettyLauncher(system);
    }

    @After
    public void tearDown() throws Exception {
        jetty.stop();
    }

    @Override
    protected void startWebServer() {
        this.system.ready();
    }

    @Override
    protected WebContextProvider getWebContextProvider() {
        return jetty;
    }

    @Test
    @SuppressWarnings("checkstyle:IllegalThrows") // start() throws Throwable
    public void testStartWithExplicitMapping() throws Throwable {
        ServletContextRegistration registration = jetty.newServletContext("/test", false, servletContext -> {
            Dynamic dynServlet = servletContext.addServlet("Test", new TestServlet());
            dynServlet.addMapping("/*");
            dynServlet.setLoadOnStartup(1);
        });

        system.ready();
        WebContextTest.checkTestServlet("test");

        registration.unregister();
        Asserts.assertThrows(FileNotFoundException.class, () -> WebContextTest.checkTestServlet("test"));
    }

    @Test
    @SuppressWarnings("checkstyle:IllegalThrows") // start() throws Throwable
    public void testTwoServletContexts() throws Throwable {
        jetty.newServletContext("/test1", false, servletContext -> {
            servletContext.addServlet("Test", new TestServlet()).addMapping("/*");
        });
        jetty.newServletContext("/test2", false, servletContext -> {
            servletContext.addServlet("Test", new TestServlet()).addMapping("/*");
        });

        system.ready();
        WebContextTest.checkTestServlet("test2");
        WebContextTest.checkTestServlet("test1");
    }

    @Test
    @Ignore // this doesn't work yet because it will read all web.xml and the one from AAA does not yet work
    @SuppressWarnings("checkstyle:IllegalThrows") // start() throws Throwable
    public void testStartWithWebXML() throws Throwable {
        jetty.addWebAppContexts();
        system.ready();
        try {
            WebContextTest.checkTestServlet("test1");
        } finally {
            jetty.stop();
        }
    }

}
