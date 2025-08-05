package sun.security.pkcs11;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.NotSerializableException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.security.AccessController;
import java.security.AuthProvider;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.Provider;
import java.security.ProviderException;
import java.security.Security;
import java.security.SecurityPermission;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.pkcs11.Secmod;
import sun.security.pkcs11.wrapper.CK_C_INITIALIZE_ARGS;
import sun.security.pkcs11.wrapper.CK_INFO;
import sun.security.pkcs11.wrapper.CK_MECHANISM_INFO;
import sun.security.pkcs11.wrapper.CK_SLOT_INFO;
import sun.security.pkcs11.wrapper.Functions;
import sun.security.pkcs11.wrapper.PKCS11;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import sun.security.util.Debug;
import sun.security.util.ECParameters;
import sun.security.util.GCMParameters;
import sun.security.util.ResourcesMgr;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/SunPKCS11.class */
public final class SunPKCS11 extends AuthProvider {
    private static final long serialVersionUID = -1354835039035306505L;
    private static int dummyConfigId;
    final PKCS11 p11;
    private final String configName;
    final Config config;
    final long slotID;
    private CallbackHandler pHandler;
    private final Object LOCK_HANDLER;
    final boolean removable;
    final Secmod.Module nssModule;
    final boolean nssUseSecmodTrust;
    private volatile Token token;
    private TokenPoller poller;
    private static final String MD = "MessageDigest";
    private static final String SIG = "Signature";
    private static final String KPG = "KeyPairGenerator";
    private static final String KG = "KeyGenerator";
    private static final String AGP = "AlgorithmParameters";
    private static final String KF = "KeyFactory";
    private static final String SKF = "SecretKeyFactory";
    private static final String CIP = "Cipher";
    private static final String MAC = "Mac";
    private static final String KA = "KeyAgreement";
    private static final String KS = "KeyStore";
    private static final String SR = "SecureRandom";
    static final Debug debug = Debug.getInstance("sunpkcs11");
    private static final Map<Integer, List<Descriptor>> descriptors = new HashMap();

    static {
        d(MD, "MD2", "sun.security.pkcs11.P11Digest", m(512L));
        d(MD, "MD5", "sun.security.pkcs11.P11Digest", m(528L));
        d(MD, "SHA1", "sun.security.pkcs11.P11Digest", s("SHA", "SHA-1", "1.3.14.3.2.26", "OID.1.3.14.3.2.26"), m(544L));
        d(MD, "SHA-224", "sun.security.pkcs11.P11Digest", s("2.16.840.1.101.3.4.2.4", "OID.2.16.840.1.101.3.4.2.4"), m(597L));
        d(MD, "SHA-256", "sun.security.pkcs11.P11Digest", s("2.16.840.1.101.3.4.2.1", "OID.2.16.840.1.101.3.4.2.1"), m(592L));
        d(MD, "SHA-384", "sun.security.pkcs11.P11Digest", s("2.16.840.1.101.3.4.2.2", "OID.2.16.840.1.101.3.4.2.2"), m(608L));
        d(MD, "SHA-512", "sun.security.pkcs11.P11Digest", s("2.16.840.1.101.3.4.2.3", "OID.2.16.840.1.101.3.4.2.3"), m(624L));
        d(MD, "SHA-512/224", "sun.security.pkcs11.P11Digest", s("2.16.840.1.101.3.4.2.5", "OID.2.16.840.1.101.3.4.2.5"), m(72L));
        d(MD, "SHA-512/256", "sun.security.pkcs11.P11Digest", s("2.16.840.1.101.3.4.2.6", "OID.2.16.840.1.101.3.4.2.6"), m(76L));
        d("Mac", "HmacMD5", "sun.security.pkcs11.P11MAC", m(529L));
        d("Mac", "HmacSHA1", "sun.security.pkcs11.P11MAC", s("1.2.840.113549.2.7", "OID.1.2.840.113549.2.7"), m(545L));
        d("Mac", "HmacSHA224", "sun.security.pkcs11.P11MAC", s("1.2.840.113549.2.8", "OID.1.2.840.113549.2.8"), m(598L));
        d("Mac", "HmacSHA256", "sun.security.pkcs11.P11MAC", s("1.2.840.113549.2.9", "OID.1.2.840.113549.2.9"), m(593L));
        d("Mac", "HmacSHA384", "sun.security.pkcs11.P11MAC", s("1.2.840.113549.2.10", "OID.1.2.840.113549.2.10"), m(609L));
        d("Mac", "HmacSHA512", "sun.security.pkcs11.P11MAC", s("1.2.840.113549.2.11", "OID.1.2.840.113549.2.11"), m(625L));
        d("Mac", "HmacSHA512/224", "sun.security.pkcs11.P11MAC", s("1.2.840.113549.2.12", "OID.1.2.840.113549.2.12"), m(73L));
        d("Mac", "HmacSHA512/256", "sun.security.pkcs11.P11MAC", s("1.2.840.113549.2.13", "OID.1.2.840.113549.2.13"), m(77L));
        d("Mac", "SslMacMD5", "sun.security.pkcs11.P11MAC", m(896L));
        d("Mac", "SslMacSHA1", "sun.security.pkcs11.P11MAC", m(897L));
        d(KPG, "RSA", "sun.security.pkcs11.P11KeyPairGenerator", s("1.2.840.113549.1.1", "OID.1.2.840.113549.1.1"), m(0L));
        d(KPG, "DSA", "sun.security.pkcs11.P11KeyPairGenerator", s("1.3.14.3.2.12", "1.2.840.10040.4.1", "OID.1.2.840.10040.4.1"), m(16L));
        d(KPG, "DH", "sun.security.pkcs11.P11KeyPairGenerator", s("DiffieHellman"), m(32L));
        d(KPG, "EC", "sun.security.pkcs11.P11KeyPairGenerator", m(4160L));
        d(KG, "ARCFOUR", "sun.security.pkcs11.P11KeyGenerator", s("RC4"), m(272L));
        d(KG, "DES", "sun.security.pkcs11.P11KeyGenerator", m(288L));
        d(KG, "DESede", "sun.security.pkcs11.P11KeyGenerator", m(305L, 304L));
        d(KG, "AES", "sun.security.pkcs11.P11KeyGenerator", m(PKCS11Constants.CKM_AES_KEY_GEN));
        d(KG, "Blowfish", "sun.security.pkcs11.P11KeyGenerator", m(PKCS11Constants.CKM_BLOWFISH_KEY_GEN));
        d(KF, "RSA", "sun.security.pkcs11.P11RSAKeyFactory", s("1.2.840.113549.1.1", "OID.1.2.840.113549.1.1"), m(0L, 1L, 3L));
        d(KF, "DSA", "sun.security.pkcs11.P11DSAKeyFactory", s("1.3.14.3.2.12", "1.2.840.10040.4.1", "OID.1.2.840.10040.4.1"), m(16L, 17L, 18L));
        d(KF, "DH", "sun.security.pkcs11.P11DHKeyFactory", s("DiffieHellman"), m(32L, 33L));
        d(KF, "EC", "sun.security.pkcs11.P11DHKeyFactory", m(4160L, PKCS11Constants.CKM_ECDH1_DERIVE, PKCS11Constants.CKM_ECDSA, PKCS11Constants.CKM_ECDSA_SHA1));
        d(AGP, "EC", "sun.security.util.ECParameters", s("1.2.840.10045.2.1"), m(4160L, PKCS11Constants.CKM_ECDH1_DERIVE, PKCS11Constants.CKM_ECDSA, PKCS11Constants.CKM_ECDSA_SHA1));
        d(AGP, "GCM", "sun.security.util.GCMParameters", m(PKCS11Constants.CKM_AES_GCM));
        d(KA, "DH", "sun.security.pkcs11.P11KeyAgreement", s("DiffieHellman"), m(33L));
        d(KA, "ECDH", "sun.security.pkcs11.P11ECDHKeyAgreement", m(PKCS11Constants.CKM_ECDH1_DERIVE));
        d(SKF, "ARCFOUR", "sun.security.pkcs11.P11SecretKeyFactory", s("RC4"), m(273L));
        d(SKF, "DES", "sun.security.pkcs11.P11SecretKeyFactory", m(290L));
        d(SKF, "DESede", "sun.security.pkcs11.P11SecretKeyFactory", m(307L));
        d(SKF, "AES", "sun.security.pkcs11.P11SecretKeyFactory", s("2.16.840.1.101.3.4.1", "OID.2.16.840.1.101.3.4.1"), m(PKCS11Constants.CKM_AES_CBC));
        d(SKF, "Blowfish", "sun.security.pkcs11.P11SecretKeyFactory", m(PKCS11Constants.CKM_BLOWFISH_CBC));
        d(CIP, "ARCFOUR", "sun.security.pkcs11.P11Cipher", s("RC4"), m(273L));
        d(CIP, "DES/CBC/NoPadding", "sun.security.pkcs11.P11Cipher", m(290L));
        d(CIP, "DES/CBC/PKCS5Padding", "sun.security.pkcs11.P11Cipher", m(293L, 290L));
        d(CIP, "DES/ECB/NoPadding", "sun.security.pkcs11.P11Cipher", m(289L));
        d(CIP, "DES/ECB/PKCS5Padding", "sun.security.pkcs11.P11Cipher", s("DES"), m(289L));
        d(CIP, "DESede/CBC/NoPadding", "sun.security.pkcs11.P11Cipher", m(307L));
        d(CIP, "DESede/CBC/PKCS5Padding", "sun.security.pkcs11.P11Cipher", m(310L, 307L));
        d(CIP, "DESede/ECB/NoPadding", "sun.security.pkcs11.P11Cipher", m(306L));
        d(CIP, "DESede/ECB/PKCS5Padding", "sun.security.pkcs11.P11Cipher", s("DESede"), m(306L));
        d(CIP, "AES/CBC/NoPadding", "sun.security.pkcs11.P11Cipher", m(PKCS11Constants.CKM_AES_CBC));
        d(CIP, "AES_128/CBC/NoPadding", "sun.security.pkcs11.P11Cipher", s("2.16.840.1.101.3.4.1.2", "OID.2.16.840.1.101.3.4.1.2"), m(PKCS11Constants.CKM_AES_CBC));
        d(CIP, "AES_192/CBC/NoPadding", "sun.security.pkcs11.P11Cipher", s("2.16.840.1.101.3.4.1.22", "OID.2.16.840.1.101.3.4.1.22"), m(PKCS11Constants.CKM_AES_CBC));
        d(CIP, "AES_256/CBC/NoPadding", "sun.security.pkcs11.P11Cipher", s("2.16.840.1.101.3.4.1.42", "OID.2.16.840.1.101.3.4.1.42"), m(PKCS11Constants.CKM_AES_CBC));
        d(CIP, "AES/CBC/PKCS5Padding", "sun.security.pkcs11.P11Cipher", m(PKCS11Constants.CKM_AES_CBC_PAD, PKCS11Constants.CKM_AES_CBC));
        d(CIP, "AES/ECB/NoPadding", "sun.security.pkcs11.P11Cipher", m(PKCS11Constants.CKM_AES_ECB));
        d(CIP, "AES_128/ECB/NoPadding", "sun.security.pkcs11.P11Cipher", s("2.16.840.1.101.3.4.1.1", "OID.2.16.840.1.101.3.4.1.1"), m(PKCS11Constants.CKM_AES_ECB));
        d(CIP, "AES_192/ECB/NoPadding", "sun.security.pkcs11.P11Cipher", s("2.16.840.1.101.3.4.1.21", "OID.2.16.840.1.101.3.4.1.21"), m(PKCS11Constants.CKM_AES_ECB));
        d(CIP, "AES_256/ECB/NoPadding", "sun.security.pkcs11.P11Cipher", s("2.16.840.1.101.3.4.1.41", "OID.2.16.840.1.101.3.4.1.41"), m(PKCS11Constants.CKM_AES_ECB));
        d(CIP, "AES/ECB/PKCS5Padding", "sun.security.pkcs11.P11Cipher", s("AES"), m(PKCS11Constants.CKM_AES_ECB));
        d(CIP, "AES/CTR/NoPadding", "sun.security.pkcs11.P11Cipher", m(PKCS11Constants.CKM_AES_CTR));
        d(CIP, "AES/GCM/NoPadding", "sun.security.pkcs11.P11AEADCipher", m(PKCS11Constants.CKM_AES_GCM));
        d(CIP, "AES_128/GCM/NoPadding", "sun.security.pkcs11.P11AEADCipher", s("2.16.840.1.101.3.4.1.6", "OID.2.16.840.1.101.3.4.1.6"), m(PKCS11Constants.CKM_AES_GCM));
        d(CIP, "AES_192/GCM/NoPadding", "sun.security.pkcs11.P11AEADCipher", s("2.16.840.1.101.3.4.1.26", "OID.2.16.840.1.101.3.4.1.26"), m(PKCS11Constants.CKM_AES_GCM));
        d(CIP, "AES_256/GCM/NoPadding", "sun.security.pkcs11.P11AEADCipher", s("2.16.840.1.101.3.4.1.46", "OID.2.16.840.1.101.3.4.1.46"), m(PKCS11Constants.CKM_AES_GCM));
        d(CIP, "Blowfish/CBC/NoPadding", "sun.security.pkcs11.P11Cipher", m(PKCS11Constants.CKM_BLOWFISH_CBC));
        d(CIP, "Blowfish/CBC/PKCS5Padding", "sun.security.pkcs11.P11Cipher", m(PKCS11Constants.CKM_BLOWFISH_CBC));
        d(CIP, "RSA/ECB/PKCS1Padding", "sun.security.pkcs11.P11RSACipher", s("RSA"), m(1L));
        d(CIP, "RSA/ECB/NoPadding", "sun.security.pkcs11.P11RSACipher", m(3L));
        d("Signature", "RawDSA", "sun.security.pkcs11.P11Signature", s("NONEwithDSA"), m(17L));
        d("Signature", "DSA", "sun.security.pkcs11.P11Signature", s("SHA1withDSA", "1.3.14.3.2.13", "1.3.14.3.2.27", "1.2.840.10040.4.3", "OID.1.2.840.10040.4.3"), m(18L, 17L));
        d("Signature", "SHA224withDSA", "sun.security.pkcs11.P11Signature", s("2.16.840.1.101.3.4.3.1", "OID.2.16.840.1.101.3.4.3.1"), m(19L));
        d("Signature", "SHA256withDSA", "sun.security.pkcs11.P11Signature", s("2.16.840.1.101.3.4.3.2", "OID.2.16.840.1.101.3.4.3.2"), m(20L));
        d("Signature", "SHA384withDSA", "sun.security.pkcs11.P11Signature", s("2.16.840.1.101.3.4.3.3", "OID.2.16.840.1.101.3.4.3.3"), m(21L));
        d("Signature", "SHA512withDSA", "sun.security.pkcs11.P11Signature", s("2.16.840.1.101.3.4.3.4", "OID.2.16.840.1.101.3.4.3.4"), m(22L));
        d("Signature", "NONEwithECDSA", "sun.security.pkcs11.P11Signature", m(PKCS11Constants.CKM_ECDSA));
        d("Signature", "SHA1withECDSA", "sun.security.pkcs11.P11Signature", s("ECDSA", "1.2.840.10045.4.1", "OID.1.2.840.10045.4.1"), m(PKCS11Constants.CKM_ECDSA_SHA1, PKCS11Constants.CKM_ECDSA));
        d("Signature", "SHA224withECDSA", "sun.security.pkcs11.P11Signature", s("1.2.840.10045.4.3.1", "OID.1.2.840.10045.4.3.1"), m(PKCS11Constants.CKM_ECDSA));
        d("Signature", "SHA256withECDSA", "sun.security.pkcs11.P11Signature", s("1.2.840.10045.4.3.2", "OID.1.2.840.10045.4.3.2"), m(PKCS11Constants.CKM_ECDSA));
        d("Signature", "SHA384withECDSA", "sun.security.pkcs11.P11Signature", s("1.2.840.10045.4.3.3", "OID.1.2.840.10045.4.3.3"), m(PKCS11Constants.CKM_ECDSA));
        d("Signature", "SHA512withECDSA", "sun.security.pkcs11.P11Signature", s("1.2.840.10045.4.3.4", "OID.1.2.840.10045.4.3.4"), m(PKCS11Constants.CKM_ECDSA));
        d("Signature", "MD2withRSA", "sun.security.pkcs11.P11Signature", s("1.2.840.113549.1.1.2", "OID.1.2.840.113549.1.1.2"), m(4L, 1L, 3L));
        d("Signature", "MD5withRSA", "sun.security.pkcs11.P11Signature", s("1.2.840.113549.1.1.4", "OID.1.2.840.113549.1.1.4"), m(5L, 1L, 3L));
        d("Signature", "SHA1withRSA", "sun.security.pkcs11.P11Signature", s("1.2.840.113549.1.1.5", "OID.1.2.840.113549.1.1.5", "1.3.14.3.2.29"), m(6L, 1L, 3L));
        d("Signature", "SHA224withRSA", "sun.security.pkcs11.P11Signature", s("1.2.840.113549.1.1.14", "OID.1.2.840.113549.1.1.14"), m(70L, 1L, 3L));
        d("Signature", "SHA256withRSA", "sun.security.pkcs11.P11Signature", s("1.2.840.113549.1.1.11", "OID.1.2.840.113549.1.1.11"), m(64L, 1L, 3L));
        d("Signature", "SHA384withRSA", "sun.security.pkcs11.P11Signature", s("1.2.840.113549.1.1.12", "OID.1.2.840.113549.1.1.12"), m(65L, 1L, 3L));
        d("Signature", "SHA512withRSA", "sun.security.pkcs11.P11Signature", s("1.2.840.113549.1.1.13", "OID.1.2.840.113549.1.1.13"), m(66L, 1L, 3L));
        d("Signature", "RSASSA-PSS", "sun.security.pkcs11.P11PSSSignature", s("1.2.840.113549.1.1.10", "OID.1.2.840.113549.1.1.10"), m(13L));
        d("Signature", "SHA1withRSASSA-PSS", "sun.security.pkcs11.P11PSSSignature", m(14L));
        d("Signature", "SHA224withRSASSA-PSS", "sun.security.pkcs11.P11PSSSignature", m(71L));
        d("Signature", "SHA256withRSASSA-PSS", "sun.security.pkcs11.P11PSSSignature", m(67L));
        d("Signature", "SHA384withRSASSA-PSS", "sun.security.pkcs11.P11PSSSignature", m(68L));
        d("Signature", "SHA512withRSASSA-PSS", "sun.security.pkcs11.P11PSSSignature", m(69L));
        d(KG, "SunTlsRsaPremasterSecret", "sun.security.pkcs11.P11TlsRsaPremasterSecretGenerator", s("SunTls12RsaPremasterSecret"), m(880L, 884L));
        d(KG, "SunTlsMasterSecret", "sun.security.pkcs11.P11TlsMasterSecretGenerator", m(881L, 885L, 883L, 887L));
        d(KG, "SunTls12MasterSecret", "sun.security.pkcs11.P11TlsMasterSecretGenerator", m(992L, 994L));
        d(KG, "SunTlsKeyMaterial", "sun.security.pkcs11.P11TlsKeyMaterialGenerator", m(882L, 886L));
        d(KG, "SunTls12KeyMaterial", "sun.security.pkcs11.P11TlsKeyMaterialGenerator", m(993L));
        d(KG, "SunTlsPrf", "sun.security.pkcs11.P11TlsPrfGenerator", m(888L, PKCS11Constants.CKM_NSS_TLS_PRF_GENERAL));
        d(KG, "SunTls12Prf", "sun.security.pkcs11.P11TlsPrfGenerator", m(996L));
    }

    Token getToken() {
        return this.token;
    }

    public SunPKCS11() {
        super("SunPKCS11-Dummy", 1.8d, "SunPKCS11-Dummy");
        this.LOCK_HANDLER = new Object();
        throw new ProviderException("SunPKCS11 requires configuration file argument");
    }

    public SunPKCS11(String str) {
        this((String) checkNull(str), null);
    }

    public SunPKCS11(InputStream inputStream) {
        this(getDummyConfigName(), (InputStream) checkNull(inputStream));
    }

    private static <T> T checkNull(T t2) {
        if (t2 == null) {
            throw new NullPointerException();
        }
        return t2;
    }

    private static synchronized String getDummyConfigName() {
        int i2 = dummyConfigId + 1;
        dummyConfigId = i2;
        return "---DummyConfig-" + i2 + "---";
    }

    @Deprecated
    public SunPKCS11(String str, InputStream inputStream) throws PKCS11Exception {
        PKCS11 pkcs11;
        int i2;
        String configDir;
        super("SunPKCS11-" + Config.getConfig(str, inputStream).getName(), 1.8d, Config.getConfig(str, inputStream).getDescription());
        this.LOCK_HANDLER = new Object();
        this.configName = str;
        this.config = Config.removeConfig(str);
        if (debug != null) {
            System.out.println("SunPKCS11 loading " + str);
        }
        String library = this.config.getLibrary();
        String functionList = this.config.getFunctionList();
        long slotID = this.config.getSlotID();
        int slotListIndex = this.config.getSlotListIndex();
        boolean nssUseSecmod = this.config.getNssUseSecmod();
        boolean nssUseSecmodTrust = this.config.getNssUseSecmodTrust();
        Secmod.Module module = null;
        if (nssUseSecmod) {
            Secmod secmod = Secmod.getInstance();
            Secmod.DbMode nssDbMode = this.config.getNssDbMode();
            try {
                String nssLibraryDirectory = this.config.getNssLibraryDirectory();
                String nssSecmodDirectory = this.config.getNssSecmodDirectory();
                boolean nssOptimizeSpace = this.config.getNssOptimizeSpace();
                if (secmod.isInitialized()) {
                    if (nssSecmodDirectory != null && (configDir = secmod.getConfigDir()) != null && !configDir.equals(nssSecmodDirectory)) {
                        throw new ProviderException("Secmod directory " + nssSecmodDirectory + " invalid, NSS already initialized with " + configDir);
                    }
                    if (nssLibraryDirectory != null) {
                        String libDir = secmod.getLibDir();
                        if (libDir != null && !libDir.equals(nssLibraryDirectory)) {
                            throw new ProviderException("NSS library directory " + nssLibraryDirectory + " invalid, NSS already initialized with " + libDir);
                        }
                    }
                } else {
                    if (nssDbMode != Secmod.DbMode.NO_DB) {
                        if (nssSecmodDirectory == null) {
                            throw new ProviderException("Secmod not initialized and nssSecmodDirectory not specified");
                        }
                    } else if (nssSecmodDirectory != null) {
                        throw new ProviderException("nssSecmodDirectory must not be specified in noDb mode");
                    }
                    secmod.initialize(nssDbMode, nssSecmodDirectory, nssLibraryDirectory, nssOptimizeSpace);
                }
                List<Secmod.Module> modules = secmod.getModules();
                if (this.config.getShowInfo()) {
                    System.out.println("NSS modules: " + ((Object) modules));
                }
                String nssModule = this.config.getNssModule();
                if (nssModule == null) {
                    module = secmod.getModule(Secmod.ModuleType.FIPS);
                    if (module != null) {
                        nssModule = "fips";
                    } else {
                        nssModule = nssDbMode == Secmod.DbMode.NO_DB ? "crypto" : "keystore";
                    }
                }
                if (nssModule.equals("fips")) {
                    module = secmod.getModule(Secmod.ModuleType.FIPS);
                    nssUseSecmodTrust = true;
                    functionList = "FC_GetFunctionList";
                } else if (nssModule.equals("keystore")) {
                    module = secmod.getModule(Secmod.ModuleType.KEYSTORE);
                    nssUseSecmodTrust = true;
                } else if (nssModule.equals("crypto")) {
                    module = secmod.getModule(Secmod.ModuleType.CRYPTO);
                } else if (nssModule.equals("trustanchors")) {
                    module = secmod.getModule(Secmod.ModuleType.TRUSTANCHOR);
                    nssUseSecmodTrust = true;
                } else if (nssModule.startsWith("external-")) {
                    try {
                        i2 = Integer.parseInt(nssModule.substring("external-".length()));
                    } catch (NumberFormatException e2) {
                        i2 = -1;
                    }
                    if (i2 < 1) {
                        throw new ProviderException("Invalid external module: " + nssModule);
                    }
                    int i3 = 0;
                    Iterator<Secmod.Module> it = modules.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        Secmod.Module next = it.next();
                        if (next.getType() == Secmod.ModuleType.EXTERNAL) {
                            i3++;
                            if (i3 == i2) {
                                module = next;
                                break;
                            }
                        }
                    }
                    if (module == null) {
                        throw new ProviderException("Invalid module " + nssModule + ": only " + i3 + " external NSS modules available");
                    }
                } else {
                    throw new ProviderException("Unknown NSS module: " + nssModule);
                }
                if (module == null) {
                    throw new ProviderException("NSS module not available: " + nssModule);
                }
                if (module.hasInitializedProvider()) {
                    throw new ProviderException("Secmod module already configured");
                }
                library = module.libraryName;
                slotListIndex = module.slot;
            } catch (IOException e3) {
                throw new ProviderException("Could not initialize NSS", e3);
            }
        }
        this.nssUseSecmodTrust = nssUseSecmodTrust;
        this.nssModule = module;
        if (!new File(library).getName().equals(library) && !new File(library).isFile()) {
            String str2 = "Library " + library + " does not exist";
            if (this.config.getHandleStartupErrors() == 1) {
                throw new ProviderException(str2);
            }
            throw new UnsupportedOperationException(str2);
        }
        try {
            if (debug != null) {
                debug.println("Initializing PKCS#11 library " + library);
            }
            CK_C_INITIALIZE_ARGS ck_c_initialize_args = new CK_C_INITIALIZE_ARGS();
            String nssArgs = this.config.getNssArgs();
            if (nssArgs != null) {
                ck_c_initialize_args.pReserved = nssArgs;
            }
            ck_c_initialize_args.flags = 2L;
            try {
                pkcs11 = PKCS11.getInstance(library, functionList, ck_c_initialize_args, this.config.getOmitInitialize());
            } catch (PKCS11Exception e4) {
                if (debug != null) {
                    debug.println("Multi-threaded initialization failed: " + ((Object) e4));
                }
                if (!this.config.getAllowSingleThreadedModules()) {
                    throw e4;
                }
                if (nssArgs == null) {
                    ck_c_initialize_args = null;
                } else {
                    ck_c_initialize_args.flags = 0L;
                }
                pkcs11 = PKCS11.getInstance(library, functionList, ck_c_initialize_args, this.config.getOmitInitialize());
            }
            this.p11 = pkcs11;
            CK_INFO ck_infoC_GetInfo = this.p11.C_GetInfo();
            if (ck_infoC_GetInfo.cryptokiVersion.major < 2) {
                throw new ProviderException("Only PKCS#11 v2.0 and later supported, library version is v" + ((Object) ck_infoC_GetInfo.cryptokiVersion));
            }
            boolean showInfo = this.config.getShowInfo();
            if (showInfo) {
                System.out.println("Information for provider " + getName());
                System.out.println("Library info:");
                System.out.println(ck_infoC_GetInfo);
            }
            if (slotID < 0 || showInfo) {
                long[] jArrC_GetSlotList = this.p11.C_GetSlotList(false);
                if (showInfo) {
                    System.out.println("All slots: " + toString(jArrC_GetSlotList));
                    jArrC_GetSlotList = this.p11.C_GetSlotList(true);
                    System.out.println("Slots with tokens: " + toString(jArrC_GetSlotList));
                }
                if (slotID < 0) {
                    if (slotListIndex < 0 || slotListIndex >= jArrC_GetSlotList.length) {
                        throw new ProviderException("slotListIndex is " + slotListIndex + " but token only has " + jArrC_GetSlotList.length + " slots");
                    }
                    slotID = jArrC_GetSlotList[slotListIndex];
                }
            }
            this.slotID = slotID;
            CK_SLOT_INFO ck_slot_infoC_GetSlotInfo = this.p11.C_GetSlotInfo(slotID);
            this.removable = (ck_slot_infoC_GetSlotInfo.flags & 2) != 0;
            initToken(ck_slot_infoC_GetSlotInfo);
            if (module != null) {
                module.setProvider(this);
            }
        } catch (Exception e5) {
            if (this.config.getHandleStartupErrors() == 2) {
                throw new UnsupportedOperationException("Initialization failed", e5);
            }
            throw new ProviderException("Initialization failed", e5);
        }
    }

    private static String toString(long[] jArr) {
        if (jArr.length == 0) {
            return "(none)";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(jArr[0]);
        for (int i2 = 1; i2 < jArr.length; i2++) {
            sb.append(", ");
            sb.append(jArr[i2]);
        }
        return sb.toString();
    }

    @Override // java.util.Hashtable, java.util.Map
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override // java.util.Hashtable, java.util.Map
    public int hashCode() {
        return System.identityHashCode(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String[] s(String... strArr) {
        return strArr;
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/SunPKCS11$Descriptor.class */
    private static final class Descriptor {
        final String type;
        final String algorithm;
        final String className;
        final String[] aliases;
        final int[] mechanisms;

        private Descriptor(String str, String str2, String str3, String[] strArr, int[] iArr) {
            this.type = str;
            this.algorithm = str2;
            this.className = str3;
            this.aliases = strArr;
            this.mechanisms = iArr;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public P11Service service(Token token, int i2) {
            return new P11Service(token, this.type, this.algorithm, this.className, this.aliases, i2);
        }

        public String toString() {
            return this.type + "." + this.algorithm;
        }
    }

    private static int[] m(long j2) {
        return new int[]{(int) j2};
    }

    private static int[] m(long j2, long j3) {
        return new int[]{(int) j2, (int) j3};
    }

    private static int[] m(long j2, long j3, long j4) {
        return new int[]{(int) j2, (int) j3, (int) j4};
    }

    private static int[] m(long j2, long j3, long j4, long j5) {
        return new int[]{(int) j2, (int) j3, (int) j4, (int) j5};
    }

    private static void d(String str, String str2, String str3, int[] iArr) {
        register(new Descriptor(str, str2, str3, null, iArr));
    }

    private static void d(String str, String str2, String str3, String[] strArr, int[] iArr) {
        register(new Descriptor(str, str2, str3, strArr, iArr));
    }

    private static void register(Descriptor descriptor) {
        for (int i2 = 0; i2 < descriptor.mechanisms.length; i2++) {
            Integer numValueOf = Integer.valueOf(descriptor.mechanisms[i2]);
            List<Descriptor> arrayList = descriptors.get(numValueOf);
            if (arrayList == null) {
                arrayList = new ArrayList();
                descriptors.put(numValueOf, arrayList);
            }
            arrayList.add(descriptor);
        }
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/SunPKCS11$TokenPoller.class */
    private static class TokenPoller implements Runnable {
        private final SunPKCS11 provider;
        private volatile boolean enabled;

        private TokenPoller(SunPKCS11 sunPKCS11) {
            this.provider = sunPKCS11;
            this.enabled = true;
        }

        @Override // java.lang.Runnable
        public void run() {
            int insertionCheckInterval = this.provider.config.getInsertionCheckInterval();
            while (this.enabled) {
                try {
                    Thread.sleep(insertionCheckInterval);
                    if (this.enabled) {
                        try {
                            this.provider.initToken(null);
                        } catch (PKCS11Exception e2) {
                        }
                    } else {
                        return;
                    }
                } catch (InterruptedException e3) {
                    return;
                }
            }
        }

        void disable() {
            this.enabled = false;
        }
    }

    private void createPoller() {
        if (this.poller != null) {
            return;
        }
        TokenPoller tokenPoller = new TokenPoller();
        Thread thread = new Thread(tokenPoller, "Poller " + getName());
        thread.setDaemon(true);
        thread.setPriority(1);
        thread.start();
        this.poller = tokenPoller;
    }

    private void destroyPoller() {
        if (this.poller != null) {
            this.poller.disable();
            this.poller = null;
        }
    }

    private boolean hasValidToken() {
        Token token = this.token;
        return token != null && token.isValid();
    }

    synchronized void uninitToken(Token token) {
        if (this.token != token) {
            return;
        }
        destroyPoller();
        this.token = null;
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: sun.security.pkcs11.SunPKCS11.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                SunPKCS11.this.clear();
                return null;
            }
        });
        createPoller();
    }

    private static boolean isLegacy(CK_MECHANISM_INFO ck_mechanism_info) throws PKCS11Exception {
        boolean z2 = false;
        if (ck_mechanism_info != null) {
            if ((ck_mechanism_info.flags & 512) != 0) {
                z2 = false | ((ck_mechanism_info.flags & 256) == 0);
            }
            if ((ck_mechanism_info.flags & 8192) != 0) {
                z2 |= (ck_mechanism_info.flags & 2048) == 0;
            }
        }
        return z2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initToken(CK_SLOT_INFO ck_slot_info) throws PKCS11Exception {
        if (ck_slot_info == null) {
            ck_slot_info = this.p11.C_GetSlotInfo(this.slotID);
        }
        if (this.removable && (ck_slot_info.flags & 1) == 0) {
            createPoller();
            return;
        }
        destroyPoller();
        boolean showInfo = this.config.getShowInfo();
        if (showInfo) {
            System.out.println("Slot info for slot " + this.slotID + CallSiteDescriptor.TOKEN_DELIMITER);
            System.out.println(ck_slot_info);
        }
        final Token token = new Token(this);
        if (showInfo) {
            System.out.println("Token info for token in slot " + this.slotID + CallSiteDescriptor.TOKEN_DELIMITER);
            System.out.println(token.tokenInfo);
        }
        long[] jArrC_GetMechanismList = this.p11.C_GetMechanismList(this.slotID);
        final HashMap map = new HashMap();
        for (long j2 : jArrC_GetMechanismList) {
            CK_MECHANISM_INFO mechanismInfo = token.getMechanismInfo(j2);
            if (showInfo) {
                System.out.println("Mechanism " + Functions.getMechanismName(j2) + CallSiteDescriptor.TOKEN_DELIMITER);
                System.out.println(mechanismInfo == null ? "  info n/a" : mechanismInfo);
            }
            if (!this.config.isEnabled(j2)) {
                if (showInfo) {
                    System.out.println("DISABLED in configuration");
                }
            } else if (isLegacy(mechanismInfo)) {
                if (showInfo) {
                    System.out.println("DISABLED due to legacy");
                }
            } else if ((j2 >>> 32) != 0) {
                if (showInfo) {
                    System.out.println("DISABLED due to unknown mech value");
                }
            } else {
                int i2 = (int) j2;
                Integer numValueOf = Integer.valueOf(i2);
                List<Descriptor> list = descriptors.get(numValueOf);
                if (list != null) {
                    for (Descriptor descriptor : list) {
                        Integer num = (Integer) map.get(descriptor);
                        if (num == null) {
                            map.put(descriptor, numValueOf);
                        } else {
                            int iIntValue = num.intValue();
                            int i3 = 0;
                            while (true) {
                                if (i3 >= descriptor.mechanisms.length) {
                                    break;
                                }
                                int i4 = descriptor.mechanisms[i3];
                                if (i2 == i4) {
                                    map.put(descriptor, numValueOf);
                                    break;
                                } else if (iIntValue == i4) {
                                    break;
                                } else {
                                    i3++;
                                }
                            }
                        }
                    }
                }
            }
        }
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: sun.security.pkcs11.SunPKCS11.2
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                for (Map.Entry entry : map.entrySet()) {
                    SunPKCS11.this.putService(((Descriptor) entry.getKey()).service(token, ((Integer) entry.getValue()).intValue()));
                }
                if ((token.tokenInfo.flags & 1) != 0 && SunPKCS11.this.config.isEnabled(PKCS11Constants.PCKM_SECURERANDOM) && !token.sessionManager.lowMaxSessions()) {
                    SunPKCS11.this.putService(new P11Service(token, SunPKCS11.SR, "PKCS11", "sun.security.pkcs11.P11SecureRandom", null, PKCS11Constants.PCKM_SECURERANDOM));
                }
                if (SunPKCS11.this.config.isEnabled(PKCS11Constants.PCKM_KEYSTORE)) {
                    SunPKCS11.this.putService(new P11Service(token, SunPKCS11.KS, "PKCS11", "sun.security.pkcs11.P11KeyStore", SunPKCS11.s("PKCS11-" + SunPKCS11.this.config.getName()), PKCS11Constants.PCKM_KEYSTORE));
                    return null;
                }
                return null;
            }
        });
        this.token = token;
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/SunPKCS11$P11Service.class */
    private static final class P11Service extends Provider.Service {
        private final Token token;
        private final long mechanism;

        P11Service(Token token, String str, String str2, String str3, String[] strArr, long j2) {
            super(token.provider, str, str2, str3, toList(strArr), null);
            this.token = token;
            this.mechanism = j2 & 4294967295L;
        }

        private static List<String> toList(String[] strArr) {
            if (strArr == null) {
                return null;
            }
            return Arrays.asList(strArr);
        }

        @Override // java.security.Provider.Service
        public Object newInstance(Object obj) throws NoSuchAlgorithmException {
            if (!this.token.isValid()) {
                throw new NoSuchAlgorithmException("Token has been removed");
            }
            try {
                return newInstance0(obj);
            } catch (PKCS11Exception e2) {
                throw new NoSuchAlgorithmException(e2);
            }
        }

        public Object newInstance0(Object obj) throws PKCS11Exception, NoSuchAlgorithmException {
            String algorithm = getAlgorithm();
            String type = getType();
            if (type == SunPKCS11.MD) {
                return new P11Digest(this.token, algorithm, this.mechanism);
            }
            if (type == SunPKCS11.CIP) {
                if (algorithm.startsWith("RSA")) {
                    return new P11RSACipher(this.token, algorithm, this.mechanism);
                }
                if (algorithm.endsWith("GCM/NoPadding")) {
                    return new P11AEADCipher(this.token, algorithm, this.mechanism);
                }
                return new P11Cipher(this.token, algorithm, this.mechanism);
            }
            if (type == "Signature") {
                if (algorithm.indexOf("RSASSA-PSS") != -1) {
                    return new P11PSSSignature(this.token, algorithm, this.mechanism);
                }
                return new P11Signature(this.token, algorithm, this.mechanism);
            }
            if (type == "Mac") {
                return new P11Mac(this.token, algorithm, this.mechanism);
            }
            if (type == SunPKCS11.KPG) {
                return new P11KeyPairGenerator(this.token, algorithm, this.mechanism);
            }
            if (type == SunPKCS11.KA) {
                if (algorithm.equals("ECDH")) {
                    return new P11ECDHKeyAgreement(this.token, algorithm, this.mechanism);
                }
                return new P11KeyAgreement(this.token, algorithm, this.mechanism);
            }
            if (type == SunPKCS11.KF) {
                return this.token.getKeyFactory(algorithm);
            }
            if (type == SunPKCS11.SKF) {
                return new P11SecretKeyFactory(this.token, algorithm);
            }
            if (type == SunPKCS11.KG) {
                if (algorithm == "SunTlsRsaPremasterSecret") {
                    return new P11TlsRsaPremasterSecretGenerator(this.token, algorithm, this.mechanism);
                }
                if (algorithm == "SunTlsMasterSecret" || algorithm == "SunTls12MasterSecret") {
                    return new P11TlsMasterSecretGenerator(this.token, algorithm, this.mechanism);
                }
                if (algorithm == "SunTlsKeyMaterial" || algorithm == "SunTls12KeyMaterial") {
                    return new P11TlsKeyMaterialGenerator(this.token, algorithm, this.mechanism);
                }
                if (algorithm == "SunTlsPrf" || algorithm == "SunTls12Prf") {
                    return new P11TlsPrfGenerator(this.token, algorithm, this.mechanism);
                }
                return new P11KeyGenerator(this.token, algorithm, this.mechanism);
            }
            if (type == SunPKCS11.SR) {
                return this.token.getRandom();
            }
            if (type == SunPKCS11.KS) {
                return this.token.getKeyStore();
            }
            if (type == SunPKCS11.AGP) {
                if (algorithm == "EC") {
                    return new ECParameters();
                }
                if (algorithm == "GCM") {
                    return new GCMParameters();
                }
                throw new NoSuchAlgorithmException("Unsupported algorithm: " + algorithm);
            }
            throw new NoSuchAlgorithmException("Unknown type: " + type);
        }

        @Override // java.security.Provider.Service
        public boolean supportsParameter(Object obj) {
            if (obj == null || !this.token.isValid()) {
                return false;
            }
            if (!(obj instanceof Key)) {
                throw new InvalidParameterException("Parameter must be a Key");
            }
            String algorithm = getAlgorithm();
            String type = getType();
            Key key = (Key) obj;
            String algorithm2 = key.getAlgorithm();
            if ((type == SunPKCS11.CIP && algorithm.startsWith("RSA")) || (type == "Signature" && algorithm.indexOf("RSA") != -1)) {
                if (algorithm2.equals("RSA")) {
                    return isLocalKey(key) || (key instanceof RSAPrivateKey) || (key instanceof RSAPublicKey);
                }
                return false;
            }
            if ((type == SunPKCS11.KA && algorithm.equals("ECDH")) || (type == "Signature" && algorithm.endsWith("ECDSA"))) {
                if (algorithm2.equals("EC")) {
                    return isLocalKey(key) || (key instanceof ECPrivateKey) || (key instanceof ECPublicKey);
                }
                return false;
            }
            if (type == "Signature" && algorithm.endsWith("DSA")) {
                if (algorithm2.equals("DSA")) {
                    return isLocalKey(key) || (key instanceof DSAPrivateKey) || (key instanceof DSAPublicKey);
                }
                return false;
            }
            if (type == SunPKCS11.CIP || type == "Mac") {
                return isLocalKey(key) || "RAW".equals(key.getFormat());
            }
            if (type == SunPKCS11.KA) {
                if (algorithm2.equals("DH")) {
                    return isLocalKey(key) || (key instanceof DHPrivateKey) || (key instanceof DHPublicKey);
                }
                return false;
            }
            throw new AssertionError((Object) ("SunPKCS11 error: " + type + ", " + algorithm));
        }

        private boolean isLocalKey(Key key) {
            return (key instanceof P11Key) && ((P11Key) key).token == this.token;
        }

        @Override // java.security.Provider.Service
        public String toString() {
            return super.toString() + " (" + Functions.getMechanismName(this.mechanism) + ")";
        }
    }

    @Override // java.security.AuthProvider
    public void login(Subject subject, CallbackHandler callbackHandler) throws LoginException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            if (debug != null) {
                debug.println("checking login permission");
            }
            securityManager.checkPermission(new SecurityPermission("authProvider." + getName()));
        }
        if (!hasValidToken()) {
            throw new LoginException("No token present");
        }
        if ((this.token.tokenInfo.flags & 4) == 0) {
            if (debug != null) {
                debug.println("login operation not required for token - ignoring login request");
                return;
            }
            return;
        }
        try {
            if (this.token.isLoggedInNow(null)) {
                if (debug != null) {
                    debug.println("user already logged in");
                    return;
                }
                return;
            }
        } catch (PKCS11Exception e2) {
        }
        char[] password = null;
        if ((this.token.tokenInfo.flags & 256) == 0) {
            CallbackHandler callbackHandler2 = getCallbackHandler(callbackHandler);
            if (callbackHandler2 == null) {
                throw new LoginException("no password provided, and no callback handler available for retrieving password");
            }
            PasswordCallback passwordCallback = new PasswordCallback(new MessageFormat(ResourcesMgr.getString("PKCS11.Token.providerName.Password.")).format(new Object[]{getName()}), false);
            try {
                callbackHandler2.handle(new Callback[]{passwordCallback});
                password = passwordCallback.getPassword();
                passwordCallback.clearPassword();
                if (password == null && debug != null) {
                    debug.println("caller passed NULL pin");
                }
            } catch (Exception e3) {
                LoginException loginException = new LoginException("Unable to perform password callback");
                loginException.initCause(e3);
                throw loginException;
            }
        }
        Session opSession = null;
        try {
            try {
                opSession = this.token.getOpSession();
                this.p11.C_Login(opSession.id(), 1L, password);
                if (debug != null) {
                    debug.println("login succeeded");
                }
                this.token.releaseSession(opSession);
                if (password != null) {
                    Arrays.fill(password, ' ');
                }
            } catch (PKCS11Exception e4) {
                if (e4.getErrorCode() == 256) {
                    if (debug != null) {
                        debug.println("user already logged in");
                    }
                    this.token.releaseSession(opSession);
                    if (password == null) {
                        return;
                    }
                    Arrays.fill(password, ' ');
                    return;
                }
                if (e4.getErrorCode() == 160) {
                    FailedLoginException failedLoginException = new FailedLoginException();
                    failedLoginException.initCause(e4);
                    throw failedLoginException;
                }
                LoginException loginException2 = new LoginException();
                loginException2.initCause(e4);
                throw loginException2;
            }
        } catch (Throwable th) {
            this.token.releaseSession(opSession);
            if (password != null) {
                Arrays.fill(password, ' ');
            }
            throw th;
        }
    }

    @Override // java.security.AuthProvider
    public void logout() throws LoginException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new SecurityPermission("authProvider." + getName()));
        }
        if (hasValidToken()) {
            if ((this.token.tokenInfo.flags & 4) == 0) {
                if (debug != null) {
                    debug.println("logout operation not required for token - ignoring logout request");
                    return;
                }
                return;
            }
            try {
                if (!this.token.isLoggedInNow(null)) {
                    if (debug != null) {
                        debug.println("user not logged in");
                        return;
                    }
                    return;
                }
            } catch (PKCS11Exception e2) {
            }
            Session opSession = null;
            try {
                try {
                    opSession = this.token.getOpSession();
                    this.p11.C_Logout(opSession.id());
                    if (debug != null) {
                        debug.println("logout succeeded");
                    }
                    this.token.releaseSession(opSession);
                } catch (Throwable th) {
                    this.token.releaseSession(opSession);
                    throw th;
                }
            } catch (PKCS11Exception e3) {
                if (e3.getErrorCode() == 257) {
                    if (debug != null) {
                        debug.println("user not logged in");
                    }
                    this.token.releaseSession(opSession);
                } else {
                    LoginException loginException = new LoginException();
                    loginException.initCause(e3);
                    throw loginException;
                }
            }
        }
    }

    @Override // java.security.AuthProvider
    public void setCallbackHandler(CallbackHandler callbackHandler) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new SecurityPermission("authProvider." + getName()));
        }
        synchronized (this.LOCK_HANDLER) {
            this.pHandler = callbackHandler;
        }
    }

    private CallbackHandler getCallbackHandler(CallbackHandler callbackHandler) {
        if (callbackHandler != null) {
            return callbackHandler;
        }
        if (debug != null) {
            debug.println("getting provider callback handler");
        }
        synchronized (this.LOCK_HANDLER) {
            if (this.pHandler != null) {
                return this.pHandler;
            }
            try {
                if (debug != null) {
                    debug.println("getting default callback handler");
                }
                CallbackHandler callbackHandler2 = (CallbackHandler) AccessController.doPrivileged(new PrivilegedExceptionAction<CallbackHandler>() { // from class: sun.security.pkcs11.SunPKCS11.3
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public CallbackHandler run() throws Exception {
                        String property = Security.getProperty("auth.login.defaultCallbackHandler");
                        if (property == null || property.length() == 0) {
                            if (SunPKCS11.debug != null) {
                                SunPKCS11.debug.println("no default handler set");
                                return null;
                            }
                            return null;
                        }
                        return (CallbackHandler) Class.forName(property, true, Thread.currentThread().getContextClassLoader()).newInstance();
                    }
                });
                this.pHandler = callbackHandler2;
                return callbackHandler2;
            } catch (PrivilegedActionException e2) {
                if (debug != null) {
                    debug.println("Unable to load default callback handler");
                    e2.printStackTrace();
                }
                return null;
            }
        }
    }

    private Object writeReplace() throws ObjectStreamException {
        return new SunPKCS11Rep(this);
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/SunPKCS11$SunPKCS11Rep.class */
    private static class SunPKCS11Rep implements Serializable {
        static final long serialVersionUID = -2896606995897745419L;
        private final String providerName;
        private final String configName;

        SunPKCS11Rep(SunPKCS11 sunPKCS11) throws NotSerializableException {
            this.providerName = sunPKCS11.getName();
            this.configName = sunPKCS11.configName;
            if (Security.getProvider(this.providerName) != sunPKCS11) {
                throw new NotSerializableException("Only SunPKCS11 providers installed in java.security.Security can be serialized");
            }
        }

        private Object readResolve() throws ObjectStreamException {
            SunPKCS11 sunPKCS11 = (SunPKCS11) Security.getProvider(this.providerName);
            if (sunPKCS11 == null || !sunPKCS11.configName.equals(this.configName)) {
                throw new NotSerializableException("Could not find " + this.providerName + " in installed providers");
            }
            return sunPKCS11;
        }
    }
}
