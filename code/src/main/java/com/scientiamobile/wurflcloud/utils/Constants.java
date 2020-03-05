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
package com.scientiamobile.wurflcloud.utils;

import com.scientiamobile.wurflcloud.CloudServerConfig;

/**
 * Date: 14/07/11
 * Constants for wurflcloud.
 *
 * @version $Id$
 */
public interface Constants {

    /**
     * The version of this client
     */
    String CLIENT_VERSION = "1.0.13";

    /**
     * Accepted encoding enum.
     */
    public static enum Encoding {
        GZIP("gzip"), PLAIN("text");
        public final String val;

        private Encoding(String val) {
            this.val = val;
        }
    }

    String UNKNOWN = "unknown";

    /**
     * Default nickname for server
     */
    String DEFAULT_SERVER_NICKNAME = "wurfl_cloud";

    /**
     * Default host name.
     */
    String DEFAULT_SERVER_HOST = "api.wurflcloud.com";

    /**
     * Default Server Info
     */
    CloudServerConfig DEFAULT_SERVER_CONFIG = new CloudServerConfig(DEFAULT_SERVER_NICKNAME, DEFAULT_SERVER_HOST, 100);

    /**
     * The WURFL Cloud Service API type
     */
    String API_HTTPS = "https";

    /**
     * Request path prefix.
     */
    String REQ_PATH_PREFIX = "/v1/json/search:('";

    /**
     * Request path suffix.
     */
    String REQ_PATH_SUFFIX = "')";

    /**
     * Cache auto purge.
     */
    boolean DEFAULT_AUTO_PURGE = false;

    /**
     * Enables or disables the use of compression in the WURFL Cloud response.  Using compression
     * can increase CPU usage in very high traffic environments, but will decrease network traffic
     * and latency.
     * Default is true.
     */
    boolean DEFAULT_COMPRESSION = true;

    /**
     * The interval in seconds that after which API will report its performance
     */
    int DEFAULT_REPORT_INTERVAL = 60;

    /**
     * The WURFL Cloud API Type to be used.  Currently, only WurflCloudClientConfig::API_HTTPS is supported.
     */

    String API_TYPE = API_HTTPS;

    /**
     * Cookie name, for cookie cache use.
     */
    String WURFL_COOKIE_NAME = "WurflCloud_Client";

    /**
     * Available cache types.
     */
    enum CacheType {
        None, HashMap, Ehcache, Cookie
    }

    /**
     * Config file name.
     */
    String WURFL_CLOUD_CONFIG_FILE = "/wurflcloud.properties";

    /**
     * Config names.
     */
    String PREFIX = "wurflcloud.";
    
    String APIKEY = PREFIX + "key";
    
    String CACHE = PREFIX + "cache";

    String CAPABILITY_PREFIX = PREFIX + "capability.";

    String COMPRESSION = PREFIX + "compression";
    
    String CONNECTION_TIMEOUT_PROP = PREFIX + "connTimeout";

    String READ_TIMEOUT_PROP = PREFIX + "readTimeout";

    /**
     * Proxy
     */
    String PROXY_PREFIX = PREFIX + "proxy.";
    String PROXY_URL = PROXY_PREFIX + "url";
    String PROXY_PORT = PROXY_PREFIX + "port";
    String PROXY_TYPE = PROXY_PREFIX + "type";

    /**
     * HashMap Cache defaults
     */
    public static final int DEFAULT_CAPACITY = 60000;
    public static final float DEFAULT_LOAD_FACTOR = .75f;
    public static final int DEFAULT_CONCURRENT_WRITES = 16;

    /**
     * Default timeout values on connections
     */
    int DEFAULT_CONNECTION_TIMEOUT = 5000;
    int DEFAULT_READ_TIMEOUT = 10000;

    /**
     * lowercase userAgent header
     */
    String USER_AGENT_LC = "user-agent";

    /**
     * Authorization header
     */
    String AUTHORIZATION = "Authorization";

    /**
     * Other lowercase headers
     */
    String REMOTE_ADDR_LC = "remote_addr";
    String ACCEPT_LC = "accept";
    String X_WAP_PROFILE_LC = "x-wap-profile";
    String X_FORWARDED_FOR_LC = "x-forwarded-for";
    String PROFILE_LC = "profile";
    String X_REQUESTED_WITH_LC = "x-requested-with";    

    /**
     * X-Accept
     */
    String X_ACCEPT = "X-Accept";
    
    /**
     * X-Wap-Profile
     */
    String X_WAP_PROFILE = "X-Wap-Profile";

    /**
     * X-Forwarded-For
     */
    String X_FORWARDED_FOR = "X-Forwarded-For";

    /**
     * The max allowed length of the user-agent
     */
    int USER_AGENT_MAX_LENGTH = 512;

    /**
     * The HTTP Headers that will be examined to find the best User Agent, if one is not specified
     */
    String[] HEADERS = new String[] {
            "Device-Stock-UA",      // "HTTP_DEVICE_STOCK_UA",
            "X-Device-User-Agent",  // "HTTP_X_DEVICE_USER_AGENT",
            "X-Original-User-Agent",// "HTTP_X_ORIGINAL_USER_AGENT",
            "X-Operamini-Phone-Ua", // "HTTP_X_OPERAMINI_PHONE_UA",
            "X-Skyfire-Phone",      // "HTTP_X_SKYFIRE_PHONE",
            "X-Bolt-Phone-Ua",      // "HTTP_X_BOLT_PHONE_UA",
            USER_AGENT_LC,          // "HTTP_USER_AGENT"
    };
    
}
