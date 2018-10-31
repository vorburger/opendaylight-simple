/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.simple;

import com.google.common.io.Resources;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import org.opendaylight.controller.blueprint.ext.BindingContext;
import org.opendaylight.controller.blueprint.ext.ConfigXMLReaderException;
import org.opendaylight.controller.blueprint.ext.DataStoreAppConfigDefaultXMLReader;
import org.opendaylight.controller.blueprint.ext.DataStoreAppConfigDefaultXMLReader.ConfigURLProvider;
import org.opendaylight.mdsal.binding.dom.codec.api.BindingNormalizedNodeSerializer;
import org.opendaylight.mdsal.dom.api.DOMSchemaService;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.xml.sax.SAXException;

/**
 * Reads YAML configuration files.
 *
 * @author Michael Vorburger.ch
 */
@Singleton
public class ConfigReader {

    // TODO this is currently only tested by the ServiceUtilsModuleTest; create a dedicated test when upstreamed

    private final DOMSchemaService schemaService;
    private final BindingNormalizedNodeSerializer bindingSerializer;

    @Inject
    public ConfigReader(DOMSchemaService schemaService, BindingNormalizedNodeSerializer bindingSerializer) {
        this.schemaService = schemaService;
        this.bindingSerializer = bindingSerializer;
    }

    public <T extends DataObject> T read(String resourcePathWithoutExtension, Class<T> yangType) {
        String xmlResourcePath = resourcePathWithoutExtension + ".xml";
        ConfigURLProvider configURLProvider = appConfigFileName -> Optional
                .of(Resources.getResource(ConfigReader.class, xmlResourcePath));
        try {
            return new DataStoreAppConfigDefaultXMLReader<T>(xmlResourcePath, xmlResourcePath, schemaService,
                    bindingSerializer, BindingContext.create(yangType.getName(), yangType, null), configURLProvider)
                            .createDefaultInstance();
        } catch (ConfigXMLReaderException | ParserConfigurationException | XMLStreamException | IOException
                | SAXException | URISyntaxException e) {
            throw new IllegalArgumentException(
                    "Resource not found on the classpath, or it's invalid: " + xmlResourcePath, e);
        }
    }
}
