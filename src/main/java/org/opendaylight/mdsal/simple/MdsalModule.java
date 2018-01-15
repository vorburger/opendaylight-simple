/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.mdsal.simple;

import org.opendaylight.infrautils.inject.guice.testutils.AbstractGuiceJsr250Module;
import org.opendaylight.mdsal.eos.binding.api.EntityOwnershipService;
import org.opendaylight.mdsal.eos.binding.dom.adapter.BindingDOMEntityOwnershipServiceAdapter;

public class MdsalModule extends AbstractGuiceJsr250Module {

    @Override
    protected void configureBindings() throws Exception {
        bind(EntityOwnershipService.class).to(BindingDOMEntityOwnershipServiceAdapter.class);
        // TODO bind(EntityOwnershipService.class).toInstance(new BindingDOMEntityOwnershipServiceAdapter(..., ...));
    }

}
