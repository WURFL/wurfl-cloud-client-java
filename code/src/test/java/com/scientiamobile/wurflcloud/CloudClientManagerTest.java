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

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Date: 03/08/11
 *
 */
@Test(groups = "connected")
public class CloudClientManagerTest extends Loggable {
	
	private ICloudClientManager ICloudClient;

    @BeforeClass
    public void setup() throws Exception {
        CloudClientLoader loader = new CloudClientLoader(null, "/CloudClientManagerTest.properties");
        ICloudClient = loader.getClientManager();
    }

    @Test
    public void testCallTest() {
        boolean b = ICloudClient.testCallWurflCloud();
        assertTrue(b);
    }

    @Test
    public void testValidateCache() {
    	// this test passes if the cache autopurge property is set to true
        boolean b = ICloudClient.validateCache();
        assertTrue(b);
    }

}
