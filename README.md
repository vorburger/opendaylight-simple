# opendaylight-simple [![Build Status](https://travis-ci.org/vorburger/opendaylight-simple.svg?branch=master)](https://travis-ci.org/vorburger/opendaylight-simple)

Run a typical OpenDaylight SDN application such as netvirt without requiring the Apache Karaf OSGi runtime container.

See this [presentation given in 2018.09 at the ODL DDF during ONS Europe in Amsterdam](https://docs.google.com/presentation/d/14TM9oCn0nLo7RJAhAHglXM4P6oTxsVjJBfFE1wl1qJc) for some background.

Current status: Work in progress feasibility study to better evaluate required effort and gaps.

Related "tech debt" clean up changes that are a result of this investigation
[are slowly trickling into ODL](https://git.opendaylight.org/gerrit/#/q/topic:simple-dist) (and [web](https://git.opendaylight.org/gerrit/#/q/topic:simple-dist_web)).

The goal of this project is to eventually upstream all work done here into EXISTING OpenDaylight projets, and eventually have 0 code left in this repo.
We do not anticipate this to become a new ODL (or external) project.

This includes support for custom Karaf CLI Commands, based on the [ch.vorburger.karaf.simple](https://github.com/vorburger/ch.vorburger.karaf.simple) POC.

## How to use in Dev

 One of the main advantages of developing with opendaylight-simple is that you DO NOT NEED TO build a package on the CLI to start it, like we are used to in ODL with Karaf.  It's just simple standalone Java, so you just Run e.g. `GeniusMain` or `OpenFlowPluginWiringTest` & Co. directly in your favourite IDE!

 The build (`maven-assembly-plugin`) is supringly slow, and you would typically only use it to package and run in production on a remote server.  Locally, in development, you do not have to leave your IDE anymore.  The running code is, of course, identical - there is no OSGi/Karaf runtime "container" anymore now.

## How to use in Prod

    mvn -s .travis-maven-settings.xml clean package
    cd target/poc-1.0.0-SNAPSHOT-simple/poc-1.0.0-SNAPSHOT
    java -cp "etc/initial/*:lib/*" org.opendaylight.genius.simple.GeniusMain

You'll also find a `poc-*-simple.tar` in `target/` which contains `lib/*`.

Configuration files, like e.g. the `serviceutils-upgrade-config.xml`, can be changed in the `etc/initial` directory (which is on the beginning of the classpath), where they are copied to on the first run.

The use of the custom `.travis-maven-settings.xml` is required due to [issue TBD](https://github.com/vorburger/opendaylight-simple/issues/37).
