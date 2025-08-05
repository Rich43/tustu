package com.sun.net.ssl;

import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

@Deprecated
/* loaded from: rt.jar:com/sun/net/ssl/X509KeyManager.class */
public interface X509KeyManager extends KeyManager {
    String[] getClientAliases(String str, Principal[] principalArr);

    String chooseClientAlias(String str, Principal[] principalArr);

    String[] getServerAliases(String str, Principal[] principalArr);

    String chooseServerAlias(String str, Principal[] principalArr);

    X509Certificate[] getCertificateChain(String str);

    PrivateKey getPrivateKey(String str);
}
