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
 * Date: 14/07/11
 *
 */
/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

import com.scientiamobile.wurflcloud.utils.AeSimpleMD5;
import com.scientiamobile.wurflcloud.utils.Base64;
import com.scientiamobile.wurflcloud.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;

/**
 * This example demonstrates the use of the {link ResponseHandler} to simplify
 * the process of processing the HTTP response and releasing associated resources.
 */
public class ClientWithResponseHandler {

    private static final String reqPath="/v1/json/search:('resolution_height')";
    private static final String userAgent = "Mozilla/5.0 (Linux; U; Android 2.2; xx-xx; HTC-A9192/1.0 Build/FRG83D) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
    private static final CloudClientConfig cfg= new CloudClientConfig();

    public final static void main(String[] args) throws Exception {

        try {
            String api_type = Constants.API_TYPE;
            String host = cfg.getCloudHost().host;
            URL httpget = new URL(api_type+"://"+host+reqPath);
            URLConnection connection = httpget.openConnection();
            connection.setDoOutput(true);

            connection.addRequestProperty("User-Agent", userAgent);

            String encoded = Base64.encode("123456:" + getSignature("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")).trim();
            System.out.println(encoded);
            connection.addRequestProperty("Authorization", "Basic " + encoded);

            connection.addRequestProperty("Connection", "Close");

            System.out.println("executing request " + httpget);

        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
           // httpclient.getConnectionManager().shutdown();
        }
    }

    private static String getSignature(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    	String a = reqPath+userAgent+password;
        return AeSimpleMD5.MD5(a);
    }

}
