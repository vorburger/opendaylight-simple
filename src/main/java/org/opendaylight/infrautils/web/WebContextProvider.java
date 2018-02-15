/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.web;

/**
 * Factory which provides new {@link WebContext}s.
 *
 * @author Michael Vorburger.ch
 */
public interface WebContextProvider {
    // TODO rename this simply to WebServer ?

    WebContext newWebContext(String contextPath, boolean sessions);

    /**
     * Base URL of this web server, without any contexts.  In production, this would
     * likely be HTTPS with a well known hostname and fixed port configured e.g. in
     * a Karaf etc/ configuration file.  In tests, this would be typically be HTTP
     * on localhost and an arbitrarily chosen port.
     *
     * @return base URL, with http[s] prefix and port, NOT ending in slash
     */
    String getBaseURL();

}
