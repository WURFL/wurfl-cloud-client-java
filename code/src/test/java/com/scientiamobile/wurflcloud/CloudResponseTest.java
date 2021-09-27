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

import com.google.gson.Gson;
import org.testng.annotations.Test;

import java.io.IOException;
import static org.testng.Assert.assertNotNull;

/**
 * Date: 24/07/11
 *
 * @version $Id$
 */
@Test
public class CloudResponseTest extends Loggable {

    private final Gson gson = new Gson();

    public void testRawData() throws IOException {
        String rawData = "{\"apiVersion\":\"WurflCloud 2.1.6\",\"mtime\":1310581109,\"id\":\"mozilla_ver5\",\"capabilities\":{\"resolution_height\":600,\"resolution_width\":800},\"errors\":{}}";
        CloudResponse cloudResponse = gson.fromJson(rawData, CloudResponse.class);
        assertNotNull(cloudResponse);
        logger.info(cloudResponse.toString());
    }
}
