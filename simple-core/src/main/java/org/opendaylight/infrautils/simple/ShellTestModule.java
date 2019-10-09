/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import java.util.Set;
import org.apache.karaf.shell.api.action.Action;
import org.opendaylight.infrautils.karaf.shell.KarafStandaloneShell;

/**
 * Guice wiring which tests shell commands wiring.
 *
 * <p>This makes tests using this wiring in their GuiceRule fail if an
 * org.apache.karaf.shell.api.action.Action is e.g. not annotated with
 * org.apache.karaf.shell.api.action.lifecycle.Service or if one of its
 * org.apache.karaf.shell.api.action.lifecycle.Reference annotated fields cannot
 * be found in the Guice injector.
 *
 * @author Michael Vorburger.ch
 */
public class ShellTestModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    KarafStandaloneShell karafStandaloneShell(Injector injector, Set<Action> actions) throws Exception {
        KarafStandaloneShell shell = new KarafStandaloneShell(injector, actions);
        shell.testAllRegisteredCommands();
        return shell;
    }
}
