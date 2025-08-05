package com.sun.org.apache.xml.internal.serializer.utils;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.IOException;
import java.util.Objects;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/utils/URI.class */
final class URI {
    private static final String RESERVED_CHARACTERS = ";/?:@&=+$,";
    private static final String MARK_CHARACTERS = "-_.!~*'() ";
    private static final String SCHEME_CHARACTERS = "+-.";
    private static final String USERINFO_CHARACTERS = ";:&=+$,";
    private String m_scheme;
    private String m_userinfo;
    private String m_host;
    private int m_port;
    private String m_path;
    private String m_queryString;
    private String m_fragment;
    private static boolean DEBUG = false;

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/utils/URI$MalformedURIException.class */
    public static class MalformedURIException extends IOException {
        public MalformedURIException() {
        }

        public MalformedURIException(String p_msg) {
            super(p_msg);
        }
    }

    public URI() {
        this.m_scheme = null;
        this.m_userinfo = null;
        this.m_host = null;
        this.m_port = -1;
        this.m_path = null;
        this.m_queryString = null;
        this.m_fragment = null;
    }

    public URI(URI p_other) {
        this.m_scheme = null;
        this.m_userinfo = null;
        this.m_host = null;
        this.m_port = -1;
        this.m_path = null;
        this.m_queryString = null;
        this.m_fragment = null;
        initialize(p_other);
    }

    public URI(String p_uriSpec) throws MalformedURIException {
        this((URI) null, p_uriSpec);
    }

    public URI(URI p_base, String p_uriSpec) throws MalformedURIException {
        this.m_scheme = null;
        this.m_userinfo = null;
        this.m_host = null;
        this.m_port = -1;
        this.m_path = null;
        this.m_queryString = null;
        this.m_fragment = null;
        initialize(p_base, p_uriSpec);
    }

    public URI(String p_scheme, String p_schemeSpecificPart) throws MalformedURIException {
        this.m_scheme = null;
        this.m_userinfo = null;
        this.m_host = null;
        this.m_port = -1;
        this.m_path = null;
        this.m_queryString = null;
        this.m_fragment = null;
        if (p_scheme == null || p_scheme.trim().length() == 0) {
            throw new MalformedURIException("Cannot construct URI with null/empty scheme!");
        }
        if (p_schemeSpecificPart == null || p_schemeSpecificPart.trim().length() == 0) {
            throw new MalformedURIException("Cannot construct URI with null/empty scheme-specific part!");
        }
        setScheme(p_scheme);
        setPath(p_schemeSpecificPart);
    }

    public URI(String p_scheme, String p_host, String p_path, String p_queryString, String p_fragment) throws MalformedURIException {
        this(p_scheme, null, p_host, -1, p_path, p_queryString, p_fragment);
    }

    public URI(String p_scheme, String p_userinfo, String p_host, int p_port, String p_path, String p_queryString, String p_fragment) throws MalformedURIException {
        this.m_scheme = null;
        this.m_userinfo = null;
        this.m_host = null;
        this.m_port = -1;
        this.m_path = null;
        this.m_queryString = null;
        this.m_fragment = null;
        if (p_scheme == null || p_scheme.trim().length() == 0) {
            throw new MalformedURIException(Utils.messages.createMessage("ER_SCHEME_REQUIRED", null));
        }
        if (p_host == null) {
            if (p_userinfo != null) {
                throw new MalformedURIException(Utils.messages.createMessage("ER_NO_USERINFO_IF_NO_HOST", null));
            }
            if (p_port != -1) {
                throw new MalformedURIException(Utils.messages.createMessage("ER_NO_PORT_IF_NO_HOST", null));
            }
        }
        if (p_path != null) {
            if (p_path.indexOf(63) != -1 && p_queryString != null) {
                throw new MalformedURIException(Utils.messages.createMessage("ER_NO_QUERY_STRING_IN_PATH", null));
            }
            if (p_path.indexOf(35) != -1 && p_fragment != null) {
                throw new MalformedURIException(Utils.messages.createMessage("ER_NO_FRAGMENT_STRING_IN_PATH", null));
            }
        }
        setScheme(p_scheme);
        setHost(p_host);
        setPort(p_port);
        setUserinfo(p_userinfo);
        setPath(p_path);
        setQueryString(p_queryString);
        setFragment(p_fragment);
    }

    private void initialize(URI p_other) {
        this.m_scheme = p_other.getScheme();
        this.m_userinfo = p_other.getUserinfo();
        this.m_host = p_other.getHost();
        this.m_port = p_other.getPort();
        this.m_path = p_other.getPath();
        this.m_queryString = p_other.getQueryString();
        this.m_fragment = p_other.getFragment();
    }

    private void initialize(URI p_base, String p_uriSpec) throws MalformedURIException {
        int segIndex;
        int lastSlash;
        char testChar;
        if (p_base == null && (p_uriSpec == null || p_uriSpec.trim().length() == 0)) {
            throw new MalformedURIException(Utils.messages.createMessage("ER_CANNOT_INIT_URI_EMPTY_PARMS", null));
        }
        if (p_uriSpec == null || p_uriSpec.trim().length() == 0) {
            initialize(p_base);
            return;
        }
        String uriSpec = p_uriSpec.trim();
        int uriSpecLen = uriSpec.length();
        int index = 0;
        int colonIndex = uriSpec.indexOf(58);
        if (colonIndex < 0) {
            if (p_base == null) {
                throw new MalformedURIException(Utils.messages.createMessage("ER_NO_SCHEME_IN_URI", new Object[]{uriSpec}));
            }
        } else {
            initializeScheme(uriSpec);
            uriSpec = uriSpec.substring(colonIndex + 1);
            uriSpecLen = uriSpec.length();
        }
        if (0 + 1 < uriSpecLen && uriSpec.substring(0).startsWith("//")) {
            index = 0 + 2;
            while (index < uriSpecLen && (testChar = uriSpec.charAt(index)) != '/' && testChar != '?' && testChar != '#') {
                index++;
            }
            if (index > index) {
                initializeAuthority(uriSpec.substring(index, index));
            } else {
                this.m_host = "";
            }
        }
        initializePath(uriSpec.substring(index));
        if (p_base != null) {
            if (this.m_path.length() == 0 && this.m_scheme == null && this.m_host == null) {
                this.m_scheme = p_base.getScheme();
                this.m_userinfo = p_base.getUserinfo();
                this.m_host = p_base.getHost();
                this.m_port = p_base.getPort();
                this.m_path = p_base.getPath();
                if (this.m_queryString == null) {
                    this.m_queryString = p_base.getQueryString();
                    return;
                }
                return;
            }
            if (this.m_scheme == null) {
                this.m_scheme = p_base.getScheme();
            }
            if (this.m_host == null) {
                this.m_userinfo = p_base.getUserinfo();
                this.m_host = p_base.getHost();
                this.m_port = p_base.getPort();
                if (this.m_path.length() > 0 && this.m_path.startsWith("/")) {
                    return;
                }
                String path = "";
                String basePath = p_base.getPath();
                if (basePath != null && (lastSlash = basePath.lastIndexOf(47)) != -1) {
                    path = basePath.substring(0, lastSlash + 1);
                }
                String path2 = path.concat(this.m_path);
                while (true) {
                    int index2 = path2.indexOf("/./");
                    if (index2 == -1) {
                        break;
                    } else {
                        path2 = path2.substring(0, index2 + 1).concat(path2.substring(index2 + 3));
                    }
                }
                if (path2.endsWith("/.")) {
                    path2 = path2.substring(0, path2.length() - 1);
                }
                while (true) {
                    int index3 = path2.indexOf("/../");
                    if (index3 <= 0) {
                        break;
                    }
                    String tempString = path2.substring(0, path2.indexOf("/../"));
                    int segIndex2 = tempString.lastIndexOf(47);
                    if (segIndex2 != -1) {
                        int segIndex3 = segIndex2 + 1;
                        if (!tempString.substring(segIndex2).equals(Constants.ATTRVAL_PARENT)) {
                            path2 = path2.substring(0, segIndex3).concat(path2.substring(index3 + 4));
                        }
                    }
                }
                if (path2.endsWith("/..") && (segIndex = path2.substring(0, path2.length() - 3).lastIndexOf(47)) != -1) {
                    path2 = path2.substring(0, segIndex + 1);
                }
                this.m_path = path2;
            }
        }
    }

    private void initializeScheme(String p_uriSpec) throws MalformedURIException {
        char testChar;
        int uriSpecLen = p_uriSpec.length();
        int index = 0;
        while (index < uriSpecLen && (testChar = p_uriSpec.charAt(index)) != ':' && testChar != '/' && testChar != '?' && testChar != '#') {
            index++;
        }
        String scheme = p_uriSpec.substring(0, index);
        if (scheme.length() == 0) {
            throw new MalformedURIException(Utils.messages.createMessage("ER_NO_SCHEME_INURI", null));
        }
        setScheme(scheme);
    }

    private void initializeAuthority(String p_uriSpec) throws MalformedURIException {
        int index = 0;
        int end = p_uriSpec.length();
        char testChar = 0;
        String userinfo = null;
        if (p_uriSpec.indexOf(64, 0) != -1) {
            while (index < end) {
                testChar = p_uriSpec.charAt(index);
                if (testChar == '@') {
                    break;
                } else {
                    index++;
                }
            }
            userinfo = p_uriSpec.substring(0, index);
            index++;
        }
        int start = index;
        while (index < end) {
            testChar = p_uriSpec.charAt(index);
            if (testChar == ':') {
                break;
            } else {
                index++;
            }
        }
        String host = p_uriSpec.substring(start, index);
        int port = -1;
        if (host.length() > 0 && testChar == ':') {
            int index2 = index + 1;
            while (index2 < end) {
                index2++;
            }
            String portStr = p_uriSpec.substring(index2, index2);
            if (portStr.length() > 0) {
                for (int i2 = 0; i2 < portStr.length(); i2++) {
                    if (!isDigit(portStr.charAt(i2))) {
                        throw new MalformedURIException(portStr + " is invalid. Port should only contain digits!");
                    }
                }
                try {
                    port = Integer.parseInt(portStr);
                } catch (NumberFormatException e2) {
                }
            }
        }
        setHost(host);
        setPort(port);
        setUserinfo(userinfo);
    }

    private void initializePath(String p_uriSpec) throws MalformedURIException {
        if (p_uriSpec == null) {
            throw new MalformedURIException("Cannot initialize path from null string!");
        }
        int index = 0;
        int end = p_uriSpec.length();
        char testChar = 0;
        while (index < end) {
            testChar = p_uriSpec.charAt(index);
            if (testChar == '?' || testChar == '#') {
                break;
            }
            if (testChar == '%') {
                if (index + 2 >= end || !isHex(p_uriSpec.charAt(index + 1)) || !isHex(p_uriSpec.charAt(index + 2))) {
                    throw new MalformedURIException(Utils.messages.createMessage("ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE", null));
                }
            } else if (!isReservedCharacter(testChar) && !isUnreservedCharacter(testChar) && '\\' != testChar) {
                throw new MalformedURIException(Utils.messages.createMessage("ER_PATH_INVALID_CHAR", new Object[]{String.valueOf(testChar)}));
            }
            index++;
        }
        this.m_path = p_uriSpec.substring(0, index);
        if (testChar == '?') {
            index++;
            while (index < end) {
                testChar = p_uriSpec.charAt(index);
                if (testChar == '#') {
                    break;
                }
                if (testChar == '%') {
                    if (index + 2 >= end || !isHex(p_uriSpec.charAt(index + 1)) || !isHex(p_uriSpec.charAt(index + 2))) {
                        throw new MalformedURIException("Query string contains invalid escape sequence!");
                    }
                } else if (!isReservedCharacter(testChar) && !isUnreservedCharacter(testChar)) {
                    throw new MalformedURIException("Query string contains invalid character:" + testChar);
                }
                index++;
            }
            this.m_queryString = p_uriSpec.substring(index, index);
        }
        if (testChar == '#') {
            int index2 = index + 1;
            while (index2 < end) {
                char testChar2 = p_uriSpec.charAt(index2);
                if (testChar2 == '%') {
                    if (index2 + 2 >= end || !isHex(p_uriSpec.charAt(index2 + 1)) || !isHex(p_uriSpec.charAt(index2 + 2))) {
                        throw new MalformedURIException("Fragment contains invalid escape sequence!");
                    }
                } else if (!isReservedCharacter(testChar2) && !isUnreservedCharacter(testChar2)) {
                    throw new MalformedURIException("Fragment contains invalid character:" + testChar2);
                }
                index2++;
            }
            this.m_fragment = p_uriSpec.substring(index2, index2);
        }
    }

    public String getScheme() {
        return this.m_scheme;
    }

    public String getSchemeSpecificPart() {
        StringBuilder schemespec = new StringBuilder();
        if (this.m_userinfo != null || this.m_host != null || this.m_port != -1) {
            schemespec.append("//");
        }
        if (this.m_userinfo != null) {
            schemespec.append(this.m_userinfo);
            schemespec.append('@');
        }
        if (this.m_host != null) {
            schemespec.append(this.m_host);
        }
        if (this.m_port != -1) {
            schemespec.append(':');
            schemespec.append(this.m_port);
        }
        if (this.m_path != null) {
            schemespec.append(this.m_path);
        }
        if (this.m_queryString != null) {
            schemespec.append('?');
            schemespec.append(this.m_queryString);
        }
        if (this.m_fragment != null) {
            schemespec.append('#');
            schemespec.append(this.m_fragment);
        }
        return schemespec.toString();
    }

    public String getUserinfo() {
        return this.m_userinfo;
    }

    public String getHost() {
        return this.m_host;
    }

    public int getPort() {
        return this.m_port;
    }

    public String getPath(boolean p_includeQueryString, boolean p_includeFragment) {
        StringBuilder pathString = new StringBuilder(this.m_path);
        if (p_includeQueryString && this.m_queryString != null) {
            pathString.append('?');
            pathString.append(this.m_queryString);
        }
        if (p_includeFragment && this.m_fragment != null) {
            pathString.append('#');
            pathString.append(this.m_fragment);
        }
        return pathString.toString();
    }

    public String getPath() {
        return this.m_path;
    }

    public String getQueryString() {
        return this.m_queryString;
    }

    public String getFragment() {
        return this.m_fragment;
    }

    public void setScheme(String p_scheme) throws MalformedURIException {
        if (p_scheme == null) {
            throw new MalformedURIException(Utils.messages.createMessage("ER_SCHEME_FROM_NULL_STRING", null));
        }
        if (!isConformantSchemeName(p_scheme)) {
            throw new MalformedURIException(Utils.messages.createMessage("ER_SCHEME_NOT_CONFORMANT", null));
        }
        this.m_scheme = p_scheme.toLowerCase();
    }

    public void setUserinfo(String p_userinfo) throws MalformedURIException {
        if (p_userinfo == null) {
            this.m_userinfo = null;
        } else {
            if (this.m_host == null) {
                throw new MalformedURIException("Userinfo cannot be set when host is null!");
            }
            int end = p_userinfo.length();
            for (int index = 0; index < end; index++) {
                char testChar = p_userinfo.charAt(index);
                if (testChar == '%') {
                    if (index + 2 >= end || !isHex(p_userinfo.charAt(index + 1)) || !isHex(p_userinfo.charAt(index + 2))) {
                        throw new MalformedURIException("Userinfo contains invalid escape sequence!");
                    }
                } else if (!isUnreservedCharacter(testChar) && USERINFO_CHARACTERS.indexOf(testChar) == -1) {
                    throw new MalformedURIException("Userinfo contains invalid character:" + testChar);
                }
            }
        }
        this.m_userinfo = p_userinfo;
    }

    public void setHost(String p_host) throws MalformedURIException {
        if (p_host == null || p_host.trim().length() == 0) {
            this.m_host = p_host;
            this.m_userinfo = null;
            this.m_port = -1;
        } else if (!isWellFormedAddress(p_host)) {
            throw new MalformedURIException(Utils.messages.createMessage("ER_HOST_ADDRESS_NOT_WELLFORMED", null));
        }
        this.m_host = p_host;
    }

    public void setPort(int p_port) throws MalformedURIException {
        if (p_port >= 0 && p_port <= 65535) {
            if (this.m_host == null) {
                throw new MalformedURIException(Utils.messages.createMessage("ER_PORT_WHEN_HOST_NULL", null));
            }
        } else if (p_port != -1) {
            throw new MalformedURIException(Utils.messages.createMessage("ER_INVALID_PORT", null));
        }
        this.m_port = p_port;
    }

    public void setPath(String p_path) throws MalformedURIException {
        if (p_path == null) {
            this.m_path = null;
            this.m_queryString = null;
            this.m_fragment = null;
            return;
        }
        initializePath(p_path);
    }

    public void appendPath(String p_addToPath) throws MalformedURIException {
        if (p_addToPath == null || p_addToPath.trim().length() == 0) {
            return;
        }
        if (!isURIString(p_addToPath)) {
            throw new MalformedURIException(Utils.messages.createMessage("ER_PATH_INVALID_CHAR", new Object[]{p_addToPath}));
        }
        if (this.m_path == null || this.m_path.trim().length() == 0) {
            if (p_addToPath.startsWith("/")) {
                this.m_path = p_addToPath;
                return;
            } else {
                this.m_path = "/" + p_addToPath;
                return;
            }
        }
        if (this.m_path.endsWith("/")) {
            if (p_addToPath.startsWith("/")) {
                this.m_path = this.m_path.concat(p_addToPath.substring(1));
                return;
            } else {
                this.m_path = this.m_path.concat(p_addToPath);
                return;
            }
        }
        if (p_addToPath.startsWith("/")) {
            this.m_path = this.m_path.concat(p_addToPath);
        } else {
            this.m_path = this.m_path.concat("/" + p_addToPath);
        }
    }

    public void setQueryString(String p_queryString) throws MalformedURIException {
        if (p_queryString == null) {
            this.m_queryString = null;
            return;
        }
        if (!isGenericURI()) {
            throw new MalformedURIException("Query string can only be set for a generic URI!");
        }
        if (getPath() == null) {
            throw new MalformedURIException("Query string cannot be set when path is null!");
        }
        if (!isURIString(p_queryString)) {
            throw new MalformedURIException("Query string contains invalid character!");
        }
        this.m_queryString = p_queryString;
    }

    public void setFragment(String p_fragment) throws MalformedURIException {
        if (p_fragment == null) {
            this.m_fragment = null;
            return;
        }
        if (!isGenericURI()) {
            throw new MalformedURIException(Utils.messages.createMessage("ER_FRAG_FOR_GENERIC_URI", null));
        }
        if (getPath() == null) {
            throw new MalformedURIException(Utils.messages.createMessage("ER_FRAG_WHEN_PATH_NULL", null));
        }
        if (!isURIString(p_fragment)) {
            throw new MalformedURIException(Utils.messages.createMessage("ER_FRAG_INVALID_CHAR", null));
        }
        this.m_fragment = p_fragment;
    }

    public boolean equals(Object p_test) {
        if (p_test instanceof URI) {
            URI testURI = (URI) p_test;
            if ((this.m_scheme == null && testURI.m_scheme == null) || (this.m_scheme != null && testURI.m_scheme != null && this.m_scheme.equals(testURI.m_scheme))) {
                if ((this.m_userinfo == null && testURI.m_userinfo == null) || (this.m_userinfo != null && testURI.m_userinfo != null && this.m_userinfo.equals(testURI.m_userinfo))) {
                    if (((this.m_host == null && testURI.m_host == null) || (this.m_host != null && testURI.m_host != null && this.m_host.equals(testURI.m_host))) && this.m_port == testURI.m_port) {
                        if ((this.m_path == null && testURI.m_path == null) || (this.m_path != null && testURI.m_path != null && this.m_path.equals(testURI.m_path))) {
                            if (!(this.m_queryString == null && testURI.m_queryString == null) && (this.m_queryString == null || testURI.m_queryString == null || !this.m_queryString.equals(testURI.m_queryString))) {
                                return false;
                            }
                            if (this.m_fragment != null || testURI.m_fragment != null) {
                                if (this.m_fragment != null && testURI.m_fragment != null && this.m_fragment.equals(testURI.m_fragment)) {
                                    return true;
                                }
                                return false;
                            }
                            return true;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        int hash = (41 * 5) + Objects.hashCode(this.m_scheme);
        return (41 * ((41 * ((41 * ((41 * ((41 * ((41 * hash) + Objects.hashCode(this.m_userinfo))) + Objects.hashCode(this.m_host))) + this.m_port)) + Objects.hashCode(this.m_path))) + Objects.hashCode(this.m_queryString))) + Objects.hashCode(this.m_fragment);
    }

    public String toString() {
        StringBuilder uriSpecString = new StringBuilder();
        if (this.m_scheme != null) {
            uriSpecString.append(this.m_scheme);
            uriSpecString.append(':');
        }
        uriSpecString.append(getSchemeSpecificPart());
        return uriSpecString.toString();
    }

    public boolean isGenericURI() {
        return this.m_host != null;
    }

    public static boolean isConformantSchemeName(String p_scheme) {
        if (p_scheme == null || p_scheme.trim().length() == 0 || !isAlpha(p_scheme.charAt(0))) {
            return false;
        }
        for (int i2 = 1; i2 < p_scheme.length(); i2++) {
            char testChar = p_scheme.charAt(i2);
            if (!isAlphanum(testChar) && SCHEME_CHARACTERS.indexOf(testChar) == -1) {
                return false;
            }
        }
        return true;
    }

    public static boolean isWellFormedAddress(String p_address) {
        String address;
        int addrLength;
        if (p_address == null || (addrLength = (address = p_address.trim()).length()) == 0 || addrLength > 255 || address.startsWith(".") || address.startsWith(LanguageTag.SEP)) {
            return false;
        }
        int index = address.lastIndexOf(46);
        if (address.endsWith(".")) {
            index = address.substring(0, index).lastIndexOf(46);
        }
        if (index + 1 < addrLength && isDigit(p_address.charAt(index + 1))) {
            int numDots = 0;
            for (int i2 = 0; i2 < addrLength; i2++) {
                char testChar = address.charAt(i2);
                if (testChar == '.') {
                    if (isDigit(address.charAt(i2 - 1))) {
                        if (i2 + 1 < addrLength && !isDigit(address.charAt(i2 + 1))) {
                            return false;
                        }
                        numDots++;
                    } else {
                        return false;
                    }
                } else if (!isDigit(testChar)) {
                    return false;
                }
            }
            if (numDots != 3) {
                return false;
            }
            return true;
        }
        for (int i3 = 0; i3 < addrLength; i3++) {
            char testChar2 = address.charAt(i3);
            if (testChar2 == '.') {
                if (!isAlphanum(address.charAt(i3 - 1))) {
                    return false;
                }
                if (i3 + 1 < addrLength && !isAlphanum(address.charAt(i3 + 1))) {
                    return false;
                }
            } else if (!isAlphanum(testChar2) && testChar2 != '-') {
                return false;
            }
        }
        return true;
    }

    private static boolean isDigit(char p_char) {
        return p_char >= '0' && p_char <= '9';
    }

    private static boolean isHex(char p_char) {
        return isDigit(p_char) || (p_char >= 'a' && p_char <= 'f') || (p_char >= 'A' && p_char <= 'F');
    }

    private static boolean isAlpha(char p_char) {
        return (p_char >= 'a' && p_char <= 'z') || (p_char >= 'A' && p_char <= 'Z');
    }

    private static boolean isAlphanum(char p_char) {
        return isAlpha(p_char) || isDigit(p_char);
    }

    private static boolean isReservedCharacter(char p_char) {
        return RESERVED_CHARACTERS.indexOf(p_char) != -1;
    }

    private static boolean isUnreservedCharacter(char p_char) {
        return isAlphanum(p_char) || MARK_CHARACTERS.indexOf(p_char) != -1;
    }

    private static boolean isURIString(String p_uric) {
        if (p_uric == null) {
            return false;
        }
        int end = p_uric.length();
        int i2 = 0;
        while (i2 < end) {
            char testChar = p_uric.charAt(i2);
            if (testChar == '%') {
                if (i2 + 2 >= end || !isHex(p_uric.charAt(i2 + 1)) || !isHex(p_uric.charAt(i2 + 2))) {
                    return false;
                }
                i2 += 2;
            } else if (!isReservedCharacter(testChar) && !isUnreservedCharacter(testChar)) {
                return false;
            }
            i2++;
        }
        return true;
    }
}
