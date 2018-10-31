/*
 * Copyright © 2018 Red Hat, Inc. and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.inject;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class path scanner designed to be used with Guice. This provides a way for modules to request the bindings they
 * need by scanning the class path; bindings are processed recursively on {@link Singleton} constructors, for all
 * all {@link Inject}-annotated constructors.
 *
 * <p>Implementations are only mapped to interfaces for which they are the sole available implementation. If the class
 * path contains multiple implementations of a requested interface, the scanner won’t bind any of them, and the
 * caller will have to bind one explicitly.
 */
@SuppressWarnings("rawtypes")
public class ClassPathScanner {
    private static final Logger LOG = LoggerFactory.getLogger(ClassPathScanner.class);

    private final Map<String, Class> implementations = new HashMap<>();

    /**
     * Create a class path scanner, scanning packages with the given prefix.
     *
     * @param prefix The package prefix.
     */
    public ClassPathScanner(String prefix) {
        try (ScanResult scanResult =
                 new ClassGraph()
                     .enableClassInfo()
                     .enableAnnotationInfo()
                     .whitelistPackages(prefix)
                     .scan()) {
            Set<String> duplicateInterfaces = new HashSet<>();
            for (ClassInfo singletonInfo : scanResult.getClassesWithAnnotation(Singleton.class.getName())) {
                for (ClassInfo interfaceInfo : singletonInfo.getInterfaces()) {
                    String interfaceName = interfaceInfo.getName();
                    if (!duplicateInterfaces.contains(interfaceName)) {
                        if (implementations.put(interfaceName, singletonInfo.loadClass()) != null) {
                            LOG.debug("{} is declared multiple times, ignoring it", interfaceName);
                            implementations.remove(interfaceName);
                            duplicateInterfaces.add(interfaceName);
                        }
                    }
                }
            }
        }
    }

    /**
     * Binds all {@link Singleton} annotated classes discovered by scanning the class path to all their interfaces.
     *
     * @param prefix the package prefix of Singleton implementations to consider
     * @param binder The binder (modeled as a generic consumer)
     */
    public void bindAllSingletons(String prefix, BiConsumer<Class, Class> binder) {
        implementations.forEach((interfaceName, singletonClass) -> {
            if (singletonClass.getName().startsWith(prefix)) {
                try {
                    Class interfaceClass = Class.forName(interfaceName);
                    binder.accept(interfaceClass, singletonClass);
                    // TODO later probably lower this info to debug, but for now it's very useful..
                    LOG.info("Bound {} to {}", interfaceClass, singletonClass);
                } catch (ClassNotFoundException e) {
                    LOG.warn("ClassNotFoundException on Class.forName: {}", interfaceName, e);
                }
            }
        });
        // we do not want nor have to scan the @Singleton's @Inject annotated constructor; will also auto-discover.
    }

    /**
     * Binds the given interfaces, using implementations discovered by scanning the class path.
     *
     * @param binder The binder (modeled as a generic consumer).
     * @param interfaces The requested interfaces.
     */
    public <T> void bind(BiConsumer<Class<T>, Class<? extends T>> binder, Class... interfaces) {
        for (Class requestedInterface : interfaces) {
            bindImplementationFor(binder, requestedInterface);
        }
        // TODO Perhaps return interfaces which weren’t bound?
    }

    @SuppressWarnings("unchecked")
    private <T> void bindImplementationFor(BiConsumer<Class<T>, Class<? extends T>> binder, Class requestedInterface) {
        Class implementation = implementations.get(requestedInterface.getName());
        if (implementation != null) {
            binder.accept(requestedInterface, implementation);
            // TODO later probably lower this info to debug, but for now it's very useful..
            LOG.info("Bound {} to {}", requestedInterface, implementation);
            for (Constructor constructor : implementation.getDeclaredConstructors()) {
                Annotation injectAnnotation = constructor.getAnnotation(Inject.class);
                if (injectAnnotation != null) {
                    for (Class parameterType : constructor.getParameterTypes()) {
                        bindImplementationFor(binder, parameterType);
                    }
                }
            }
        }
    }
}
