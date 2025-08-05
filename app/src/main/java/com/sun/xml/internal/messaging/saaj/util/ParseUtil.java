package com.sun.xml.internal.messaging.saaj.util;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/util/ParseUtil.class */
public class ParseUtil {
    private static char unescape(String s2, int i2) {
        return (char) Integer.parseInt(s2.substring(i2 + 1, i2 + 3), 16);
    }

    public static String decode(String s2) {
        StringBuffer sb = new StringBuffer();
        int i2 = 0;
        while (i2 < s2.length()) {
            char c2 = s2.charAt(i2);
            if (c2 != '%') {
                i2++;
            } else {
                try {
                    c2 = unescape(s2, i2);
                    i2 += 3;
                    if ((c2 & 128) != 0) {
                        switch (c2 >> 4) {
                            case 12:
                            case 13:
                                char c22 = unescape(s2, i2);
                                i2 += 3;
                                c2 = (char) (((c2 & 31) << 6) | (c22 & '?'));
                                break;
                            case 14:
                                char c23 = unescape(s2, i2);
                                int i3 = i2 + 3;
                                char c3 = unescape(s2, i3);
                                i2 = i3 + 3;
                                c2 = (char) (((c2 & 15) << 12) | ((c23 & '?') << 6) | (c3 & '?'));
                                break;
                            default:
                                throw new IllegalArgumentException();
                        }
                    }
                } catch (NumberFormatException e2) {
                    throw new IllegalArgumentException();
                }
            }
            sb.append(c2);
        }
        return sb.toString();
    }
}
