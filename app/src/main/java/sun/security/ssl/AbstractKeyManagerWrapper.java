package sun.security.ssl;

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509KeyManager;

/* compiled from: SSLContextImpl.java */
/* loaded from: jsse.jar:sun/security/ssl/AbstractKeyManagerWrapper.class */
final class AbstractKeyManagerWrapper extends X509ExtendedKeyManager {
    private final X509KeyManager km;

    AbstractKeyManagerWrapper(X509KeyManager x509KeyManager) {
        this.km = x509KeyManager;
    }

    @Override // javax.net.ssl.X509KeyManager
    public String[] getClientAliases(String str, Principal[] principalArr) {
        return this.km.getClientAliases(str, principalArr);
    }

    @Override // javax.net.ssl.X509KeyManager
    public String chooseClientAlias(String[] strArr, Principal[] principalArr, Socket socket) {
        return this.km.chooseClientAlias(strArr, principalArr, socket);
    }

    @Override // javax.net.ssl.X509KeyManager
    public String[] getServerAliases(String str, Principal[] principalArr) {
        return this.km.getServerAliases(str, principalArr);
    }

    @Override // javax.net.ssl.X509KeyManager
    public String chooseServerAlias(String str, Principal[] principalArr, Socket socket) {
        return this.km.chooseServerAlias(str, principalArr, socket);
    }

    @Override // javax.net.ssl.X509KeyManager
    public X509Certificate[] getCertificateChain(String str) {
        return this.km.getCertificateChain(str);
    }

    @Override // javax.net.ssl.X509KeyManager
    public PrivateKey getPrivateKey(String str) {
        return this.km.getPrivateKey(str);
    }
}
