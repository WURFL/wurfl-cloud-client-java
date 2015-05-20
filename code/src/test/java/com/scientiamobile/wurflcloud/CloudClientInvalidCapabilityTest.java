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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.scientiamobile.wurflcloud.utils.Constants;


/**
 * @version $Id$
 */
@Test(groups = "unit")
public class CloudClientInvalidCapabilityTest extends Loggable{

    private static final String ua = "Mozilla/5.0 (BlackBerry; U; BlackBerry 9800; en-US) AppleWebKit/534.8+ (KHTML, like Gecko) Version/6.0.0.466 Mobile Safari/534.8+";

    private ICloudClientManager ICloudClient;
    private String[] capabilities;


    @BeforeClass
    public void setup() throws Exception {
        CloudClientLoader loader = new CloudClientLoader(null, "/InvalidCapability.properties");
        ICloudClient = loader.getClientManager();
    }

    @Test
    public void invalidCapabilityQuery() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(Constants.USER_AGENT_LC)).thenReturn(ua);

        HttpServletResponse response = mock(HttpServletResponse.class);
        boolean passed = false;
        try {
            ICloudClient.getDeviceFromRequest(request, response, capabilities);
		} catch (IllegalArgumentException e) {
			passed = true;
		}
        
        Assert.assertTrue(passed);
    }
}
