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
import java.io.IOException;
import java.security.Security;
import javax.inject.Singleton;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.opendaylight.aaa.cert.api.ICertificateManager;
import org.opendaylight.aaa.cert.impl.AaaCertRpcServiceImpl;
import org.opendaylight.aaa.cert.impl.CertificateManagerService;
import org.opendaylight.aaa.encrypt.AAAEncryptionService;
import org.opendaylight.controller.simple.ConfigReader;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.yang.gen.v1.urn.opendaylight.yang.aaa.cert.rev151126.AaaCertServiceConfig;
import org.opendaylight.yang.gen.v1.urn.opendaylight.yang.aaa.cert.rpc.rev151215.AaaCertRpcService;

/**
 * Guice wiring equivalent of AAA Cert BP XML in
 * aaa/aaa-cert/impl/src/main/resources/org/opendaylight/blueprint/aaaCert.xml.
 *
 * @author Michael Vorburger.ch
 */
public class CertModule extends AbstractModule {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Override
    protected void configure() {
        install(new EncryptModule());
    }

    @Provides
    @Singleton ICertificateManager certificateManagerService(AaaCertServiceConfig config, DataBroker db,
            AAAEncryptionService crypto) {
        return new CertificateManagerService(config, db, crypto);
    }

    @Provides
    @Singleton AaaCertServiceConfig getAaaCertServiceConfig(ConfigReader configReader) throws IOException {
        // TODO Urgh, CertificateManagerService is hard-coded to expect aaa-cert-config.xml to be in
        // etc/opendaylight/datastore/initial/config/aaa-cert-config.xml instead of in initial/aaa-cert-config.xml
        // and -more importantly- it *WRITES INTO IT* - OMG, what is that?!
        //
        // This _HACK_ prevents the WARN, but it should be changed in AAA:
//        File file = new File("etc/opendaylight/datastore/initial/config/aaa-cert-config.xml");
//        file.getParentFile().mkdirs();
//        Resources.asByteSource(Resources.getResource("initial/aaa-cert-config.xml")).copyTo(Files.asByteSink(file));

        return configReader.read("/initial/aaa-cert-config", AaaCertServiceConfig.class);
    }

    @Provides
    @Singleton
    AaaCertRpcService aaaCertRpcService(AaaCertServiceConfig config, DataBroker db, AAAEncryptionService crypt) {
        return new AaaCertRpcServiceImpl(config, db, crypt);
    }
}
