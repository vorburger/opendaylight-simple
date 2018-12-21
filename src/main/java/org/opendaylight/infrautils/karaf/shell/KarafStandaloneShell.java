/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.karaf.shell;

import static java.util.Objects.requireNonNull;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Injector;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.console.Session;
import org.apache.karaf.shell.api.console.SessionFactory;

/**
 * Karaf standalone shell, with programmatic instead of file-based command registration.
 *
 * @author Michael Vorburger.ch
 */
public class KarafStandaloneShell {

    private final InnerMain karafMain = new InnerMain();
    private final Set<Action> actions;
    private final Injector injector;

    public KarafStandaloneShell(Injector injector, Set<Action> actions) {
        this.injector = requireNonNull(injector, "injector");
        this.actions = requireNonNull(actions, "actions");
    }

    public void run() throws Exception {
        karafMain.run(new String[0]);
    }

    @VisibleForTesting
    // TODO This should ideally only be in src/test, not src/main ...
    public void testAllRegisteredCommands() throws Exception {
        karafMain.testAllRegisteredCommands();
    }

    private class InnerMain extends org.apache.karaf.shell.impl.console.standalone.Main {
        private GuiceManagerImpl manager;

        @Override
        protected void discoverCommands(Session session, ClassLoader cl, String resource) {
            manager = new GuiceManagerImpl(injector, session.getRegistry(), session.getFactory().getRegistry(),
                    false); // allowCustomServices = false so that there is an IllegalStateException if no service found
            for (Action action : actions) {
                manager.register(action.getClass());
            }
        }

        @SuppressWarnings("checkstyle:RegexpSingleLineJava")
        // TODO This should ideally only be in src/test, not src/main ...
        private void testAllRegisteredCommands() throws Exception {
            SessionFactory sessionFactory = createSessionFactory(null);
            Session session = createSession(sessionFactory, new EmptyInputStream(), System.out, System.err, null);
            discoverCommands(session, getClass().getClassLoader(), null);
            for (Action action : actions) {
                manager.instantiate(action.getClass());
            }
        }
    }

    // TODO when we're on Java 11, replace this with InputStream.nullInputStream()
    private static class EmptyInputStream extends InputStream {
        @Override
        public int read() throws IOException {
            return -1;
        }
    }
}
