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
import java.util.Random;

import com.scientiamobile.wurflcloud.utils.Constants;


/**
 * Mantains Cloud client configuration.
 */
public class CloudClientConfig extends Loggable implements Serializable {
	private static final long serialVersionUID = 2L;

    /**
     * The API Key is used to authenticate with the WURFL Cloud Service.  It can be found at in your account
     * at http://www.scientiamobile.com/myaccount
     * The API Key is 39 characters in with the format: nnnnnn:xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
     * where 'n' is a number and 'x' is a letter or number
     */
    protected String apiKey = null;

    /**
     * WURFL Cloud servers to use for uncached requests.  The "weight" field can contain any positive number,
     * the weights are relative to each other.
     */
    protected final Map<String, CloudServerConfig> wcloud_servers = new HashMap<String, CloudServerConfig>();

    /**
     * The WURFL Cloud Server that is currently in use, formatted like:
     */
    protected CloudServerConfig current_server;

    /**
     * Set gzip compressed server response
     */
    protected boolean compression = Constants.DEFAULT_COMPRESSION;

    protected int reportInterval = Constants.DEFAULT_REPORT_INTERVAL;

    protected boolean autoPurge = false;

    /**
     * The timeouts on connections
     */
    protected int connectionTimeout = Constants.DEFAULT_CONNECTION_TIMEOUT;
    protected int readTimeout = Constants.DEFAULT_READ_TIMEOUT;

    /**
     * Protected, created by application.
     */
    protected CloudClientConfig() {
        addCloudServer(Constants.DEFAULT_SERVER_CONFIG);
    }

    public void addCloudServer(CloudServerConfig sc) {
        wcloud_servers.put(sc.nickname, sc);
    }

    /**
     * Adds the specified WURFL Cloud Server
     *
     * @param nickname Unique identifier for this server
     * @param url      URL to this server's API
     * @param weight   Specifies the chances that this server will be chosen over
     *                 the other servers in the pool.  This number is relative to the other servers' weights.
     */
    public void addCloudServer(String nickname, String url, int weight) {
        wcloud_servers.put(nickname, new CloudServerConfig(nickname, url, weight));
    }

    /**
     * Removes the WURFL Cloud Servers
     */
    public void clearServers() {
        wcloud_servers.clear();
        current_server = null;
    }

    /**
     * Determines the WURFL Cloud Server that will be used and returns its URL.
     *
     * @return string WURFL Cloud Server URL
     */
    public CloudServerConfig getCloudHost() {
        return getWeightedServers()[0];
    }

    /**
     * Uses a weighted-random algorithm to chose a server from the pool
     *
     * @return array CloudServerConfig
     */
    private synchronized CloudServerConfig[] getWeightedServers() {
        if (current_server == null) {
            if (wcloud_servers.size() == 1) {
                current_server = wcloud_servers.values().toArray(new CloudServerConfig[0])[0];
            } else {
                int max = 0, rcount = 0;
                for (CloudServerConfig sc : wcloud_servers.values()) {
                    max += sc.weight;
                }

                Random r = new Random();
                int wrand = r.nextInt(max + 1);
                CloudServerConfig peek = null;
                for (CloudServerConfig sc : wcloud_servers.values()) {
                    if (wrand <= (rcount += sc.weight)) {
                        peek = sc;
                        break;
                    }
                }
                current_server = peek;
            }
        }
        return new CloudServerConfig[]{current_server};
    }


}