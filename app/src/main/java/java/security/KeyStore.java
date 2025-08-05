package java.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import javax.crypto.SecretKey;
import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import sun.security.util.Debug;

/* loaded from: rt.jar:java/security/KeyStore.class */
public class KeyStore {
    private static final Debug pdebug = Debug.getInstance("provider", "Provider");
    private static final boolean skipDebug;
    private static final String KEYSTORE_TYPE = "keystore.type";
    private String type;
    private Provider provider;
    private KeyStoreSpi keyStoreSpi;
    private boolean initialized = false;

    /* loaded from: rt.jar:java/security/KeyStore$LoadStoreParameter.class */
    public interface LoadStoreParameter {
        ProtectionParameter getProtectionParameter();
    }

    /* loaded from: rt.jar:java/security/KeyStore$ProtectionParameter.class */
    public interface ProtectionParameter {
    }

    static {
        skipDebug = Debug.isOn("engine=") && !Debug.isOn("keystore");
    }

    /* loaded from: rt.jar:java/security/KeyStore$PasswordProtection.class */
    public static class PasswordProtection implements ProtectionParameter, Destroyable {
        private final char[] password;
        private final String protectionAlgorithm;
        private final AlgorithmParameterSpec protectionParameters;
        private volatile boolean destroyed = false;

        public PasswordProtection(char[] cArr) {
            this.password = cArr == null ? null : (char[]) cArr.clone();
            this.protectionAlgorithm = null;
            this.protectionParameters = null;
        }

        public PasswordProtection(char[] cArr, String str, AlgorithmParameterSpec algorithmParameterSpec) {
            if (str == null) {
                throw new NullPointerException("invalid null input");
            }
            this.password = cArr == null ? null : (char[]) cArr.clone();
            this.protectionAlgorithm = str;
            this.protectionParameters = algorithmParameterSpec;
        }

        public String getProtectionAlgorithm() {
            return this.protectionAlgorithm;
        }

        public AlgorithmParameterSpec getProtectionParameters() {
            return this.protectionParameters;
        }

        public synchronized char[] getPassword() {
            if (this.destroyed) {
                throw new IllegalStateException("password has been cleared");
            }
            return this.password;
        }

        @Override // javax.security.auth.Destroyable
        public synchronized void destroy() throws DestroyFailedException {
            this.destroyed = true;
            if (this.password != null) {
                Arrays.fill(this.password, ' ');
            }
        }

        @Override // javax.security.auth.Destroyable
        public synchronized boolean isDestroyed() {
            return this.destroyed;
        }
    }

    /* loaded from: rt.jar:java/security/KeyStore$CallbackHandlerProtection.class */
    public static class CallbackHandlerProtection implements ProtectionParameter {
        private final CallbackHandler handler;

        public CallbackHandlerProtection(CallbackHandler callbackHandler) {
            if (callbackHandler == null) {
                throw new NullPointerException("handler must not be null");
            }
            this.handler = callbackHandler;
        }

        public CallbackHandler getCallbackHandler() {
            return this.handler;
        }
    }

    /* loaded from: rt.jar:java/security/KeyStore$Entry.class */
    public interface Entry {

        /* loaded from: rt.jar:java/security/KeyStore$Entry$Attribute.class */
        public interface Attribute {
            String getName();

            String getValue();
        }

        default Set<Attribute> getAttributes() {
            return Collections.emptySet();
        }
    }

    /* loaded from: rt.jar:java/security/KeyStore$PrivateKeyEntry.class */
    public static final class PrivateKeyEntry implements Entry {
        private final PrivateKey privKey;
        private final java.security.cert.Certificate[] chain;
        private final Set<Entry.Attribute> attributes;

        public PrivateKeyEntry(PrivateKey privateKey, java.security.cert.Certificate[] certificateArr) {
            this(privateKey, certificateArr, Collections.emptySet());
        }

        public PrivateKeyEntry(PrivateKey privateKey, java.security.cert.Certificate[] certificateArr, Set<Entry.Attribute> set) {
            if (privateKey == null || certificateArr == null || set == null) {
                throw new NullPointerException("invalid null input");
            }
            if (certificateArr.length == 0) {
                throw new IllegalArgumentException("invalid zero-length input chain");
            }
            java.security.cert.Certificate[] certificateArr2 = (java.security.cert.Certificate[]) certificateArr.clone();
            String type = certificateArr2[0].getType();
            for (int i2 = 1; i2 < certificateArr2.length; i2++) {
                if (!type.equals(certificateArr2[i2].getType())) {
                    throw new IllegalArgumentException("chain does not contain certificates of the same type");
                }
            }
            if (!privateKey.getAlgorithm().equals(certificateArr2[0].getPublicKey().getAlgorithm())) {
                throw new IllegalArgumentException("private key algorithm does not match algorithm of public key in end entity certificate (at index 0)");
            }
            this.privKey = privateKey;
            if ((certificateArr2[0] instanceof X509Certificate) && !(certificateArr2 instanceof X509Certificate[])) {
                this.chain = new X509Certificate[certificateArr2.length];
                System.arraycopy(certificateArr2, 0, this.chain, 0, certificateArr2.length);
            } else {
                this.chain = certificateArr2;
            }
            this.attributes = Collections.unmodifiableSet(new HashSet(set));
        }

        public PrivateKey getPrivateKey() {
            return this.privKey;
        }

        public java.security.cert.Certificate[] getCertificateChain() {
            return (java.security.cert.Certificate[]) this.chain.clone();
        }

        public java.security.cert.Certificate getCertificate() {
            return this.chain[0];
        }

        @Override // java.security.KeyStore.Entry
        public Set<Entry.Attribute> getAttributes() {
            return this.attributes;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Private key entry and certificate chain with " + this.chain.length + " elements:\r\n");
            for (java.security.cert.Certificate certificate : this.chain) {
                sb.append((Object) certificate);
                sb.append("\r\n");
            }
            return sb.toString();
        }
    }

    /* loaded from: rt.jar:java/security/KeyStore$SecretKeyEntry.class */
    public static final class SecretKeyEntry implements Entry {
        private final SecretKey sKey;
        private final Set<Entry.Attribute> attributes;

        public SecretKeyEntry(SecretKey secretKey) {
            if (secretKey == null) {
                throw new NullPointerException("invalid null input");
            }
            this.sKey = secretKey;
            this.attributes = Collections.emptySet();
        }

        public SecretKeyEntry(SecretKey secretKey, Set<Entry.Attribute> set) {
            if (secretKey == null || set == null) {
                throw new NullPointerException("invalid null input");
            }
            this.sKey = secretKey;
            this.attributes = Collections.unmodifiableSet(new HashSet(set));
        }

        public SecretKey getSecretKey() {
            return this.sKey;
        }

        @Override // java.security.KeyStore.Entry
        public Set<Entry.Attribute> getAttributes() {
            return this.attributes;
        }

        public String toString() {
            return "Secret key entry with algorithm " + this.sKey.getAlgorithm();
        }
    }

    /* loaded from: rt.jar:java/security/KeyStore$TrustedCertificateEntry.class */
    public static final class TrustedCertificateEntry implements Entry {
        private final java.security.cert.Certificate cert;
        private final Set<Entry.Attribute> attributes;

        public TrustedCertificateEntry(java.security.cert.Certificate certificate) {
            if (certificate == null) {
                throw new NullPointerException("invalid null input");
            }
            this.cert = certificate;
            this.attributes = Collections.emptySet();
        }

        public TrustedCertificateEntry(java.security.cert.Certificate certificate, Set<Entry.Attribute> set) {
            if (certificate == null || set == null) {
                throw new NullPointerException("invalid null input");
            }
            this.cert = certificate;
            this.attributes = Collections.unmodifiableSet(new HashSet(set));
        }

        public java.security.cert.Certificate getTrustedCertificate() {
            return this.cert;
        }

        @Override // java.security.KeyStore.Entry
        public Set<Entry.Attribute> getAttributes() {
            return this.attributes;
        }

        public String toString() {
            return "Trusted certificate entry:\r\n" + this.cert.toString();
        }
    }

    protected KeyStore(KeyStoreSpi keyStoreSpi, Provider provider, String str) {
        this.keyStoreSpi = keyStoreSpi;
        this.provider = provider;
        this.type = str;
        if (!skipDebug && pdebug != null) {
            pdebug.println("KeyStore." + str.toUpperCase() + " type from: " + this.provider.getName());
        }
    }

    public static KeyStore getInstance(String str) throws KeyStoreException {
        try {
            Object[] impl = Security.getImpl(str, "KeyStore", (String) null);
            return new KeyStore((KeyStoreSpi) impl[0], (Provider) impl[1], str);
        } catch (NoSuchAlgorithmException e2) {
            throw new KeyStoreException(str + " not found", e2);
        } catch (NoSuchProviderException e3) {
            throw new KeyStoreException(str + " not found", e3);
        }
    }

    public static KeyStore getInstance(String str, String str2) throws KeyStoreException, NoSuchProviderException {
        if (str2 == null || str2.length() == 0) {
            throw new IllegalArgumentException("missing provider");
        }
        try {
            Object[] impl = Security.getImpl(str, "KeyStore", str2);
            return new KeyStore((KeyStoreSpi) impl[0], (Provider) impl[1], str);
        } catch (NoSuchAlgorithmException e2) {
            throw new KeyStoreException(str + " not found", e2);
        }
    }

    public static KeyStore getInstance(String str, Provider provider) throws KeyStoreException {
        if (provider == null) {
            throw new IllegalArgumentException("missing provider");
        }
        try {
            Object[] impl = Security.getImpl(str, "KeyStore", provider);
            return new KeyStore((KeyStoreSpi) impl[0], (Provider) impl[1], str);
        } catch (NoSuchAlgorithmException e2) {
            throw new KeyStoreException(str + " not found", e2);
        }
    }

    public static final String getDefaultType() {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: java.security.KeyStore.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public String run() {
                return Security.getProperty(KeyStore.KEYSTORE_TYPE);
            }
        });
        if (str == null) {
            str = "jks";
        }
        return str;
    }

    public final Provider getProvider() {
        return this.provider;
    }

    public final String getType() {
        return this.type;
    }

    public final Key getKey(String str, char[] cArr) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
        if (!this.initialized) {
            throw new KeyStoreException("Uninitialized keystore");
        }
        return this.keyStoreSpi.engineGetKey(str, cArr);
    }

    public final java.security.cert.Certificate[] getCertificateChain(String str) throws KeyStoreException {
        if (!this.initialized) {
            throw new KeyStoreException("Uninitialized keystore");
        }
        return this.keyStoreSpi.engineGetCertificateChain(str);
    }

    public final java.security.cert.Certificate getCertificate(String str) throws KeyStoreException {
        if (!this.initialized) {
            throw new KeyStoreException("Uninitialized keystore");
        }
        return this.keyStoreSpi.engineGetCertificate(str);
    }

    public final Date getCreationDate(String str) throws KeyStoreException {
        if (!this.initialized) {
            throw new KeyStoreException("Uninitialized keystore");
        }
        return this.keyStoreSpi.engineGetCreationDate(str);
    }

    public final void setKeyEntry(String str, Key key, char[] cArr, java.security.cert.Certificate[] certificateArr) throws KeyStoreException {
        if (!this.initialized) {
            throw new KeyStoreException("Uninitialized keystore");
        }
        if ((key instanceof PrivateKey) && (certificateArr == null || certificateArr.length == 0)) {
            throw new IllegalArgumentException("Private key must be accompanied by certificate chain");
        }
        this.keyStoreSpi.engineSetKeyEntry(str, key, cArr, certificateArr);
    }

    public final void setKeyEntry(String str, byte[] bArr, java.security.cert.Certificate[] certificateArr) throws KeyStoreException {
        if (!this.initialized) {
            throw new KeyStoreException("Uninitialized keystore");
        }
        this.keyStoreSpi.engineSetKeyEntry(str, bArr, certificateArr);
    }

    public final void setCertificateEntry(String str, java.security.cert.Certificate certificate) throws KeyStoreException {
        if (!this.initialized) {
            throw new KeyStoreException("Uninitialized keystore");
        }
        this.keyStoreSpi.engineSetCertificateEntry(str, certificate);
    }

    public final void deleteEntry(String str) throws KeyStoreException {
        if (!this.initialized) {
            throw new KeyStoreException("Uninitialized keystore");
        }
        this.keyStoreSpi.engineDeleteEntry(str);
    }

    public final Enumeration<String> aliases() throws KeyStoreException {
        if (!this.initialized) {
            throw new KeyStoreException("Uninitialized keystore");
        }
        return this.keyStoreSpi.engineAliases();
    }

    public final boolean containsAlias(String str) throws KeyStoreException {
        if (!this.initialized) {
            throw new KeyStoreException("Uninitialized keystore");
        }
        return this.keyStoreSpi.engineContainsAlias(str);
    }

    public final int size() throws KeyStoreException {
        if (!this.initialized) {
            throw new KeyStoreException("Uninitialized keystore");
        }
        return this.keyStoreSpi.engineSize();
    }

    public final boolean isKeyEntry(String str) throws KeyStoreException {
        if (!this.initialized) {
            throw new KeyStoreException("Uninitialized keystore");
        }
        return this.keyStoreSpi.engineIsKeyEntry(str);
    }

    public final boolean isCertificateEntry(String str) throws KeyStoreException {
        if (!this.initialized) {
            throw new KeyStoreException("Uninitialized keystore");
        }
        return this.keyStoreSpi.engineIsCertificateEntry(str);
    }

    public final String getCertificateAlias(java.security.cert.Certificate certificate) throws KeyStoreException {
        if (!this.initialized) {
            throw new KeyStoreException("Uninitialized keystore");
        }
        return this.keyStoreSpi.engineGetCertificateAlias(certificate);
    }

    public final void store(OutputStream outputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, KeyStoreException, CertificateException {
        if (!this.initialized) {
            throw new KeyStoreException("Uninitialized keystore");
        }
        this.keyStoreSpi.engineStore(outputStream, cArr);
    }

    public final void store(LoadStoreParameter loadStoreParameter) throws NoSuchAlgorithmException, IOException, KeyStoreException, CertificateException {
        if (!this.initialized) {
            throw new KeyStoreException("Uninitialized keystore");
        }
        this.keyStoreSpi.engineStore(loadStoreParameter);
    }

    public final void load(InputStream inputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
        this.keyStoreSpi.engineLoad(inputStream, cArr);
        this.initialized = true;
    }

    public final void load(LoadStoreParameter loadStoreParameter) throws NoSuchAlgorithmException, IOException, CertificateException {
        this.keyStoreSpi.engineLoad(loadStoreParameter);
        this.initialized = true;
    }

    public final Entry getEntry(String str, ProtectionParameter protectionParameter) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableEntryException {
        if (str == null) {
            throw new NullPointerException("invalid null input");
        }
        if (!this.initialized) {
            throw new KeyStoreException("Uninitialized keystore");
        }
        return this.keyStoreSpi.engineGetEntry(str, protectionParameter);
    }

    public final void setEntry(String str, Entry entry, ProtectionParameter protectionParameter) throws KeyStoreException {
        if (str == null || entry == null) {
            throw new NullPointerException("invalid null input");
        }
        if (!this.initialized) {
            throw new KeyStoreException("Uninitialized keystore");
        }
        this.keyStoreSpi.engineSetEntry(str, entry, protectionParameter);
    }

    public final boolean entryInstanceOf(String str, Class<? extends Entry> cls) throws KeyStoreException {
        if (str == null || cls == null) {
            throw new NullPointerException("invalid null input");
        }
        if (!this.initialized) {
            throw new KeyStoreException("Uninitialized keystore");
        }
        return this.keyStoreSpi.engineEntryInstanceOf(str, cls);
    }

    /* loaded from: rt.jar:java/security/KeyStore$Builder.class */
    public static abstract class Builder {
        static final int MAX_CALLBACK_TRIES = 3;

        public abstract KeyStore getKeyStore() throws KeyStoreException;

        public abstract ProtectionParameter getProtectionParameter(String str) throws KeyStoreException;

        protected Builder() {
        }

        public static Builder newInstance(final KeyStore keyStore, final ProtectionParameter protectionParameter) {
            if (keyStore != null && protectionParameter != null) {
                if (!keyStore.initialized) {
                    throw new IllegalArgumentException("KeyStore not initialized");
                }
                return new Builder() { // from class: java.security.KeyStore.Builder.1
                    private volatile boolean getCalled;

                    @Override // java.security.KeyStore.Builder
                    public KeyStore getKeyStore() {
                        this.getCalled = true;
                        return keyStore;
                    }

                    @Override // java.security.KeyStore.Builder
                    public ProtectionParameter getProtectionParameter(String str) {
                        if (str == null) {
                            throw new NullPointerException();
                        }
                        if (!this.getCalled) {
                            throw new IllegalStateException("getKeyStore() must be called first");
                        }
                        return protectionParameter;
                    }
                };
            }
            throw new NullPointerException();
        }

        public static Builder newInstance(String str, Provider provider, File file, ProtectionParameter protectionParameter) {
            if (str == null || file == null || protectionParameter == null) {
                throw new NullPointerException();
            }
            if (!(protectionParameter instanceof PasswordProtection) && !(protectionParameter instanceof CallbackHandlerProtection)) {
                throw new IllegalArgumentException("Protection must be PasswordProtection or CallbackHandlerProtection");
            }
            if (!file.isFile()) {
                throw new IllegalArgumentException("File does not exist or it does not refer to a normal file: " + ((Object) file));
            }
            return new FileBuilder(str, provider, file, protectionParameter, AccessController.getContext());
        }

        /* loaded from: rt.jar:java/security/KeyStore$Builder$FileBuilder.class */
        private static final class FileBuilder extends Builder {
            private final String type;
            private final Provider provider;
            private final File file;
            private ProtectionParameter protection;
            private ProtectionParameter keyProtection;
            private final AccessControlContext context;
            private KeyStore keyStore;
            private Throwable oldException;

            FileBuilder(String str, Provider provider, File file, ProtectionParameter protectionParameter, AccessControlContext accessControlContext) {
                this.type = str;
                this.provider = provider;
                this.file = file;
                this.protection = protectionParameter;
                this.context = accessControlContext;
            }

            @Override // java.security.KeyStore.Builder
            public synchronized KeyStore getKeyStore() throws KeyStoreException {
                if (this.keyStore != null) {
                    return this.keyStore;
                }
                if (this.oldException != null) {
                    throw new KeyStoreException("Previous KeyStore instantiation failed", this.oldException);
                }
                try {
                    this.keyStore = (KeyStore) AccessController.doPrivileged(new PrivilegedExceptionAction<KeyStore>() { // from class: java.security.KeyStore.Builder.FileBuilder.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedExceptionAction
                        public KeyStore run() throws Exception {
                            if (!(FileBuilder.this.protection instanceof CallbackHandlerProtection)) {
                                return run0();
                            }
                            int i2 = 0;
                            do {
                                i2++;
                                try {
                                    return run0();
                                } catch (IOException e2) {
                                    if (i2 >= 3) {
                                        break;
                                    }
                                    throw e2;
                                }
                            } while (e2.getCause() instanceof UnrecoverableKeyException);
                            throw e2;
                        }

                        public KeyStore run0() throws Exception {
                            char[] password;
                            KeyStore keyStore = FileBuilder.this.provider == null ? KeyStore.getInstance(FileBuilder.this.type) : KeyStore.getInstance(FileBuilder.this.type, FileBuilder.this.provider);
                            AutoCloseable autoCloseable = null;
                            try {
                                FileInputStream fileInputStream = new FileInputStream(FileBuilder.this.file);
                                if (FileBuilder.this.protection instanceof PasswordProtection) {
                                    password = ((PasswordProtection) FileBuilder.this.protection).getPassword();
                                    FileBuilder.this.keyProtection = FileBuilder.this.protection;
                                } else {
                                    CallbackHandler callbackHandler = ((CallbackHandlerProtection) FileBuilder.this.protection).getCallbackHandler();
                                    PasswordCallback passwordCallback = new PasswordCallback("Password for keystore " + FileBuilder.this.file.getName(), false);
                                    callbackHandler.handle(new Callback[]{passwordCallback});
                                    password = passwordCallback.getPassword();
                                    if (password == null) {
                                        throw new KeyStoreException("No password provided");
                                    }
                                    passwordCallback.clearPassword();
                                    FileBuilder.this.keyProtection = new PasswordProtection(password);
                                }
                                keyStore.load(fileInputStream, password);
                                KeyStore keyStore2 = keyStore;
                                if (fileInputStream != null) {
                                    fileInputStream.close();
                                }
                                return keyStore2;
                            } catch (Throwable th) {
                                if (0 != 0) {
                                    autoCloseable.close();
                                }
                                throw th;
                            }
                        }
                    }, this.context);
                    return this.keyStore;
                } catch (PrivilegedActionException e2) {
                    this.oldException = e2.getCause();
                    throw new KeyStoreException("KeyStore instantiation failed", this.oldException);
                }
            }

            @Override // java.security.KeyStore.Builder
            public synchronized ProtectionParameter getProtectionParameter(String str) {
                if (str == null) {
                    throw new NullPointerException();
                }
                if (this.keyStore == null) {
                    throw new IllegalStateException("getKeyStore() must be called first");
                }
                return this.keyProtection;
            }
        }

        public static Builder newInstance(final String str, final Provider provider, final ProtectionParameter protectionParameter) {
            if (str == null || protectionParameter == null) {
                throw new NullPointerException();
            }
            final AccessControlContext context = AccessController.getContext();
            return new Builder() { // from class: java.security.KeyStore.Builder.2
                private volatile boolean getCalled;
                private IOException oldException;
                private final PrivilegedExceptionAction<KeyStore> action = new PrivilegedExceptionAction<KeyStore>() { // from class: java.security.KeyStore.Builder.2.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public KeyStore run() throws Exception {
                        KeyStore keyStore;
                        if (provider == null) {
                            keyStore = KeyStore.getInstance(str);
                        } else {
                            keyStore = KeyStore.getInstance(str, provider);
                        }
                        SimpleLoadStoreParameter simpleLoadStoreParameter = new SimpleLoadStoreParameter(protectionParameter);
                        if (!(protectionParameter instanceof CallbackHandlerProtection)) {
                            keyStore.load(simpleLoadStoreParameter);
                        } else {
                            int i2 = 0;
                            while (true) {
                                i2++;
                                try {
                                    keyStore.load(simpleLoadStoreParameter);
                                    break;
                                } catch (IOException e2) {
                                    if (!(e2.getCause() instanceof UnrecoverableKeyException)) {
                                        break;
                                    }
                                    if (i2 >= 3) {
                                        AnonymousClass2.this.oldException = e2;
                                        break;
                                    }
                                    throw e2;
                                }
                            }
                            throw e2;
                        }
                        AnonymousClass2.this.getCalled = true;
                        return keyStore;
                    }
                };

                @Override // java.security.KeyStore.Builder
                public synchronized KeyStore getKeyStore() throws KeyStoreException {
                    if (this.oldException != null) {
                        throw new KeyStoreException("Previous KeyStore instantiation failed", this.oldException);
                    }
                    try {
                        return (KeyStore) AccessController.doPrivileged(this.action, context);
                    } catch (PrivilegedActionException e2) {
                        throw new KeyStoreException("KeyStore instantiation failed", e2.getCause());
                    }
                }

                @Override // java.security.KeyStore.Builder
                public ProtectionParameter getProtectionParameter(String str2) {
                    if (str2 == null) {
                        throw new NullPointerException();
                    }
                    if (!this.getCalled) {
                        throw new IllegalStateException("getKeyStore() must be called first");
                    }
                    return protectionParameter;
                }
            };
        }
    }

    /* loaded from: rt.jar:java/security/KeyStore$SimpleLoadStoreParameter.class */
    static class SimpleLoadStoreParameter implements LoadStoreParameter {
        private final ProtectionParameter protection;

        SimpleLoadStoreParameter(ProtectionParameter protectionParameter) {
            this.protection = protectionParameter;
        }

        @Override // java.security.KeyStore.LoadStoreParameter
        public ProtectionParameter getProtectionParameter() {
            return this.protection;
        }
    }
}
