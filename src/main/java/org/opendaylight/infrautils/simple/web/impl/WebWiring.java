/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple.web.impl;

import com.google.inject.Provides;
import java.io.IOException;
import org.opendaylight.infrautils.inject.ModuleSetupRuntimeException;
import org.opendaylight.infrautils.inject.guice.testutils.AbstractCheckedModule;
import org.opendaylight.infrautils.ready.SystemReadyMonitor;
import org.opendaylight.infrautils.web.ServletContextProvider;
import org.opendaylight.infrautils.web.WebContextProvider;

public class WebWiring extends AbstractCheckedModule {

    private final boolean autoScanClassPathForWebXML;

    public WebWiring() {
        // TODO change default to true when AAA works?
        this(false);
    }

    public WebWiring(boolean autoScanClassPathForWebXML) {
        this.autoScanClassPathForWebXML = autoScanClassPathForWebXML;
    }

    @Override
    protected void checkedConfigure() throws IOException {
    }

    @Provides JettyLauncher jettyLauncher(SystemReadyMonitor systemReadyMonitor) {
        JettyLauncher jettyLauncher = new JettyLauncher(systemReadyMonitor);
        if (autoScanClassPathForWebXML) {
            try {
                jettyLauncher.addWebAppContexts();
            } catch (IOException e) {
                throw new ModuleSetupRuntimeException(e);
            }
        }
        return jettyLauncher;
    }

    @Provides ServletContextProvider servletContextProvider(JettyLauncher jetty) {
        return jetty;
    }

    @Provides WebContextProvider webContextProvider(JettyLauncher jetty) {
        return jetty;
    }
}
