package sun.security.ssl;

import java.net.Socket;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.security.auth.x500.X500Principal;

/* loaded from: jsse.jar:sun/security/ssl/SunX509KeyManagerImpl.class */
final class SunX509KeyManagerImpl extends X509ExtendedKeyManager {
    private static final String[] STRING0 = new String[0];
    private Map<String, X509Credentials> credentialsMap = new HashMap();
    private final Map<String, String[]> serverAliasCache = Collections.synchronizedMap(new HashMap());

    /* loaded from: jsse.jar:sun/security/ssl/SunX509KeyManagerImpl$X509Credentials.class */
    private static class X509Credentials {
        PrivateKey privateKey;
        X509Certificate[] certificates;
        private Set<X500Principal> issuerX500Principals;

        X509Credentials(PrivateKey privateKey, X509Certificate[] x509CertificateArr) {
            this.privateKey = privateKey;
            this.certificates = x509CertificateArr;
        }

        synchronized Set<X500Principal> getIssuerX500Principals() {
            if (this.issuerX500Principals == null) {
                this.issuerX500Principals = new HashSet();
                for (int i2 = 0; i2 < this.certificates.length; i2++) {
                    this.issuerX500Principals.add(this.certificates[i2].getIssuerX500Principal());
                }
            }
            return this.issuerX500Principals;
        }
    }

    SunX509KeyManagerImpl(KeyStore keyStore, char[] cArr) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
        if (keyStore == null) {
            return;
        }
        Enumeration<String> enumerationAliases = keyStore.aliases();
        while (enumerationAliases.hasMoreElements()) {
            String strNextElement2 = enumerationAliases.nextElement2();
            if (keyStore.isKeyEntry(strNextElement2)) {
                Key key = keyStore.getKey(strNextElement2, cArr);
                if (key instanceof PrivateKey) {
                    Certificate[] certificateChain = keyStore.getCertificateChain(strNextElement2);
                    if (certificateChain != null && certificateChain.length != 0 && (certificateChain[0] instanceof X509Certificate)) {
                        if (!(certificateChain instanceof X509Certificate[])) {
                            X509Certificate[] x509CertificateArr = new X509Certificate[certificateChain.length];
                            System.arraycopy(certificateChain, 0, x509CertificateArr, 0, certificateChain.length);
                            certificateChain = x509CertificateArr;
                        }
                        this.credentialsMap.put(strNextElement2, new X509Credentials((PrivateKey) key, (X509Certificate[]) certificateChain));
                        if (SSLLogger.isOn && SSLLogger.isOn("keymanager")) {
                            SSLLogger.fine("found key for : " + strNextElement2, certificateChain);
                        }
                    }
                }
            }
        }
    }

    @Override // javax.net.ssl.X509KeyManager
    public X509Certificate[] getCertificateChain(String str) {
        X509Credentials x509Credentials;
        if (str == null || (x509Credentials = this.credentialsMap.get(str)) == null) {
            return null;
        }
        return (X509Certificate[]) x509Credentials.certificates.clone();
    }

    @Override // javax.net.ssl.X509KeyManager
    public PrivateKey getPrivateKey(String str) {
        X509Credentials x509Credentials;
        if (str == null || (x509Credentials = this.credentialsMap.get(str)) == null) {
            return null;
        }
        return x509Credentials.privateKey;
    }

    @Override // javax.net.ssl.X509KeyManager
    public String chooseClientAlias(String[] strArr, Principal[] principalArr, Socket socket) {
        if (strArr == null) {
            return null;
        }
        for (String str : strArr) {
            String[] clientAliases = getClientAliases(str, principalArr);
            if (clientAliases != null && clientAliases.length > 0) {
                return clientAliases[0];
            }
        }
        return null;
    }

    @Override // javax.net.ssl.X509ExtendedKeyManager
    public String chooseEngineClientAlias(String[] strArr, Principal[] principalArr, SSLEngine sSLEngine) {
        return chooseClientAlias(strArr, principalArr, null);
    }

    @Override // javax.net.ssl.X509KeyManager
    public String chooseServerAlias(String str, Principal[] principalArr, Socket socket) {
        String[] serverAliases;
        if (str == null) {
            return null;
        }
        if (principalArr == null || principalArr.length == 0) {
            serverAliases = this.serverAliasCache.get(str);
            if (serverAliases == null) {
                serverAliases = getServerAliases(str, principalArr);
                if (serverAliases == null) {
                    serverAliases = STRING0;
                }
                this.serverAliasCache.put(str, serverAliases);
            }
        } else {
            serverAliases = getServerAliases(str, principalArr);
        }
        if (serverAliases != null && serverAliases.length > 0) {
            return serverAliases[0];
        }
        return null;
    }

    @Override // javax.net.ssl.X509ExtendedKeyManager
    public String chooseEngineServerAlias(String str, Principal[] principalArr, SSLEngine sSLEngine) {
        return chooseServerAlias(str, principalArr, null);
    }

    @Override // javax.net.ssl.X509KeyManager
    public String[] getClientAliases(String str, Principal[] principalArr) {
        return getAliases(str, principalArr);
    }

    @Override // javax.net.ssl.X509KeyManager
    public String[] getServerAliases(String str, Principal[] principalArr) {
        return getAliases(str, principalArr);
    }

    private String[] getAliases(String str, Principal[] principalArr) {
        String strSubstring;
        if (str == null) {
            return null;
        }
        if (principalArr == null) {
            principalArr = new X500Principal[0];
        }
        if (!(principalArr instanceof X500Principal[])) {
            principalArr = convertPrincipals(principalArr);
        }
        if (str.contains("_")) {
            int iIndexOf = str.indexOf(95);
            strSubstring = str.substring(iIndexOf + 1);
            str = str.substring(0, iIndexOf);
        } else {
            strSubstring = null;
        }
        X500Principal[] x500PrincipalArr = (X500Principal[]) principalArr;
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<String, X509Credentials> entry : this.credentialsMap.entrySet()) {
            String key = entry.getKey();
            X509Credentials value = entry.getValue();
            X509Certificate[] x509CertificateArr = value.certificates;
            if (str.equals(x509CertificateArr[0].getPublicKey().getAlgorithm())) {
                if (strSubstring != null) {
                    if (x509CertificateArr.length > 1) {
                        if (!strSubstring.equals(x509CertificateArr[1].getPublicKey().getAlgorithm())) {
                        }
                    } else if (!x509CertificateArr[0].getSigAlgName().toUpperCase(Locale.ENGLISH).contains("WITH" + strSubstring.toUpperCase(Locale.ENGLISH))) {
                    }
                }
                if (principalArr.length == 0) {
                    arrayList.add(key);
                    if (SSLLogger.isOn && SSLLogger.isOn("keymanager")) {
                        SSLLogger.fine("matching alias: " + key, new Object[0]);
                    }
                } else {
                    Set<X500Principal> issuerX500Principals = value.getIssuerX500Principals();
                    int i2 = 0;
                    while (true) {
                        if (i2 >= x500PrincipalArr.length) {
                            break;
                        }
                        if (!issuerX500Principals.contains(principalArr[i2])) {
                            i2++;
                        } else {
                            arrayList.add(key);
                            if (SSLLogger.isOn && SSLLogger.isOn("keymanager")) {
                                SSLLogger.fine("matching alias: " + key, new Object[0]);
                            }
                        }
                    }
                }
            }
        }
        String[] strArr = (String[]) arrayList.toArray(STRING0);
        if (strArr.length == 0) {
            return null;
        }
        return strArr;
    }

    private static X500Principal[] convertPrincipals(Principal[] principalArr) {
        ArrayList arrayList = new ArrayList(principalArr.length);
        for (Principal principal : principalArr) {
            if (principal instanceof X500Principal) {
                arrayList.add((X500Principal) principal);
            } else {
                try {
                    arrayList.add(new X500Principal(principal.getName()));
                } catch (IllegalArgumentException e2) {
                }
            }
        }
        return (X500Principal[]) arrayList.toArray(new X500Principal[arrayList.size()]);
    }
}
