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
import org.opendaylight.controller.md.sal.dom.api.DOMDataBroker;
import org.opendaylight.controller.md.sal.dom.api.DOMMountPointService;
import org.opendaylight.controller.md.sal.dom.api.DOMNotificationService;
import org.opendaylight.controller.md.sal.dom.broker.impl.DOMNotificationRouter;
import org.opendaylight.controller.md.sal.dom.broker.impl.mount.DOMMountPointServiceImpl;
import org.opendaylight.infrautils.inject.guice.AbstractCloseableModule;
import org.opendaylight.mdsal.binding.dom.codec.api.BindingNormalizedNodeSerializer;
import org.opendaylight.mdsal.dom.api.DOMRpcService;
import org.opendaylight.mdsal.dom.api.DOMSchemaService;
import org.opendaylight.mdsal.dom.broker.DOMRpcRouter;
import org.opendaylight.mdsal.simple.MdsalWiring;

@SuppressFBWarnings("UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
public class ControllerWiring extends AbstractCloseableModule {
    // TODO rename this to InMemoryDataStoreModule - because that's what this really is

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
        DOMSchemaService domSchemaService = dataBrokerTestModule.getSchemaService();

        bind(DOMSchemaService.class).toInstance(domSchemaService);
        bind(DOMDataBroker.class).toInstance(dataBrokerTestModule.getDOMDataBroker());
        bind(DataBroker.class).toInstance(dataBroker);

        bindingToNormalizedNodeCodec = dataBrokerTestModule.getBindingToNormalizedNodeCodec();
        domNotificationPublishService = dataBrokerTestModule.getDOMNotificationRouter();
        bind(DOMNotificationService.class).toInstance(domNotificationPublishService);

        bindingDOMNotificationPublishServiceAdapter = new BindingDOMNotificationPublishServiceAdapter(
                bindingToNormalizedNodeCodec, domNotificationPublishService);
        bind(NotificationPublishService.class).toInstance(bindingDOMNotificationPublishServiceAdapter);
        bind(BindingNormalizedNodeSerializer.class).toInstance(bindingToNormalizedNodeCodec);

        bind(DOMMountPointService.class).to(DOMMountPointServiceImpl.class);

        DOMRpcRouter domRpcRouter = DOMRpcRouter.newInstance(domSchemaService);
        bind(DOMRpcService.class).toInstance(domRpcRouter.getRpcService());

        org.opendaylight.controller.md.sal.dom.broker.impl.DOMRpcRouter controllerDOMRpcService
            = new org.opendaylight.controller.md.sal.dom.broker.impl.DOMRpcRouter(
                    domRpcRouter.getRpcService(), domRpcRouter.getRpcProviderService());
        bind(org.opendaylight.controller.md.sal.dom.api.DOMRpcService.class).toInstance(controllerDOMRpcService);
        bind(org.opendaylight.controller.md.sal.dom.api.DOMRpcProviderService.class)
                .toInstance(controllerDOMRpcService);
    }

    @Override
    public void close() throws Exception {
        bindingToNormalizedNodeCodec.close();
        bindingDOMNotificationPublishServiceAdapter.close();
        domNotificationPublishService.close();
    }
}
