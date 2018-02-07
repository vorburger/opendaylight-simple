/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple.jetty;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedList;
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
        WebAppContext webApp = new WebAppContext();
        webApp.getServletHandler().setStartWithUnavailable(false);
        webApp.setThrowUnavailableOnStartupException(true);
        webApp.setLogUrlOnStart(true);
        server.setHandler(webApp);

        // TODO read this from... Web-ContextPath from MANIFEST.MF
        webApp.setContextPath("/test");

        // TODO LOG.info baseResource, with context..
        webApp.setBaseResource(chop(webXmlUrl(), WEB_INF_WEB_XML));
        LOG.info("webApp.getWebInf() = {}", webApp.getWebInf());
        // TODO not needed? webApp.setDescriptor("WEB-INF/web.xml");
        // TODO or? webApp.setDescriptor(JettyLauncher.class.getResource("/WEB-INF/web.xml").toString());

        // see https://www.eclipse.org/jetty/documentation/9.3.x/jetty-classloading.html
        webApp.setParentLoaderPriority(true);
        // TODO need this, or delete?
        // "This will make EVERYTHING on the classpath be
        // scanned for META-INF/resources and web-fragment.xml - great for dev!
        // NOTE: Several patterns can be listed, separate by comma"
        // ? webApp.setAttribute(WebInfConfiguration.CONTAINER_JAR_PATTERN, ".*");

        server.start();

        Throwable unavailableException = webApp.getUnavailableException();
        if (unavailableException != null) {
            throw unavailableException;
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

    private Resource chop(final URL baseURL, String toChop) throws MalformedURLException, IOException {
        String base = baseURL.toExternalForm();
        if (!base.endsWith(toChop)) {
            throw new IllegalArgumentException(base + " does not endWith " + toChop);
        }
        base = base.substring(0, base.length() - toChop.length());
        return Resource.newResource(base);
    }

    private static URL webXmlUrl() throws IOException {
        final Collection<URL> urls = getResources(WEB_INF_WEB_XML);
        if (urls.isEmpty()) {
            throw new IllegalStateException(WEB_INF_WEB_XML + " not found on the classpath");
        }
// TODO support multiple..
//        if (urls.size() != 1) {
//            throw new IllegalStateException(
//                    WEB_INF_WEB_XML + " was found more than once on the classpath: " + urls.toString());
//        }
        return (URL) urls.toArray()[0];
    }

    private static Collection<URL> getResources(String resource) throws IOException {
        final ClassLoader cl = JettyLauncher.class.getClassLoader();
        final Enumeration<URL> urls = cl.getResources(resource);
        final LinkedList<URL> list = new LinkedList<>();
        while (urls.hasMoreElements()) {
            list.add(urls.nextElement());
        }
        return list;
    }

}
