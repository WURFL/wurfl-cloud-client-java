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

import javax.servlet.http.HttpServletRequest;
import com.scientiamobile.wurflcloud.ICloudClientRequest;
import com.scientiamobile.wurflcloud.device.AbstractDevice;
import com.scientiamobile.wurflcloud.utils.Constants;

public abstract class AbstractUseragentWurflCloudCache extends AbstractWurflCloudCache {

    /**
     * Queries the cache for a {@link AbstractDevice} instance, using the provided servlet request. 
     * @param request The servlet request
     * @return A {@link AbstractDevice} instance, or null if there's no such device cached
     */
    @Override
    public final AbstractDevice getDevice(HttpServletRequest request, ICloudClientRequest client) {
        if (request == null) return null;

        String userAgentLC = request.getHeader(Constants.USER_AGENT_LC);
        String userAgentUC = request.getHeader("User-Agent");
        String userAgent =  userAgentUC != null ? userAgentUC : userAgentLC;
        return getDeviceFromUserAgent(userAgent);
    }

    /**
     * Queries the cache for a {@link AbstractDevice} instance, using the provided user agent. 
     * @param userAgent The user agent to use in the request
     * @return A {@link AbstractDevice} instance, or null if there's no such device cached
     */
    protected abstract AbstractDevice getDeviceFromUserAgent(String userAgent);

}
