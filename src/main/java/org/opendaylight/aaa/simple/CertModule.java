/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.aaa.simple;

import com.google.inject.AbstractModule;

/**
 * Guice wiring equivalent of AAA Cert BP XML in
 * aaa/aaa-cert/impl/src/main/resources/org/opendaylight/blueprint/aaaCert.xml.
 *
 * @author Michael Vorburger.ch
 */
public class CertModule extends AbstractModule {

    @Override
    protected void configure() {
    }

/*
    @Override
    protected void configure() {
        // as per aaa-cert-config.xml
        // TODO read this from XML with that helper I once wrote somewhere for tests instead of duplicating here..
        bind(AaaCertServiceConfig.class).toInstance(new AaaCertServiceConfigBuilder().setUseConfig(true)
                .setUseMdsal(true).setBundleName("opendaylight").build());
        // TODO ctlKeystore & trustKeystore, but what are those, where are the stores, and needed for what?
    }

    @Provides
    @Singleton public ICertificateManager certificateManagerService(AaaCertServiceConfig config, DataBroker db,
            AAAEncryptionService crypto) {
        return new CertificateManagerService(config, db, crypto);
    }

    @Provides
    @Singleton
    public AaaCertRpcService aaaCertRpcService(AaaCertServiceConfig config, DataBroker db, AAAEncryptionService crypt) {
        return new AaaCertRpcServiceImpl(config, db, crypt);
    }
*/
}
