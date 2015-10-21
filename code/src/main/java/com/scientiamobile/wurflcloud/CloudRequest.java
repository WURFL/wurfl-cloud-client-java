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

import javax.servlet.http.Cookie;

/**
 * CloudRequest interface
 */
public interface CloudRequest {
	
	/**
	 * Returns all the header names
	 * @return The header names
	 */
    Enumeration<String> getHeaderNames();
    
    /**
     * Gets the header value associated to the requested header name
     * @param name The requested header name
     * @return The header value
     */
    String getHeader(String name);
    
    /**
     * Returns the Internet Protocol (IP) address of the client or last proxy that sent the request
     * @return The IP address of the client or last proxy that sent the request
     */
    String getRemoteAddr();

    /**
     * @return The stored {@link Cookie} array, if available
     */
	Cookie[] getCookies();
}
