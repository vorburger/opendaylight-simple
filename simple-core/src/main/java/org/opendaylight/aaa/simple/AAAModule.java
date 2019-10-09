/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.aaa.simple;

import javax.annotation.Nullable;
import javax.inject.Singleton;

import org.opendaylight.aaa.api.AuthenticationException;
import org.opendaylight.aaa.api.Claim;
import org.opendaylight.aaa.api.CredentialAuth;
import org.opendaylight.aaa.api.PasswordCredentials;
import org.opendaylight.aaa.shiro.tokenauthrealm.auth.ClaimBuilder;
import org.opendaylight.aaa.shiro.tokenauthrealm.auth.PasswordCredentialBuilder;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class AAAModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new CertModule());
        install(new ShiroModule());
    }

    @Provides
	@Singleton
	CredentialAuth<PasswordCredentials> getPasswordCredentialAuth() {
    	PasswordCredentials passwordCredentials = new PasswordCredentialBuilder().setUserName("admin").setPassword("admin").setDomain("").build();
    	return new CredentialAuth<PasswordCredentials>() {

			@Nullable
			@Override
			public Claim authenticate(PasswordCredentials cred) throws AuthenticationException {
				if (cred.equals(passwordCredentials)) {
					return new ClaimBuilder()
							.setUser("admin")
							.build();
				}
				return null;
			}
		};
	}
}
