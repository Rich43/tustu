package sun.net.util;

import java.net.URL;
import java.nio.CharBuffer;
import java.util.Arrays;
import org.icepdf.core.util.PdfOps;
import sun.security.action.GetPropertyAction;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:sun/net/util/IPAddressUtil.class */
public class IPAddressUtil {
    private static final int INADDR4SZ = 4;
    private static final int INADDR16SZ = 16;
    private static final int INT16SZ = 2;
    private static final long L_IPV6_DELIMS = 0;
    private static final long H_IPV6_DELIMS = 671088640;
    private static final long L_GEN_DELIMS = -8935000888854970368L;
    private static final long H_GEN_DELIMS = 671088641;
    private static final long L_AUTH_DELIMS = 288230376151711744L;
    private static final long H_AUTH_DELIMS = 671088641;
    private static final long L_COLON = 288230376151711744L;
    private static final long H_COLON = 0;
    private static final long L_SLASH = 140737488355328L;
    private static final long H_SLASH = 0;
    private static final long L_BACKSLASH = 0;
    private static final long H_BACKSLASH = 268435456;
    private static final long L_NON_PRINTABLE = 4294967295L;
    private static final long H_NON_PRINTABLE = Long.MIN_VALUE;
    private static final long L_EXCLUDE = -8935000884560003073L;
    private static final long H_EXCLUDE = -9223372035915251711L;
    private static final char[] OTHERS;
    private static final int HEXADECIMAL = 16;
    private static final int DECIMAL = 10;
    private static final int OCTAL = 8;
    private static final int[] SUPPORTED_RADIXES;
    private static final long CANT_PARSE_IN_RADIX = -1;
    private static final long TERMINAL_PARSE_ERROR = -2;
    private static final String ALLOW_AMBIGUOUS_IPADDRESS_LITERALS_SP = "jdk.net.allowAmbiguousIPAddressLiterals";
    private static final boolean ALLOW_AMBIGUOUS_IPADDRESS_LITERALS_SP_VALUE;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !IPAddressUtil.class.desiredAssertionStatus();
        OTHERS = new char[]{8263, 8264, 8265, 8448, 8449, 8453, 8454, 10868, 65109, 65110, 65119, 65131, 65283, 65295, 65306, 65311, 65312};
        SUPPORTED_RADIXES = new int[]{16, 8, 10};
        ALLOW_AMBIGUOUS_IPADDRESS_LITERALS_SP_VALUE = Boolean.valueOf(GetPropertyAction.privilegedGetProperty(ALLOW_AMBIGUOUS_IPADDRESS_LITERALS_SP, "false")).booleanValue();
    }

    public static byte[] textToNumericFormatV4(String str) {
        boolean z2;
        byte[] bArr = new byte[4];
        long j2 = 0;
        int i2 = 0;
        boolean z3 = true;
        int length = str.length();
        if (length == 0 || length > 15) {
            return null;
        }
        for (int i3 = 0; i3 < length; i3++) {
            char cCharAt = str.charAt(i3);
            if (cCharAt == '.') {
                if (z3 || j2 < 0 || j2 > 255 || i2 == 3) {
                    return null;
                }
                int i4 = i2;
                i2++;
                bArr[i4] = (byte) (j2 & 255);
                j2 = 0;
                z2 = true;
            } else {
                int iDigit = digit(cCharAt, 10);
                if (iDigit < 0) {
                    return null;
                }
                j2 = (j2 * 10) + iDigit;
                z2 = false;
            }
            z3 = z2;
        }
        if (z3 || j2 < 0 || j2 >= (1 << ((4 - i2) * 8))) {
            return null;
        }
        switch (i2) {
            case 0:
                bArr[0] = (byte) ((j2 >> 24) & 255);
            case 1:
                bArr[1] = (byte) ((j2 >> 16) & 255);
            case 2:
                bArr[2] = (byte) ((j2 >> 8) & 255);
            case 3:
                bArr[3] = (byte) ((j2 >> 0) & 255);
                break;
        }
        return bArr;
    }

    public static byte[] validateNumericFormatV4(String str) {
        byte[] bArrTextToNumericFormatV4 = textToNumericFormatV4(str);
        if (!ALLOW_AMBIGUOUS_IPADDRESS_LITERALS_SP_VALUE && bArrTextToNumericFormatV4 == null && isBsdParsableV4(str)) {
            throw new IllegalArgumentException("Invalid IP address literal: " + str);
        }
        return bArrTextToNumericFormatV4;
    }

    /* JADX WARN: Code restructure failed: missing block: B:71:0x0165, code lost:
    
        if (r9 == false) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x016e, code lost:
    
        if ((r16 + 2) <= 16) goto L76;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0171, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0173, code lost:
    
        r1 = r16;
        r16 = r16 + 1;
        r0[r1] = (byte) ((r10 >> 8) & 255);
        r16 = r16 + 1;
        r0[r16] = (byte) (r10 & 255);
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x0196, code lost:
    
        if (r7 == (-1)) goto L88;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0199, code lost:
    
        r0 = r16 - r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x01a3, code lost:
    
        if (r16 != 16) goto L83;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x01a6, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x01a8, code lost:
    
        r15 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x01af, code lost:
    
        if (r15 > r0) goto L114;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x01b2, code lost:
    
        r0[16 - r15] = r0[(r7 + r0) - r15];
        r0[(r7 + r0) - r15] = 0;
        r15 = r15 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x01d5, code lost:
    
        r16 = 16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x01dd, code lost:
    
        if (r16 == 16) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x01e0, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x01e2, code lost:
    
        r0 = convertFromIPv4MappedAddress(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x01eb, code lost:
    
        if (r0 == null) goto L96;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x01f0, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x01f3, code lost:
    
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static byte[] textToNumericFormatV6(java.lang.String r6) {
        /*
            Method dump skipped, instructions count: 500
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.util.IPAddressUtil.textToNumericFormatV6(java.lang.String):byte[]");
    }

    public static boolean isIPv4LiteralAddress(String str) {
        return textToNumericFormatV4(str) != null;
    }

    public static boolean isIPv6LiteralAddress(String str) {
        return textToNumericFormatV6(str) != null;
    }

    public static byte[] convertFromIPv4MappedAddress(byte[] bArr) {
        if (isIPv4MappedAddress(bArr)) {
            byte[] bArr2 = new byte[4];
            System.arraycopy(bArr, 12, bArr2, 0, 4);
            return bArr2;
        }
        return null;
    }

    private static boolean isIPv4MappedAddress(byte[] bArr) {
        if (bArr.length >= 16 && bArr[0] == 0 && bArr[1] == 0 && bArr[2] == 0 && bArr[3] == 0 && bArr[4] == 0 && bArr[5] == 0 && bArr[6] == 0 && bArr[7] == 0 && bArr[8] == 0 && bArr[9] == 0 && bArr[10] == -1 && bArr[11] == -1) {
            return true;
        }
        return false;
    }

    public static boolean match(char c2, long j2, long j3) {
        return c2 < '@' ? ((1 << c2) & j2) != 0 : c2 < 128 && ((1 << (c2 - 64)) & j3) != 0;
    }

    public static int scan(String str, long j2, long j3) {
        int length;
        boolean zMatch;
        int i2 = -1;
        if (str == null || (length = str.length()) == 0) {
            return -1;
        }
        boolean z2 = false;
        do {
            i2++;
            if (i2 >= length) {
                break;
            }
            zMatch = match(str.charAt(i2), j2, j3);
            z2 = zMatch;
        } while (!zMatch);
        if (z2) {
            return i2;
        }
        return -1;
    }

    public static int scan(String str, long j2, long j3, char[] cArr) {
        int length;
        int i2 = -1;
        if (str == null || (length = str.length()) == 0) {
            return -1;
        }
        boolean z2 = false;
        char c2 = cArr[0];
        while (true) {
            i2++;
            if (i2 >= length) {
                break;
            }
            char cCharAt = str.charAt(i2);
            boolean zMatch = match(cCharAt, j2, j3);
            z2 = zMatch;
            if (!zMatch) {
                if (cCharAt >= c2 && Arrays.binarySearch(cArr, cCharAt) > -1) {
                    z2 = true;
                    break;
                }
            } else {
                break;
            }
        }
        if (z2) {
            return i2;
        }
        return -1;
    }

    private static String describeChar(char c2) {
        return (c2 < ' ' || c2 == 127) ? c2 == '\n' ? "LF" : c2 == '\r' ? SwingUtilities2.IMPLIED_CR : "control char (code=" + ((int) c2) + ")" : c2 == '\\' ? "'\\'" : PdfOps.SINGLE_QUOTE_TOKEN + c2 + PdfOps.SINGLE_QUOTE_TOKEN;
    }

    private static String checkUserInfo(String str) {
        int iScan = scan(str, -9223231260711714817L, H_EXCLUDE);
        if (iScan >= 0) {
            return "Illegal character found in user-info: " + describeChar(str.charAt(iScan));
        }
        return null;
    }

    private static String checkHost(String str) {
        String strSubstring;
        int iScan;
        if (str.startsWith("[") && str.endsWith("]")) {
            String strSubstring2 = str.substring(1, str.length() - 1);
            if (isIPv6LiteralAddress(strSubstring2)) {
                int iIndexOf = strSubstring2.indexOf(37);
                if (iIndexOf >= 0 && (iScan = scan((strSubstring = strSubstring2.substring(iIndexOf)), 4294967295L, -9223372036183687168L)) >= 0) {
                    return "Illegal character found in IPv6 scoped address: " + describeChar(strSubstring.charAt(iScan));
                }
                return null;
            }
            return "Unrecognized IPv6 address format";
        }
        int iScan2 = scan(str, L_EXCLUDE, H_EXCLUDE);
        if (iScan2 >= 0) {
            return "Illegal character found in host: " + describeChar(str.charAt(iScan2));
        }
        return null;
    }

    private static String checkAuth(String str) {
        int iScan = scan(str, -9223231260711714817L, -9223372036586340352L);
        if (iScan >= 0) {
            return "Illegal character found in authority: " + describeChar(str.charAt(iScan));
        }
        return null;
    }

    public static String checkAuthority(URL url) {
        if (url == null) {
            return null;
        }
        String userInfo = url.getUserInfo();
        String strCheckUserInfo = checkUserInfo(userInfo);
        if (strCheckUserInfo != null) {
            return strCheckUserInfo;
        }
        String host = url.getHost();
        String strCheckHost = checkHost(host);
        if (strCheckHost != null) {
            return strCheckHost;
        }
        if (host == null && userInfo == null) {
            return checkAuth(url.getAuthority());
        }
        return null;
    }

    public static String checkExternalForm(URL url) {
        if (url == null) {
            return null;
        }
        String userInfo = url.getUserInfo();
        int iScan = scan(userInfo, 140741783322623L, Long.MIN_VALUE);
        if (iScan >= 0) {
            return "Illegal character found in authority: " + describeChar(userInfo.charAt(iScan));
        }
        String strCheckHostString = checkHostString(url.getHost());
        if (strCheckHostString != null) {
            return strCheckHostString;
        }
        return null;
    }

    public static String checkHostString(String str) {
        int iScan;
        if (str != null && (iScan = scan(str, 140741783322623L, Long.MIN_VALUE, OTHERS)) >= 0) {
            return "Illegal character found in host: " + describeChar(str.charAt(iScan));
        }
        return null;
    }

    public static int digit(char c2, int i2) {
        if (ALLOW_AMBIGUOUS_IPADDRESS_LITERALS_SP_VALUE) {
            return Character.digit(c2, i2);
        }
        return parseAsciiDigit(c2, i2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x0088, code lost:
    
        if (r10 >= 0) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x008b, code lost:
    
        return false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean isBsdParsableV4(java.lang.String r5) {
        /*
            r0 = r5
            r1 = 0
            char r0 = r0.charAt(r1)
            r6 = r0
            r0 = r6
            r1 = 10
            int r0 = parseAsciiDigit(r0, r1)
            r1 = -1
            if (r0 != r1) goto L12
            r0 = 0
            return r0
        L12:
            r0 = r5
            r1 = r5
            int r1 = r1.length()
            r2 = 1
            int r1 = r1 - r2
            char r0 = r0.charAt(r1)
            r7 = r0
            r0 = r7
            r1 = 46
            if (r0 == r1) goto L2b
            r0 = r7
            int r0 = parseAsciiHexDigit(r0)
            r1 = -1
            if (r0 != r1) goto L2d
        L2b:
            r0 = 0
            return r0
        L2d:
            r0 = r5
            java.nio.CharBuffer r0 = java.nio.CharBuffer.wrap(r0)
            r8 = r0
            r0 = 0
            r9 = r0
        L35:
            r0 = r8
            boolean r0 = r0.hasRemaining()
            if (r0 == 0) goto L90
            r0 = -1
            r10 = r0
            int[] r0 = sun.net.util.IPAddressUtil.SUPPORTED_RADIXES
            r12 = r0
            r0 = r12
            int r0 = r0.length
            r13 = r0
            r0 = 0
            r14 = r0
        L4e:
            r0 = r14
            r1 = r13
            if (r0 >= r1) goto L84
            r0 = r12
            r1 = r14
            r0 = r0[r1]
            r15 = r0
            r0 = r15
            r1 = r8
            r2 = r9
            long r0 = parseV4FieldBsd(r0, r1, r2)
            r10 = r0
            r0 = r10
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 < 0) goto L73
            int r9 = r9 + 1
            goto L84
        L73:
            r0 = r10
            r1 = -2
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 != 0) goto L7e
            r0 = 0
            return r0
        L7e:
            int r14 = r14 + 1
            goto L4e
        L84:
            r0 = r10
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 >= 0) goto L8d
            r0 = 0
            return r0
        L8d:
            goto L35
        L90:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.util.IPAddressUtil.isBsdParsableV4(java.lang.String):boolean");
    }

    private static long parseV4FieldBsd(int i2, CharBuffer charBuffer, int i3) {
        int iPosition = charBuffer.position();
        long j2 = 0;
        int i4 = 0;
        if (!checkPrefix(charBuffer, i2)) {
            j2 = -1;
        }
        boolean z2 = false;
        while (charBuffer.hasRemaining() && j2 != -1 && !z2) {
            char c2 = charBuffer.get();
            if (c2 == '.') {
                z2 = true;
                if (i3 == 3 || i4 == 0 || j2 > 255) {
                    return -2L;
                }
            } else {
                int asciiDigit = parseAsciiDigit(c2, i2);
                if (asciiDigit >= 0) {
                    i4++;
                    j2 = (j2 * i2) + asciiDigit;
                } else {
                    return -2L;
                }
            }
        }
        if (j2 == -1) {
            charBuffer.position(iPosition);
        } else if (!z2 && j2 > (1 << ((4 - i3) * 8)) - 1) {
            return -2L;
        }
        return j2;
    }

    private static boolean checkPrefix(CharBuffer charBuffer, int i2) {
        switch (i2) {
            case 8:
                return isOctalFieldStart(charBuffer);
            case 10:
                return isDecimalFieldStart(charBuffer);
            case 16:
                return isHexFieldStart(charBuffer);
            default:
                throw new AssertionError((Object) "Not supported radix");
        }
    }

    private static boolean isOctalFieldStart(CharBuffer charBuffer) {
        if (charBuffer.remaining() < 2) {
            return false;
        }
        int iPosition = charBuffer.position();
        boolean z2 = charBuffer.get() == '0' && charBuffer.get() != '.';
        if (z2) {
            charBuffer.position(iPosition + 1);
        }
        return z2;
    }

    private static boolean isDecimalFieldStart(CharBuffer charBuffer) {
        return charBuffer.hasRemaining();
    }

    private static boolean isHexFieldStart(CharBuffer charBuffer) {
        if (charBuffer.remaining() < 2) {
            return false;
        }
        char c2 = charBuffer.get();
        char c3 = charBuffer.get();
        return c2 == '0' && (c3 == 'x' || c3 == 'X');
    }

    public static int parseAsciiDigit(char c2, int i2) {
        if (!$assertionsDisabled && i2 != 8 && i2 != 10 && i2 != 16) {
            throw new AssertionError();
        }
        if (i2 == 16) {
            return parseAsciiHexDigit(c2);
        }
        int i3 = c2 - 48;
        if (i3 < 0 || i3 >= i2) {
            return -1;
        }
        return i3;
    }

    private static int parseAsciiHexDigit(char c2) {
        char lowerCase = Character.toLowerCase(c2);
        if (lowerCase >= 'a' && lowerCase <= 'f') {
            return (lowerCase - 'a') + 10;
        }
        return parseAsciiDigit(lowerCase, 10);
    }
}
