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
package com.scientiamobile.wurflcloud.cache;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.scientiamobile.wurflcloud.ICloudClientRequest;
import com.scientiamobile.wurflcloud.device.AbstractDevice;
import com.scientiamobile.wurflcloud.device.CookieDevice;
import com.scientiamobile.wurflcloud.device.JsonCookie;

/**
 * Cache implementation using Cookies.
 *
 */
public class SimpleCookieCache extends AbstractWurflCloudCache {

    /**
     * Default cookie cache expiration, in seconds.
     */
    public static final int COOKIE_CACHE_EXPIRATION = 86400;

    private final ObjectMapper mapper = new ObjectMapper();
    
    /**
     * Cookie encoding charset
     */
    private static final String US_ASCII = "US-ASCII";

    @Override
    protected boolean purgeInternal() {
        return true;
    }

    public AbstractDevice getDevice(HttpServletRequest request, ICloudClientRequest client) {
        if (request == null) throw new IllegalArgumentException("request cannot be null");
        AbstractDevice ret = null;
        Cookie[] cookies = null;
        try {
            cookies = request.getCookies();
        } catch (IllegalStateException e) {}
        if (cookies == null) return null;
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookie.getName().equals(WURFL_COOKIE_NAME)) {
                String value = cookie.getValue();
                try {
                    value = URLDecoder.decode(value, US_ASCII);
                    if (logger.isDebugEnabled()) logger.debug("decoded cookie value: " + value);
                    JsonCookie jc = mapper.readValue(value, JsonCookie.class);
                    long expiration = jc.getDate_set() + COOKIE_CACHE_EXPIRATION;
                    long nowSecs = System.currentTimeMillis() / 1000;
                    if (expiration < nowSecs) {
                        logger.debug("cache expired: " + expiration + "; nowSecs is " + nowSecs);
                        break;
                    }
                    ret = new CookieDevice(jc.getCapabilities(), jc.getId(), client);
                    logger.debug("returning device from cookie: " + cookie.getValue());
                    break;
                } catch (IOException e) {
                    logger.error(e.toString(), e);
                }
            }
        }
        logger.debug("device: " + ret);
        return ret;
    }

    public AbstractDevice getDeviceFromID(String key) {
        return null;
    }

    /**
     * Sets a cookie in the response, with max age default cache expiration.
     *
     * @param response
     * @param key
     * @param device
     * @return
     * @see COOKIE_CACHE_EXPIRATION
     */
    public boolean setDevice(HttpServletResponse response, String key, AbstractDevice device) {
        String cookieVal;
        HashMap<String, Object> capabilitiesMap = new HashMap<String, Object>(device.getCapabilities());
        long nowSecs = System.currentTimeMillis() / 1000;
        JsonCookie jsonCookie = new JsonCookie();
        jsonCookie.setCapabilities(capabilitiesMap);
        jsonCookie.setDate_set(nowSecs);
        jsonCookie.setId(device.getId());
        try {
            cookieVal = mapper.writeValueAsString(jsonCookie);
            logger.debug("cookie value: " + cookieVal);
            cookieVal = URLEncoder.encode(cookieVal, US_ASCII);
            logger.debug("encoded cookie value: " + cookieVal);
        } catch (IOException e) {
            logger.error(e.toString(), e);
            return false;
        }
        Cookie cookie = new Cookie(WURFL_COOKIE_NAME, cookieVal);
        cookie.setMaxAge(COOKIE_CACHE_EXPIRATION);
        if (response != null) {
            response.addCookie(cookie);
            logger.debug(WURFL_COOKIE_NAME + " cookie added to response: " + cookie.getValue());
        } else {
            // TODO: Check message
            logger.warn("Trying to save cookie on a null response. Maybe you're trying to use a CookieCache while querying the cloud with a simple user agent string.");
        }
        return true;
    }

    public boolean setDeviceFromID(String key, AbstractDevice device) {
        return false;
    }

    public void close() {
    }
}
