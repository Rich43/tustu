package sun.net.www;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.BitSet;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.nio.cs.ThreadLocalCoders;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/net/www/ParseUtil.class */
public class ParseUtil {
    static BitSet encodedInPath;
    private static final char[] hexDigits;
    private static final long L_DIGIT;
    private static final long H_DIGIT = 0;
    private static final long L_HEX;
    private static final long H_HEX;
    private static final long L_UPALPHA = 0;
    private static final long H_UPALPHA;
    private static final long L_LOWALPHA = 0;
    private static final long H_LOWALPHA;
    private static final long L_ALPHA = 0;
    private static final long H_ALPHA;
    private static final long L_ALPHANUM;
    private static final long H_ALPHANUM;
    private static final long L_MARK;
    private static final long H_MARK;
    private static final long L_UNRESERVED;
    private static final long H_UNRESERVED;
    private static final long L_RESERVED;
    private static final long H_RESERVED;
    private static final long L_ESCAPED = 1;
    private static final long H_ESCAPED = 0;
    private static final long L_DASH;
    private static final long H_DASH;
    private static final long L_URIC;
    private static final long H_URIC;
    private static final long L_PCHAR;
    private static final long H_PCHAR;
    private static final long L_PATH;
    private static final long H_PATH;
    private static final long L_USERINFO;
    private static final long H_USERINFO;
    private static final long L_REG_NAME;
    private static final long H_REG_NAME;
    private static final long L_SERVER;
    private static final long H_SERVER;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ParseUtil.class.desiredAssertionStatus();
        encodedInPath = new BitSet(256);
        encodedInPath.set(61);
        encodedInPath.set(59);
        encodedInPath.set(63);
        encodedInPath.set(47);
        encodedInPath.set(35);
        encodedInPath.set(32);
        encodedInPath.set(60);
        encodedInPath.set(62);
        encodedInPath.set(37);
        encodedInPath.set(34);
        encodedInPath.set(123);
        encodedInPath.set(125);
        encodedInPath.set(124);
        encodedInPath.set(92);
        encodedInPath.set(94);
        encodedInPath.set(91);
        encodedInPath.set(93);
        encodedInPath.set(96);
        for (int i2 = 0; i2 < 32; i2++) {
            encodedInPath.set(i2);
        }
        encodedInPath.set(127);
        hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        L_DIGIT = lowMask('0', '9');
        L_HEX = L_DIGIT;
        H_HEX = highMask('A', 'F') | highMask('a', 'f');
        H_UPALPHA = highMask('A', 'Z');
        H_LOWALPHA = highMask('a', 'z');
        H_ALPHA = H_LOWALPHA | H_UPALPHA;
        L_ALPHANUM = L_DIGIT | 0;
        H_ALPHANUM = 0 | H_ALPHA;
        L_MARK = lowMask("-_.!~*'()");
        H_MARK = highMask("-_.!~*'()");
        L_UNRESERVED = L_ALPHANUM | L_MARK;
        H_UNRESERVED = H_ALPHANUM | H_MARK;
        L_RESERVED = lowMask(";/?:@&=+$,[]");
        H_RESERVED = highMask(";/?:@&=+$,[]");
        L_DASH = lowMask(LanguageTag.SEP);
        H_DASH = highMask(LanguageTag.SEP);
        L_URIC = L_RESERVED | L_UNRESERVED | 1;
        H_URIC = H_RESERVED | H_UNRESERVED | 0;
        L_PCHAR = L_UNRESERVED | 1 | lowMask(":@&=+$,");
        H_PCHAR = H_UNRESERVED | 0 | highMask(":@&=+$,");
        L_PATH = L_PCHAR | lowMask(";/");
        H_PATH = H_PCHAR | highMask(";/");
        L_USERINFO = L_UNRESERVED | 1 | lowMask(";:&=+$,");
        H_USERINFO = H_UNRESERVED | 0 | highMask(";:&=+$,");
        L_REG_NAME = L_UNRESERVED | 1 | lowMask("$,;:@&=+");
        H_REG_NAME = H_UNRESERVED | 0 | highMask("$,;:@&=+");
        L_SERVER = L_USERINFO | L_ALPHANUM | L_DASH | lowMask(".:@[]");
        H_SERVER = H_USERINFO | H_ALPHANUM | H_DASH | highMask(".:@[]");
    }

    public static String encodePath(String str) {
        return encodePath(str, true);
    }

    public static String encodePath(String str, boolean z2) {
        char[] cArr = new char[(str.length() * 2) + 16];
        int iEscape = 0;
        char[] charArray = str.toCharArray();
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            char c2 = charArray[i2];
            if ((!z2 && c2 == '/') || (z2 && c2 == File.separatorChar)) {
                int i3 = iEscape;
                iEscape++;
                cArr[i3] = '/';
            } else if (c2 <= 127) {
                if ((c2 >= 'a' && c2 <= 'z') || ((c2 >= 'A' && c2 <= 'Z') || (c2 >= '0' && c2 <= '9'))) {
                    int i4 = iEscape;
                    iEscape++;
                    cArr[i4] = c2;
                } else if (encodedInPath.get(c2)) {
                    iEscape = escape(cArr, c2, iEscape);
                } else {
                    int i5 = iEscape;
                    iEscape++;
                    cArr[i5] = c2;
                }
            } else if (c2 > 2047) {
                iEscape = escape(cArr, (char) (128 | ((c2 >> 0) & 63)), escape(cArr, (char) (128 | ((c2 >> 6) & 63)), escape(cArr, (char) (224 | ((c2 >> '\f') & 15)), iEscape)));
            } else {
                iEscape = escape(cArr, (char) (128 | ((c2 >> 0) & 63)), escape(cArr, (char) (192 | ((c2 >> 6) & 31)), iEscape));
            }
            if (iEscape + 9 > cArr.length) {
                int length2 = (cArr.length * 2) + 16;
                if (length2 < 0) {
                    length2 = Integer.MAX_VALUE;
                }
                char[] cArr2 = new char[length2];
                System.arraycopy(cArr, 0, cArr2, 0, iEscape);
                cArr = cArr2;
            }
        }
        return new String(cArr, 0, iEscape);
    }

    private static int escape(char[] cArr, char c2, int i2) {
        int i3 = i2 + 1;
        cArr[i2] = '%';
        int i4 = i3 + 1;
        cArr[i3] = Character.forDigit((c2 >> 4) & 15, 16);
        int i5 = i4 + 1;
        cArr[i4] = Character.forDigit(c2 & 15, 16);
        return i5;
    }

    private static byte unescape(String str, int i2) {
        return (byte) Integer.parseInt(str.substring(i2 + 1, i2 + 3), 16);
    }

    public static String decode(String str) {
        int length = str.length();
        if (length == 0 || str.indexOf(37) < 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder(length);
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(length);
        CharBuffer charBufferAllocate = CharBuffer.allocate(length);
        CharsetDecoder charsetDecoderOnUnmappableCharacter = ThreadLocalCoders.decoderFor("UTF-8").onMalformedInput(CodingErrorAction.REPORT).onUnmappableCharacter(CodingErrorAction.REPORT);
        char cCharAt = str.charAt(0);
        int i2 = 0;
        while (i2 < length) {
            if (!$assertionsDisabled && cCharAt != str.charAt(i2)) {
                throw new AssertionError();
            }
            if (cCharAt != '%') {
                sb.append(cCharAt);
                i2++;
                if (i2 >= length) {
                    break;
                }
                cCharAt = str.charAt(i2);
            } else {
                byteBufferAllocate.clear();
                do {
                    if (!$assertionsDisabled && length - i2 < 2) {
                        throw new AssertionError();
                    }
                    try {
                        byteBufferAllocate.put(unescape(str, i2));
                        i2 += 3;
                        if (i2 >= length) {
                            break;
                        }
                        cCharAt = str.charAt(i2);
                    } catch (NumberFormatException e2) {
                        throw new IllegalArgumentException();
                    }
                } while (cCharAt == '%');
                byteBufferAllocate.flip();
                charBufferAllocate.clear();
                charsetDecoderOnUnmappableCharacter.reset();
                if (charsetDecoderOnUnmappableCharacter.decode(byteBufferAllocate, charBufferAllocate, true).isError()) {
                    throw new IllegalArgumentException("Error decoding percent encoded characters");
                }
                if (charsetDecoderOnUnmappableCharacter.flush(charBufferAllocate).isError()) {
                    throw new IllegalArgumentException("Error decoding percent encoded characters");
                }
                sb.append(charBufferAllocate.flip().toString());
            }
        }
        return sb.toString();
    }

    public String canonizeString(String str) {
        str.length();
        while (true) {
            int iIndexOf = str.indexOf("/../");
            if (iIndexOf < 0) {
                break;
            }
            int iLastIndexOf = str.lastIndexOf(47, iIndexOf - 1);
            if (iLastIndexOf >= 0) {
                str = str.substring(0, iLastIndexOf) + str.substring(iIndexOf + 3);
            } else {
                str = str.substring(iIndexOf + 3);
            }
        }
        while (true) {
            int iIndexOf2 = str.indexOf("/./");
            if (iIndexOf2 < 0) {
                break;
            }
            str = str.substring(0, iIndexOf2) + str.substring(iIndexOf2 + 2);
        }
        while (str.endsWith("/..")) {
            int iIndexOf3 = str.indexOf("/..");
            int iLastIndexOf2 = str.lastIndexOf(47, iIndexOf3 - 1);
            if (iLastIndexOf2 >= 0) {
                str = str.substring(0, iLastIndexOf2 + 1);
            } else {
                str = str.substring(0, iIndexOf3);
            }
        }
        if (str.endsWith("/.")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static URL fileToEncodedURL(File file) throws MalformedURLException {
        String strEncodePath = encodePath(file.getAbsolutePath());
        if (!strEncodePath.startsWith("/")) {
            strEncodePath = "/" + strEncodePath;
        }
        if (!strEncodePath.endsWith("/") && file.isDirectory()) {
            strEncodePath = strEncodePath + "/";
        }
        return new URL(DeploymentDescriptorParser.ATTR_FILE, "", strEncodePath);
    }

    public static URI toURI(URL url) {
        URI uriCreateURI;
        String protocol = url.getProtocol();
        String authority = url.getAuthority();
        String path = url.getPath();
        String query = url.getQuery();
        String ref = url.getRef();
        if (path != null && !path.startsWith("/")) {
            path = "/" + path;
        }
        if (authority != null && authority.endsWith(":-1")) {
            authority = authority.substring(0, authority.length() - 3);
        }
        try {
            uriCreateURI = createURI(protocol, authority, path, query, ref);
        } catch (URISyntaxException e2) {
            uriCreateURI = null;
        }
        return uriCreateURI;
    }

    private static URI createURI(String str, String str2, String str3, String str4, String str5) throws URISyntaxException {
        String string = toString(str, null, str2, null, null, -1, str3, str4, str5);
        checkPath(string, str, str3);
        return new URI(string);
    }

    private static String toString(String str, String str2, String str3, String str4, String str5, int i2, String str6, String str7, String str8) {
        StringBuffer stringBuffer = new StringBuffer();
        if (str != null) {
            stringBuffer.append(str);
            stringBuffer.append(':');
        }
        appendSchemeSpecificPart(stringBuffer, str2, str3, str4, str5, i2, str6, str7);
        appendFragment(stringBuffer, str8);
        return stringBuffer.toString();
    }

    private static void appendSchemeSpecificPart(StringBuffer stringBuffer, String str, String str2, String str3, String str4, int i2, String str5, String str6) {
        String strSubstring;
        String strSubstring2;
        if (str != null) {
            if (str.startsWith("//[")) {
                int iIndexOf = str.indexOf("]");
                if (iIndexOf != -1 && str.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) != -1) {
                    if (iIndexOf == str.length()) {
                        strSubstring = str;
                        strSubstring2 = "";
                    } else {
                        strSubstring = str.substring(0, iIndexOf + 1);
                        strSubstring2 = str.substring(iIndexOf + 1);
                    }
                    stringBuffer.append(strSubstring);
                    stringBuffer.append(quote(strSubstring2, L_URIC, H_URIC));
                    return;
                }
                return;
            }
            stringBuffer.append(quote(str, L_URIC, H_URIC));
            return;
        }
        appendAuthority(stringBuffer, str2, str3, str4, i2);
        if (str5 != null) {
            stringBuffer.append(quote(str5, L_PATH, H_PATH));
        }
        if (str6 != null) {
            stringBuffer.append('?');
            stringBuffer.append(quote(str6, L_URIC, H_URIC));
        }
    }

    private static void appendAuthority(StringBuffer stringBuffer, String str, String str2, String str3, int i2) {
        String strSubstring;
        String strSubstring2;
        if (str3 != null) {
            stringBuffer.append("//");
            if (str2 != null) {
                stringBuffer.append(quote(str2, L_USERINFO, H_USERINFO));
                stringBuffer.append('@');
            }
            boolean z2 = (str3.indexOf(58) < 0 || str3.startsWith("[") || str3.endsWith("]")) ? false : true;
            if (z2) {
                stringBuffer.append('[');
            }
            stringBuffer.append(str3);
            if (z2) {
                stringBuffer.append(']');
            }
            if (i2 != -1) {
                stringBuffer.append(':');
                stringBuffer.append(i2);
                return;
            }
            return;
        }
        if (str != null) {
            stringBuffer.append("//");
            if (str.startsWith("[")) {
                int iIndexOf = str.indexOf("]");
                if (iIndexOf != -1 && str.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) != -1) {
                    if (iIndexOf == str.length()) {
                        strSubstring = str;
                        strSubstring2 = "";
                    } else {
                        strSubstring = str.substring(0, iIndexOf + 1);
                        strSubstring2 = str.substring(iIndexOf + 1);
                    }
                    stringBuffer.append(strSubstring);
                    stringBuffer.append(quote(strSubstring2, L_REG_NAME | L_SERVER, H_REG_NAME | H_SERVER));
                    return;
                }
                return;
            }
            stringBuffer.append(quote(str, L_REG_NAME | L_SERVER, H_REG_NAME | H_SERVER));
        }
    }

    private static void appendFragment(StringBuffer stringBuffer, String str) {
        if (str != null) {
            stringBuffer.append('#');
            stringBuffer.append(quote(str, L_URIC, H_URIC));
        }
    }

    private static String quote(String str, long j2, long j3) {
        str.length();
        StringBuffer stringBuffer = null;
        boolean z2 = (j2 & 1) != 0;
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt < 128) {
                if (!match(cCharAt, j2, j3) && !isEscaped(str, i2)) {
                    if (stringBuffer == null) {
                        stringBuffer = new StringBuffer();
                        stringBuffer.append(str.substring(0, i2));
                    }
                    appendEscape(stringBuffer, (byte) cCharAt);
                } else if (stringBuffer != null) {
                    stringBuffer.append(cCharAt);
                }
            } else if (z2 && (Character.isSpaceChar(cCharAt) || Character.isISOControl(cCharAt))) {
                if (stringBuffer == null) {
                    stringBuffer = new StringBuffer();
                    stringBuffer.append(str.substring(0, i2));
                }
                appendEncoded(stringBuffer, cCharAt);
            } else if (stringBuffer != null) {
                stringBuffer.append(cCharAt);
            }
        }
        return stringBuffer == null ? str : stringBuffer.toString();
    }

    private static boolean isEscaped(String str, int i2) {
        return str != null && str.length() > i2 + 2 && str.charAt(i2) == '%' && match(str.charAt(i2 + 1), L_HEX, H_HEX) && match(str.charAt(i2 + 2), L_HEX, H_HEX);
    }

    private static void appendEncoded(StringBuffer stringBuffer, char c2) {
        ByteBuffer byteBufferEncode = null;
        try {
            byteBufferEncode = ThreadLocalCoders.encoderFor("UTF-8").encode(CharBuffer.wrap("" + c2));
        } catch (CharacterCodingException e2) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        }
        while (byteBufferEncode.hasRemaining()) {
            int i2 = byteBufferEncode.get() & 255;
            if (i2 >= 128) {
                appendEscape(stringBuffer, (byte) i2);
            } else {
                stringBuffer.append((char) i2);
            }
        }
    }

    private static void appendEscape(StringBuffer stringBuffer, byte b2) {
        stringBuffer.append('%');
        stringBuffer.append(hexDigits[(b2 >> 4) & 15]);
        stringBuffer.append(hexDigits[(b2 >> 0) & 15]);
    }

    private static boolean match(char c2, long j2, long j3) {
        return c2 < '@' ? ((1 << c2) & j2) != 0 : c2 < 128 && ((1 << (c2 - 64)) & j3) != 0;
    }

    private static void checkPath(String str, String str2, String str3) throws URISyntaxException {
        if (str2 != null && str3 != null && str3.length() > 0 && str3.charAt(0) != '/') {
            throw new URISyntaxException(str, "Relative path in absolute URI");
        }
    }

    private static long lowMask(char c2, char c3) {
        long j2 = 0;
        for (int iMax = Math.max(Math.min((int) c2, 63), 0); iMax <= Math.max(Math.min((int) c3, 63), 0); iMax++) {
            j2 |= 1 << iMax;
        }
        return j2;
    }

    private static long lowMask(String str) {
        int length = str.length();
        long j2 = 0;
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt < '@') {
                j2 |= 1 << cCharAt;
            }
        }
        return j2;
    }

    private static long highMask(char c2, char c3) {
        long j2 = 0;
        for (int iMax = Math.max(Math.min((int) c2, 127), 64) - 64; iMax <= Math.max(Math.min((int) c3, 127), 64) - 64; iMax++) {
            j2 |= 1 << iMax;
        }
        return j2;
    }

    private static long highMask(String str) {
        int length = str.length();
        long j2 = 0;
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt >= '@' && cCharAt < 128) {
                j2 |= 1 << (cCharAt - '@');
            }
        }
        return j2;
    }
}
