/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.mdsal.simple.test;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.opendaylight.controller.simple.InMemoryControllerModule;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule2;
import org.opendaylight.infrautils.simple.testutils.AbstractSimpleDistributionTest;
import org.opendaylight.mdsal.binding.api.DataBroker;

public class InMemoryMdsalModuleTest extends AbstractSimpleDistributionTest {

    public @Rule GuiceRule2 guice = new GuiceRule2(InMemoryControllerModule.class, AnnotationsModule.class);

    // TODO @Inject
    DataBroker dataBroker;

    @Ignore // TODO sort out DataBrokerAdapter in MdsalModule
    @Test public void testDataBroker() {
        // dataBroker.newReadWriteTransaction().commit();
    }
}