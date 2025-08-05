package sun.security.mscapi;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.SecurityPermission;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.apache.commons.math3.geometry.VectorFormat;
import org.slf4j.Logger;
import sun.security.util.Debug;

/* loaded from: sunmscapi.jar:sun/security/mscapi/CKeyStore.class */
abstract class CKeyStore extends KeyStoreSpi {
    private static final String KEYSTORE_COMPATIBILITY_MODE_PROP = "sun.security.mscapi.keyStoreCompatibilityMode";
    private final boolean keyStoreCompatibilityMode;
    private static final Debug debug = Debug.getInstance("keystore");
    private final String storeName;
    private CertificateFactory certificateFactory = null;
    private Map<String, KeyEntry> entries = new HashMap();

    private native void loadKeysOrCertificateChains(String str) throws KeyStoreException;

    /* JADX INFO: Access modifiers changed from: private */
    public native void storeCertificate(String str, String str2, byte[] bArr, int i2, long j2, long j3) throws CertificateException, KeyStoreException;

    private native void removeCertificate(String str, String str2, byte[] bArr, int i2) throws CertificateException, KeyStoreException;

    private native void destroyKeyContainer(String str) throws KeyStoreException;

    /* JADX INFO: Access modifiers changed from: private */
    public native byte[] generateRSAPrivateKeyBlob(int i2, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5, byte[] bArr6, byte[] bArr7, byte[] bArr8) throws InvalidKeyException;

    /* JADX INFO: Access modifiers changed from: private */
    public native CPrivateKey storePrivateKey(String str, byte[] bArr, String str2, int i2) throws KeyStoreException;

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CKeyStore$MY.class */
    public static final class MY extends CKeyStore {
        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineLoad(InputStream inputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
            super.engineLoad(inputStream, cArr);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineStore(OutputStream outputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
            super.engineStore(outputStream, cArr);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ String engineGetCertificateAlias(Certificate certificate) {
            return super.engineGetCertificateAlias(certificate);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ boolean engineIsCertificateEntry(String str) {
            return super.engineIsCertificateEntry(str);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ boolean engineIsKeyEntry(String str) {
            return super.engineIsKeyEntry(str);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ int engineSize() {
            return super.engineSize();
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ boolean engineContainsAlias(String str) {
            return super.engineContainsAlias(str);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Enumeration engineAliases() {
            return super.engineAliases();
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineDeleteEntry(String str) throws KeyStoreException {
            super.engineDeleteEntry(str);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineSetCertificateEntry(String str, Certificate certificate) throws KeyStoreException {
            super.engineSetCertificateEntry(str, certificate);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineSetKeyEntry(String str, byte[] bArr, Certificate[] certificateArr) throws KeyStoreException {
            super.engineSetKeyEntry(str, bArr, certificateArr);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineSetKeyEntry(String str, Key key, char[] cArr, Certificate[] certificateArr) throws KeyStoreException {
            super.engineSetKeyEntry(str, key, cArr, certificateArr);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Date engineGetCreationDate(String str) {
            return super.engineGetCreationDate(str);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Certificate engineGetCertificate(String str) {
            return super.engineGetCertificate(str);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Certificate[] engineGetCertificateChain(String str) {
            return super.engineGetCertificateChain(str);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Key engineGetKey(String str, char[] cArr) throws NoSuchAlgorithmException, UnrecoverableKeyException {
            return super.engineGetKey(str, cArr);
        }

        public MY() {
            super("MY");
        }
    }

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CKeyStore$ROOT.class */
    public static final class ROOT extends CKeyStore {
        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineLoad(InputStream inputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
            super.engineLoad(inputStream, cArr);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineStore(OutputStream outputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
            super.engineStore(outputStream, cArr);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ String engineGetCertificateAlias(Certificate certificate) {
            return super.engineGetCertificateAlias(certificate);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ boolean engineIsCertificateEntry(String str) {
            return super.engineIsCertificateEntry(str);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ boolean engineIsKeyEntry(String str) {
            return super.engineIsKeyEntry(str);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ int engineSize() {
            return super.engineSize();
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ boolean engineContainsAlias(String str) {
            return super.engineContainsAlias(str);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Enumeration engineAliases() {
            return super.engineAliases();
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineDeleteEntry(String str) throws KeyStoreException {
            super.engineDeleteEntry(str);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineSetCertificateEntry(String str, Certificate certificate) throws KeyStoreException {
            super.engineSetCertificateEntry(str, certificate);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineSetKeyEntry(String str, byte[] bArr, Certificate[] certificateArr) throws KeyStoreException {
            super.engineSetKeyEntry(str, bArr, certificateArr);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineSetKeyEntry(String str, Key key, char[] cArr, Certificate[] certificateArr) throws KeyStoreException {
            super.engineSetKeyEntry(str, key, cArr, certificateArr);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Date engineGetCreationDate(String str) {
            return super.engineGetCreationDate(str);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Certificate engineGetCertificate(String str) {
            return super.engineGetCertificate(str);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Certificate[] engineGetCertificateChain(String str) {
            return super.engineGetCertificateChain(str);
        }

        @Override // sun.security.mscapi.CKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Key engineGetKey(String str, char[] cArr) throws NoSuchAlgorithmException, UnrecoverableKeyException {
            return super.engineGetKey(str, cArr);
        }

        public ROOT() {
            super(Logger.ROOT_LOGGER_NAME);
        }
    }

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CKeyStore$KeyEntry.class */
    class KeyEntry {
        private CKey privateKey;
        private X509Certificate[] certChain;
        private String alias;

        KeyEntry(CKeyStore cKeyStore, CKey cKey, X509Certificate[] x509CertificateArr) {
            this(null, cKey, x509CertificateArr);
        }

        KeyEntry(String str, CKey cKey, X509Certificate[] x509CertificateArr) {
            this.privateKey = cKey;
            this.certChain = x509CertificateArr;
            if (str == null) {
                this.alias = Integer.toString(x509CertificateArr[0].hashCode());
            } else {
                this.alias = str;
            }
        }

        String getAlias() {
            return this.alias;
        }

        void setAlias(String str) {
            this.alias = str;
        }

        CKey getPrivateKey() {
            return this.privateKey;
        }

        void setRSAPrivateKey(Key key) throws InvalidKeyException, KeyStoreException {
            RSAPrivateCrtKey rSAPrivateCrtKey = (RSAPrivateCrtKey) key;
            byte[] byteArray = rSAPrivateCrtKey.getModulus().toByteArray();
            int length = byteArray[0] == 0 ? (byteArray.length - 1) * 8 : byteArray.length * 8;
            this.privateKey = CKeyStore.this.storePrivateKey("RSA", (byte[]) Objects.requireNonNull(CKeyStore.this.generateRSAPrivateKeyBlob(length, byteArray, rSAPrivateCrtKey.getPublicExponent().toByteArray(), rSAPrivateCrtKey.getPrivateExponent().toByteArray(), rSAPrivateCrtKey.getPrimeP().toByteArray(), rSAPrivateCrtKey.getPrimeQ().toByteArray(), rSAPrivateCrtKey.getPrimeExponentP().toByteArray(), rSAPrivateCrtKey.getPrimeExponentQ().toByteArray(), rSAPrivateCrtKey.getCrtCoefficient().toByteArray())), VectorFormat.DEFAULT_PREFIX + UUID.randomUUID().toString() + "}", length);
        }

        X509Certificate[] getCertificateChain() {
            return this.certChain;
        }

        void setCertificateChain(X509Certificate[] x509CertificateArr) throws CertificateException, KeyStoreException {
            for (int i2 = 0; i2 < x509CertificateArr.length; i2++) {
                byte[] encoded = x509CertificateArr[i2].getEncoded();
                if (i2 != 0 || this.privateKey == null) {
                    CKeyStore.this.storeCertificate(CKeyStore.this.getName(), this.alias, encoded, encoded.length, 0L, 0L);
                } else {
                    CKeyStore.this.storeCertificate(CKeyStore.this.getName(), this.alias, encoded, encoded.length, this.privateKey.getHCryptProvider(), this.privateKey.getHCryptKey());
                }
            }
            this.certChain = x509CertificateArr;
        }
    }

    CKeyStore(String str) {
        if ("false".equalsIgnoreCase((String) AccessController.doPrivileged(() -> {
            return System.getProperty(KEYSTORE_COMPATIBILITY_MODE_PROP);
        }))) {
            this.keyStoreCompatibilityMode = false;
        } else {
            this.keyStoreCompatibilityMode = true;
        }
        this.storeName = str;
    }

    @Override // java.security.KeyStoreSpi
    public Key engineGetKey(String str, char[] cArr) throws UnrecoverableKeyException, NoSuchAlgorithmException {
        KeyEntry keyEntry;
        if (str == null) {
            return null;
        }
        if (cArr != null && !this.keyStoreCompatibilityMode) {
            throw new UnrecoverableKeyException("Password must be null");
        }
        if (engineIsKeyEntry(str) && (keyEntry = this.entries.get(str)) != null) {
            return keyEntry.getPrivateKey();
        }
        return null;
    }

    @Override // java.security.KeyStoreSpi
    public Certificate[] engineGetCertificateChain(String str) {
        if (str == null) {
            return null;
        }
        KeyEntry keyEntry = this.entries.get(str);
        X509Certificate[] certificateChain = keyEntry == null ? null : keyEntry.getCertificateChain();
        if (certificateChain == null) {
            return null;
        }
        return (Certificate[]) certificateChain.clone();
    }

    @Override // java.security.KeyStoreSpi
    public Certificate engineGetCertificate(String str) {
        if (str == null) {
            return null;
        }
        KeyEntry keyEntry = this.entries.get(str);
        X509Certificate[] certificateChain = keyEntry == null ? null : keyEntry.getCertificateChain();
        if (certificateChain == null || certificateChain.length == 0) {
            return null;
        }
        return certificateChain[0];
    }

    @Override // java.security.KeyStoreSpi
    public Date engineGetCreationDate(String str) {
        if (str == null) {
            return null;
        }
        return new Date();
    }

    @Override // java.security.KeyStoreSpi
    public void engineSetKeyEntry(String str, Key key, char[] cArr, Certificate[] certificateArr) throws KeyStoreException {
        X509Certificate[] x509CertificateArr;
        if (str == null) {
            throw new KeyStoreException("alias must not be null");
        }
        if (cArr != null && !this.keyStoreCompatibilityMode) {
            throw new KeyStoreException("Password must be null");
        }
        if (key instanceof RSAPrivateCrtKey) {
            KeyEntry keyEntry = this.entries.get(str);
            if (certificateArr != null) {
                if (certificateArr instanceof X509Certificate[]) {
                    x509CertificateArr = (X509Certificate[]) certificateArr;
                } else {
                    x509CertificateArr = new X509Certificate[certificateArr.length];
                    System.arraycopy(certificateArr, 0, x509CertificateArr, 0, certificateArr.length);
                }
            } else {
                x509CertificateArr = null;
            }
            if (keyEntry == null) {
                keyEntry = new KeyEntry(str, null, x509CertificateArr);
                storeWithUniqueAlias(str, keyEntry);
            }
            keyEntry.setAlias(str);
            try {
                keyEntry.setRSAPrivateKey(key);
                keyEntry.setCertificateChain(x509CertificateArr);
                return;
            } catch (InvalidKeyException e2) {
                throw new KeyStoreException(e2);
            } catch (CertificateException e3) {
                throw new KeyStoreException(e3);
            }
        }
        throw new UnsupportedOperationException("Cannot assign the key to the given alias.");
    }

    @Override // java.security.KeyStoreSpi
    public void engineSetKeyEntry(String str, byte[] bArr, Certificate[] certificateArr) throws KeyStoreException {
        throw new UnsupportedOperationException("Cannot assign the encoded key to the given alias.");
    }

    @Override // java.security.KeyStoreSpi
    public void engineSetCertificateEntry(String str, Certificate certificate) throws KeyStoreException {
        if (str == null) {
            throw new KeyStoreException("alias must not be null");
        }
        if (certificate instanceof X509Certificate) {
            X509Certificate[] x509CertificateArr = {(X509Certificate) certificate};
            KeyEntry keyEntry = this.entries.get(str);
            if (keyEntry == null) {
                keyEntry = new KeyEntry(str, null, x509CertificateArr);
                storeWithUniqueAlias(str, keyEntry);
            }
            if (keyEntry.getPrivateKey() == null) {
                keyEntry.setAlias(str);
                try {
                    keyEntry.setCertificateChain(x509CertificateArr);
                    return;
                } catch (CertificateException e2) {
                    throw new KeyStoreException(e2);
                }
            }
            return;
        }
        throw new UnsupportedOperationException("Cannot assign the certificate to the given alias.");
    }

    @Override // java.security.KeyStoreSpi
    public void engineDeleteEntry(String str) throws KeyStoreException {
        if (str == null) {
            throw new KeyStoreException("alias must not be null");
        }
        KeyEntry keyEntryRemove = this.entries.remove(str);
        if (keyEntryRemove != null) {
            X509Certificate[] certificateChain = keyEntryRemove.getCertificateChain();
            if (certificateChain != null && certificateChain.length > 0) {
                try {
                    byte[] encoded = certificateChain[0].getEncoded();
                    removeCertificate(getName(), keyEntryRemove.getAlias(), encoded, encoded.length);
                } catch (CertificateException e2) {
                    throw new KeyStoreException("Cannot remove entry: ", e2);
                }
            }
            CKey privateKey = keyEntryRemove.getPrivateKey();
            if (privateKey != null) {
                destroyKeyContainer(CKey.getContainerName(privateKey.getHCryptProvider()));
            }
        }
    }

    @Override // java.security.KeyStoreSpi
    public Enumeration<String> engineAliases() {
        final Iterator<String> it = this.entries.keySet().iterator();
        return new Enumeration<String>() { // from class: sun.security.mscapi.CKeyStore.1
            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                return it.hasNext();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Enumeration
            /* renamed from: nextElement */
            public String nextElement2() {
                return (String) it.next();
            }
        };
    }

    @Override // java.security.KeyStoreSpi
    public boolean engineContainsAlias(String str) {
        return this.entries.containsKey(str);
    }

    @Override // java.security.KeyStoreSpi
    public int engineSize() {
        return this.entries.size();
    }

    @Override // java.security.KeyStoreSpi
    public boolean engineIsKeyEntry(String str) {
        KeyEntry keyEntry;
        return (str == null || (keyEntry = this.entries.get(str)) == null || keyEntry.getPrivateKey() == null) ? false : true;
    }

    @Override // java.security.KeyStoreSpi
    public boolean engineIsCertificateEntry(String str) {
        KeyEntry keyEntry;
        return (str == null || (keyEntry = this.entries.get(str)) == null || keyEntry.getPrivateKey() != null) ? false : true;
    }

    @Override // java.security.KeyStoreSpi
    public String engineGetCertificateAlias(Certificate certificate) {
        Iterator<Map.Entry<String, KeyEntry>> it = this.entries.entrySet().iterator();
        while (it.hasNext()) {
            KeyEntry value = it.next().getValue();
            if (value.certChain != null && value.certChain.length > 0 && value.certChain[0].equals(certificate)) {
                return value.getAlias();
            }
        }
        return null;
    }

    @Override // java.security.KeyStoreSpi
    public void engineStore(OutputStream outputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
        if (outputStream != null && !this.keyStoreCompatibilityMode) {
            throw new IOException("Keystore output stream must be null");
        }
        if (cArr != null && !this.keyStoreCompatibilityMode) {
            throw new IOException("Keystore password must be null");
        }
    }

    @Override // java.security.KeyStoreSpi
    public void engineLoad(InputStream inputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
        if (inputStream != null && !this.keyStoreCompatibilityMode) {
            throw new IOException("Keystore input stream must be null");
        }
        if (cArr != null && !this.keyStoreCompatibilityMode) {
            throw new IOException("Keystore password must be null");
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new SecurityPermission("authProvider.SunMSCAPI"));
        }
        this.entries.clear();
        try {
            loadKeysOrCertificateChains(getName());
            if (debug != null) {
                debug.println("MSCAPI keystore load: entry count: " + this.entries.size());
            }
        } catch (KeyStoreException e2) {
            throw new IOException(e2);
        }
    }

    private void storeWithUniqueAlias(String str, KeyEntry keyEntry) {
        String str2 = str;
        int i2 = 1;
        while (this.entries.putIfAbsent(str2, keyEntry) != null) {
            int i3 = i2;
            i2++;
            str2 = str + " (" + i3 + ")";
        }
    }

    private void generateCertificateChain(String str, Collection<? extends Certificate> collection) {
        try {
            X509Certificate[] x509CertificateArr = new X509Certificate[collection.size()];
            int i2 = 0;
            Iterator<? extends Certificate> it = collection.iterator();
            while (it.hasNext()) {
                x509CertificateArr[i2] = (X509Certificate) it.next();
                i2++;
            }
            storeWithUniqueAlias(str, new KeyEntry(str, null, x509CertificateArr));
        } catch (Throwable th) {
        }
    }

    private void generateKeyAndCertificateChain(boolean z2, String str, long j2, long j3, int i2, Collection<? extends Certificate> collection) {
        try {
            X509Certificate[] x509CertificateArr = new X509Certificate[collection.size()];
            int i3 = 0;
            Iterator<? extends Certificate> it = collection.iterator();
            while (it.hasNext()) {
                x509CertificateArr[i3] = (X509Certificate) it.next();
                i3++;
            }
            storeWithUniqueAlias(str, new KeyEntry(str, CPrivateKey.of(z2 ? "RSA" : "EC", j2, j3, i2), x509CertificateArr));
        } catch (Throwable th) {
        }
    }

    private void generateCertificate(byte[] bArr, Collection<Certificate> collection) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
            if (this.certificateFactory == null) {
                this.certificateFactory = CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID, "SUN");
            }
            collection.addAll(this.certificateFactory.generateCertificates(byteArrayInputStream));
        } catch (CertificateException e2) {
        } catch (Throwable th) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getName() {
        return this.storeName;
    }
}
