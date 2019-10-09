/*
 * (C) 2019 Lumina Networks, Inc.
 * 2077 Gateway Place, Suite 500, San Jose, CA 95110.
 * All rights reserved.
 *
 * Use of the software files and documentation is subject to license terms.
 */
package org.opendaylight.netconf.simple.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import org.opendaylight.controller.config.threadpool.ScheduledThreadPool;

/**
 * PingPong {@link ScheduledThreadPool} dependency injection annotation.
 *
 * <p>USAGE: <pre> {@literal @}Singleton
 * public class Thing {
 *     {@literal @}Inject
 *     public Thing({@literal @}GlobalNetconfSshScheduledExecutor ScheduledThreadPool scheduledThreadPool) {
 *         ...
 *     }
 * }</pre>
 */
@Qualifier
@Documented
@Retention(RUNTIME)
@Target({ PARAMETER, METHOD, FIELD })
public @interface GlobalNetconfProcessingExecutor {

}
