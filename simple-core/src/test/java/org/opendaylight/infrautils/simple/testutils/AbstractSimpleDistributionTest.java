/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple.testutils;

import static com.google.common.truth.Truth.assertThat;

import com.mycila.guice.ext.closeable.CloseableInjector;
import javax.inject.Inject;
import org.junit.Rule;
import org.junit.Test;
import org.opendaylight.infrautils.testutils.LogRule;

/**
 * Abstract base class for simple distribution component tests.
 *
 * @author Michael Vorburger.ch
 */
public abstract class AbstractSimpleDistributionTest {

    // TODO public static @ClassRule ClasspathHellDuplicatesCheckRule jHades = new ClasspathHellDuplicatesCheckRule();

    public @Rule LogRule logRule = new LogRule();
    // TODO NOK together with Log4j on CP, re-enable when split into separate modules:
    // public @Rule LogCaptureRule logCaptureRule = new LogCaptureRule();

    // The point of this is really just to make sure that subclasses have a @Rule GuiceRule
    private @Inject CloseableInjector closeableInjector;

    @Test public void testDistribution() {
        assertThat(closeableInjector).isNotNull();
    }
}
