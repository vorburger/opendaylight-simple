/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.restconf.simple;

import java.net.InetAddress;
import org.immutables.value.Value;
import org.opendaylight.infrautils.simple.ConfigImmutableStyle;

/**
 * Configuration for the RESTCONF server.
 *
 * @author Michael Vorburger.ch
 */
@Value.Immutable
@ConfigImmutableStyle
public interface RestConfConfig {

    static RestConfConfigBuilder builder() {
        return new RestConfConfigBuilder();
    }

    /**
     * IP interface which the WebSocket server will listen on.
     */
    // TODO make this Optional<InetAddress> and by default run on the same IF as the WebServer (TBD)
    InetAddress webSocketAddress();

    /**
     * TCP port which the WebSocket server will listen on.
     */
    int webSocketPort();

    /**
     * Web URL prefix. Defaults to "/restconf".
     */
    default String contextPath() {
        return "/restconf";
    }

    default Version version() {
        return Version.DRAFT_02;
    }

    enum Version {
        // TODO Confirm what are appropriate names for these versions?
        DRAFT_18, // RFC_8040 ?
        DRAFT_02
    }
}
