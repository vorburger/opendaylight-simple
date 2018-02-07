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
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;
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
    }

    @PostConstruct
    @SuppressWarnings("checkstyle:IllegalThrows") // Jetty WebAppContext.getUnavailableException() throws Throwable
    public void start() throws Throwable {
        WebAppContext webApp = new WebAppContext();
        webApp.setThrowUnavailableOnStartupException(true);
        webApp.setContextPath("/test"); // TODO read this from... Web-ContextPath from MANIFEST.MF ?
        webApp.setLogUrlOnStart(true);

        // ? webApp.setParentLoaderPriority(true);
        // File warFile = new File("../../jetty-distribution/target/distribution/test/webapps/test/");
        // webApp.setWar(warFile.getAbsolutePath());
        // webApp.getWebInf()

        // ProtectionDomain protectionDomain = Start.class.getProtectionDomain();
        // URL location = protectionDomain.getCodeSource().getLocation();
        // webApp.setWar(location.toExternalForm());
        // LOG.info("location = {}", location);

        webApp.setBaseResource(baseResources());

        // This will make EVERYTHING on the classpath be
        // scanned for META-INF/resources and web-fragment.xml - great for dev!
        // NOTE: Several patterns can be listed, separate by comma
        // webApp.setAttribute(WebInfConfiguration.CONTAINER_JAR_PATTERN, ".*");

        LOG.info("webApp.getWebInf() = {}", webApp.getWebInf());

        // TODO watdat? webapp.addAliasCheck(new AllowSymLinkAliasChecker());
        server.setHandler(webApp);

        server.start();
        server.dumpStdErr(); // TODO waz this for?

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

    private ResourceCollection baseResources() throws IOException, MalformedURLException {
        final List<Resource> webResourceModules = new LinkedList<>();
        webResourceModules.add(chop(webXmlUrl(), WEB_INF_WEB_XML));
//        final Collection<URL> webTagURLs = getResources(WEB_TAG_FILE);
//        for (final URL webTagFileURL : webTagURLs) {
//            webResourceModules.add(chop(webTagFileURL, WEB_TAG_FILE));
//        }
        return new ResourceCollection(webResourceModules.toArray(new Resource[webResourceModules.size()]));
    }

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
        return urls.iterator().next();
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
