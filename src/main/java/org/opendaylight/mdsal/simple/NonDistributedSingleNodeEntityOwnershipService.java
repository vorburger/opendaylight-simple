/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.mdsal.simple;

import com.google.common.base.Optional;
import org.opendaylight.mdsal.eos.common.api.CandidateAlreadyRegisteredException;
import org.opendaylight.mdsal.eos.common.api.EntityOwnershipState;
import org.opendaylight.mdsal.eos.dom.api.DOMEntity;
import org.opendaylight.mdsal.eos.dom.api.DOMEntityOwnershipCandidateRegistration;
import org.opendaylight.mdsal.eos.dom.api.DOMEntityOwnershipListener;
import org.opendaylight.mdsal.eos.dom.api.DOMEntityOwnershipListenerRegistration;
import org.opendaylight.mdsal.eos.dom.api.DOMEntityOwnershipService;

public class NonDistributedSingleNodeEntityOwnershipService implements DOMEntityOwnershipService {

    // TODO This is *WRONG* because it is a NOOP instead of a correct working simple impl...

    @Override
    public DOMEntityOwnershipCandidateRegistration registerCandidate(DOMEntity entity)
            throws CandidateAlreadyRegisteredException {
        return new DOMEntityOwnershipCandidateRegistration() {

            @Override
            public void close() {
                // NOOP
            }

            @Override
            public DOMEntity getInstance() {
                return entity;
            }
        };
    }

    @Override
    public DOMEntityOwnershipListenerRegistration registerListener(String entityType,
            DOMEntityOwnershipListener listener) {
        return new DOMEntityOwnershipListenerRegistration() {

            @Override
            public DOMEntityOwnershipListener getInstance() {
                return listener;
            }

            @Override
            public String getEntityType() {
                return entityType;
            }

            @Override
            public void close() {
                // NOOP
            }
        };
    }

    @Override
    public Optional<EntityOwnershipState> getOwnershipState(DOMEntity forEntity) {
        return Optional.of(EntityOwnershipState.IS_OWNER);
    }

    @Override
    public boolean isCandidateRegistered(DOMEntity forEntity) {
        return true;
    }

}
