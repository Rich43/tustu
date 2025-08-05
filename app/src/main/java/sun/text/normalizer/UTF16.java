package sun.text.normalizer;

/* loaded from: rt.jar:sun/text/normalizer/UTF16.class */
public final class UTF16 {
    public static final int CODEPOINT_MIN_VALUE = 0;
    public static final int CODEPOINT_MAX_VALUE = 1114111;
    public static final int SUPPLEMENTARY_MIN_VALUE = 65536;
    public static final int LEAD_SURROGATE_MIN_VALUE = 55296;
    public static final int TRAIL_SURROGATE_MIN_VALUE = 56320;
    public static final int LEAD_SURROGATE_MAX_VALUE = 56319;
    public static final int TRAIL_SURROGATE_MAX_VALUE = 57343;
    public static final int SURROGATE_MIN_VALUE = 55296;
    private static final int LEAD_SURROGATE_SHIFT_ = 10;
    private static final int TRAIL_SURROGATE_MASK_ = 1023;
    private static final int LEAD_SURROGATE_OFFSET_ = 55232;

    public static int charAt(String str, int i2) {
        char cCharAt = str.charAt(i2);
        if (cCharAt < 55296) {
            return cCharAt;
        }
        return _charAt(str, i2, cCharAt);
    }

    private static int _charAt(String str, int i2, char c2) {
        char cCharAt;
        char cCharAt2;
        if (c2 > 57343) {
            return c2;
        }
        if (c2 <= 56319) {
            int i3 = i2 + 1;
            if (str.length() != i3 && (cCharAt2 = str.charAt(i3)) >= 56320 && cCharAt2 <= 57343) {
                return UCharacterProperty.getRawSupplementary(c2, cCharAt2);
            }
        } else {
            int i4 = i2 - 1;
            if (i4 >= 0 && (cCharAt = str.charAt(i4)) >= 55296 && cCharAt <= 56319) {
                return UCharacterProperty.getRawSupplementary(cCharAt, c2);
            }
        }
        return c2;
    }

    public static int charAt(char[] cArr, int i2, int i3, int i4) {
        int i5 = i4 + i2;
        if (i5 < i2 || i5 >= i3) {
            throw new ArrayIndexOutOfBoundsException(i5);
        }
        char c2 = cArr[i5];
        if (!isSurrogate(c2)) {
            return c2;
        }
        if (c2 <= 56319) {
            int i6 = i5 + 1;
            if (i6 >= i3) {
                return c2;
            }
            char c3 = cArr[i6];
            if (isTrailSurrogate(c3)) {
                return UCharacterProperty.getRawSupplementary(c2, c3);
            }
        } else {
            if (i5 == i2) {
                return c2;
            }
            char c4 = cArr[i5 - 1];
            if (isLeadSurrogate(c4)) {
                return UCharacterProperty.getRawSupplementary(c4, c2);
            }
        }
        return c2;
    }

    public static int getCharCount(int i2) {
        if (i2 < 65536) {
            return 1;
        }
        return 2;
    }

    public static boolean isSurrogate(char c2) {
        return 55296 <= c2 && c2 <= 57343;
    }

    public static boolean isTrailSurrogate(char c2) {
        return 56320 <= c2 && c2 <= 57343;
    }

    public static boolean isLeadSurrogate(char c2) {
        return 55296 <= c2 && c2 <= 56319;
    }

    public static char getLeadSurrogate(int i2) {
        if (i2 >= 65536) {
            return (char) (LEAD_SURROGATE_OFFSET_ + (i2 >> 10));
        }
        return (char) 0;
    }

    public static char getTrailSurrogate(int i2) {
        if (i2 >= 65536) {
            return (char) (56320 + (i2 & 1023));
        }
        return (char) i2;
    }

    public static String valueOf(int i2) {
        if (i2 < 0 || i2 > 1114111) {
            throw new IllegalArgumentException("Illegal codepoint");
        }
        return toString(i2);
    }

    public static StringBuffer append(StringBuffer stringBuffer, int i2) {
        if (i2 < 0 || i2 > 1114111) {
            throw new IllegalArgumentException("Illegal codepoint: " + Integer.toHexString(i2));
        }
        if (i2 >= 65536) {
            stringBuffer.append(getLeadSurrogate(i2));
            stringBuffer.append(getTrailSurrogate(i2));
        } else {
            stringBuffer.append((char) i2);
        }
        return stringBuffer;
    }

    public static int moveCodePointOffset(char[] cArr, int i2, int i3, int i4, int i5) {
        int i6;
        int length = cArr.length;
        int i7 = i4 + i2;
        if (i2 < 0 || i3 < i2) {
            throw new StringIndexOutOfBoundsException(i2);
        }
        if (i3 > length) {
            throw new StringIndexOutOfBoundsException(i3);
        }
        if (i4 < 0 || i7 > i3) {
            throw new StringIndexOutOfBoundsException(i4);
        }
        if (i5 > 0) {
            if (i5 + i7 > length) {
                throw new StringIndexOutOfBoundsException(i7);
            }
            i6 = i5;
            while (i7 < i3 && i6 > 0) {
                if (isLeadSurrogate(cArr[i7]) && i7 + 1 < i3 && isTrailSurrogate(cArr[i7 + 1])) {
                    i7++;
                }
                i6--;
                i7++;
            }
        } else {
            if (i7 + i5 < i2) {
                throw new StringIndexOutOfBoundsException(i7);
            }
            i6 = -i5;
            while (i6 > 0) {
                i7--;
                if (i7 < i2) {
                    break;
                }
                if (isTrailSurrogate(cArr[i7]) && i7 > i2 && isLeadSurrogate(cArr[i7 - 1])) {
                    i7--;
                }
                i6--;
            }
        }
        if (i6 != 0) {
            throw new StringIndexOutOfBoundsException(i5);
        }
        return i7 - i2;
    }

    private static String toString(int i2) {
        if (i2 < 65536) {
            return String.valueOf((char) i2);
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getLeadSurrogate(i2));
        stringBuffer.append(getTrailSurrogate(i2));
        return stringBuffer.toString();
    }
}
