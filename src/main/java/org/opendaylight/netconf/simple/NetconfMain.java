/*
 * (C) 2019 Lumina Networks, Inc.
 * 2077 Gateway Place, Suite 500, San Jose, CA 95110.
 * All rights reserved.
 *
 * Use of the software files and documentation is subject to license terms.
 */
package org.opendaylight.netconf.simple;

import org.opendaylight.infrautils.inject.guice.GuiceClassPathBinder;
import org.opendaylight.infrautils.simple.Main;

public final class NetconfMain {

    private NetconfMain() { }

    public static void main(String[] args) {
        GuiceClassPathBinder classPathBinder = new GuiceClassPathBinder("org.opendaylight");
        new Main(new NetconfModule(classPathBinder)).awaitShutdown();
    }
}
