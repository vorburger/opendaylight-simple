/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.karaf.shell;

import com.google.errorprone.annotations.Var;
import com.google.inject.Injector;
import org.apache.karaf.shell.api.console.Registry;
import org.apache.karaf.shell.impl.action.command.ManagerImpl;

/**
 * Karaf shell command manager implementation which instantiates through Guice,
 * therefore supporting {@literal @}Inject.
 *
 * @author Michael Vorburger.ch
 */
public class GuiceManagerImpl extends ManagerImpl {

    private final Injector guiceInjector;

    public GuiceManagerImpl(Injector guiceInjector, Registry dependencies, Registry registrations,
            boolean allowCustomServices) {
        super(dependencies, registrations, allowCustomServices);
        this.guiceInjector = guiceInjector;
    }

    @Override
    public <T> T instantiate(Class<? extends T> clazz, Registry registry) throws Exception {
        return super.instantiate(clazz, new GuiceDelegatingRegistry(registry));
    }

    private class GuiceDelegatingRegistry extends DelegatingRegistry {

        GuiceDelegatingRegistry(Registry delegate) {
            super(delegate);
        }

        @Override
        public <T> T getService(Class<T> clazz) {
            @Var T service = super.getService(clazz);
            if (service == null) {
                service = guiceInjector.getInstance(clazz);
            }
            return service;
        }

        // TODO @Override public <T> List<T> getServices(Class<T> clazz) {

        @Override
        public boolean hasService(Class<?> clazz) {
            boolean has = super.hasService(clazz);
            if (!has) {
                return guiceInjector.getInstance(clazz) != null;
            }
            return has;
        }
    }

}
