
- [X] add genius.ItmWiring

- [X] quid CLI commands? tep:show-state and tep:show pointers, see https://github.com/vorburger/ch.vorburger.karaf.simple

- [X] fix broken ShellTestWiring so that it really finds commands that would not really work

- [X] real logging, see https://logging.apache.org/log4j/2.x/manual/configuration.html and try -Dlog4j2.debug=true

- [ ] CDS instead of in-memory DS: [CONTROLLER-1831](https://jira.opendaylight.org/browse/CONTROLLER-1831)

- [ ] RestConfWiring with Web API

- [ ] OpenFlowPlugin wiring

- [ ] packaging?  Swarm?

- [ ] ditch AAA and use Filter from Jetty for BASIC auth instead

- [ ] run genius CSIT

- [ ] create a Binding Generator (reflecting upon annotated classes)

- [ ] NeutronWiring with Web API (req. by netvirt, but not genius)

- [ ] do the same as this already did for genius for all of netvirt

- [ ] run netvirt CSIT

- [ ] clean up all problems found by duplicate-finder-maven-plugin upon "mvn verify" with <duplicate-finder.skip>false

- [ ] dependency convergence with Maven Enforcer plugin

- [ ] add all *Wiring+++ upstream, and make the project disappear (incl GuiceRule2 etc.)
