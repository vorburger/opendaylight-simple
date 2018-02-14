/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.web.osgi.impl;

import java.util.Hashtable;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import org.opendaylight.infrautils.web.WebContext;
import org.opendaylight.infrautils.web.WebContextProvider;
import org.ops4j.pax.cdi.api.OsgiService;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link WebContextProvider} (and {@link WebContext}) bridge implementation
 * delegating to an OSGi {@link HttpService}.
 *
 * <p>This only implements
 * {@link WebContext#registerServlet(String, String, Servlet)} but throws
 * {@link UnsupportedOperationException} for all other operations of
 * {@link WebContext} (except {@link WebContext#close()}), because
 * {@link HttpService} does not allow to register Servlet {@link Filter}s and
 * {@link ServletContextListener}s. As such, it may be useless.
 *
 * @author Michael Vorburger.ch
 */
@Singleton
public class WebContextProviderOSGiImpl implements WebContextProvider {

    // TODO Evaluate if we could use infrautils.web INSTEAD of Karaf's built-in web support

    // TODO Evaluate if we could directly talk to Jetty / PAX Web instead of only
    // HttpService and fully implement this (incl. Filters and Listeners and Context Parameters?)

    private static final Logger LOG = LoggerFactory.getLogger(WebContextProviderOSGiImpl.class);

    private final HttpService osgiHttpService;

    @Inject
    public WebContextProviderOSGiImpl(@OsgiService HttpService osgiHttpService) {
        this.osgiHttpService = osgiHttpService;
    }

    @Override
    public WebContext newWebContext(String contextPath, boolean sessions) {
        return new WebContextImpl(contextPath, sessions);
    }

    private class WebContextImpl implements WebContext {

        private final String contextPath;
        private final HttpContext osgiHttpContext;
        private final Queue<String> registeredAliases = new ConcurrentLinkedQueue<>();

        WebContextImpl(String contextPath, boolean sessions) {
            this.contextPath = contextPath;
            this.osgiHttpContext = osgiHttpService.createDefaultHttpContext();
            // ignore sessions; OSGi HttpService does not seem to support on/off
            // (it probably assumes always with session?)
        }

        @Override
        public void registerServlet(String urlPattern, String name, Servlet servlet, Map<String, String> initParams) {
            String alias = contextPath + "/" + urlPattern;
            LOG.info("Registering Servlet for alias {}: {}", alias, servlet);
            try {
                osgiHttpService.registerServlet(alias, servlet, new Hashtable<>(initParams), osgiHttpContext);
            } catch (ServletException | NamespaceException e) {
                throw new IllegalArgumentException("Failed to register Servlet: " + alias, e);
            }
            registeredAliases.add(alias);
        }

        @Override
        public void registerFilter(String urlPattern, String name, Filter filter, Map<String, String> initParams) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void registerListener(String name, ServletContextListener listener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addContextParam(String name, String value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ServletContext getServletContext() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void close() {
            for (String alias : registeredAliases) {
                osgiHttpService.unregister(alias);
            }
        }

    }

}
