package sun.security.pkcs11;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.ProviderException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/Secmod.class */
public final class Secmod {
    private static final boolean DEBUG = false;
    private static final Secmod INSTANCE;
    private static final String NSS_LIB_NAME = "nss3";
    private static final String SOFTTOKEN_LIB_NAME = "softokn3";
    private static final String TRUST_LIB_NAME = "nssckbi";
    private static final int NETSCAPE_SLOT_ID = 1;
    private static final int PRIVATE_KEY_SLOT_ID = 2;
    private static final int FIPS_SLOT_ID = 3;
    private long nssHandle;
    private boolean supported;
    private List<Module> modules;
    private String configDir;
    private String nssLibDir;
    static final String TEMPLATE_EXTERNAL = "library = %s\nname = \"%s\"\nslotListIndex = %d\n";
    static final String TEMPLATE_TRUSTANCHOR = "library = %s\nname = \"NSS Trust Anchors\"\nslotListIndex = 0\nenabledMechanisms = { KeyStore }\nnssUseSecmodTrust = true\n";
    static final String TEMPLATE_CRYPTO = "library = %s\nname = \"NSS SoftToken Crypto\"\nslotListIndex = 0\ndisabledMechanisms = { KeyStore }\n";
    static final String TEMPLATE_KEYSTORE = "library = %s\nname = \"NSS SoftToken KeyStore\"\nslotListIndex = 1\nnssUseSecmodTrust = true\n";
    static final String TEMPLATE_FIPS = "library = %s\nname = \"NSS FIPS SoftToken\"\nslotListIndex = 0\nnssUseSecmodTrust = true\n";

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/Secmod$ModuleType.class */
    public enum ModuleType {
        CRYPTO,
        KEYSTORE,
        FIPS,
        TRUSTANCHOR,
        EXTERNAL
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/Secmod$TrustType.class */
    public enum TrustType {
        ALL,
        CLIENT_AUTH,
        SERVER_AUTH,
        CODE_SIGNING,
        EMAIL_PROTECTION
    }

    private static native long nssGetLibraryHandle(String str);

    private static native long nssLoadLibrary(String str) throws IOException;

    private static native boolean nssVersionCheck(long j2, String str);

    private static native boolean nssInitialize(String str, long j2, String str2, boolean z2);

    private static native Object nssGetModuleList(long j2, String str);

    static {
        PKCS11.loadNative();
        INSTANCE = new Secmod();
    }

    private Secmod() {
    }

    public static Secmod getInstance() {
        return INSTANCE;
    }

    private boolean isLoaded() {
        if (this.nssHandle == 0) {
            this.nssHandle = nssGetLibraryHandle(System.mapLibraryName(NSS_LIB_NAME));
            if (this.nssHandle != 0) {
                fetchVersions();
            }
        }
        return this.nssHandle != 0;
    }

    private void fetchVersions() {
        this.supported = nssVersionCheck(this.nssHandle, "3.7");
    }

    public synchronized boolean isInitialized() throws IOException {
        if (!isLoaded()) {
            return false;
        }
        if (!this.supported) {
            throw new IOException("An incompatible version of NSS is already loaded, 3.7 or later required");
        }
        return true;
    }

    String getConfigDir() {
        return this.configDir;
    }

    String getLibDir() {
        return this.nssLibDir;
    }

    public void initialize(String str, String str2) throws IOException {
        initialize(DbMode.READ_WRITE, str, str2, false);
    }

    public void initialize(DbMode dbMode, String str, String str2) throws IOException {
        initialize(dbMode, str, str2, false);
    }

    public synchronized void initialize(DbMode dbMode, String str, String str2, boolean z2) throws IOException {
        String path;
        String strSubstring;
        if (isInitialized()) {
            throw new IOException("NSS is already initialized");
        }
        if (dbMode == null) {
            throw new NullPointerException();
        }
        if (dbMode != DbMode.NO_DB && str == null) {
            throw new NullPointerException();
        }
        String strMapLibraryName = System.mapLibraryName(NSS_LIB_NAME);
        if (str2 == null) {
            path = strMapLibraryName;
        } else {
            File file = new File(str2);
            if (!file.isDirectory()) {
                throw new IOException("nssLibDir must be a directory:" + str2);
            }
            File file2 = new File(file, strMapLibraryName);
            if (!file2.isFile()) {
                throw new FileNotFoundException(file2.getPath());
            }
            path = file2.getPath();
        }
        if (str != null) {
            if (!str.startsWith("sql:")) {
                strSubstring = str;
            } else {
                strSubstring = new StringBuilder(str).substring("sql:".length());
            }
            File file3 = new File(strSubstring);
            if (!file3.isDirectory()) {
                throw new IOException("configDir must be a directory: " + strSubstring);
            }
            if (!str.startsWith("sql:")) {
                File file4 = new File(file3, "secmod.db");
                if (!file4.isFile()) {
                    throw new FileNotFoundException(file4.getPath());
                }
            }
        }
        this.nssHandle = nssLoadLibrary(path);
        fetchVersions();
        if (!this.supported) {
            throw new IOException("The specified version of NSS is incompatible, 3.7 or later required");
        }
        if (!nssInitialize(dbMode.functionName, this.nssHandle, str, z2)) {
            throw new IOException("NSS initialization failed");
        }
        this.configDir = str;
        this.nssLibDir = str2;
    }

    public synchronized List<Module> getModules() {
        try {
            if (!isInitialized()) {
                throw new IllegalStateException("NSS not initialized");
            }
            if (this.modules == null) {
                this.modules = Collections.unmodifiableList((List) nssGetModuleList(this.nssHandle, this.nssLibDir));
            }
            return this.modules;
        } catch (IOException e2) {
            throw new IllegalStateException(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] getDigest(X509Certificate x509Certificate, String str) {
        try {
            return MessageDigest.getInstance(str).digest(x509Certificate.getEncoded());
        } catch (GeneralSecurityException e2) {
            throw new ProviderException(e2);
        }
    }

    boolean isTrusted(X509Certificate x509Certificate, TrustType trustType) {
        Bytes bytes = new Bytes(getDigest(x509Certificate, "SHA-1"));
        TrustAttributes moduleTrust = getModuleTrust(ModuleType.KEYSTORE, bytes);
        if (moduleTrust == null) {
            moduleTrust = getModuleTrust(ModuleType.FIPS, bytes);
            if (moduleTrust == null) {
                moduleTrust = getModuleTrust(ModuleType.TRUSTANCHOR, bytes);
            }
        }
        if (moduleTrust == null) {
            return false;
        }
        return moduleTrust.isTrusted(trustType);
    }

    private TrustAttributes getModuleTrust(ModuleType moduleType, Bytes bytes) {
        Module module = getModule(moduleType);
        return module == null ? null : module.getTrust(bytes);
    }

    public Module getModule(ModuleType moduleType) {
        for (Module module : getModules()) {
            if (module.getType() == moduleType) {
                return module;
            }
        }
        return null;
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/Secmod$Module.class */
    public static final class Module {
        final String libraryName;
        final String commonName;
        final int slot;
        final ModuleType type;
        private String config;
        private SunPKCS11 provider;
        private Map<Bytes, TrustAttributes> trust;

        Module(String str, String str2, String str3, int i2, int i3) {
            ModuleType moduleType;
            if (str2 == null || str2.length() == 0) {
                str2 = System.mapLibraryName(Secmod.SOFTTOKEN_LIB_NAME);
                if (i3 == 1) {
                    moduleType = ModuleType.CRYPTO;
                } else if (i3 == 2) {
                    moduleType = ModuleType.KEYSTORE;
                } else if (i3 == 3) {
                    moduleType = ModuleType.FIPS;
                } else {
                    throw new RuntimeException("Unexpected slot ID " + i3 + " in the NSS Internal Module");
                }
            } else if (str2.endsWith(System.mapLibraryName(Secmod.TRUST_LIB_NAME)) || str3.equals("Builtin Roots Module")) {
                moduleType = ModuleType.TRUSTANCHOR;
            } else {
                moduleType = ModuleType.EXTERNAL;
            }
            File file = new File(str, str2);
            if (!file.isFile()) {
                File file2 = new File(str, "nss/" + str2);
                if (file2.isFile()) {
                    file = file2;
                }
            }
            this.libraryName = file.getPath();
            this.commonName = str3;
            this.slot = i2;
            this.type = moduleType;
            initConfiguration();
        }

        private void initConfiguration() {
            switch (this.type) {
                case EXTERNAL:
                    this.config = String.format(Secmod.TEMPLATE_EXTERNAL, this.libraryName, this.commonName + " " + this.slot, Integer.valueOf(this.slot));
                    return;
                case CRYPTO:
                    this.config = String.format(Secmod.TEMPLATE_CRYPTO, this.libraryName);
                    return;
                case KEYSTORE:
                    this.config = String.format(Secmod.TEMPLATE_KEYSTORE, this.libraryName);
                    return;
                case FIPS:
                    this.config = String.format(Secmod.TEMPLATE_FIPS, this.libraryName);
                    return;
                case TRUSTANCHOR:
                    this.config = String.format(Secmod.TEMPLATE_TRUSTANCHOR, this.libraryName);
                    return;
                default:
                    throw new RuntimeException("Unknown module type: " + ((Object) this.type));
            }
        }

        @Deprecated
        public synchronized String getConfiguration() {
            return this.config;
        }

        @Deprecated
        public synchronized void setConfiguration(String str) {
            if (this.provider != null) {
                throw new IllegalStateException("Provider instance already created");
            }
            this.config = str;
        }

        public String getLibraryName() {
            return this.libraryName;
        }

        public ModuleType getType() {
            return this.type;
        }

        @Deprecated
        public synchronized Provider getProvider() {
            if (this.provider == null) {
                this.provider = newProvider();
            }
            return this.provider;
        }

        synchronized boolean hasInitializedProvider() {
            return this.provider != null;
        }

        void setProvider(SunPKCS11 sunPKCS11) {
            if (this.provider != null) {
                throw new ProviderException("Secmod provider already initialized");
            }
            this.provider = sunPKCS11;
        }

        private SunPKCS11 newProvider() {
            try {
                return new SunPKCS11(new ByteArrayInputStream(this.config.getBytes(InternalZipConstants.CHARSET_UTF8)));
            } catch (Exception e2) {
                throw new ProviderException(e2);
            }
        }

        synchronized void setTrust(Token token, X509Certificate x509Certificate) {
            Bytes bytes = new Bytes(Secmod.getDigest(x509Certificate, "SHA-1"));
            TrustAttributes trust = getTrust(bytes);
            if (trust == null) {
                this.trust.put(bytes, new TrustAttributes(token, x509Certificate, bytes, PKCS11Constants.CKT_NETSCAPE_TRUSTED_DELEGATOR));
            } else if (!trust.isTrusted(TrustType.ALL)) {
                throw new ProviderException("Cannot change existing trust attributes");
            }
        }

        TrustAttributes getTrust(Bytes bytes) {
            if (this.trust == null) {
                synchronized (this) {
                    SunPKCS11 sunPKCS11NewProvider = this.provider;
                    if (sunPKCS11NewProvider == null) {
                        sunPKCS11NewProvider = newProvider();
                    }
                    try {
                        this.trust = Secmod.getTrust(sunPKCS11NewProvider);
                    } catch (PKCS11Exception e2) {
                        throw new RuntimeException(e2);
                    }
                }
            }
            return this.trust.get(bytes);
        }

        public String toString() {
            return this.commonName + " (" + ((Object) this.type) + ", " + this.libraryName + ", slot " + this.slot + ")";
        }
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/Secmod$DbMode.class */
    public enum DbMode {
        READ_WRITE("NSS_InitReadWrite"),
        READ_ONLY("NSS_Init"),
        NO_DB("NSS_NoDB_Init");

        final String functionName;

        DbMode(String str) {
            this.functionName = str;
        }
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/Secmod$KeyStoreLoadParameter.class */
    public static final class KeyStoreLoadParameter implements KeyStore.LoadStoreParameter {
        final TrustType trustType;
        final KeyStore.ProtectionParameter protection;

        public KeyStoreLoadParameter(TrustType trustType, char[] cArr) {
            this(trustType, new KeyStore.PasswordProtection(cArr));
        }

        public KeyStoreLoadParameter(TrustType trustType, KeyStore.ProtectionParameter protectionParameter) {
            if (trustType == null) {
                throw new NullPointerException("trustType must not be null");
            }
            this.trustType = trustType;
            this.protection = protectionParameter;
        }

        @Override // java.security.KeyStore.LoadStoreParameter
        public KeyStore.ProtectionParameter getProtectionParameter() {
            return this.protection;
        }

        public TrustType getTrustType() {
            return this.trustType;
        }
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/Secmod$TrustAttributes.class */
    static class TrustAttributes {
        final long handle;
        final long clientAuth;
        final long serverAuth;
        final long codeSigning;
        final long emailProtection;
        final byte[] shaHash;

        TrustAttributes(Token token, X509Certificate x509Certificate, Bytes bytes, long j2) {
            Session opSession = null;
            try {
                try {
                    opSession = token.getOpSession();
                    this.handle = token.p11.C_CreateObject(opSession.id(), new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(1L, true), new CK_ATTRIBUTE(0L, 3461563219L), new CK_ATTRIBUTE(PKCS11Constants.CKA_NETSCAPE_TRUST_SERVER_AUTH, j2), new CK_ATTRIBUTE(PKCS11Constants.CKA_NETSCAPE_TRUST_CODE_SIGNING, j2), new CK_ATTRIBUTE(PKCS11Constants.CKA_NETSCAPE_TRUST_EMAIL_PROTECTION, j2), new CK_ATTRIBUTE(PKCS11Constants.CKA_NETSCAPE_TRUST_CLIENT_AUTH, j2), new CK_ATTRIBUTE(PKCS11Constants.CKA_NETSCAPE_CERT_SHA1_HASH, bytes.f13629b), new CK_ATTRIBUTE(PKCS11Constants.CKA_NETSCAPE_CERT_MD5_HASH, Secmod.getDigest(x509Certificate, "MD5")), new CK_ATTRIBUTE(129L, x509Certificate.getIssuerX500Principal().getEncoded()), new CK_ATTRIBUTE(130L, x509Certificate.getSerialNumber().toByteArray())});
                    this.shaHash = bytes.f13629b;
                    this.clientAuth = j2;
                    this.serverAuth = j2;
                    this.codeSigning = j2;
                    this.emailProtection = j2;
                    token.releaseSession(opSession);
                } catch (PKCS11Exception e2) {
                    throw new ProviderException("Could not create trust object", e2);
                }
            } catch (Throwable th) {
                token.releaseSession(opSession);
                throw th;
            }
        }

        TrustAttributes(Token token, Session session, long j2) throws PKCS11Exception {
            long j3;
            this.handle = j2;
            CK_ATTRIBUTE[] ck_attributeArr = {new CK_ATTRIBUTE(PKCS11Constants.CKA_NETSCAPE_TRUST_SERVER_AUTH), new CK_ATTRIBUTE(PKCS11Constants.CKA_NETSCAPE_TRUST_CODE_SIGNING), new CK_ATTRIBUTE(PKCS11Constants.CKA_NETSCAPE_TRUST_EMAIL_PROTECTION), new CK_ATTRIBUTE(PKCS11Constants.CKA_NETSCAPE_CERT_SHA1_HASH)};
            token.p11.C_GetAttributeValue(session.id(), j2, ck_attributeArr);
            this.serverAuth = ck_attributeArr[0].getLong();
            this.codeSigning = ck_attributeArr[1].getLong();
            this.emailProtection = ck_attributeArr[2].getLong();
            this.shaHash = ck_attributeArr[3].getByteArray();
            CK_ATTRIBUTE[] ck_attributeArr2 = {new CK_ATTRIBUTE(PKCS11Constants.CKA_NETSCAPE_TRUST_CLIENT_AUTH)};
            try {
                token.p11.C_GetAttributeValue(session.id(), j2, ck_attributeArr2);
                j3 = ck_attributeArr2[0].getLong();
            } catch (PKCS11Exception e2) {
                j3 = this.serverAuth;
            }
            this.clientAuth = j3;
        }

        Bytes getHash() {
            return new Bytes(this.shaHash);
        }

        boolean isTrusted(TrustType trustType) {
            switch (trustType) {
                case CLIENT_AUTH:
                    return isTrusted(this.clientAuth);
                case SERVER_AUTH:
                    return isTrusted(this.serverAuth);
                case CODE_SIGNING:
                    return isTrusted(this.codeSigning);
                case EMAIL_PROTECTION:
                    return isTrusted(this.emailProtection);
                case ALL:
                    return isTrusted(TrustType.CLIENT_AUTH) && isTrusted(TrustType.SERVER_AUTH) && isTrusted(TrustType.CODE_SIGNING) && isTrusted(TrustType.EMAIL_PROTECTION);
                default:
                    return false;
            }
        }

        private boolean isTrusted(long j2) {
            return j2 == PKCS11Constants.CKT_NETSCAPE_TRUSTED_DELEGATOR;
        }
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/Secmod$Bytes.class */
    private static class Bytes {

        /* renamed from: b, reason: collision with root package name */
        final byte[] f13629b;

        Bytes(byte[] bArr) {
            this.f13629b = bArr;
        }

        public int hashCode() {
            return Arrays.hashCode(this.f13629b);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Bytes)) {
                return false;
            }
            return Arrays.equals(this.f13629b, ((Bytes) obj).f13629b);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Map<Bytes, TrustAttributes> getTrust(SunPKCS11 sunPKCS11) throws PKCS11Exception {
        HashMap map = new HashMap();
        Token token = sunPKCS11.getToken();
        Session opSession = null;
        try {
            opSession = token.getOpSession();
            token.p11.C_FindObjectsInit(opSession.id(), new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(0L, 3461563219L)});
            long[] jArrC_FindObjects = token.p11.C_FindObjects(opSession.id(), 8192);
            token.p11.C_FindObjectsFinal(opSession.id());
            for (long j2 : jArrC_FindObjects) {
                try {
                    TrustAttributes trustAttributes = new TrustAttributes(token, opSession, j2);
                    map.put(trustAttributes.getHash(), trustAttributes);
                } catch (PKCS11Exception e2) {
                }
            }
            token.releaseSession(opSession);
            return map;
        } catch (Throwable th) {
            token.releaseSession(opSession);
            throw th;
        }
    }
}
