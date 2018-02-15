/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple;

import com.google.inject.AbstractModule;
import org.opendaylight.infrautils.inject.PostFullSystemInjectionListener;
import org.opendaylight.infrautils.ready.SystemReadyBaseImpl;
import org.opendaylight.infrautils.ready.SystemReadyMonitor;

public class ReadyWiring extends AbstractModule implements PostFullSystemInjectionListener {

    private final SystemReadyBaseImpl systemReadyImpl = new SystemReadyBaseImpl();

    @Override
    protected void configure() {
        bind(SystemReadyMonitor.class)/*.annotatedWith(OsgiService.class)*/.toInstance(systemReadyImpl);
        bind(PostFullSystemInjectionListener.class).toInstance(this);
    }

    @Override
    public void onFullSystemInjected() {
        systemReadyImpl.ready();
    }

}
