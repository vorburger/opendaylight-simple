/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.simple;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;
import org.opendaylight.controller.md.sal.binding.impl.BindingDOMNotificationPublishServiceAdapter;
import org.opendaylight.controller.md.sal.binding.impl.BindingToNormalizedNodeCodec;
import org.opendaylight.controller.md.sal.binding.test.DataBrokerTestModule;
import org.opendaylight.controller.md.sal.dom.broker.impl.DOMNotificationRouter;
import org.opendaylight.infrautils.inject.guice.AbstractCloseableModule;
import org.opendaylight.mdsal.binding.dom.codec.api.BindingNormalizedNodeSerializer;
import org.opendaylight.mdsal.simple.MdsalWiring;

@SuppressFBWarnings("UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
public class ControllerWiring extends AbstractCloseableModule {

    // TODO propose @Inject and @PreDestroy close() annotations at source to simplify this, a lot...

    private BindingToNormalizedNodeCodec bindingToNormalizedNodeCodec;
    private BindingDOMNotificationPublishServiceAdapter bindingDOMNotificationPublishServiceAdapter;
    private DOMNotificationRouter domNotificationPublishService;

    @Override
    protected void configureCloseables() {
        install(new MdsalWiring());

        // TODO this is just for early stage POC! switch to real CDS wiring here, eventually..
        DataBrokerTestModule dataBrokerTestModule = new DataBrokerTestModule(true);
        DataBroker dataBroker = dataBrokerTestModule.getDataBroker();
        bind(DataBroker.class).toInstance(dataBroker);

        bindingToNormalizedNodeCodec = dataBrokerTestModule.getBindingToNormalizedNodeCodec();
        domNotificationPublishService = dataBrokerTestModule.getDOMNotificationRouter();
        bindingDOMNotificationPublishServiceAdapter = new BindingDOMNotificationPublishServiceAdapter(
                bindingToNormalizedNodeCodec, domNotificationPublishService);
        bind(NotificationPublishService.class).toInstance(bindingDOMNotificationPublishServiceAdapter);
        bind(BindingNormalizedNodeSerializer.class).toInstance(bindingToNormalizedNodeCodec);
    }

    @Override
    public void close() throws Exception {
        bindingToNormalizedNodeCodec.close();
        bindingDOMNotificationPublishServiceAdapter.close();
        domNotificationPublishService.close();
    }

}
