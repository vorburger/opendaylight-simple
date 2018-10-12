/*
 * Copyright © 2018 Red Hat, Inc. and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.inject.tests;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.opendaylight.infrautils.inject.ClassPathScanner;

public class ClassPathScannerTest {
    private final Map<Class<?>, Class<?>> bindings = new HashMap<>();

    @Before
    public void setup() {
        new ClassPathScanner("org.opendaylight.infrautils.inject.tests").bind(bindings::put,
            ClassPathScannerTestTopInterface.class);
    }

    @Test
    public void verifyImplementationBinding() {
        assertEquals(ClassPathScannerTestImplementation.class, bindings.get(ClassPathScannerTestTopInterface.class));
    }
}
