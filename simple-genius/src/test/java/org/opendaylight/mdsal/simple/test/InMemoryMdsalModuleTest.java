/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.mdsal.simple.test;

import java.util.concurrent.ExecutionException;
import javax.inject.Inject;
import org.junit.Rule;
import org.junit.Test;
import org.opendaylight.controller.simple.InMemoryControllerModule;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule;
import org.opendaylight.infrautils.simple.testutils.AbstractSimpleDistributionTest;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.mdsal.binding.api.RpcConsumerRegistry;
import org.opendaylight.mdsal.binding.api.RpcProviderService;
import org.opendaylight.mdsal.simple.PingPong;

public class InMemoryMdsalModuleTest extends AbstractSimpleDistributionTest {

    public @Rule GuiceRule guice = new GuiceRule(InMemoryControllerModule.class, AnnotationsModule.class);

    @Inject @PingPong DataBroker pingPongDataBroker;
    @Inject DataBroker dataBroker;

    @Inject RpcProviderService rpcProviderService;
    @Inject RpcConsumerRegistry rpcConsumerRegistry;

    @Test public void testDataBroker() throws InterruptedException, ExecutionException {
        dataBroker.newReadWriteTransaction().commit().get();
    }

}
