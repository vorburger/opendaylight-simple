/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.simple;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;
import org.opendaylight.controller.md.sal.binding.impl.BindingDOMNotificationPublishServiceAdapter;
import org.opendaylight.controller.md.sal.binding.impl.BindingToNormalizedNodeCodec;
import org.opendaylight.controller.md.sal.binding.test.DataBrokerTestModule;
import org.opendaylight.controller.md.sal.dom.api.DOMNotificationPublishService;
import org.opendaylight.infrautils.inject.guice.AbstractCloseableModule;

public class ControllerWiring extends AbstractCloseableModule {

    BindingToNormalizedNodeCodec bindingToNormalizedNodeCodec;
    BindingDOMNotificationPublishServiceAdapter bindingDOMNotificationPublishServiceAdapter;

    @Override
    protected void configureCloseables() {
        // TODO this is just for early stage POC! switch to real CDS wiring here, eventually..
        DataBroker dataBroker = DataBrokerTestModule.dataBroker();
        bind(DataBroker.class).toInstance(dataBroker);

        bindingToNormalizedNodeCodec = ???;
        DOMNotificationPublishService domNotificationPublishService = ???;
        // TODO propose @Inject and @PreDestroy close() annotations at source instead of having to do this...
        bindingDOMNotificationPublishServiceAdapter = new BindingDOMNotificationPublishServiceAdapter(
                bindingToNormalizedNodeCodec, domNotificationPublishService);
        bind(NotificationPublishService.class).toInstance(bindingDOMNotificationPublishServiceAdapter);
    }

    @Override
    public void close() throws Exception {
        bindingToNormalizedNodeCodec.close();
        bindingDOMNotificationPublishServiceAdapter.close();
    }

}
