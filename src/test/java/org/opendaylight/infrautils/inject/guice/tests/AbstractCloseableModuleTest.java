/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.inject.guice.tests;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runners.model.Statement;
import org.opendaylight.infrautils.inject.guice.AbstractCloseableModule;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule;

/**
 * Test PreDestroy in Module.
 *
 * @author Michael Vorburger.ch
 */
public class AbstractCloseableModuleTest {

    public static class TestModule extends AbstractCloseableModule {
        boolean isClosed = false;

        @Override
        public void close() {
            isClosed = true;
        }

        @Override protected void configureCloseables() {
            // IRL: bind(...)
        }
    }

    @SuppressWarnings("checkstyle:IllegalThrows")
    @Test public void testModuleWithPreDestroy() throws Throwable {
        @SuppressWarnings("resource")
        TestModule testModule = new TestModule();
        new GuiceRule(testModule, new AnnotationsModule()).apply(EMPTY_STATEMENT, null, new Object()).evaluate();
        assertThat(testModule.isClosed).isTrue();
    }

    private static final Statement EMPTY_STATEMENT = new Statement() {
        @Override
        public void evaluate() throws Throwable {
        }
    };

}
