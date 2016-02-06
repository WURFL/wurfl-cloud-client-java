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

* Download and Install a Java JDK version 1.5 or greater
* Download and Install Apache Tomcat version 6 or greater
* Download and Install Apache Ant version 1.8 or greater

Create a `JAVA_HOME` Environment Variable which points to the location of your
Java JDK installation (`eq: C:\Program Files (x86)\Java \jdk1.6.0_31`).

Create a `ANT_HOME` Environment Variable which points to the location of your
Ant installation (`eq: C:\apache-ant-1.8.3`). Add `%ANT_HOME%/bin` to your
`PATH` System Variable.

*Note for Windows users: If you are setting your environment variables 
using a terminal window (`cmd.exe`), you must quit and restart the Command Prompt
utility for the environment variables you have defined to be recognized.*

Build a Sample Client
---------------------

To build and test the sample code you will first need to edit two files which
came with the WURFL Cloud Client.

Locate the `MyCloudClientServlet.java` file and open it with a text editor.

Change the value of the `TEST_API_KEY` variable to your API Key. You can retrieve
your API Key in your Scientiamobile account. Save and close the file.

Locate the `build.properties` file and open it with a text editor. Set the value of
`tomcat.home` to the location of your tomcat installation and other settings as
explained. Save and close the file. 

*Note: Please observe that this step assumes that Tomcat is configured
to 'talk' to other applications. Tomcat must be running and you must have
configured an user with 'manager-script' role. For this step, please read
how-to in `<tomcat-home>/conf/tomcat-users.xml`. If this is not the case,
you can skip this step and deploy/copy the '.war' (more later) following
the procedure that you normally follow to install web applications on your
Java application server.*

If your Tomcat installation is accessible from Ant, the `ant install`
command will deploy the cloud client automatically.

*Warning: manager paths are slightly different between [Tomcat 6](http://tomcat.apache.org/tomcat-6.0-doc/manager-howto.html#Executing%20Manager%20Commands%20With%20Ant) and [Tomcat 7](http://tomcat.apache.org/tomcat-7.0-doc/manager-howto.html#Executing%20Manager%20Commands%20With%20Ant).*

Open a terminal window and change to the WURFL Cloud Client directory. Type
`ant` and hit enter. This builds a `.war` file in `dist` folder.

Assuming you are using Tomcat, copy the '.war' file in the dist directory to
the  tomcat webapps directory. For other application servers, simply follow
the normal procedure to deploy `.war` files.

If not already started, start Tomcat.

Test the WURFL Cloud Client
---------------------------

Assuming you have installed Tomcat and all of the traditional defaults apply,
you can access the Cloud Client from your browser at one of the following URLs: 

*Please Note that it may take a few minutes from the time that you signup
for your WURFL Cloud API Key to become active.*

`http://localhost:8080/cloud-client-example-1.0/`

(or, if you have hot-deployed with ant)

`http://localhost:8080/cloud-client-example)`

*Note: if your application server is not tomcat or you changed some of the
defaults, you will need to adjust your URLs accordingly.*

Integration
-----------

You should review the included example code (`MyCloudClientServlet.java` and `response.jsp`)
to get a feel for the Client API, and how best to use it in your application.

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
