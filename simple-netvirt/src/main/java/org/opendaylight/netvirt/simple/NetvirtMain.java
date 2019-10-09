/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.netvirt.simple;

import org.opendaylight.infrautils.inject.guice.GuiceClassPathBinder;
import org.opendaylight.infrautils.simple.Main;

public final class NetvirtMain {

    private NetvirtMain() { }

    public static void main(String[] args) {
        GuiceClassPathBinder classPathBinder = new GuiceClassPathBinder("org.opendaylight");
        new Main(new NetvirtModule(classPathBinder)).awaitShutdown();
    }
}
