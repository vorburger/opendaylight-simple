/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple.osgi;

import java.util.Dictionary;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

/**
 * Simple {@link HttpService} implementation.
 *
 * @author Michael Vorburger.ch
 */
public class HttpServiceImpl implements HttpService {

    // TODO IFF really needed, then this could perhaps be implemented based on ServletContextProvider..
    // but I'm rather hoping to eventually remove it and instead directly use ServletContextProvider in ODL.

    @Override
    public void registerServlet(String alias, Servlet servlet, @SuppressWarnings("rawtypes") Dictionary initparams,
            HttpContext context) throws ServletException, NamespaceException {
        throw new UnsupportedOperationException("TODO Implement me... ;)");
    }

    @Override
    public void registerResources(String alias, String name, HttpContext context) throws NamespaceException {
        throw new UnsupportedOperationException("TODO Implement me... ;)");
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
