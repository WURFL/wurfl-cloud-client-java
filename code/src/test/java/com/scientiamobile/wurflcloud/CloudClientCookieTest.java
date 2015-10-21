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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Date: 20/07/11
 *
 * @version $Id$
 */
@Test(groups = "unit")
public class CloudClientCookieTest extends Loggable {

    static final String ua = "Mozilla/5.0 (BlackBerry; U; BlackBerry 9800; en-US) AppleWebKit/534.8+ (KHTML, like Gecko) Version/6.0.0.466 Mobile Safari/534.8+";

    private ICloudClientManager ICloudClient;
    private AbstractDevice device;
    private String[] capabilities;
    private long time;
    private long start;
    private String mobile;
    private Enumeration<Object> e;
    private HttpServletRequest request;
    private HttpServletResponse response;


    @BeforeClass
    public void setup() throws Exception {
        CloudClientLoader loader = new CloudClientLoader(null, "/CookieTest.properties");
        ICloudClient = loader.getClientManager();
        capabilities = loader.getSearchedCapabilities();
    }

    @BeforeMethod
    public void setupMethod() {

        e = new Enumeration<Object>() {
            private boolean set = false;

            public boolean hasMoreElements() {
                return !set;
            }

            public Object nextElement() {
                if (!set) {
                    set = true;
                    return Constants.USER_AGENT_LC;
                }
                throw new IllegalStateException("finished objects");
            }
        };
        request = mock(HttpServletRequest.class);
        when(request.getHeaderNames()).thenReturn(e);
        when(request.getHeader(Constants.USER_AGENT_LC)).thenReturn(ua);

        response = mock(HttpServletResponse.class);

    }

    @Test
    public void testWoCookie() {
        start = System.currentTimeMillis();
        device = ICloudClient.getDeviceFromRequest(request, response, capabilities);
        print();
    }

    private void print() {
        logger.debug("\ndevice: " + device);
        time = System.currentTimeMillis() - start;
        Object mob = device.get("is_wireless_device");
        logger.debug("mobile=" + mob);
        this.mobile = mob != null ? mob.toString() : "unknown";

        String s = "Result: " + device+ "\n";
        if ("true".equals(mobile)) {
            s += "This is a mobile device.\nDevice: " + device.get("brand_name") +
                    " " + device.get("model_name") + "\t";
        } else {
            s += "This is a desktop browser";
        }
        logger.info(s);

        logger.info("ResponseType: " + device.getSource());
        logger.info("Server: " + ICloudClient.getCloudServer());
        logger.info("Query time: " + time+"\n");
    }

    @Test
    public void testWithCookie() {
        start = System.currentTimeMillis();
        Cookie[] cks = new Cookie[1];
        cks[0] = new Cookie(Constants.WURFL_COOKIE_NAME, "{\"date_set\":\""+((start/1000)-5000)+"\",\"capabilities\":{\"id\":\"pippo\",\"brand_name\":\"RIM\",\"is_wireless_device\":true,\"resolution_height\":480,\"model_name\":\"BlackBerry 9800\"}}");//"resolution_width":360,
        when(request.getCookies()).thenReturn(cks);

        device = ICloudClient.getDeviceFromRequest(request, response, capabilities);
        print();
    }
    @Test
    public void testWithExpiredCookie() {
        start = System.currentTimeMillis();
        Cookie[] cks = new Cookie[1];
        cks[0] = new Cookie(Constants.WURFL_COOKIE_NAME, "{\"date_set\":\""+((start/1000)-100000)+"\",\"capabilities\":{\"id\":\"pippo\",\"brand_name\":\"RIM\",\"is_wireless_device\":true,\"resolution_width\":360,\"resolution_height\":480,\"model_name\":\"BlackBerry 9800\"}}");
        when(request.getCookies()).thenReturn(cks);

        device = ICloudClient.getDeviceFromRequest(request, response, capabilities);
        print();
    }
}
