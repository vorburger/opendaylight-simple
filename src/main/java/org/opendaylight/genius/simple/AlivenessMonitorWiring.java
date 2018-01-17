/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.genius.simple;

import com.google.inject.AbstractModule;
import org.opendaylight.genius.alivenessmonitor.internal.AlivenessMonitor;
import org.opendaylight.genius.alivenessmonitor.protocols.AlivenessProtocolHandlerRegistry;
import org.opendaylight.genius.alivenessmonitor.protocols.internal.AlivenessProtocolHandlerRegistryImpl;
import org.opendaylight.yang.gen.v1.urn.opendaylight.genius.alivenessmonitor.rev160411.AlivenessMonitorService;

public class AlivenessMonitorWiring extends AbstractModule {

    @Override
    protected void configure() {
        bind(AlivenessMonitorService.class).to(AlivenessMonitor.class);
        bind(AlivenessProtocolHandlerRegistry.class).to(AlivenessProtocolHandlerRegistryImpl.class);
    }

}
