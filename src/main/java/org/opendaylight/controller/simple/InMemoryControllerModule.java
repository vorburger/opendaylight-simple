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
import org.opendaylight.controller.md.sal.binding.api.NotificationService;
import org.opendaylight.controller.md.sal.binding.compat.HeliumRpcProviderRegistry;
import org.opendaylight.controller.md.sal.binding.impl.BindingDOMDataBrokerAdapter;
import org.opendaylight.controller.md.sal.binding.impl.BindingDOMNotificationPublishServiceAdapter;
import org.opendaylight.controller.md.sal.binding.impl.BindingDOMNotificationServiceAdapter;
import org.opendaylight.controller.md.sal.binding.impl.BindingDOMRpcProviderServiceAdapter;
import org.opendaylight.controller.md.sal.binding.impl.BindingDOMRpcServiceAdapter;
import org.opendaylight.controller.md.sal.binding.impl.BindingToNormalizedNodeCodec;
import org.opendaylight.controller.md.sal.binding.test.DataBrokerTestModule;
import org.opendaylight.controller.md.sal.dom.api.DOMDataBroker;
import org.opendaylight.controller.md.sal.dom.api.DOMMountPointService;
import org.opendaylight.controller.md.sal.dom.api.DOMNotificationService;
import org.opendaylight.controller.md.sal.dom.broker.impl.DOMNotificationRouter;
import org.opendaylight.controller.md.sal.dom.broker.impl.PingPongDataBroker;
import org.opendaylight.controller.md.sal.dom.broker.impl.mount.DOMMountPointServiceImpl;
import org.opendaylight.controller.sal.binding.api.RpcProviderRegistry;
import org.opendaylight.infrautils.inject.guice.AbstractCloseableModule;
import org.opendaylight.mdsal.binding.dom.codec.api.BindingNormalizedNodeSerializer;
import org.opendaylight.mdsal.dom.api.DOMRpcProviderService;
import org.opendaylight.mdsal.dom.api.DOMRpcService;
import org.opendaylight.mdsal.dom.api.DOMSchemaService;
import org.opendaylight.mdsal.dom.broker.DOMRpcRouter;
import org.opendaylight.mdsal.simple.MdsalModule;
import org.opendaylight.mdsal.simple.PingPong;

@SuppressFBWarnings("UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
public class InMemoryControllerModule extends AbstractCloseableModule {

    // TODO re-use org.opendaylight.controller.md.sal.binding.impl.BindingBrokerWiring

    // TODO propose @Inject and @PreDestroy close() annotations at source to simplify this, a lot...

    private BindingToNormalizedNodeCodec bindingToNormalizedNodeCodec;
    private BindingDOMNotificationPublishServiceAdapter bindingDOMNotificationPublishServiceAdapter;
    private DOMNotificationRouter domNotificationPublishService;

    @Override
    protected void configureCloseables() {
        install(new MdsalModule());

        // TODO this is just for early stage POC! switch to real CDS wiring here, eventually..
        DataBrokerTestModule dataBrokerTestModule = new DataBrokerTestModule(true);
        DataBroker dataBroker = dataBrokerTestModule.getDataBroker();
        DOMSchemaService domSchemaService = dataBrokerTestModule.getSchemaService();

        bind(DOMSchemaService.class).toInstance(domSchemaService);
        DOMDataBroker domDefaultDataBroker = dataBrokerTestModule.getDOMDataBroker();
        bind(DOMDataBroker.class).toInstance(domDefaultDataBroker);
        bind(DataBroker.class).toInstance(dataBroker);

        bindingToNormalizedNodeCodec = dataBrokerTestModule.getBindingToNormalizedNodeCodec();
        bind(BindingToNormalizedNodeCodec.class).toInstance(bindingToNormalizedNodeCodec);

        PingPongDataBroker domPingPongDataBroker = new PingPongDataBroker(domDefaultDataBroker);
        bind(DOMDataBroker.class).annotatedWith(PingPong.class).toInstance(domPingPongDataBroker);
        bind(DataBroker.class).annotatedWith(PingPong.class)
                .toInstance(new BindingDOMDataBrokerAdapter(domPingPongDataBroker, bindingToNormalizedNodeCodec));

        domNotificationPublishService = dataBrokerTestModule.getDOMNotificationRouter();
        bind(DOMNotificationService.class).toInstance(domNotificationPublishService);
        bind(NotificationService.class).toInstance(
                new BindingDOMNotificationServiceAdapter(bindingToNormalizedNodeCodec, domNotificationPublishService));

        bindingDOMNotificationPublishServiceAdapter = new BindingDOMNotificationPublishServiceAdapter(
                bindingToNormalizedNodeCodec, domNotificationPublishService);
        bind(NotificationPublishService.class).toInstance(bindingDOMNotificationPublishServiceAdapter);
        bind(BindingNormalizedNodeSerializer.class).toInstance(bindingToNormalizedNodeCodec);

        bind(DOMMountPointService.class).to(DOMMountPointServiceImpl.class);

        DOMRpcRouter domRpcRouter = DOMRpcRouter.newInstance(domSchemaService);
        bind(DOMRpcRouter.class).toInstance(domRpcRouter);

        DOMRpcService rpcService = domRpcRouter.getRpcService();
        bind(DOMRpcService.class).toInstance(rpcService);

        org.opendaylight.controller.md.sal.dom.broker.impl.DOMRpcRouter controllerDOMRpcService
            = new org.opendaylight.controller.md.sal.dom.broker.impl.DOMRpcRouter(
                    rpcService, domRpcRouter.getRpcProviderService());
        bind(org.opendaylight.controller.md.sal.dom.api.DOMRpcService.class).toInstance(controllerDOMRpcService);
        bind(org.opendaylight.controller.md.sal.dom.api.DOMRpcProviderService.class)
                .toInstance(controllerDOMRpcService);

        DOMRpcProviderService domRpcProviderService = domRpcRouter.getRpcProviderService();
        org.opendaylight.controller.md.sal.dom.api.DOMRpcService controllerDomRpcService
            = new org.opendaylight.controller.md.sal.dom.broker.impl.DOMRpcRouter(rpcService, domRpcProviderService);
        BindingDOMRpcServiceAdapter bindingDOMRpcServiceAdapter
            = new BindingDOMRpcServiceAdapter(controllerDomRpcService, bindingToNormalizedNodeCodec);
        BindingDOMRpcProviderServiceAdapter bindingDOMRpcProviderServiceAdapter
            = new BindingDOMRpcProviderServiceAdapter(controllerDOMRpcService, bindingToNormalizedNodeCodec);
        bind(RpcProviderRegistry.class).toInstance(
                new HeliumRpcProviderRegistry(bindingDOMRpcServiceAdapter, bindingDOMRpcProviderServiceAdapter));
    }

    @Override
    public void close() throws Exception {
        bindingToNormalizedNodeCodec.close();
        bindingDOMNotificationPublishServiceAdapter.close();
        domNotificationPublishService.close();
    }
}
