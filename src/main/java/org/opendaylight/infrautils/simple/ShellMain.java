/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.opendaylight.genius.itm.cli.TepShowState;
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

        karafStandaloneShell = createKarafStandaloneShell(injector);
    }

    @VisibleForTesting
    static KarafStandaloneShell createKarafStandaloneShell(Injector injector) {
        KarafStandaloneShell karafStandaloneShell = new KarafStandaloneShell(injector);
        // TODO karafStandaloneShell.register(...); all Guice registered beans which implement org.apache.karaf.shell.api.action.Action
        karafStandaloneShell.register(TepShowState.class); // TODO remove this hard-coded test/example
        return karafStandaloneShell;
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
