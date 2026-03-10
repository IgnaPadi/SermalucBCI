package com.bci.user.common;

import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class EncodeDecode {

    /**
     * Codifica en Base64
     */
    public static String passwordEncode(String valueToEncode) {
        if (valueToEncode == null) return null;
        return Base64.getEncoder().encodeToString(valueToEncode.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decodifica un valor Base64
     */
    public static String passwordDecode(String valueToDecode) {
        if (valueToDecode == null) return null;
        byte[] decodedBytes = Base64.getDecoder().decode(valueToDecode);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
