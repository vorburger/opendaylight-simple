/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.netvirt.simple;

import org.opendaylight.genius.simple.GeniusWiring;
import org.opendaylight.infrautils.inject.guice.AutoWiringModule;
import org.opendaylight.infrautils.inject.guice.GuiceClassPathBinder;

public class NetvirtWiring extends AutoWiringModule {

    public NetvirtWiring(GuiceClassPathBinder classPathBinder) {
        super(classPathBinder);
    }

    @Override
    protected void configure() {
        install(new GeniusWiring(classPathBinder));

        install(new AclServiceWiring());
    }
}
