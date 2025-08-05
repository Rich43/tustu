package java.net;

import java.util.Formatter;
import java.util.Locale;
import sun.net.util.IPAddressUtil;

/* loaded from: rt.jar:java/net/HostPortrange.class */
class HostPortrange {
    String hostname;
    String scheme;
    int[] portrange;
    boolean wildcard;
    boolean literal;
    boolean ipv6;
    boolean ipv4;
    static final int PORT_MIN = 0;
    static final int PORT_MAX = 65535;
    static final int CASE_DIFF = -32;
    static final int[] HTTP_PORT = {80, 80};
    static final int[] HTTPS_PORT = {443, 443};
    static final int[] NO_PORT = {-1, -1};

    boolean equals(HostPortrange hostPortrange) {
        return this.hostname.equals(hostPortrange.hostname) && this.portrange[0] == hostPortrange.portrange[0] && this.portrange[1] == hostPortrange.portrange[1] && this.wildcard == hostPortrange.wildcard && this.literal == hostPortrange.literal;
    }

    public int hashCode() {
        return this.hostname.hashCode() + this.portrange[0] + this.portrange[1];
    }

    HostPortrange(String str, String str2) {
        String strSubstring;
        String strSubstring2 = null;
        this.scheme = str;
        if (str2.charAt(0) == '[') {
            this.literal = true;
            this.ipv6 = true;
            int iIndexOf = str2.indexOf(93);
            if (iIndexOf != -1) {
                String strSubstring3 = str2.substring(1, iIndexOf);
                int iIndexOf2 = str2.indexOf(58, iIndexOf + 1);
                if (iIndexOf2 != -1 && str2.length() > iIndexOf2) {
                    strSubstring2 = str2.substring(iIndexOf2 + 1);
                }
                byte[] bArrTextToNumericFormatV6 = IPAddressUtil.textToNumericFormatV6(strSubstring3);
                if (bArrTextToNumericFormatV6 == null) {
                    throw new IllegalArgumentException("illegal IPv6 address");
                }
                StringBuilder sb = new StringBuilder();
                new Formatter(sb, Locale.US).format("%02x%02x:%02x%02x:%02x%02x:%02x%02x:%02x%02x:%02x%02x:%02x%02x:%02x%02x", Byte.valueOf(bArrTextToNumericFormatV6[0]), Byte.valueOf(bArrTextToNumericFormatV6[1]), Byte.valueOf(bArrTextToNumericFormatV6[2]), Byte.valueOf(bArrTextToNumericFormatV6[3]), Byte.valueOf(bArrTextToNumericFormatV6[4]), Byte.valueOf(bArrTextToNumericFormatV6[5]), Byte.valueOf(bArrTextToNumericFormatV6[6]), Byte.valueOf(bArrTextToNumericFormatV6[7]), Byte.valueOf(bArrTextToNumericFormatV6[8]), Byte.valueOf(bArrTextToNumericFormatV6[9]), Byte.valueOf(bArrTextToNumericFormatV6[10]), Byte.valueOf(bArrTextToNumericFormatV6[11]), Byte.valueOf(bArrTextToNumericFormatV6[12]), Byte.valueOf(bArrTextToNumericFormatV6[13]), Byte.valueOf(bArrTextToNumericFormatV6[14]), Byte.valueOf(bArrTextToNumericFormatV6[15]));
                this.hostname = sb.toString();
            } else {
                throw new IllegalArgumentException("invalid IPv6 address: " + str2);
            }
        } else {
            int iIndexOf3 = str2.indexOf(58);
            if (iIndexOf3 != -1 && str2.length() > iIndexOf3) {
                strSubstring = str2.substring(0, iIndexOf3);
                strSubstring2 = str2.substring(iIndexOf3 + 1);
            } else {
                strSubstring = iIndexOf3 == -1 ? str2 : str2.substring(0, iIndexOf3);
            }
            if (strSubstring.lastIndexOf(42) > 0) {
                throw new IllegalArgumentException("invalid host wildcard specification");
            }
            if (strSubstring.startsWith("*")) {
                this.wildcard = true;
                if (strSubstring.equals("*")) {
                    strSubstring = "";
                } else if (strSubstring.startsWith("*.")) {
                    strSubstring = toLowerCase(strSubstring.substring(1));
                } else {
                    throw new IllegalArgumentException("invalid host wildcard specification");
                }
            } else {
                int iLastIndexOf = strSubstring.lastIndexOf(46);
                if (iLastIndexOf != -1 && strSubstring.length() > 1) {
                    boolean z2 = true;
                    int length = strSubstring.length();
                    for (int i2 = iLastIndexOf + 1; i2 < length; i2++) {
                        char cCharAt = strSubstring.charAt(i2);
                        if (cCharAt < '0' || cCharAt > '9') {
                            z2 = false;
                            break;
                        }
                    }
                    boolean z3 = z2;
                    this.literal = z3;
                    this.ipv4 = z3;
                    if (z2) {
                        byte[] bArrValidateNumericFormatV4 = IPAddressUtil.validateNumericFormatV4(strSubstring);
                        if (bArrValidateNumericFormatV4 == null) {
                            throw new IllegalArgumentException("illegal IPv4 address");
                        }
                        StringBuilder sb2 = new StringBuilder();
                        new Formatter(sb2, Locale.US).format("%d.%d.%d.%d", Byte.valueOf(bArrValidateNumericFormatV4[0]), Byte.valueOf(bArrValidateNumericFormatV4[1]), Byte.valueOf(bArrValidateNumericFormatV4[2]), Byte.valueOf(bArrValidateNumericFormatV4[3]));
                        strSubstring = sb2.toString();
                    } else {
                        strSubstring = toLowerCase(strSubstring);
                    }
                }
            }
            this.hostname = strSubstring;
        }
        try {
            this.portrange = parsePort(strSubstring2);
        } catch (Exception e2) {
            throw new IllegalArgumentException("invalid port range: " + strSubstring2);
        }
    }

    static String toLowerCase(String str) {
        int length = str.length();
        StringBuilder sb = null;
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            if ((cCharAt >= 'a' && cCharAt <= 'z') || cCharAt == '.') {
                if (sb != null) {
                    sb.append(cCharAt);
                }
            } else if ((cCharAt >= '0' && cCharAt <= '9') || cCharAt == '-') {
                if (sb != null) {
                    sb.append(cCharAt);
                }
            } else if (cCharAt >= 'A' && cCharAt <= 'Z') {
                if (sb == null) {
                    sb = new StringBuilder(length);
                    sb.append((CharSequence) str, 0, i2);
                }
                sb.append((char) (cCharAt - CASE_DIFF));
            } else {
                throw new IllegalArgumentException("Invalid characters in hostname");
            }
        }
        return sb == null ? str : sb.toString();
    }

    public boolean literal() {
        return this.literal;
    }

    public boolean ipv4Literal() {
        return this.ipv4;
    }

    public boolean ipv6Literal() {
        return this.ipv6;
    }

    public String hostname() {
        return this.hostname;
    }

    public int[] portrange() {
        return this.portrange;
    }

    public boolean wildcard() {
        return this.wildcard;
    }

    int[] defaultPort() {
        if (this.scheme.equals("http")) {
            return HTTP_PORT;
        }
        if (this.scheme.equals("https")) {
            return HTTPS_PORT;
        }
        return NO_PORT;
    }

    int[] parsePort(String str) {
        int i2;
        int i3;
        if (str == null || str.equals("")) {
            return defaultPort();
        }
        if (str.equals("*")) {
            return new int[]{0, 65535};
        }
        try {
            int iIndexOf = str.indexOf(45);
            if (iIndexOf == -1) {
                int i4 = Integer.parseInt(str);
                return new int[]{i4, i4};
            }
            String strSubstring = str.substring(0, iIndexOf);
            String strSubstring2 = str.substring(iIndexOf + 1);
            if (strSubstring.equals("")) {
                i2 = 0;
            } else {
                i2 = Integer.parseInt(strSubstring);
            }
            if (strSubstring2.equals("")) {
                i3 = 65535;
            } else {
                i3 = Integer.parseInt(strSubstring2);
            }
            if (i2 < 0 || i3 < 0 || i3 < i2) {
                return defaultPort();
            }
            return new int[]{i2, i3};
        } catch (IllegalArgumentException e2) {
            return defaultPort();
        }
    }
}
