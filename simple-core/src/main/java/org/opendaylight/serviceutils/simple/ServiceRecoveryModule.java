/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.serviceutils.simple;

import com.google.inject.AbstractModule;
import org.opendaylight.serviceutils.srm.ServiceRecoveryRegistry;
import org.opendaylight.serviceutils.srm.impl.ServiceRecoveryRegistryImpl;

public class ServiceRecoveryModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ServiceRecoveryRegistry.class).to(ServiceRecoveryRegistryImpl.class);
    }

}
