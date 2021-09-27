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

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

/**
 * Bean serialized from/to a cookie.
 */
public class JsonCookie {
    private Map<String, Object> capabilities;
    private long date_set;
    private String id;

    @JsonCreator
    public JsonCookie(
            @JsonProperty("capabilities")
            Map<String, Object> capabilities,
            @JsonProperty("date_set")
            long date,
            @JsonProperty("id")
            String devId)
    {
        this.capabilities = capabilities;
        this.date_set = date;
        this.id = devId;
    }

    /**
     * Gets the capability map.
     * @return The capability map
     */
    public Map<String, Object> getCapabilities() {
        return capabilities;
    }

    private static final Logger logger = LoggerFactory.getLogger(JsonCookie.class);

    public long getDate_set() {
        return date_set;
    }

    /**
     * Gets the stored cookie identifier
     * @return The cookie identifier
     */
    public String getId() {
        return id;
    }
}
