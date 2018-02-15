/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.web.osgi.impl;

import com.google.common.collect.ImmutableMap;
import java.util.EventListener;
import java.util.Hashtable;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import org.opendaylight.infrautils.web.WebContext;
import org.opendaylight.infrautils.web.WebContextProvider;
import org.ops4j.pax.cdi.api.OsgiService;
import org.ops4j.pax.cdi.api.OsgiServiceProvider;
import org.ops4j.pax.web.service.WebContainer;
import org.ops4j.pax.web.service.WebContainerDTO;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link WebContextProvider} (and {@link WebContext}) bridge implementation
 * delegating to Pax Web WebContainer (which extends an OSGi {@link HttpService}).
 *
 * @author Michael Vorburger.ch
 */
@Singleton
@OsgiServiceProvider(classes = WebContextProvider.class)
public class WebContextProviderOSGiImpl implements WebContextProvider {

    // TODO write an IT (using Pax Exam) which tests this, re-use JettyLauncherTest

    private static final Logger LOG = LoggerFactory.getLogger(WebContextProviderOSGiImpl.class);

    private final WebContainer paxWeb;

    @Inject
    public WebContextProviderOSGiImpl(@OsgiService WebContainer osgiHttpService) {
        this.paxWeb = osgiHttpService;
    }

    @Override
    public String getBaseURL() {
        WebContainerDTO details = paxWeb.getWebcontainerDTO();
        return "http://" + details.listeningAddresses[0] + ":" + details.port;
    }

    @Override
    public WebContext newWebContext(String contextPath, boolean sessions) {
        return new WebContextImpl(contextPath, sessions);
    }

    private class WebContextImpl implements WebContext {

        private final String contextPath;
        private final HttpContext osgiHttpContext;

        private final Queue<Servlet> registeredServlets = new ConcurrentLinkedQueue<>();
        private final Queue<EventListener> registeredEventListeners = new ConcurrentLinkedQueue<>();
        private final Queue<Filter> registeredFilters = new ConcurrentLinkedQueue<>();

        WebContextImpl(String contextPath, boolean sessions) {
            this.contextPath = contextPath;
            this.osgiHttpContext = paxWeb.createDefaultHttpContext();
            // ignore sessions; OSGi HttpService / Pax Web does not seem to support on/off
            // (it probably assumes always with session?)
        }

        @Override
        public WebContext registerServlet(String urlPattern, String name, Servlet servlet, Map<String, String> params)
                throws ServletException {
            String alias = contextPath + "/" + urlPattern;
            LOG.info("Registering Servlet for alias {}: {}", alias, servlet);
            try {
                paxWeb.registerServlet(alias, servlet, new Hashtable<>(params), osgiHttpContext);
            } catch (NamespaceException e) {
                throw new IllegalArgumentException("Failed to register Servlet: " + alias, e);
            }
            registeredServlets.add(servlet);
            return this;
        }

        @Override
        public WebContext registerFilter(String urlPattern, String name, Filter filter, Map<String, String> params) {
            boolean asyncSupported = false;
            String alias = contextPath + "/" + urlPattern;
            paxWeb.registerFilter(filter, new String[] { name }, new String[] { alias }, new Hashtable<>(params),
                    asyncSupported, osgiHttpContext);
            registeredFilters.add(filter);
            return this;
        }

        @Override
        public WebContext registerListener(ServletContextListener listener) {
            paxWeb.registerEventListener(listener, osgiHttpContext);
            registeredEventListeners.add(listener);
            return this;
        }

        @Override
        public WebContext addContextParam(String name, String value) {
            // TODO This is probably incorrect, and will cause 2nd context-param to wipe 1st?
            // This will get solved when WebContext is immutable and holds all context params together
            Map<String, String> map = ImmutableMap.of(name, value);
            paxWeb.setContextParam(new Hashtable<>(map), osgiHttpContext);
            return this;
        }

        @Override
        public void close() {
            // The order is relevant here.. Servlets first, then Filters, Listeners last
            for (Servlet registeredServlet : registeredServlets) {
                paxWeb.unregisterServlet(registeredServlet);
            }
            for (Filter filter : registeredFilters) {
                paxWeb.unregisterFilter(filter);
            }
            for (EventListener eventListener : registeredEventListeners) {
                paxWeb.unregisterEventListener(eventListener);
            }
        }

    }

}
