/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.inject.guice;

import com.google.inject.Binder;
import org.opendaylight.infrautils.inject.ClassPathScanner;

/**
 * Binds interfaces to implementations in Guice by scanning the classpath.
 */
public class GuiceClassPathBinder {
    private final ClassPathScanner scanner;

    public GuiceClassPathBinder(String prefix) {
        this.scanner = new ClassPathScanner(prefix);
    }

    /**
     * Binds the implementation of the given interface, if any, in the given binder, along with all dependencies.
     *
     * @param binder The binder to set up.
     * @param requestedInterface The requested interface.
     */
    public void bind(Binder binder, Class<?> requestedInterface) {
        scanner.bind((contract, implementation) -> binder.bind(contract).to(implementation), requestedInterface);
    }

    public void bindAllSingletons(String prefix, Binder binder) {
        scanner.bindAllSingletons(prefix, (contract, implementation) -> binder.bind(contract).to(implementation));
    }
}
