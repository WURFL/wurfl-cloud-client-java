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
import org.testng.annotations.Test;

import com.scientiamobile.wurflcloud.device.AbstractDevice;
import com.scientiamobile.wurflcloud.exc.WURFLCloudClientException;
import com.scientiamobile.wurflcloud.cache.WurflCloudCache_Null;
/**
 * @version $Id$
 */
@Test(groups = "unit")
public class CloudClientConnectionTimeoutTest extends Loggable{

    private static final String ua = "Mozilla/5.0 (iPad; CPU OS 7_0 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) CriOS/30.0.1599.12 Mobile/11A465 Safari/8536.25 (3B92C18B-D9DE-4CB7-A02A-22FD2AF17C8F)";

    private ICloudClientManager ICloudClient;
    private AbstractDevice device;
    private String[] capabilities;
    
    private CloudClientConfig cfg;
    private IAuthenticationManager am;
    private ICloudClientManager ccm;
    


    @BeforeClass
    public void setup() throws Exception {
        
        cfg = new CloudClientConfig();
        cfg.clearServers();
        cfg.addCloudServer("timeout_server", "8.8.8.8", 10);
        cfg.apiKey = "XXXXXX:YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY";
        cfg.connectionTimeout = 2000;
        am = new AuthenticationManager(cfg);
        ccm = new CloudClientManager(am, cfg, new WurflCloudCache_Null(), null);
        
    }

    @Test 
    public void testClient() {
        /**
         * Mantaining compatibility with Junit version < 4
         */
        boolean excThrown = false;
        try {
            device = ccm.getDeviceFromUserAgent(ua, capabilities);
        }
        catch (WURFLCloudClientException exc) {
            excThrown = true;
        }
        assertTrue(excThrown);
    }
}
