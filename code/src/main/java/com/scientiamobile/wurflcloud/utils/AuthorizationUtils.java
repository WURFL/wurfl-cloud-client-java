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
package com.scientiamobile.wurflcloud.utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Static methods to build signature string.
 *
 * @version $Id$
 */
public class AuthorizationUtils {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationUtils.class);

    /**
     * Private Constructor
     */
    private AuthorizationUtils() {
    }

    /**
     * Build domain inner path, with searched capabilities.
     *
     * @param search_capabilities The searched capabilities
     * @return The constructed build domain inner path
     */
    public static String buildRequestPath(String[] search_capabilities) {
    	// build capabilities
    	StringBuilder sb = new StringBuilder();
        if (search_capabilities != null) {
        	char delimiter = ',';
            for (int i = 0; i < search_capabilities.length; i++) {
                final String s = search_capabilities[i];
                if (s != null && s.length() > 0)
                    sb.append(s).append(delimiter);
            }
            if (sb.length() > 0) sb.setLength(sb.length() - 1);
        }
        
        return Constants.REQ_PATH_PREFIX + sb.toString() + Constants.REQ_PATH_SUFFIX;
    }


    /**
     * Return the complete string, included 'Basic ' word
     *
     * @param credentials The {@link Credentials} object used to store the user credentials
     * @return The computed Basic HTTP Auth header value.
     */
    public static String getBasicAuthString(Credentials credentials) {
        try {
            String ret = "Basic " + buildEncodedSignature(credentials).trim();
            if (logger.isDebugEnabled()) logger.debug("auth string: " + ret);
            return ret;
        } catch (Exception e) {
            throw new IllegalArgumentException("Signature not valid: " + e.getMessage(), e);
        }
    }


    /**
     * Get the HTTP Encoded Signature for the API
     *
     * @param credentials The {@link Credentials} object used to store the user credentials
     * @return The computed Basic HTTP Auth header value.
     */
    private static String buildEncodedSignature(Credentials credentials) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String username = credentials.getUsername();
        logger.debug("user: " + username);
        return Base64.encode(username + ":" + credentials.getPassword());
    }

}
