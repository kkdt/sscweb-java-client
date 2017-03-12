> Original project exported from a personal subversion server into a git
> repository, and pushed to Github. This is intended to be "notes to myself"
> because I have trouble remembering what I did the previous day...

# Satellite Situation Center 2.0 Web Services
Gradle project that pulls the WSDL from the [Satellite Situation Center](https://sscweb.sci.gsfc.nasa.gov/WebServices/SOAP) interface and builds the necessary client stubs to access the web service. The built artifact is a Jar file containing all the generated JAX-WS artifacts.

The WSDL location is `https://sscweb.gsfc.nasa.gov/WS/ssc/2/SatelliteSituationCenterService?wsdl`.

## Gradle Build
```javascript
------------------------------------------------------------
Gradle 3.3
------------------------------------------------------------

Build time:   2017-01-03 15:31:04 UTC
Revision:     075893a3d0798c0c1f322899b41ceca82e4e134b

Groovy:       2.4.7
Ant:          Apache Ant(TM) version 1.9.6 compiled on June 29 2015
JVM:          1.8.0_112 (Oracle Corporation 25.112-b16)
OS:           Mac OS X 10.12.3 x86_64
```

The Gradle build is very simple and highlights

1. Gradle custom `configuration` to pull down all the dependencies for jaxws
2. Invoking Ant through Gradle via a `taskdef` tag that essentially executes `com.sun.tools.ws.ant.WsImport`
3. Allow the Ant wsimport task to be a dependency of the built-in `compileJava` target defined in the Gradle [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html)

### Gradle Plugins

1. [Java Plugin](https://docs.gradle.org/current/userguide/java_plugin.html)
2. [Eclipse Plugin](https://docs.gradle.org/current/userguide/eclipse_plugin.html)
3. [Maven Plugin](https://docs.gradle.org/current/userguide/maven_plugin.html)

```javascript
apply plugin: 'java'
apply plugin: 'eclipse'
apply plugin: 'maven'
```

### Useful Gradle Commands

`gradle cleanAll` Clean all generated artifacts

`gradle cleanEclipse eclipse` Generated all Eclipse artifacts

`gradle install` Install to local Maven repository
