/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.inject.guice.testutils.tests;

import static com.google.common.truth.Truth.assertThat;

import com.google.inject.AbstractModule;
import javax.annotation.PreDestroy;
import org.junit.Test;
import org.junit.runners.model.Statement;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule;

/**
 * Test PreDestroy in Module.
 *
 * @author Michael Vorburger.ch
 */
public class ModuleWithPreDestroyTest {

    public static class TestModule extends AbstractModule {
        boolean isClosed = false;

        @Override protected void configure() {
            bind(TestModule.class).toInstance(this);
        }

        @PreDestroy void close() {
            isClosed = true;
        }
    }

    @SuppressWarnings("checkstyle:IllegalThrows")
    @Test public void testModuleWithPreDestroy() throws Throwable {
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
