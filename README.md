# opendaylight-simple [![Build Status](https://travis-ci.org/vorburger/opendaylight-simple.svg?branch=master)](https://travis-ci.org/vorburger/opendaylight-simple)

Run a typical OpenDaylight SDN application such as CoE or netvirt without requiring the Apache Karaf OSGi runtime container.

See this [presentation given in 2018.09 at the ODL DDF during ONS Europe in Amsterdam](https://docs.google.com/presentation/d/14TM9oCn0nLo7RJAhAHglXM4P6oTxsVjJBfFE1wl1qJc) for some background.

Current status: Work in progress feasibility study to better evaluate required effort and gaps.

Related "tech debt" clean up changes that are a result of this investigation
[are slowly trickling into ODL](https://git.opendaylight.org/gerrit/#/q/topic:simple-dist) (and [web](https://git.opendaylight.org/gerrit/#/q/topic:simple-dist_web)).

The goal of this project is to eventually upstream all work done here into EXISTING OpenDaylight projects, and eventually have 0 code left in this repo.
We do not anticipate this to become a new ODL (or external) project.

We currently use [Guice](https://github.com/google/guice) as dependency injection (DI) framework, instead the OSGi Blueprint (BP) implementation Apache Aries which is used in Karaf.  This choice was made just because Guide was already used for "Component Tests" in ODL, and [infrautils has some useful related helpers](https://wiki.opendaylight.org/view/BestPractices/Component_Tests).  ODL projects' code is not made to depend on Guice.  We simply re-use declarative DI by `javax.inject` standard `@Singleton` & `@Inject` annotations, which [work for both Blueprint (with the blueprint-maven-plugin)](https://wiki.opendaylight.org/view/BestPractices/DI_Guidelines) as well as Guice.  ODL projects are encouraged to migrate their use of `<bean>`, `<reference>` and `<service>` elements in BP XML to annotations to ease this integration.  When we exceptionally need to "stitch together" objects in a non-trivial fashion (e.g. custom object factories & external configuration), we migrate BP XML logic into simple `*Wiring` classes.  _Wiring_ classes are not Guice specific, but pure plain old simple Java.  We then write small `*Module` classes, which are Guice specific, to tie everything together.

This includes support for custom Karaf CLI Commands, based on the [ch.vorburger.karaf.simple](https://github.com/vorburger/ch.vorburger.karaf.simple) POC.

## How to use in Dev

 One of the main advantages of developing with opendaylight-simple is that you DO NOT NEED TO build a package on the CLI to start it, like we are used to in ODL with Karaf.  It's just simple standalone Java, so you just Run e.g. `GeniusMain` or `OpenFlowPluginWiringTest` & Co. directly in your favourite IDE!

 The build (`maven-assembly-plugin`) is surprisingly slow, and you would typically only use it to package and run in production on a remote server.  Locally, in development, you do not have to leave your IDE anymore.  The running code is, of course, identical - there is no OSGi/Karaf runtime "container" anymore now.

## How to use in Prod

    mvn -s .travis-maven-settings.xml clean package
    cd target/poc-1.0.0-SNAPSHOT-simple/poc-1.0.0-SNAPSHOT
    java -cp "etc/initial/*:lib/*" org.opendaylight.genius.simple.GeniusMain

You'll also find a `poc-*-simple.tar` in `target/` which contains `lib/*`.

Configuration files, like e.g. the `serviceutils-upgrade-config.xml`, can be changed in the `etc/initial` directory (which is on the beginning of the classpath), where they are copied to on the first run.

The use of the custom `.travis-maven-settings.xml` is required due to [issue TBD](https://github.com/vorburger/opendaylight-simple/issues/37).

## FAQ

* _Q: Is this goal of this project to get rid of the use of Apache Karaf in ODL?_ A: Yes, this project explores how to run ODL without Karaf.  It illustrates this simply by building another distribution in parallel, see above.  It's not necessarily a goal of this project to completely remove Karaf of all of OpenDaylight - that decision can be taken separately and in parallel by those needing distributions - perhaps some will still see any benefit in Karaf, while others won't anymore.
