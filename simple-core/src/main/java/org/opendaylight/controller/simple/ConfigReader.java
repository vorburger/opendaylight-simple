/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.simple;

import com.google.common.io.Files;
import com.google.common.io.Resources;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * Reads YAML configuration files.
 *
 * @author Michael Vorburger.ch
 */
@Singleton
public class ConfigReader {

    // TODO this is currently only tested by the ServiceUtilsModuleTest; create a dedicated test when upstreamed

    private static final Logger LOG = LoggerFactory.getLogger(ConfigReader.class);

    // The idea is that this directory is *also* on the classpath, and *before* the main JARs (so that it can override)
    private final File configurationFilesBaseDirectory = new File("etc/");

    private final DOMSchemaService schemaService;
    private final BindingNormalizedNodeSerializer bindingSerializer;

    @Inject
    public ConfigReader(DOMSchemaService schemaService, BindingNormalizedNodeSerializer bindingSerializer) {
        this.schemaService = schemaService;
        this.bindingSerializer = bindingSerializer;
    }

    public <T extends DataObject> T read(String resourcePathWithoutExtension, Class<T> yangType) {
        return read(resourcePathWithoutExtension, yangType, null);
    }

    public <T extends DataObject> T read(String resourcePathWithoutExtension, Class<T> yangType, String listKeyValue) {
        String xmlResourcePath = resourcePathWithoutExtension + ".xml";
        URL xmlResourceURL = Resources.getResource(ConfigReader.class, xmlResourcePath);

        if (!xmlResourceURL.toExternalForm().contains("etc/")) {
            File newConfigurationFile = new File(configurationFilesBaseDirectory, xmlResourcePath);
            try {
                File newConfigurationFileDirectory = newConfigurationFile.getParentFile();
                if (newConfigurationFileDirectory.mkdirs()) {
                    LOG.info("Created new directory for configuration files: {}", newConfigurationFileDirectory);
                }
                String configurationAsText = Resources.toString(xmlResourceURL, StandardCharsets.UTF_8);
                Files.write(configurationAsText, newConfigurationFile, StandardCharsets.UTF_8);
                LOG.warn("Copied configuration file to directory where it can be overriden on next start "
                        + "(but this run isn't reading this, yet): {}", newConfigurationFile);
            } catch (IOException e) {
                LOG.error("Failed to copy configuration file {} to directory {}", xmlResourcePath,
                        configurationFilesBaseDirectory, e);
            }
        }

        try {
            ConfigURLProvider configURLProvider = appConfigFileName -> Optional.of(xmlResourceURL);
            T configuration = new DataStoreAppConfigDefaultXMLReader<T>(xmlResourcePath, xmlResourcePath, schemaService,
                    bindingSerializer, BindingContext.create(yangType.getName(), yangType, listKeyValue),
                        configURLProvider).createDefaultInstance();
            LOG.info("Read configuration from {} : {}", xmlResourceURL, configuration);
            return configuration;
        } catch (ConfigXMLReaderException | ParserConfigurationException | XMLStreamException | IOException
                | SAXException | URISyntaxException e) {
            throw new IllegalArgumentException(
                    "Resource not found on the classpath, or it's invalid: " + xmlResourcePath, e);
        }
    }
}
