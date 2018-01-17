/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple;

import com.google.inject.AbstractModule;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.opendaylight.infrautils.inject.PostFullSystemInjectionListener;
import org.opendaylight.infrautils.ready.SystemReadyListener;
import org.opendaylight.infrautils.ready.SystemReadyMonitor;
import org.opendaylight.infrautils.ready.SystemState;

public class ReadyWiring extends AbstractModule implements PostFullSystemInjectionListener {

    // AFAIK Guice does not actually inject concurrently, but better safe than sorry...
    private final Queue<SystemReadyListener> systemReadyListeners = new ConcurrentLinkedQueue<>();

    @Override
    protected void configure() {
        bind(SystemReadyMonitor.class)/*.annotatedWith(OsgiService.class)*/.toInstance(new SystemReadyMonitor() {

            @Override
            public void registerListener(SystemReadyListener listener) {
                systemReadyListeners.add(listener);
            }

            @Override
            public SystemState getSystemState() {
                return SystemState.ACTIVE;
            }
        });
        bind(PostFullSystemInjectionListener.class).toInstance(this);
    }

    @Override
    public void onFullSystemInjected() {
        for (SystemReadyListener systemReadyListener : systemReadyListeners) {
            systemReadyListener.onSystemBootReady();
        }
    }

}
