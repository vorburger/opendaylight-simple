/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.inject.guice;

import java.util.Optional;
import org.opendaylight.infrautils.inject.guice.testutils.AbstractCheckedModule;

/**
 * Guice Module with classpath scanning based autowiring.
 *
 * @author Michael Vorburger.ch
 */
public class AutoWiringModule extends AbstractCheckedModule {

    protected final GuiceClassPathBinder classPathBinder;
    private final Optional<String> packagePrefix;

    public AutoWiringModule(GuiceClassPathBinder classPathBinder, String packagePrefix) {
        this.classPathBinder = classPathBinder;
        this.packagePrefix = Optional.of(packagePrefix);
    }

    @Override
    protected final void checkedConfigure() throws Exception {
        packagePrefix.ifPresent(prefix -> classPathBinder.bindAllSingletons(prefix, binder()));
        configureMore();
    }

    protected void configureMore() throws Exception {
    }
}
