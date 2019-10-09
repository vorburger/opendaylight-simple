/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.aaa.simple;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import javax.inject.Singleton;
import org.opendaylight.aaa.encrypt.AAAEncryptionService;
import org.opendaylight.aaa.encrypt.impl.AAAEncryptionServiceImpl;
import org.opendaylight.controller.simple.ConfigReader;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.yang.gen.v1.config.aaa.authn.encrypt.service.config.rev160915.AaaEncryptServiceConfig;

public class EncryptModule extends AbstractModule {

    @Provides
    @Singleton AaaEncryptServiceConfig getEncryptServiceConfig(ConfigReader configReader) {
        return configReader.read("/initial/aaa-encrypt-service-config", AaaEncryptServiceConfig.class);
    }

    @Provides
    @Singleton
    public AAAEncryptionService aaaEncryptionService(AaaEncryptServiceConfig config, DataBroker db) {
        return new AAAEncryptionServiceImpl(config, db);
    }

    @Override
    protected void configure() {
    }
}
