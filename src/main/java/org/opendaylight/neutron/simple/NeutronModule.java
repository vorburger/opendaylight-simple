/*
 * Copyright (c) 2017 Red Hat, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.neutron.simple;

import com.google.inject.AbstractModule;
import org.opendaylight.neutron.northbound.api.WebInitializer;
import org.opendaylight.neutron.spi.INeutronBgpvpnCRUD;
import org.opendaylight.neutron.spi.INeutronFirewallCRUD;
import org.opendaylight.neutron.spi.INeutronFirewallPolicyCRUD;
import org.opendaylight.neutron.spi.INeutronFirewallRuleCRUD;
import org.opendaylight.neutron.spi.INeutronFloatingIpCRUD;
import org.opendaylight.neutron.spi.INeutronL2gatewayCRUD;
import org.opendaylight.neutron.spi.INeutronL2gatewayConnectionCRUD;
import org.opendaylight.neutron.spi.INeutronLoadBalancerCRUD;
import org.opendaylight.neutron.spi.INeutronLoadBalancerHealthMonitorCRUD;
import org.opendaylight.neutron.spi.INeutronLoadBalancerListenerCRUD;
import org.opendaylight.neutron.spi.INeutronLoadBalancerPoolCRUD;
import org.opendaylight.neutron.spi.INeutronMeteringLabelCRUD;
import org.opendaylight.neutron.spi.INeutronMeteringLabelRuleCRUD;
import org.opendaylight.neutron.spi.INeutronNetworkCRUD;
import org.opendaylight.neutron.spi.INeutronPortCRUD;
import org.opendaylight.neutron.spi.INeutronQosPolicyCRUD;
import org.opendaylight.neutron.spi.INeutronRouterCRUD;
import org.opendaylight.neutron.spi.INeutronSFCFlowClassifierCRUD;
import org.opendaylight.neutron.spi.INeutronSFCPortChainCRUD;
import org.opendaylight.neutron.spi.INeutronSFCPortPairCRUD;
import org.opendaylight.neutron.spi.INeutronSFCPortPairGroupCRUD;
import org.opendaylight.neutron.spi.INeutronSecurityGroupCRUD;
import org.opendaylight.neutron.spi.INeutronSecurityRuleCRUD;
import org.opendaylight.neutron.spi.INeutronSubnetCRUD;
import org.opendaylight.neutron.spi.INeutronTapFlowCRUD;
import org.opendaylight.neutron.spi.INeutronTapServiceCRUD;
import org.opendaylight.neutron.spi.INeutronTrunkCRUD;
import org.opendaylight.neutron.spi.INeutronVpnIkePolicyCRUD;
import org.opendaylight.neutron.spi.INeutronVpnIpSecPolicyCRUD;
import org.opendaylight.neutron.spi.INeutronVpnIpSecSiteConnectionsCRUD;
import org.opendaylight.neutron.spi.INeutronVpnServiceCRUD;
import org.opendaylight.neutron.transcriber.NeutronBgpvpnInterface;
import org.opendaylight.neutron.transcriber.NeutronFirewallInterface;
import org.opendaylight.neutron.transcriber.NeutronFirewallPolicyInterface;
import org.opendaylight.neutron.transcriber.NeutronFirewallRuleInterface;
import org.opendaylight.neutron.transcriber.NeutronFloatingIpInterface;
import org.opendaylight.neutron.transcriber.NeutronL2gatewayConnectionInterface;
import org.opendaylight.neutron.transcriber.NeutronL2gatewayInterface;
import org.opendaylight.neutron.transcriber.NeutronLoadBalancerHealthMonitorInterface;
import org.opendaylight.neutron.transcriber.NeutronLoadBalancerInterface;
import org.opendaylight.neutron.transcriber.NeutronLoadBalancerListenerInterface;
import org.opendaylight.neutron.transcriber.NeutronLoadBalancerPoolInterface;
import org.opendaylight.neutron.transcriber.NeutronMeteringLabelInterface;
import org.opendaylight.neutron.transcriber.NeutronMeteringLabelRuleInterface;
import org.opendaylight.neutron.transcriber.NeutronNetworkInterface;
import org.opendaylight.neutron.transcriber.NeutronPortInterface;
import org.opendaylight.neutron.transcriber.NeutronQosPolicyInterface;
import org.opendaylight.neutron.transcriber.NeutronRouterInterface;
import org.opendaylight.neutron.transcriber.NeutronSFCFlowClassifierInterface;
import org.opendaylight.neutron.transcriber.NeutronSFCPortChainInterface;
import org.opendaylight.neutron.transcriber.NeutronSFCPortPairGroupInterface;
import org.opendaylight.neutron.transcriber.NeutronSFCPortPairInterface;
import org.opendaylight.neutron.transcriber.NeutronSecurityGroupInterface;
import org.opendaylight.neutron.transcriber.NeutronSecurityRuleInterface;
import org.opendaylight.neutron.transcriber.NeutronSubnetInterface;
import org.opendaylight.neutron.transcriber.NeutronTapFlowInterface;
import org.opendaylight.neutron.transcriber.NeutronTapServiceInterface;
import org.opendaylight.neutron.transcriber.NeutronTrunkInterface;
import org.opendaylight.neutron.transcriber.NeutronVpnIkePolicyInterface;
import org.opendaylight.neutron.transcriber.NeutronVpnIpSecPolicyInterface;
import org.opendaylight.neutron.transcriber.NeutronVpnIpSecSiteConnectionsInterface;
import org.opendaylight.neutron.transcriber.NeutronVpnServiceInterface;
import org.ops4j.pax.cdi.api.OsgiService;

/**
 * Guice module for Neutron.
 *
 * @author Michael Vorburger.ch
 */
public class NeutronModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(WebInitializer.class);

        // The following is currently copy/pasted from
        // org.opendaylight.neutron.e2etest.NeutronTestWiring
        // but likely will get replaced with automated classpath scanning anyway later
        // (strangely though it needs .annotatedWith(OsgiService.class) everywhere, only here)
        bind(INeutronNetworkCRUD.class).annotatedWith(OsgiService.class).to(NeutronNetworkInterface.class);
        bind(INeutronSubnetCRUD.class).annotatedWith(OsgiService.class).to(NeutronSubnetInterface.class);
        bind(INeutronPortCRUD.class).annotatedWith(OsgiService.class).to(NeutronPortInterface.class);
        bind(INeutronRouterCRUD.class).annotatedWith(OsgiService.class).to(NeutronRouterInterface.class);
        bind(INeutronFloatingIpCRUD.class).annotatedWith(OsgiService.class).to(NeutronFloatingIpInterface.class);
        bind(INeutronSecurityGroupCRUD.class).annotatedWith(OsgiService.class).to(NeutronSecurityGroupInterface.class);
        bind(INeutronSecurityRuleCRUD.class).annotatedWith(OsgiService.class).to(NeutronSecurityRuleInterface.class);
        bind(INeutronFirewallCRUD.class).annotatedWith(OsgiService.class).to(NeutronFirewallInterface.class);
        bind(INeutronFirewallPolicyCRUD.class).annotatedWith(OsgiService.class)
                .to(NeutronFirewallPolicyInterface.class);
        bind(INeutronFirewallRuleCRUD.class).annotatedWith(OsgiService.class).to(NeutronFirewallRuleInterface.class);
        bind(INeutronLoadBalancerCRUD.class).annotatedWith(OsgiService.class).to(NeutronLoadBalancerInterface.class);
        bind(INeutronLoadBalancerListenerCRUD.class).annotatedWith(OsgiService.class)
                .to(NeutronLoadBalancerListenerInterface.class);
        bind(INeutronLoadBalancerPoolCRUD.class).annotatedWith(OsgiService.class)
                .to(NeutronLoadBalancerPoolInterface.class);
        bind(INeutronBgpvpnCRUD.class).annotatedWith(OsgiService.class).to(NeutronBgpvpnInterface.class);
        bind(INeutronL2gatewayCRUD.class).annotatedWith(OsgiService.class).to(NeutronL2gatewayInterface.class);
        bind(INeutronL2gatewayConnectionCRUD.class).annotatedWith(OsgiService.class)
                .to(NeutronL2gatewayConnectionInterface.class);
        bind(INeutronLoadBalancerHealthMonitorCRUD.class).annotatedWith(OsgiService.class)
                .to(NeutronLoadBalancerHealthMonitorInterface.class);
        bind(INeutronMeteringLabelCRUD.class).annotatedWith(OsgiService.class).to(NeutronMeteringLabelInterface.class);
        bind(INeutronMeteringLabelRuleCRUD.class).annotatedWith(OsgiService.class)
                .to(NeutronMeteringLabelRuleInterface.class);
        bind(INeutronVpnServiceCRUD.class).annotatedWith(OsgiService.class).to(NeutronVpnServiceInterface.class);
        bind(INeutronVpnIkePolicyCRUD.class).annotatedWith(OsgiService.class).to(NeutronVpnIkePolicyInterface.class);
        bind(INeutronVpnIpSecPolicyCRUD.class).annotatedWith(OsgiService.class)
                .to(NeutronVpnIpSecPolicyInterface.class);
        bind(INeutronSFCFlowClassifierCRUD.class).annotatedWith(OsgiService.class)
                .to(NeutronSFCFlowClassifierInterface.class);
        bind(INeutronSFCPortChainCRUD.class).annotatedWith(OsgiService.class).to(NeutronSFCPortChainInterface.class);
        bind(INeutronSFCPortPairGroupCRUD.class).annotatedWith(OsgiService.class)
                .to(NeutronSFCPortPairGroupInterface.class);
        bind(INeutronSFCPortPairCRUD.class).annotatedWith(OsgiService.class).to(NeutronSFCPortPairInterface.class);
        bind(INeutronQosPolicyCRUD.class).annotatedWith(OsgiService.class).to(NeutronQosPolicyInterface.class);
        bind(INeutronTrunkCRUD.class).annotatedWith(OsgiService.class).to(NeutronTrunkInterface.class);
        bind(INeutronTapServiceCRUD.class).annotatedWith(OsgiService.class).to(NeutronTapServiceInterface.class);
        bind(INeutronTapFlowCRUD.class).annotatedWith(OsgiService.class).to(NeutronTapFlowInterface.class);
        bind(INeutronVpnIpSecSiteConnectionsCRUD.class).annotatedWith(OsgiService.class)
                .to(NeutronVpnIpSecSiteConnectionsInterface.class);
    }
}
