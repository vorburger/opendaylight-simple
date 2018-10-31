/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.genius.simple;

import org.opendaylight.infrautils.inject.guice.AutoWiringModule;
import org.opendaylight.infrautils.inject.guice.GuiceClassPathBinder;

public class AlivenessMonitorWiring extends AutoWiringModule {

    public AlivenessMonitorWiring(GuiceClassPathBinder classPathBinder) {
        super(classPathBinder, "org.opendaylight.genius.alivenessmonitor");
    }
}
