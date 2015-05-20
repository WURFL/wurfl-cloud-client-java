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

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import com.scientiamobile.wurflcloud.ICloudClientRequest;
import com.scientiamobile.wurflcloud.Loggable;
import com.scientiamobile.wurflcloud.device.AbstractDevice;

/**
 *         $Id$
 */
public abstract class AbstractWurflCloudCache extends Loggable implements IWurflCloudCache {
    protected static final String HIT = "hit";
    protected static final String MISS = "miss";
    protected static final String ERROR = "error";
    protected static final String AGE = "age";

    protected final Map<String, Long> counters = createStatCache();

    protected long mtime;
    private long reportAge;

    public final long getMtime() {
        return mtime;
    }

    public AbstractDevice getDevice(HttpServletRequest request, ICloudClientRequest client) {
    	return null;
    }
    
    public final boolean setMtime(long server_mtime) {
        logger.debug("setting mtime to " + server_mtime);
        mtime = server_mtime;
        return false;
    }

    public final long getReportAge() {
        return (System.currentTimeMillis() - reportAge) / 1000;
    }

    public final void resetReportAge() {
        reportAge = System.currentTimeMillis();
    }


    public Map<String, Long> getCounters() {
        counters.put(AGE, getReportAge());
        return Collections.unmodifiableMap(counters);
    }

    public void resetCounters() {
        reset(counters);
    }

    private void reset(Map<String, Long> map) {
        map.put(HIT, 0L);
        map.put(MISS, 0L);
        map.put(ERROR, 0L);
        map.put(AGE, 0L);
    }


    protected void incrementHit() {
        Long i = counters.get(HIT);
        counters.put(HIT, i + 1);
    }

    protected void incrementMiss() {
        Long i = counters.get(MISS);
        counters.put(MISS, i + 1);
    }

    protected void incrementError() {
        Long i = counters.get(ERROR);
        counters.put(ERROR, i + 1);
    }

    /**
     * Factory method to create stats cache Map. Subclass can override this method to
     * change the backing HashMap implementation.
     */
    protected Map<String, Long> createStatCache() {
        ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<String, Long>(100, (float) 0.75, 16);
        reset(map);
        return map;
    }

    public final boolean purge() {
        logger.info("Cache purging");
        return purgeInternal();
    }

    protected abstract boolean purgeInternal();
}
