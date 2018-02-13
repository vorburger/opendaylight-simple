/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple.jetty.test;

import static com.google.common.truth.Truth.assertThat;
import static java.nio.charset.StandardCharsets.US_ASCII;

import com.google.common.io.CharStreams;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.junit.Test;
import org.opendaylight.infrautils.simple.jetty.JettyLauncher;

/**
 * Test of {@link JettyLauncher}.
 * @author Michael Vorburger.ch
 */
public class JettyLauncherTest {

    // TODO public static @ClassRule ClasspathHellDuplicatesCheckRule jHades = new ClasspathHellDuplicatesCheckRule();

    @Test
    @SuppressWarnings("checkstyle:IllegalThrows") // Jetty WebAppContext.getUnavailableException() throws Throwable
    public void testStart() throws Throwable {
        JettyLauncher jettyLauncher = new JettyLauncher();
        jettyLauncher.start();
        try {
            URL url = new URL("http://localhost:8080/test/something");
            URLConnection conn = url.openConnection();
            try (InputStream inputStream = conn.getInputStream()) {
                // The hard-coded ASCII here is strictly speaking wrong of course
                // (should interpret header from reply), but good enough for a test.
                try (InputStreamReader reader = new InputStreamReader(inputStream, US_ASCII)) {
                    String result = CharStreams.toString(reader);
                    assertThat(result).startsWith("hello, world");
                }
            }
        } finally {
            jettyLauncher.stop();
        }
    }

}
