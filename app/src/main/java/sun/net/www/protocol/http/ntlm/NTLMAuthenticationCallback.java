package sun.net.www.protocol.http.ntlm;

import java.net.URL;

/* loaded from: rt.jar:sun/net/www/protocol/http/ntlm/NTLMAuthenticationCallback.class */
public abstract class NTLMAuthenticationCallback {
    private static volatile NTLMAuthenticationCallback callback;

    public abstract boolean isTrustedSite(URL url);

    public static void setNTLMAuthenticationCallback(NTLMAuthenticationCallback nTLMAuthenticationCallback) {
        callback = nTLMAuthenticationCallback;
    }

    public static NTLMAuthenticationCallback getNTLMAuthenticationCallback() {
        return callback;
    }
}
