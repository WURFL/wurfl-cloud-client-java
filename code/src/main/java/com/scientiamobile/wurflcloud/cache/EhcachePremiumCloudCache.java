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
import com.scientiamobile.wurflcloud.utils.AeSimpleMD5;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Cache implementation using Java {@linkplain http://ehcache.org/} Ehcache implementation.
 */
public class EhcachePremiumCloudCache extends AbstractUseragentWurflCloudCache {

    private final Cache cache;

    public EhcachePremiumCloudCache() {
        CacheManager manager = new CacheManager();
        cache = manager.getCache("com.scientiamobile.wurflcloud.Device");
    }

    protected final AbstractDevice getDeviceFromUserAgent(String ua) {
        String key = null;
        try {
            key = AeSimpleMD5.MD5(ua);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Element item = cache.get(key);

        Object stored = null;
        if (item != null) {
            // Can be expired
            stored = item.getObjectValue();
        }

        return (AbstractDevice) stored;

    }

    public AbstractDevice getDeviceFromID(String key) {
        
    	try {
            key = AeSimpleMD5.MD5(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        Element item = cache.get(key);

        Object stored = null;
        if (item != null) {
            // Can be expired
            stored = item.getObjectValue();
        }

        return (AbstractDevice) stored;

    }

    public boolean setDevice(HttpServletResponse res,String ua, AbstractDevice device) {
        String key = null;
        try {
            key = AeSimpleMD5.MD5(ua);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Element item = new Element(key, device);
        cache.put(item);
        return true;
    }

    public boolean setDeviceFromID(String key, AbstractDevice device) {
        try {
            key = AeSimpleMD5.MD5(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Element item = new Element(key, device);
        cache.put(item);
        return true;
    }


    protected boolean purgeInternal() {
        cache.removeAll();
        return true;
    }

    public Map<String, Long> getCounters() {
        counters.put(HIT, cache.getStatistics().getCacheHits());
        counters.put(MISS, cache.getStatistics().getCacheMisses());
        return counters;
    }

    public void resetCounters() {
        cache.clearStatistics();
    }

    public void close() {
        cache.dispose();
    }
}
