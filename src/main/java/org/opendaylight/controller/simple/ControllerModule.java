/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.simple;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.test.DataBrokerTestModule;
import org.opendaylight.infrautils.inject.guice.testutils.AbstractGuiceJsr250Module;

public class ControllerModule extends AbstractGuiceJsr250Module {

    @Override
    protected void configureBindings() throws Exception {
        // TODO this is just for early stage POC! switch to real CDS wiring here, eventually..
        DataBroker dataBroker = DataBrokerTestModule.dataBroker();
        bind(DataBroker.class).toInstance(dataBroker);
    }

}
