/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.web;

import com.google.inject.AbstractModule;
import org.opendaylight.aaa.web.WebServer;
import org.opendaylight.aaa.web.jetty.JettyWebServer;

/**
 * Wiring for a web server.
 *
 * @author Michael Vorburger.ch
 */
public class WebWiring extends AbstractModule {

    @Override
    protected void configure() {
        // TODO read port from a -D parameter or configuration file instead of hard-coding
        bind(WebServer.class).toInstance(new JettyWebServer(8181));
    }
}
