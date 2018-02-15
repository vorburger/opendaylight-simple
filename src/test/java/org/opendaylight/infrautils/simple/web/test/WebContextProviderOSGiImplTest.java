/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple.web.test;

import javax.inject.Inject;
import org.opendaylight.infrautils.simple.web.impl.JettyLauncher;
import org.opendaylight.infrautils.web.WebContext;
import org.opendaylight.infrautils.web.WebContextProvider;

/**
 * Test of {@link JettyLauncher} implementation of {@link WebContext}..
 *
 * @author Michael Vorburger.ch
 */
public class WebContextProviderOSGiImplTest extends WebContextTest {

    // TODO move this into a separate Maven artifact and make it a complete Pax Exam Karaf IT test ..

    @Inject // this will get injected by Pax Exam
    public WebContextProvider webContextProvder;

    @Override
    protected void startWebServer() {
        // Ignore; under Karaf, the Pax Web managed Jetty server is always running
    }

    @Override
    protected WebContextProvider getWebContextProvider() {
        return webContextProvder;
    }

}
