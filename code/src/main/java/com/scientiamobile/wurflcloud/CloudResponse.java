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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Bean which is automatically filled by json engine.
 *
 *        $Id$
 */
public class CloudResponse extends Loggable implements Serializable {
	private static final long serialVersionUID = 2L;
	
	private String apiVersion;
    private long mtime;
    private String id;
    private Map<String, Object> capabilities;
    private Map<String, String> errors;

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public long getMtime() {
        return mtime;
    }

    public void setMtime(long mtime) {
        this.mtime = mtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getCapabilities() {
        return capabilities;

    }

    public void setCapabilities(HashMap<String, Object> capMaps) {
        this.capabilities = capMaps;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(HashMap<String, String> errors) {
        if (errors != null && errors.size() > 0) {
            logger.warn("There are errors: " + errors);
        }
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "CloudResponse{" +
                "apiVersion='" + apiVersion + '\'' +
                ", mtime=" + mtime +
                ", id='" + id + '\'' +
                ", capabilities=" + capabilities +
                ", errors=" + (errors == null ? null : errors) +
                '}';
    }
}
