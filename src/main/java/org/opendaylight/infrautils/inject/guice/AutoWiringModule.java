/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.inject.guice;

import com.google.inject.AbstractModule;
import java.util.Optional;

/**
 * Guice Module with classpath scanning based autowiring.
 *
 * @author Michael Vorburger.ch
 */
public class AutoWiringModule extends AbstractModule {

    protected final GuiceClassPathBinder classPathBinder;
    private final Optional<String> packagePrefix;

    public AutoWiringModule(GuiceClassPathBinder classPathBinder, String packagePrefix) {
        this.classPathBinder = classPathBinder;
        this.packagePrefix = Optional.of(packagePrefix);
    }

    protected AutoWiringModule(GuiceClassPathBinder classPathBinder) {
        this.classPathBinder = classPathBinder;
        this.packagePrefix = Optional.empty();
    }

    @Override
    protected void configure() {
        packagePrefix.ifPresent(prefix -> classPathBinder.bindAllSingletons(prefix, binder()));
    }
}
