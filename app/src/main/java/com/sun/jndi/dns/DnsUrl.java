package com.sun.jndi.dns;

import com.sun.jndi.toolkit.url.Uri;
import com.sun.jndi.toolkit.url.UrlUtil;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.AccessController;
import java.util.Locale;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:com/sun/jndi/dns/DnsUrl.class */
public class DnsUrl extends Uri {
    private static final String PARSE_MODE_PROP = "com.sun.jndi.dnsURLParsing";
    private static final Uri.ParseMode DEFAULT_PARSE_MODE = Uri.ParseMode.COMPAT;
    public static final Uri.ParseMode PARSE_MODE;
    private String domain;

    static {
        GetPropertyAction getPropertyAction = new GetPropertyAction(PARSE_MODE_PROP, DEFAULT_PARSE_MODE.toString());
        Uri.ParseMode parseModeValueOf = DEFAULT_PARSE_MODE;
        try {
            try {
                parseModeValueOf = Uri.ParseMode.valueOf(((String) AccessController.doPrivileged(getPropertyAction)).toUpperCase(Locale.ROOT));
                PARSE_MODE = parseModeValueOf;
            } catch (Throwable th) {
                parseModeValueOf = DEFAULT_PARSE_MODE;
                PARSE_MODE = parseModeValueOf;
            }
        } catch (Throwable th2) {
            PARSE_MODE = parseModeValueOf;
            throw th2;
        }
    }

    public static DnsUrl[] fromList(String str) throws MalformedURLException {
        DnsUrl[] dnsUrlArr = new DnsUrl[(str.length() + 1) / 2];
        int i2 = 0;
        StringTokenizer stringTokenizer = new StringTokenizer(str, " ");
        while (stringTokenizer.hasMoreTokens()) {
            try {
                int i3 = i2;
                i2++;
                dnsUrlArr[i3] = new DnsUrl(validateURI(stringTokenizer.nextToken()));
            } catch (URISyntaxException e2) {
                MalformedURLException malformedURLException = new MalformedURLException(e2.getMessage());
                malformedURLException.initCause(e2);
                throw malformedURLException;
            }
        }
        DnsUrl[] dnsUrlArr2 = new DnsUrl[i2];
        System.arraycopy(dnsUrlArr, 0, dnsUrlArr2, 0, i2);
        return dnsUrlArr2;
    }

    @Override // com.sun.jndi.toolkit.url.Uri
    protected Uri.ParseMode parseMode() {
        return PARSE_MODE;
    }

    @Override // com.sun.jndi.toolkit.url.Uri
    protected final boolean isSchemeOnly(String str) {
        return isDnsSchemeOnly(str);
    }

    @Override // com.sun.jndi.toolkit.url.Uri
    protected boolean checkSchemeOnly(String str, String str2) {
        return str.equals(new StringBuilder().append(str2).append(CallSiteDescriptor.TOKEN_DELIMITER).toString()) || str.equals(new StringBuilder().append(str2).append("://").toString());
    }

    @Override // com.sun.jndi.toolkit.url.Uri
    protected final MalformedURLException newInvalidURISchemeException(String str) {
        return new MalformedURLException(str + " is not a valid DNS pseudo-URL");
    }

    private static boolean isDnsSchemeOnly(String str) {
        return "dns:".equals(str) || "dns://".equals(str);
    }

    private static String validateURI(String str) throws URISyntaxException {
        if (PARSE_MODE != Uri.ParseMode.LEGACY && !isDnsSchemeOnly(str)) {
            return new URI(str).toString();
        }
        return str;
    }

    public DnsUrl(String str) throws MalformedURLException {
        super(str);
        if (!this.scheme.equals("dns")) {
            throw newInvalidURISchemeException(str);
        }
        this.domain = this.path.startsWith("/") ? this.path.substring(1) : this.path;
        this.domain = this.domain.equals("") ? "." : UrlUtil.decode(this.domain);
    }

    public String getDomain() {
        return this.domain;
    }
}
