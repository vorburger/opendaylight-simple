/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.aaa.simple;

import com.google.inject.AbstractModule;

public class EncryptWiring extends AbstractModule {

    @Override
    protected void configure() {
    }

/*
    @Override
    protected void configure() {
        // as per aaa-encrypt-service-config.xml
        // TODO read this from XML with that helper I once wrote somewhere for tests instead of duplicating here..
        bind(AaaEncryptServiceConfig.class).toInstance(new AaaEncryptServiceConfigBuilder()
                .setEncryptKey("V1S1ED4OMeEh").setPasswordLength(12).setEncryptSalt("TdtWeHbch/7xP52/rp3Usw==")
                .setEncryptMethod("PBKDF2WithHmacSHA1").setEncryptType("AES").setEncryptIterationCount(32768)
                .setEncryptKeyLength(128).setCipherTransforms("AES/CBC/PKCS5Padding").build());
    }

    @Provides
    @Singleton
    public AAAEncryptionService aaaEncryptionService(AaaEncryptServiceConfig config, DataBroker db) {
        return new AAAEncryptionServiceImpl(config, db);
    }
*/
}
