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

import com.scientiamobile.wurflcloud.utils.Credentials;

/**
 * @version $Id$
 */
public class AuthenticationManager implements IAuthenticationManager {
    private final CloudClientConfig config;

    /**
     * Build a new AuthenticationManager instance using the provided configuration
     * @param config The configuration to use
     */
    public AuthenticationManager(CloudClientConfig config) {
        this.config = config;
    }

    /**
     * {@inheritDoc}
     */
    public Credentials splitApiKey() {
        String api_key = config.apiKey;
        if (api_key == null || api_key.length() == 0) {
            throw new IllegalArgumentException("Api key must be not empty");
        }
        
        int indexOfColon = api_key.indexOf(':');
        if (indexOfColon < 0) {
        	throw new IllegalArgumentException("Api key must contain a \':\' separator.");
        }
        
        String username = api_key.substring(0, indexOfColon);
        if (username.length() == 0) {
        	throw new IllegalArgumentException("Api key username is empty.");
        }
        
        String pwd = api_key.substring(indexOfColon + 1);
        if (pwd.length() == 0) {
        	throw new IllegalArgumentException("Api key password is empty.");
        }
        
        return new Credentials(username, pwd);
    }

}
