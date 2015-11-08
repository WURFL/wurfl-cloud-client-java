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
package com.scientiamobile.wurflcloud.device;

import com.scientiamobile.wurflcloud.ICloudClientRequest;
import com.scientiamobile.wurflcloud.ResponseType;

import java.util.Map;

/**
 * Represents a {@link AbstractDevice} retrieved from a Cookie.
 */
public class CookieDevice extends AbstractDevice{

    /**
     * Builds an AbstractDevice object using "cookie" as response type.
     * @param capabilities The map of queried capabilities
     * @param id The device identifier
     * @param clientRequest The client request
     */
    public CookieDevice(Map<String, Object> capabilities, String id, ICloudClientRequest clientRequest) {
        super(capabilities, ResponseType.cookie, id, null, clientRequest);
    }
}
