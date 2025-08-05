package com.sun.org.apache.xml.internal.utils;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/XMLCharacterRecognizer.class */
public class XMLCharacterRecognizer {
    public static boolean isWhiteSpace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
    }

    public static boolean isWhiteSpace(char[] ch, int start, int length) {
        int end = start + length;
        for (int s2 = start; s2 < end; s2++) {
            if (!isWhiteSpace(ch[s2])) {
                return false;
            }
        }
        return true;
    }

    public static boolean isWhiteSpace(StringBuffer buf) {
        int n2 = buf.length();
        for (int i2 = 0; i2 < n2; i2++) {
            if (!isWhiteSpace(buf.charAt(i2))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isWhiteSpace(String s2) {
        if (null != s2) {
            int n2 = s2.length();
            for (int i2 = 0; i2 < n2; i2++) {
                if (!isWhiteSpace(s2.charAt(i2))) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }
}
