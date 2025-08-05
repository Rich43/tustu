package sun.net.www.protocol.http;

import java.io.IOException;
import java.net.Authenticator;
import java.net.URL;
import java.security.AccessController;
import java.util.Base64;
import java.util.HashMap;
import sun.net.www.HeaderParser;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/net/www/protocol/http/NegotiateAuthentication.class */
class NegotiateAuthentication extends AuthenticationInfo {
    private static final long serialVersionUID = 100;
    private final HttpCallerInfo hci;
    static HashMap<String, Boolean> supported = null;
    static ThreadLocal<HashMap<String, Negotiator>> cache = null;
    private static final boolean cacheSPNEGO = Boolean.parseBoolean((String) AccessController.doPrivileged(new GetPropertyAction("jdk.spnego.cache", "true")));
    private Negotiator negotiator;

    public NegotiateAuthentication(HttpCallerInfo httpCallerInfo) {
        super(Authenticator.RequestorType.PROXY == httpCallerInfo.authType ? 'p' : 's', httpCallerInfo.scheme.equalsIgnoreCase("Negotiate") ? AuthScheme.NEGOTIATE : AuthScheme.KERBEROS, httpCallerInfo.url, "");
        this.negotiator = null;
        this.hci = httpCallerInfo;
    }

    @Override // sun.net.www.protocol.http.AuthenticationInfo
    public boolean supportsPreemptiveAuthorization() {
        return false;
    }

    public static synchronized boolean isSupported(HttpCallerInfo httpCallerInfo) {
        if (supported == null) {
            supported = new HashMap<>();
        }
        String lowerCase = httpCallerInfo.host.toLowerCase();
        if (supported.containsKey(lowerCase)) {
            return supported.get(lowerCase).booleanValue();
        }
        Negotiator negotiator = Negotiator.getNegotiator(httpCallerInfo);
        if (negotiator != null) {
            supported.put(lowerCase, true);
            if (cache == null) {
                cache = new ThreadLocal<HashMap<String, Negotiator>>() { // from class: sun.net.www.protocol.http.NegotiateAuthentication.1
                    /* JADX INFO: Access modifiers changed from: protected */
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.lang.ThreadLocal
                    public HashMap<String, Negotiator> initialValue() {
                        return new HashMap<>();
                    }
                };
            }
            cache.get().put(lowerCase, negotiator);
            return true;
        }
        supported.put(lowerCase, false);
        return false;
    }

    private static synchronized HashMap<String, Negotiator> getCache() {
        if (cache == null) {
            return null;
        }
        return cache.get();
    }

    @Override // sun.net.www.protocol.http.AuthenticationInfo
    protected boolean useAuthCache() {
        return super.useAuthCache() && cacheSPNEGO;
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
            byte[] bArrDecode = null;
            String[] strArrSplit = str.split("\\s+");
            if (strArrSplit.length > 1) {
                bArrDecode = Base64.getDecoder().decode(strArrSplit[1]);
            }
            httpURLConnection.setAuthenticationProperty(getHeaderName(), this.hci.scheme + " " + Base64.getEncoder().encodeToString(bArrDecode == null ? firstToken() : nextToken(bArrDecode)));
            return true;
        } catch (IOException e2) {
            return false;
        }
    }

    private byte[] firstToken() throws IOException {
        this.negotiator = null;
        HashMap<String, Negotiator> cache2 = getCache();
        if (cache2 != null) {
            this.negotiator = cache2.get(getHost());
            if (this.negotiator != null) {
                cache2.remove(getHost());
            }
        }
        if (this.negotiator == null) {
            this.negotiator = Negotiator.getNegotiator(this.hci);
            if (this.negotiator == null) {
                throw new IOException("Cannot initialize Negotiator");
            }
        }
        return this.negotiator.firstToken();
    }

    private byte[] nextToken(byte[] bArr) throws IOException {
        return this.negotiator.nextToken(bArr);
    }
}
