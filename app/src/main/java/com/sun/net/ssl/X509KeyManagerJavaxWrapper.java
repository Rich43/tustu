package com.sun.net.ssl;

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLEngine;

/* compiled from: SSLSecurity.java */
/* loaded from: rt.jar:com/sun/net/ssl/X509KeyManagerJavaxWrapper.class */
final class X509KeyManagerJavaxWrapper implements javax.net.ssl.X509KeyManager {
    private X509KeyManager theX509KeyManager;

    X509KeyManagerJavaxWrapper(X509KeyManager x509KeyManager) {
        this.theX509KeyManager = x509KeyManager;
    }

    @Override // javax.net.ssl.X509KeyManager
    public String[] getClientAliases(String str, Principal[] principalArr) {
        return this.theX509KeyManager.getClientAliases(str, principalArr);
    }

    @Override // javax.net.ssl.X509KeyManager
    public String chooseClientAlias(String[] strArr, Principal[] principalArr, Socket socket) {
        if (strArr == null) {
            return null;
        }
        for (String str : strArr) {
            String strChooseClientAlias = this.theX509KeyManager.chooseClientAlias(str, principalArr);
            if (strChooseClientAlias != null) {
                return strChooseClientAlias;
            }
        }
        return null;
    }

    public String chooseEngineClientAlias(String[] strArr, Principal[] principalArr, SSLEngine sSLEngine) {
        if (strArr == null) {
            return null;
        }
        for (String str : strArr) {
            String strChooseClientAlias = this.theX509KeyManager.chooseClientAlias(str, principalArr);
            if (strChooseClientAlias != null) {
                return strChooseClientAlias;
            }
        }
        return null;
    }

    @Override // javax.net.ssl.X509KeyManager
    public String[] getServerAliases(String str, Principal[] principalArr) {
        return this.theX509KeyManager.getServerAliases(str, principalArr);
    }

    @Override // javax.net.ssl.X509KeyManager
    public String chooseServerAlias(String str, Principal[] principalArr, Socket socket) {
        if (str == null) {
            return null;
        }
        return this.theX509KeyManager.chooseServerAlias(str, principalArr);
    }

    public String chooseEngineServerAlias(String str, Principal[] principalArr, SSLEngine sSLEngine) {
        if (str == null) {
            return null;
        }
        return this.theX509KeyManager.chooseServerAlias(str, principalArr);
    }

    @Override // javax.net.ssl.X509KeyManager
    public X509Certificate[] getCertificateChain(String str) {
        return this.theX509KeyManager.getCertificateChain(str);
    }

    @Override // javax.net.ssl.X509KeyManager
    public PrivateKey getPrivateKey(String str) {
        return this.theX509KeyManager.getPrivateKey(str);
    }
}
