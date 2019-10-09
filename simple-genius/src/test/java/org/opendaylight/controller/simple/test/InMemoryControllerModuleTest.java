/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.simple.test;

import java.util.concurrent.ExecutionException;
import javax.inject.Inject;
import org.junit.Rule;
import org.junit.Test;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.NotificationService;
import org.opendaylight.controller.simple.InMemoryControllerModule;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule;
import org.opendaylight.infrautils.simple.testutils.AbstractSimpleDistributionTest;
import org.opendaylight.mdsal.simple.PingPong;

public class InMemoryControllerModuleTest extends AbstractSimpleDistributionTest {

    public @Rule GuiceRule guice = new GuiceRule(InMemoryControllerModule.class, AnnotationsModule.class);

    @Inject DataBroker dataBroker;
    @Inject @PingPong DataBroker pingPongDataBroker;
    @Inject NotificationService notificationService;

    @Test public void testDataBroker() throws InterruptedException, ExecutionException {
        dataBroker.newReadWriteTransaction().commit().get();
    }
}
