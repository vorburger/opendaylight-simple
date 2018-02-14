/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple.osgi;

import com.google.inject.AbstractModule;
import org.opendaylight.infrautils.web.osgi.impl.HttpServiceWebContextImpl;
import org.osgi.service.http.HttpService;

public class OsgiServicesWiring extends AbstractModule {

    @Override
    protected void configure() {
        bind(HttpService.class).to(HttpServiceWebContextImpl.class);
    }

}
