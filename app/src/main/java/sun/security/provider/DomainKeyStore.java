package sun.security.provider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DomainLoadStoreParameter;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:sun/security/provider/DomainKeyStore.class */
abstract class DomainKeyStore extends KeyStoreSpi {
    private static final String ENTRY_NAME_SEPARATOR = "entrynameseparator";
    private static final String KEYSTORE_PROVIDER_NAME = "keystoreprovidername";
    private static final String KEYSTORE_TYPE = "keystoretype";
    private static final String KEYSTORE_URI = "keystoreuri";
    private static final String KEYSTORE_PASSWORD_ENV = "keystorepasswordenv";
    private static final String REGEX_META = ".$|()[{^?*+\\";
    private static final String DEFAULT_STREAM_PREFIX = "iostream";
    private static final String DEFAULT_KEYSTORE_TYPE = KeyStore.getDefaultType();
    private int streamCounter = 1;
    private String entryNameSeparator = " ";
    private String entryNameSeparatorRegEx = " ";
    private final Map<String, KeyStore> keystores = new HashMap();

    abstract String convertAlias(String str);

    /* loaded from: rt.jar:sun/security/provider/DomainKeyStore$DKS.class */
    public static final class DKS extends DomainKeyStore {
        @Override // sun.security.provider.DomainKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineLoad(KeyStore.LoadStoreParameter loadStoreParameter) throws NoSuchAlgorithmException, IOException, CertificateException {
            super.engineLoad(loadStoreParameter);
        }

        @Override // sun.security.provider.DomainKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineLoad(InputStream inputStream, char[] cArr) throws Exception {
            super.engineLoad(inputStream, cArr);
        }

        @Override // sun.security.provider.DomainKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineStore(KeyStore.LoadStoreParameter loadStoreParameter) throws NoSuchAlgorithmException, IOException, KeyStoreException, CertificateException {
            super.engineStore(loadStoreParameter);
        }

        @Override // sun.security.provider.DomainKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineStore(OutputStream outputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
            super.engineStore(outputStream, cArr);
        }

        @Override // sun.security.provider.DomainKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ String engineGetCertificateAlias(Certificate certificate) {
            return super.engineGetCertificateAlias(certificate);
        }

        @Override // sun.security.provider.DomainKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ boolean engineIsCertificateEntry(String str) {
            return super.engineIsCertificateEntry(str);
        }

        @Override // sun.security.provider.DomainKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ boolean engineIsKeyEntry(String str) {
            return super.engineIsKeyEntry(str);
        }

        @Override // sun.security.provider.DomainKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ int engineSize() {
            return super.engineSize();
        }

        @Override // sun.security.provider.DomainKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ boolean engineContainsAlias(String str) {
            return super.engineContainsAlias(str);
        }

        @Override // sun.security.provider.DomainKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Enumeration engineAliases() {
            return super.engineAliases();
        }

        @Override // sun.security.provider.DomainKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineDeleteEntry(String str) throws KeyStoreException {
            super.engineDeleteEntry(str);
        }

        @Override // sun.security.provider.DomainKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineSetCertificateEntry(String str, Certificate certificate) throws KeyStoreException {
            super.engineSetCertificateEntry(str, certificate);
        }

        @Override // sun.security.provider.DomainKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineSetKeyEntry(String str, byte[] bArr, Certificate[] certificateArr) throws KeyStoreException {
            super.engineSetKeyEntry(str, bArr, certificateArr);
        }

        @Override // sun.security.provider.DomainKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ void engineSetKeyEntry(String str, Key key, char[] cArr, Certificate[] certificateArr) throws KeyStoreException {
            super.engineSetKeyEntry(str, key, cArr, certificateArr);
        }

        @Override // sun.security.provider.DomainKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Date engineGetCreationDate(String str) {
            return super.engineGetCreationDate(str);
        }

        @Override // sun.security.provider.DomainKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Certificate engineGetCertificate(String str) {
            return super.engineGetCertificate(str);
        }

        @Override // sun.security.provider.DomainKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Certificate[] engineGetCertificateChain(String str) {
            return super.engineGetCertificateChain(str);
        }

        @Override // sun.security.provider.DomainKeyStore, java.security.KeyStoreSpi
        public /* bridge */ /* synthetic */ Key engineGetKey(String str, char[] cArr) throws NoSuchAlgorithmException, UnrecoverableKeyException {
            return super.engineGetKey(str, cArr);
        }

        @Override // sun.security.provider.DomainKeyStore
        String convertAlias(String str) {
            return str.toLowerCase(Locale.ENGLISH);
        }
    }

    DomainKeyStore() {
    }

    @Override // java.security.KeyStoreSpi
    public Key engineGetKey(String str, char[] cArr) throws NoSuchAlgorithmException, UnrecoverableKeyException {
        AbstractMap.SimpleEntry<String, Collection<KeyStore>> keystoresForReading = getKeystoresForReading(str);
        Key key = null;
        try {
            String key2 = keystoresForReading.getKey();
            Iterator<KeyStore> it = keystoresForReading.getValue().iterator();
            while (it.hasNext()) {
                key = it.next().getKey(key2, cArr);
                if (key != null) {
                    break;
                }
            }
            return key;
        } catch (KeyStoreException e2) {
            throw new IllegalStateException(e2);
        }
    }

    @Override // java.security.KeyStoreSpi
    public Certificate[] engineGetCertificateChain(String str) {
        AbstractMap.SimpleEntry<String, Collection<KeyStore>> keystoresForReading = getKeystoresForReading(str);
        Certificate[] certificateChain = null;
        try {
            String key = keystoresForReading.getKey();
            Iterator<KeyStore> it = keystoresForReading.getValue().iterator();
            while (it.hasNext()) {
                certificateChain = it.next().getCertificateChain(key);
                if (certificateChain != null) {
                    break;
                }
            }
            return certificateChain;
        } catch (KeyStoreException e2) {
            throw new IllegalStateException(e2);
        }
    }

    @Override // java.security.KeyStoreSpi
    public Certificate engineGetCertificate(String str) {
        AbstractMap.SimpleEntry<String, Collection<KeyStore>> keystoresForReading = getKeystoresForReading(str);
        Certificate certificate = null;
        try {
            String key = keystoresForReading.getKey();
            Iterator<KeyStore> it = keystoresForReading.getValue().iterator();
            while (it.hasNext()) {
                certificate = it.next().getCertificate(key);
                if (certificate != null) {
                    break;
                }
            }
            return certificate;
        } catch (KeyStoreException e2) {
            throw new IllegalStateException(e2);
        }
    }

    @Override // java.security.KeyStoreSpi
    public Date engineGetCreationDate(String str) {
        AbstractMap.SimpleEntry<String, Collection<KeyStore>> keystoresForReading = getKeystoresForReading(str);
        Date creationDate = null;
        try {
            String key = keystoresForReading.getKey();
            Iterator<KeyStore> it = keystoresForReading.getValue().iterator();
            while (it.hasNext()) {
                creationDate = it.next().getCreationDate(key);
                if (creationDate != null) {
                    break;
                }
            }
            return creationDate;
        } catch (KeyStoreException e2) {
            throw new IllegalStateException(e2);
        }
    }

    @Override // java.security.KeyStoreSpi
    public void engineSetKeyEntry(String str, Key key, char[] cArr, Certificate[] certificateArr) throws KeyStoreException {
        AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, KeyStore>> keystoreForWriting = getKeystoreForWriting(str);
        if (keystoreForWriting == null) {
            throw new KeyStoreException("Error setting key entry for '" + str + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        keystoreForWriting.getValue().getValue().setKeyEntry(keystoreForWriting.getKey(), key, cArr, certificateArr);
    }

    @Override // java.security.KeyStoreSpi
    public void engineSetKeyEntry(String str, byte[] bArr, Certificate[] certificateArr) throws KeyStoreException {
        AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, KeyStore>> keystoreForWriting = getKeystoreForWriting(str);
        if (keystoreForWriting == null) {
            throw new KeyStoreException("Error setting protected key entry for '" + str + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        keystoreForWriting.getValue().getValue().setKeyEntry(keystoreForWriting.getKey(), bArr, certificateArr);
    }

    @Override // java.security.KeyStoreSpi
    public void engineSetCertificateEntry(String str, Certificate certificate) throws KeyStoreException {
        AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, KeyStore>> keystoreForWriting = getKeystoreForWriting(str);
        if (keystoreForWriting == null) {
            throw new KeyStoreException("Error setting certificate entry for '" + str + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        keystoreForWriting.getValue().getValue().setCertificateEntry(keystoreForWriting.getKey(), certificate);
    }

    @Override // java.security.KeyStoreSpi
    public void engineDeleteEntry(String str) throws KeyStoreException {
        AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, KeyStore>> keystoreForWriting = getKeystoreForWriting(str);
        if (keystoreForWriting == null) {
            throw new KeyStoreException("Error deleting entry for '" + str + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        keystoreForWriting.getValue().getValue().deleteEntry(keystoreForWriting.getKey());
    }

    @Override // java.security.KeyStoreSpi
    public Enumeration<String> engineAliases() {
        final Iterator<Map.Entry<String, KeyStore>> it = this.keystores.entrySet().iterator();
        return new Enumeration<String>() { // from class: sun.security.provider.DomainKeyStore.1
            private int index = 0;
            private Map.Entry<String, KeyStore> keystoresEntry = null;
            private String prefix = null;
            private Enumeration<String> aliases = null;

            @Override // java.util.Enumeration
            public boolean hasMoreElements() {
                try {
                    if (this.aliases == null) {
                        if (it.hasNext()) {
                            this.keystoresEntry = (Map.Entry) it.next();
                            this.prefix = this.keystoresEntry.getKey() + DomainKeyStore.this.entryNameSeparator;
                            this.aliases = this.keystoresEntry.getValue().aliases();
                        } else {
                            return false;
                        }
                    }
                    if (this.aliases.hasMoreElements()) {
                        return true;
                    }
                    if (it.hasNext()) {
                        this.keystoresEntry = (Map.Entry) it.next();
                        this.prefix = this.keystoresEntry.getKey() + DomainKeyStore.this.entryNameSeparator;
                        this.aliases = this.keystoresEntry.getValue().aliases();
                        return this.aliases.hasMoreElements();
                    }
                    return false;
                } catch (KeyStoreException e2) {
                    return false;
                }
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Enumeration
            /* renamed from: nextElement */
            public String nextElement2() {
                if (hasMoreElements()) {
                    return this.prefix + this.aliases.nextElement2();
                }
                throw new NoSuchElementException();
            }
        };
    }

    @Override // java.security.KeyStoreSpi
    public boolean engineContainsAlias(String str) {
        AbstractMap.SimpleEntry<String, Collection<KeyStore>> keystoresForReading = getKeystoresForReading(str);
        try {
            String key = keystoresForReading.getKey();
            Iterator<KeyStore> it = keystoresForReading.getValue().iterator();
            while (it.hasNext()) {
                if (it.next().containsAlias(key)) {
                    return true;
                }
            }
            return false;
        } catch (KeyStoreException e2) {
            throw new IllegalStateException(e2);
        }
    }

    @Override // java.security.KeyStoreSpi
    public int engineSize() {
        int size = 0;
        try {
            Iterator<KeyStore> it = this.keystores.values().iterator();
            while (it.hasNext()) {
                size += it.next().size();
            }
            return size;
        } catch (KeyStoreException e2) {
            throw new IllegalStateException(e2);
        }
    }

    @Override // java.security.KeyStoreSpi
    public boolean engineIsKeyEntry(String str) {
        AbstractMap.SimpleEntry<String, Collection<KeyStore>> keystoresForReading = getKeystoresForReading(str);
        try {
            String key = keystoresForReading.getKey();
            Iterator<KeyStore> it = keystoresForReading.getValue().iterator();
            while (it.hasNext()) {
                if (it.next().isKeyEntry(key)) {
                    return true;
                }
            }
            return false;
        } catch (KeyStoreException e2) {
            throw new IllegalStateException(e2);
        }
    }

    @Override // java.security.KeyStoreSpi
    public boolean engineIsCertificateEntry(String str) {
        AbstractMap.SimpleEntry<String, Collection<KeyStore>> keystoresForReading = getKeystoresForReading(str);
        try {
            String key = keystoresForReading.getKey();
            Iterator<KeyStore> it = keystoresForReading.getValue().iterator();
            while (it.hasNext()) {
                if (it.next().isCertificateEntry(key)) {
                    return true;
                }
            }
            return false;
        } catch (KeyStoreException e2) {
            throw new IllegalStateException(e2);
        }
    }

    private AbstractMap.SimpleEntry<String, Collection<KeyStore>> getKeystoresForReading(String str) {
        String[] strArrSplit = str.split(this.entryNameSeparatorRegEx, 2);
        if (strArrSplit.length == 2) {
            KeyStore keyStore = this.keystores.get(strArrSplit[0]);
            if (keyStore != null) {
                return new AbstractMap.SimpleEntry<>(strArrSplit[1], Collections.singleton(keyStore));
            }
        } else if (strArrSplit.length == 1) {
            return new AbstractMap.SimpleEntry<>(str, this.keystores.values());
        }
        return new AbstractMap.SimpleEntry<>("", Collections.emptyList());
    }

    private AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, KeyStore>> getKeystoreForWriting(String str) {
        KeyStore keyStore;
        String[] strArrSplit = str.split(this.entryNameSeparator, 2);
        if (strArrSplit.length == 2 && (keyStore = this.keystores.get(strArrSplit[0])) != null) {
            return new AbstractMap.SimpleEntry<>(strArrSplit[1], new AbstractMap.SimpleEntry(strArrSplit[0], keyStore));
        }
        return null;
    }

    @Override // java.security.KeyStoreSpi
    public String engineGetCertificateAlias(Certificate certificate) {
        try {
            String str = null;
            Iterator<KeyStore> it = this.keystores.values().iterator();
            while (it.hasNext()) {
                String certificateAlias = it.next().getCertificateAlias(certificate);
                str = certificateAlias;
                if (certificateAlias != null) {
                    break;
                }
            }
            return str;
        } catch (KeyStoreException e2) {
            throw new IllegalStateException(e2);
        }
    }

    @Override // java.security.KeyStoreSpi
    public void engineStore(OutputStream outputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
        try {
            if (this.keystores.size() == 1) {
                this.keystores.values().iterator().next().store(outputStream, cArr);
                return;
            }
            throw new UnsupportedOperationException("This keystore must be stored using a DomainLoadStoreParameter");
        } catch (KeyStoreException e2) {
            throw new IllegalStateException(e2);
        }
    }

    @Override // java.security.KeyStoreSpi
    public void engineStore(KeyStore.LoadStoreParameter loadStoreParameter) throws NoSuchAlgorithmException, IOException, KeyStoreException, CertificateException {
        if (loadStoreParameter instanceof DomainLoadStoreParameter) {
            DomainLoadStoreParameter domainLoadStoreParameter = (DomainLoadStoreParameter) loadStoreParameter;
            for (KeyStoreBuilderComponents keyStoreBuilderComponents : getBuilders(domainLoadStoreParameter.getConfiguration(), domainLoadStoreParameter.getProtectionParams())) {
                try {
                    if (!(keyStoreBuilderComponents.protection instanceof KeyStore.PasswordProtection)) {
                        throw new KeyStoreException(new IllegalArgumentException("ProtectionParameter must be a KeyStore.PasswordProtection"));
                    }
                    char[] password = ((KeyStore.PasswordProtection) keyStoreBuilderComponents.protection).getPassword();
                    KeyStore keyStore = this.keystores.get(keyStoreBuilderComponents.name);
                    FileOutputStream fileOutputStream = new FileOutputStream(keyStoreBuilderComponents.file);
                    Throwable th = null;
                    try {
                        try {
                            keyStore.store(fileOutputStream, password);
                            if (fileOutputStream != null) {
                                if (0 != 0) {
                                    try {
                                        fileOutputStream.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                } else {
                                    fileOutputStream.close();
                                }
                            }
                        } finally {
                        }
                    } finally {
                    }
                } catch (KeyStoreException e2) {
                    throw new IOException(e2);
                }
            }
            return;
        }
        throw new UnsupportedOperationException("This keystore must be stored using a DomainLoadStoreParameter");
    }

    @Override // java.security.KeyStoreSpi
    public void engineLoad(InputStream inputStream, char[] cArr) throws Exception {
        KeyStore keyStore;
        try {
            try {
                keyStore = KeyStore.getInstance("JKS");
                keyStore.load(inputStream, cArr);
            } catch (Exception e2) {
                if (!"JKS".equalsIgnoreCase(DEFAULT_KEYSTORE_TYPE)) {
                    keyStore = KeyStore.getInstance(DEFAULT_KEYSTORE_TYPE);
                    keyStore.load(inputStream, cArr);
                } else {
                    throw e2;
                }
            }
            StringBuilder sbAppend = new StringBuilder().append(DEFAULT_STREAM_PREFIX);
            int i2 = this.streamCounter;
            this.streamCounter = i2 + 1;
            this.keystores.put(sbAppend.append(i2).toString(), keyStore);
        } catch (Exception e3) {
            throw new UnsupportedOperationException("This keystore must be loaded using a DomainLoadStoreParameter");
        }
    }

    @Override // java.security.KeyStoreSpi
    public void engineLoad(KeyStore.LoadStoreParameter loadStoreParameter) throws NoSuchAlgorithmException, IOException, CertificateException {
        if (loadStoreParameter instanceof DomainLoadStoreParameter) {
            DomainLoadStoreParameter domainLoadStoreParameter = (DomainLoadStoreParameter) loadStoreParameter;
            for (KeyStoreBuilderComponents keyStoreBuilderComponents : getBuilders(domainLoadStoreParameter.getConfiguration(), domainLoadStoreParameter.getProtectionParams())) {
                try {
                    if (keyStoreBuilderComponents.file != null) {
                        this.keystores.put(keyStoreBuilderComponents.name, KeyStore.Builder.newInstance(keyStoreBuilderComponents.type, keyStoreBuilderComponents.provider, keyStoreBuilderComponents.file, keyStoreBuilderComponents.protection).getKeyStore());
                    } else {
                        this.keystores.put(keyStoreBuilderComponents.name, KeyStore.Builder.newInstance(keyStoreBuilderComponents.type, keyStoreBuilderComponents.provider, keyStoreBuilderComponents.protection).getKeyStore());
                    }
                } catch (KeyStoreException e2) {
                    throw new IOException(e2);
                }
            }
            return;
        }
        throw new UnsupportedOperationException("This keystore must be loaded using a DomainLoadStoreParameter");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0319  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0334  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.util.List<sun.security.provider.DomainKeyStore.KeyStoreBuilderComponents> getBuilders(java.net.URI r11, java.util.Map<java.lang.String, java.security.KeyStore.ProtectionParameter> r12) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 823
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.provider.DomainKeyStore.getBuilders(java.net.URI, java.util.Map):java.util.List");
    }

    /* loaded from: rt.jar:sun/security/provider/DomainKeyStore$KeyStoreBuilderComponents.class */
    class KeyStoreBuilderComponents {
        String name;
        String type;
        Provider provider;
        File file;
        KeyStore.ProtectionParameter protection;

        KeyStoreBuilderComponents(String str, String str2, Provider provider, File file, KeyStore.ProtectionParameter protectionParameter) {
            this.name = str;
            this.type = str2;
            this.provider = provider;
            this.file = file;
            this.protection = protectionParameter;
        }
    }
}
