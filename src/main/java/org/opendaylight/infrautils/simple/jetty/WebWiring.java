/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple.jetty;

import java.io.IOException;
import org.opendaylight.infrautils.inject.guice.testutils.AbstractCheckedModule;
import org.opendaylight.infrautils.web.ServletContextProvider;

public class WebWiring extends AbstractCheckedModule {

    private final boolean autoScanClassPathForWebXML;

    public WebWiring() {
        this(false);
    }

    public WebWiring(boolean autoScanClassPathForWebXML) {
        this.autoScanClassPathForWebXML = autoScanClassPathForWebXML;
    }

    @Override
    protected void checkedConfigure() throws IOException {
        JettyLauncher jettyLauncher = new JettyLauncher();
        if (autoScanClassPathForWebXML) {
            jettyLauncher.addWebAppContexts();
        }
        bind(ServletContextProvider.class).toInstance(jettyLauncher);
    }

}
