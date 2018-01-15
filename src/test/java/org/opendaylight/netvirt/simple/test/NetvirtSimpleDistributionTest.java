/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.netvirt.simple.test;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule;
import org.opendaylight.infrautils.testutils.ClasspathHellDuplicatesCheckRule;
import org.opendaylight.infrautils.testutils.LogCaptureRule;
import org.opendaylight.infrautils.testutils.LogRule;
import org.opendaylight.netvirt.simple.NetvirtModule;

/**
 * Netvirt full distribution component test.
 *
 * @author Michael Vorburger.ch
 */
public class NetvirtSimpleDistributionTest {

    public static @ClassRule ClasspathHellDuplicatesCheckRule jHades = new ClasspathHellDuplicatesCheckRule();

    public @Rule LogRule logRule = new LogRule();
    public @Rule LogCaptureRule logCaptureRule = new LogCaptureRule();
    public @Rule GuiceRule guice = new GuiceRule(NetvirtModule.class);

    // TODO @Inject SomeInterfaceWithPostConstruct someService;

    @Test public void testNetvirtBindings() {
        // This is intentionally empty.
    }

}
