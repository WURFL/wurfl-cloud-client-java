# ScientiaMobile WURFL Cloud Client for Java

The WURFL Cloud Service by ScientiaMobile, Inc., is a cloud-based
mobile device detection service that can quickly and accurately
detect over 500 capabilities of visiting devices.  It can differentiate
between portable mobile devices, desktop devices, SmartTVs and any 
other types of devices that have a web browser.

This is the Java Client for accessing the WURFL Cloud Service, and
it requires a free or paid WURFL Cloud account from ScientiaMobile:
http://www.scientiamobile.com/cloud 

Installation
------------

First, you must go to http://www.scientiamobile.com/cloud and signup
for a free or paid WURFL Cloud account (see above).  When you've finished
creating your account, you must copy your API Key, as it will be needed in
the Client.

Setup a Development Environment
-------------------------------

1. Download and Install a Java JDK version 7 or greater (for compatibility with older Java versions, please use client releases up to 1.0.10)
2. Download and Install Apache Tomcat version 6 or greater
3. Download and Install Apache Maven (Tested with version 3.5)
4. Create a `JAVA_HOME` Environment Variable which points to the location of your
Java JDK installation (Example:`C:\Program Files\Java\jdk1.8.0_31`).
5. Create `MAVEN_HOME` and `M2_HOME` Environment Variables that point to the location of your
Maven installation (Example: `C:\Program Files\Apache Software Foundation\Maven`). Add `%M2_HOME%\bin` to your
`PATH` System Variable.

*Note for Windows users: If you are setting your environment variables 
using a terminal window (`cmd.exe`), you must quit and restart the Command Prompt
utility for the environment variables you have defined to be recognized.*

Build the WURFL Cloud Client
----------------------------

1. Clone the WURFL Cloud Client repo locally `git clone https://github.com/WURFL/wurfl-cloud-client-java.git`
2. Enter the directory `cd wurfl-cloud-client-java` 
3. Add your WURFL Cloud API key to the following files in the `code/src/test/resources` directory
    * CloudClientAcceptEncodingHeadersTest.properties
    * CookieTest.properties
    * EhCacheTest.properties
    * HashMapCacheTest.properties
    * CloudClientReadTimeoutTest.properties
    * CloudClientManagerTest.properties
    * InvalidCapability.properties
    * InvalidApiKey.properties
    * DefaultTest.properties
4. Enter the `code` directory in the root of the project
5. Build the WURFL Cloud by typing `mvn clean install`

Build the sample project
----------------------------

1. Enter your local WURFL Cloud Client directory `cd wurfl-cloud-client-java`
2. Enter the examples directory `cd examples`
3. Add your WURFL Cloud API key to the following files:
    * `wurflcloud.properties` located at `wurfl-cloud-client-java/examples/src/main/resources`
    * `MyCloudClientServlet.java` located at `wurfl-cloud-client-java/examples/src/main/java/my/wurflcloud/example`
4. Build the example project by typing `mvn clean install`

Test the WURFL Cloud Client
---------------------------

Building the sample project in the previous step creates a .war file in the `wurfl-cloud-client-java/examples/target` directory. Deploy the .war file to Tomcat either by placing it in your Tomcat webapps folder or by uploading it to the Tomcat installation via the Tomcat Web Application Manager App (usually available at `http://localhost:8080/manager/html`).

Assuming you have installed Tomcat and all of the traditional defaults apply, you can access the Cloud Client from your browser at one of the following URLs: 

*Please Note that it may take a few minutes from signup for your WURFL Cloud API Key to become active.*

`http://localhost:8080/client-example-1.0.15/`

*Note: if your application server is not Tomcat or if you changed some of the defaults, you will need to adjust your URLs accordingly.*

Integration
-----------

You should review the included example code (`MyCloudClientServlet.java` and `response.jsp`) in the `wurfl-cloud-client-java/examples/` directory to get a feel for the Client API, and how best to use it in your application.

Migrating to Jakarta EE9 (Tomcat 10 and other new servers)
-----------
With Jakarta EE 9, the enterprise Java application ecosystem has faced a huge change. The most impacting one is the naming change from the Oracle owned `javax.*` 
package namespace to the new `jakarta.*`
This naming change will break most of the code that, for example, uses class `HttpServletRequest` or others from the same packages.
So if you want to use the `wurfl-cloud-client-java` in a web/application server that supports Jakarta EE9 such as:
  - Tomcat 10.x
  - Glassfish 6.x
  - Jetty 11.x
  or any other that will comply to this spec in the near future, you will need to migrate both the cloud client and the sample application (or yours, if needed) and build them from the source code.
    
### Step 1: use the Tomcat migration tool
If you use Tomcat 10 you can migrate part of the code by using the Tomcat Migration tool: https://tomcat.apache.org/download-migration.cgi
This tool replaces all the projects javax.* occurrences with jakarta.*. There are different option that you can specify depending on how your application uses the 
Java Enterprise features. Execute the `migrate.sh` script without parameters for more info on the usage options.

The command `./migrate.sh <path/to/wurfl-cloud-client-java> <path/to/destination dir> -profile=EE` will generate a "migrated" copy of the project code using any javax.* package to
the specified destination directory.

### Step 2: check the pom.xml dependencies
The migration tool tries to update the dependencies in your pom.xml files, but it's not perfect in that at the current development stage.
This means you have to check the EE dependencies yourself.

These are the dependencies used in the java cloud client example app to make it compliant with Jakarta EE 9 on Tomcat 10.

```xml
<dependencies>
        <dependency>
            <groupId>jakarta.servlet</groupId>
            <artifactId>jakarta.servlet-api</artifactId>
            <version>5.0.0</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>jakarta.servlet.jsp</groupId>
            <artifactId>jakarta.servlet.jsp-api</artifactId>
            <version>3.0.0</version>
            <scope>provided</scope>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.glassfish.web/jakarta.servlet.jsp.jstl -->
        <dependency>
            <groupId>org.glassfish.web</groupId>
            <artifactId>jakarta.servlet.jsp.jstl</artifactId>
            <version>2.0.0</version>
        </dependency>

        <dependency>
            <groupId>jakarta.servlet.jsp.jstl</groupId>
            <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
            <version>2.0.0</version>
        </dependency>
    </dependencies>
```

The project branch on : `https://github.com/WURFL/wurfl-cloud-client-java/tree/1.0.15-jakarta-migration`
contains an already migrated version of the cloud-client-java and the example application projects.
Build them as previously shown. The migrated project build and execution has been tested using this environment:

- JDK 16
- Maven 3.8.1
- Tomcat 10.0.6

### Step 3: updating other dependencies or code (if needed).
If you are migrating your custom project to Jakarta EE9, the project build may fail because some other dependency has not been updated, or its interface has changed. Fix the dependency or code and retry building you project.


-----------

**2015-2021 ScientiaMobile Incorporated**

**All Rights Reserved.**

**NOTICE**:  All information contained herein is, and remains the property of
ScientiaMobile Incorporated and its suppliers, if any.  The intellectual
and technical concepts contained herein are proprietary to ScientiaMobile
Incorporated and its suppliers and may be covered by U.S. and Foreign
Patents, patents in process, and are protected by trade secret or copyright
law. Dissemination of this information or reproduction of this material is
strictly forbidden unless prior written permission is obtained from 
ScientiaMobile Incorporated.
