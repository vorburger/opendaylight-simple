
- [X] add genius.ItmWiring

- [X] quid CLI commands? tep:show-state and tep:show pointers, see https://github.com/vorburger/ch.vorburger.karaf.simple

- [X] fix broken ShellTestWiring so that it really finds commands that would not really work

- [X] real logging, see https://logging.apache.org/log4j/2.x/manual/configuration.html and try -Dlog4j2.debug=true

- [X] quit/shutdown shell

- [ ] CDS instead of in-memory DS: [CONTROLLER-1831](https://jira.opendaylight.org/browse/CONTROLLER-1831)

- [ ] RestConfWiring with Web API

- [ ] OpenFlowPlugin wiring

- [X] packaging
- [ ] why does maven-assembly-plugin dependencySet attempt to download from so many other Maven repos than only nexus.opendaylight.org
- [ ] why does maven-assembly-plugin dependencySet attempt to download so many additional unsed dependencies, like Maven plugins's
- [ ] why does maven-assembly-plugin dependencySet attempt to constantly re-download all SNAPSHOT unless we use -nsu -o
- [ ] MINOR: why does "java -jar lib/ch.vorburger.opendaylight.simple.poc-1.5.0-SNAPSHOT.jar" not work?  (MANIFEST.MF has all lib/*)

- [ ] ditch AAA and use Filter from Jetty for BASIC auth instead

- [ ] read YANG XML configuration files using [DataStoreAppConfigDefaultXMLReader](https://git.opendaylight.org/gerrit/#/c/76416/3/opendaylight/blueprint/src/test/java/org/opendaylight/controller/blueprint/tests/DataStoreAppConfigDefaultXMLReaderTest.java)

- [ ] run genius CSIT

- [ ] create a Binding Generator (reflecting upon annotated classes)

- [ ] NeutronWiring with Web API (req. by netvirt, but not genius)

- [ ] do the same as this already did for genius for all of netvirt

- [ ] run netvirt CSIT

- [ ] clean up all problems found by duplicate-finder-maven-plugin upon "mvn verify" with <duplicate-finder.skip>false

- [ ] dependency convergence with Maven Enforcer plugin

- [ ] add all *Wiring+++ upstream, and make the project disappear (incl GuiceRule2 etc.)

- [ ] headless distribution without shell
