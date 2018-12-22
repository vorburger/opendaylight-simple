/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.serviceutils.simple.test;

import javax.inject.Inject;
import org.junit.Rule;
import org.opendaylight.controller.simple.InMemoryControllerModule;
import org.opendaylight.infrautils.inject.guice.testutils.AnnotationsModule;
import org.opendaylight.infrautils.inject.guice.testutils.GuiceRule;
import org.opendaylight.infrautils.simple.testutils.AbstractSimpleDistributionTest;
import org.opendaylight.serviceutils.simple.ServiceUtilsModule;
import org.opendaylight.serviceutils.upgrade.UpgradeState;

/**
 * Test the serviceutils wiring.
 *
 * @author Michael Vorburger.ch
 */
public class ServiceUtilsModuleTest extends AbstractSimpleDistributionTest {

    public @Rule GuiceRule guice = new GuiceRule(ServiceUtilsModule.class, InMemoryControllerModule.class,
            AnnotationsModule.class);

    @Inject UpgradeState upgradeState;

}
