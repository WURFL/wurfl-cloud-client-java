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

import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache implementation using HashMap
 */
public final class HashMapPremiumCloudCache extends AbstractUseragentWurflCloudCache {

    /**
     * The hashMap containing the cached objects
     */
    private final Map<String, AbstractDevice> cache;

    // Constructors *******************************************************

    /**
     * Default constructor
     */
    public HashMapPremiumCloudCache() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructor by the initial capacity.
     *
     * @param initialCapacity
     */
    public HashMapPremiumCloudCache(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructor by the initial capacity and load factor.
     *
     * @param initialCapacity The hashMap initial capacity.
     * @param loadFactor      The hashMap load factor.
     */
    public HashMapPremiumCloudCache(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, DEFAULT_CONCURRENT_WRITES);
    }

    /**
     * Constructor by the initial capacity, loadFactor and maximum concurrent
     * writes
     *
     * @param initialCapacity  The hashMap initial capacity.
     * @param loadFactor       The hashMap load factor.
     * @param concurrentWrites The maximum thread can write the cache.
     */
    public HashMapPremiumCloudCache(int initialCapacity, float loadFactor, int concurrentWrites) {

        cache = new ConcurrentHashMap<String, AbstractDevice>(initialCapacity, loadFactor, concurrentWrites);

        StringBuffer logBuffer = new StringBuffer("Created HashMap Cache with initial capacity: ");
        logBuffer.append(initialCapacity);
        logBuffer.append(" load factor: ");
        logBuffer.append(loadFactor);
        logBuffer.append(" concurrent writes: ");
        logBuffer.append(concurrentWrites);

        logger.info(logBuffer.toString());

    }

    // Commons methods ****************************************************

    public String toString() {
        return cache.toString();
    }

    @Override
    protected AbstractDevice getDeviceFromUserAgent(String ua) {
        AbstractDevice device = cache.get(ua);
        if (device == null) {
            incrementMiss();
        } else {
            incrementHit();
        }
        return device;
    }

    public AbstractDevice getDeviceFromID(String id) {
        AbstractDevice device = cache.get(id);
        if (device == null) {
            incrementMiss();
        } else {
            incrementHit();
        }
        return device;
    }

    public boolean setDevice(HttpServletResponse res, String ua, AbstractDevice d) {
        return cache.put(ua, d) != null;
    }

    public boolean setDeviceFromID(String id, AbstractDevice d) {
        return cache.put(id, d) != null;
    }

    protected boolean purgeInternal() {
        cache.clear();
        resetCounters();
        return true;
    }

    public void close() {
        cache.clear();
        resetCounters();
    }

}
