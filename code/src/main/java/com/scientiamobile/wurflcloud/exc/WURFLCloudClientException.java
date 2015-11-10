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
package com.scientiamobile.wurflcloud.exc;

public class WURFLCloudClientException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final int httpResponseCode;
    
    public WURFLCloudClientException(String s, int httpResponseCode) {
        super(s);
        this.httpResponseCode = httpResponseCode;
    }
    
    /**
     * Returns the HTTP response code received from the server, from the connection
     * which has thrown the exception
     * @return the HTTP response code
     */
    public int getHttpResponseCode() {
        return httpResponseCode;
    }
}
