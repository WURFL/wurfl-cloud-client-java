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
package com.scientiamobile.wurflcloud.cache;

import com.scientiamobile.wurflcloud.device.AbstractDevice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cache implementation with no specific behavior.
 */
public class WurflCloudCache_Null extends AbstractWurflCloudCache {

    public AbstractDevice getDevice(HttpServletRequest request) {
        return null;
    }

    public AbstractDevice getDeviceFromID(String device_id) {
        return null;
    }

    public boolean setDevice(HttpServletResponse res, String user_agent, AbstractDevice d) {
        return true;
    }

    public boolean setDeviceFromID(String device_id, AbstractDevice d) {
        return true;
    }

    protected boolean purgeInternal() {
        return true;
    }

    public void resetCounters() {
    }

    public void close() {
    }
}
