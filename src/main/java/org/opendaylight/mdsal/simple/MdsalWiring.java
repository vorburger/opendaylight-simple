/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.mdsal.simple;

import com.google.inject.AbstractModule;
import org.opendaylight.mdsal.eos.binding.api.EntityOwnershipService;
import org.opendaylight.mdsal.eos.binding.dom.adapter.BindingDOMEntityOwnershipServiceAdapter;

public class MdsalWiring extends AbstractModule {

    @Override
    protected void configure() {
        bind(EntityOwnershipService.class).to(BindingDOMEntityOwnershipServiceAdapter.class);
        // TODO bind(EntityOwnershipService.class).toInstance(new BindingDOMEntityOwnershipServiceAdapter(..., ...));
    }

}
