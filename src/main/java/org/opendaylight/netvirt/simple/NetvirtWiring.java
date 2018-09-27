/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.netvirt.simple;

import com.google.inject.AbstractModule;
import org.opendaylight.genius.simple.GeniusWiring;
import org.opendaylight.infrautils.inject.ClassPathScanner;

public class NetvirtWiring extends AbstractModule {

    private final ClassPathScanner scanner;

    public NetvirtWiring(ClassPathScanner scanner) {
        this.scanner = scanner;
    }

    @Override
    protected void configure() {
        install(new GeniusWiring(scanner));

        install(new AclServiceWiring());
    }

}
