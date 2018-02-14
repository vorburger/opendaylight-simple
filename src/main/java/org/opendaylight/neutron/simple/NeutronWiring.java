/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.simple;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import javax.inject.Singleton;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.ws.rs.core.Application;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.opendaylight.infrautils.web.WebContextProvider;
import org.opendaylight.neutron.northbound.api.NeutronNorthboundRSApplication;

public class NeutronWiring extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    Servlet jaxRSNeutron(WebContextProvider webContextProvider) throws ServletException {
        Application app = new NeutronNorthboundRSApplication();
        Servlet jaxRSNeutronServlet = new ServletContainer(app);
        // /controller/nb/v2/neutron is from neutron.northbound-api/pom.xml's <Web-ContextPath>
        webContextProvider.newWebContext("/controller/nb/v2/neutron", false /* no HTTP sessions needed */)
             // following is from neutron.northbound-api web.xml
            .registerServlet("/*", "JAXRSNeutron", jaxRSNeutronServlet)

            .registerFilter("/*", "cross-origin-restconf", new CrossOriginFilter(), ImmutableMap.of(
                        "allowedOrigins", "*", "allowedMethods",
                        "GET,POST,OPTIONS,DELETE,PUT,HEAD",
                        "allowedHeaders", "origin, content-type, accept, authorization"));

        // TODO add security stuff... AAA Shiro - or another one?
        return jaxRSNeutronServlet;
    }

}
