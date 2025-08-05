package sun.security.pkcs11;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.x500.X500Principal;
import sun.security.pkcs11.Secmod;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.Constants;
import sun.security.pkcs11.wrapper.Functions;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import sun.security.rsa.RSAKeyFactory;
import sun.security.util.Debug;
import sun.security.util.DerValue;
import sun.security.util.ECUtil;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11KeyStore.class */
final class P11KeyStore extends KeyStoreSpi {
    private static final long NO_HANDLE = -1;
    private static final long FINDOBJECTS_MAX = 100;
    private static final String ALIAS_SEP = "/";
    private static final boolean NSS_TEST = false;
    private final Token token;
    private boolean writeDisabled = false;
    private HashMap<String, AliasInfo> aliasMap;
    private final boolean useSecmodTrust;
    private Secmod.TrustType nssTrustType;
    private static final CK_ATTRIBUTE ATTR_CLASS_CERT = new CK_ATTRIBUTE(0, 1);
    private static final CK_ATTRIBUTE ATTR_CLASS_PKEY = new CK_ATTRIBUTE(0, 3);
    private static final CK_ATTRIBUTE ATTR_CLASS_SKEY = new CK_ATTRIBUTE(0, 4);
    private static final CK_ATTRIBUTE ATTR_X509_CERT_TYPE = new CK_ATTRIBUTE(128, 0);
    private static final CK_ATTRIBUTE ATTR_TOKEN_TRUE = new CK_ATTRIBUTE(1L, true);
    private static CK_ATTRIBUTE ATTR_SKEY_TOKEN_TRUE = ATTR_TOKEN_TRUE;
    private static final CK_ATTRIBUTE ATTR_TRUSTED_TRUE = new CK_ATTRIBUTE(134L, true);
    private static final CK_ATTRIBUTE ATTR_PRIVATE_TRUE = new CK_ATTRIBUTE(2L, true);
    private static final Debug debug = Debug.getInstance("pkcs11keystore");
    private static boolean CKA_TRUSTED_SUPPORTED = true;
    private static final long[] LONG0 = new long[0];

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11KeyStore$AliasInfo.class */
    private static class AliasInfo {
        private CK_ATTRIBUTE type;
        private String label;
        private byte[] id;
        private boolean trusted;
        private X509Certificate cert;
        private X509Certificate[] chain;
        private boolean matched;

        public AliasInfo(String str) {
            this.type = null;
            this.label = null;
            this.id = null;
            this.trusted = false;
            this.cert = null;
            this.chain = null;
            this.matched = false;
            this.type = P11KeyStore.ATTR_CLASS_SKEY;
            this.label = str;
        }

        public AliasInfo(String str, byte[] bArr, boolean z2, X509Certificate x509Certificate) {
            this.type = null;
            this.label = null;
            this.id = null;
            this.trusted = false;
            this.cert = null;
            this.chain = null;
            this.matched = false;
            this.type = P11KeyStore.ATTR_CLASS_PKEY;
            this.label = str;
            this.id = bArr;
            this.trusted = z2;
            this.cert = x509Certificate;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (this.type != P11KeyStore.ATTR_CLASS_PKEY) {
                if (this.type != P11KeyStore.ATTR_CLASS_SKEY) {
                    if (this.type == P11KeyStore.ATTR_CLASS_CERT) {
                        sb.append("\ttype=[trusted cert]\n");
                    }
                } else {
                    sb.append("\ttype=[secret key]\n");
                }
            } else {
                sb.append("\ttype=[private key]\n");
            }
            sb.append("\tlabel=[" + this.label + "]\n");
            if (this.id == null) {
                sb.append("\tid=[null]\n");
            } else {
                sb.append("\tid=" + P11KeyStore.getID(this.id) + "\n");
            }
            sb.append("\ttrusted=[" + this.trusted + "]\n");
            sb.append("\tmatched=[" + this.matched + "]\n");
            if (this.cert == null) {
                sb.append("\tcert=[null]\n");
            } else {
                sb.append("\tcert=[\tsubject: " + ((Object) this.cert.getSubjectX500Principal()) + "\n\t\tissuer: " + ((Object) this.cert.getIssuerX500Principal()) + "\n\t\tserialNum: " + this.cert.getSerialNumber().toString() + "]");
            }
            return sb.toString();
        }
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11KeyStore$PasswordCallbackHandler.class */
    private static class PasswordCallbackHandler implements CallbackHandler {
        private char[] password;

        private PasswordCallbackHandler(char[] cArr) {
            if (cArr != null) {
                this.password = (char[]) cArr.clone();
            }
        }

        @Override // javax.security.auth.callback.CallbackHandler
        public void handle(Callback[] callbackArr) throws UnsupportedCallbackException, IOException {
            if (!(callbackArr[0] instanceof PasswordCallback)) {
                throw new UnsupportedCallbackException(callbackArr[0]);
            }
            ((PasswordCallback) callbackArr[0]).setPassword(this.password);
        }

        protected void finalize() throws Throwable {
            if (this.password != null) {
                Arrays.fill(this.password, ' ');
            }
            super.finalize();
        }
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11KeyStore$THandle.class */
    private static class THandle {
        private final long handle;
        private final CK_ATTRIBUTE type;

        private THandle(long j2, CK_ATTRIBUTE ck_attribute) {
            this.handle = j2;
            this.type = ck_attribute;
        }
    }

    P11KeyStore(Token token) {
        this.token = token;
        this.useSecmodTrust = token.provider.nssUseSecmodTrust;
    }

    @Override // java.security.KeyStoreSpi
    public synchronized Key engineGetKey(String str, char[] cArr) throws NoSuchAlgorithmException, UnrecoverableKeyException {
        this.token.ensureValid();
        if (cArr != null && !this.token.config.getKeyStoreCompatibilityMode()) {
            throw new NoSuchAlgorithmException("password must be null");
        }
        AliasInfo aliasInfo = this.aliasMap.get(str);
        if (aliasInfo == null || aliasInfo.type == ATTR_CLASS_CERT) {
            return null;
        }
        try {
            try {
                Session opSession = this.token.getOpSession();
                if (aliasInfo.type == ATTR_CLASS_PKEY) {
                    THandle tokenObject = getTokenObject(opSession, aliasInfo.type, aliasInfo.id, null);
                    if (tokenObject.type == ATTR_CLASS_PKEY) {
                        PrivateKey privateKeyLoadPkey = loadPkey(opSession, tokenObject.handle);
                        this.token.releaseSession(opSession);
                        return privateKeyLoadPkey;
                    }
                } else {
                    THandle tokenObject2 = getTokenObject(opSession, ATTR_CLASS_SKEY, null, str);
                    if (tokenObject2.type == ATTR_CLASS_SKEY) {
                        SecretKey secretKeyLoadSkey = loadSkey(opSession, tokenObject2.handle);
                        this.token.releaseSession(opSession);
                        return secretKeyLoadSkey;
                    }
                }
                this.token.releaseSession(opSession);
                return null;
            } catch (KeyStoreException | PKCS11Exception e2) {
                throw new ProviderException(e2);
            }
        } catch (Throwable th) {
            this.token.releaseSession(null);
            throw th;
        }
    }

    @Override // java.security.KeyStoreSpi
    public synchronized Certificate[] engineGetCertificateChain(String str) {
        this.token.ensureValid();
        AliasInfo aliasInfo = this.aliasMap.get(str);
        if (aliasInfo != null && aliasInfo.type == ATTR_CLASS_PKEY) {
            return aliasInfo.chain;
        }
        return null;
    }

    @Override // java.security.KeyStoreSpi
    public synchronized Certificate engineGetCertificate(String str) {
        this.token.ensureValid();
        AliasInfo aliasInfo = this.aliasMap.get(str);
        if (aliasInfo != null) {
            return aliasInfo.cert;
        }
        return null;
    }

    @Override // java.security.KeyStoreSpi
    public Date engineGetCreationDate(String str) {
        this.token.ensureValid();
        throw new ProviderException(new UnsupportedOperationException());
    }

    @Override // java.security.KeyStoreSpi
    public synchronized void engineSetKeyEntry(String str, Key key, char[] cArr, Certificate[] certificateArr) throws KeyStoreException {
        this.token.ensureValid();
        checkWrite();
        if (!(key instanceof PrivateKey) && !(key instanceof SecretKey)) {
            throw new KeyStoreException("key must be PrivateKey or SecretKey");
        }
        if ((key instanceof PrivateKey) && certificateArr == null) {
            throw new KeyStoreException("PrivateKey must be accompanied by non-null chain");
        }
        if ((key instanceof SecretKey) && certificateArr != null) {
            throw new KeyStoreException("SecretKey must be accompanied by null chain");
        }
        if (cArr != null && !this.token.config.getKeyStoreCompatibilityMode()) {
            throw new KeyStoreException("Password must be null");
        }
        KeyStore.Entry secretKeyEntry = null;
        try {
            if (key instanceof PrivateKey) {
                secretKeyEntry = new KeyStore.PrivateKeyEntry((PrivateKey) key, certificateArr);
            } else if (key instanceof SecretKey) {
                secretKeyEntry = new KeyStore.SecretKeyEntry((SecretKey) key);
            }
            engineSetEntry(str, secretKeyEntry, new KeyStore.PasswordProtection(cArr));
        } catch (IllegalArgumentException | NullPointerException e2) {
            throw new KeyStoreException(e2);
        }
    }

    @Override // java.security.KeyStoreSpi
    public void engineSetKeyEntry(String str, byte[] bArr, Certificate[] certificateArr) throws KeyStoreException {
        this.token.ensureValid();
        throw new ProviderException(new UnsupportedOperationException());
    }

    @Override // java.security.KeyStoreSpi
    public synchronized void engineSetCertificateEntry(String str, Certificate certificate) throws KeyStoreException {
        this.token.ensureValid();
        checkWrite();
        if (certificate == null) {
            throw new KeyStoreException("invalid null certificate");
        }
        engineSetEntry(str, new KeyStore.TrustedCertificateEntry(certificate), null);
    }

    @Override // java.security.KeyStoreSpi
    public synchronized void engineDeleteEntry(String str) throws KeyStoreException {
        this.token.ensureValid();
        if (this.token.isWriteProtected()) {
            throw new KeyStoreException("token write-protected");
        }
        checkWrite();
        deleteEntry(str);
    }

    private boolean deleteEntry(String str) throws KeyStoreException {
        AliasInfo aliasInfo = this.aliasMap.get(str);
        if (aliasInfo != null) {
            this.aliasMap.remove(str);
            try {
                if (aliasInfo.type != ATTR_CLASS_CERT) {
                    if (aliasInfo.type == ATTR_CLASS_PKEY) {
                        return destroyPkey(aliasInfo.id) && destroyChain(aliasInfo.id);
                    }
                    if (aliasInfo.type == ATTR_CLASS_SKEY) {
                        return destroySkey(str);
                    }
                    throw new KeyStoreException("unexpected entry type");
                }
                return destroyCert(aliasInfo.id);
            } catch (CertificateException | PKCS11Exception e2) {
                throw new KeyStoreException(e2);
            }
        }
        return false;
    }

    @Override // java.security.KeyStoreSpi
    public synchronized Enumeration<String> engineAliases() {
        this.token.ensureValid();
        return Collections.enumeration(new HashSet(this.aliasMap.keySet()));
    }

    @Override // java.security.KeyStoreSpi
    public synchronized boolean engineContainsAlias(String str) {
        this.token.ensureValid();
        return this.aliasMap.containsKey(str);
    }

    @Override // java.security.KeyStoreSpi
    public synchronized int engineSize() {
        this.token.ensureValid();
        return this.aliasMap.size();
    }

    @Override // java.security.KeyStoreSpi
    public synchronized boolean engineIsKeyEntry(String str) {
        this.token.ensureValid();
        AliasInfo aliasInfo = this.aliasMap.get(str);
        if (aliasInfo == null || aliasInfo.type == ATTR_CLASS_CERT) {
            return false;
        }
        return true;
    }

    @Override // java.security.KeyStoreSpi
    public synchronized boolean engineIsCertificateEntry(String str) {
        this.token.ensureValid();
        AliasInfo aliasInfo = this.aliasMap.get(str);
        if (aliasInfo == null || aliasInfo.type != ATTR_CLASS_CERT) {
            return false;
        }
        return true;
    }

    @Override // java.security.KeyStoreSpi
    public synchronized String engineGetCertificateAlias(Certificate certificate) {
        this.token.ensureValid();
        Enumeration<String> enumerationEngineAliases = engineAliases();
        while (enumerationEngineAliases.hasMoreElements()) {
            String strNextElement2 = enumerationEngineAliases.nextElement2();
            Certificate certificateEngineGetCertificate = engineGetCertificate(strNextElement2);
            if (certificateEngineGetCertificate != null && certificateEngineGetCertificate.equals(certificate)) {
                return strNextElement2;
            }
        }
        return null;
    }

    @Override // java.security.KeyStoreSpi
    public synchronized void engineStore(OutputStream outputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
        this.token.ensureValid();
        if (outputStream != null && !this.token.config.getKeyStoreCompatibilityMode()) {
            throw new IOException("output stream must be null");
        }
        if (cArr != null && !this.token.config.getKeyStoreCompatibilityMode()) {
            throw new IOException("password must be null");
        }
    }

    @Override // java.security.KeyStoreSpi
    public synchronized void engineStore(KeyStore.LoadStoreParameter loadStoreParameter) throws NoSuchAlgorithmException, IOException, CertificateException {
        this.token.ensureValid();
        if (loadStoreParameter != null) {
            throw new IllegalArgumentException("LoadStoreParameter must be null");
        }
    }

    @Override // java.security.KeyStoreSpi
    public synchronized void engineLoad(InputStream inputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
        this.token.ensureValid();
        if (inputStream != null && !this.token.config.getKeyStoreCompatibilityMode()) {
            throw new IOException("input stream must be null");
        }
        if (this.useSecmodTrust) {
            this.nssTrustType = Secmod.TrustType.ALL;
        }
        try {
            if (cArr == null) {
                login(null);
            } else {
                login(new PasswordCallbackHandler(cArr));
            }
            try {
                if (mapLabels()) {
                    this.writeDisabled = true;
                }
                if (debug != null) {
                    dumpTokenMap();
                    debug.println("P11KeyStore load. Entry count: " + this.aliasMap.size());
                }
            } catch (KeyStoreException | PKCS11Exception e2) {
                throw new IOException("load failed", e2);
            }
        } catch (LoginException e3) {
            Throwable cause = e3.getCause();
            if ((cause instanceof PKCS11Exception) && ((PKCS11Exception) cause).getErrorCode() == 160) {
                throw new IOException("load failed", new UnrecoverableKeyException().initCause(e3));
            }
            throw new IOException("load failed", e3);
        }
    }

    @Override // java.security.KeyStoreSpi
    public synchronized void engineLoad(KeyStore.LoadStoreParameter loadStoreParameter) throws NoSuchAlgorithmException, IOException, CertificateException {
        CallbackHandler callbackHandler;
        this.token.ensureValid();
        if (loadStoreParameter == null) {
            throw new IllegalArgumentException("invalid null LoadStoreParameter");
        }
        if (this.useSecmodTrust) {
            if (loadStoreParameter instanceof Secmod.KeyStoreLoadParameter) {
                this.nssTrustType = ((Secmod.KeyStoreLoadParameter) loadStoreParameter).getTrustType();
            } else {
                this.nssTrustType = Secmod.TrustType.ALL;
            }
        }
        KeyStore.ProtectionParameter protectionParameter = loadStoreParameter.getProtectionParameter();
        if (protectionParameter instanceof KeyStore.PasswordProtection) {
            char[] password = ((KeyStore.PasswordProtection) protectionParameter).getPassword();
            if (password == null) {
                callbackHandler = null;
            } else {
                callbackHandler = new PasswordCallbackHandler(password);
            }
        } else if (protectionParameter instanceof KeyStore.CallbackHandlerProtection) {
            callbackHandler = ((KeyStore.CallbackHandlerProtection) protectionParameter).getCallbackHandler();
        } else {
            throw new IllegalArgumentException("ProtectionParameter must be either PasswordProtection or CallbackHandlerProtection");
        }
        try {
            login(callbackHandler);
            if (mapLabels()) {
                this.writeDisabled = true;
            }
            if (debug != null) {
                dumpTokenMap();
            }
        } catch (KeyStoreException | LoginException | PKCS11Exception e2) {
            throw new IOException("load failed", e2);
        }
    }

    private void login(CallbackHandler callbackHandler) throws LoginException {
        if ((this.token.tokenInfo.flags & 256) == 0) {
            this.token.provider.login(null, callbackHandler);
        } else {
            if (callbackHandler != null && !this.token.config.getKeyStoreCompatibilityMode()) {
                throw new LoginException("can not specify password if token supports protected authentication path");
            }
            this.token.provider.login(null, null);
        }
    }

    @Override // java.security.KeyStoreSpi
    public synchronized KeyStore.Entry engineGetEntry(String str, KeyStore.ProtectionParameter protectionParameter) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableEntryException {
        this.token.ensureValid();
        if (protectionParameter != null && (protectionParameter instanceof KeyStore.PasswordProtection) && ((KeyStore.PasswordProtection) protectionParameter).getPassword() != null && !this.token.config.getKeyStoreCompatibilityMode()) {
            throw new KeyStoreException("ProtectionParameter must be null");
        }
        AliasInfo aliasInfo = this.aliasMap.get(str);
        if (aliasInfo == null) {
            if (debug == null) {
                return null;
            }
            debug.println("engineGetEntry did not find alias [" + str + "] in map");
            return null;
        }
        try {
            try {
                Session opSession = this.token.getOpSession();
                if (aliasInfo.type == ATTR_CLASS_CERT) {
                    if (debug != null) {
                        debug.println("engineGetEntry found trusted cert entry");
                    }
                    KeyStore.TrustedCertificateEntry trustedCertificateEntry = new KeyStore.TrustedCertificateEntry(aliasInfo.cert);
                    this.token.releaseSession(opSession);
                    return trustedCertificateEntry;
                }
                if (aliasInfo.type == ATTR_CLASS_SKEY) {
                    if (debug != null) {
                        debug.println("engineGetEntry found secret key entry");
                    }
                    THandle tokenObject = getTokenObject(opSession, ATTR_CLASS_SKEY, null, aliasInfo.label);
                    if (tokenObject.type != ATTR_CLASS_SKEY) {
                        throw new KeyStoreException("expected but could not find secret key");
                    }
                    KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(loadSkey(opSession, tokenObject.handle));
                    this.token.releaseSession(opSession);
                    return secretKeyEntry;
                }
                if (debug != null) {
                    debug.println("engineGetEntry found private key entry");
                }
                THandle tokenObject2 = getTokenObject(opSession, ATTR_CLASS_PKEY, aliasInfo.id, null);
                if (tokenObject2.type != ATTR_CLASS_PKEY) {
                    throw new KeyStoreException("expected but could not find private key");
                }
                PrivateKey privateKeyLoadPkey = loadPkey(opSession, tokenObject2.handle);
                X509Certificate[] x509CertificateArr = aliasInfo.chain;
                if (privateKeyLoadPkey != null && x509CertificateArr != null) {
                    KeyStore.PrivateKeyEntry privateKeyEntry = new KeyStore.PrivateKeyEntry(privateKeyLoadPkey, x509CertificateArr);
                    this.token.releaseSession(opSession);
                    return privateKeyEntry;
                }
                if (debug != null) {
                    debug.println("engineGetEntry got null cert chain or private key");
                }
                this.token.releaseSession(opSession);
                return null;
            } catch (PKCS11Exception e2) {
                throw new KeyStoreException(e2);
            }
        } catch (Throwable th) {
            this.token.releaseSession(null);
            throw th;
        }
    }

    @Override // java.security.KeyStoreSpi
    public synchronized void engineSetEntry(String str, KeyStore.Entry entry, KeyStore.ProtectionParameter protectionParameter) throws KeyStoreException {
        this.token.ensureValid();
        checkWrite();
        if (protectionParameter != null && (protectionParameter instanceof KeyStore.PasswordProtection) && ((KeyStore.PasswordProtection) protectionParameter).getPassword() != null && !this.token.config.getKeyStoreCompatibilityMode()) {
            throw new KeyStoreException(new UnsupportedOperationException("ProtectionParameter must be null"));
        }
        if (this.token.isWriteProtected()) {
            throw new KeyStoreException("token write-protected");
        }
        if (entry instanceof KeyStore.TrustedCertificateEntry) {
            if (!this.useSecmodTrust) {
                throw new KeyStoreException(new UnsupportedOperationException("trusted certificates may only be set by token initialization application"));
            }
            Secmod.Module module = this.token.provider.nssModule;
            if (module.type != Secmod.ModuleType.KEYSTORE && module.type != Secmod.ModuleType.FIPS) {
                throw new KeyStoreException("Trusted certificates can only be added to the NSS KeyStore module");
            }
            Certificate trustedCertificate = ((KeyStore.TrustedCertificateEntry) entry).getTrustedCertificate();
            if (!(trustedCertificate instanceof X509Certificate)) {
                throw new KeyStoreException("Certificate must be an X509Certificate");
            }
            X509Certificate x509Certificate = (X509Certificate) trustedCertificate;
            if (this.aliasMap.get(str) != null) {
                deleteEntry(str);
            }
            try {
                storeCert(str, x509Certificate);
                module.setTrust(this.token, x509Certificate);
                mapLabels();
            } catch (CertificateException | PKCS11Exception e2) {
                throw new KeyStoreException(e2);
            }
        } else {
            if (entry instanceof KeyStore.PrivateKeyEntry) {
                PrivateKey privateKey = ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
                if (!(privateKey instanceof P11Key) && !(privateKey instanceof RSAPrivateKey) && !(privateKey instanceof DSAPrivateKey) && !(privateKey instanceof DHPrivateKey) && !(privateKey instanceof ECPrivateKey)) {
                    throw new KeyStoreException("unsupported key type: " + privateKey.getClass().getName());
                }
                Certificate[] certificateChain = ((KeyStore.PrivateKeyEntry) entry).getCertificateChain();
                if (!(certificateChain instanceof X509Certificate[])) {
                    throw new KeyStoreException(new UnsupportedOperationException("unsupported certificate array type: " + certificateChain.getClass().getName()));
                }
                try {
                    boolean z2 = false;
                    Iterator<String> it = this.aliasMap.keySet().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        AliasInfo aliasInfo = this.aliasMap.get(it.next());
                        if (aliasInfo.type == ATTR_CLASS_PKEY && aliasInfo.cert.getPublicKey().equals(certificateChain[0].getPublicKey())) {
                            updatePkey(str, aliasInfo.id, (X509Certificate[]) certificateChain, !aliasInfo.cert.equals(certificateChain[0]));
                            z2 = true;
                        }
                    }
                    if (!z2) {
                        engineDeleteEntry(str);
                        storePkey(str, (KeyStore.PrivateKeyEntry) entry);
                    }
                } catch (CertificateException | PKCS11Exception e3) {
                    throw new KeyStoreException(e3);
                }
            } else if (entry instanceof KeyStore.SecretKeyEntry) {
                KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) entry;
                secretKeyEntry.getSecretKey();
                try {
                    if (this.aliasMap.get(str) != null) {
                        engineDeleteEntry(str);
                    }
                    storeSkey(str, secretKeyEntry);
                } catch (PKCS11Exception e4) {
                    throw new KeyStoreException(e4);
                }
            } else {
                throw new KeyStoreException(new UnsupportedOperationException("unsupported entry type: " + entry.getClass().getName()));
            }
            try {
                mapLabels();
                if (debug != null) {
                    dumpTokenMap();
                }
            } catch (CertificateException | PKCS11Exception e5) {
                throw new KeyStoreException(e5);
            }
        }
        if (debug != null) {
            debug.println("engineSetEntry added new entry for [" + str + "] to token");
        }
    }

    @Override // java.security.KeyStoreSpi
    public synchronized boolean engineEntryInstanceOf(String str, Class<? extends KeyStore.Entry> cls) {
        this.token.ensureValid();
        return super.engineEntryInstanceOf(str, cls);
    }

    private X509Certificate loadCert(Session session, long j2) throws PKCS11Exception, CertificateException {
        CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(17L)};
        this.token.p11.C_GetAttributeValue(session.id(), j2, ck_attributeArr);
        byte[] byteArray = ck_attributeArr[0].getByteArray();
        if (byteArray == null) {
            throw new CertificateException("unexpectedly retrieved null byte array");
        }
        return (X509Certificate) CertificateFactory.getInstance(XMLX509Certificate.JCA_CERT_ID).generateCertificate(new ByteArrayInputStream(byteArray));
    }

    private X509Certificate[] loadChain(Session session, X509Certificate x509Certificate) throws PKCS11Exception, CertificateException {
        if (x509Certificate.getSubjectX500Principal().equals(x509Certificate.getIssuerX500Principal())) {
            return new X509Certificate[]{x509Certificate};
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(x509Certificate);
        X509Certificate x509CertificateLoadCert = x509Certificate;
        do {
            long[] jArrFindObjects = findObjects(session, new CK_ATTRIBUTE[]{ATTR_TOKEN_TRUE, ATTR_CLASS_CERT, new CK_ATTRIBUTE(257L, x509CertificateLoadCert.getIssuerX500Principal().getEncoded())});
            if (jArrFindObjects == null || jArrFindObjects.length == 0) {
                break;
            }
            if (debug != null && jArrFindObjects.length > 1) {
                debug.println("engineGetEntry found " + jArrFindObjects.length + " certificate entries for subject [" + x509CertificateLoadCert.getIssuerX500Principal().toString() + "] in token - using first entry");
            }
            x509CertificateLoadCert = loadCert(session, jArrFindObjects[0]);
            arrayList.add(x509CertificateLoadCert);
        } while (!x509CertificateLoadCert.getSubjectX500Principal().equals(x509CertificateLoadCert.getIssuerX500Principal()));
        return (X509Certificate[]) arrayList.toArray(new X509Certificate[arrayList.size()]);
    }

    private SecretKey loadSkey(Session session, long j2) throws PKCS11Exception {
        CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(256L)};
        this.token.p11.C_GetAttributeValue(session.id(), j2, ck_attributeArr);
        long j3 = ck_attributeArr[0].getLong();
        String str = null;
        int i2 = -1;
        if (j3 != 19 && j3 != 21) {
            if (j3 == 31) {
                str = "AES";
            } else if (j3 == 32) {
                str = "Blowfish";
            } else if (j3 == 18) {
                str = "ARCFOUR";
            } else {
                if (debug != null) {
                    debug.println("unknown key type [" + j3 + "] - using 'Generic Secret'");
                }
                str = "Generic Secret";
            }
            CK_ATTRIBUTE[] ck_attributeArr2 = {new CK_ATTRIBUTE(353L)};
            this.token.p11.C_GetAttributeValue(session.id(), j2, ck_attributeArr2);
            i2 = (int) ck_attributeArr2[0].getLong();
        } else if (j3 == 19) {
            str = "DES";
            i2 = 64;
        } else if (j3 == 21) {
            str = "DESede";
            i2 = 192;
        }
        return P11Key.secretKey(session, j2, str, i2, null);
    }

    private PrivateKey loadPkey(Session session, long j2) throws PKCS11Exception, KeyStoreException {
        CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(256L)};
        this.token.p11.C_GetAttributeValue(session.id(), j2, ck_attributeArr);
        long j3 = ck_attributeArr[0].getLong();
        if (j3 == 0) {
            CK_ATTRIBUTE[] ck_attributeArr2 = {new CK_ATTRIBUTE(288L)};
            this.token.p11.C_GetAttributeValue(session.id(), j2, ck_attributeArr2);
            int iBitLength = ck_attributeArr2[0].getBigInteger().bitLength();
            try {
                RSAKeyFactory.checkKeyLengths(iBitLength, null, -1, Integer.MAX_VALUE);
                return P11Key.privateKey(session, j2, "RSA", iBitLength, null);
            } catch (InvalidKeyException e2) {
                throw new KeyStoreException(e2.getMessage());
            }
        }
        if (j3 == 1) {
            CK_ATTRIBUTE[] ck_attributeArr3 = {new CK_ATTRIBUTE(304L)};
            this.token.p11.C_GetAttributeValue(session.id(), j2, ck_attributeArr3);
            return P11Key.privateKey(session, j2, "DSA", ck_attributeArr3[0].getBigInteger().bitLength(), null);
        }
        if (j3 == 2) {
            CK_ATTRIBUTE[] ck_attributeArr4 = {new CK_ATTRIBUTE(304L)};
            this.token.p11.C_GetAttributeValue(session.id(), j2, ck_attributeArr4);
            return P11Key.privateKey(session, j2, "DH", ck_attributeArr4[0].getBigInteger().bitLength(), null);
        }
        if (j3 == 3) {
            CK_ATTRIBUTE[] ck_attributeArr5 = {new CK_ATTRIBUTE(384L)};
            this.token.p11.C_GetAttributeValue(session.id(), j2, ck_attributeArr5);
            try {
                return P11Key.privateKey(session, j2, "EC", ECUtil.getECParameterSpec((Provider) null, ck_attributeArr5[0].getByteArray()).getCurve().getField().getFieldSize(), null);
            } catch (IOException e3) {
                throw new KeyStoreException("Unsupported parameters", e3);
            }
        }
        if (debug != null) {
            debug.println("unknown key type [" + j3 + "]");
        }
        throw new KeyStoreException("unknown key type");
    }

    private void updatePkey(String str, byte[] bArr, X509Certificate[] x509CertificateArr, boolean z2) throws PKCS11Exception, KeyStoreException, CertificateException {
        try {
            Session opSession = this.token.getOpSession();
            THandle tokenObject = getTokenObject(opSession, ATTR_CLASS_PKEY, bArr, null);
            if (tokenObject.type == ATTR_CLASS_PKEY) {
                long j2 = tokenObject.handle;
                THandle tokenObject2 = getTokenObject(opSession, ATTR_CLASS_CERT, bArr, null);
                if (tokenObject2.type != ATTR_CLASS_CERT) {
                    throw new KeyStoreException("expected but could not find certificate with CKA_ID " + getID(bArr));
                }
                if (1 == 0) {
                    this.token.p11.C_SetAttributeValue(opSession.id(), tokenObject2.handle, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(3L, str), new CK_ATTRIBUTE(258L, str)});
                } else {
                    destroyChain(bArr);
                }
                if (1 != 0) {
                    storeChain(str, x509CertificateArr);
                } else {
                    storeCaCerts(x509CertificateArr, 1);
                }
                this.token.p11.C_SetAttributeValue(opSession.id(), j2, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(258L, str)});
                if (debug != null) {
                    debug.println("updatePkey set new alias [" + str + "] for private key entry");
                }
                this.token.releaseSession(opSession);
                return;
            }
            throw new KeyStoreException("expected but could not find private key with CKA_ID " + getID(bArr));
        } catch (Throwable th) {
            this.token.releaseSession(null);
            throw th;
        }
    }

    private void updateP11Pkey(String str, CK_ATTRIBUTE ck_attribute, P11Key p11Key) throws PKCS11Exception {
        long keyID = p11Key.getKeyID();
        try {
            Session opSession = this.token.getOpSession();
            if (p11Key.tokenObject) {
                this.token.p11.C_SetAttributeValue(opSession.id(), keyID, new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(258L, str)});
                if (debug != null) {
                    debug.println("updateP11Pkey set new alias [" + str + "] for key entry");
                }
            } else {
                CK_ATTRIBUTE[] ck_attributeArrAddAttribute = {ATTR_TOKEN_TRUE, new CK_ATTRIBUTE(258L, str)};
                if (ck_attribute != null) {
                    ck_attributeArrAddAttribute = addAttribute(ck_attributeArrAddAttribute, ck_attribute);
                }
                this.token.p11.C_CopyObject(opSession.id(), keyID, ck_attributeArrAddAttribute);
                if (debug != null) {
                    debug.println("updateP11Pkey copied private session key for [" + str + "] to token entry");
                }
            }
            this.token.releaseSession(opSession);
            p11Key.releaseKeyID();
        } catch (Throwable th) {
            this.token.releaseSession(null);
            p11Key.releaseKeyID();
            throw th;
        }
    }

    private void storeCert(String str, X509Certificate x509Certificate) throws PKCS11Exception, CertificateException {
        ArrayList arrayList = new ArrayList();
        arrayList.add(ATTR_TOKEN_TRUE);
        arrayList.add(ATTR_CLASS_CERT);
        arrayList.add(ATTR_X509_CERT_TYPE);
        arrayList.add(new CK_ATTRIBUTE(257L, x509Certificate.getSubjectX500Principal().getEncoded()));
        arrayList.add(new CK_ATTRIBUTE(129L, x509Certificate.getIssuerX500Principal().getEncoded()));
        arrayList.add(new CK_ATTRIBUTE(130L, x509Certificate.getSerialNumber().toByteArray()));
        arrayList.add(new CK_ATTRIBUTE(17L, x509Certificate.getEncoded()));
        if (str != null) {
            arrayList.add(new CK_ATTRIBUTE(3L, str));
            arrayList.add(new CK_ATTRIBUTE(258L, str));
        } else {
            arrayList.add(new CK_ATTRIBUTE(258L, getID(x509Certificate.getSubjectX500Principal().getName(X500Principal.CANONICAL), x509Certificate)));
        }
        Session opSession = null;
        try {
            opSession = this.token.getOpSession();
            this.token.p11.C_CreateObject(opSession.id(), (CK_ATTRIBUTE[]) arrayList.toArray(new CK_ATTRIBUTE[arrayList.size()]));
            this.token.releaseSession(opSession);
        } catch (Throwable th) {
            this.token.releaseSession(opSession);
            throw th;
        }
    }

    private void storeChain(String str, X509Certificate[] x509CertificateArr) throws PKCS11Exception, CertificateException {
        storeCert(str, x509CertificateArr[0]);
        storeCaCerts(x509CertificateArr, 1);
    }

    private void storeCaCerts(X509Certificate[] x509CertificateArr, int i2) throws PKCS11Exception, CertificateException {
        Session opSession = null;
        HashSet hashSet = new HashSet();
        try {
            opSession = this.token.getOpSession();
            for (long j2 : findObjects(opSession, new CK_ATTRIBUTE[]{ATTR_TOKEN_TRUE, ATTR_CLASS_CERT})) {
                hashSet.add(loadCert(opSession, j2));
            }
            this.token.releaseSession(opSession);
            for (int i3 = i2; i3 < x509CertificateArr.length; i3++) {
                if (!hashSet.contains(x509CertificateArr[i3])) {
                    storeCert(null, x509CertificateArr[i3]);
                } else if (debug != null) {
                    debug.println("ignoring duplicate CA cert for [" + ((Object) x509CertificateArr[i3].getSubjectX500Principal()) + "]");
                }
            }
        } catch (Throwable th) {
            this.token.releaseSession(opSession);
            throw th;
        }
    }

    private void storeSkey(String str, KeyStore.SecretKeyEntry secretKeyEntry) throws PKCS11Exception, KeyStoreException {
        try {
            P11SecretKeyFactory.convertKey(this.token, secretKeyEntry.getSecretKey(), null, new CK_ATTRIBUTE[]{ATTR_SKEY_TOKEN_TRUE, ATTR_PRIVATE_TRUE, new CK_ATTRIBUTE(3L, str)});
            this.aliasMap.put(str, new AliasInfo(str));
            if (debug != null) {
                debug.println("storeSkey created token secret key for [" + str + "]");
            }
        } catch (InvalidKeyException e2) {
            throw new KeyStoreException("Cannot convert to PKCS11 keys", e2);
        }
    }

    private static CK_ATTRIBUTE[] addAttribute(CK_ATTRIBUTE[] ck_attributeArr, CK_ATTRIBUTE ck_attribute) {
        int length = ck_attributeArr.length;
        CK_ATTRIBUTE[] ck_attributeArr2 = new CK_ATTRIBUTE[length + 1];
        System.arraycopy(ck_attributeArr, 0, ck_attributeArr2, 0, length);
        ck_attributeArr2[length] = ck_attribute;
        return ck_attributeArr2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void storePkey(String str, KeyStore.PrivateKeyEntry privateKeyEntry) throws PKCS11Exception, CertificateException, KeyStoreException {
        CK_ATTRIBUTE[] attributes;
        PrivateKey privateKey = privateKeyEntry.getPrivateKey();
        if (privateKey instanceof P11Key) {
            P11Key p11Key = (P11Key) privateKey;
            if (p11Key.tokenObject && p11Key.token == this.token) {
                updateP11Pkey(str, null, p11Key);
                storeChain(str, (X509Certificate[]) privateKeyEntry.getCertificateChain());
                return;
            }
        }
        boolean nssNetscapeDbWorkaround = this.token.config.getNssNetscapeDbWorkaround();
        PublicKey publicKey = privateKeyEntry.getCertificate().getPublicKey();
        if (privateKey instanceof RSAPrivateKey) {
            attributes = getRsaPrivKeyAttrs(str, (RSAPrivateKey) privateKey, ((X509Certificate) privateKeyEntry.getCertificate()).getSubjectX500Principal());
        } else if (privateKey instanceof DSAPrivateKey) {
            DSAPrivateKey dSAPrivateKey = (DSAPrivateKey) privateKey;
            CK_ATTRIBUTE[] idAttributes = getIdAttributes(privateKey, publicKey, false, nssNetscapeDbWorkaround);
            if (idAttributes[0] == null) {
                idAttributes[0] = new CK_ATTRIBUTE(258L, str);
            }
            CK_ATTRIBUTE[] ck_attributeArrAddAttribute = {ATTR_TOKEN_TRUE, ATTR_CLASS_PKEY, ATTR_PRIVATE_TRUE, new CK_ATTRIBUTE(256L, 1L), idAttributes[0], new CK_ATTRIBUTE(304L, dSAPrivateKey.getParams().getP()), new CK_ATTRIBUTE(305L, dSAPrivateKey.getParams().getQ()), new CK_ATTRIBUTE(306L, dSAPrivateKey.getParams().getG()), new CK_ATTRIBUTE(17L, dSAPrivateKey.getX())};
            if (idAttributes[1] != null) {
                ck_attributeArrAddAttribute = addAttribute(ck_attributeArrAddAttribute, idAttributes[1]);
            }
            attributes = this.token.getAttributes("import", 3L, 1L, ck_attributeArrAddAttribute);
            if (debug != null) {
                debug.println("storePkey created DSA template");
            }
        } else if (privateKey instanceof DHPrivateKey) {
            DHPrivateKey dHPrivateKey = (DHPrivateKey) privateKey;
            CK_ATTRIBUTE[] idAttributes2 = getIdAttributes(privateKey, publicKey, false, nssNetscapeDbWorkaround);
            if (idAttributes2[0] == null) {
                idAttributes2[0] = new CK_ATTRIBUTE(258L, str);
            }
            CK_ATTRIBUTE[] ck_attributeArrAddAttribute2 = {ATTR_TOKEN_TRUE, ATTR_CLASS_PKEY, ATTR_PRIVATE_TRUE, new CK_ATTRIBUTE(256L, 2L), idAttributes2[0], new CK_ATTRIBUTE(304L, dHPrivateKey.getParams().getP()), new CK_ATTRIBUTE(306L, dHPrivateKey.getParams().getG()), new CK_ATTRIBUTE(17L, dHPrivateKey.getX())};
            if (idAttributes2[1] != null) {
                ck_attributeArrAddAttribute2 = addAttribute(ck_attributeArrAddAttribute2, idAttributes2[1]);
            }
            attributes = this.token.getAttributes("import", 3L, 2L, ck_attributeArrAddAttribute2);
        } else {
            if (!(privateKey instanceof ECPrivateKey)) {
                if (privateKey instanceof P11Key) {
                    P11Key p11Key2 = (P11Key) privateKey;
                    if (p11Key2.token != this.token) {
                        throw new KeyStoreException("Cannot move sensitive keys across tokens");
                    }
                    CK_ATTRIBUTE ck_attribute = null;
                    if (nssNetscapeDbWorkaround) {
                        ck_attribute = getIdAttributes(privateKey, publicKey, false, true)[1];
                    }
                    updateP11Pkey(str, ck_attribute, p11Key2);
                    storeChain(str, (X509Certificate[]) privateKeyEntry.getCertificateChain());
                    return;
                }
                throw new KeyStoreException("unsupported key type: " + ((Object) privateKey));
            }
            ECPrivateKey eCPrivateKey = (ECPrivateKey) privateKey;
            CK_ATTRIBUTE[] idAttributes3 = getIdAttributes(privateKey, publicKey, false, nssNetscapeDbWorkaround);
            if (idAttributes3[0] == null) {
                idAttributes3[0] = new CK_ATTRIBUTE(258L, str);
            }
            CK_ATTRIBUTE[] ck_attributeArrAddAttribute3 = {ATTR_TOKEN_TRUE, ATTR_CLASS_PKEY, ATTR_PRIVATE_TRUE, new CK_ATTRIBUTE(256L, 3L), idAttributes3[0], new CK_ATTRIBUTE(17L, eCPrivateKey.getS()), new CK_ATTRIBUTE(384L, ECUtil.encodeECParameterSpec(null, eCPrivateKey.getParams()))};
            if (idAttributes3[1] != null) {
                ck_attributeArrAddAttribute3 = addAttribute(ck_attributeArrAddAttribute3, idAttributes3[1]);
            }
            attributes = this.token.getAttributes("import", 3L, 3L, ck_attributeArrAddAttribute3);
            if (debug != null) {
                debug.println("storePkey created EC template");
            }
        }
        Session opSession = null;
        try {
            opSession = this.token.getOpSession();
            this.token.p11.C_CreateObject(opSession.id(), attributes);
            if (debug != null) {
                debug.println("storePkey created token key for [" + str + "]");
            }
            this.token.releaseSession(opSession);
            storeChain(str, (X509Certificate[]) privateKeyEntry.getCertificateChain());
        } catch (Throwable th) {
            this.token.releaseSession(opSession);
            throw th;
        }
    }

    private CK_ATTRIBUTE[] getRsaPrivKeyAttrs(String str, RSAPrivateKey rSAPrivateKey, X500Principal x500Principal) throws PKCS11Exception {
        CK_ATTRIBUTE[] attributes;
        if (rSAPrivateKey instanceof RSAPrivateCrtKey) {
            if (debug != null) {
                debug.println("creating RSAPrivateCrtKey attrs");
            }
            RSAPrivateCrtKey rSAPrivateCrtKey = (RSAPrivateCrtKey) rSAPrivateKey;
            attributes = this.token.getAttributes("import", 3L, 0L, new CK_ATTRIBUTE[]{ATTR_TOKEN_TRUE, ATTR_CLASS_PKEY, ATTR_PRIVATE_TRUE, new CK_ATTRIBUTE(256L, 0L), new CK_ATTRIBUTE(258L, str), new CK_ATTRIBUTE(288L, rSAPrivateCrtKey.getModulus()), new CK_ATTRIBUTE(291L, rSAPrivateCrtKey.getPrivateExponent()), new CK_ATTRIBUTE(290L, rSAPrivateCrtKey.getPublicExponent()), new CK_ATTRIBUTE(292L, rSAPrivateCrtKey.getPrimeP()), new CK_ATTRIBUTE(293L, rSAPrivateCrtKey.getPrimeQ()), new CK_ATTRIBUTE(294L, rSAPrivateCrtKey.getPrimeExponentP()), new CK_ATTRIBUTE(295L, rSAPrivateCrtKey.getPrimeExponentQ()), new CK_ATTRIBUTE(296L, rSAPrivateCrtKey.getCrtCoefficient())});
        } else {
            if (debug != null) {
                debug.println("creating RSAPrivateKey attrs");
            }
            attributes = this.token.getAttributes("import", 3L, 0L, new CK_ATTRIBUTE[]{ATTR_TOKEN_TRUE, ATTR_CLASS_PKEY, ATTR_PRIVATE_TRUE, new CK_ATTRIBUTE(256L, 0L), new CK_ATTRIBUTE(258L, str), new CK_ATTRIBUTE(288L, rSAPrivateKey.getModulus()), new CK_ATTRIBUTE(291L, rSAPrivateKey.getPrivateExponent())});
        }
        return attributes;
    }

    private CK_ATTRIBUTE[] getIdAttributes(PrivateKey privateKey, PublicKey publicKey, boolean z2, boolean z3) {
        CK_ATTRIBUTE[] ck_attributeArr = new CK_ATTRIBUTE[2];
        if (!(z2 || z3)) {
            return ck_attributeArr;
        }
        String algorithm = privateKey.getAlgorithm();
        if (algorithm.equals("RSA") && (publicKey instanceof RSAPublicKey)) {
            if (z2) {
                ck_attributeArr[0] = new CK_ATTRIBUTE(258L, P11Util.sha1(P11Util.getMagnitude(((RSAPublicKey) publicKey).getModulus())));
            }
        } else if (algorithm.equals("DSA") && (publicKey instanceof DSAPublicKey)) {
            BigInteger y2 = ((DSAPublicKey) publicKey).getY();
            if (z2) {
                ck_attributeArr[0] = new CK_ATTRIBUTE(258L, P11Util.sha1(P11Util.getMagnitude(y2)));
            }
            if (z3) {
                ck_attributeArr[1] = new CK_ATTRIBUTE(PKCS11Constants.CKA_NETSCAPE_DB, y2);
            }
        } else if (algorithm.equals("DH") && (publicKey instanceof DHPublicKey)) {
            BigInteger y3 = ((DHPublicKey) publicKey).getY();
            if (z2) {
                ck_attributeArr[0] = new CK_ATTRIBUTE(258L, P11Util.sha1(P11Util.getMagnitude(y3)));
            }
            if (z3) {
                ck_attributeArr[1] = new CK_ATTRIBUTE(PKCS11Constants.CKA_NETSCAPE_DB, y3);
            }
        } else if (algorithm.equals("EC") && (publicKey instanceof ECPublicKey)) {
            ECPublicKey eCPublicKey = (ECPublicKey) publicKey;
            byte[] bArrEncodePoint = ECUtil.encodePoint(eCPublicKey.getW(), eCPublicKey.getParams().getCurve());
            if (z2) {
                ck_attributeArr[0] = new CK_ATTRIBUTE(258L, P11Util.sha1(bArrEncodePoint));
            }
            if (z3) {
                ck_attributeArr[1] = new CK_ATTRIBUTE(PKCS11Constants.CKA_NETSCAPE_DB, bArrEncodePoint);
            }
        } else {
            throw new RuntimeException("Unknown key algorithm " + algorithm);
        }
        return ck_attributeArr;
    }

    private boolean destroyCert(byte[] bArr) throws PKCS11Exception, KeyStoreException {
        Session opSession = null;
        try {
            opSession = this.token.getOpSession();
            THandle tokenObject = getTokenObject(opSession, ATTR_CLASS_CERT, bArr, null);
            if (tokenObject.type == ATTR_CLASS_CERT) {
                this.token.p11.C_DestroyObject(opSession.id(), tokenObject.handle);
                if (debug != null) {
                    debug.println("destroyCert destroyed cert with CKA_ID [" + getID(bArr) + "]");
                }
                this.token.releaseSession(opSession);
                return true;
            }
            this.token.releaseSession(opSession);
            return false;
        } catch (Throwable th) {
            this.token.releaseSession(opSession);
            throw th;
        }
    }

    private boolean destroyChain(byte[] bArr) throws PKCS11Exception, CertificateException, KeyStoreException {
        long[] jArrFindObjects;
        try {
            Session opSession = this.token.getOpSession();
            THandle tokenObject = getTokenObject(opSession, ATTR_CLASS_CERT, bArr, null);
            if (tokenObject.type != ATTR_CLASS_CERT) {
                if (debug != null) {
                    debug.println("destroyChain could not find end entity cert with CKA_ID [0x" + Functions.toHexString(bArr) + "]");
                }
                this.token.releaseSession(opSession);
                return false;
            }
            X509Certificate x509CertificateLoadCert = loadCert(opSession, tokenObject.handle);
            this.token.p11.C_DestroyObject(opSession.id(), tokenObject.handle);
            if (debug != null) {
                debug.println("destroyChain destroyed end entity cert with CKA_ID [" + getID(bArr) + "]");
            }
            X509Certificate x509CertificateLoadCert2 = x509CertificateLoadCert;
            while (!x509CertificateLoadCert2.getSubjectX500Principal().equals(x509CertificateLoadCert2.getIssuerX500Principal()) && (jArrFindObjects = findObjects(opSession, new CK_ATTRIBUTE[]{ATTR_TOKEN_TRUE, ATTR_CLASS_CERT, new CK_ATTRIBUTE(257L, x509CertificateLoadCert2.getIssuerX500Principal().getEncoded())})) != null && jArrFindObjects.length != 0) {
                if (debug != null && jArrFindObjects.length > 1) {
                    debug.println("destroyChain found " + jArrFindObjects.length + " certificate entries for subject [" + ((Object) x509CertificateLoadCert2.getIssuerX500Principal()) + "] in token - using first entry");
                }
                x509CertificateLoadCert2 = loadCert(opSession, jArrFindObjects[0]);
                long[] jArrFindObjects2 = findObjects(opSession, new CK_ATTRIBUTE[]{ATTR_TOKEN_TRUE, ATTR_CLASS_CERT, new CK_ATTRIBUTE(129L, x509CertificateLoadCert2.getSubjectX500Principal().getEncoded())});
                boolean z2 = false;
                if (jArrFindObjects2 == null || jArrFindObjects2.length == 0) {
                    z2 = true;
                } else if (jArrFindObjects2.length == 1 && x509CertificateLoadCert2.equals(loadCert(opSession, jArrFindObjects2[0]))) {
                    z2 = true;
                }
                if (z2) {
                    this.token.p11.C_DestroyObject(opSession.id(), jArrFindObjects[0]);
                    if (debug != null) {
                        debug.println("destroyChain destroyed cert in chain with subject [" + ((Object) x509CertificateLoadCert2.getSubjectX500Principal()) + "]");
                    }
                } else if (debug != null) {
                    debug.println("destroyChain did not destroy shared cert in chain with subject [" + ((Object) x509CertificateLoadCert2.getSubjectX500Principal()) + "]");
                }
            }
            this.token.releaseSession(opSession);
            return true;
        } catch (Throwable th) {
            this.token.releaseSession(null);
            throw th;
        }
    }

    private boolean destroySkey(String str) throws PKCS11Exception, KeyStoreException {
        try {
            Session opSession = this.token.getOpSession();
            THandle tokenObject = getTokenObject(opSession, ATTR_CLASS_SKEY, null, str);
            if (tokenObject.type == ATTR_CLASS_SKEY) {
                this.token.p11.C_DestroyObject(opSession.id(), tokenObject.handle);
                this.token.releaseSession(opSession);
                return true;
            }
            if (debug != null) {
                debug.println("destroySkey did not find secret key with CKA_LABEL [" + str + "]");
            }
            this.token.releaseSession(opSession);
            return false;
        } catch (Throwable th) {
            this.token.releaseSession(null);
            throw th;
        }
    }

    private boolean destroyPkey(byte[] bArr) throws PKCS11Exception, KeyStoreException {
        try {
            Session opSession = this.token.getOpSession();
            THandle tokenObject = getTokenObject(opSession, ATTR_CLASS_PKEY, bArr, null);
            if (tokenObject.type == ATTR_CLASS_PKEY) {
                this.token.p11.C_DestroyObject(opSession.id(), tokenObject.handle);
                this.token.releaseSession(opSession);
                return true;
            }
            if (debug != null) {
                debug.println("destroyPkey did not find private key with CKA_ID [" + getID(bArr) + "]");
            }
            this.token.releaseSession(opSession);
            return false;
        } catch (Throwable th) {
            this.token.releaseSession(null);
            throw th;
        }
    }

    private String getID(String str, X509Certificate x509Certificate) {
        return str + "/" + x509Certificate.getIssuerX500Principal().getName(X500Principal.CANONICAL) + "/" + x509Certificate.getSerialNumber().toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getID(byte[] bArr) {
        boolean z2 = true;
        int i2 = 0;
        while (true) {
            if (i2 >= bArr.length) {
                break;
            }
            if (DerValue.isPrintableStringChar((char) bArr[i2])) {
                i2++;
            } else {
                z2 = false;
                break;
            }
        }
        if (!z2) {
            return "0x" + Functions.toHexString(bArr);
        }
        try {
            return new String(bArr, "UTF-8");
        } catch (UnsupportedEncodingException e2) {
            return "0x" + Functions.toHexString(bArr);
        }
    }

    private THandle getTokenObject(Session session, CK_ATTRIBUTE ck_attribute, byte[] bArr, String str) throws PKCS11Exception, KeyStoreException {
        CK_ATTRIBUTE[] ck_attributeArr;
        if (ck_attribute == ATTR_CLASS_SKEY) {
            ck_attributeArr = new CK_ATTRIBUTE[]{ATTR_SKEY_TOKEN_TRUE, new CK_ATTRIBUTE(3L, str), ck_attribute};
        } else {
            ck_attributeArr = new CK_ATTRIBUTE[]{ATTR_TOKEN_TRUE, new CK_ATTRIBUTE(258L, bArr), ck_attribute};
        }
        long[] jArrFindObjects = findObjects(session, ck_attributeArr);
        if (jArrFindObjects.length == 0) {
            if (debug != null) {
                if (ck_attribute == ATTR_CLASS_SKEY) {
                    debug.println("getTokenObject did not find secret key with CKA_LABEL [" + str + "]");
                } else if (ck_attribute == ATTR_CLASS_CERT) {
                    debug.println("getTokenObject did not find cert with CKA_ID [" + getID(bArr) + "]");
                } else {
                    debug.println("getTokenObject did not find private key with CKA_ID [" + getID(bArr) + "]");
                }
            }
            return new THandle(-1L, null);
        }
        if (jArrFindObjects.length == 1) {
            return new THandle(jArrFindObjects[0], ck_attribute);
        }
        if (ck_attribute == ATTR_CLASS_SKEY) {
            ArrayList arrayList = new ArrayList(jArrFindObjects.length);
            for (int i2 = 0; i2 < jArrFindObjects.length; i2++) {
                CK_ATTRIBUTE[] ck_attributeArr2 = {new CK_ATTRIBUTE(3L)};
                this.token.p11.C_GetAttributeValue(session.id(), jArrFindObjects[i2], ck_attributeArr2);
                if (ck_attributeArr2[0].pValue != null && str.equals(new String(ck_attributeArr2[0].getCharArray()))) {
                    arrayList.add(new THandle(jArrFindObjects[i2], ATTR_CLASS_SKEY));
                }
            }
            if (arrayList.size() == 1) {
                return (THandle) arrayList.get(0);
            }
            throw new KeyStoreException("invalid KeyStore state: found " + arrayList.size() + " secret keys sharing CKA_LABEL [" + str + "]");
        }
        if (ck_attribute == ATTR_CLASS_CERT) {
            throw new KeyStoreException("invalid KeyStore state: found " + jArrFindObjects.length + " certificates sharing CKA_ID " + getID(bArr));
        }
        throw new KeyStoreException("invalid KeyStore state: found " + jArrFindObjects.length + " private keys sharing CKA_ID " + getID(bArr));
    }

    private boolean mapLabels() throws PKCS11Exception, CertificateException, KeyStoreException {
        CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(134L)};
        Session opSession = null;
        try {
            opSession = this.token.getOpSession();
            ArrayList<byte[]> arrayList = new ArrayList<>();
            for (long j2 : findObjects(opSession, new CK_ATTRIBUTE[]{ATTR_TOKEN_TRUE, ATTR_CLASS_PKEY})) {
                CK_ATTRIBUTE[] ck_attributeArr2 = {new CK_ATTRIBUTE(258L)};
                this.token.p11.C_GetAttributeValue(opSession.id(), j2, ck_attributeArr2);
                if (ck_attributeArr2[0].pValue != null) {
                    arrayList.add(ck_attributeArr2[0].getByteArray());
                }
            }
            HashMap<String, HashSet<AliasInfo>> map = new HashMap<>();
            for (long j3 : findObjects(opSession, new CK_ATTRIBUTE[]{ATTR_TOKEN_TRUE, ATTR_CLASS_CERT})) {
                CK_ATTRIBUTE[] ck_attributeArr3 = {new CK_ATTRIBUTE(3L)};
                String id = null;
                byte[] byteArray = null;
                try {
                    this.token.p11.C_GetAttributeValue(opSession.id(), j3, ck_attributeArr3);
                    if (ck_attributeArr3[0].pValue != null) {
                        id = new String(ck_attributeArr3[0].getCharArray());
                    }
                } catch (PKCS11Exception e2) {
                    if (e2.getErrorCode() != 18) {
                        throw e2;
                    }
                }
                CK_ATTRIBUTE[] ck_attributeArr4 = {new CK_ATTRIBUTE(258L)};
                this.token.p11.C_GetAttributeValue(opSession.id(), j3, ck_attributeArr4);
                if (ck_attributeArr4[0].pValue == null) {
                    if (id == null) {
                    }
                } else {
                    if (id == null) {
                        id = getID(ck_attributeArr4[0].getByteArray());
                    }
                    byteArray = ck_attributeArr4[0].getByteArray();
                }
                X509Certificate x509CertificateLoadCert = loadCert(opSession, j3);
                boolean zIsTrusted = false;
                if (this.useSecmodTrust) {
                    zIsTrusted = Secmod.getInstance().isTrusted(x509CertificateLoadCert, this.nssTrustType);
                } else if (CKA_TRUSTED_SUPPORTED) {
                    try {
                        this.token.p11.C_GetAttributeValue(opSession.id(), j3, ck_attributeArr);
                        zIsTrusted = ck_attributeArr[0].getBoolean();
                    } catch (PKCS11Exception e3) {
                        if (e3.getErrorCode() == 18) {
                            CKA_TRUSTED_SUPPORTED = false;
                            if (debug != null) {
                                debug.println("CKA_TRUSTED attribute not supported");
                            }
                        }
                    }
                }
                HashSet<AliasInfo> hashSet = map.get(id);
                if (hashSet == null) {
                    hashSet = new HashSet<>(2);
                    map.put(id, hashSet);
                }
                hashSet.add(new AliasInfo(id, byteArray, zIsTrusted, x509CertificateLoadCert));
            }
            HashMap<String, AliasInfo> map2 = new HashMap<>();
            for (long j4 : findObjects(opSession, new CK_ATTRIBUTE[]{ATTR_SKEY_TOKEN_TRUE, ATTR_CLASS_SKEY})) {
                CK_ATTRIBUTE[] ck_attributeArr5 = {new CK_ATTRIBUTE(3L)};
                this.token.p11.C_GetAttributeValue(opSession.id(), j4, ck_attributeArr5);
                if (ck_attributeArr5[0].pValue != null) {
                    String str = new String(ck_attributeArr5[0].getCharArray());
                    if (map2.get(str) == null) {
                        map2.put(str, new AliasInfo(str));
                    } else {
                        throw new KeyStoreException("invalid KeyStore state: found multiple secret keys sharing same CKA_LABEL [" + str + "]");
                    }
                }
            }
            boolean zMapCerts = mapCerts(mapPrivateKeys(arrayList, map), map);
            mapSecretKeys(map2);
            this.token.releaseSession(opSession);
            return zMapCerts;
        } catch (Throwable th) {
            this.token.releaseSession(opSession);
            throw th;
        }
    }

    private ArrayList<AliasInfo> mapPrivateKeys(ArrayList<byte[]> arrayList, HashMap<String, HashSet<AliasInfo>> map) throws PKCS11Exception, CertificateException {
        this.aliasMap = new HashMap<>();
        ArrayList<AliasInfo> arrayList2 = new ArrayList<>();
        Iterator<byte[]> it = arrayList.iterator();
        while (it.hasNext()) {
            byte[] next = it.next();
            boolean z2 = false;
            for (String str : map.keySet()) {
                HashSet<AliasInfo> hashSet = map.get(str);
                Iterator<AliasInfo> it2 = hashSet.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    AliasInfo next2 = it2.next();
                    if (Arrays.equals(next, next2.id)) {
                        if (hashSet.size() == 1) {
                            next2.matched = true;
                            this.aliasMap.put(str, next2);
                        } else {
                            next2.matched = true;
                            this.aliasMap.put(getID(str, next2.cert), next2);
                        }
                        arrayList2.add(next2);
                        z2 = true;
                    }
                }
                if (z2) {
                    break;
                }
            }
            if (!z2 && debug != null) {
                debug.println("did not find match for private key with CKA_ID [" + getID(next) + "] (ignoring entry)");
            }
        }
        return arrayList2;
    }

    private boolean mapCerts(ArrayList<AliasInfo> arrayList, HashMap<String, HashSet<AliasInfo>> map) throws PKCS11Exception, CertificateException {
        Iterator<AliasInfo> it = arrayList.iterator();
        while (it.hasNext()) {
            AliasInfo next = it.next();
            Session opSession = null;
            try {
                opSession = this.token.getOpSession();
                next.chain = loadChain(opSession, next.cert);
                this.token.releaseSession(opSession);
            } catch (Throwable th) {
                this.token.releaseSession(opSession);
                throw th;
            }
        }
        boolean z2 = false;
        for (String str : map.keySet()) {
            HashSet<AliasInfo> hashSet = map.get(str);
            Iterator<AliasInfo> it2 = hashSet.iterator();
            while (it2.hasNext()) {
                AliasInfo next2 = it2.next();
                if (next2.matched) {
                    next2.trusted = false;
                } else if (CKA_TRUSTED_SUPPORTED && next2.trusted && mapTrustedCert(str, next2, hashSet)) {
                    z2 = true;
                }
            }
        }
        return z2;
    }

    private boolean mapTrustedCert(String str, AliasInfo aliasInfo, HashSet<AliasInfo> hashSet) {
        boolean z2 = false;
        aliasInfo.type = ATTR_CLASS_CERT;
        aliasInfo.trusted = true;
        if (hashSet.size() == 1) {
            this.aliasMap.put(str, aliasInfo);
        } else {
            z2 = true;
            this.aliasMap.put(getID(str, aliasInfo.cert), aliasInfo);
        }
        return z2;
    }

    private void mapSecretKeys(HashMap<String, AliasInfo> map) throws KeyStoreException {
        for (String str : map.keySet()) {
            if (this.aliasMap.containsKey(str)) {
                throw new KeyStoreException("invalid KeyStore state: found secret key sharing CKA_LABEL [" + str + "] with another token object");
            }
        }
        this.aliasMap.putAll(map);
    }

    private void dumpTokenMap() {
        Set<String> setKeySet = this.aliasMap.keySet();
        System.out.println("Token Alias Map:");
        if (setKeySet.isEmpty()) {
            System.out.println("  [empty]");
            return;
        }
        for (String str : setKeySet) {
            System.out.println(Constants.INDENT + str + ((Object) this.aliasMap.get(str)));
        }
    }

    private void checkWrite() throws KeyStoreException {
        if (this.writeDisabled) {
            throw new KeyStoreException("This PKCS11KeyStore does not support write capabilities");
        }
    }

    private static long[] findObjects(Session session, CK_ATTRIBUTE[] ck_attributeArr) throws PKCS11Exception {
        Token token = session.token;
        long[] jArrConcat = LONG0;
        token.p11.C_FindObjectsInit(session.id(), ck_attributeArr);
        while (true) {
            long[] jArrC_FindObjects = token.p11.C_FindObjects(session.id(), 100L);
            if (jArrC_FindObjects.length != 0) {
                jArrConcat = P11Util.concat(jArrConcat, jArrC_FindObjects);
            } else {
                token.p11.C_FindObjectsFinal(session.id());
                return jArrConcat;
            }
        }
    }
}
