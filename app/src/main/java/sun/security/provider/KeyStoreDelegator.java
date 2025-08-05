package sun.security.provider;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;
import java.security.Security;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Enumeration;
import sun.security.util.Debug;

/* loaded from: rt.jar:sun/security/provider/KeyStoreDelegator.class */
class KeyStoreDelegator extends KeyStoreSpi {
    private static final String KEYSTORE_TYPE_COMPAT = "keystore.type.compat";
    private static final Debug debug = Debug.getInstance("keystore");
    private final String primaryType;
    private final String secondaryType;
    private final Class<? extends KeyStoreSpi> primaryKeyStore;
    private final Class<? extends KeyStoreSpi> secondaryKeyStore;
    private String type;
    private KeyStoreSpi keystore;
    private boolean compatModeEnabled;

    public KeyStoreDelegator(String str, Class<? extends KeyStoreSpi> cls, String str2, Class<? extends KeyStoreSpi> cls2) {
        this.compatModeEnabled = true;
        this.compatModeEnabled = "true".equalsIgnoreCase((String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.security.provider.KeyStoreDelegator.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return Security.getProperty(KeyStoreDelegator.KEYSTORE_TYPE_COMPAT);
            }
        }));
        if (this.compatModeEnabled) {
            this.primaryType = str;
            this.secondaryType = str2;
            this.primaryKeyStore = cls;
            this.secondaryKeyStore = cls2;
            return;
        }
        this.primaryType = str;
        this.secondaryType = null;
        this.primaryKeyStore = cls;
        this.secondaryKeyStore = null;
        if (debug != null) {
            debug.println("WARNING: compatibility mode disabled for " + str + " and " + str2 + " keystore types");
        }
    }

    @Override // java.security.KeyStoreSpi
    public Key engineGetKey(String str, char[] cArr) throws NoSuchAlgorithmException, UnrecoverableKeyException {
        return this.keystore.engineGetKey(str, cArr);
    }

    @Override // java.security.KeyStoreSpi
    public Certificate[] engineGetCertificateChain(String str) {
        return this.keystore.engineGetCertificateChain(str);
    }

    @Override // java.security.KeyStoreSpi
    public Certificate engineGetCertificate(String str) {
        return this.keystore.engineGetCertificate(str);
    }

    @Override // java.security.KeyStoreSpi
    public Date engineGetCreationDate(String str) {
        return this.keystore.engineGetCreationDate(str);
    }

    @Override // java.security.KeyStoreSpi
    public void engineSetKeyEntry(String str, Key key, char[] cArr, Certificate[] certificateArr) throws KeyStoreException {
        this.keystore.engineSetKeyEntry(str, key, cArr, certificateArr);
    }

    @Override // java.security.KeyStoreSpi
    public void engineSetKeyEntry(String str, byte[] bArr, Certificate[] certificateArr) throws KeyStoreException {
        this.keystore.engineSetKeyEntry(str, bArr, certificateArr);
    }

    @Override // java.security.KeyStoreSpi
    public void engineSetCertificateEntry(String str, Certificate certificate) throws KeyStoreException {
        this.keystore.engineSetCertificateEntry(str, certificate);
    }

    @Override // java.security.KeyStoreSpi
    public void engineDeleteEntry(String str) throws KeyStoreException {
        this.keystore.engineDeleteEntry(str);
    }

    @Override // java.security.KeyStoreSpi
    public Enumeration<String> engineAliases() {
        return this.keystore.engineAliases();
    }

    @Override // java.security.KeyStoreSpi
    public boolean engineContainsAlias(String str) {
        return this.keystore.engineContainsAlias(str);
    }

    @Override // java.security.KeyStoreSpi
    public int engineSize() {
        return this.keystore.engineSize();
    }

    @Override // java.security.KeyStoreSpi
    public boolean engineIsKeyEntry(String str) {
        return this.keystore.engineIsKeyEntry(str);
    }

    @Override // java.security.KeyStoreSpi
    public boolean engineIsCertificateEntry(String str) {
        return this.keystore.engineIsCertificateEntry(str);
    }

    @Override // java.security.KeyStoreSpi
    public String engineGetCertificateAlias(Certificate certificate) {
        return this.keystore.engineGetCertificateAlias(certificate);
    }

    @Override // java.security.KeyStoreSpi
    public KeyStore.Entry engineGetEntry(String str, KeyStore.ProtectionParameter protectionParameter) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableEntryException {
        return this.keystore.engineGetEntry(str, protectionParameter);
    }

    @Override // java.security.KeyStoreSpi
    public void engineSetEntry(String str, KeyStore.Entry entry, KeyStore.ProtectionParameter protectionParameter) throws KeyStoreException {
        this.keystore.engineSetEntry(str, entry, protectionParameter);
    }

    @Override // java.security.KeyStoreSpi
    public boolean engineEntryInstanceOf(String str, Class<? extends KeyStore.Entry> cls) {
        return this.keystore.engineEntryInstanceOf(str, cls);
    }

    @Override // java.security.KeyStoreSpi
    public void engineStore(OutputStream outputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
        if (debug != null) {
            debug.println("Storing keystore in " + this.type + " format");
        }
        this.keystore.engineStore(outputStream, cArr);
    }

    @Override // java.security.KeyStoreSpi
    public void engineLoad(InputStream inputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
        if (inputStream == null || !this.compatModeEnabled) {
            try {
                this.keystore = this.primaryKeyStore.newInstance();
            } catch (IllegalAccessException | InstantiationException e2) {
            }
            this.type = this.primaryType;
            if (debug != null && inputStream == null) {
                debug.println("Creating a new keystore in " + this.type + " format");
            }
            this.keystore.engineLoad(inputStream, cArr);
            return;
        }
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        bufferedInputStream.mark(Integer.MAX_VALUE);
        try {
            this.keystore = this.primaryKeyStore.newInstance();
            this.type = this.primaryType;
            this.keystore.engineLoad(bufferedInputStream, cArr);
        } catch (Exception e3) {
            if ((e3 instanceof IOException) && (e3.getCause() instanceof UnrecoverableKeyException)) {
                throw ((IOException) e3);
            }
            try {
                this.keystore = this.secondaryKeyStore.newInstance();
                this.type = this.secondaryType;
                bufferedInputStream.reset();
                this.keystore.engineLoad(bufferedInputStream, cArr);
                if (debug != null) {
                    debug.println("WARNING: switching from " + this.primaryType + " to " + this.secondaryType + " keystore file format has altered the keystore security level");
                }
            } catch (IOException | NoSuchAlgorithmException | CertificateException e4) {
                if ((e4 instanceof IOException) && (e4.getCause() instanceof UnrecoverableKeyException)) {
                    throw ((IOException) e4);
                }
                if (e3 instanceof IOException) {
                    throw ((IOException) e3);
                }
                if (e3 instanceof CertificateException) {
                    throw ((CertificateException) e3);
                }
                if (e3 instanceof NoSuchAlgorithmException) {
                    throw ((NoSuchAlgorithmException) e3);
                }
                if (e3 instanceof RuntimeException) {
                    throw ((RuntimeException) e3);
                }
            } catch (IllegalAccessException | InstantiationException e5) {
            }
        }
        if (debug != null) {
            debug.println("Loaded a keystore in " + this.type + " format");
        }
    }
}
