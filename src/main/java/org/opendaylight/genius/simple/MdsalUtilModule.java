/*
 * Copyright (c) 2016, 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.genius.simple;

import com.google.inject.AbstractModule;
import org.opendaylight.genius.mdsalutil.interfaces.IMdsalApiManager;
import org.opendaylight.genius.mdsalutil.internal.MDSALManager;
import org.opendaylight.genius.utils.hwvtep.HwvtepNodeHACache;
import org.opendaylight.genius.utils.hwvtep.internal.HwvtepNodeHACacheImpl;

public class MdsalUtilModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IMdsalApiManager.class).to(MDSALManager.class);
        bind(HwvtepNodeHACache.class).to(HwvtepNodeHACacheImpl.class);
    }
}
