package java.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Enumeration;
import javax.crypto.SecretKey;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/* loaded from: rt.jar:java/security/KeyStoreSpi.class */
public abstract class KeyStoreSpi {
    public abstract Key engineGetKey(String str, char[] cArr) throws NoSuchAlgorithmException, UnrecoverableKeyException;

    public abstract java.security.cert.Certificate[] engineGetCertificateChain(String str);

    public abstract java.security.cert.Certificate engineGetCertificate(String str);

    public abstract Date engineGetCreationDate(String str);

    public abstract void engineSetKeyEntry(String str, Key key, char[] cArr, java.security.cert.Certificate[] certificateArr) throws KeyStoreException;

    public abstract void engineSetKeyEntry(String str, byte[] bArr, java.security.cert.Certificate[] certificateArr) throws KeyStoreException;

    public abstract void engineSetCertificateEntry(String str, java.security.cert.Certificate certificate) throws KeyStoreException;

    public abstract void engineDeleteEntry(String str) throws KeyStoreException;

    public abstract Enumeration<String> engineAliases();

    public abstract boolean engineContainsAlias(String str);

    public abstract int engineSize();

    public abstract boolean engineIsKeyEntry(String str);

    public abstract boolean engineIsCertificateEntry(String str);

    public abstract String engineGetCertificateAlias(java.security.cert.Certificate certificate);

    public abstract void engineStore(OutputStream outputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException;

    public abstract void engineLoad(InputStream inputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException;

    public void engineStore(KeyStore.LoadStoreParameter loadStoreParameter) throws NoSuchAlgorithmException, IOException, CertificateException {
        throw new UnsupportedOperationException();
    }

    public void engineLoad(KeyStore.LoadStoreParameter loadStoreParameter) throws NoSuchAlgorithmException, IOException, CertificateException {
        char[] password;
        if (loadStoreParameter == null) {
            engineLoad((InputStream) null, (char[]) null);
            return;
        }
        if (loadStoreParameter instanceof KeyStore.SimpleLoadStoreParameter) {
            KeyStore.ProtectionParameter protectionParameter = loadStoreParameter.getProtectionParameter();
            if (protectionParameter instanceof KeyStore.PasswordProtection) {
                password = ((KeyStore.PasswordProtection) protectionParameter).getPassword();
            } else if (protectionParameter instanceof KeyStore.CallbackHandlerProtection) {
                CallbackHandler callbackHandler = ((KeyStore.CallbackHandlerProtection) protectionParameter).getCallbackHandler();
                PasswordCallback passwordCallback = new PasswordCallback("Password: ", false);
                try {
                    callbackHandler.handle(new Callback[]{passwordCallback});
                    password = passwordCallback.getPassword();
                    passwordCallback.clearPassword();
                    if (password == null) {
                        throw new NoSuchAlgorithmException("No password provided");
                    }
                } catch (UnsupportedCallbackException e2) {
                    throw new NoSuchAlgorithmException("Could not obtain password", e2);
                }
            } else {
                throw new NoSuchAlgorithmException("ProtectionParameter must be PasswordProtection or CallbackHandlerProtection");
            }
            engineLoad(null, password);
            return;
        }
        throw new UnsupportedOperationException();
    }

    public KeyStore.Entry engineGetEntry(String str, KeyStore.ProtectionParameter protectionParameter) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableEntryException {
        if (!engineContainsAlias(str)) {
            return null;
        }
        if (protectionParameter == null) {
            if (engineIsCertificateEntry(str)) {
                return new KeyStore.TrustedCertificateEntry(engineGetCertificate(str));
            }
            throw new UnrecoverableKeyException("requested entry requires a password");
        }
        if (protectionParameter instanceof KeyStore.PasswordProtection) {
            if (engineIsCertificateEntry(str)) {
                throw new UnsupportedOperationException("trusted certificate entries are not password-protected");
            }
            if (engineIsKeyEntry(str)) {
                Key keyEngineGetKey = engineGetKey(str, ((KeyStore.PasswordProtection) protectionParameter).getPassword());
                if (keyEngineGetKey instanceof PrivateKey) {
                    return new KeyStore.PrivateKeyEntry((PrivateKey) keyEngineGetKey, engineGetCertificateChain(str));
                }
                if (keyEngineGetKey instanceof SecretKey) {
                    return new KeyStore.SecretKeyEntry((SecretKey) keyEngineGetKey);
                }
            }
        }
        throw new UnsupportedOperationException();
    }

    public void engineSetEntry(String str, KeyStore.Entry entry, KeyStore.ProtectionParameter protectionParameter) throws KeyStoreException {
        if (protectionParameter != null && !(protectionParameter instanceof KeyStore.PasswordProtection)) {
            throw new KeyStoreException("unsupported protection parameter");
        }
        KeyStore.PasswordProtection passwordProtection = null;
        if (protectionParameter != null) {
            passwordProtection = (KeyStore.PasswordProtection) protectionParameter;
        }
        if (entry instanceof KeyStore.TrustedCertificateEntry) {
            if (protectionParameter != null && passwordProtection.getPassword() != null) {
                throw new KeyStoreException("trusted certificate entries are not password-protected");
            }
            engineSetCertificateEntry(str, ((KeyStore.TrustedCertificateEntry) entry).getTrustedCertificate());
            return;
        }
        if (entry instanceof KeyStore.PrivateKeyEntry) {
            if (passwordProtection == null || passwordProtection.getPassword() == null) {
                throw new KeyStoreException("non-null password required to create PrivateKeyEntry");
            }
            engineSetKeyEntry(str, ((KeyStore.PrivateKeyEntry) entry).getPrivateKey(), passwordProtection.getPassword(), ((KeyStore.PrivateKeyEntry) entry).getCertificateChain());
            return;
        }
        if (entry instanceof KeyStore.SecretKeyEntry) {
            if (passwordProtection == null || passwordProtection.getPassword() == null) {
                throw new KeyStoreException("non-null password required to create SecretKeyEntry");
            }
            engineSetKeyEntry(str, ((KeyStore.SecretKeyEntry) entry).getSecretKey(), passwordProtection.getPassword(), (java.security.cert.Certificate[]) null);
            return;
        }
        throw new KeyStoreException("unsupported entry type: " + entry.getClass().getName());
    }

    public boolean engineEntryInstanceOf(String str, Class<? extends KeyStore.Entry> cls) {
        if (cls == KeyStore.TrustedCertificateEntry.class) {
            return engineIsCertificateEntry(str);
        }
        return cls == KeyStore.PrivateKeyEntry.class ? engineIsKeyEntry(str) && engineGetCertificate(str) != null : cls == KeyStore.SecretKeyEntry.class && engineIsKeyEntry(str) && engineGetCertificate(str) == null;
    }
}
