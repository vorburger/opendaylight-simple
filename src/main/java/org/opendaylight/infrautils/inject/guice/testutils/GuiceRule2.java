/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.inject.guice.testutils;

import com.google.inject.ConfigurationException;
import com.google.inject.Module;
import org.opendaylight.infrautils.inject.PostFullSystemInjectionListener;

public class GuiceRule2 extends GuiceRule {

    @SafeVarargs
    public GuiceRule2(Class<? extends Module>... moduleClasses) {
        super(moduleClasses);
    }

    public GuiceRule2(Module... modules) {
        super(modules);
    }

    @Override
    protected void setUpGuice(Object target) {
        super.setUpGuice(target);
        try {
            injector.getInstance(PostFullSystemInjectionListener.class).onFullSystemInjected();
        } catch (ConfigurationException e) {
            // It's OK if we didn't bind a PostFullSystemInjectionListener.
        }
    }
}
