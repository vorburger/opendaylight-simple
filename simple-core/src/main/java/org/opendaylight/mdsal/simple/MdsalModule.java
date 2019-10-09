/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.mdsal.simple;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import javax.inject.Singleton;
import org.opendaylight.controller.sal.core.compat.DOMDataBrokerAdapter;
import org.opendaylight.controller.sal.core.compat.DOMNotificationServiceAdapter;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.mdsal.binding.api.RpcConsumerRegistry;
import org.opendaylight.mdsal.binding.api.RpcProviderService;
import org.opendaylight.mdsal.binding.dom.adapter.BindingDOMDataBrokerAdapter;
import org.opendaylight.mdsal.binding.dom.adapter.BindingDOMRpcProviderServiceAdapter;
import org.opendaylight.mdsal.binding.dom.adapter.BindingDOMRpcServiceAdapter;
import org.opendaylight.mdsal.binding.dom.adapter.BindingToNormalizedNodeCodec;
import org.opendaylight.mdsal.binding.dom.codec.api.BindingNormalizedNodeSerializer;
import org.opendaylight.mdsal.dom.api.DOMDataBroker;
import org.opendaylight.mdsal.dom.api.DOMMountPointService;
import org.opendaylight.mdsal.dom.api.DOMNotificationService;
import org.opendaylight.mdsal.dom.api.DOMRpcProviderService;
import org.opendaylight.mdsal.dom.api.DOMRpcService;
import org.opendaylight.mdsal.dom.broker.DOMMountPointServiceImpl;
import org.opendaylight.mdsal.dom.broker.DOMRpcRouter;
import org.opendaylight.mdsal.dom.broker.pingpong.PingPongDataBroker;
import org.opendaylight.mdsal.eos.binding.api.EntityOwnershipService;
import org.opendaylight.mdsal.eos.binding.dom.adapter.BindingDOMEntityOwnershipServiceAdapter;
import org.opendaylight.mdsal.eos.dom.api.DOMEntityOwnershipService;
import org.opendaylight.mdsal.eos.dom.simple.SimpleDOMEntityOwnershipService;
import org.opendaylight.mdsal.singleton.common.api.ClusterSingletonServiceProvider;
import org.opendaylight.mdsal.singleton.dom.impl.DOMClusterSingletonServiceProviderImpl;

@SuppressWarnings("deprecation") // sure, but that's the point of this class...
public class MdsalModule extends AbstractModule {

    // see org.opendaylight.controller.sal.restconf.impl.test.incubate.InMemoryMdsalModule ...
    // from https://git.opendaylight.org/gerrit/#/c/79388/

    // TODO [LOW] like InMemoryControllerModule extends AbstractCloseableModule and close up..
    // or rather, better, instead just annotate respective classes with @PreDestroy!

    @Override
    protected void configure() {
        // TODO This is WRONG; later need to use the DistributedEntityOwnershipService instead here!
        bind(DOMEntityOwnershipService.class).to(SimpleDOMEntityOwnershipService.class);
    }

    @Provides
    @Singleton DataBroker getDataBroker(DOMDataBroker domDataBroker, BindingToNormalizedNodeCodec codec) {
        return new BindingDOMDataBrokerAdapter(domDataBroker, codec);
    }

    @Provides
    @Singleton DOMDataBroker getDOMDataBroker(org.opendaylight.controller.md.sal.dom.api.DOMDataBroker controllerDDB) {
        return new DOMDataBrokerAdapter(controllerDDB);
    }

    @Provides
    @Singleton
    @PingPong DOMDataBroker getDOMPingPongDataBroker(DOMDataBroker domDataBroker) {
        return new PingPongDataBroker(domDataBroker);
    }

    @Provides
    @Singleton
    @PingPong
    DataBroker getPingPongDOMDataBroker(@PingPong DOMDataBroker domDataBroker, BindingToNormalizedNodeCodec codec) {
        return new BindingDOMDataBrokerAdapter(domDataBroker, codec);
    }

    @Provides
    @Singleton
    BindingToNormalizedNodeCodec getBindingToNormalizedNodeCodec(
            org.opendaylight.controller.md.sal.binding.impl.BindingToNormalizedNodeCodec controllerCodec) {
        return controllerCodec;
    }

    @Provides
    @Singleton DOMNotificationService getDOMNotificationService(
            org.opendaylight.controller.md.sal.dom.api.DOMNotificationService controllerDNS) {
        return new DOMNotificationServiceAdapter(controllerDNS);
    }

    @Provides
    @Singleton DOMMountPointService getDOMMountPoint(
            /* org.opendaylight.controller.md.sal.dom.api.DOMMountPointService controllerDMP */) {
        // TODO doesn't the mdsal DOMMountPointServiceImpl need the controller DOMMountPointService ?!
        return new DOMMountPointServiceImpl();
    }

    @Provides
    @Singleton EntityOwnershipService getDOMEntityOwnershipService(
            DOMEntityOwnershipService domService, BindingNormalizedNodeSerializer conversionCodec) {
        return new BindingDOMEntityOwnershipServiceAdapter(domService, conversionCodec);
    }

    @Provides
    @Singleton ClusterSingletonServiceProvider getClusterSingletonServiceProvider(DOMEntityOwnershipService eos) {
        return new DOMClusterSingletonServiceProviderImpl(eos);
    }

//    @Provides
//    @Singleton DOMRpcService getDOMService(org.opendaylight.controller.md.sal.dom.api.DOMRpcService controllerDRSA) {
//        return new DOMRpcServiceAdapter(controllerDRSA);
//    }

    @Provides
    @Singleton
    RpcConsumerRegistry getRpcConsumerRegistry(DOMRpcService domService, BindingToNormalizedNodeCodec codec) {
        return new BindingDOMRpcServiceAdapter(domService, codec);
    }

    @Provides
    @Singleton
    RpcProviderService getRpcProviderService(DOMRpcProviderService domRpcRegistry, BindingToNormalizedNodeCodec codec) {
        return new BindingDOMRpcProviderServiceAdapter(domRpcRegistry, codec);
    }

    @Provides
    @Singleton DOMRpcProviderService getDOMRpcProviderService(DOMRpcRouter domRpcRouter) {
        return domRpcRouter.getRpcProviderService();
    }
}
