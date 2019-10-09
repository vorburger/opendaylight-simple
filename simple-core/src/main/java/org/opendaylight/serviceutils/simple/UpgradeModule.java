/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.serviceutils.simple;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import javax.inject.Singleton;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.simple.ConfigReader;
import org.opendaylight.serviceutils.upgrade.UpgradeState;
import org.opendaylight.serviceutils.upgrade.impl.UpgradeStateListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.serviceutils.upgrade.rev180702.UpgradeConfig;

/**
 * Guice Module for the Upgrades API.
 *
 * @author Michael Vorburger.ch
 */
public class UpgradeModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton UpgradeConfig getUpgradeConfig(ConfigReader configReader) {
        return configReader.read("/initial/serviceutils-upgrade-config", UpgradeConfig.class);
    }

    @Provides
    @Singleton UpgradeState getUpgradeStateService(DataBroker dataBroker, UpgradeConfig upgradeConfig) {
        return new UpgradeStateListener(dataBroker, upgradeConfig);
    }
}
