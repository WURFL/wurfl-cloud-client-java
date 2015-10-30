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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.scientiamobile.wurflcloud.cache.EhcachePremiumCloudCache;
import com.scientiamobile.wurflcloud.cache.HashMapPremiumCloudCache;
import com.scientiamobile.wurflcloud.cache.IWurflCloudCache;
import com.scientiamobile.wurflcloud.cache.SimpleCookieCache;
import com.scientiamobile.wurflcloud.cache.WurflCloudCache_Null;
import com.scientiamobile.wurflcloud.utils.Constants;

/**
 * Entry point for Cloud Client Application.
 */
public class CloudClientLoader extends Loggable implements Constants, Serializable {
	private static final long serialVersionUID = 2L;
	
	private final Properties properties = new Properties();
    private final CloudClientConfig config = new CloudClientConfig();
    private final ICloudClientManager managerI;

    private final String[] capabilities;

    /**
     * Constructs a CloudClientLoader by reading all the configuration properties (including the API Key) from the configuration file.
     * @throws Exception
     */
    public CloudClientLoader() throws Exception {
    	this (null, null, null);
    }
    
    /**
     * Constructs a CloudClientLoader with proxy configuration represented by a {@link Proxy} object
     * @param apiKey required key for Cloud Client use.
     * @throws Exception
     */
    public CloudClientLoader(String apiKey) throws Exception {
    	this(apiKey, null, null);
    }    
    
    /**
     * Constructs a CloudClientLoader with proxy configuration
     * @param apiKey     the API key
     * @param proxyIp    the proxy IP
     * @param proxyPort  the proxy port
     * @param proxyType  the {@link Proxy.Type} representing the proxy protocol
     * @throws Exception
     */
    public CloudClientLoader(String apiKey, String proxyIp, int proxyPort, Proxy.Type proxyType) throws Exception {
    	this(apiKey, new Proxy(proxyType, new InetSocketAddress(proxyIp, proxyPort)));
    }
    
    /**
     * Constructs a CloudClientLoader with proxy configuration
     * @param proxyIp    the proxy IP
     * @param proxyPort  the proxy port
     * @param proxyType  the {@link Proxy.Type} representing the proxy protocol
     * @throws Exception
     */
    public CloudClientLoader(String proxyIp, int proxyPort, Proxy.Type proxyType) throws Exception {
    	this(null, new Proxy(proxyType, new InetSocketAddress(proxyIp, proxyPort)));
    }
    
    /**
     * Constructs a CloudClientLoader without specific proxy configuration
     * @param the {@link Proxy} to be used to connect to cloud server
     * @throws Exception
     */
    public CloudClientLoader(Proxy proxy) throws Exception {
    	this(null, null, proxy);
    }
    
    /**
     * Constructs a CloudClientLoader without specific proxy configuration
     * @param apiKey required key for Cloud Client use.
     * @param the {@link Proxy} to be used to connect to cloud server
     * @throws Exception
     */
    public CloudClientLoader(String apiKey, Proxy proxy) throws Exception {
    	this(apiKey, null, proxy);
    }

    /**
     * Constructs a CloudClientLoader with proxy configuration represented by a {@link Proxy} object
     * @param apiKey required key for Cloud Client use.
     * @param the .properties file path to be used
     * @throws Exception
     */
    public CloudClientLoader(String apiKey, String propertiesFile) throws Exception {
    	this(apiKey, propertiesFile, null);
    }    
    
    /**
     * Constructs a CloudClientLoader with proxy configuration
     * @param apiKey     the API key
     * @param the .properties file path to be used
     * @param proxyIp    the proxy IP
     * @param proxyPort  the proxy port
     * @param proxyType  the {@link Proxy.Type} representing the proxy protocol
     * @throws Exception
     */
    public CloudClientLoader(String apiKey, String propertiesFile, String proxyIp, int proxyPort, Proxy.Type proxyType) throws Exception {
    	this(apiKey, propertiesFile, new Proxy(proxyType, new InetSocketAddress(proxyIp, proxyPort)));
    }
    
    /**
     * Constructs a CloudClientLoader without specific proxy configuration
     * @param apiKey required key for Cloud Client use.
     * @param the .properties file path to be used
     * @param the {@link Proxy} to be used to connect to cloud server
     * @throws Exception
     */
    public CloudClientLoader(String apiKey, String propertiesFile, Proxy proxy) throws Exception {

    	IWurflCloudCache cache = null;
        String[] capabilities = new String[0];

        if (apiKey != null && apiKey.length() > 0) {
            config.apiKey = apiKey;
        }
        
        if (propertiesFile == null) {
        	propertiesFile = WURFL_CLOUD_CONFIG_FILE;
        }

        try {
            InputStream stream = getClass().getResourceAsStream(propertiesFile);
            
            if (stream == null) {
            	stream = new FileInputStream(new File(propertiesFile));
            }
            
            properties.load(stream);
            stream.close();
            
            if (apiKey == null || apiKey.length() == 0) {
                String key = properties.getProperty(APIKEY);
                if (key == null || key.length() == 0) {
                    throw new IllegalArgumentException("You must supply a not empty Cloud Client API key");
                }
                config.apiKey = key;
            }

            String property = properties.getProperty(CACHE);
            if (property == null) {
                logger.warn("Unable to load '" + CACHE + "' property, setting default cache");
                property = defaultCacheType();
            }
            logger.info("setting cache type to " + property);
            cache = getStrategyCache(property);
            
            property = properties.getProperty(COMPRESSION);
            if (property == null) {
                logger.warn("Unable to load '" + COMPRESSION + "' property, setting default");
                property = defaultCompression();
            }
            logger.info("setting 'compression' to " + property);
            config.compression = Boolean.valueOf(property);

            property = properties.getProperty(CONNECTION_TIMEOUT_PROP);
            if (property != null) {
                try {
                    int timeout = Integer.parseInt(property);
                    if ( timeout >= 0 ) {
                        logger.info("setting 'connection_timeout' to " + timeout + " mSec");
                        config.connectionTimeout = timeout;
                    }
                    else {
                        logger.warn("Unable to load '" + CONNECTION_TIMEOUT_PROP + "' property (not an integer value >= 0) , using default (" + DEFAULT_CONNECTION_TIMEOUT + " mSec)");
                        config.connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
                    }
                } catch(NumberFormatException e){
                    logger.warn("Unable to load '" + CONNECTION_TIMEOUT_PROP + "' property (not a valid integer value) , using default (" + DEFAULT_CONNECTION_TIMEOUT + " mSec)");
                    config.connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
                }
            }

            property = properties.getProperty(READ_TIMEOUT_PROP);
            if (property != null) {
                try {
                    int timeout = Integer.parseInt(property);
                    if ( timeout >= 0 ) {
                        logger.info("setting 'read_timeout' to " + timeout + " mSec");
                        config.readTimeout = timeout;
                    }
                    else {
                        logger.warn("Unable to load '" + READ_TIMEOUT_PROP + "' property (not an integer value >= 0) , using default (" + DEFAULT_CONNECTION_TIMEOUT + " mSec)");
                        config.readTimeout = DEFAULT_READ_TIMEOUT;
                    }
                } catch(NumberFormatException e){
                    logger.warn("Unable to load '" + READ_TIMEOUT_PROP + "' property (not a valid integer value) , using default (" + DEFAULT_CONNECTION_TIMEOUT + " mSec)");
                    config.readTimeout = DEFAULT_READ_TIMEOUT;
                }
            }

            property = properties.getProperty(CACHE + ".autoPurge");
            if (property == null) {
                logger.warn("Unable to load 'autoPurge' property, setting default");
                property = String.valueOf(DEFAULT_AUTO_PURGE);
            }
            logger.info("setting 'autoPurge' to " + property);
            config.autoPurge = Boolean.valueOf(property);

            List<String> capList = new ArrayList<String>();
            for (Object propertyObject : properties.keySet()) {
            	String propertyString = (String) propertyObject;
            	if (propertyString.startsWith(CAPABILITY_PREFIX)) {
                    capList.add(properties.getProperty(propertyString));
                }
            }

            if (capList.size() == 0) {
                logger.debug("no capabilities in config, server will give back all authorized ones");
            } else {
            	capabilities = capList.toArray(new String[]{});
            }
            
            // Proxy
            if (proxy == null) { // ignore if passed programmatically
            	String address = properties.getProperty(PROXY_URL);
            	
            	if (address != null) {
	            	String portStr = properties.getProperty(PROXY_PORT);
	            	String typeStr = properties.getProperty(PROXY_TYPE);
	            	
	            	try {
	            		int port = Integer.valueOf(portStr);
	            		Proxy.Type type = Proxy.Type.valueOf(typeStr);
	            		
	            		try {
	            			proxy = new Proxy(type, new InetSocketAddress(address, port));
	            			logger.info("wurfl-cloud initialized with proxy from properties file on " + address + ":" + portStr + " of type " + proxy.type().toString());
	            			
	            		} catch (IllegalArgumentException e) {
	                		logger.warn("unable to setup proxy from properties file; invalid values: " + e.getLocalizedMessage());
	                	}            			
	            		
	            	} catch (NumberFormatException e) {
	            		logger.warn("unable to setup proxy from properties file; invalid port: " + portStr);
	            	} catch (IllegalArgumentException e) {
	            		logger.warn("unable to setup proxy from properties file; invalid proxy type: " + typeStr +  " - see java.net.Proxy.Type javadoc for valid values");
	            	} catch (NullPointerException e) {
	            		logger.warn("unable to setup proxy from properties file; invalid proxy type: " + typeStr +  " - see java.net.Proxy.Type javadoc for valid values");
	            	}
            	}
            }
            
            property = properties.getProperty(CACHE + ".reportInterval");
            if (property == null) {
                logger.warn("Unable to load 'reportInterval' property, setting default");
                property = String.valueOf(DEFAULT_REPORT_INTERVAL);
            }
            logger.info("setting 'reportInterval' to " + property);
            config.reportInterval = Integer.valueOf(property);
            
        } catch (FileNotFoundException exception) {
        	logger.warn("Configuration file not found at path: " + propertiesFile + ", proceeding with defaults");
        } catch (NumberFormatException exception) {
        	logger.warn("Could not parse cache's reportInterval parameter, not a valid integer value.");
        	config.reportInterval = DEFAULT_REPORT_INTERVAL;
        }
        
        if (cache == null) {
            cache = getStrategyCache(defaultCacheType());
        }
        this.capabilities = capabilities;

        IAuthenticationManager am = new AuthenticationManager(config);
        managerI = new CloudClientManager(am, config, cache, proxy, capabilities);

    }
    
    /**
     * Returns the default compression value to be used.
     * @return The default compression value
     */
    private String defaultCompression() {
        return String.valueOf(DEFAULT_COMPRESSION);
    }
    
    /**
     * Returns the default cache type to be used. 
     * @return The default cache type
     */
    private String defaultCacheType() {
        return CacheType.Cookie.name();
    }

    /**
     * Builds and returns a specific cache instance to be used.
     * @param property The desired cache type
     * @return A specific cache instance
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    private IWurflCloudCache getStrategyCache(String property) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        CacheType type = CacheType.valueOf(property);
        IWurflCloudCache ret = null;
        switch (type) {
            case None:
                ret = new WurflCloudCache_Null();
                break;
            case HashMap:

                String prop = properties.getProperty(CACHE + ".HashMap.initialCapacity");
                if (prop == null) {
                    logger.warn("Unable to load initialCapacity property, setting default");
                    prop = String.valueOf(DEFAULT_CAPACITY);
                }
                int capacity = Integer.valueOf(prop);
                prop = properties.getProperty(CACHE + ".HashMap.loadFactor");
                if (prop == null) {
                    logger.warn("Unable to load loadFactor property, setting default");
                    prop = String.valueOf(DEFAULT_LOAD_FACTOR);
                }
                float loadFactor = Float.valueOf(prop);
                prop = properties.getProperty(CACHE + ".HashMap.concurrentWrites");
                if (prop == null) {
                    logger.warn("Unable to load concurrentWrites property, setting default");
                    prop = String.valueOf(DEFAULT_CONCURRENT_WRITES);
                }
                int concurrentWrites = Integer.valueOf(prop);
                ret = new HashMapPremiumCloudCache(capacity, loadFactor, concurrentWrites);
                break;
            case Cookie:
                ret = new SimpleCookieCache();
                break;
            case Ehcache:
                ret = new EhcachePremiumCloudCache();
                break;
            default:
                throw new IllegalArgumentException("To be implemented for " + type);
        }
        logger.info("Created cache " + ret.getClass().getSimpleName());
        return ret;
    }

    /**
     * Returns the cloud client manager
     * @return The cloud client manager instance
     */
    public ICloudClientManager getClientManager() {
        return managerI;
    }

    /**
     * Returns the capabilities application queries for
     * @return The capabilities application queries for
     */
    public String[] getSearchedCapabilities() {
        return capabilities;
    }

}
