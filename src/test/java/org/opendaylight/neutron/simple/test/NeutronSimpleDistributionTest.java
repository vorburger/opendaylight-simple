/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.simple.test;

import org.junit.Rule;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule2;
import org.opendaylight.infrautils.simple.ReadyWiring;
import org.opendaylight.infrautils.simple.testutils.AbstractSimpleDistributionTest;
import org.opendaylight.infrautils.simple.web.impl.WebWiring;
import org.opendaylight.neutron.simple.NeutronWiring;

/**
 * Netvirt full distribution component test.
 *
 * @author Michael Vorburger.ch
 */
public class NeutronSimpleDistributionTest extends AbstractSimpleDistributionTest {

    public @Rule GuiceRule2 guice = new GuiceRule2(
            NeutronWiring.class, WebWiring.class, ReadyWiring.class, AnnotationsModule.class);
}
