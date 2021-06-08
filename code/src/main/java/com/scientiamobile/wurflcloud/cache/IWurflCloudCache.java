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

import com.scientiamobile.wurflcloud.ICloudClientRequest;
import com.scientiamobile.wurflcloud.device.AbstractDevice;
import com.scientiamobile.wurflcloud.utils.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Cache interface.
 */
public interface IWurflCloudCache extends Constants {

    /**
     * Get the device capabilities for the given user agent from the cache provider
     */
    public AbstractDevice getDevice(HttpServletRequest request, ICloudClientRequest client);
    /**
     * Get the device for the given key from the cache provider
     * 
     * @param key The key to use when looking up the cache
     * @return An @{link AbstractDevice} instance, or null if no device is associated with the search key
     */
    public AbstractDevice getDeviceFromID(String key);

    /**
     * Stores a new entry in the cache, using the specified key and device
     * 
     * @param response The servlet response
     * @param key The entry's key to use
     * @param device The @{link AbstractDevice} instance
     * @return True if the store procedure has been successful, false otherwise
     */
    public boolean setDevice(HttpServletResponse response, String key, AbstractDevice device);

    /**
     * Stores a new entry in the cache, using the specified key and device
     *
     * @param key The entry's key to use
     * @param device The @{link AbstractDevice} instance
     * @return True if the store procedure has been successful, false otherwise
     */
    public boolean setDeviceFromID(String key, AbstractDevice device);

    /**
     * Gets the last loaded WURFL timestamp from the cache provider - this is used to detect when a new WURFL has been loaded on the server
     *
     * @return The last loaded WURFL timestamp
     */
    public long getMtime();

    /**
     * Sets the last loaded WURFL timestamp in the cache provider
     * @link IWurflCloudCache.#getMtime()
     *
     * @param server_mtime The new time to set
     * @return True if the new time was set successfully, false otherwise
     */
    public boolean setMtime(long server_mtime);

    /**
     * Clears the cache provider
     * 
     * @return True if the purge succeeded, false otherwise
     */
    public boolean purge();

    /**
     * Returns a map of counters
     * 
     * @return A map of counters 
     */
    public Map<String, Long> getCounters();

    /**
     * Resets the counters to zero
     */
    public void resetCounters();

    /**
     * Returns the report age
     *
     * @return The number of seconds since the counters report was last sent
     */
    public long getReportAge();

    /**
     * Resets the report age
     */
    public void resetReportAge();

    /**
     * Closes the connection to the cache provider
     */
    public void close();

}