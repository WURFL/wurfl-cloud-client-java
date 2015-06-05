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

import com.scientiamobile.wurflcloud.ResponseType;

/**
 * Represents a cached {@link AbstractDevice}.
 */
public class CacheDevice extends AbstractDevice {

	/**
	 * Builds an AbstractDevice object using "cache" as response type.
	 * @param d The device to use
	 */
    public CacheDevice(AbstractDevice device) {
        super(device.getCapabilities(), ResponseType.cache, device.getId(), device.getErrors());
    }
    
}
