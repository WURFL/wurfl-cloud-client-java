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

/**
 *  The client request interface
 */
public interface ICloudClientRequest {

    /**
     * Perform a request to the Cloud for a specific device and check if the caller is authorized to get a certain capability
     * @param capabilityName The requested capability name 
     * @param device The device to be used with the query
     * @return The capability value, or null if the caller isn't authorized to get the requested capability
     */
    Object queryCloudForCapability(String capabilityName, AbstractDevice device);
}
