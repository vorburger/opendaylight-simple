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

/**
 * Guice module for RestConf, based on {@link RestConfWiring}.
 *
 * @author Michael Vorburger.ch
 */
public class RestConfModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RestConfWiring.class);
        bind(RestConfConfig.class).toInstance(RestConfConfig.builder()
                // TODO webSocketAddress should be required, and read from WebServer (configurable in WebWiring)
                .webSocketAddress(InetAddress.getLoopbackAddress())
                // TODO webSocketPort should be read from some configuration file
                .webSocketPort(9090).build());
    }
}
