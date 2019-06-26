package com.data4truth.pi.util;



import org.apache.commons.lang.StringUtils;

import java.util.Base64;

/**
 * @author: lindj
 * @date: 2018/6/15 17:01
 * @description: base64编码
 */
public class Base64Util {
    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder();
    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();

    public static String encode(String value) {
        if (StringUtils.isEmpty(value)) {
            return "";
        }
        return encode(value.getBytes());
    }

    public static String encode(byte[] binaryData) {
        if (binaryData == null) {
            return null;
        }
        return new String(BASE64_ENCODER.encode(binaryData));
    }

    public static String decode2Sting(String encoded) {
        byte[] bytes = decode(encoded);
        return new String(bytes);
    }

    public static byte[] decode(String encoded) {
        if (encoded == null) {
            return null;
        }
        if (encoded.contains("=")) {
            encoded = encoded.substring(0, encoded.indexOf("="));
        }
        return BASE64_DECODER.decode(encoded);
    }
}
