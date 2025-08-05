package javax.net.ssl;

import java.security.Principal;

/* loaded from: rt.jar:javax/net/ssl/X509ExtendedKeyManager.class */
public abstract class X509ExtendedKeyManager implements X509KeyManager {
    protected X509ExtendedKeyManager() {
    }

    public String chooseEngineClientAlias(String[] strArr, Principal[] principalArr, SSLEngine sSLEngine) {
        return null;
    }

    public String chooseEngineServerAlias(String str, Principal[] principalArr, SSLEngine sSLEngine) {
        return null;
    }
}
