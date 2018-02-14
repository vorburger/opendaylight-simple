/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.web;

import static java.util.Collections.emptyMap;

import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

/**
 * Web Context with URL prefix. AKA Web App or Servlet context. This not
 * surprisingly looks like a Servlet (3.0) {@link ServletContext}, which also
 * allows programmatic dynamic Servlet registration; however in practice direct
 * use of that API has been found to be problematic under OSGi because it is
 * intended for JSE and
 * <a href="https://github.com/eclipse/jetty.project/issues/1395">does not
 * easily appear to permit dynamic registration at any time</a> (only during
 * Servlet container initialization time by
 * {@link ServletContainerInitializer}), and is generally less clear to use than
 * this simple API which intentionally maps directly to what one would have
 * declared in a web.xml file. You can still {@link #getServletContext()} from
 * this service, if you really need to. It also looks similar to the OSGi
 * HttpService, but we want to avoid any org.osgi dependency (both API and impl)
 * here, and that API is also less clear (and uses an ancient Dictionary in its
 * method signature), and most importantly does not support Filters and
 * Listeners, only Servlets. We can however bridge this API to the OSGi
 * HttpService API, in both directions.
 *
 * @author Michael Vorburger.ch
 */
public interface WebContext extends AutoCloseable {

    /**
     * Registers new {@link Servlet}. It will automatically
     * <code>load-on-startup</code> (1), in order to "fail fast" in case of start-up
     * error.
     */
    default WebContext registerServlet(String urlPattern, String name, Servlet servlet) throws ServletException {
        return registerServlet(urlPattern, name, servlet, emptyMap());
    }

    WebContext registerServlet(String urlPattern, String name, Servlet servlet, Map<String, String> initParams)
            throws ServletException;

    default WebContext registerFilter(String urlPattern, String name, Filter filter) throws ServletException {
        return registerFilter(urlPattern, name, filter, emptyMap());
    }

    WebContext registerFilter(String urlPattern, String name, Filter filter, Map<String, String> initParams)
            throws ServletException;

    WebContext registerListener(String name, ServletContextListener listener) throws ServletException;

    WebContext addContextParam(String name, String value);

    // TODO Can we do without <security-constraint> <web-resource-collection> ?

    // TODO Do we need the ServletContext anywhere, or remove this to simplify?
    ServletContext getServletContext();

    @Override
    void close(); // does not throw Exception

}
