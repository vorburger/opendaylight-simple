/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.ready;

import static org.opendaylight.infrautils.ready.SystemState.BOOTING;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basic implementation of {@link SystemReadyMonitor}.
 *
 * @author Michael Vorburger.ch
 */
public class SystemReadyBaseImpl implements SystemReadyMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(SystemReadyBaseImpl.class);

    // TODO SystemReadyImpl should extend this.. but quid AbstractMXBean?

    private final Queue<SystemReadyListener> listeners = new ConcurrentLinkedQueue<>();
    private final AtomicReference<SystemState> systemState = new AtomicReference<>(BOOTING);

    @Override
    public SystemState getSystemState() {
        return systemState.get();
    }

    @Override
    public void registerListener(SystemReadyListener listener) {
        listeners.add(listener);
    }

    @Override
    public String getFailureCause() {
        // This doesn't fail
        return "";
    }

    @SuppressWarnings("checkstyle:IllegalCatch")
    public void ready() {
        for (SystemReadyListener systemReadyListener : listeners) {
            try {
                systemReadyListener.onSystemBootReady();
            } catch (Exception e) {
                LOG.error("onSystemBootReady() on a SystemReadyListener failed", e);
            }
        }
        systemState.set(SystemState.ACTIVE);
    }
}
