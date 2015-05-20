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

import static org.testng.Assert.assertFalse;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Date: 06/05/15
 *
 * @author andrea.galasso@codemachine.it
 *          $Id$
 */
@Test(groups = "connected")
public class CloudClientInvalidApiKeyTest extends Loggable {
	
	private ICloudClientManager manager;
	
    @BeforeClass
    public void setup() throws Exception {
        CloudClientLoader loader = new CloudClientLoader(null, "/InvalidApiKey.properties");
        manager = loader.getClientManager();
    }

    @Test
    public void testApiKeyFailure() {
    	boolean res = manager.testCallWurflCloud();
    	assertFalse(res);
    }

}
