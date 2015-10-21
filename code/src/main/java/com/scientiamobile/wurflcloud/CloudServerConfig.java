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

/**
 * Abstraction of a server, in a server pool structure.
 */
public class CloudServerConfig {
    public final String nickname;
    public final String host;
    public final int weight;

    public CloudServerConfig(String nickname, String host, int weight) {
        this.host = host;
        this.weight = weight;
        this.nickname = nickname;
    }

    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(this.getClass().getName())
    			.append('@')
    			.append(this.hashCode())
    			.append('-')
    			.append("nickname:")
    			.append(nickname)
    			.append('-')
    			.append("host:")
    			.append(host)
    			.append('-')
    			.append("weight:")
    			.append(weight);
        return sb.toString();
    }
}
