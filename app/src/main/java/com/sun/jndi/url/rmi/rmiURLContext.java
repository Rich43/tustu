package com.sun.jndi.url.rmi;

import com.sun.jndi.rmi.registry.RegistryContext;
import com.sun.jndi.toolkit.url.GenericURLContext;
import com.sun.jndi.toolkit.url.Uri;
import java.net.URI;
import java.security.AccessController;
import java.util.Hashtable;
import java.util.Locale;
import javax.naming.CompositeName;
import javax.naming.InvalidNameException;
import javax.naming.NamingException;
import javax.naming.spi.ResolveResult;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:com/sun/jndi/url/rmi/rmiURLContext.class */
public class rmiURLContext extends GenericURLContext {
    private static final String PARSE_MODE_PROP = "com.sun.jndi.rmiURLParsing";
    private static final Uri.ParseMode DEFAULT_PARSE_MODE = Uri.ParseMode.COMPAT;
    public static final Uri.ParseMode PARSE_MODE;

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

    public rmiURLContext(Hashtable<?, ?> hashtable) {
        super(hashtable);
    }

    /* loaded from: rt.jar:com/sun/jndi/url/rmi/rmiURLContext$Parser.class */
    public static class Parser {
        final String url;
        final Uri.ParseMode mode;
        String host;
        int port;
        String objName;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !rmiURLContext.class.desiredAssertionStatus();
        }

        public Parser(String str) {
            this(str, rmiURLContext.PARSE_MODE);
        }

        public Parser(String str, Uri.ParseMode parseMode) {
            this.host = null;
            this.port = -1;
            this.objName = null;
            this.url = str;
            this.mode = parseMode;
        }

        public String url() {
            return this.url;
        }

        public String host() {
            return this.host;
        }

        public int port() {
            return this.port;
        }

        public String objName() {
            return this.objName;
        }

        public Uri.ParseMode mode() {
            return this.mode;
        }

        public void parse() throws NamingException {
            if (!this.url.startsWith("rmi:")) {
                throw new IllegalArgumentException("rmiURLContext: name is not an RMI URL: " + this.url);
            }
            switch (this.mode) {
                case STRICT:
                    parseStrict();
                    return;
                case COMPAT:
                    parseCompat();
                    return;
                case LEGACY:
                    parseLegacy();
                    return;
                default:
                    return;
            }
        }

        private void parseStrict() throws NamingException {
            if (!$assertionsDisabled && !this.url.startsWith("rmi:")) {
                throw new AssertionError();
            }
            if (this.url.equals("rmi:") || this.url.equals("rmi://")) {
                return;
            }
            int length = 4;
            if (this.url.startsWith("//", 4)) {
                int i2 = 4 + 2;
                try {
                    URI uriCreate = URI.create(this.url);
                    this.host = uriCreate.getHost();
                    this.port = uriCreate.getPort();
                    String rawAuthority = uriCreate.getRawAuthority();
                    String str = (this.host == null ? "" : this.host) + (this.port == -1 ? "" : CallSiteDescriptor.TOKEN_DELIMITER + this.port);
                    if (!str.equals(rawAuthority)) {
                        boolean z2 = true;
                        if (str.equals("") && rawAuthority.startsWith(CallSiteDescriptor.TOKEN_DELIMITER)) {
                            try {
                                this.port = Integer.parseInt(rawAuthority.substring(1));
                                z2 = false;
                            } catch (NumberFormatException e2) {
                                z2 = true;
                            }
                        }
                        if (z2) {
                            throw newNamingException(new IllegalArgumentException("invalid authority: " + rawAuthority));
                        }
                    }
                    length = i2 + rawAuthority.length();
                } catch (IllegalArgumentException e3) {
                    throw newNamingException(e3);
                }
            }
            if (this.url.indexOf(35, length) > -1 && !acceptsFragment()) {
                throw newNamingException(new IllegalArgumentException("URI fragments not supported: " + this.url));
            }
            if ("".equals(this.host)) {
                this.host = null;
            }
            if (this.url.startsWith("/", length)) {
                length++;
            }
            if (length < this.url.length()) {
                this.objName = this.url.substring(length);
            }
        }

        private void parseCompat() throws NamingException {
            int length;
            if (!$assertionsDisabled && !this.url.startsWith("rmi:")) {
                throw new AssertionError();
            }
            int i2 = 4;
            boolean zStartsWith = this.url.startsWith("//", 4);
            if (zStartsWith) {
                i2 = 4 + 2;
            }
            int iIndexOf = this.url.indexOf(47, i2);
            int iIndexOf2 = this.url.indexOf(63, i2);
            int iIndexOf3 = this.url.indexOf(35, i2);
            if (iIndexOf3 > -1 && iIndexOf2 > iIndexOf3) {
                iIndexOf2 = -1;
            }
            if (iIndexOf3 > -1 && iIndexOf > iIndexOf3) {
                iIndexOf = -1;
            }
            if (iIndexOf2 > -1 && iIndexOf > iIndexOf2) {
                iIndexOf = -1;
            }
            if (iIndexOf > -1) {
                length = iIndexOf;
            } else if (iIndexOf2 > -1) {
                length = iIndexOf2;
            } else {
                length = iIndexOf3 > -1 ? iIndexOf3 : this.url.length();
            }
            int i3 = length;
            if (iIndexOf3 > -1 && !acceptsFragment()) {
                throw newNamingException(new IllegalArgumentException("URI fragments not supported: " + this.url));
            }
            if (zStartsWith && i3 > i2) {
                if (this.url.startsWith(CallSiteDescriptor.TOKEN_DELIMITER, i2)) {
                    int i4 = i2 + 1;
                    this.host = "";
                    if (i3 > i4) {
                        this.port = Integer.parseInt(this.url.substring(i4, i3));
                    }
                } else {
                    try {
                        URI uriCreate = URI.create(this.url.substring(0, i3));
                        this.host = uriCreate.getHost();
                        this.port = uriCreate.getPort();
                        if (!((this.host == null ? "" : this.host) + (this.port == -1 ? "" : CallSiteDescriptor.TOKEN_DELIMITER + this.port)).equals(uriCreate.getRawAuthority())) {
                            throw newNamingException(new IllegalArgumentException("invalid authority: " + uriCreate.getRawAuthority()));
                        }
                    } catch (IllegalArgumentException e2) {
                        throw newNamingException(e2);
                    }
                }
                i2 = i3;
            }
            if ("".equals(this.host)) {
                this.host = null;
            }
            if (this.url.startsWith("/", i2)) {
                i2++;
            }
            if (i2 < this.url.length()) {
                this.objName = this.url.substring(i2);
            }
        }

        private void parseLegacy() {
            int i2;
            if (!$assertionsDisabled && !this.url.startsWith("rmi:")) {
                throw new AssertionError();
            }
            int i3 = 4;
            if (this.url.startsWith("//", 4)) {
                int i4 = 4 + 2;
                int iIndexOf = this.url.indexOf(47, i4);
                if (iIndexOf < 0) {
                    iIndexOf = this.url.length();
                }
                if (this.url.startsWith("[", i4)) {
                    int iIndexOf2 = this.url.indexOf(93, i4 + 1);
                    if (iIndexOf2 < 0 || iIndexOf2 > iIndexOf) {
                        throw new IllegalArgumentException("rmiURLContext: name is an Invalid URL: " + this.url);
                    }
                    this.host = this.url.substring(i4, iIndexOf2 + 1);
                    i2 = iIndexOf2 + 1;
                } else {
                    int iIndexOf3 = this.url.indexOf(58, i4);
                    int i5 = (iIndexOf3 < 0 || iIndexOf3 > iIndexOf) ? iIndexOf : iIndexOf3;
                    if (i4 < i5) {
                        this.host = this.url.substring(i4, i5);
                    }
                    i2 = i5;
                }
                if (i2 + 1 < iIndexOf) {
                    if (this.url.startsWith(CallSiteDescriptor.TOKEN_DELIMITER, i2)) {
                        this.port = Integer.parseInt(this.url.substring(i2 + 1, iIndexOf));
                    } else {
                        throw new IllegalArgumentException("rmiURLContext: name is an Invalid URL: " + this.url);
                    }
                }
                i3 = iIndexOf;
            }
            if ("".equals(this.host)) {
                this.host = null;
            }
            if (this.url.startsWith("/", i3)) {
                i3++;
            }
            if (i3 < this.url.length()) {
                this.objName = this.url.substring(i3);
            }
        }

        NamingException newNamingException(Throwable th) {
            InvalidNameException invalidNameException = new InvalidNameException(th.getMessage());
            invalidNameException.initCause(th);
            return invalidNameException;
        }

        protected boolean acceptsFragment() {
            return true;
        }
    }

    @Override // com.sun.jndi.toolkit.url.GenericURLContext
    protected ResolveResult getRootURLContext(String str, Hashtable<?, ?> hashtable) throws NamingException {
        Parser parser = new Parser(str);
        parser.parse();
        String str2 = parser.host;
        int i2 = parser.port;
        String str3 = parser.objName;
        CompositeName compositeName = new CompositeName();
        if (str3 != null) {
            compositeName.add(str3);
        }
        return new ResolveResult(new RegistryContext(str2, i2, hashtable), compositeName);
    }
}
