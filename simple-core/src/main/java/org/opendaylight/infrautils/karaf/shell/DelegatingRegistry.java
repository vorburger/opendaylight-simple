/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.karaf.shell;

import java.util.List;
import java.util.concurrent.Callable;
import org.apache.karaf.shell.api.console.Command;
import org.apache.karaf.shell.api.console.Registry;

public class DelegatingRegistry implements Registry {

    private final Registry delegate;

    public DelegatingRegistry(Registry delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<Command> getCommands() {
        return delegate.getCommands();
    }

    @Override
    public Command getCommand(String scope, String name) {
        return delegate.getCommand(scope, name);
    }

    @Override
    public <T> void register(Callable<T> factory, Class<T> clazz) {
        delegate.register(factory, clazz);
    }

    @Override
    public void register(Object service) {
        delegate.register(service);
    }

    @Override
    public void unregister(Object service) {
        delegate.unregister(service);
    }

    @Override
    public <T> T getService(Class<T> clazz) {
        return delegate.getService(clazz);
    }

    @Override
    public <T> List<T> getServices(Class<T> clazz) {
        return delegate.getServices(clazz);
    }

    @Override
    public boolean hasService(Class<?> clazz) {
        return delegate.hasService(clazz);
    }

}
