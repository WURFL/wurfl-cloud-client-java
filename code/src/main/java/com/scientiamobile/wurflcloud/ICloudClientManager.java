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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * CloudClientManager interface
 */
public interface ICloudClientManager {
    String getClientVersion();

    /**
     * Retrieves a {@link AbstractDevice} instance given the request and an optional array of capabilities
     * @param request The request object
     * @param response The servlet response
     * @param search_capabilities Array of capabilities that you would like to retrieve, optional
     * @return A {@link AbstractDevice} instance
     */
    AbstractDevice getDeviceFromRequest(HttpServletRequest request, HttpServletResponse response, String... search_capabilities);

    /**
     * Retrieves a {@link AbstractDevice} instance given the user agent and an optional array of capabilities
     * @param userAgent The user agent string
     * @param search_capabilities Array of capabilities that you would like to retrieve, optional
     * @return A {@link AbstractDevice} instance
     */
    AbstractDevice getDeviceFromUserAgent(String userAgent, String... search_capabilities);

    /**
     * Checks if local cache is still valid based on the date that the WURFL Cloud Server
     * was last updated.  If auto_purge is enabled, this method will clear the cache provider
     * if the cache is outdated.
     * 
     * @return True if the cache has been purged, false otherwise
     */
    boolean validateCache();

    /**
     * Get the version of the WURFL Cloud Server.  This is only available
     * after a query has been made since it is returned in the response.
     *
     * @return The WURFL Cloud server version
     */
    String getAPIVersion();

    /**
     * Returns the Cloud server configuration being used
     * @return The Cloud server configuration
     */
    CloudServerConfig getCloudServer();

    /**
     * Make a test webservice call to the server using the GET method and load the response.
     * 
     * @return True if the test call was successful, false otherwise
     */
    boolean testCallWurflCloud();
}
