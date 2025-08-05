package com.sun.net.ssl;

import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/* compiled from: SSLSecurity.java */
/* loaded from: rt.jar:com/sun/net/ssl/X509KeyManagerComSunWrapper.class */
final class X509KeyManagerComSunWrapper implements X509KeyManager {
    private javax.net.ssl.X509KeyManager theX509KeyManager;

    X509KeyManagerComSunWrapper(javax.net.ssl.X509KeyManager x509KeyManager) {
        this.theX509KeyManager = x509KeyManager;
    }

    @Override // com.sun.net.ssl.X509KeyManager
    public String[] getClientAliases(String str, Principal[] principalArr) {
        return this.theX509KeyManager.getClientAliases(str, principalArr);
    }

    @Override // com.sun.net.ssl.X509KeyManager
    public String chooseClientAlias(String str, Principal[] principalArr) {
        return this.theX509KeyManager.chooseClientAlias(new String[]{str}, principalArr, null);
    }

    @Override // com.sun.net.ssl.X509KeyManager
    public String[] getServerAliases(String str, Principal[] principalArr) {
        return this.theX509KeyManager.getServerAliases(str, principalArr);
    }

    @Override // com.sun.net.ssl.X509KeyManager
    public String chooseServerAlias(String str, Principal[] principalArr) {
        return this.theX509KeyManager.chooseServerAlias(str, principalArr, null);
    }

    @Override // com.sun.net.ssl.X509KeyManager
    public X509Certificate[] getCertificateChain(String str) {
        return this.theX509KeyManager.getCertificateChain(str);
    }

    @Override // com.sun.net.ssl.X509KeyManager
    public PrivateKey getPrivateKey(String str) {
        return this.theX509KeyManager.getPrivateKey(str);
    }
}
