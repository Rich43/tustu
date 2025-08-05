package com.sun.jndi.toolkit.url;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/jndi/toolkit/url/Uri.class */
public class Uri {
    protected String uri;
    protected String scheme;
    protected boolean hasAuthority;
    protected String path;
    protected String fragment;
    protected String host = null;
    protected int port = -1;
    protected String query = null;

    /* loaded from: rt.jar:com/sun/jndi/toolkit/url/Uri$ParseMode.class */
    public enum ParseMode {
        STRICT,
        COMPAT,
        LEGACY
    }

    public Uri(String str) throws MalformedURLException {
        init(str);
    }

    protected Uri() {
    }

    protected ParseMode parseMode() {
        return ParseMode.COMPAT;
    }

    protected void init(String str) throws MalformedURLException {
        this.uri = str;
        parse(str, parseMode());
    }

    public String getScheme() {
        return this.scheme;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getPath() {
        return this.path;
    }

    public String getQuery() {
        return this.query;
    }

    public String toString() {
        return this.uri;
    }

    private void parse(String str, ParseMode parseMode) throws MalformedURLException {
        switch (parseMode) {
            case STRICT:
                parseStrict(str);
                break;
            case COMPAT:
                parseCompat(str);
                break;
            case LEGACY:
                parseLegacy(str);
                break;
        }
    }

    private void parseStrict(String str) throws MalformedURLException {
        try {
            if (!isSchemeOnly(str)) {
                URI uri = new URI(str);
                this.scheme = uri.getScheme();
                if (this.scheme == null) {
                    throw new MalformedURLException("Invalid URI: " + str);
                }
                String rawAuthority = uri.getRawAuthority();
                this.hasAuthority = rawAuthority != null;
                if (this.hasAuthority) {
                    String host = uri.getHost();
                    int port = uri.getPort();
                    if (host != null) {
                        this.host = host;
                    }
                    if (port != -1) {
                        this.port = port;
                    }
                    if (!((host == null ? "" : host) + (port == -1 ? "" : CallSiteDescriptor.TOKEN_DELIMITER + port)).equals(rawAuthority)) {
                        throw new MalformedURLException("unsupported authority: " + rawAuthority);
                    }
                }
                this.path = uri.getRawPath();
                if (uri.getRawQuery() != null) {
                    this.query = "?" + uri.getRawQuery();
                }
                if (uri.getRawFragment() != null) {
                    if (!acceptsFragment()) {
                        throw new MalformedURLException("URI fragments not supported: " + str);
                    }
                    this.fragment = FXMLLoader.CONTROLLER_METHOD_PREFIX + uri.getRawFragment();
                }
            } else {
                String strSubstring = str.substring(0, str.indexOf(58));
                URI uri2 = new URI(str + "/");
                if (!strSubstring.equals(uri2.getScheme()) || !checkSchemeOnly(str, uri2.getScheme())) {
                    throw newInvalidURISchemeException(str);
                }
                this.scheme = strSubstring;
                this.path = "";
            }
        } catch (URISyntaxException e2) {
            MalformedURLException malformedURLException = new MalformedURLException(e2.getMessage());
            malformedURLException.initCause(e2);
            throw malformedURLException;
        }
    }

    private void parseCompat(String str) throws MalformedURLException {
        int iIndexOf = str.indexOf(58);
        int iIndexOf2 = str.indexOf(47);
        int iIndexOf3 = str.indexOf(63);
        int iIndexOf4 = str.indexOf(35);
        if (iIndexOf < 0 || ((iIndexOf2 > 0 && iIndexOf > iIndexOf2) || ((iIndexOf3 > 0 && iIndexOf > iIndexOf3) || (iIndexOf4 > 0 && iIndexOf > iIndexOf4)))) {
            throw new MalformedURLException("Invalid URI: " + str);
        }
        if (iIndexOf4 > -1 && !acceptsFragment()) {
            throw new MalformedURLException("URI fragments not supported: " + str);
        }
        if (iIndexOf == str.length() - 1 && !isSchemeOnly(str)) {
            throw newInvalidURISchemeException(str);
        }
        this.scheme = str.substring(0, iIndexOf);
        int i2 = iIndexOf + 1;
        this.hasAuthority = str.startsWith("//", i2);
        if (iIndexOf4 > -1 && iIndexOf3 > iIndexOf4) {
            iIndexOf3 = -1;
        }
        int length = iIndexOf3 > -1 ? iIndexOf3 : iIndexOf4 > -1 ? iIndexOf4 : str.length();
        if (this.hasAuthority) {
            int i3 = i2 + 2;
            int iIndexOf5 = str.indexOf(47, i3);
            if (iIndexOf5 == -1 || (iIndexOf3 > -1 && iIndexOf3 < iIndexOf5)) {
                iIndexOf5 = iIndexOf3;
            }
            if (iIndexOf5 == -1 || (iIndexOf4 > -1 && iIndexOf4 < iIndexOf5)) {
                iIndexOf5 = iIndexOf4;
            }
            if (iIndexOf5 < 0) {
                iIndexOf5 = str.length();
            }
            if (str.startsWith(CallSiteDescriptor.TOKEN_DELIMITER, i3)) {
                int i4 = i3 + 1;
                this.host = "";
                if (iIndexOf5 > i4) {
                    this.port = Integer.parseInt(str.substring(i4, iIndexOf5));
                }
            } else {
                try {
                    URI uri = new URI(str.substring(0, iIndexOf5) + "/");
                    String strSubstring = str.substring(i3, iIndexOf5);
                    this.host = uri.getHost();
                    this.port = uri.getPort();
                    String rawPath = uri.getRawPath();
                    String rawQuery = uri.getRawQuery();
                    String rawFragment = uri.getRawFragment();
                    String rawUserInfo = uri.getRawUserInfo();
                    if (rawUserInfo != null) {
                        throw new MalformedURLException("user info not supported in authority: " + rawUserInfo);
                    }
                    if (!"/".equals(rawPath)) {
                        throw new MalformedURLException("invalid authority: " + strSubstring);
                    }
                    if (rawQuery != null) {
                        throw new MalformedURLException("invalid trailing characters in authority: ?" + rawQuery);
                    }
                    if (rawFragment != null) {
                        throw new MalformedURLException("invalid trailing characters in authority: #" + rawFragment);
                    }
                    if (!strSubstring.equals((this.host == null ? "" : this.host) + (this.port == -1 ? "" : CallSiteDescriptor.TOKEN_DELIMITER + this.port))) {
                        throw new MalformedURLException("unsupported authority: " + strSubstring);
                    }
                } catch (URISyntaxException e2) {
                    MalformedURLException malformedURLException = new MalformedURLException(e2.getMessage());
                    malformedURLException.initCause(e2);
                    throw malformedURLException;
                }
            }
            i2 = iIndexOf5;
        }
        this.path = str.substring(i2, length);
        if (iIndexOf3 > -1) {
            if (iIndexOf4 > -1) {
                this.query = str.substring(iIndexOf3, iIndexOf4);
            } else {
                this.query = str.substring(iIndexOf3);
            }
        }
        if (iIndexOf4 > -1) {
            this.fragment = str.substring(iIndexOf4);
        }
    }

    protected boolean isSchemeOnly(String str) {
        return false;
    }

    protected boolean checkSchemeOnly(String str, String str2) {
        return str.equals(str2 + CallSiteDescriptor.TOKEN_DELIMITER);
    }

    protected MalformedURLException newInvalidURISchemeException(String str) {
        return new MalformedURLException("Invalid URI scheme: " + str);
    }

    protected boolean acceptsFragment() {
        return parseMode() == ParseMode.LEGACY;
    }

    private void parseLegacy(String str) throws MalformedURLException {
        int i2;
        int iIndexOf = str.indexOf(58);
        if (iIndexOf < 0) {
            throw new MalformedURLException("Invalid URI: " + str);
        }
        this.scheme = str.substring(0, iIndexOf);
        int i3 = iIndexOf + 1;
        this.hasAuthority = str.startsWith("//", i3);
        if (this.hasAuthority) {
            int i4 = i3 + 2;
            int iIndexOf2 = str.indexOf(47, i4);
            if (iIndexOf2 < 0) {
                iIndexOf2 = str.length();
            }
            if (str.startsWith("[", i4)) {
                int iIndexOf3 = str.indexOf(93, i4 + 1);
                if (iIndexOf3 < 0 || iIndexOf3 > iIndexOf2) {
                    throw new MalformedURLException("Invalid URI: " + str);
                }
                this.host = str.substring(i4, iIndexOf3 + 1);
                i2 = iIndexOf3 + 1;
            } else {
                int iIndexOf4 = str.indexOf(58, i4);
                int i5 = (iIndexOf4 < 0 || iIndexOf4 > iIndexOf2) ? iIndexOf2 : iIndexOf4;
                if (i4 < i5) {
                    this.host = str.substring(i4, i5);
                }
                i2 = i5;
            }
            if (i2 + 1 < iIndexOf2 && str.startsWith(CallSiteDescriptor.TOKEN_DELIMITER, i2)) {
                this.port = Integer.parseInt(str.substring(i2 + 1, iIndexOf2));
            }
            i3 = iIndexOf2;
        }
        int iIndexOf5 = str.indexOf(63, i3);
        if (iIndexOf5 < 0) {
            this.path = str.substring(i3);
        } else {
            this.path = str.substring(i3, iIndexOf5);
            this.query = str.substring(iIndexOf5);
        }
    }
}
