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

    public Main(Module mainModule) {
        LOG.info("Starting up {}...", mainModule);
        // TODO share why PRODUCTION Javadoc, or more, w. org.opendaylight.infrautils.inject.guice.testutils.GuiceRule
        this.injector = Guice.createInjector(Stage.PRODUCTION, mainModule);
        LOG.info("Start up of dependency injection completed; Guice injector is now ready.");
        injector.getInstance(PostFullSystemInjectionListener.class).onFullSystemInjected();
        LOG.info("Completed invoking PostFullSystemInjectionListener.onFullSystemInjected");
    }

    public void close() {
        LOG.info("Initiating orderly shutdown by closing Guice injector...");
        injector.getInstance(CloseableInjector.class).close();
        LOG.info("Shutdown complete; Guice injector closed.");
        LOG.info("Now System.exit(0) so that any hanging non-daemon threads don't prevent JVM stop.");
        System.exit(0);
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
