/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.testutils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.opendaylight.aaa.web.WebServer;

/**
 * HTTP Client.
 *
 * @author Michael Vorburger.ch
 */
@Singleton
public class TestHttpClient {

    public enum Method {
        GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE
    }

    private final WebServer webServer;

    @Inject
    public TestHttpClient(WebServer webServer) {
        this.webServer = webServer;
    }

    public int responseCode(Method httpMethod, String path) throws IOException {
        URL url = new URL(webServer.getBaseURL() + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(httpMethod.name());
        return conn.getResponseCode();
    }
}
