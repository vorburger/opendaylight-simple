
- [X] add genius.ItmWiring

- [X] quid CLI commands? tep:show-state and tep:show pointers, see https://github.com/vorburger/ch.vorburger.karaf.simple

- [X] fix broken ShellTestWiring so that it really finds commands that would not really work

- [X] real logging, see https://logging.apache.org/log4j/2.x/manual/configuration.html and try -Dlog4j2.debug=true

- [X] quit/shutdown shell

- [X] working deployment

- [X] working /diagstatus and showSvcStatus (#39) [waiting for infrautils merges]

- [X] RestConfWiring with Web API

- [ ] make RestConfConfig readable from YAML using http://immutables.github.io/json.html and update @ConfigImmutableStyle for it, then upstream to infrautils

- [ ] ditch AAA and use Filter from Jetty for BASIC auth instead

- [X] NeutronModule with Web API (req. by netvirt, but not genius)

- [ ] ovsdb https://github.com/vorburger/opendaylight-simple/issues/47

- [ ] CDS instead of in-memory DS: [CONTROLLER-1831](https://jira.opendaylight.org/browse/CONTROLLER-1831)

- [X] OpenFlowPlugin wiring ConfigurationServiceFactoryImpl (OPNFLWPLUG-1037)
- [X] OpenFlowPluginWiring PacketProcessingService <odl:action-provider>
- [X] OpenFlowPlugin wiring

- [ ] add a Dockerfile to build a demo container
- [ ] create a Hello World toaster example
- [ ] kubectl apply YAML to get started

- [X] skitt's https://github.com/vorburger/opendaylight-simple/issues/38
- [ ] DiagStatusWiring auto-discover ServiceStatusProvider
- [ ] CLI commands, such as ItmWiring's TepShowState and DiagStatusCommand in DiagStatusWiring, which `implements Action`, should be auto-discovered

- [X] packaging
- [ ] https://github.com/vorburger/opendaylight-simple/issues/37 including:
- [ ] why does maven-assembly-plugin dependencySet attempt to download from so many other Maven repos than only nexus.opendaylight.org
- [ ] why does maven-assembly-plugin dependencySet attempt to download so many additional unsed dependencies, like Maven plugins's
- [ ] why does maven-assembly-plugin dependencySet attempt to constantly re-download all SNAPSHOT unless we use -nsu -o
- [ ] MINOR: why does "java -jar lib/ch.vorburger.opendaylight.simple.poc-1.5.0-SNAPSHOT.jar" not work?  (MANIFEST.MF has all lib/*)

- [X] [re-implement ClassPathScanner](https://github.com/vorburger/opendaylight-simple/pull/18#issuecomment-426859615) using [ClassGraph](https://github.com/classgraph/classgraph) used in [INFRAUTILS-52](https://jira.opendaylight.org/browse/INFRAUTILS-52) (and rename it to something more appropriate)

- [X] read YANG XML configuration files using [DataStoreAppConfigDefaultXMLReader](https://git.opendaylight.org/gerrit/#/c/76416/3/opendaylight/blueprint/src/test/java/org/opendaylight/controller/blueprint/tests/DataStoreAppConfigDefaultXMLReaderTest.java)

- [ ] run genius CSIT

- [ ] do the same as this already did for genius for all of netvirt

- [ ] run netvirt CSIT

- [ ] clean up all problems found by duplicate-finder-maven-plugin upon "mvn verify" with <duplicate-finder.skip>false

- [ ] dependency convergence with Maven Enforcer plugin

- [ ] add all *Wiring+++ upstream, and make the project disappear

- [ ] headless distribution without shell

- [ ] Non Karaf shell? E.g. https://github.com/Mojang/brigadier, https://www.crashub.org (why deprecated in Spring?), ...

- [ ] introduce new Maven profiles suitable for the simple new world in odlparent: `-Ps` to skip running `karaf-maven-plugin`, `SingleFeatureTest`,  `karaf-plugin`, `depends-maven-plugin`, ...

- [ ] upstream everything that is in this project, and have a netvirt/distribution/simple (next to netvirt/distribution/karaf)

- [ ] Archive this project
