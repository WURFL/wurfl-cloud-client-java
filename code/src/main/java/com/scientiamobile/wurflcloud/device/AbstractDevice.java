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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.scientiamobile.wurflcloud.ICloudClientRequest;
import com.scientiamobile.wurflcloud.Loggable;
import com.scientiamobile.wurflcloud.ResponseType;

/**
 * Device abstraction.
 */
public class AbstractDevice extends Loggable {
    protected final Map<String, Object> capabilityMap;
    protected final String id;
    protected final Map<String, String> errors;
    private final ResponseType source;
    private final ICloudClientRequest clientRequest;

    /**
     * Constructs a new AbstractDevice with the queried capabilities, response type, device identifier, a map of errors and a {@link ICloudClientRequest} object
     * @param capabilities The capabilities queried for this AbstractDevice
     * @param source The response type {@link ResponseType}
     * @param id The device identifier
     * @param errors A map of errors
     * @param clientRequest The client request
     */
    public AbstractDevice(Map<String, Object> capabilities, ResponseType source, String id, Map<String, String> errors, ICloudClientRequest clientRequest) {
        if (capabilities == null) throw new IllegalArgumentException("Argument must not be null");
        this.source = source;
        this.id = id;
        this.errors = errors != null ? errors : new HashMap<String, String>();
        capabilityMap = capabilities;
        this.clientRequest = clientRequest;
    }

    /**
     * Convenience constructor which takes the queried capabilities, response type, device identifier and a map of errors
     * @param capabilities The capabilities queried for this AbstractDevice
     * @param source The response type {@link ResponseType}
     * @param id The device identifier
     * @param errors A map of errors
     */
    public AbstractDevice(Map<String, Object> capabilities, ResponseType source, String id, Map<String, String> errors) {
        this(capabilities, source, id, errors, null);
    }

    /**
     * Gets the capability value corresponding to the requested capability name. If this capability isn't already available locally the WURFL Cloud server will be queried for it.
     * @param capabilityName The requested capability name
     * @return The capability value.
     */
    public Object get(String capabilityName) {
        // check the stored capabilities for this device
        Object value = capabilityMap.get(capabilityName);
        if (value == null) {
            // Must query the Cloud to check if the user is authorized to get the requested capability
            logger.debug("Requested unauthorized capability \'" + capabilityName + "\'. Querying the Cloud..");
            value = clientRequest.queryCloudForCapability(capabilityName, this);
        }
        return value;
    }

    /**
     * Gets a map of all the previously stored capabilities
     * @return A map containing the capability names and their correspondent values
     */
    public Map<String, Object> getCapabilities() {
        return Collections.unmodifiableMap(capabilityMap);
    }

    /**
     * Gets this AbstractDevice identifier
     * @return The device identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the response type.
     * @see {@link ResponseType}
     * 
     * Possible values are:
     * - cache:  from local cache
     * - cloud:  from WURFL Cloud Service
     * - client: from detection logic in the client
     * - null:   no detection was performed
     *
     * @return The response type
     */
    public ResponseType getSource() {
        return source;
    }

    /**
     * Gets the map of errors occurred during detection or Cloud query.
     * @return The stored map of errors
     */
    public Map<String, String> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "capabilityMap=" + capabilityMap +
                ", id='" + id + '\'' +
                ", errors=" + errors +
                ", source=" + source +
                '}';
    }
}
