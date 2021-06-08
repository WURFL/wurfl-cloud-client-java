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

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.testng.Assert.assertEquals;



/**
 * @version $Id$
 */
@Test(groups = "unit")
public class CloudClientHashMapCacheTest extends Loggable {

    static final String uaFirstRun = "Mzilla/5.0 (BlackBerry; U; BlackBerry 9800; en-US) AppleWebKit/534.8+ (KHTML, like Gecko) Version/6.0.0.466 Mobile Safari/534.8+";
    static final String uaSecondRun = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

    private ICloudClientManager ICloudClient;
    private AbstractDevice device;
    private String[] capabilities;
    private Enumeration<String> e1, e2, e3;
    private HttpServletRequest request1, request2, request3;
    private HttpServletResponse response;


    @BeforeClass
    public void setup() throws Exception {
        CloudClientLoader loader = new CloudClientLoader(null, "/HashMapCacheTest.properties");
        ICloudClient = loader.getClientManager();
        capabilities = loader.getSearchedCapabilities();
    }

    @BeforeMethod
    public void setupMethod() {
    	
    	/*
    	 * We need an enumerator for each mocked request because we can't reset it to its first
    	 * element after "consuming" it
    	 * 
    	 * So we have 
    	 * 	e1 for request1
    	 *  e2 for request2
    	 *  e3 for request3
    	 */

        e1 = new Enumeration<String>() {
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
        
        e2 = new Enumeration<String>() {
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

        e3 = new Enumeration<String>() {
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

        request1 = mock(HttpServletRequest.class);
        when(request1.getHeaderNames()).thenReturn(e1);
        when(request1.getHeader(Constants.USER_AGENT_LC)).thenReturn(uaFirstRun);

        request2 = mock(HttpServletRequest.class);
        when(request2.getHeaderNames()).thenReturn(e2);
        when(request2.getHeader(Constants.USER_AGENT_LC)).thenReturn(uaFirstRun);

        request3 = mock(HttpServletRequest.class);
        when(request3.getHeaderNames()).thenReturn(e3);
        when(request3.getHeader(Constants.USER_AGENT_LC)).thenReturn(uaSecondRun);

        response = mock(HttpServletResponse.class);

    }

    /*
     * Testing the cache populating it through a getDeviceFromRequest
     * and hitting it with a second getDeviceFromRequest and then a getDeviceFromUserAgent
     * 
     * request1 and request2 contains the same ua uaA
     */
    @Test
    public void firstRun() {

    	device = ICloudClient.getDeviceFromRequest(request1, response, capabilities);
        assertEquals(device.getSource(), ResponseType.cloud, "getDeviceFromRequest should hit CLOUD");
        
    	device = ICloudClient.getDeviceFromRequest(request2, response, capabilities);
        assertEquals(device.getSource(), ResponseType.cache, "getDeviceFromRequest should hit CACHE");

    	device = ICloudClient.getDeviceFromUserAgent(uaFirstRun, capabilities);
        assertEquals(device.getSource(), ResponseType.cache, "getDeviceFromUserAgent should hit CACHE");
    }

    /*
     * Testing the cache populating it through a getDeviceFromUserAgent
     * and hitting it with a second getDeviceFromUserAgent and then a getDeviceFromRequest
     * 
     * request3 contains the ua uaB
     */
    @Test
    public void secondRun() {

    	device = ICloudClient.getDeviceFromUserAgent(uaSecondRun, capabilities);
        assertEquals(device.getSource(), ResponseType.cloud, "getDeviceFromUserAgent should hit CLOUD");
        
    	device = ICloudClient.getDeviceFromUserAgent(uaSecondRun, capabilities);
        assertEquals(device.getSource(), ResponseType.cache, "getDeviceFromUserAgent should hit CACHE");

    	device = ICloudClient.getDeviceFromRequest(request3, response, capabilities);
        assertEquals(device.getSource(), ResponseType.cache, "getDeviceFromRequest should hit CACHE");
    }

}
