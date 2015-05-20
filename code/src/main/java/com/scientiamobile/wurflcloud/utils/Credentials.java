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

/**
 * Simple username-password bean.
 *
 * @version $Id$
 * @since 2.0
 */
public class Credentials {
    private final String password;
    private final String username;

    /**
     * Builds a new Credentials object with the provided username and password.
     * @param username The user name
     * @param password The password
     */
    public Credentials(String username, String password) {
        this.password = password;
        this.username = username;
    }

    /**
     * Gets the stored user name
     * @return The user name
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the stored password
     * @return The password
     */
    public String getPassword() {
        return password;
    }
}
