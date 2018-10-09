/*
 * Copyright © 2018 Red Hat, Inc. and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple;

import com.google.inject.AbstractModule;
import org.opendaylight.infrautils.web.WebWiring;

public class InfraUtilsWiring extends AbstractModule {
    @Override
    protected void configure() {
        install(new WebWiring());
        install(new ReadyWiring());
        install(new DiagStatusWiring());
        install(new MetricsWiring());
        install(new CachesWiring());
        install(new JobCoordinatorWiring());
    }
}
