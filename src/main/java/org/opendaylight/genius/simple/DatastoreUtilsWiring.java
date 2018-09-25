/*
 * Copyright © 2018 Red Hat, Inc. and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.genius.simple;

import com.google.inject.AbstractModule;
import org.opendaylight.genius.datastoreutils.listeners.DataTreeEventCallbackRegistrar;
import org.opendaylight.genius.datastoreutils.listeners.internal.DataTreeEventCallbackRegistrarImpl;

public class DatastoreUtilsWiring extends AbstractModule {
    @Override
    protected void configure() {
        bind(DataTreeEventCallbackRegistrar.class).to(DataTreeEventCallbackRegistrarImpl.class);
    }
}
