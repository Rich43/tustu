package javax.net.ssl;

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/* loaded from: rt.jar:javax/net/ssl/X509KeyManager.class */
public interface X509KeyManager extends KeyManager {
    String[] getClientAliases(String str, Principal[] principalArr);

    String chooseClientAlias(String[] strArr, Principal[] principalArr, Socket socket);

    String[] getServerAliases(String str, Principal[] principalArr);

    String chooseServerAlias(String str, Principal[] principalArr, Socket socket);

    X509Certificate[] getCertificateChain(String str);

    PrivateKey getPrivateKey(String str);
}
