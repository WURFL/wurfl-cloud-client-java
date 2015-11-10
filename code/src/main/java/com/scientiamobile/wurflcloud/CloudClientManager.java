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


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Serializable;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.scientiamobile.wurflcloud.cache.IWurflCloudCache;
import com.scientiamobile.wurflcloud.device.AbstractDevice;
import com.scientiamobile.wurflcloud.exc.WURFLCloudClientException;
import com.scientiamobile.wurflcloud.utils.Constants;
import com.scientiamobile.wurflcloud.utils.Credentials;

/**
 * WURFL Cloud Client Manager.
 */
public class CloudClientManager extends Loggable implements CloudListener, ICloudClientManager, Serializable {
    private static final long serialVersionUID = 2L;

    /**
     * The WURFL Cloud Server that will be used to request device information (e.g.. 'api.scientiamobile.com')
     *
     */
    private final CloudServerConfig wcloudHost;

    /**
     * The capabilities parsed from the .properties file
     */
    private final String[] parsedCapabilities;

    /**
     * The version of the WURFL Cloud Server
     *
     */
    protected String serverVersion;

    /**
     * The date that the WURFL Cloud Server's data was updated
     *
     */
    private long loadedDate;

    /**
     * Client configuration object
     *
     */
    protected final CloudClientConfig config;

    /**
     * Client cache object
     *
     */
    protected final IWurflCloudCache cache;

    /**
     * Auhentication credentials.
     */
    protected final Credentials credentials;

    /**
     * The software client version.
     */
    private final String clientVersion;

    /**
     * The Proxy used to connect (if not null)
     */
    private Proxy proxy;

    /**
     * Lock to cache purging
     */
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final Lock read = lock.readLock();
    private final Lock write = lock.writeLock();

    /**
     * CloudClientManager constructor.
     * @param authenticationManager
     * @param config
     * @param cache
     * @param proxy
     */
    public CloudClientManager(IAuthenticationManager authenticationManager, CloudClientConfig config, IWurflCloudCache cache, Proxy proxy) {
        this(authenticationManager, config, cache, proxy, null);
    }

    /**
     * Full constructor.
     * @param authenticationManager
     * @param config
     * @param cache
     * @param proxy
     * @param parsedCapabilities
     */
    public CloudClientManager(IAuthenticationManager authenticationManager, CloudClientConfig config, IWurflCloudCache cache, Proxy proxy, String[] parsedCapabilities) {
        this.config = config;
        clientVersion = Constants.CLIENT_VERSION;
        serverVersion = Constants.UNKNOWN;
        this.cache = cache;
        this.wcloudHost = this.config.getCloudHost();
        this.proxy = proxy;
        this.parsedCapabilities = parsedCapabilities;
        credentials = authenticationManager.splitApiKey();
    }

    /**
    * Retrieves the client version
    * @return The client version
    */
    public String getClientVersion() {
        return clientVersion;
    }

    private AbstractDevice detectDevice(CloudClient client) {
        read.lock();
        AbstractDevice dev = null;
        try {
            dev = client.detectDevice();
        } catch (SocketTimeoutException e) {
            throw new WURFLCloudClientException("Unable to contact server: socket timeout", -1);
        } catch (IOException e) {
            throw new WURFLCloudClientException("Unable to contact server: connection error", -1);
        } finally {
            read.unlock();
        }
        return dev;
    }

    /**
     * {@inheritDoc}
     */
    public AbstractDevice getDeviceFromUserAgent(String userAgent, String... search_capabilities) {
        CloudClient cc = new CloudClient(new DefaultCloudRequest(userAgent), null, config, (search_capabilities != null && search_capabilities.length > 0) ? search_capabilities : parsedCapabilities, credentials, cache, this, proxy);
        return detectDevice(cc);
    }

    /**
     * {@inheritDoc}
     */
    public AbstractDevice getDeviceFromRequest(HttpServletRequest request, HttpServletResponse response, String... search_capabilities) {
        CloudClient cc = new CloudClient(new DefaultCloudRequest(request), response, config, (search_capabilities != null && search_capabilities.length > 0) ? search_capabilities : parsedCapabilities, credentials, cache, this, proxy);
        return detectDevice(cc);
    }

    /**
     * Get the date that the WURFL Cloud Server was last updated.  This will be 0 if there
     * has not been a recent query to the server, or if the cached value was pushed out of memory
     *
     * @return int UNIX timestamp (seconds since Epoch)
     */
    protected long getLoadedDate() {
        return loadedDate;
    }

    /**
     * {@inheritDoc}
     */
    public boolean validateCache() {
        long mtime = cache.getMtime();
        if (mtime != loadedDate) {
            write.lock();
            try {
                cache.setMtime(loadedDate);
                if (config.autoPurge) {
                    return cache.purge();
                }
            } finally {
                write.unlock();
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public String getAPIVersion() {
        return serverVersion;
    }

    /**
     * {@inheritDoc}
     */
    public CloudServerConfig getCloudServer() {
        return wcloudHost;
    }

    /**
     * Make the webservice call to the server using the GET method and load the response.
     */
    public boolean testCallWurflCloud() {

        String ua = "useragent_test";
        Enumeration<Object> e = new Enumeration<Object>() {
            private boolean set = false;

            public boolean hasMoreElements() {
                return !set;
            }

            public Object nextElement() {
                if (!set) {
                    set = true;
                    return Constants.USER_AGENT_LC;
                }
                throw new IllegalStateException("finished objects");
            }
        };

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeaderNames()).thenReturn(e);
        when(request.getHeader(Constants.USER_AGENT_LC)).thenReturn(ua);

        HttpServletResponse response = mock(HttpServletResponse.class);

        // Determine the HTTP method to use and grab the response from the server
        CloudClient client = new CloudClient(new DefaultCloudRequest(request), response, config, new String[0], credentials, cache, this, proxy);
        return client.testCall(Constants.Encoding.PLAIN);
    }

    /**
     * Return the requesting client's User Agent
     *
     * @return string
     */
    protected String getUserAgent(Map<String, String> reqParams, Map<String, String> reqHeaders) {
        String user_agent = reqParams.get("UA");

        if (user_agent == null) {
            user_agent = reqHeaders.get(Constants.USER_AGENT_LC);
        }

        return user_agent;
    }

    public void processEvent(CloudEvent evt) {
        logger.debug("processEvent: " + evt);
        setLoadedDate(evt.getMtime());
        this.serverVersion = evt.getApiVersion();
    }

    private void setLoadedDate(long mtime) {
        this.loadedDate = mtime;
    }
}
