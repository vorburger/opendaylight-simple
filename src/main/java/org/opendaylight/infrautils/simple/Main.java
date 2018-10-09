/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.mycila.guice.ext.closeable.CloseableInjector;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import org.opendaylight.infrautils.inject.PostFullSystemInjectionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Main.
 *
 * @author Michael Vorburger.ch
 */
public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    protected final Injector injector;

    @SuppressWarnings("checkstyle:IllegalCatch")
    public Main(Module mainModule) {
        try {
            LOG.info("Starting up {}...", mainModule);
            // TODO share why PRODUCTION Javadoc, or more, w. infrautils.inject.guice.testutils.GuiceRule
            this.injector = Guice.createInjector(Stage.PRODUCTION, mainModule);
            LOG.info("Start up of dependency injection completed; Guice injector is now ready.");
            injector.getInstance(PostFullSystemInjectionListener.class).onFullSystemInjected();
            LOG.info("Completed invoking PostFullSystemInjectionListener.onFullSystemInjected");
        } catch (Throwable t) {
            // If Guice stuff failed, there may be non-daemon threads which leave us hanging; force exit.
            LOG.error("Failed to start up, going to close up and exit", t);
            close();
            throw t;
        }
    }

    @SuppressFBWarnings("DM_EXIT")
    public final void close() {
        closeInjector();
        LOG.info("Now System.exit(0) so that any hanging non-daemon threads don't prevent JVM stop.");
        System.exit(0);
    }

    @SuppressWarnings("checkstyle:IllegalCatch")
    public final void closeInjector() {
        LOG.info("Initiating orderly shutdown by closing Guice injector...");
        try {
            if (injector != null) {
                injector.getInstance(CloseableInjector.class).close();
            }
        } catch (Throwable t) {
            LOG.warn("Trouble while closing CloseableInjector, but ignoring and continuing anyway", t);
        }
        LOG.info("Shutdown complete; Guice injector closed.");
    }

    public void awaitShutdown() {
        try {
            LOG.info("Awaiting shutdown signal, via CR/LF on STDIN...");
            System.in.read();
        } catch (IOException e) {
            LOG.error("System.in.read() failed?!", e);
        } finally {
            this.close();
        }
    }
}
