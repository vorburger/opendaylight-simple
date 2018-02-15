/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple.web.test;

import static com.google.common.truth.Truth.assertThat;
import static java.nio.charset.StandardCharsets.US_ASCII;

import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.junit.Test;
import org.opendaylight.infrautils.web.WebContext;
import org.opendaylight.infrautils.web.WebContextProvider;

/**
 * Test of {@link WebContext} API.
 *
 * @author Michael Vorburger.ch
 */
public abstract class WebContextTest {

    protected abstract void startWebServer();

    protected abstract WebContextProvider getWebContextProvider();

    @Test
    @SuppressWarnings("checkstyle:IllegalThrows") // start() throws Throwable
    public void testAddAfterStart() throws Throwable {
        startWebServer();
        WebContext webContext = getWebContextProvider().newWebContext("/test1", false);
        webContext.registerServlet("/*", "Test", new TestServlet());
        checkTestServlet(getWebContextProvider().getBaseURL() + "/test1");
    }

    @Test
    public void testAddFilter() throws Exception {
        startWebServer();
        TestFilter testFilter = new TestFilter();
        WebContext webContext = getWebContextProvider().newWebContext("/testingFilters", false);
        webContext.addContextParam("testParam1", "avalue").registerFilter("/*", "Test", testFilter);
        assertThat(testFilter.isInitialized).isTrue();

    }

    @Test
    public void testRegisterListener() throws Exception {
        {
            WebContext webContext = getWebContextProvider().newWebContext("/testingListenerPreBoot", false);
            TestListener testListener = new TestListener();
            webContext.registerListener(testListener);
            assertThat(testListener.isInitialized).isFalse();
            startWebServer();
            assertThat(testListener.isInitialized).isTrue();
        } {
            WebContext webContext = getWebContextProvider().newWebContext("/testingListenerWhenRunning", false);
            TestListener testListener = new TestListener();
            webContext.registerListener(testListener);
            assertThat(testListener.isInitialized).isTrue();
        }
    }

    static void checkTestServlet(String urlPrefix) throws IOException {
        URL url = new URL(urlPrefix + "/something");
        URLConnection conn = url.openConnection();
        try (InputStream inputStream = conn.getInputStream()) {
            // The hard-coded ASCII here is strictly speaking wrong of course
            // (should interpret header from reply), but good enough for a test.
            try (InputStreamReader reader = new InputStreamReader(inputStream, US_ASCII)) {
                String result = CharStreams.toString(reader);
                assertThat(result).startsWith("hello, world");
            }
        }
    }

}
