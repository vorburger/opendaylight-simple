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
import org.opendaylight.aaa.cert.api.ICertificateManager;
import org.opendaylight.aaa.cert.impl.AaaCertRpcServiceImpl;
import org.opendaylight.aaa.cert.impl.CertificateManagerService;
import org.opendaylight.aaa.encrypt.AAAEncryptionService;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.yang.gen.v1.urn.opendaylight.yang.aaa.cert.rev151126.AaaCertServiceConfig;
import org.opendaylight.yang.gen.v1.urn.opendaylight.yang.aaa.cert.rev151126.AaaCertServiceConfigBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.yang.aaa.cert.rpc.rev151215.AaaCertRpcService;

/**
 * Guice wiring equivalent of AAA Cert BP XML in
 * aaa/aaa-cert/impl/src/main/resources/org/opendaylight/blueprint/aaaCert.xml.
 *
 * @author Michael Vorburger.ch
 */
public class CertWiring extends AbstractModule {

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
}
