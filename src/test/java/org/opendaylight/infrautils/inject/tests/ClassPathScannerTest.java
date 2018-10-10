/*
 * Copyright © 2018 Red Hat, Inc. and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.inject.tests;

import static com.google.common.truth.Truth.assertThat;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.junit.Test;
import org.opendaylight.infrautils.inject.ClassPathScanner;

public class ClassPathScannerTest {

    class TestModule extends AbstractModule {

        final ClassPathScanner scanner;

        TestModule(ClassPathScanner scanner) {
            this.scanner = scanner;
        }

        @Override
        protected void configure() {
            scanner.bind(binder(), ClassPathScannerTestTopInterface.class);
        }
    }

    @Test
    public void verifyImplementationBinding() {
        Injector injector = Guice.createInjector(Stage.PRODUCTION,
            new TestModule(
                new ClassPathScanner("org.opendaylight.infrautils.inject.tests")));

        assertThat(injector.getInstance(ClassPathScannerTestTopInterface.class))
                .isInstanceOf(ClassPathScannerTestImplementation.class);
    }
}
