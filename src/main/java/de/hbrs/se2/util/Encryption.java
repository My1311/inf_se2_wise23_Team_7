package de.hbrs.se2.util;

import org.apache.commons.codec.digest.DigestUtils;

public class Encryption {
    public static String sha256(String value) {
        return DigestUtils.sha256Hex(value + "DirkIstCool");
    }
}
