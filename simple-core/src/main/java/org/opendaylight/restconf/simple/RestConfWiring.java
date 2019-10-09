/*
 * Copyright (c) 2018 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.restconf.simple;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.ws.rs.core.Application;
import org.opendaylight.aaa.web.ServletDetails;
import org.opendaylight.aaa.web.WebContext;
import org.opendaylight.aaa.web.WebContextRegistration;
import org.opendaylight.aaa.web.WebServer;
import org.opendaylight.aaa.web.servlet.ServletSupport;
import org.opendaylight.mdsal.dom.api.DOMDataBroker;
import org.opendaylight.mdsal.dom.api.DOMMountPointService;
import org.opendaylight.mdsal.dom.api.DOMNotificationService;
import org.opendaylight.mdsal.dom.api.DOMRpcService;
import org.opendaylight.mdsal.dom.api.DOMSchemaService;
import org.opendaylight.netconf.sal.restconf.impl.BrokerFacade;
import org.opendaylight.netconf.sal.restconf.impl.ControllerContext;
import org.opendaylight.netconf.sal.restconf.impl.RestconfImpl;
import org.opendaylight.netconf.sal.restconf.impl.RestconfProviderImpl;
import org.opendaylight.netconf.sal.restconf.impl.StatisticsRestconfServiceWrapper;
import org.opendaylight.restconf.nb.rfc8040.handlers.DOMDataBrokerHandler;
import org.opendaylight.restconf.nb.rfc8040.handlers.DOMMountPointServiceHandler;
import org.opendaylight.restconf.nb.rfc8040.handlers.NotificationServiceHandler;
import org.opendaylight.restconf.nb.rfc8040.handlers.RpcServiceHandler;
import org.opendaylight.restconf.nb.rfc8040.handlers.SchemaContextHandler;
import org.opendaylight.restconf.nb.rfc8040.handlers.TransactionChainHandler;
import org.opendaylight.restconf.nb.rfc8040.services.wrapper.ServicesWrapper;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpAddress;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.IpAddressBuilder;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.PortNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Standalone wiring for RESTCONF.
 *
 * <p>ACK: Some lines here were originally inspired by the CommunityRestConf class
 * from lighty.io. The differences include (1) that this class is "pure Java",
 * because the intention here is only to incubate and then propose it upstream
 * right into the netconf ODL project; (2) that we use infrautils.web (currently
 * in aaa),
 *
 * @author Michael Vorburger.ch, partially based on code in lighty.io
 */
@Singleton
public class RestConfWiring {

    private static final Logger LOG = LoggerFactory.getLogger(RestConfWiring.class);

    // TODO upstream this into ODL netconf/restconf and replace its Blueprint XML with this

    private final WebServer webServer;
    private final WebContext webContext;
    private final RestconfProviderImpl webSockerServer;
    private WebContextRegistration webContextRegistration;

    @Inject
    public RestConfWiring(RestConfConfig config, WebServer webServer, ServletSupport jaxRS,
            DOMSchemaService domSchemaService, DOMMountPointService domMountPointService, DOMRpcService domRpcService,
            DOMDataBroker domDataBroker, DOMNotificationService domNotificationService) {
        this.webServer = webServer;
        LOG.info("config = {}", config);

        // WebSocket
        ControllerContext controllerContext = ControllerContext.newInstance(domSchemaService, domMountPointService,
                domSchemaService);
        BrokerFacade broker = BrokerFacade.newInstance(domRpcService, domDataBroker, domNotificationService,
                controllerContext);
        RestconfImpl restconf = RestconfImpl.newInstance(broker, controllerContext);
        StatisticsRestconfServiceWrapper stats = StatisticsRestconfServiceWrapper.newInstance(restconf);
        IpAddress wsIpAddress = IpAddressBuilder.getDefaultInstance(config.webSocketAddress().getHostAddress());
        this.webSockerServer = new RestconfProviderImpl(stats, wsIpAddress, new PortNumber(config.webSocketPort()));

        // Servlet
        TransactionChainHandler transactionChainHandler = new TransactionChainHandler(domDataBroker);
        SchemaContextHandler schemaCtxHandler = SchemaContextHandler.newInstance(transactionChainHandler,
                domSchemaService);
        schemaCtxHandler.init();
        DOMMountPointServiceHandler domMountPointServiceHandler = DOMMountPointServiceHandler
                .newInstance(domMountPointService);
        DOMDataBrokerHandler domDataBrokerHandler = new DOMDataBrokerHandler(domDataBroker);
        RpcServiceHandler rpcServiceHandler = new RpcServiceHandler(domRpcService);
        NotificationServiceHandler notificationServiceHandler = new NotificationServiceHandler(domNotificationService);
        ServicesWrapper servicesWrapper = ServicesWrapper.newInstance(schemaCtxHandler, domMountPointServiceHandler,
                transactionChainHandler, domDataBrokerHandler, rpcServiceHandler, notificationServiceHandler,
                domSchemaService);

        // This is currently hard-coded to DRAFT_18; if we ever actually need to support the
        // older DRAFT_02 for anything, then (only) add it to the RestConfConfig and switch here
        Application application;
        switch (config.version()) {
            case DRAFT_02:
                application = new org.opendaylight.netconf.sal.rest.impl.RestconfApplication(
                        controllerContext, stats);
                break;

            case DRAFT_18:
                application = new org.opendaylight.restconf.nb.rfc8040.RestconfApplication(
                        schemaCtxHandler, domMountPointServiceHandler, servicesWrapper);
                break;

            default:
                throw new UnsupportedOperationException(config.version().name());
        }

        HttpServlet servlet = jaxRS.createHttpServletBuilder(application).build();
        this.webContext = WebContext.builder().contextPath(config.contextPath())
                .addServlet(ServletDetails.builder().addUrlPattern("/*").servlet(servlet).build())
                .build();

        // TODO secure it, using web API
    }

    @PostConstruct
    public void start() throws ServletException {
        this.webContextRegistration = this.webServer.registerWebContext(webContext);
        this.webSockerServer.start();
    }

    @PreDestroy
    public void stop() {
        this.webSockerServer.close();
        if (webContextRegistration != null) {
            this.webContextRegistration.close();
        }
    }
}
