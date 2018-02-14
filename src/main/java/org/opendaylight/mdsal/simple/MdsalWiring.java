/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.mdsal.simple;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import javax.inject.Singleton;
import org.opendaylight.controller.simple.ControllerWiring;
import org.opendaylight.mdsal.binding.dom.codec.api.BindingNormalizedNodeSerializer;
import org.opendaylight.mdsal.eos.binding.api.EntityOwnershipService;
import org.opendaylight.mdsal.eos.binding.dom.adapter.BindingDOMEntityOwnershipServiceAdapter;
import org.opendaylight.mdsal.eos.dom.api.DOMEntityOwnershipService;
import org.opendaylight.mdsal.eos.dom.simple.SimpleDOMEntityOwnershipService;

public class MdsalWiring extends AbstractModule {

    @Override
    protected void configure() {
        install(new ControllerWiring());

        bind(DOMEntityOwnershipService.class).to(SimpleDOMEntityOwnershipService.class);
    }

    @Provides
    @Singleton EntityOwnershipService getDOMEntityOwnershipService(
            DOMEntityOwnershipService domService, BindingNormalizedNodeSerializer conversionCodec) {
        return new BindingDOMEntityOwnershipServiceAdapter(domService, conversionCodec);
    }

}
