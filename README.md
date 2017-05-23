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

1. Download and Install a Java JDK version 1.5 or greater
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
3. Add your WURFL Cloud API key to the following files in the `/code/src/test/resources` directory
    *CloudClientAcceptEncodingHeadersTest
    *CookieTest
    *EhCacheTest
    *HashMapCacheTest
    *CloudClientReadTimeoutTest
    *CloudClientManagerTest
    *InvalidCapability
    *InvalidApiKey
    *DefaultTest
3a. Alternatively you can run the following command if you have GNU `sed` installed `sed -i -- 's/XXXXXX:YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY/<Insert WURFL Cloud API Key>/g' *.properties`
4. Enter the `code` directory in the root of the project
5. Build the WURFL Cloud by typing `mvn clean install`

Build the sample project
----------------------------

1. Enter your local WURFL Cloud Client directory `cd wurfl-cloud-client-java`
2. Enter the examples directory `cd examples`
3. Add your WURFL Cloud API key to the following files:
    *`wurflcloud.properties` located at `wurfl-cloud-client-java/examples/src/main/resources`
    *`MyCloudClientServlet.java` located at `wurfl-cloud-client-java/examples/src/main/java/my/wurflcloud/example`
4. Build the example project by typing `mvn clean install`

Test the WURFL Cloud Client
---------------------------

Building the sample project in the previous step creates a .war file in the `wurfl-cloud-client-java/examples/target` directory. Deploy the .war file to Tomcat either by placing it in your Tomcat webapps folder or by uploading it to the Tomcat installation via the Tomcat Web Application Manager App (usually available at `http://localhost:8080/manager/html`).

Assuming you have installed Tomcat and all of the traditional defaults apply, you can access the Cloud Client from your browser at one of the following URLs: 

*Please Note that it may take a few minutes from signup for your WURFL Cloud API Key to become active.*

`http://localhost:8080/client-example-1.0.7/`

*Note: if your application server is not Tomcat or if you changed some of the defaults, you will need to adjust your URLs accordingly.*

Integration
-----------

You should review the included example code (`MyCloudClientServlet.java` and `response.jsp`) in the `wurfl-cloud-client-java/examples/` directory to get a feel for the Client API, and how best to use it in your application.

**2015 ScientiaMobile Incorporated**

**All Rights Reserved.**

**NOTICE**:  All information contained herein is, and remains the property of
ScientiaMobile Incorporated and its suppliers, if any.  The intellectual
and technical concepts contained herein are proprietary to ScientiaMobile
Incorporated and its suppliers and may be covered by U.S. and Foreign
Patents, patents in process, and are protected by trade secret or copyright
law. Dissemination of this information or reproduction of this material is
strictly forbidden unless prior written permission is obtained from 
ScientiaMobile Incorporated.
