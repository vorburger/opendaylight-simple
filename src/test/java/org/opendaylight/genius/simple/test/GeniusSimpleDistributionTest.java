/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.genius.simple.test;

import javax.inject.Inject;
import org.junit.Rule;
import org.opendaylight.genius.interfacemanager.interfaces.InterfaceManagerService;
import org.opendaylight.genius.itm.api.IITMProvider;
import org.opendaylight.genius.simple.GeniusWiring;
import org.opendaylight.infrautils.inject.guice.GuiceClassPathBinder;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule2;
import org.opendaylight.infrautils.simple.ShellTestWiring;
import org.opendaylight.infrautils.simple.testutils.AbstractSimpleDistributionTest;

/**
 * Genius full distribution component test.
 *
 * @author Michael Vorburger.ch
 */
public class GeniusSimpleDistributionTest extends AbstractSimpleDistributionTest {

    // TODO https://github.com/google/guice/wiki/Grapher ...
    // TODO https://google.github.io/guice/api-docs/latest/javadoc/com/google/inject/tools/jmx/Manager.html

    private static final GuiceClassPathBinder CLASS_PATH_BINDER = new GuiceClassPathBinder("org.opendaylight");

    public @Rule GuiceRule2 guice = new GuiceRule2(new GeniusWiring(CLASS_PATH_BINDER), new ShellTestWiring());

    @SuppressWarnings("unused")
    private @Inject InterfaceManagerService interfaceManagerService;

    @SuppressWarnings("unused")
    private @Inject IITMProvider itmProvider;

}
