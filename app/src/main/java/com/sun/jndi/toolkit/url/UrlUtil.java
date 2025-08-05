package com.sun.jndi.toolkit.url;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;

/* loaded from: rt.jar:com/sun/jndi/toolkit/url/UrlUtil.class */
public final class UrlUtil {
    private UrlUtil() {
    }

    public static final String decode(String str) throws MalformedURLException {
        try {
            return decode(str, "8859_1");
        } catch (UnsupportedEncodingException e2) {
            throw new MalformedURLException("ISO-Latin-1 decoder unavailable");
        }
    }

    public static final String decode(String str, String str2) throws MalformedURLException, UnsupportedEncodingException {
        try {
            return URLDecoder.decode(str, str2);
        } catch (IllegalArgumentException e2) {
            MalformedURLException malformedURLException = new MalformedURLException("Invalid URI encoding: " + str);
            malformedURLException.initCause(e2);
            throw malformedURLException;
        }
    }

    public static final String encode(String str, String str2) throws UnsupportedEncodingException {
        byte[] bytes = str.getBytes(str2);
        int length = bytes.length;
        char[] cArr = new char[3 * length];
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            if ((bytes[i3] >= 97 && bytes[i3] <= 122) || ((bytes[i3] >= 65 && bytes[i3] <= 90) || ((bytes[i3] >= 48 && bytes[i3] <= 57) || "=,+;.'-@&/$_()!~*:".indexOf(bytes[i3]) >= 0))) {
                int i4 = i2;
                i2++;
                cArr[i4] = (char) bytes[i3];
            } else {
                int i5 = i2;
                int i6 = i2 + 1;
                cArr[i5] = '%';
                int i7 = i6 + 1;
                cArr[i6] = Character.forDigit(15 & (bytes[i3] >>> 4), 16);
                i2 = i7 + 1;
                cArr[i7] = Character.forDigit(15 & bytes[i3], 16);
            }
        }
        return new String(cArr, 0, i2);
    }
}
