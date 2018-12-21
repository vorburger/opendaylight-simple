/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.genius.simple;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import org.apache.karaf.shell.api.action.Action;
import org.opendaylight.genius.itm.cli.TepShowState;
import org.opendaylight.genius.itm.globals.ITMConstants;
import org.opendaylight.yang.gen.v1.urn.opendaylight.genius.itm.config.rev160406.ItmConfig;
import org.opendaylight.yang.gen.v1.urn.opendaylight.genius.itm.config.rev160406.ItmConfigBuilder;

public class ItmModule extends AbstractModule {

    // TODO share code here with ItmTestModule instead of copy/pasting it from there!

    @Override
    protected void configure() {
        ItmConfig itmConfigObj = new ItmConfigBuilder()
                .setDefTzEnabled(true)
                .setDefTzTunnelType(ITMConstants.TUNNEL_TYPE_VXLAN)
                .setGpeExtensionEnabled(false)
                .build();
        bind(ItmConfig.class).toInstance(itmConfigObj);

        // Commands
        Multibinder<Action> actionsBinder = Multibinder.newSetBinder(binder(), Action.class);
        actionsBinder.addBinding().to(TepShowState.class);
    }

}
