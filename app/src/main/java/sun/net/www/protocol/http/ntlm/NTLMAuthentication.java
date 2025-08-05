package sun.net.www.protocol.http.ntlm;

import java.io.IOException;
import java.net.InetAddress;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.net.NetProperties;
import sun.net.www.HeaderParser;
import sun.net.www.protocol.http.AuthScheme;
import sun.net.www.protocol.http.AuthenticationInfo;
import sun.net.www.protocol.http.HttpURLConnection;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/net/www/protocol/http/ntlm/NTLMAuthentication.class */
public class NTLMAuthentication extends AuthenticationInfo {
    private static final long serialVersionUID = 100;
    private String hostname;
    private static final TransparentAuth authMode;
    String username;
    String ntdomain;
    String password;
    private static final boolean isTrustedSiteAvailable;
    private static final NTLMAuthenticationCallback NTLMAuthCallback = NTLMAuthenticationCallback.getNTLMAuthenticationCallback();
    private static String defaultDomain = (String) AccessController.doPrivileged(new GetPropertyAction("http.auth.ntlm.domain", "domain"));
    private static final boolean ntlmCache = Boolean.parseBoolean((String) AccessController.doPrivileged(new GetPropertyAction("jdk.ntlm.cache", "true")));

    /* loaded from: rt.jar:sun/net/www/protocol/http/ntlm/NTLMAuthentication$TransparentAuth.class */
    enum TransparentAuth {
        DISABLED,
        TRUSTED_HOSTS,
        ALL_HOSTS
    }

    private static native boolean isTrustedSiteAvailable();

    private static native boolean isTrustedSite0(String str);

    static {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.net.www.protocol.http.ntlm.NTLMAuthentication.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return NetProperties.get("jdk.http.ntlm.transparentAuth");
            }
        });
        if ("trustedHosts".equalsIgnoreCase(str)) {
            authMode = TransparentAuth.TRUSTED_HOSTS;
        } else if ("allHosts".equalsIgnoreCase(str)) {
            authMode = TransparentAuth.ALL_HOSTS;
        } else {
            authMode = TransparentAuth.DISABLED;
        }
        isTrustedSiteAvailable = isTrustedSiteAvailable();
    }

    private void init0() {
        this.hostname = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.net.www.protocol.http.ntlm.NTLMAuthentication.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                String upperCase;
                try {
                    upperCase = InetAddress.getLocalHost().getHostName().toUpperCase();
                } catch (UnknownHostException e2) {
                    upperCase = "localhost";
                }
                return upperCase;
            }
        });
        int iIndexOf = this.hostname.indexOf(46);
        if (iIndexOf != -1) {
            this.hostname = this.hostname.substring(0, iIndexOf);
        }
    }

    public NTLMAuthentication(boolean z2, URL url, PasswordAuthentication passwordAuthentication) {
        super(z2 ? 'p' : 's', AuthScheme.NTLM, url, "");
        init(passwordAuthentication);
    }

    private void init(PasswordAuthentication passwordAuthentication) {
        this.pw = passwordAuthentication;
        if (passwordAuthentication != null) {
            String userName = passwordAuthentication.getUserName();
            int iIndexOf = userName.indexOf(92);
            if (iIndexOf == -1) {
                this.username = userName;
                this.ntdomain = defaultDomain;
            } else {
                this.ntdomain = userName.substring(0, iIndexOf).toUpperCase();
                this.username = userName.substring(iIndexOf + 1);
            }
            this.password = new String(passwordAuthentication.getPassword());
        } else {
            this.username = null;
            this.ntdomain = null;
            this.password = null;
        }
        init0();
    }

    public NTLMAuthentication(boolean z2, String str, int i2, PasswordAuthentication passwordAuthentication) {
        super(z2 ? 'p' : 's', AuthScheme.NTLM, str, i2, "");
        init(passwordAuthentication);
    }

    @Override // sun.net.www.protocol.http.AuthenticationInfo
    protected boolean useAuthCache() {
        return ntlmCache && super.useAuthCache();
    }

    @Override // sun.net.www.protocol.http.AuthenticationInfo
    public boolean supportsPreemptiveAuthorization() {
        return false;
    }

    public static boolean supportsTransparentAuth() {
        return true;
    }

    public static boolean isTrustedSite(URL url) {
        if (NTLMAuthCallback != null) {
            return NTLMAuthCallback.isTrustedSite(url);
        }
        switch (authMode) {
            case TRUSTED_HOSTS:
                return isTrustedSite(url.toString());
            case ALL_HOSTS:
                return true;
            default:
                return false;
        }
    }

    private static boolean isTrustedSite(String str) {
        if (isTrustedSiteAvailable) {
            return isTrustedSite0(str);
        }
        return false;
    }

    @Override // sun.net.www.protocol.http.AuthenticationInfo
    public String getHeaderValue(URL url, String str) {
        throw new RuntimeException("getHeaderValue not supported");
    }

    @Override // sun.net.www.protocol.http.AuthenticationInfo
    public boolean isAuthorizationStale(String str) {
        return false;
    }

    @Override // sun.net.www.protocol.http.AuthenticationInfo
    public synchronized boolean setHeaders(HttpURLConnection httpURLConnection, HeaderParser headerParser, String str) {
        try {
            NTLMAuthSequence nTLMAuthSequence = (NTLMAuthSequence) httpURLConnection.authObj();
            if (nTLMAuthSequence == null) {
                nTLMAuthSequence = new NTLMAuthSequence(this.username, this.password, this.ntdomain);
                httpURLConnection.authObj(nTLMAuthSequence);
            }
            httpURLConnection.setAuthenticationProperty(getHeaderName(), "NTLM " + nTLMAuthSequence.getAuthHeader(str.length() > 6 ? str.substring(5) : null));
            if (nTLMAuthSequence.isComplete()) {
                httpURLConnection.authObj(null);
                return true;
            }
            return true;
        } catch (IOException e2) {
            httpURLConnection.authObj(null);
            return false;
        }
    }
}
