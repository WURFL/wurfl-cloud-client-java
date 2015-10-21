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

import java.util.Map;

/**
 * Date: 28/07/11
 *
 * @since 1.0
 * @version $Id$
 */
public class RecoveryDevice extends AbstractDevice {

    public RecoveryDevice(Map<String, Object> capabilities, String recoveryID, String mobile) {
        super(capabilities, ResponseType.recovery, recoveryID, null);
    }
}
