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

import com.scientiamobile.wurflcloud.device.AbstractDevice;
import com.scientiamobile.wurflcloud.utils.Constants;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;

import static org.mockito.Mockito.*;


/**
 * Date: 20/07/11
 *
 * @version $Id$
 */
@Test(groups = "unit")
public class CloudClientAcceptEncodingHeadersTest extends Loggable{

    private static final String ua = "Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; Touch; rv:11.0) like Gecko";

    private ICloudClientManager ICloudClient;
    private AbstractDevice device;
    private String[] capabilities;
    private long time;
    private long start;
    private String mobile;


    @BeforeClass
    public void setup() throws Exception {
        CloudClientLoader loader = new CloudClientLoader(null, "/CloudClientAcceptEncodingHeadersTest.properties");
        ICloudClient = loader.getClientManager();
    }

    @BeforeMethod
    public void setupDevice() {
        start = System.currentTimeMillis();

        Enumeration<String> e = new Enumeration<String>() {
            private boolean set = false;

            public boolean hasMoreElements() {
                return !set;
            }

            public String nextElement() {
                if (!set) {
                    set = true;
                    return Constants.USER_AGENT_LC;
                }
                throw new IllegalStateException("finished objects");
            }
        };

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeaderNames()).thenReturn(e);
        when(request.getHeader(Constants.USER_AGENT_LC)).thenReturn(ua);

        HttpServletResponse response = mock(HttpServletResponse.class);

        device = ICloudClient.getDeviceFromRequest(request, response, capabilities);
        
    }

    @Test
    public void testClient() {

    	String cap_value = device.get("form_factor").toString();
    	boolean passed = (cap_value.compareToIgnoreCase("Robot") != 0);
    	assertTrue(passed);
    }
}
