/**
 * Copyright (c) 2015 ScientiaMobile Inc.
 *
 * The WURFL Cloud Client is intended to be used in both open-source and
 * commercial environments. To allow its use in as many situations as possible,
 * the WURFL Cloud Client is dual-licensed. You may choose to use the WURFL
 * Cloud Client under either the GNU GENERAL PUBLIC LICENSE, Version 2.0, or
 * the MIT License.
 *
 * Refer to the COPYING.txt file distributed with this package.
 */
package com.scientiamobile.wurflcloud;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.scientiamobile.wurflcloud.device.AbstractDevice;

/**
 * Date: 20/07/11
 *
 * @version $Id$
 */
@Test(groups = "unit")
public class CloudClientUserAgentQueryTest extends Loggable{

    private static final String ua = "Mozilla/5.0 (iPad; CPU OS 7_0 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) CriOS/30.0.1599.12 Mobile/11A465 Safari/8536.25 (3B92C18B-D9DE-4CB7-A02A-22FD2AF17C8F)";

    private ICloudClientManager ICloudClient;
    private AbstractDevice device;
    private String[] capabilities;
    private String mobile;


    @BeforeClass
    public void setup() throws Exception {
        CloudClientLoader loader = new CloudClientLoader(null, "/DefaultTest.properties");
        ICloudClient = loader.getClientManager();
    }

    @BeforeMethod
    public void setupDevice() {
        device = ICloudClient.getDeviceFromUserAgent(ua, capabilities);
        
        Object mobile = device.get("is_mobile");
        this.mobile = mobile != null ? mobile.toString() : "unknown";
    }

    @Test
    public void testClient() {
        assertTrue(mobile.equals("true"));
    }
}
