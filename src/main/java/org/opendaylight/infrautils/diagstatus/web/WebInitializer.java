/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.diagstatus.web;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import org.opendaylight.aaa.web.ServletDetails;
import org.opendaylight.aaa.web.WebContext;
import org.opendaylight.aaa.web.WebContextBuilder;
import org.opendaylight.aaa.web.WebContextRegistration;
import org.opendaylight.aaa.web.WebServer;
import org.opendaylight.infrautils.diagstatus.DiagStatusService;

/**
 * Initializes and registers the {@link DiagStatusServlet}.
 *
 * @author Michael Vorburger.ch
 */
@Singleton
public class WebInitializer implements AutoCloseable {

    // TODO unify this with the OsgiWebInitializer in infrautils after moving aaa.web (back) to infrautils

    // TODO share this with the identical private field in OsgiWebInitializer in infrautils
    public static final String DIAGSTATUS_URL = "/diagstatus";

    private final WebContextRegistration webContextRegistration;

    @Inject
    public WebInitializer(WebServer webServer, DiagStatusService diagStatusService) throws ServletException {
        WebContextBuilder webContextBuilder = WebContext.builder().contextPath(DIAGSTATUS_URL);
        webContextBuilder.addServlet(
            ServletDetails.builder().addUrlPattern("/*").servlet(new DiagStatusServlet(diagStatusService)).build());
        webContextRegistration = webServer.registerWebContext(webContextBuilder.build());
    }

    @Override
    public void close() throws Exception {
        webContextRegistration.close();
    }
}
