/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple.web.test;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import java.io.IOException;
import javax.inject.Singleton;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.junit.Rule;
import org.junit.Test;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule2;
import org.opendaylight.infrautils.simple.ReadyWiring;
import org.opendaylight.infrautils.simple.web.impl.WebWiring;
import org.opendaylight.infrautils.web.ServletContextProvider;
import org.opendaylight.infrautils.web.ServletContextRegistration;
import org.opendaylight.infrautils.web.WebContextProvider;

/**
 * Test of {@link ServletContextRegistration} via {@link WebWiring}.
 * Similar to {@link JettyLauncherTest} but in a Guice environment.
 *
 * @author Michael Vorburger.ch
 */
public class WebWiringTest {

    public static class TestWiring extends AbstractModule {

        @Override
        protected void configure() {
        }

        @Provides
        @Singleton
        TestFilter testFilter(WebContextProvider webContextProvider) throws ServletException {
            TestFilter testFilter = new TestFilter();
            webContextProvider.newWebContext("/*", false).registerFilter("/*", "Test", testFilter);
            return testFilter;
        }

        @Provides
        @Singleton
        TestServlet testServlet1(WebContextProvider webContextProvider) throws ServletException {
            TestServlet testServlet = new TestServlet();
            webContextProvider.newWebContext("/test1", false).registerServlet("/*", "Test", testServlet);
            return testServlet;
        }

        @Provides
        @Singleton Servlet testServlet2(ServletContextProvider servletContextProvider) {
            // We *COULD* retain the returned ServletContextRegistration and unregister() it
            // e.g. in a @PostDestroy of this Module... but in a JSE Guice environment, who cares!
            TestServlet testServlet = new TestServlet();
            servletContextProvider.newServletContext("/test2", false, servletContext -> {
                servletContext.addServlet("Test", testServlet).addMapping("/*");
            });
            return testServlet;
        }
    }

    public @Rule GuiceRule2 guice = new GuiceRule2(
           TestWiring.class, WebWiring.class, ReadyWiring.class, AnnotationsModule.class);

    @Test
    public void testServlet() throws IOException {
        JettyLauncherTest.checkTestServlet("test1");
        JettyLauncherTest.checkTestServlet("test2");
    }

}
