/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.web.osgi.impl;

import java.util.Dictionary;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.opendaylight.infrautils.web.WebContext;
import org.opendaylight.infrautils.web.WebContextProvider;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

/**
 * {@link HttpService} bridge implementation delegating to an
 * {@link WebContextProvider} (and {@link WebContext}).
 *
 * @author Michael Vorburger.ch
 */
@Singleton
// @OsgiServiceProvider(classes = HttpService.class)
public class HttpServiceWebContextImpl implements HttpService {

    // TODO must clarify/fully understand how HttpService deals with context prefix before implementing this..

    // TODO I'm hoping to eventually remove this and instead directly use
    // WebContextProvider and WebContext everywhere web related in ODL.

    private final WebContextProvider webContextProvider;

    @Inject
    public HttpServiceWebContextImpl(WebContextProvider webContextProvider) {
        this.webContextProvider = webContextProvider;
    }

    @Override
    public void registerServlet(String alias, Servlet servlet, @SuppressWarnings("rawtypes") Dictionary initparams,
            HttpContext context) throws ServletException, NamespaceException {
        throw new UnsupportedOperationException("TODO Implement me... ;)");
    }

    @Override
    public void registerResources(String alias, String name, HttpContext context) throws NamespaceException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unregister(String alias) {
        throw new UnsupportedOperationException("TODO Implement me... ;)");
    }

    @Override
    public HttpContext createDefaultHttpContext() {
        throw new UnsupportedOperationException("TODO Implement me... ;)");
    }

}
