/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple.jetty;

import com.google.errorprone.annotations.Var;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Launcher of Jetty-based web server, reading web.xml from classpath.
 *
 * @author Michael Vorburger.ch
 */
@Singleton
public class JettyLauncher {

    private static final Logger LOG = LoggerFactory.getLogger(JettyLauncher.class);

    private static final String WEB_INF_WEB_XML = "WEB-INF/web.xml";

    private final Server server;

    public JettyLauncher() {
        server = new Server();

        ServerConnector http = new ServerConnector(server);
        // TODO intro. Configuration (like in metrics) to make this configurable
        http.setHost("localhost");
        http.setPort(8080);
        http.setIdleTimeout(30000);
        server.addConnector(http);

        MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
        server.addBean(mbContainer);

        // No sure how much use that is, as we'll terminate this via Ctrl-C, but it doesn't hurt either:
        server.setStopAtShutdown(true);
    }

    @PostConstruct
    @SuppressWarnings("checkstyle:IllegalThrows") // Jetty WebAppContext.getUnavailableException() throws Throwable
    public void start() throws Throwable {
        List<WebAppContext> webAppContexts = addWebAppContexts();
        server.start();

        for (WebAppContext webAppContext : webAppContexts) {
            Throwable unavailableException = webAppContext.getUnavailableException();
            if (unavailableException != null) {
                throw unavailableException;
            }
        }
    }

    @PreDestroy
    public void stop() throws Exception {
        server.stop();
    }

    // code from https://github.com/vorburger/EclipseWebDevEnv/blob/master/simpleservers/ch.vorburger.modudemo.core/src/main/java/ch/vorburger/demo/server/ServerLauncher.java
    // see also https://sites.google.com/site/michaelvorburger/simpleservers
    // and http://blog2.vorburger.ch/2015/03/mifos-standalone-package-how-to.html
    // and http://blog2.vorburger.ch/2014/09/mifos-executable-war-with-mariadb4j.html

    private List<WebAppContext> addWebAppContexts() throws IOException {
        @Var int temporaryToRemove = 1;
        List<WebAppContext> webAppContexts = new ArrayList<>();
        for (URL webXmlUrl : getResources(WEB_INF_WEB_XML)) {
            String baseResourceURL = chop(webXmlUrl.toExternalForm(), WEB_INF_WEB_XML);
            Resource baseResource = Resource.newResource(baseResourceURL);

            WebAppContext webAppContext = new WebAppContext();
            // TODO LOG.info baseResource, with context..
            webAppContext.setBaseResource(baseResource);

            // TODO read this from... Web-ContextPath from MANIFEST.MF
            String contextPath = "/test" + temporaryToRemove++;
            webAppContext.setContextPath(contextPath);
            LOG.info("Found web.xml; adding {} from {}", contextPath, baseResourceURL);

            webAppContext.getServletHandler().setStartWithUnavailable(false);
            webAppContext.setThrowUnavailableOnStartupException(true);
            webAppContext.setLogUrlOnStart(true);
            server.setHandler(webAppContext);

            LOG.debug("webApp.getWebInf() = {}", webAppContext.getWebInf());
            // TODO not needed? webApp.setDescriptor("WEB-INF/web.xml");
            // TODO or? webApp.setDescriptor(JettyLauncher.class.getResource("/WEB-INF/web.xml").toString());

            // see https://www.eclipse.org/jetty/documentation/9.3.x/jetty-classloading.html
            webAppContext.setParentLoaderPriority(true);
            // TODO need this, or delete?
            // "This will make EVERYTHING on the classpath be
            // scanned for META-INF/resources and web-fragment.xml - great for dev!
            // NOTE: Several patterns can be listed, separate by comma"
            // ? webApp.setAttribute(WebInfConfiguration.CONTAINER_JAR_PATTERN, ".*");

            webAppContexts.add(webAppContext);
        }
        return webAppContexts;
    }

    private String chop(String base, String toChop) throws MalformedURLException, IOException {
        if (!base.endsWith(toChop)) {
            throw new IllegalArgumentException(base + " does not endWith " + toChop);
        }
        return base.substring(0, base.length() - toChop.length());
    }

    private static List<URL> getResources(String resource) throws IOException {
        ClassLoader cl = JettyLauncher.class.getClassLoader();
        Enumeration<URL> urls = cl.getResources(resource);
        return Collections.list(urls);
    }

}
