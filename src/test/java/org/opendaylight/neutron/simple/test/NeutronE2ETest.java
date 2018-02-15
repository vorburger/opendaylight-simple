/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.simple.test;

import java.io.IOException;
import javax.inject.Inject;
import org.junit.Test;
import org.opendaylight.infrautils.web.WebContextProvider;
import org.opendaylight.neutron.e2etest.NeutronAllTests;

/**
 * Neutron end-to-end test.
 * Contrary to the ITNeutronE2E, this test does not need to run inside OSGi/Karaf.
 *
 * @author Michael Vorburger.ch
 */
public class NeutronE2ETest extends NeutronSimpleDistributionTest {

    // TODO move into Neutron next to the existing ITNeutronE2E

    public @Inject WebContextProvider webServer;

    @Test
    public void testNeutronWebAPI() throws IOException, InterruptedException {
        NeutronAllTests.testNeutron(webServer.getBaseURL() + "/controller/nb/v2/neutron");
    }

}
