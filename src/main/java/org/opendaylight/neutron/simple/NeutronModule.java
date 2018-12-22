/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.simple;

import org.opendaylight.infrautils.inject.guice.AutoWiringModule;
import org.opendaylight.infrautils.inject.guice.GuiceClassPathBinder;

/**
 * Guice module for Neutron.
 *
 * @author Michael Vorburger.ch
 */
public class NeutronModule extends AutoWiringModule {

    public NeutronModule(GuiceClassPathBinder classPathBinder) {
        super(classPathBinder, "org.opendaylight.neutron");
    }
}
