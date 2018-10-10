/*
 * Copyright © 2018 Red Hat, Inc. and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.inject.tests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

import com.google.inject.Binder;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.opendaylight.infrautils.inject.ClassPathScanner;

public class ClassPathScannerTest {
    private final TestBinder testBinder = mock(TestBinder.class, Mockito.CALLS_REAL_METHODS);

    @Before
    public void setup() {
        new ClassPathScanner("org.opendaylight.infrautils.inject.tests").bind(testBinder,
            ClassPathScannerTestTopInterface.class);
    }

    @Test
    public void verifyImplementationBinding() {
        assertEquals(ClassPathScannerTestImplementation.class,
            testBinder.getImplementation(ClassPathScannerTestTopInterface.class));
    }

    private abstract static class TestBinder implements Binder {
        private Map<Class<?>, Class<?>> bindings;

        private <T> void storeBinding(Class<T> type, Class<? extends T> implementation) {
            if (bindings == null) {
                bindings = new HashMap<>();
            }
            bindings.put(type, implementation);
        }

        <T> Class<? extends T> getImplementation(Class<T> type) {
            return (Class<? extends T>) bindings.get(type);
        }

        @Override
        public <T> AnnotatedBindingBuilder<T> bind(Class<T> type) {
            TestAnnotatedBindingBuilder builder = mock(TestAnnotatedBindingBuilder.class, CALLS_REAL_METHODS);
            builder.setType(type);
            builder.setBinder(this);
            return builder;
        }

        private abstract static class TestAnnotatedBindingBuilder<T> implements AnnotatedBindingBuilder<T> {
            private Class<T> type;
            private TestBinder binder;

            @Nullable
            @Override
            public ScopedBindingBuilder to(Class<? extends T> implementation) {
                binder.storeBinding(type, implementation);
                return null;
            }

            void setType(Class<T> type) {
                this.type = type;
            }

            void setBinder(TestBinder binder) {
                this.binder = binder;
            }
        }
    }
}
