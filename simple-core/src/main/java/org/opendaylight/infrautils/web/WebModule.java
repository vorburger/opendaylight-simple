/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.web;

import com.google.inject.AbstractModule;
import org.opendaylight.aaa.web.WebContextSecurer;
import org.opendaylight.aaa.web.WebServer;
import org.opendaylight.aaa.web.jetty.JettyWebServer;
import org.opendaylight.aaa.web.servlet.ServletSupport;
import org.opendaylight.aaa.web.servlet.jersey2.JerseyServletSupport;

/**
 * Wiring for a web server.
 *
 * @author Michael Vorburger.ch
 */
public class WebModule extends AbstractModule {

    // TODO note (new) org.opendaylight.aaa.web.testutils.WebTestModule .. integrate?

    @Override
    protected void configure() {
        // TODO read port from a -D parameter or configuration file instead of hard-coding
        bind(WebServer.class).toInstance(new JettyWebServer(8181));

        // JAX-RS
        bind(ServletSupport.class).to(JerseyServletSupport.class);

        // TODO replace this NOOP WebContextSecurer with one with a fixed uid/pwd for HTTP BASIC (and ditch AAA)
        bind(WebContextSecurer.class).toInstance((webContextBuilder, urlPatterns) -> { });
    }
}
