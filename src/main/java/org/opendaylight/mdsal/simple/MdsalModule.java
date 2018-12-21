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
import org.opendaylight.mdsal.binding.dom.codec.api.BindingNormalizedNodeSerializer;
import org.opendaylight.mdsal.dom.api.DOMDataBroker;
import org.opendaylight.mdsal.dom.api.DOMMountPointService;
import org.opendaylight.mdsal.dom.api.DOMNotificationService;
import org.opendaylight.mdsal.dom.broker.DOMMountPointServiceImpl;
import org.opendaylight.mdsal.eos.binding.api.EntityOwnershipService;
import org.opendaylight.mdsal.eos.binding.dom.adapter.BindingDOMEntityOwnershipServiceAdapter;
import org.opendaylight.mdsal.eos.dom.api.DOMEntityOwnershipService;
import org.opendaylight.mdsal.eos.dom.simple.SimpleDOMEntityOwnershipService;
import org.opendaylight.mdsal.singleton.common.api.ClusterSingletonServiceProvider;
import org.opendaylight.mdsal.singleton.dom.impl.DOMClusterSingletonServiceProviderImpl;

@SuppressWarnings("deprecation") // sure, but that's the point of this class...
public class MdsalModule extends AbstractModule {

    @Override
    protected void configure() {
        // TODO This is WRONG; later need to use the DistributedEntityOwnershipService instead here!
        bind(DOMEntityOwnershipService.class).to(SimpleDOMEntityOwnershipService.class);
    }
/*
    @Provides
    @Singleton DataBroker getDataBroker(org.opendaylight.controller.md.sal.binding.api.DataBroker controllerDB) {
        return new DataBrokerAdapter(controllerDB);
    }

    @Provides
    @Singleton
    @PingPong DataBroker getPingPongDataBroker(
            @PingPong org.opendaylight.controller.md.sal.binding.api.DataBroker controllerDB) {
        return new DataBrokerAdapter(controllerDB);
    }
*/
    @Provides
    @Singleton DOMDataBroker getDOMDataBroker(org.opendaylight.controller.md.sal.dom.api.DOMDataBroker controllerDDB) {
        return new DOMDataBrokerAdapter(controllerDDB);
    }

    @Provides
    @Singleton
    @PingPong DOMDataBroker getPingPongDOMDataBroker(
            @PingPong org.opendaylight.controller.md.sal.dom.api.DOMDataBroker controllerDDB) {
        return new DOMDataBrokerAdapter(controllerDDB);
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
/*
    @Provides
    @Singleton
    RpcProviderService getRpcProviderService(DOMRpcProviderService domRpcRegistry, BindingToNormalizedNodeCodec codec) {
        return new BindingDOMRpcProviderServiceAdapter(domRpcRegistry, codec);
    }
*/
}
