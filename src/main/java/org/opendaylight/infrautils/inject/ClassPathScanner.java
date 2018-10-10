/*
 * Copyright © 2018 Red Hat, Inc. and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.infrautils.inject;

import com.google.inject.Binder;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
     * Binds the given interfaces in the given binder, using implementations discovered by scanning the class path.
     *
     * @param binder The binder.
     * @param interfaces The requested interfaces.
     */
    public void bind(Binder binder, Class... interfaces) {
        for (Class requestedInterface : interfaces) {
            bindImplementationFor(binder, requestedInterface);
        }
        // TODO Perhaps return interfaces which weren’t bound?
    }

    @SuppressWarnings("unchecked")
    private void bindImplementationFor(Binder binder, Class requestedInterface) {
        Class implementation = implementations.get(requestedInterface.getName());
        if (implementation != null) {
            binder.bind(requestedInterface).to(implementation);
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
