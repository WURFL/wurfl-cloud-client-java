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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.scientiamobile.wurflcloud.cache.IWurflCloudCache;
import com.scientiamobile.wurflcloud.device.AbstractDevice;
import com.scientiamobile.wurflcloud.device.CacheDevice;
import com.scientiamobile.wurflcloud.device.CloudDevice;
import com.scientiamobile.wurflcloud.exc.UnreachableServerException;
import com.scientiamobile.wurflcloud.exc.WURFLCloudClientException;
import com.scientiamobile.wurflcloud.utils.AuthorizationUtils;
import com.scientiamobile.wurflcloud.utils.Constants;
import com.scientiamobile.wurflcloud.utils.Credentials;

import static com.jsoniter.JsonIterator.deserialize;


/**
 * Cloud thin client, associated to every request.
 */
public class CloudClient extends Loggable implements ICloudClientRequest, Constants, Serializable {
    private static final long serialVersionUID = 2L;
    
    private static final Set<String> FILTERED_HEADERS = new HashSet<String>();
    static {
        FILTERED_HEADERS.add("content-length");
        FILTERED_HEADERS.add("content-type");
    }
    
    private final CloudClientConfig config;
    private final String[] searchCapabilities;
    private final Credentials credentials;
    private final IWurflCloudCache cache;
    private final Set<CloudListener> listeners = new HashSet<CloudListener>();
    
    private static final int HTTP_ERROR_INVALID_KEY = 401;
    private static final int HTTP_ERROR_MISSING_KEY = 402;
    private static final int HTTP_ERROR_EXPIRED_KEY = 403;
    private static final int HTTP_ERROR_UNREACHABLE = 404;
    private static final int HTTP_ERROR_JSON_KEY = 500;

    /**
     * The HTTP Headers that will be used to query the WURFL Cloud Server in Map<String, String> format.
     *
     * @var array
     */
    private final Map<String, String> reqHeaders = new LinkedHashMap<String, String>();

    private String userAgent;

    /**
     * The incoming request, not propagated to the Cloud Server!
     */
    private final HttpServletRequest request;
    
    /**
     * The response that will be served to the client application.
     */
    private final HttpServletResponse response;

    /**
     * The path to the Cloud Server
     */
    private final String reqPath;

    /**
     * The Proxy object used to connect
     */
    private final Proxy proxy;

    /**
     * Construct a new CloudClient object
     *
     * @param request
     * @param response
     * @param cfg
     * @param searchCapabilities
     * @param credentials
     * @param cache
     * @param recoveryManager
     * @param clientManager
     */
    protected CloudClient(HttpServletRequest request, HttpServletResponse response, CloudClientConfig cfg, String[] searchCapabilities, Credentials credentials, IWurflCloudCache cache, CloudClientManager clientManager, Proxy proxy) {
        this.response = response;
        this.request = request;
        this.searchCapabilities = searchCapabilities;
        this.config = cfg;
        this.credentials = credentials;
        this.cache = cache;
        this.proxy = proxy;
        this.userAgent = "";
        addCloudListener(clientManager);
        reqPath = initialize();
    }

    protected CloudClient(String userAgent, HttpServletResponse response, CloudClientConfig cfg, String[] searchCapabilities, Credentials credentials, IWurflCloudCache cache, CloudClientManager clientManager, Proxy proxy) {
        this.response = response;
        this.request = null;
        this.searchCapabilities = searchCapabilities;
        this.config = cfg;
        this.credentials = credentials;
        this.cache = cache;
        this.proxy = proxy;
        this.userAgent = userAgent;
        addCloudListener(clientManager);
        reqPath = initialize();
    }

    public void addCloudListener(CloudListener l) {
        listeners.add(l);
    }

    public void removeCloudListener(CloudListener l) {
        listeners.remove(l);
    }

    /**
     * Initializes the data for the server call.
     */
    private String initialize() {

        logger.info("starting initialize");
        // If the reportInterval is enabled and past the report age, include the report data in the next request
        if (config.reportInterval > 0 && cache.getReportAge() >= config.reportInterval) {
            addReportDataToRequest();
        }

        if (request != null) {
            //All headers are set in lowercase, to clear various java server differences
            Enumeration<?> headerNames = request.getHeaderNames();
            HashMap<String, String> uaMap = new HashMap();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String headerName = (String) headerNames.nextElement();
                    String headerNameLC = headerName.toLowerCase();
                    
                    if ( headerNameLC.compareTo(REMOTE_ADDR_LC) != 0 || 
                    	headerNameLC.compareTo(X_FORWARDED_FOR_LC) != 0 || 
                    	headerNameLC.compareTo(ACCEPT_LC) != 0 || 
                    	headerNameLC.compareTo(X_WAP_PROFILE_LC) != 0 ||
                    	headerNameLC.compareTo(X_REQUESTED_WITH_LC) != 0 ) {

                    	logger.info("putting " + headerNameLC);
	                    reqHeaders.put(headerNameLC, request.getHeader(headerName));
                    }

                    //check if header is one that contains ua info and store it temporarely
                    for (String header : HEADERS) {
                        if (headerNameLC.equalsIgnoreCase(header)) {
                        	uaMap.put(headerNameLC, request.getHeader(headerName));
                            break;
                        } 
                    }
                }

                //get the best user-agent value from headers stored before
                for (String header : HEADERS) {
                    if (uaMap.get(header.toLowerCase())!= null) {
                    	userAgent = uaMap.get(header);
                        break;
                    } 
                }
            }        	
        } else {
        	reqHeaders.put(USER_AGENT_LC, userAgent);
        }

        if (userAgent.length() == 0) {
            logger.warn("The User-Agent is empty.");
        } else if (userAgent.length() > Constants.USER_AGENT_MAX_LENGTH) {
            userAgent = userAgent.substring(0, Constants.USER_AGENT_MAX_LENGTH);
        }
        
        logger.info(USER_AGENT_LC + ": " + userAgent);
        
        // add X-Forwarded-For        
        String ip = reqHeaders.get(REMOTE_ADDR_LC);
        if (ip != null) {
            String fwd = reqHeaders.get(X_FORWARDED_FOR_LC);
            if (fwd != null) {
                addRequestHeader(X_FORWARDED_FOR, ip + ", " + fwd);
            } else {
                addRequestHeader(X_FORWARDED_FOR, ip);
            }
        } else {
        	String remoteAddr = null;
        	
        	if ( request != null) 
        		remoteAddr = request.getRemoteAddr();
        	
            if (remoteAddr != null) addRequestHeader(X_FORWARDED_FOR, remoteAddr);
        }

        // add X-Accept
        String accept = reqHeaders.get(ACCEPT_LC);
        if (accept != null) {
            addRequestHeader(X_ACCEPT, accept);
            removeRequestHeader(ACCEPT_LC);
        } 
        addRequestHeader(ACCEPT_LC, "*/*");
        	
        
        // add X-Wap-Profile
        String xWapProfile = reqHeaders.get(X_WAP_PROFILE_LC);
        if (xWapProfile != null) {
            addRequestHeader(X_WAP_PROFILE, xWapProfile);
        }
        
        String reqPath = AuthorizationUtils.buildRequestPath(searchCapabilities);

        addOtherHeaders(reqPath);
        logger.info("Headers map at initialize end: " + reqHeaders);
        return reqPath;
    }

    /**
     * Fill request with all needed headers.
     *
     * @param reqPath The request path
     */
    private void addOtherHeaders(String reqPath) {
        setEncodingAccept();
        addRequestHeader("X-Cloud-Client", "WurflCloudClient/Java_" + CLIENT_VERSION);
        addRequestHeader("Connection", "Close");
        addRequestHeader(AUTHORIZATION, AuthorizationUtils.getBasicAuthString(credentials));
    }

    /**
     * Retrieves the report data from the cache provider and adds it to the request
     * parameters to be included with the next server call.
     */
    private void addReportDataToRequest() {
        Map<String, Long> counters = cache.getCounters();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Long> cacheItem : counters.entrySet()) {
            sb.append(cacheItem.getKey()).append(":").append(cacheItem.getValue()).append(",");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }

        addRequestHeader("X-Cloud-Counters", sb.toString());
        cache.resetReportAge();
        cache.resetCounters();
    }

    /**
     * Convenience method to add a specific header name and value to the other headers.
     *
     * @param headerName The header name
     * @param headerValue The header value
     * @return This object
     */
    private CloudClient addRequestHeader(String headerName, String headerValue) {
        reqHeaders.put(headerName, headerValue);
        return this;
    }

    /**
     * Convenience method to remove a specific header from the headers will be sent to server.
     *
     * @param headerName The header name
     * @return This object
     */
    private CloudClient removeRequestHeader(String headerName) {
        reqHeaders.remove(headerName);
        return this;
    }

    /**
     * Add encoding header, if required by the configuration.
     *
     * @return This object
     */
    private CloudClient setEncodingAccept() {
        if (config.compression) {
            addRequestHeader("X-Accept-Encoding", Encoding.GZIP.val);
        }
        return this;
    }

    /**
     * Try a test call, using a specific encoding.
     *
     * @param enc The desired encoding
     * @return True if the call completed successfully, false otherwise
     */
    public boolean testCall(Encoding enc) {

        String api_type = Constants.API_TYPE;
        String host = config.getCloudHost().host;
        String reqPath = AuthorizationUtils.buildRequestPath(new String[]{"is_wireless_device"});
        String reqString = api_type + "://" + host + reqPath;
        logger.info("Request: " + reqString);

        try {
            URLConnection connection;
            if (proxy != null) {
                connection = new URL(reqString).openConnection(proxy);
            } else {
                connection = new URL(reqString).openConnection();
            }
            logger.info(connection.toString());
            switch (enc) {
                case GZIP:
                    connection.addRequestProperty("X-Accept-Encoding", Encoding.GZIP.val);
                    break;
                default:
            }
            connection.addRequestProperty("User-Agent", userAgent);
            connection.addRequestProperty("Authorization", AuthorizationUtils.getBasicAuthString(credentials));

            String message = processContent(connection);

            logger.info(message);

            CloudResponse cloudResponse = processResponse(message);

            updateListeners(cloudResponse);
            return true;
        } catch (Exception e) {
            logger.error(e.toString());
            return false;
        }
    }

    /**
     * Parses the response into the CloudResponse object.
     * 
     * @param rawData The raw data on which the response is built
     * @return A CloudResponse object containing the Cloud response
     * @throws WURFLCloudClientException If the parser could not read the passed raw data
     */
    private CloudResponse processResponse(String rawData) {
        try {
            return deserialize(rawData, CloudResponse.class);
        } catch (Exception e) {
            throw new WURFLCloudClientException("", HTTP_ERROR_JSON_KEY);
        }
    }
    
    /**
     * Compose the request URL to be used when connecting to the Cloud
     * 
     * @return
     */
    private String buildRequestURL() {
        String api_type = Constants.API_TYPE;
        String host = config.getCloudHost().host;
        String reqString = api_type + "://" + host + reqPath;
        logger.info("Request: " + reqString);
        return reqString;
    }

    /**
     * Create an URLConnection object using a URL string
     * 
     * @param request
     * @return
     */
    private URLConnection setupUrlConnection(String request) throws IOException {
        URLConnection connection = null;

        if (proxy != null) {
            connection = new URL(request).openConnection(proxy);
        } else {
            connection = new URL(request).openConnection();
        }
        
        logger.debug("Setting connection timeout: " + config.connectionTimeout + " mSec");
        connection.setConnectTimeout(config.connectionTimeout);

        logger.debug("Setting read timeout: " + config.readTimeout + " mSec");
        connection.setReadTimeout(config.readTimeout);
        
        if (Constants.API_TYPE.equals(Constants.API_HTTPS) && connection instanceof HttpURLConnection) {
            logger.info("Explicitly setting connection method to GET");
            ((HttpURLConnection)connection).setRequestMethod("GET");
        }
        
        logger.info(connection.toString());
        logger.info("Incoming connection headers count: " + reqHeaders.size());
        for (Map.Entry<String, String> entry : reqHeaders.entrySet()) {
            if (FILTERED_HEADERS.contains(entry.getKey().toLowerCase())) {
                logger.info("filtering entry: " + entry);
            } else {
                logger.info("   adding entry: " + entry);
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        
        Map<String, List<String>> headers = connection.getRequestProperties();
        logger.info("Outgoing connection headers count: " + headers.size());
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            logger.info("Outgoing Header: " + entry.getKey() + " -> " + entry.getValue());
        }

        return connection;
    }
    
    /**
     * {@inheritDoc}
     */
    public Object queryCloudForCapability(String capabilityName, AbstractDevice device) {
        String reqString = buildRequestURL();
        Object cap = null;
        try {
            URLConnection connection = setupUrlConnection(reqString);
            
            checkHttpConnectionOrThrow(connection);

            String message = processContent(connection);
            CloudResponse response = processResponse(message);
            updateCache(device, response.getMtime());
            cap = response.getCapabilities().get(capabilityName);
            if (cap == null) {
                throw new IllegalArgumentException("You're not authorized to retrieve the capability \'" + capabilityName + "\'");
            }
        } catch (IOException e) {
            logger.error(e.toString());
        }
        return cap;
    }
    
    /**
     * Queries cache, then cloud.
     * If cloud is not available, tries a recovery answer.
     *
     * @return
     * @throws IOException 
     */
    protected AbstractDevice detectDevice() throws IOException {
        AbstractDevice device = null;
    	
        if (request != null)
            device = cache.getDevice(request, this);
        // if request == null we came here from getDeviceFromUA
        // we need to check cache directly with UA
        else
            device = cache.getDeviceFromID(this.userAgent);
    	    	
        if (device != null) {
            //check if capabilities search is changed
            if (searchCapabilities != null && searchCapabilities.length > 0) {
                Map<String, Object> capabilities = device.getCapabilities();
                for (String searchCapability : searchCapabilities) {
                    if (!capabilities.containsKey(searchCapability)) {
                        device = null;
                        logger.info("capability not found, must query Cloud: " + searchCapability);
                        break;
                    }
                }
            }
        }

        if (device == null) {
            String reqString = buildRequestURL();
            URLConnection connection = setupUrlConnection(reqString);
            
            checkHttpConnectionOrThrow(connection);
            
            String message = processContent(connection);
            CloudResponse cloudResponse = processResponse(message);

            // check if capabilities search is changed and unauthorized search will raise an exception
            if (searchCapabilities != null && searchCapabilities.length > 0) {
                Map<String, Object> capabilities = cloudResponse.getCapabilities();
                for (String searchCapability : searchCapabilities) {
                    if (capabilities.get(searchCapability) == null) {
                        throw new IllegalArgumentException("The requested capability '" + searchCapability + "' is invalid or you are not subscribed to it.");
                    }
                }
            }

            long mtime = updateListeners(cloudResponse);
            device = new CloudDevice(cloudResponse, this);
            updateCache(device, mtime);
        }
        return device;
    }
    
    private void checkHttpConnectionOrThrow(URLConnection connection) throws IOException {
        int httpResponseCode = HTTP_ERROR_UNREACHABLE;
        if (connection instanceof HttpURLConnection) {
            httpResponseCode = ((HttpURLConnection)connection).getResponseCode();
        }
        
        if (httpResponseCode >= 400) {
            switch (httpResponseCode) {
                case HTTP_ERROR_INVALID_KEY:
                    throw new WURFLCloudClientException("Invalid API key", httpResponseCode);
                case HTTP_ERROR_MISSING_KEY:
                    throw new WURFLCloudClientException("No API key was provided", httpResponseCode);
                case HTTP_ERROR_EXPIRED_KEY:
                    throw new WURFLCloudClientException("API key is expired or revoked", httpResponseCode);
                default:
                    throw new UnreachableServerException("The WURFL Cloud service returned an unexpected response: " + httpResponseCode);
            }
        }
        logger.info("URLConnection to cloud returned correctly");
    }

    /**
     * Notify listeners and return server mtime.
     *
     * @param cloudResponse
     * @return server mtime
     */
    private long updateListeners(CloudResponse cloudResponse) {
        long mtime = cloudResponse.getMtime();
        String apiVersion = cloudResponse.getApiVersion();

        CloudEvent evt = new CloudEvent(this, mtime, apiVersion);
        for (CloudListener listener : listeners) {
            listener.processEvent(evt);
        }
        return mtime;
    }

    /**
     * Reads response from cloud.
     *
     * @param connection
     * @return
     * @throws IOException
     */
    private String processContent(URLConnection connection) throws IOException {
        final char[] buffer = new char[0x10000];
        StringBuilder out = new StringBuilder();
        logger.info("Trying to get InputStream from Connection...");
        InputStream is = connection.getInputStream();
        if (is == null) {
            logger.error("Failed, InputStream is NULL");
        } else {
            logger.info("InputStream received");
        }
        InputStreamReader in = new InputStreamReader(getStream(getEncodingType(connection), is));
        int read;
        do {
            logger.debug("Trying to read " + buffer.length + " bytes...");
            read = in.read(buffer, 0, buffer.length);
            if (read >= 0) {
                logger.debug(read + " bytes received");
            } else {
                logger.info("EOF received");
            }
            if (read > 0) {
                out.append(buffer, 0, read);
            }
        } while (read >= 0);
        String message = out.toString();

        logger.info("message: " + message);
        return message;
    }


    /**
     * Read header from response, and sets encoding for right stream to read response..
     *
     * @param connection
     * @return encoding enum type
     */
    private Encoding getEncodingType(URLConnection connection) {
        String encoding = connection.getHeaderField("Content-Encoding");
        if (encoding != null && encoding.equals("gzip")) {
            return Encoding.GZIP;
        }
        return Encoding.PLAIN;
    }

    /**
     * Sets InputStream to read response.
     *
     * @param type
     * @param in
     * @return
     * @throws IOException
     */
    private InputStream getStream(Encoding type, InputStream in) throws IOException {
        switch (type) {
            case GZIP:
                return new GZIPInputStream(in);
            default:
                return in;
        }
    }

    private void updateCache(AbstractDevice device, long mtime) {
    	String userAgent = "";
    	if (request != null) {
            userAgent = request.getHeader(USER_AGENT_LC);
            if (userAgent == null || userAgent.length() == 0) {
                userAgent = request.getHeader("User-Agent");
            }    		
    	} else {
    		userAgent = this.userAgent;
    	}
        cache.setDevice(response, userAgent, new CacheDevice(device));
        cache.setMtime(mtime);
    }
}
