/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple;

import com.google.inject.Module;
import org.opendaylight.infrautils.karaf.KarafStandaloneShell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Main, with shell.
 *
 * @author Michael Vorburger.ch
 */
public class ShellMain extends Main {

    private static final Logger LOG = LoggerFactory.getLogger(ShellMain.class);

    private final KarafStandaloneShell karafStandaloneShell;

    public ShellMain(Module mainModule) {
        super(mainModule);

        karafStandaloneShell = new KarafStandaloneShell();
        // TODO karafStandaloneShell.register(...);
    }

    @Override
    @SuppressWarnings("checkstyle:IllegalCatch")
    public void awaitShutdown() {
        try {
            karafStandaloneShell.run();
        } catch (Exception e) {
            LOG.error("Karaf standalone shell run failed", e);
        }

        this.close();
    }

}
