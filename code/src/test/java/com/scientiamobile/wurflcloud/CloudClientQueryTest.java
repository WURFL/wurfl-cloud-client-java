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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;

import static org.mockito.Mockito.*;


/**
 * @version $Id$
 */
@Test(groups = "unit")
public class CloudClientQueryTest extends Loggable{

    private static final String ua = "Mozilla/5.0 (BlackBerry; U; BlackBerry 9800; en-US) AppleWebKit/534.8+ (KHTML, like Gecko) Version/6.0.0.466 Mobile Safari/534.8+";

    private ICloudClientManager ICloudClient;
    private AbstractDevice device;
    private String[] capabilities;
    private long time;
    private long start;
    private String mobile;


    @BeforeClass
    public void setup() throws Exception {
        CloudClientLoader loader = new CloudClientLoader(null, "/DefaultTest.properties");
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
        
        logger.debug("\ndevice: " + device);
        time = System.currentTimeMillis() - start;
        Object mobile = device.get("is_mobile");
        this.mobile = mobile != null ? mobile.toString() : "unknown";
    }

    @Test
    public void testClient() {

        String s = "Result: " + device.getId() + "\n";
        if (mobile.equals("true")) {
            s += "This is a mobile device.\ncomplete_device_name capability: " + device.get("complete_device_name") + "\n";
        } else {
            s += "This is a desktop browser\n";
        }

        logger.info(s);

        logger.info("ResponseType: " + device.getSource());
        logger.info("Server: " + ICloudClient.getCloudServer());
        logger.info("Query time: " + time);
    }
}
