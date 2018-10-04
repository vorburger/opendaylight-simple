# opendaylight-simple

Run a typical OpenDaylight SDN application such as netvirt without requiring the Apache Karaf OSGi runtime container.

See this [presentation given in 2018.09 at the ODL DDF during ONS Europe in Amsterdam](https://docs.google.com/presentation/d/14TM9oCn0nLo7RJAhAHglXM4P6oTxsVjJBfFE1wl1qJc) for some background.

Current status: Work in progress feasibility study to better evaluate required effort and gaps.

Related "tech debt" clean up changes that are a result of this investigation
[are slowly trickling into ODL](https://git.opendaylight.org/gerrit/#/q/topic:simple-dist) (and [web](https://git.opendaylight.org/gerrit/#/q/topic:simple-dist_web)).

The goal of this project is to eventually upstream all work done here into EXISTING OpenDaylight projets, and eventually have 0 code left in this repo.
We do not anticipate this to become a new ODL (or external) project.

This includes support for custom Karaf CLI Commands, based on the [ch.vorburger.karaf.simple](https://github.com/vorburger/ch.vorburger.karaf.simple) POC.

## How to use

    mvn clean package
    cd target/poc-1.5.0-SNAPSHOT-simple/poc-1.5.0-SNAPSHOT
    java -cp "lib/*" org.opendaylight.genius.simple.GeniusMain

You'll also find a `poc-*-simple.tar` in `target/` which contains `lib/*`.
