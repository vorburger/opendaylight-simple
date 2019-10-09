/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.inject.guice.testutils.tests;

import static com.google.common.truth.Truth.assertThat;

import javax.inject.Inject;
import org.junit.Rule;
import org.junit.Test;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule;

/**
 * Test illustrating how to use the GuiceRule with an inline lamda binding.
 *
 * @author Michael Vorburger.ch
 */
public class GuiceRuleLambdaTest {

    @SuppressWarnings("BindingToUnqualifiedCommonType")
    public @Rule GuiceRule guice = new GuiceRule(
        binder -> binder.bind(String.class).toInstance("hello, world"),
        new AnnotationsModule());

    @Inject String string;

    @Test public void testLambdaBinding() {
        assertThat(string).isEqualTo("hello, world");
    }
}
