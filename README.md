# Java EE starter project
Project is based on *Maven*, thus import project to IntelliJ IDEA by:
* File -> Open... -> pick `pom.xml` file.

## Application Server configuration

### TomEE (Apache)
Short version:
1. Download WebProfile, ZIP from: [http://tomee.apache.org/downloads.html](http://tomee.apache.org/downloads.html)
2. Unzip
4. In IntelliJ IDEA: register "TomEE Server" -> local:
    * Press "Fix", choose "exploded war" as artifact
5. Run the server, project should start successfully.

Long version: [TomEE and IntelliJ IDEA](http://tomee.apache.org/tomee-and-intellij.html)

### Payara
1. Download WebProfile from: [http://www.payara.fish/all_downloads](http://www.payara.fish/all_downloads)
2. Unzip
3. In IntelliJ IDEA: register "GlassFish Server" -> local:
    * Choose "Server Domain": **payaradomain**
    * Press "Fix", choose "exploded war"
    * Press "Fix" again to update debugger settings
4. Run the server, project should start successfully.

### WildFly (RedHat)
1. Download ZIP "Java EE7 Full & Web Distribution" from: [http://wildfly.org/downloads/]
2. Unzip
3. In IntelliJ IDEA: register "JBoss Server" -> local:
    * Press "Fix", choose "exploded war"
4. Run the server, project should start successfully.

### WAS Liberty (IBM)
1. Download and unzip: [WAS Liberty](https://developer.ibm.com/wasdev/downloads/#asset/runtimes-wlp-webProfile7)
2. Run: `wlp/bin/server create`
3. In IntelliJ IDEA: register "WebSphere Server" -> local:
    * Press "Fix" to enable JMX
    * Press "Fix" again, choose "exploded war" as artifact
    * In tab "Deployment", for "Use custom context root" enter: **JavaEEstarter**
4. Run the server for the first time (it will fail - that is OK), then stop the server.
5. Open this file with text editor: `wlp/usr/servers/defaultServer/server.xml`, 
   add `classloader` and `library` tags:
```
    ...
    <application id="JavaEEstarter_war_exploded" ...>
        <classloader commonLibraryRef="h2" />
    </application>
    <library id="h2">
        <fileset dir="${shared.resource.dir}/h2" includes="*.jar" />
    </library>
</server>
```
6. Copy file `JavaEEstarter/target/JavaEEstarter-1.0-SNAPSHOT/WEB-INF/lib/h2-1.4.193.jar`
   to directory `wlp/usr/shared/resources/h2`
7. Run the server again: project should work fine now.