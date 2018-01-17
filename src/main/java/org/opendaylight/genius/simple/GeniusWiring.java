/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.genius.simple;

import com.google.inject.AbstractModule;
import org.opendaylight.controller.simple.ControllerWiring;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;

public class GeniusWiring extends AbstractModule {

    @Override
    protected void configure() {
        install(new AnnotationsModule());

        install(new ControllerWiring());

        install(new MdsalUtilWiring());
        install(new IdManagerWiring());
        install(new AlivenessMonitorWiring());
        install(new InterfaceManagerWiring());
    }

}
