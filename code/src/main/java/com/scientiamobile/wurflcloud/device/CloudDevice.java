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

import com.scientiamobile.wurflcloud.CloudResponse;
import com.scientiamobile.wurflcloud.ICloudClientRequest;
import com.scientiamobile.wurflcloud.ResponseType;

/**
 * Object filled by wurfl cloud response, by mean of {@link com.scientiamobile.wurflcloud.CloudResponse}
 *
 * @version $Id$
 */
public class CloudDevice extends AbstractDevice {

	/**
	 * Builds an AbstractDevice object using "cloud" as response type.
	 * @param response The CloudResponse object to use
	 * @param clientRequest The client request
	 */
    public CloudDevice(CloudResponse response, ICloudClientRequest clientRequest) {
        super(response.getCapabilities(), ResponseType.cloud, response.getId(), response.getErrors(), clientRequest);
    }
}
