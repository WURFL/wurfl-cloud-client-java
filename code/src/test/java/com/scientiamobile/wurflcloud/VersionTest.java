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

import junit.framework.Assert;

import org.testng.annotations.Test;

import com.scientiamobile.wurflcloud.utils.Constants;

@Test(groups = "unit")
public class VersionTest extends Loggable {
	
	/**
	 * Ensures that the version reported in pom.xml matches the
	 * String value returned from Constants.CLIENT_VERSION
	 */
	@Test
	public void versionReadTest() {
		String versionStr = System.getProperty("wurflcloudversion");
		logger.debug("Version read from pom.xml: " + versionStr);
		logger.debug("Version read from Constants.java: " + Constants.CLIENT_VERSION);
		Assert.assertEquals(versionStr, Constants.CLIENT_VERSION);
	}

}
