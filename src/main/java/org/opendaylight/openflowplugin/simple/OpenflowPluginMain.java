/*
 * (C) 2019 Lumina Networks, Inc.
 * 2077 Gateway Place, Suite 500, San Jose, CA 95110.
 * All rights reserved.
 *
 * Use of the software files and documentation is subject to license terms.
 */
package org.opendaylight.openflowplugin.simple;

import org.opendaylight.infrautils.inject.guice.GuiceClassPathBinder;
import org.opendaylight.infrautils.simple.ShellMain;

public final class OpenflowPluginMain {

    private OpenflowPluginMain() { }

    public static void main(String[] args) {
        GuiceClassPathBinder classPathBinder = new GuiceClassPathBinder("org.opendaylight");
        new ShellMain(new OpenFlowPluginModule(classPathBinder)).awaitShutdown();
    }
}
