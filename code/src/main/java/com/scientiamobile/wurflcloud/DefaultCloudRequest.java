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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.scientiamobile.wurflcloud.utils.Constants;

/**
 * CloudRequest default implementation.
 *
 */
public final class DefaultCloudRequest implements CloudRequest {

    private final Map<String, String> headers = new HashMap<String, String>();

    public DefaultCloudRequest(HttpServletRequest servletRequest) throws IllegalArgumentException {
        if (servletRequest == null) {
            throw new IllegalArgumentException("ERROR: Servlet request cannot be null.");
        }
        String userAgentLC = servletRequest.getHeader(Constants.USER_AGENT_LC);
        String userAgentUC = servletRequest.getHeader("User-Agent");
        this.headers.put("user-agent", userAgentUC != null ? userAgentUC : userAgentLC);
    }

    public DefaultCloudRequest(String userAgent) throws IllegalArgumentException {
        this.headers.put(Constants.USER_AGENT_LC, userAgent);
    }

    /**
     * {@inheritDoc}
     */
    public Enumeration<String> getHeaderNames() {
        return new Vector<String>(headers.keySet()).elements();
    }

    /**
     * {@inheritDoc}
     */
    public String getHeader(String name) {
        return headers.get(name);
    }

    /**
     * {@inheritDoc}
     */
    public String getRemoteAddr() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Cookie[] getCookies() {
        throw new IllegalStateException("ERROR: Trying to get cookies from a CloudRequest which does not support cookie storage.");
    }

}
