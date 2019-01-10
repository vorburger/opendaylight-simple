/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.restconf.simple;

import com.google.inject.AbstractModule;
import java.net.InetAddress;
import org.opendaylight.netconf.sal.restconf.api.Bierman02RestConfWiring;
import org.opendaylight.netconf.sal.restconf.api.RestConfConfig;

/**
 * Guice module for RestConf, based on {@link RestConfWiring}.
 *
 * @author Michael Vorburger.ch
 */
public class RestConfModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Bierman02RestConfWiring.class);
        bind(RestConfConfig.class).toInstance(new RestConfConfig() {

            @Override
            public InetAddress webSocketAddress() {
                return InetAddress.getLoopbackAddress();
            }

            @Override
            public int webSocketPort() {
                // TODO webSocketPort should be read from some configuration file
                return 9090;
            }
        });
    }
}
