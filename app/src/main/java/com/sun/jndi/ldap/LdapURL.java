package com.sun.jndi.ldap;

import com.sun.jndi.toolkit.url.Uri;
import com.sun.jndi.toolkit.url.UrlUtil;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.security.AccessController;
import java.util.Locale;
import java.util.StringTokenizer;
import javax.naming.NamingException;
import jdk.internal.dynalink.CallSiteDescriptor;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:com/sun/jndi/ldap/LdapURL.class */
public final class LdapURL extends Uri {
    private static final String PARSE_MODE_PROP = "com.sun.jndi.ldapURLParsing";
    private static final Uri.ParseMode DEFAULT_PARSE_MODE = Uri.ParseMode.COMPAT;
    public static final Uri.ParseMode PARSE_MODE;
    private boolean useSsl;
    private String DN = null;
    private String attributes = null;
    private String scope = null;
    private String filter = null;
    private String extensions = null;

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

    public LdapURL(String str) throws MalformedURLException, NamingException {
        this.useSsl = false;
        try {
            init(str);
            this.useSsl = this.scheme.equalsIgnoreCase("ldaps");
            if (!this.scheme.equalsIgnoreCase("ldap") && !this.useSsl) {
                throw newInvalidURISchemeException(str);
            }
            parsePathAndQuery();
        } catch (UnsupportedEncodingException e2) {
            NamingException namingException = new NamingException("Cannot parse url: " + str);
            namingException.setRootCause(e2);
            throw namingException;
        } catch (MalformedURLException e3) {
            NamingException namingException2 = new NamingException("Cannot parse url: " + str);
            namingException2.setRootCause(e3);
            throw namingException2;
        }
    }

    @Override // com.sun.jndi.toolkit.url.Uri
    protected MalformedURLException newInvalidURISchemeException(String str) {
        return new MalformedURLException("Not an LDAP URL: " + str);
    }

    @Override // com.sun.jndi.toolkit.url.Uri
    protected boolean isSchemeOnly(String str) {
        return isLdapSchemeOnly(str);
    }

    @Override // com.sun.jndi.toolkit.url.Uri
    protected Uri.ParseMode parseMode() {
        return PARSE_MODE;
    }

    public boolean useSsl() {
        return this.useSsl;
    }

    public String getDN() {
        return this.DN;
    }

    public String getAttributes() {
        return this.attributes;
    }

    public String getScope() {
        return this.scope;
    }

    public String getFilter() {
        return this.filter;
    }

    public String getExtensions() {
        return this.extensions;
    }

    public static String[] fromList(String str) throws NamingException {
        String[] strArr = new String[(str.length() + 1) / 2];
        int i2 = 0;
        StringTokenizer stringTokenizer = new StringTokenizer(str, " ");
        while (stringTokenizer.hasMoreTokens()) {
            int i3 = i2;
            i2++;
            strArr[i3] = validateURI(stringTokenizer.nextToken());
        }
        String[] strArr2 = new String[i2];
        System.arraycopy(strArr, 0, strArr2, 0, i2);
        return strArr2;
    }

    public static boolean isLdapSchemeOnly(String str) {
        return "ldap:".equals(str) || "ldaps:".equals(str);
    }

    public static String validateURI(String str) {
        if (PARSE_MODE == Uri.ParseMode.LEGACY) {
            return str;
        }
        if (isLdapSchemeOnly(str)) {
            return str;
        }
        return URI.create(str).toString();
    }

    public static boolean hasQueryComponents(String str) {
        return str.lastIndexOf(63) != -1;
    }

    static String toUrlString(String str, int i2, String str2, boolean z2) {
        try {
            String str3 = str != null ? str : "";
            if (str3.indexOf(58) != -1 && str3.charAt(0) != '[') {
                str3 = "[" + str3 + "]";
            }
            String str4 = i2 != -1 ? CallSiteDescriptor.TOKEN_DELIMITER + i2 : "";
            String str5 = str2 != null ? "/" + UrlUtil.encode(str2, InternalZipConstants.CHARSET_UTF8) : "";
            return validateURI(z2 ? "ldaps://" + str3 + str4 + str5 : "ldap://" + str3 + str4 + str5);
        } catch (UnsupportedEncodingException e2) {
            throw new IllegalStateException("UTF-8 encoding unavailable");
        }
    }

    private void parsePathAndQuery() throws MalformedURLException, UnsupportedEncodingException {
        if (this.path.equals("")) {
            return;
        }
        this.DN = this.path.startsWith("/") ? this.path.substring(1) : this.path;
        if (this.DN.length() > 0) {
            this.DN = UrlUtil.decode(this.DN, InternalZipConstants.CHARSET_UTF8);
        }
        if (this.query == null || this.query.length() < 2) {
            return;
        }
        int iIndexOf = this.query.indexOf(63, 1);
        int length = iIndexOf == -1 ? this.query.length() : iIndexOf;
        if (length - 1 > 0) {
            this.attributes = this.query.substring(1, length);
        }
        int i2 = length + 1;
        if (i2 >= this.query.length()) {
            return;
        }
        int iIndexOf2 = this.query.indexOf(63, i2);
        int length2 = iIndexOf2 == -1 ? this.query.length() : iIndexOf2;
        if (length2 - i2 > 0) {
            this.scope = this.query.substring(i2, length2);
        }
        int i3 = length2 + 1;
        if (i3 >= this.query.length()) {
            return;
        }
        int iIndexOf3 = this.query.indexOf(63, i3);
        int length3 = iIndexOf3 == -1 ? this.query.length() : iIndexOf3;
        if (length3 - i3 > 0) {
            this.filter = this.query.substring(i3, length3);
            this.filter = UrlUtil.decode(this.filter, InternalZipConstants.CHARSET_UTF8);
        }
        int i4 = length3 + 1;
        if (i4 < this.query.length() && this.query.length() - i4 > 0) {
            this.extensions = this.query.substring(i4);
            this.extensions = UrlUtil.decode(this.extensions, InternalZipConstants.CHARSET_UTF8);
        }
    }
}
