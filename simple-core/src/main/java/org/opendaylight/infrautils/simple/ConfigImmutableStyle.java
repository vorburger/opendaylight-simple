/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.simple;

import static org.immutables.value.Value.Style.ImplementationVisibility.PRIVATE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.immutables.value.Value;

/**
 * <a href="http://immutables.org">Immutables.org</a> style meta annotation.
 *
 * @author Michael Vorburger.ch
 */
@Target({ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS) // Make it class retention for incremental compilation
@Value.Style(
        // generate *Builder as public top level type and the *Immutable impl. as private inner class
        visibility = PRIVATE,
        strictBuilder = true)
public @interface ConfigImmutableStyle { }
// Beware: Changes made here are not active without a restart in Eclipse (would need separate project)
