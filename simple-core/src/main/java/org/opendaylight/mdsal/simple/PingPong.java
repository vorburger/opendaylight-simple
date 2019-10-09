/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.mdsal.simple;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.inject.Qualifier;
import org.opendaylight.mdsal.binding.api.DataBroker;

/**
 * PingPong {@link DataBroker} dependency injection annotation.
 *
 * <p>USAGE: <pre> {@literal @}Singleton
 * public class Thing {
 *     {@literal @}Inject
 *     public Thing({@literal @}PingPong DataBroker dataBroker) {
 *         ...
 *     }
 * }</pre>
 *
 * @author Michael Vorburger.ch
 */
@Qualifier
@Documented
@Retention(RUNTIME)
@Target({ PARAMETER, METHOD, FIELD })
public @interface PingPong {

}
