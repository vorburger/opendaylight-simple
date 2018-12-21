/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.simple.test;

import javax.inject.Inject;
import org.junit.Rule;
import org.junit.Test;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.simple.InMemoryControllerModule;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule2;
import org.opendaylight.infrautils.simple.testutils.AbstractSimpleDistributionTest;

public class InMemoryControllerModuleTest extends AbstractSimpleDistributionTest {

    public @Rule GuiceRule2 guice = new GuiceRule2(InMemoryControllerModule.class, AnnotationsModule.class);

    @Inject DataBroker dataBroker;

    @Test public void testDataBroker() {
        dataBroker.newReadWriteTransaction().commit();
    }
}
