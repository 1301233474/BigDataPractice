package com.common.util.encrypt;

public class EncryptUtils {

    public static String encryptByMd5(char[] password) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < password.length; i++) {
            stringBuffer.append(password[i] ^ password.length);
        }
        return MD5Utils.getMD5(stringBuffer.toString());
    }
}
