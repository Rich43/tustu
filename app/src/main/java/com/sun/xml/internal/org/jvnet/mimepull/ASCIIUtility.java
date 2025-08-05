package com.sun.xml.internal.org.jvnet.mimepull;

import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/ASCIIUtility.class */
final class ASCIIUtility {
    private ASCIIUtility() {
    }

    public static int parseInt(byte[] b2, int start, int end, int radix) throws NumberFormatException {
        int limit;
        if (b2 == null) {
            throw new NumberFormatException(FXMLLoader.NULL_KEYWORD);
        }
        int result = 0;
        boolean negative = false;
        int i2 = start;
        if (end > start) {
            if (b2[i2] == 45) {
                negative = true;
                limit = Integer.MIN_VALUE;
                i2++;
            } else {
                limit = -2147483647;
            }
            int multmin = limit / radix;
            if (i2 < end) {
                int i3 = i2;
                i2++;
                int digit = Character.digit((char) b2[i3], radix);
                if (digit < 0) {
                    throw new NumberFormatException("illegal number: " + toString(b2, start, end));
                }
                result = -digit;
            }
            while (i2 < end) {
                int i4 = i2;
                i2++;
                int digit2 = Character.digit((char) b2[i4], radix);
                if (digit2 < 0) {
                    throw new NumberFormatException("illegal number");
                }
                if (result < multmin) {
                    throw new NumberFormatException("illegal number");
                }
                int result2 = result * radix;
                if (result2 < limit + digit2) {
                    throw new NumberFormatException("illegal number");
                }
                result = result2 - digit2;
            }
            if (negative) {
                if (i2 > start + 1) {
                    return result;
                }
                throw new NumberFormatException("illegal number");
            }
            return -result;
        }
        throw new NumberFormatException("illegal number");
    }

    public static String toString(byte[] b2, int start, int end) {
        int size = end - start;
        char[] theChars = new char[size];
        int i2 = 0;
        int j2 = start;
        while (i2 < size) {
            int i3 = i2;
            i2++;
            int i4 = j2;
            j2++;
            theChars[i3] = (char) (b2[i4] & 255);
        }
        return new String(theChars);
    }
}
