package java.net;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.text.Normalizer;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.nio.cs.ThreadLocalCoders;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:java/net/URI.class */
public final class URI implements Comparable<URI>, Serializable {
    static final long serialVersionUID = -6052424284110960213L;
    private transient String scheme;
    private transient String fragment;
    private transient String authority;
    private transient String userInfo;
    private transient String host;
    private transient int port;
    private transient String path;
    private transient String query;
    private volatile transient String schemeSpecificPart;
    private volatile transient int hash;
    private volatile transient String decodedUserInfo;
    private volatile transient String decodedAuthority;
    private volatile transient String decodedPath;
    private volatile transient String decodedQuery;
    private volatile transient String decodedFragment;
    private volatile transient String decodedSchemeSpecificPart;
    private volatile String string;
    private static final long L_DIGIT;
    private static final long H_DIGIT = 0;
    private static final long L_UPALPHA = 0;
    private static final long H_UPALPHA;
    private static final long L_LOWALPHA = 0;
    private static final long H_LOWALPHA;
    private static final long L_ALPHA = 0;
    private static final long H_ALPHA;
    private static final long L_ALPHANUM;
    private static final long H_ALPHANUM;
    private static final long L_HEX;
    private static final long H_HEX;
    private static final long L_MARK;
    private static final long H_MARK;
    private static final long L_UNRESERVED;
    private static final long H_UNRESERVED;
    private static final long L_RESERVED;
    private static final long H_RESERVED;
    private static final long L_ESCAPED = 1;
    private static final long H_ESCAPED = 0;
    private static final long L_URIC;
    private static final long H_URIC;
    private static final long L_PCHAR;
    private static final long H_PCHAR;
    private static final long L_PATH;
    private static final long H_PATH;
    private static final long L_DASH;
    private static final long H_DASH;
    private static final long L_DOT;
    private static final long H_DOT;
    private static final long L_USERINFO;
    private static final long H_USERINFO;
    private static final long L_REG_NAME;
    private static final long H_REG_NAME;
    private static final long L_SERVER;
    private static final long H_SERVER;
    private static final long L_SERVER_PERCENT;
    private static final long H_SERVER_PERCENT;
    private static final long L_LEFT_BRACKET;
    private static final long H_LEFT_BRACKET;
    private static final long L_SCHEME;
    private static final long H_SCHEME;
    private static final long L_URIC_NO_SLASH;
    private static final long H_URIC_NO_SLASH;
    private static final char[] hexDigits;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !URI.class.desiredAssertionStatus();
        L_DIGIT = lowMask('0', '9');
        H_UPALPHA = highMask('A', 'Z');
        H_LOWALPHA = highMask('a', 'z');
        H_ALPHA = H_LOWALPHA | H_UPALPHA;
        L_ALPHANUM = L_DIGIT | 0;
        H_ALPHANUM = 0 | H_ALPHA;
        L_HEX = L_DIGIT;
        H_HEX = highMask('A', 'F') | highMask('a', 'f');
        L_MARK = lowMask("-_.!~*'()");
        H_MARK = highMask("-_.!~*'()");
        L_UNRESERVED = L_ALPHANUM | L_MARK;
        H_UNRESERVED = H_ALPHANUM | H_MARK;
        L_RESERVED = lowMask(";/?:@&=+$,[]");
        H_RESERVED = highMask(";/?:@&=+$,[]");
        L_URIC = L_RESERVED | L_UNRESERVED | 1;
        H_URIC = H_RESERVED | H_UNRESERVED | 0;
        L_PCHAR = L_UNRESERVED | 1 | lowMask(":@&=+$,");
        H_PCHAR = H_UNRESERVED | 0 | highMask(":@&=+$,");
        L_PATH = L_PCHAR | lowMask(";/");
        H_PATH = H_PCHAR | highMask(";/");
        L_DASH = lowMask(LanguageTag.SEP);
        H_DASH = highMask(LanguageTag.SEP);
        L_DOT = lowMask(".");
        H_DOT = highMask(".");
        L_USERINFO = L_UNRESERVED | 1 | lowMask(";:&=+$,");
        H_USERINFO = H_UNRESERVED | 0 | highMask(";:&=+$,");
        L_REG_NAME = L_UNRESERVED | 1 | lowMask("$,;:@&=+");
        H_REG_NAME = H_UNRESERVED | 0 | highMask("$,;:@&=+");
        L_SERVER = L_USERINFO | L_ALPHANUM | L_DASH | lowMask(".:@[]");
        H_SERVER = H_USERINFO | H_ALPHANUM | H_DASH | highMask(".:@[]");
        L_SERVER_PERCENT = L_SERVER | lowMask(FXMLLoader.RESOURCE_KEY_PREFIX);
        H_SERVER_PERCENT = H_SERVER | highMask(FXMLLoader.RESOURCE_KEY_PREFIX);
        L_LEFT_BRACKET = lowMask("[");
        H_LEFT_BRACKET = highMask("[");
        L_SCHEME = 0 | L_DIGIT | lowMask("+-.");
        H_SCHEME = H_ALPHA | 0 | highMask("+-.");
        L_URIC_NO_SLASH = L_UNRESERVED | 1 | lowMask(";?:@&=+$,");
        H_URIC_NO_SLASH = H_UNRESERVED | 0 | highMask(";?:@&=+$,");
        hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    }

    private URI() {
        this.port = -1;
        this.decodedUserInfo = null;
        this.decodedAuthority = null;
        this.decodedPath = null;
        this.decodedQuery = null;
        this.decodedFragment = null;
        this.decodedSchemeSpecificPart = null;
    }

    public URI(String str) throws URISyntaxException {
        this.port = -1;
        this.decodedUserInfo = null;
        this.decodedAuthority = null;
        this.decodedPath = null;
        this.decodedQuery = null;
        this.decodedFragment = null;
        this.decodedSchemeSpecificPart = null;
        new Parser(str).parse(false);
    }

    public URI(String str, String str2, String str3, int i2, String str4, String str5, String str6) throws URISyntaxException {
        this.port = -1;
        this.decodedUserInfo = null;
        this.decodedAuthority = null;
        this.decodedPath = null;
        this.decodedQuery = null;
        this.decodedFragment = null;
        this.decodedSchemeSpecificPart = null;
        String string = toString(str, null, null, str2, str3, i2, str4, str5, str6);
        checkPath(string, str, str4);
        new Parser(string).parse(true);
    }

    public URI(String str, String str2, String str3, String str4, String str5) throws URISyntaxException {
        this.port = -1;
        this.decodedUserInfo = null;
        this.decodedAuthority = null;
        this.decodedPath = null;
        this.decodedQuery = null;
        this.decodedFragment = null;
        this.decodedSchemeSpecificPart = null;
        String string = toString(str, null, str2, null, null, -1, str3, str4, str5);
        checkPath(string, str, str3);
        new Parser(string).parse(false);
    }

    public URI(String str, String str2, String str3, String str4) throws URISyntaxException {
        this(str, null, str2, -1, str3, null, str4);
    }

    public URI(String str, String str2, String str3) throws URISyntaxException {
        this.port = -1;
        this.decodedUserInfo = null;
        this.decodedAuthority = null;
        this.decodedPath = null;
        this.decodedQuery = null;
        this.decodedFragment = null;
        this.decodedSchemeSpecificPart = null;
        new Parser(toString(str, str2, null, null, null, -1, null, null, str3)).parse(false);
    }

    public static URI create(String str) {
        try {
            return new URI(str);
        } catch (URISyntaxException e2) {
            throw new IllegalArgumentException(e2.getMessage(), e2);
        }
    }

    public URI parseServerAuthority() throws URISyntaxException {
        if (this.host != null || this.authority == null) {
            return this;
        }
        defineString();
        new Parser(this.string).parse(true);
        return this;
    }

    public URI normalize() {
        return normalize(this);
    }

    public URI resolve(URI uri) {
        return resolve(this, uri);
    }

    public URI resolve(String str) {
        return resolve(create(str));
    }

    public URI relativize(URI uri) {
        return relativize(this, uri);
    }

    public URL toURL() throws MalformedURLException {
        if (!isAbsolute()) {
            throw new IllegalArgumentException("URI is not absolute");
        }
        return new URL(toString());
    }

    public String getScheme() {
        return this.scheme;
    }

    public boolean isAbsolute() {
        return this.scheme != null;
    }

    public boolean isOpaque() {
        return this.path == null;
    }

    public String getRawSchemeSpecificPart() {
        defineSchemeSpecificPart();
        return this.schemeSpecificPart;
    }

    public String getSchemeSpecificPart() {
        if (this.decodedSchemeSpecificPart == null) {
            this.decodedSchemeSpecificPart = decode(getRawSchemeSpecificPart());
        }
        return this.decodedSchemeSpecificPart;
    }

    public String getRawAuthority() {
        return this.authority;
    }

    public String getAuthority() {
        if (this.decodedAuthority == null) {
            this.decodedAuthority = decode(this.authority);
        }
        return this.decodedAuthority;
    }

    public String getRawUserInfo() {
        return this.userInfo;
    }

    public String getUserInfo() {
        if (this.decodedUserInfo == null && this.userInfo != null) {
            this.decodedUserInfo = decode(this.userInfo);
        }
        return this.decodedUserInfo;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getRawPath() {
        return this.path;
    }

    public String getPath() {
        if (this.decodedPath == null && this.path != null) {
            this.decodedPath = decode(this.path);
        }
        return this.decodedPath;
    }

    public String getRawQuery() {
        return this.query;
    }

    public String getQuery() {
        if (this.decodedQuery == null && this.query != null) {
            this.decodedQuery = decode(this.query);
        }
        return this.decodedQuery;
    }

    public String getRawFragment() {
        return this.fragment;
    }

    public String getFragment() {
        if (this.decodedFragment == null && this.fragment != null) {
            this.decodedFragment = decode(this.fragment);
        }
        return this.decodedFragment;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof URI)) {
            return false;
        }
        URI uri = (URI) obj;
        if (isOpaque() != uri.isOpaque() || !equalIgnoringCase(this.scheme, uri.scheme) || !equal(this.fragment, uri.fragment)) {
            return false;
        }
        if (isOpaque()) {
            return equal(this.schemeSpecificPart, uri.schemeSpecificPart);
        }
        if (!equal(this.path, uri.path) || !equal(this.query, uri.query)) {
            return false;
        }
        if (this.authority == uri.authority) {
            return true;
        }
        if (this.host != null) {
            return equal(this.userInfo, uri.userInfo) && equalIgnoringCase(this.host, uri.host) && this.port == uri.port;
        }
        if (this.authority != null) {
            return equal(this.authority, uri.authority);
        }
        if (this.authority != uri.authority) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int iHash;
        if (this.hash != 0) {
            return this.hash;
        }
        int iHash2 = hash(hashIgnoringCase(0, this.scheme), this.fragment);
        if (isOpaque()) {
            iHash = hash(iHash2, this.schemeSpecificPart);
        } else {
            int iHash3 = hash(hash(iHash2, this.path), this.query);
            if (this.host != null) {
                iHash = hashIgnoringCase(hash(iHash3, this.userInfo), this.host) + (1949 * this.port);
            } else {
                iHash = hash(iHash3, this.authority);
            }
        }
        this.hash = iHash;
        return iHash;
    }

    @Override // java.lang.Comparable
    public int compareTo(URI uri) {
        int iCompareIgnoringCase = compareIgnoringCase(this.scheme, uri.scheme);
        if (iCompareIgnoringCase != 0) {
            return iCompareIgnoringCase;
        }
        if (isOpaque()) {
            if (uri.isOpaque()) {
                int iCompare = compare(this.schemeSpecificPart, uri.schemeSpecificPart);
                if (iCompare != 0) {
                    return iCompare;
                }
                return compare(this.fragment, uri.fragment);
            }
            return 1;
        }
        if (uri.isOpaque()) {
            return -1;
        }
        if (this.host != null && uri.host != null) {
            int iCompare2 = compare(this.userInfo, uri.userInfo);
            if (iCompare2 != 0) {
                return iCompare2;
            }
            int iCompareIgnoringCase2 = compareIgnoringCase(this.host, uri.host);
            if (iCompareIgnoringCase2 != 0) {
                return iCompareIgnoringCase2;
            }
            int i2 = this.port - uri.port;
            if (i2 != 0) {
                return i2;
            }
        } else {
            int iCompare3 = compare(this.authority, uri.authority);
            if (iCompare3 != 0) {
                return iCompare3;
            }
        }
        int iCompare4 = compare(this.path, uri.path);
        if (iCompare4 != 0) {
            return iCompare4;
        }
        int iCompare5 = compare(this.query, uri.query);
        return iCompare5 != 0 ? iCompare5 : compare(this.fragment, uri.fragment);
    }

    public String toString() {
        defineString();
        return this.string;
    }

    public String toASCIIString() {
        defineString();
        return encode(this.string);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        defineString();
        objectOutputStream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.port = -1;
        objectInputStream.defaultReadObject();
        try {
            new Parser(this.string).parse(false);
        } catch (URISyntaxException e2) {
            InvalidObjectException invalidObjectException = new InvalidObjectException("Invalid URI");
            invalidObjectException.initCause(e2);
            throw invalidObjectException;
        }
    }

    private static int toLower(char c2) {
        if (c2 >= 'A' && c2 <= 'Z') {
            return c2 + ' ';
        }
        return c2;
    }

    private static int toUpper(char c2) {
        if (c2 >= 'a' && c2 <= 'z') {
            return c2 - ' ';
        }
        return c2;
    }

    private static boolean equal(String str, String str2) {
        if (str == str2) {
            return true;
        }
        if (str == null || str2 == null || str.length() != str2.length()) {
            return false;
        }
        if (str.indexOf(37) < 0) {
            return str.equals(str2);
        }
        int length = str.length();
        int i2 = 0;
        while (i2 < length) {
            char cCharAt = str.charAt(i2);
            char cCharAt2 = str2.charAt(i2);
            if (cCharAt != '%') {
                if (cCharAt != cCharAt2) {
                    return false;
                }
                i2++;
            } else {
                if (cCharAt2 != '%') {
                    return false;
                }
                int i3 = i2 + 1;
                if (toLower(str.charAt(i3)) != toLower(str2.charAt(i3))) {
                    return false;
                }
                int i4 = i3 + 1;
                if (toLower(str.charAt(i4)) != toLower(str2.charAt(i4))) {
                    return false;
                }
                i2 = i4 + 1;
            }
        }
        return true;
    }

    private static boolean equalIgnoringCase(String str, String str2) {
        int length;
        if (str == str2) {
            return true;
        }
        if (str == null || str2 == null || str2.length() != (length = str.length())) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (toLower(str.charAt(i2)) != toLower(str2.charAt(i2))) {
                return false;
            }
        }
        return true;
    }

    private static int hash(int i2, String str) {
        return str == null ? i2 : str.indexOf(37) < 0 ? (i2 * 127) + str.hashCode() : normalizedHash(i2, str);
    }

    private static int normalizedHash(int i2, String str) {
        int upper = 0;
        int i3 = 0;
        while (i3 < str.length()) {
            char cCharAt = str.charAt(i3);
            upper = (31 * upper) + cCharAt;
            if (cCharAt == '%') {
                for (int i4 = i3 + 1; i4 < i3 + 3; i4++) {
                    upper = (31 * upper) + toUpper(str.charAt(i4));
                }
                i3 += 2;
            }
            i3++;
        }
        return (i2 * 127) + upper;
    }

    private static int hashIgnoringCase(int i2, String str) {
        if (str == null) {
            return i2;
        }
        int lower = i2;
        int length = str.length();
        for (int i3 = 0; i3 < length; i3++) {
            lower = (31 * lower) + toLower(str.charAt(i3));
        }
        return lower;
    }

    private static int compare(String str, String str2) {
        if (str == str2) {
            return 0;
        }
        if (str != null) {
            if (str2 != null) {
                return str.compareTo(str2);
            }
            return 1;
        }
        return -1;
    }

    private static int compareIgnoringCase(String str, String str2) {
        if (str == str2) {
            return 0;
        }
        if (str != null) {
            if (str2 != null) {
                int length = str.length();
                int length2 = str2.length();
                int i2 = length < length2 ? length : length2;
                for (int i3 = 0; i3 < i2; i3++) {
                    int lower = toLower(str.charAt(i3)) - toLower(str2.charAt(i3));
                    if (lower != 0) {
                        return lower;
                    }
                }
                return length - length2;
            }
            return 1;
        }
        return -1;
    }

    private static void checkPath(String str, String str2, String str3) throws URISyntaxException {
        if (str2 != null && str3 != null && str3.length() > 0 && str3.charAt(0) != '/') {
            throw new URISyntaxException(str, "Relative path in absolute URI");
        }
    }

    private void appendAuthority(StringBuffer stringBuffer, String str, String str2, String str3, int i2) {
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
                String strSubstring = str;
                String strSubstring2 = "";
                if (iIndexOf != -1 && str.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) != -1) {
                    if (iIndexOf == str.length()) {
                        strSubstring2 = str;
                        strSubstring = "";
                    } else {
                        strSubstring2 = str.substring(0, iIndexOf + 1);
                        strSubstring = str.substring(iIndexOf + 1);
                    }
                }
                stringBuffer.append(strSubstring2);
                stringBuffer.append(quote(strSubstring, L_REG_NAME | L_SERVER, H_REG_NAME | H_SERVER));
                return;
            }
            stringBuffer.append(quote(str, L_REG_NAME | L_SERVER, H_REG_NAME | H_SERVER));
        }
    }

    private void appendSchemeSpecificPart(StringBuffer stringBuffer, String str, String str2, String str3, String str4, int i2, String str5, String str6) {
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

    private void appendFragment(StringBuffer stringBuffer, String str) {
        if (str != null) {
            stringBuffer.append('#');
            stringBuffer.append(quote(str, L_URIC, H_URIC));
        }
    }

    private String toString(String str, String str2, String str3, String str4, String str5, int i2, String str6, String str7, String str8) {
        StringBuffer stringBuffer = new StringBuffer();
        if (str != null) {
            stringBuffer.append(str);
            stringBuffer.append(':');
        }
        appendSchemeSpecificPart(stringBuffer, str2, str3, str4, str5, i2, str6, str7);
        appendFragment(stringBuffer, str8);
        return stringBuffer.toString();
    }

    private void defineSchemeSpecificPart() {
        if (this.schemeSpecificPart != null) {
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        appendSchemeSpecificPart(stringBuffer, null, getAuthority(), getUserInfo(), this.host, this.port, getPath(), getQuery());
        this.schemeSpecificPart = stringBuffer.toString();
    }

    private void defineString() {
        if (this.string != null) {
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (this.scheme != null) {
            stringBuffer.append(this.scheme);
            stringBuffer.append(':');
        }
        if (isOpaque()) {
            stringBuffer.append(this.schemeSpecificPart);
        } else {
            if (this.host != null) {
                stringBuffer.append("//");
                if (this.userInfo != null) {
                    stringBuffer.append(this.userInfo);
                    stringBuffer.append('@');
                }
                boolean z2 = (this.host.indexOf(58) < 0 || this.host.startsWith("[") || this.host.endsWith("]")) ? false : true;
                if (z2) {
                    stringBuffer.append('[');
                }
                stringBuffer.append(this.host);
                if (z2) {
                    stringBuffer.append(']');
                }
                if (this.port != -1) {
                    stringBuffer.append(':');
                    stringBuffer.append(this.port);
                }
            } else if (this.authority != null) {
                stringBuffer.append("//");
                stringBuffer.append(this.authority);
            }
            if (this.path != null) {
                stringBuffer.append(this.path);
            }
            if (this.query != null) {
                stringBuffer.append('?');
                stringBuffer.append(this.query);
            }
        }
        if (this.fragment != null) {
            stringBuffer.append('#');
            stringBuffer.append(this.fragment);
        }
        this.string = stringBuffer.toString();
    }

    private static String resolvePath(String str, String str2, boolean z2) {
        int iLastIndexOf = str.lastIndexOf(47);
        int length = str2.length();
        String string = "";
        if (length == 0) {
            if (iLastIndexOf >= 0) {
                string = str.substring(0, iLastIndexOf + 1);
            }
        } else {
            StringBuffer stringBuffer = new StringBuffer(str.length() + length);
            if (iLastIndexOf >= 0) {
                stringBuffer.append(str.substring(0, iLastIndexOf + 1));
            }
            stringBuffer.append(str2);
            string = stringBuffer.toString();
        }
        return normalize(string);
    }

    private static URI resolve(URI uri, URI uri2) {
        if (uri2.isOpaque() || uri.isOpaque()) {
            return uri2;
        }
        if (uri2.scheme == null && uri2.authority == null && uri2.path.equals("") && uri2.fragment != null && uri2.query == null) {
            if (uri.fragment != null && uri2.fragment.equals(uri.fragment)) {
                return uri;
            }
            URI uri3 = new URI();
            uri3.scheme = uri.scheme;
            uri3.authority = uri.authority;
            uri3.userInfo = uri.userInfo;
            uri3.host = uri.host;
            uri3.port = uri.port;
            uri3.path = uri.path;
            uri3.fragment = uri2.fragment;
            uri3.query = uri.query;
            return uri3;
        }
        if (uri2.scheme != null) {
            return uri2;
        }
        URI uri4 = new URI();
        uri4.scheme = uri.scheme;
        uri4.query = uri2.query;
        uri4.fragment = uri2.fragment;
        if (uri2.authority == null) {
            uri4.authority = uri.authority;
            uri4.host = uri.host;
            uri4.userInfo = uri.userInfo;
            uri4.port = uri.port;
            String str = uri2.path == null ? "" : uri2.path;
            if (str.length() > 0 && str.charAt(0) == '/') {
                uri4.path = uri2.path;
            } else {
                uri4.path = resolvePath(uri.path, str, uri.isAbsolute());
            }
        } else {
            uri4.authority = uri2.authority;
            uri4.host = uri2.host;
            uri4.userInfo = uri2.userInfo;
            uri4.host = uri2.host;
            uri4.port = uri2.port;
            uri4.path = uri2.path;
        }
        return uri4;
    }

    private static URI normalize(URI uri) {
        if (uri.isOpaque() || uri.path == null || uri.path.length() == 0) {
            return uri;
        }
        String strNormalize = normalize(uri.path);
        if (strNormalize == uri.path) {
            return uri;
        }
        URI uri2 = new URI();
        uri2.scheme = uri.scheme;
        uri2.fragment = uri.fragment;
        uri2.authority = uri.authority;
        uri2.userInfo = uri.userInfo;
        uri2.host = uri.host;
        uri2.port = uri.port;
        uri2.path = strNormalize;
        uri2.query = uri.query;
        return uri2;
    }

    private static URI relativize(URI uri, URI uri2) {
        if (uri2.isOpaque() || uri.isOpaque()) {
            return uri2;
        }
        if (!equalIgnoringCase(uri.scheme, uri2.scheme) || !equal(uri.authority, uri2.authority)) {
            return uri2;
        }
        String strNormalize = normalize(uri.path);
        String strNormalize2 = normalize(uri2.path);
        if (!strNormalize.equals(strNormalize2)) {
            if (!strNormalize.endsWith("/")) {
                strNormalize = strNormalize + "/";
            }
            if (!strNormalize2.startsWith(strNormalize)) {
                return uri2;
            }
        }
        URI uri3 = new URI();
        uri3.path = strNormalize2.substring(strNormalize.length());
        uri3.query = uri2.query;
        uri3.fragment = uri2.fragment;
        return uri3;
    }

    private static int needsNormalization(String str) {
        boolean z2 = true;
        int i2 = 0;
        int length = str.length() - 1;
        int i3 = 0;
        while (i3 <= length && str.charAt(i3) == '/') {
            i3++;
        }
        if (i3 > 1) {
            z2 = false;
        }
        while (i3 <= length) {
            if (str.charAt(i3) == '.' && (i3 == length || str.charAt(i3 + 1) == '/' || (str.charAt(i3 + 1) == '.' && (i3 + 1 == length || str.charAt(i3 + 2) == '/')))) {
                z2 = false;
            }
            i2++;
            while (true) {
                if (i3 <= length) {
                    int i4 = i3;
                    i3++;
                    if (str.charAt(i4) == '/') {
                        while (i3 <= length && str.charAt(i3) == '/') {
                            z2 = false;
                            i3++;
                        }
                    }
                }
            }
        }
        if (z2) {
            return -1;
        }
        return i2;
    }

    private static void split(char[] cArr, int[] iArr) {
        int length = cArr.length - 1;
        int i2 = 0;
        int i3 = 0;
        while (i2 <= length && cArr[i2] == '/') {
            cArr[i2] = 0;
            i2++;
        }
        while (i2 <= length) {
            int i4 = i3;
            i3++;
            int i5 = i2;
            i2++;
            iArr[i4] = i5;
            while (true) {
                if (i2 <= length) {
                    int i6 = i2;
                    i2++;
                    if (cArr[i6] == '/') {
                        cArr[i2 - 1] = 0;
                        while (i2 <= length && cArr[i2] == '/') {
                            int i7 = i2;
                            i2++;
                            cArr[i7] = 0;
                        }
                    }
                }
            }
        }
        if (i3 != iArr.length) {
            throw new InternalError();
        }
    }

    private static int join(char[] cArr, int[] iArr) {
        int length = iArr.length;
        int length2 = cArr.length - 1;
        int i2 = 0;
        if (cArr[0] == 0) {
            i2 = 0 + 1;
            cArr[0] = '/';
        }
        for (int i3 = 0; i3 < length; i3++) {
            int i4 = iArr[i3];
            if (i4 != -1) {
                if (i2 == i4) {
                    while (i2 <= length2 && cArr[i2] != 0) {
                        i2++;
                    }
                    if (i2 <= length2) {
                        int i5 = i2;
                        i2++;
                        cArr[i5] = '/';
                    }
                } else if (i2 < i4) {
                    while (i4 <= length2 && cArr[i4] != 0) {
                        int i6 = i2;
                        i2++;
                        int i7 = i4;
                        i4++;
                        cArr[i6] = cArr[i7];
                    }
                    if (i4 <= length2) {
                        int i8 = i2;
                        i2++;
                        cArr[i8] = '/';
                    }
                } else {
                    throw new InternalError();
                }
            }
        }
        return i2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x005a, code lost:
    
        r9 = 2;
     */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0069 A[EDGE_INSN: B:60:0x0069->B:23:0x0069 BREAK  A[LOOP:1: B:6:0x0014->B:61:?], SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:61:? A[LOOP:1: B:6:0x0014->B:61:?, LOOP_END, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void removeDots(char[] r4, int[] r5) {
        /*
            Method dump skipped, instructions count: 218
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.URI.removeDots(char[], int[]):void");
    }

    private static void maybeAddLeadingDot(char[] cArr, int[] iArr) {
        if (cArr[0] == 0) {
            return;
        }
        int length = iArr.length;
        int i2 = 0;
        while (i2 < length && iArr[i2] < 0) {
            i2++;
        }
        if (i2 >= length || i2 == 0) {
            return;
        }
        int i3 = iArr[i2];
        while (i3 < cArr.length && cArr[i3] != ':' && cArr[i3] != 0) {
            i3++;
        }
        if (i3 >= cArr.length || cArr[i3] == 0) {
            return;
        }
        cArr[0] = '.';
        cArr[1] = 0;
        iArr[0] = 0;
    }

    private static String normalize(String str) {
        int iNeedsNormalization = needsNormalization(str);
        if (iNeedsNormalization < 0) {
            return str;
        }
        char[] charArray = str.toCharArray();
        int[] iArr = new int[iNeedsNormalization];
        split(charArray, iArr);
        removeDots(charArray, iArr);
        maybeAddLeadingDot(charArray, iArr);
        String str2 = new String(charArray, 0, join(charArray, iArr));
        if (str2.equals(str)) {
            return str;
        }
        return str2;
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

    private static long lowMask(char c2, char c3) {
        long j2 = 0;
        for (int iMax = Math.max(Math.min((int) c2, 63), 0); iMax <= Math.max(Math.min((int) c3, 63), 0); iMax++) {
            j2 |= 1 << iMax;
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

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean match(char c2, long j2, long j3) {
        if (c2 == 0) {
            return false;
        }
        return c2 < '@' ? ((1 << c2) & j2) != 0 : c2 < 128 && ((1 << (c2 - 64)) & j3) != 0;
    }

    private static void appendEscape(StringBuffer stringBuffer, byte b2) {
        stringBuffer.append('%');
        stringBuffer.append(hexDigits[(b2 >> 4) & 15]);
        stringBuffer.append(hexDigits[(b2 >> 0) & 15]);
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

    private static String quote(String str, long j2, long j3) {
        str.length();
        StringBuffer stringBuffer = null;
        boolean z2 = (j2 & 1) != 0;
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt < 128) {
                if (!match(cCharAt, j2, j3)) {
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

    private static String encode(String str) {
        int length = str.length();
        if (length == 0) {
            return str;
        }
        int i2 = 0;
        while (str.charAt(i2) < 128) {
            i2++;
            if (i2 >= length) {
                return str;
            }
        }
        ByteBuffer byteBufferEncode = null;
        try {
            byteBufferEncode = ThreadLocalCoders.encoderFor("UTF-8").encode(CharBuffer.wrap(Normalizer.normalize(str, Normalizer.Form.NFC)));
        } catch (CharacterCodingException e2) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        while (byteBufferEncode.hasRemaining()) {
            int i3 = byteBufferEncode.get() & 255;
            if (i3 >= 128) {
                appendEscape(stringBuffer, (byte) i3);
            } else {
                stringBuffer.append((char) i3);
            }
        }
        return stringBuffer.toString();
    }

    private static int decode(char c2) {
        if (c2 >= '0' && c2 <= '9') {
            return c2 - '0';
        }
        if (c2 >= 'a' && c2 <= 'f') {
            return (c2 - 'a') + 10;
        }
        if (c2 >= 'A' && c2 <= 'F') {
            return (c2 - 'A') + 10;
        }
        if ($assertionsDisabled) {
            return -1;
        }
        throw new AssertionError();
    }

    private static byte decode(char c2, char c3) {
        return (byte) (((decode(c2) & 15) << 4) | ((decode(c3) & 15) << 0));
    }

    private static String decode(String str) {
        if (str == null) {
            return str;
        }
        int length = str.length();
        if (length == 0) {
            return str;
        }
        if (str.indexOf(37) < 0) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer(length);
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(length);
        CharBuffer charBufferAllocate = CharBuffer.allocate(length);
        CharsetDecoder charsetDecoderOnUnmappableCharacter = ThreadLocalCoders.decoderFor("UTF-8").onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
        char cCharAt = str.charAt(0);
        boolean z2 = false;
        int i2 = 0;
        while (i2 < length) {
            if (!$assertionsDisabled && cCharAt != str.charAt(i2)) {
                throw new AssertionError();
            }
            if (cCharAt == '[') {
                z2 = true;
            } else if (z2 && cCharAt == ']') {
                z2 = false;
            }
            if (cCharAt != '%' || z2) {
                stringBuffer.append(cCharAt);
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
                    int i3 = i2 + 1;
                    char cCharAt2 = str.charAt(i3);
                    int i4 = i3 + 1;
                    byteBufferAllocate.put(decode(cCharAt2, str.charAt(i4)));
                    i2 = i4 + 1;
                    if (i2 >= length) {
                        break;
                    }
                    cCharAt = str.charAt(i2);
                } while (cCharAt == '%');
                byteBufferAllocate.flip();
                charBufferAllocate.clear();
                charsetDecoderOnUnmappableCharacter.reset();
                CoderResult coderResultDecode = charsetDecoderOnUnmappableCharacter.decode(byteBufferAllocate, charBufferAllocate, true);
                if (!$assertionsDisabled && !coderResultDecode.isUnderflow()) {
                    throw new AssertionError();
                }
                CoderResult coderResultFlush = charsetDecoderOnUnmappableCharacter.flush(charBufferAllocate);
                if (!$assertionsDisabled && !coderResultFlush.isUnderflow()) {
                    throw new AssertionError();
                }
                stringBuffer.append(charBufferAllocate.flip().toString());
            }
        }
        return stringBuffer.toString();
    }

    /* loaded from: rt.jar:java/net/URI$Parser.class */
    private class Parser {
        private String input;
        private boolean requireServerAuthority = false;
        private int ipv6byteCount = 0;

        Parser(String str) {
            this.input = str;
            URI.this.string = str;
        }

        private void fail(String str) throws URISyntaxException {
            throw new URISyntaxException(this.input, str);
        }

        private void fail(String str, int i2) throws URISyntaxException {
            throw new URISyntaxException(this.input, str, i2);
        }

        private void failExpecting(String str, int i2) throws URISyntaxException {
            fail("Expected " + str, i2);
        }

        private void failExpecting(String str, String str2, int i2) throws URISyntaxException {
            fail("Expected " + str + " following " + str2, i2);
        }

        private String substring(int i2, int i3) {
            return this.input.substring(i2, i3);
        }

        private char charAt(int i2) {
            return this.input.charAt(i2);
        }

        private boolean at(int i2, int i3, char c2) {
            return i2 < i3 && charAt(i2) == c2;
        }

        private boolean at(int i2, int i3, String str) {
            int i4 = i2;
            int length = str.length();
            if (length > i3 - i4) {
                return false;
            }
            int i5 = 0;
            while (i5 < length) {
                int i6 = i4;
                i4++;
                if (charAt(i6) != str.charAt(i5)) {
                    break;
                }
                i5++;
            }
            return i5 == length;
        }

        private int scan(int i2, int i3, char c2) {
            if (i2 < i3 && charAt(i2) == c2) {
                return i2 + 1;
            }
            return i2;
        }

        private int scan(int i2, int i3, String str, String str2) {
            int i4 = i2;
            while (i4 < i3) {
                char cCharAt = charAt(i4);
                if (str.indexOf(cCharAt) >= 0) {
                    return -1;
                }
                if (str2.indexOf(cCharAt) >= 0) {
                    break;
                }
                i4++;
            }
            return i4;
        }

        private int scanEscape(int i2, int i3, char c2) throws URISyntaxException {
            if (c2 == '%') {
                if (i2 + 3 <= i3 && URI.match(charAt(i2 + 1), URI.L_HEX, URI.H_HEX) && URI.match(charAt(i2 + 2), URI.L_HEX, URI.H_HEX)) {
                    return i2 + 3;
                }
                fail("Malformed escape pair", i2);
            } else if (c2 > 128 && !Character.isSpaceChar(c2) && !Character.isISOControl(c2)) {
                return i2 + 1;
            }
            return i2;
        }

        private int scan(int i2, int i3, long j2, long j3) throws URISyntaxException {
            int iScanEscape;
            int i4 = i2;
            while (i4 < i3) {
                char cCharAt = charAt(i4);
                if (URI.match(cCharAt, j2, j3)) {
                    i4++;
                } else {
                    if ((j2 & 1) == 0 || (iScanEscape = scanEscape(i4, i3, cCharAt)) <= i4) {
                        break;
                    }
                    i4 = iScanEscape;
                }
            }
            return i4;
        }

        private void checkChars(int i2, int i3, long j2, long j3, String str) throws URISyntaxException {
            int iScan = scan(i2, i3, j2, j3);
            if (iScan < i3) {
                fail("Illegal character in " + str, iScan);
            }
        }

        private void checkChar(int i2, long j2, long j3, String str) throws URISyntaxException {
            checkChars(i2, i2 + 1, j2, j3, str);
        }

        void parse(boolean z2) throws URISyntaxException {
            int i2;
            int hierarchical;
            this.requireServerAuthority = z2;
            int length = this.input.length();
            int iScan = scan(0, length, "/?#", CallSiteDescriptor.TOKEN_DELIMITER);
            if (iScan >= 0 && at(iScan, length, ':')) {
                if (iScan == 0) {
                    failExpecting("scheme name", 0);
                }
                checkChar(0, 0L, URI.H_ALPHA, "scheme name");
                checkChars(1, iScan, URI.L_SCHEME, URI.H_SCHEME, "scheme name");
                URI.this.scheme = substring(0, iScan);
                int i3 = iScan + 1;
                i2 = i3;
                if (at(i3, length, '/')) {
                    hierarchical = parseHierarchical(i3, length);
                } else {
                    int iScan2 = scan(i3, length, "", FXMLLoader.CONTROLLER_METHOD_PREFIX);
                    if (iScan2 <= i3) {
                        failExpecting("scheme-specific part", i3);
                    }
                    checkChars(i3, iScan2, URI.L_URIC, URI.H_URIC, "opaque part");
                    hierarchical = iScan2;
                }
            } else {
                i2 = 0;
                hierarchical = parseHierarchical(0, length);
            }
            URI.this.schemeSpecificPart = substring(i2, hierarchical);
            if (at(hierarchical, length, '#')) {
                checkChars(hierarchical + 1, length, URI.L_URIC, URI.H_URIC, "fragment");
                URI.this.fragment = substring(hierarchical + 1, length);
                hierarchical = length;
            }
            if (hierarchical < length) {
                fail("end of URI", hierarchical);
            }
        }

        private int parseHierarchical(int i2, int i3) throws URISyntaxException {
            int authority = i2;
            if (at(authority, i3, '/') && at(authority + 1, i3, '/')) {
                authority += 2;
                int iScan = scan(authority, i3, "", "/?#");
                if (iScan > authority) {
                    authority = parseAuthority(authority, iScan);
                } else if (iScan >= i3) {
                    failExpecting("authority", authority);
                }
            }
            int iScan2 = scan(authority, i3, "", "?#");
            checkChars(authority, iScan2, URI.L_PATH, URI.H_PATH, "path");
            URI.this.path = substring(authority, iScan2);
            int i4 = iScan2;
            if (at(i4, i3, '?')) {
                int i5 = i4 + 1;
                int iScan3 = scan(i5, i3, "", FXMLLoader.CONTROLLER_METHOD_PREFIX);
                checkChars(i5, iScan3, URI.L_URIC, URI.H_URIC, "query");
                URI.this.query = substring(i5, iScan3);
                i4 = iScan3;
            }
            return i4;
        }

        private int parseAuthority(int i2, int i3) throws URISyntaxException {
            boolean z2;
            int server = i2;
            URISyntaxException uRISyntaxException = null;
            if (scan(i2, i3, "", "]") > i2) {
                z2 = scan(i2, i3, URI.L_SERVER_PERCENT, URI.H_SERVER_PERCENT) == i3;
            } else {
                z2 = scan(i2, i3, URI.L_SERVER, URI.H_SERVER) == i3;
            }
            boolean z3 = scan(i2, i3, URI.L_REG_NAME, URI.H_REG_NAME) == i3;
            if (z3 && !z2) {
                URI.this.authority = substring(i2, i3);
                return i3;
            }
            if (z2) {
                try {
                    server = parseServer(i2, i3);
                    if (server < i3) {
                        failExpecting("end of authority", server);
                    }
                    URI.this.authority = substring(i2, i3);
                } catch (URISyntaxException e2) {
                    URI.this.userInfo = null;
                    URI.this.host = null;
                    URI.this.port = -1;
                    if (this.requireServerAuthority) {
                        throw e2;
                    }
                    uRISyntaxException = e2;
                    server = i2;
                }
            }
            if (server < i3) {
                if (z3) {
                    URI.this.authority = substring(i2, i3);
                } else {
                    if (uRISyntaxException != null) {
                        throw uRISyntaxException;
                    }
                    fail("Illegal character in authority", server);
                }
            }
            return i3;
        }

        private int parseServer(int i2, int i3) throws URISyntaxException {
            int i4;
            int i5 = i2;
            int iScan = scan(i5, i3, "/?#", "@");
            if (iScan >= i5 && at(iScan, i3, '@')) {
                checkChars(i5, iScan, URI.L_USERINFO, URI.H_USERINFO, "user info");
                URI.this.userInfo = substring(i5, iScan);
                i5 = iScan + 1;
            }
            if (at(i5, i3, '[')) {
                i4 = i5 + 1;
                int iScan2 = scan(i4, i3, "/?#", "]");
                if (iScan2 > i4 && at(iScan2, i3, ']')) {
                    int iScan3 = scan(i4, iScan2, "", FXMLLoader.RESOURCE_KEY_PREFIX);
                    if (iScan3 > i4) {
                        parseIPv6Reference(i4, iScan3);
                        if (iScan3 + 1 == iScan2) {
                            fail("scope id expected");
                        }
                        checkChars(iScan3 + 1, iScan2, URI.L_ALPHANUM, URI.H_ALPHANUM, "scope id");
                    } else {
                        parseIPv6Reference(i4, iScan2);
                    }
                    URI.this.host = substring(i4 - 1, iScan2 + 1);
                    i4 = iScan2 + 1;
                } else {
                    failExpecting("closing bracket for IPv6 address", iScan2);
                }
            } else {
                int iPv4Address = parseIPv4Address(i5, i3);
                if (iPv4Address <= i5) {
                    iPv4Address = parseHostname(i5, i3);
                }
                i4 = iPv4Address;
            }
            if (at(i4, i3, ':')) {
                i4++;
                int iScan4 = scan(i4, i3, "", "/");
                if (iScan4 > i4) {
                    checkChars(i4, iScan4, URI.L_DIGIT, 0L, "port number");
                    try {
                        URI.this.port = Integer.parseInt(substring(i4, iScan4));
                    } catch (NumberFormatException e2) {
                        fail("Malformed port number", i4);
                    }
                    i4 = iScan4;
                }
            }
            if (i4 < i3) {
                failExpecting("port number", i4);
            }
            return i4;
        }

        private int scanByte(int i2, int i3) throws URISyntaxException {
            int iScan = scan(i2, i3, URI.L_DIGIT, 0L);
            if (iScan <= i2) {
                return iScan;
            }
            if (Integer.parseInt(substring(i2, iScan)) > 255) {
                return i2;
            }
            return iScan;
        }

        private int scanIPv4Address(int i2, int i3, boolean z2) throws URISyntaxException {
            int iScan = scan(i2, i3, URI.L_DIGIT | URI.L_DOT, 0 | URI.H_DOT);
            if (iScan <= i2) {
                return -1;
            }
            if (z2 && iScan != i3) {
                return -1;
            }
            int iScanByte = scanByte(i2, iScan);
            int i4 = iScanByte;
            if (iScanByte > i2) {
                int iScan2 = scan(i4, iScan, '.');
                i4 = iScan2;
                if (iScan2 > i4) {
                    int iScanByte2 = scanByte(i4, iScan);
                    i4 = iScanByte2;
                    if (iScanByte2 > i4) {
                        int iScan3 = scan(i4, iScan, '.');
                        i4 = iScan3;
                        if (iScan3 > i4) {
                            int iScanByte3 = scanByte(i4, iScan);
                            i4 = iScanByte3;
                            if (iScanByte3 > i4) {
                                int iScan4 = scan(i4, iScan, '.');
                                i4 = iScan4;
                                if (iScan4 > i4) {
                                    int iScanByte4 = scanByte(i4, iScan);
                                    i4 = iScanByte4;
                                    if (iScanByte4 > i4 && i4 >= iScan) {
                                        return i4;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            fail("Malformed IPv4 address", i4);
            return -1;
        }

        private int takeIPv4Address(int i2, int i3, String str) throws URISyntaxException {
            int iScanIPv4Address = scanIPv4Address(i2, i3, true);
            if (iScanIPv4Address <= i2) {
                failExpecting(str, i2);
            }
            return iScanIPv4Address;
        }

        private int parseIPv4Address(int i2, int i3) {
            try {
                int iScanIPv4Address = scanIPv4Address(i2, i3, false);
                if (iScanIPv4Address > i2 && iScanIPv4Address < i3 && charAt(iScanIPv4Address) != ':') {
                    iScanIPv4Address = -1;
                }
                if (iScanIPv4Address > i2) {
                    URI.this.host = substring(i2, iScanIPv4Address);
                }
                return iScanIPv4Address;
            } catch (NumberFormatException e2) {
                return -1;
            } catch (URISyntaxException e3) {
                return -1;
            }
        }

        private int parseHostname(int i2, int i3) throws URISyntaxException {
            int i4 = i2;
            int i5 = -1;
            do {
                int iScan = scan(i4, i3, URI.L_ALPHANUM, URI.H_ALPHANUM);
                if (iScan <= i4) {
                    break;
                }
                i5 = i4;
                if (iScan > i4) {
                    i4 = iScan;
                    int iScan2 = scan(i4, i3, URI.L_ALPHANUM | URI.L_DASH, URI.H_ALPHANUM | URI.H_DASH);
                    if (iScan2 > i4) {
                        if (charAt(iScan2 - 1) == '-') {
                            fail("Illegal character in hostname", iScan2 - 1);
                        }
                        i4 = iScan2;
                    }
                }
                int iScan3 = scan(i4, i3, '.');
                if (iScan3 <= i4) {
                    break;
                }
                i4 = iScan3;
            } while (i4 < i3);
            if (i4 < i3 && !at(i4, i3, ':')) {
                fail("Illegal character in hostname", i4);
            }
            if (i5 < 0) {
                failExpecting("hostname", i2);
            }
            if (i5 > i2 && !URI.match(charAt(i5), 0L, URI.H_ALPHA)) {
                fail("Illegal character in hostname", i5);
            }
            URI.this.host = substring(i2, i4);
            return i4;
        }

        private int parseIPv6Reference(int i2, int i3) throws URISyntaxException {
            int iScanHexPost = i2;
            boolean z2 = false;
            int iScanHexSeq = scanHexSeq(iScanHexPost, i3);
            if (iScanHexSeq > iScanHexPost) {
                iScanHexPost = iScanHexSeq;
                if (at(iScanHexPost, i3, "::")) {
                    z2 = true;
                    iScanHexPost = scanHexPost(iScanHexPost + 2, i3);
                } else if (at(iScanHexPost, i3, ':')) {
                    iScanHexPost = takeIPv4Address(iScanHexPost + 1, i3, "IPv4 address");
                    this.ipv6byteCount += 4;
                }
            } else if (at(iScanHexPost, i3, "::")) {
                z2 = true;
                iScanHexPost = scanHexPost(iScanHexPost + 2, i3);
            }
            if (iScanHexPost < i3) {
                fail("Malformed IPv6 address", i2);
            }
            if (this.ipv6byteCount > 16) {
                fail("IPv6 address too long", i2);
            }
            if (!z2 && this.ipv6byteCount < 16) {
                fail("IPv6 address too short", i2);
            }
            if (z2 && this.ipv6byteCount == 16) {
                fail("Malformed IPv6 address", i2);
            }
            return iScanHexPost;
        }

        private int scanHexPost(int i2, int i3) throws URISyntaxException {
            int iTakeIPv4Address;
            if (i2 == i3) {
                return i2;
            }
            int iScanHexSeq = scanHexSeq(i2, i3);
            if (iScanHexSeq > i2) {
                iTakeIPv4Address = iScanHexSeq;
                if (at(iTakeIPv4Address, i3, ':')) {
                    iTakeIPv4Address = takeIPv4Address(iTakeIPv4Address + 1, i3, "hex digits or IPv4 address");
                    this.ipv6byteCount += 4;
                }
            } else {
                iTakeIPv4Address = takeIPv4Address(i2, i3, "hex digits or IPv4 address");
                this.ipv6byteCount += 4;
            }
            return iTakeIPv4Address;
        }

        private int scanHexSeq(int i2, int i3) throws URISyntaxException {
            int i4;
            int iScan = scan(i2, i3, URI.L_HEX, URI.H_HEX);
            if (iScan <= i2 || at(iScan, i3, '.')) {
                return -1;
            }
            if (iScan > i2 + 4) {
                fail("IPv6 hexadecimal digit sequence too long", i2);
            }
            this.ipv6byteCount += 2;
            while (true) {
                i4 = iScan;
                if (i4 >= i3 || !at(i4, i3, ':') || at(i4 + 1, i3, ':')) {
                    break;
                }
                int i5 = i4 + 1;
                iScan = scan(i5, i3, URI.L_HEX, URI.H_HEX);
                if (iScan <= i5) {
                    failExpecting("digits for an IPv6 address", i5);
                }
                if (at(iScan, i3, '.')) {
                    i4 = i5 - 1;
                    break;
                }
                if (iScan > i5 + 4) {
                    fail("IPv6 hexadecimal digit sequence too long", i5);
                }
                this.ipv6byteCount += 2;
            }
            return i4;
        }
    }
}
