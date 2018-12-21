/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.netvirt.simple;

import org.opendaylight.genius.simple.GeniusModule;
import org.opendaylight.infrautils.inject.guice.AutoWiringModule;
import org.opendaylight.infrautils.inject.guice.GuiceClassPathBinder;

public class NetvirtModule extends AutoWiringModule {

    public NetvirtModule(GuiceClassPathBinder classPathBinder) {
        super(classPathBinder, "org.opendaylight.netvirt");
    }

    @Override
    protected void configureMore() {
        install(new GeniusModule(classPathBinder));

        install(new AclServiceModule());
    }
}
