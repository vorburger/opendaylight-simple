/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.simple;

import com.google.inject.AbstractModule;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.test.DataBrokerTestModule;

public class ControllerWiring extends AbstractModule {

    @Override
    protected void configure() {
        // TODO this is just for early stage POC! switch to real CDS wiring here, eventually..
        DataBroker dataBroker = DataBrokerTestModule.dataBroker();
        bind(DataBroker.class).toInstance(dataBroker);
    }

}
