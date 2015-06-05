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

/**
 * Date: 14/07/11
 *
 * @version $Id$
 */
public class Base64 {

    private static final String base64code = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz" + "0123456789" + "+/";

    private static final int splitLinesAt = 76;

    public static byte[] zeroPad(int length, byte[] bytes) {
        byte[] padded = new byte[length];
        System.arraycopy(bytes, 0, padded, 0, bytes.length);
        return padded;
    }

    public static String encode(String string) {

        String encoded = "";
        byte[] stringArray;
        try {
            stringArray = string.getBytes("UTF-8");
        } catch (Exception ignored) {
            stringArray = string.getBytes();
        }
        int paddingCount = (3 - (stringArray.length % 3)) % 3;
        stringArray = zeroPad(stringArray.length + paddingCount, stringArray);
        for (int i = 0; i < stringArray.length; i += 3) {
            int j = ((stringArray[i] & 0xff) << 16) +
                    ((stringArray[i + 1] & 0xff) << 8) +
                    (stringArray[i + 2] & 0xff);
            encoded = encoded + base64code.charAt((j >> 18) & 0x3f) +
                    base64code.charAt((j >> 12) & 0x3f) +
                    base64code.charAt((j >> 6) & 0x3f) +
                    base64code.charAt(j & 0x3f);
        }
        return splitLines(encoded.substring(0, encoded.length() -
                paddingCount) + "==".substring(0, paddingCount));

    }

    public static String splitLines(String string) {

        String lines = "";
        for (int i = 0; i < string.length(); i += splitLinesAt) {

            lines += string.substring(i, Math.min(string.length(), i + splitLinesAt));
            lines += "\r\n";

        }
        return lines;

    }

    public static void main(String[] args) {

        for (int i = 0; i < args.length; i++) {

            System.err.println("encoding \"" + args[i] + "\"");
            System.out.println(encode(args[i]));

        }

    }

}
