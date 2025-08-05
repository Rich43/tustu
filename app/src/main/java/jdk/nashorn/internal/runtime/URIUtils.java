package jdk.nashorn.internal.runtime;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/URIUtils.class */
public final class URIUtils {
    private static final String URI_UNESCAPED_NONALPHANUMERIC = "-_.!~*'()";
    private static final String URI_RESERVED = ";/?:@&=+$,#";

    private URIUtils() {
    }

    static String encodeURI(Object self, String string) {
        return encode(self, string, false);
    }

    static String encodeURIComponent(Object self, String string) {
        return encode(self, string, true);
    }

    static String decodeURI(Object self, String string) {
        return decode(self, string, false);
    }

    static String decodeURIComponent(Object self, String string) {
        return decode(self, string, true);
    }

    private static String encode(Object self, String string, boolean component) {
        int V2;
        if (string.isEmpty()) {
            return string;
        }
        int len = string.length();
        StringBuilder sb = new StringBuilder();
        int k2 = 0;
        while (k2 < len) {
            char C2 = string.charAt(k2);
            if (isUnescaped(C2, component)) {
                sb.append(C2);
            } else {
                if (C2 >= 56320 && C2 <= 57343) {
                    return error(string, k2);
                }
                if (C2 < 55296 || C2 > 56319) {
                    V2 = C2;
                } else {
                    k2++;
                    if (k2 == len) {
                        return error(string, k2);
                    }
                    char kChar = string.charAt(k2);
                    if (kChar < 56320 || kChar > 57343) {
                        return error(string, k2);
                    }
                    V2 = ((C2 - 55296) * 1024) + (kChar - 56320) + 65536;
                }
                try {
                    sb.append(toHexEscape(V2));
                } catch (Exception e2) {
                    throw ECMAErrors.uriError(e2, "bad.uri", string, Integer.toString(k2));
                }
            }
            k2++;
        }
        return sb.toString();
    }

    private static String decode(Object self, String string, boolean component) {
        int n2;
        int V2;
        int minV;
        if (string.isEmpty()) {
            return string;
        }
        int len = string.length();
        StringBuilder sb = new StringBuilder();
        int k2 = 0;
        while (k2 < len) {
            char ch = string.charAt(k2);
            if (ch != '%') {
                sb.append(ch);
            } else {
                int start = k2;
                if (k2 + 2 >= len) {
                    return error(string, k2);
                }
                int B2 = toHexByte(string.charAt(k2 + 1), string.charAt(k2 + 2));
                if (B2 < 0) {
                    return error(string, k2 + 1);
                }
                k2 += 2;
                if ((B2 & 128) == 0) {
                    char C2 = (char) B2;
                    if (!component && URI_RESERVED.indexOf(C2) >= 0) {
                        for (int j2 = start; j2 <= k2; j2++) {
                            sb.append(string.charAt(j2));
                        }
                    } else {
                        sb.append(C2);
                    }
                } else {
                    if ((B2 & 192) == 128) {
                        return error(string, k2);
                    }
                    if ((B2 & 32) == 0) {
                        n2 = 2;
                        V2 = B2 & 31;
                        minV = 128;
                    } else if ((B2 & 16) == 0) {
                        n2 = 3;
                        V2 = B2 & 15;
                        minV = 2048;
                    } else if ((B2 & 8) == 0) {
                        n2 = 4;
                        V2 = B2 & 7;
                        minV = 65536;
                    } else if ((B2 & 4) == 0) {
                        n2 = 5;
                        V2 = B2 & 3;
                        minV = 2097152;
                    } else if ((B2 & 2) == 0) {
                        n2 = 6;
                        V2 = B2 & 1;
                        minV = 67108864;
                    } else {
                        return error(string, k2);
                    }
                    if (k2 + (3 * (n2 - 1)) >= len) {
                        return error(string, k2);
                    }
                    for (int j3 = 1; j3 < n2; j3++) {
                        int k3 = k2 + 1;
                        if (string.charAt(k3) != '%') {
                            return error(string, k3);
                        }
                        int B3 = toHexByte(string.charAt(k3 + 1), string.charAt(k3 + 2));
                        if (B3 < 0 || (B3 & 192) != 128) {
                            return error(string, k3 + 1);
                        }
                        V2 = (V2 << 6) | (B3 & 63);
                        k2 = k3 + 2;
                    }
                    if (V2 < minV || (V2 >= 55296 && V2 <= 57343)) {
                        V2 = Integer.MAX_VALUE;
                    }
                    if (V2 < 65536) {
                        char C3 = (char) V2;
                        if (!component && URI_RESERVED.indexOf(C3) >= 0) {
                            for (int j4 = start; j4 != k2; j4++) {
                                sb.append(string.charAt(j4));
                            }
                        } else {
                            sb.append(C3);
                        }
                    } else {
                        if (V2 > 1114111) {
                            return error(string, k2);
                        }
                        int L2 = ((V2 - 65536) & 1023) + 56320;
                        int H2 = (((V2 - 65536) >> 10) & 1023) + 55296;
                        sb.append((char) H2);
                        sb.append((char) L2);
                    }
                }
            }
            k2++;
        }
        return sb.toString();
    }

    private static int hexDigit(char ch) {
        char chu = Character.toUpperCase(ch);
        if (chu >= '0' && chu <= '9') {
            return chu - '0';
        }
        if (chu >= 'A' && chu <= 'F') {
            return (chu - 'A') + 10;
        }
        return -1;
    }

    private static int toHexByte(char ch1, char ch2) {
        int i1 = hexDigit(ch1);
        int i2 = hexDigit(ch2);
        if (i1 >= 0 && i2 >= 0) {
            return (i1 << 4) | i2;
        }
        return -1;
    }

    private static String toHexEscape(int u0) {
        int len;
        int u2 = u0;
        byte[] b2 = new byte[6];
        if (u2 <= 127) {
            b2[0] = (byte) u2;
            len = 1;
        } else {
            len = 2;
            int i2 = u2;
            int i3 = 11;
            while (true) {
                int mask = i2 >>> i3;
                if (mask == 0) {
                    break;
                }
                len++;
                i2 = mask;
                i3 = 5;
            }
            for (int i4 = len - 1; i4 > 0; i4--) {
                b2[i4] = (byte) (128 | (u2 & 63));
                u2 >>>= 6;
            }
            b2[0] = (byte) ((((1 << (8 - len)) - 1) ^ (-1)) | u2);
        }
        StringBuilder sb = new StringBuilder();
        for (int i5 = 0; i5 < len; i5++) {
            sb.append('%');
            if ((b2[i5] & 255) < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(b2[i5] & 255).toUpperCase());
        }
        return sb.toString();
    }

    private static String error(String string, int index) {
        throw ECMAErrors.uriError("bad.uri", string, Integer.toString(index));
    }

    private static boolean isUnescaped(char ch, boolean component) {
        if ('A' <= ch && ch <= 'Z') {
            return true;
        }
        if ('a' <= ch && ch <= 'z') {
            return true;
        }
        if (('0' > ch || ch > '9') && URI_UNESCAPED_NONALPHANUMERIC.indexOf(ch) < 0) {
            return !component && URI_RESERVED.indexOf(ch) >= 0;
        }
        return true;
    }
}
