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
import com.google.inject.TypeLiteral;
import java.math.BigInteger;
import org.opendaylight.aaa.AAAShiroProvider;
import org.opendaylight.aaa.api.CredentialAuth;
import org.opendaylight.aaa.api.IdMService;
import org.opendaylight.aaa.api.PasswordCredentials;
import org.opendaylight.aaa.cert.api.ICertificateManager;
import org.opendaylight.aaa.shiro.idm.IdmLightProxy;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.yang.gen.v1.urn.opendaylight.aaa.app.config.rev170619.DatastoreConfig;
import org.opendaylight.yang.gen.v1.urn.opendaylight.aaa.app.config.rev170619.DatastoreConfig.Store;
import org.opendaylight.yang.gen.v1.urn.opendaylight.aaa.app.config.rev170619.DatastoreConfigBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.aaa.app.config.rev170619.ShiroConfiguration;
import org.opendaylight.yang.gen.v1.urn.opendaylight.aaa.app.config.rev170619.ShiroConfigurationBuilder;
import org.osgi.service.http.HttpService;

/**
 * Guice wiring equivalent of AAA Shiro BP XML in
 * aaa/aaa-shiro/impl/src/main/resources/org/opendaylight/blueprint/impl-blueprint.xml.
 *
 * @author Michael Vorburger.ch
 */
public class ShiroWiring extends AbstractModule {

    // TODO private AAAShiroProvider aaaShiroProvider;

    @Override
    protected void configure() {
        IdmLightProxy idmLightProxy = new IdmLightProxy();
        bind(IdMService.class).toInstance(idmLightProxy);
        bind(new TypeLiteral<CredentialAuth<PasswordCredentials>>() { }).toInstance(idmLightProxy);

        bind(DatastoreConfig.class).toInstance(new DatastoreConfigBuilder().setStore(Store.H2DataStore)
                .setTimeToLive(BigInteger.valueOf(36000)).setTimeToWait(BigInteger.valueOf(36000)).build());

        // TODO read this from aaa-app-config.xml
        // TODO read this from XML with that helper I once wrote somewhere for tests instead of duplicating here..
        bind(ShiroConfiguration.class).toInstance(new ShiroConfigurationBuilder().build());
    }

    @Provides
    AAAShiroProvider aaaShiroProvider(DataBroker dataBroker, ICertificateManager certificateManager,
            CredentialAuth<PasswordCredentials> credentialAuth, ShiroConfiguration shiroConfiguration,
            HttpService httpService,
            DatastoreConfig datastoreConfig) {
        AAAShiroProvider aaaShiroProvider = AAAShiroProvider.newInstance(dataBroker, certificateManager, credentialAuth,
                shiroConfiguration, httpService, "/moon", "/oauth2", datastoreConfig, "foo", "bar");
        aaaShiroProvider.init();
        return aaaShiroProvider;
    }

    // TODO AAAShiroProvider destroy-method="close"

}
