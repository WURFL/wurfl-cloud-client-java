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

import java.util.EventObject;

/**
 * Thin response event.
 *
 */
public class CloudEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	
	private final long mtime;
    private final String apiVersion;

    public CloudEvent(Object o, long mtime, String apiVersion) {
        super(o);
        this.mtime = mtime;
        this.apiVersion = apiVersion;
    }

    public long getMtime() {
        return mtime;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    @Override
    public String toString() {
        return "Event: " + getSource() + "{" +
                "mtime=" + mtime +
                ", apiVersion='" + apiVersion + '\'' + '}';
    }
}
