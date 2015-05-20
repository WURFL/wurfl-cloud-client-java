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

import org.codehaus.jackson.map.ObjectMapper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertNotNull;

/**
 * @version $Id$
 * @since 2.0
 */
@Test
public class CloudResponseTest extends Loggable {
    ObjectMapper mapper;

    @BeforeClass
    public void setup() {
        mapper = new ObjectMapper();
    }

    public void testRawData() throws IOException {
        String rawData = "{\"apiVersion\":\"WurflCloud 2.1.6\",\"mtime\":1310581109,\"id\":\"mozilla_ver5\",\"capabilities\":{\"resolution_height\":600,\"resolution_width\":800},\"errors\":{}}";
        CloudResponse cloudResponse = mapper.readValue(rawData, CloudResponse.class);
        assertNotNull(cloudResponse);
        logger.info(cloudResponse.toString());
    }
}
