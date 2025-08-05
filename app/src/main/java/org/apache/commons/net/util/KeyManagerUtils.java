package org.apache.commons.net.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import javax.net.ssl.KeyManager;
import javax.net.ssl.X509ExtendedKeyManager;
import org.apache.commons.net.io.Util;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/util/KeyManagerUtils.class */
public final class KeyManagerUtils {
    private static final String DEFAULT_STORE_TYPE = KeyStore.getDefaultType();

    private KeyManagerUtils() {
    }

    public static KeyManager createClientKeyManager(KeyStore ks, String keyAlias, String keyPass) throws GeneralSecurityException {
        ClientKeyStore cks = new ClientKeyStore(ks, keyAlias != null ? keyAlias : findAlias(ks), keyPass);
        return new X509KeyManager(cks);
    }

    public static KeyManager createClientKeyManager(String storeType, File storePath, String storePass, String keyAlias, String keyPass) throws GeneralSecurityException, IOException {
        KeyStore ks = loadStore(storeType, storePath, storePass);
        return createClientKeyManager(ks, keyAlias, keyPass);
    }

    public static KeyManager createClientKeyManager(File storePath, String storePass, String keyAlias) throws GeneralSecurityException, IOException {
        return createClientKeyManager(DEFAULT_STORE_TYPE, storePath, storePass, keyAlias, storePass);
    }

    public static KeyManager createClientKeyManager(File storePath, String storePass) throws GeneralSecurityException, IOException {
        return createClientKeyManager(DEFAULT_STORE_TYPE, storePath, storePass, null, storePass);
    }

    private static KeyStore loadStore(String storeType, File storePath, String storePass) throws GeneralSecurityException, IOException {
        KeyStore ks = KeyStore.getInstance(storeType);
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(storePath);
            ks.load(stream, storePass.toCharArray());
            Util.closeQuietly(stream);
            return ks;
        } catch (Throwable th) {
            Util.closeQuietly(stream);
            throw th;
        }
    }

    private static String findAlias(KeyStore ks) throws KeyStoreException {
        Enumeration<String> e2 = ks.aliases();
        while (e2.hasMoreElements()) {
            String entry = e2.nextElement2();
            if (ks.isKeyEntry(entry)) {
                return entry;
            }
        }
        throw new KeyStoreException("Cannot find a private key entry");
    }

    /* loaded from: commons-net-3.6.jar:org/apache/commons/net/util/KeyManagerUtils$ClientKeyStore.class */
    private static class ClientKeyStore {
        private final X509Certificate[] certChain;
        private final PrivateKey key;
        private final String keyAlias;

        ClientKeyStore(KeyStore ks, String keyAlias, String keyPass) throws GeneralSecurityException {
            this.keyAlias = keyAlias;
            this.key = (PrivateKey) ks.getKey(this.keyAlias, keyPass.toCharArray());
            Certificate[] certs = ks.getCertificateChain(this.keyAlias);
            X509Certificate[] X509certs = new X509Certificate[certs.length];
            for (int i2 = 0; i2 < certs.length; i2++) {
                X509certs[i2] = (X509Certificate) certs[i2];
            }
            this.certChain = X509certs;
        }

        final X509Certificate[] getCertificateChain() {
            return this.certChain;
        }

        final PrivateKey getPrivateKey() {
            return this.key;
        }

        final String getAlias() {
            return this.keyAlias;
        }
    }

    /* loaded from: commons-net-3.6.jar:org/apache/commons/net/util/KeyManagerUtils$X509KeyManager.class */
    private static class X509KeyManager extends X509ExtendedKeyManager {
        private final ClientKeyStore keyStore;

        X509KeyManager(ClientKeyStore keyStore) {
            this.keyStore = keyStore;
        }

        @Override // javax.net.ssl.X509KeyManager
        public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
            return this.keyStore.getAlias();
        }

        @Override // javax.net.ssl.X509KeyManager
        public X509Certificate[] getCertificateChain(String alias) {
            return this.keyStore.getCertificateChain();
        }

        @Override // javax.net.ssl.X509KeyManager
        public String[] getClientAliases(String keyType, Principal[] issuers) {
            return new String[]{this.keyStore.getAlias()};
        }

        @Override // javax.net.ssl.X509KeyManager
        public PrivateKey getPrivateKey(String alias) {
            return this.keyStore.getPrivateKey();
        }

        @Override // javax.net.ssl.X509KeyManager
        public String[] getServerAliases(String keyType, Principal[] issuers) {
            return null;
        }

        @Override // javax.net.ssl.X509KeyManager
        public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
            return null;
        }
    }
}
