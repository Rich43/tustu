package java.net;

import java.io.UnsupportedEncodingException;

/* loaded from: rt.jar:java/net/URLDecoder.class */
public class URLDecoder {
    static String dfltEncName = URLEncoder.dfltEncName;

    @Deprecated
    public static String decode(String str) {
        String strDecode = null;
        try {
            strDecode = decode(str, dfltEncName);
        } catch (UnsupportedEncodingException e2) {
        }
        return strDecode;
    }

    public static String decode(String str, String str2) throws UnsupportedEncodingException {
        boolean z2 = false;
        int length = str.length();
        StringBuffer stringBuffer = new StringBuffer(length > 500 ? length / 2 : length);
        int i2 = 0;
        if (str2.length() == 0) {
            throw new UnsupportedEncodingException("URLDecoder: empty string enc parameter");
        }
        byte[] bArr = null;
        while (i2 < length) {
            char cCharAt = str.charAt(i2);
            switch (cCharAt) {
                case '%':
                    if (bArr == null) {
                        try {
                            bArr = new byte[(length - i2) / 3];
                        } catch (NumberFormatException e2) {
                            throw new IllegalArgumentException("URLDecoder: Illegal hex characters in escape (%) pattern - " + e2.getMessage());
                        }
                    }
                    int i3 = 0;
                    while (i2 + 2 < length && cCharAt == '%') {
                        int i4 = Integer.parseInt(str.substring(i2 + 1, i2 + 3), 16);
                        if (i4 < 0) {
                            throw new IllegalArgumentException("URLDecoder: Illegal hex characters in escape (%) pattern - negative value");
                        }
                        int i5 = i3;
                        i3++;
                        bArr[i5] = (byte) i4;
                        i2 += 3;
                        if (i2 < length) {
                            cCharAt = str.charAt(i2);
                        }
                    }
                    if (i2 < length && cCharAt == '%') {
                        throw new IllegalArgumentException("URLDecoder: Incomplete trailing escape (%) pattern");
                    }
                    stringBuffer.append(new String(bArr, 0, i3, str2));
                    z2 = true;
                    break;
                    break;
                case '+':
                    stringBuffer.append(' ');
                    i2++;
                    z2 = true;
                    break;
                default:
                    stringBuffer.append(cCharAt);
                    i2++;
                    break;
            }
        }
        return z2 ? stringBuffer.toString() : str;
    }
}
