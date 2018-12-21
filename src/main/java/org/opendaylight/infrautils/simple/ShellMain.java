/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple;

import com.google.inject.Module;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Set;
import javax.inject.Inject;
import org.apache.karaf.shell.api.action.Action;
import org.opendaylight.infrautils.karaf.shell.KarafStandaloneShell;
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

    private Set<Action> actions;

    @SuppressFBWarnings("UR_UNINIT_READ") // injectMembers() DI magic is beyond FB
    public ShellMain(Module mainModule) {
        super(mainModule);
        injector.injectMembers(this);
        karafStandaloneShell = new KarafStandaloneShell(injector, actions);
    }

    @Inject // invoked by injector.injectMembers(this) above
    public void setActions(Set<Action> actions) {
        this.actions = actions;
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
