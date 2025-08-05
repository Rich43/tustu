package sun.security.pkcs12;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.AccessController;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.PKCS12Attribute;
import java.security.PrivateKey;
import java.security.PrivilegedAction;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.DestroyFailedException;
import javax.security.auth.x500.X500Principal;
import javax.xml.datatype.DatatypeConstants;
import jdk.management.jfr.StreamManager;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import net.lingala.zip4j.util.InternalZipConstants;
import org.icepdf.core.util.PdfOps;
import sun.security.pkcs.ContentInfo;
import sun.security.pkcs.EncryptedPrivateKeyInfo;
import sun.security.util.Debug;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;
import sun.security.util.SecurityProperties;
import sun.security.x509.AlgorithmId;

/* loaded from: rt.jar:sun/security/pkcs12/PKCS12KeyStore.class */
public final class PKCS12KeyStore extends KeyStoreSpi {
    public static final int VERSION_3 = 3;
    private static final int MAX_ITERATION_COUNT = 5000000;
    private static final int SALT_LEN = 20;
    private static final String[] CORE_ATTRIBUTES = {"1.2.840.113549.1.9.20", "1.2.840.113549.1.9.21", "2.16.840.1.113894.746875.1.1"};
    private static final Debug debug = Debug.getInstance("pkcs12");
    private static final int[] keyBag = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 12, 10, 1, 2};
    private static final int[] certBag = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 12, 10, 1, 3};
    private static final int[] secretBag = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 12, 10, 1, 5};
    private static final int[] pkcs9Name = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 9, 20};
    private static final int[] pkcs9KeyId = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 9, 21};
    private static final int[] pkcs9certType = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 9, 22, 1};
    private static final int[] pbes2 = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 5, 13};
    private static final int[] TrustedKeyUsage = {2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 113894, 746875, 1, 1};
    private static final int[] AnyExtendedKeyUsage = {2, 5, 29, 37, 0};
    private static final ObjectIdentifier PKCS8ShroudedKeyBag_OID;
    private static final ObjectIdentifier CertBag_OID;
    private static final ObjectIdentifier SecretBag_OID;
    private static final ObjectIdentifier PKCS9FriendlyName_OID;
    private static final ObjectIdentifier PKCS9LocalKeyId_OID;
    private static final ObjectIdentifier PKCS9CertType_OID;
    private static final ObjectIdentifier pbes2_OID;
    private static final ObjectIdentifier TrustedKeyUsage_OID;
    private static final ObjectIdentifier[] AnyUsage;
    private SecureRandom random;
    private int counter = 0;
    private int privateKeyCount = 0;
    private int secretKeyCount = 0;
    private int certificateCount = 0;
    private String certProtectionAlgorithm = null;
    private int certPbeIterationCount = -1;
    private String macAlgorithm = null;
    private int macIterationCount = -1;
    private Map<String, Entry> entries = Collections.synchronizedMap(new LinkedHashMap());
    private ArrayList<KeyEntry> keyList = new ArrayList<>();
    private LinkedHashMap<X500Principal, X509Certificate> certsMap = new LinkedHashMap<>();
    private ArrayList<CertEntry> certEntries = new ArrayList<>();

    static {
        try {
            PKCS8ShroudedKeyBag_OID = new ObjectIdentifier(keyBag);
            CertBag_OID = new ObjectIdentifier(certBag);
            SecretBag_OID = new ObjectIdentifier(secretBag);
            PKCS9FriendlyName_OID = new ObjectIdentifier(pkcs9Name);
            PKCS9LocalKeyId_OID = new ObjectIdentifier(pkcs9KeyId);
            PKCS9CertType_OID = new ObjectIdentifier(pkcs9certType);
            pbes2_OID = new ObjectIdentifier(pbes2);
            TrustedKeyUsage_OID = new ObjectIdentifier(TrustedKeyUsage);
            AnyUsage = new ObjectIdentifier[]{new ObjectIdentifier(AnyExtendedKeyUsage)};
        } catch (IOException e2) {
            throw new AssertionError("OID not initialized", e2);
        }
    }

    /* loaded from: rt.jar:sun/security/pkcs12/PKCS12KeyStore$Entry.class */
    private static class Entry {
        Date date;
        String alias;
        byte[] keyId;
        Set<KeyStore.Entry.Attribute> attributes;

        private Entry() {
        }
    }

    /* loaded from: rt.jar:sun/security/pkcs12/PKCS12KeyStore$KeyEntry.class */
    private static class KeyEntry extends Entry {
        private KeyEntry() {
            super();
        }
    }

    /* loaded from: rt.jar:sun/security/pkcs12/PKCS12KeyStore$PrivateKeyEntry.class */
    private static class PrivateKeyEntry extends KeyEntry {
        byte[] protectedPrivKey;
        Certificate[] chain;

        private PrivateKeyEntry() {
            super();
        }
    }

    /* loaded from: rt.jar:sun/security/pkcs12/PKCS12KeyStore$SecretKeyEntry.class */
    private static class SecretKeyEntry extends KeyEntry {
        byte[] protectedSecretKey;

        private SecretKeyEntry() {
            super();
        }
    }

    /* loaded from: rt.jar:sun/security/pkcs12/PKCS12KeyStore$CertEntry.class */
    private static class CertEntry extends Entry {
        final X509Certificate cert;
        ObjectIdentifier[] trustedKeyUsage;

        CertEntry(X509Certificate x509Certificate, byte[] bArr, String str) {
            this(x509Certificate, bArr, str, null, null);
        }

        CertEntry(X509Certificate x509Certificate, byte[] bArr, String str, ObjectIdentifier[] objectIdentifierArr, Set<? extends KeyStore.Entry.Attribute> set) {
            super();
            this.date = new Date();
            this.cert = x509Certificate;
            this.keyId = bArr;
            this.alias = str;
            this.trustedKeyUsage = objectIdentifierArr;
            this.attributes = new HashSet();
            if (set != null) {
                this.attributes.addAll(set);
            }
        }
    }

    @FunctionalInterface
    /* loaded from: rt.jar:sun/security/pkcs12/PKCS12KeyStore$RetryWithZero.class */
    private interface RetryWithZero<T> {
        T tryOnce(char[] cArr) throws Exception;

        static <S> S run(RetryWithZero<S> retryWithZero, char[] cArr) throws Exception {
            try {
                return retryWithZero.tryOnce(cArr);
            } catch (Exception e2) {
                if (cArr.length == 0) {
                    if (PKCS12KeyStore.debug != null) {
                        PKCS12KeyStore.debug.println("Retry with a NUL password");
                    }
                    return retryWithZero.tryOnce(new char[1]);
                }
                throw e2;
            }
        }
    }

    @Override // java.security.KeyStoreSpi
    public Key engineGetKey(String str, char[] cArr) throws UnrecoverableKeyException, NoSuchAlgorithmException, IOException {
        byte[] bArr;
        int iterationCount;
        Entry entry = this.entries.get(str.toLowerCase(Locale.ENGLISH));
        if (entry == null || !(entry instanceof KeyEntry)) {
            return null;
        }
        if (entry instanceof PrivateKeyEntry) {
            bArr = ((PrivateKeyEntry) entry).protectedPrivKey;
        } else if (entry instanceof SecretKeyEntry) {
            bArr = ((SecretKeyEntry) entry).protectedSecretKey;
        } else {
            throw new UnrecoverableKeyException("Error locating key");
        }
        try {
            EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo(bArr);
            byte[] encryptedData = encryptedPrivateKeyInfo.getEncryptedData();
            DerInputStream derInputStream = new DerValue(encryptedPrivateKeyInfo.getAlgorithm().encode()).toDerInputStream();
            ObjectIdentifier oid = derInputStream.getOID();
            AlgorithmParameters algParameters = parseAlgParameters(oid, derInputStream);
            try {
                if (algParameters != null) {
                    try {
                        iterationCount = ((PBEParameterSpec) algParameters.getParameterSpec(PBEParameterSpec.class)).getIterationCount();
                        if (iterationCount > MAX_ITERATION_COUNT) {
                            throw new IOException("key PBE iteration count too large");
                        }
                    } catch (InvalidParameterSpecException e2) {
                        throw new IOException("Invalid PBE algorithm parameters");
                    }
                } else {
                    iterationCount = 0;
                }
                int i2 = iterationCount;
                return (Key) RetryWithZero.run(cArr2 -> {
                    Key keyGenerateSecret;
                    SecretKey pBEKey = getPBEKey(cArr2);
                    Cipher cipher = Cipher.getInstance(mapPBEParamsToAlgorithm(oid, algParameters));
                    cipher.init(2, pBEKey, algParameters);
                    byte[] bArrDoFinal = cipher.doFinal(encryptedData);
                    DerInputStream derInputStream2 = new DerValue(bArrDoFinal).toDerInputStream();
                    derInputStream2.getInteger();
                    DerValue[] sequence = derInputStream2.getSequence(2);
                    if (sequence.length < 1 || sequence.length > 2) {
                        throw new IOException("Invalid length for AlgorithmIdentifier");
                    }
                    String name = new AlgorithmId(sequence[0].getOID()).getName();
                    if (entry instanceof PrivateKeyEntry) {
                        PrivateKey privateKeyGeneratePrivate = KeyFactory.getInstance(name).generatePrivate(new PKCS8EncodedKeySpec(bArrDoFinal));
                        if (debug != null) {
                            debug.println("Retrieved a protected private key at alias '" + str + "' (" + mapPBEParamsToAlgorithm(oid, algParameters) + " iterations: " + i2 + ")");
                        }
                        return privateKeyGeneratePrivate;
                    }
                    SecretKeySpec secretKeySpec = new SecretKeySpec(derInputStream2.getOctetString(), name);
                    if (name.startsWith("PBE")) {
                        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(name);
                        keyGenerateSecret = secretKeyFactory.generateSecret(secretKeyFactory.getKeySpec(secretKeySpec, PBEKeySpec.class));
                    } else {
                        keyGenerateSecret = secretKeySpec;
                    }
                    if (debug != null) {
                        debug.println("Retrieved a protected secret key at alias '" + str + "' (" + mapPBEParamsToAlgorithm(oid, algParameters) + " iterations: " + i2 + ")");
                    }
                    return keyGenerateSecret;
                }, cArr);
            } catch (Exception e3) {
                UnrecoverableKeyException unrecoverableKeyException = new UnrecoverableKeyException("Get Key failed: " + e3.getMessage());
                unrecoverableKeyException.initCause(e3);
                throw unrecoverableKeyException;
            }
        } catch (IOException e4) {
            UnrecoverableKeyException unrecoverableKeyException2 = new UnrecoverableKeyException("Private key not stored as PKCS#8 EncryptedPrivateKeyInfo: " + ((Object) e4));
            unrecoverableKeyException2.initCause(e4);
            throw unrecoverableKeyException2;
        }
    }

    @Override // java.security.KeyStoreSpi
    public Certificate[] engineGetCertificateChain(String str) {
        Entry entry = this.entries.get(str.toLowerCase(Locale.ENGLISH));
        if (entry == null || !(entry instanceof PrivateKeyEntry) || ((PrivateKeyEntry) entry).chain == null) {
            return null;
        }
        if (debug != null) {
            debug.println("Retrieved a " + ((PrivateKeyEntry) entry).chain.length + "-certificate chain at alias '" + str + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        return (Certificate[]) ((PrivateKeyEntry) entry).chain.clone();
    }

    @Override // java.security.KeyStoreSpi
    public Certificate engineGetCertificate(String str) {
        Entry entry = this.entries.get(str.toLowerCase(Locale.ENGLISH));
        if (entry == null) {
            return null;
        }
        if ((entry instanceof CertEntry) && ((CertEntry) entry).trustedKeyUsage != null) {
            if (debug != null) {
                if (Arrays.equals(AnyUsage, ((CertEntry) entry).trustedKeyUsage)) {
                    debug.println("Retrieved a certificate at alias '" + str + "' (trusted for any purpose)");
                } else {
                    debug.println("Retrieved a certificate at alias '" + str + "' (trusted for limited purposes)");
                }
            }
            return ((CertEntry) entry).cert;
        }
        if (!(entry instanceof PrivateKeyEntry) || ((PrivateKeyEntry) entry).chain == null) {
            return null;
        }
        if (debug != null) {
            debug.println("Retrieved a certificate at alias '" + str + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        return ((PrivateKeyEntry) entry).chain[0];
    }

    @Override // java.security.KeyStoreSpi
    public Date engineGetCreationDate(String str) {
        Entry entry = this.entries.get(str.toLowerCase(Locale.ENGLISH));
        if (entry != null) {
            return new Date(entry.date.getTime());
        }
        return null;
    }

    @Override // java.security.KeyStoreSpi
    public synchronized void engineSetKeyEntry(String str, Key key, char[] cArr, Certificate[] certificateArr) throws KeyStoreException {
        KeyStore.PasswordProtection passwordProtection = new KeyStore.PasswordProtection(cArr);
        try {
            setKeyEntry(str, key, passwordProtection, certificateArr, null);
        } finally {
            try {
                passwordProtection.destroy();
            } catch (DestroyFailedException e2) {
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void setKeyEntry(String str, Key key, KeyStore.PasswordProtection passwordProtection, Certificate[] certificateArr, Set<KeyStore.Entry.Attribute> set) throws KeyStoreException {
        SecretKeyEntry secretKeyEntry;
        try {
            if (key instanceof PrivateKey) {
                PrivateKeyEntry privateKeyEntry = new PrivateKeyEntry();
                privateKeyEntry.date = new Date();
                if (key.getFormat().equals("PKCS#8") || key.getFormat().equals("PKCS8")) {
                    if (debug != null) {
                        debug.println("Setting a protected private key at alias '" + str + PdfOps.SINGLE_QUOTE_TOKEN);
                    }
                    privateKeyEntry.protectedPrivKey = encryptPrivateKey(key.getEncoded(), passwordProtection);
                    if (certificateArr != null) {
                        if (certificateArr.length > 1 && !validateChain(certificateArr)) {
                            throw new KeyStoreException("Certificate chain is not valid");
                        }
                        privateKeyEntry.chain = (Certificate[]) certificateArr.clone();
                        this.certificateCount += certificateArr.length;
                        if (debug != null) {
                            debug.println("Setting a " + certificateArr.length + "-certificate chain at alias '" + str + PdfOps.SINGLE_QUOTE_TOKEN);
                        }
                    }
                    this.privateKeyCount++;
                    secretKeyEntry = privateKeyEntry;
                } else {
                    throw new KeyStoreException("Private key is not encodedas PKCS#8");
                }
            } else if (key instanceof SecretKey) {
                SecretKeyEntry secretKeyEntry2 = new SecretKeyEntry();
                secretKeyEntry2.date = new Date();
                DerOutputStream derOutputStream = new DerOutputStream();
                DerOutputStream derOutputStream2 = new DerOutputStream();
                derOutputStream2.putInteger(0);
                AlgorithmId.get(key.getAlgorithm()).encode(derOutputStream2);
                derOutputStream2.putOctetString(key.getEncoded());
                derOutputStream.write((byte) 48, derOutputStream2);
                secretKeyEntry2.protectedSecretKey = encryptPrivateKey(derOutputStream.toByteArray(), passwordProtection);
                if (debug != null) {
                    debug.println("Setting a protected secret key at alias '" + str + PdfOps.SINGLE_QUOTE_TOKEN);
                }
                this.secretKeyCount++;
                secretKeyEntry = secretKeyEntry2;
            } else {
                throw new KeyStoreException("Unsupported Key type");
            }
            secretKeyEntry.attributes = new HashSet();
            if (set != null) {
                secretKeyEntry.attributes.addAll(set);
            }
            secretKeyEntry.keyId = ("Time " + secretKeyEntry.date.getTime()).getBytes(InternalZipConstants.CHARSET_UTF8);
            secretKeyEntry.alias = str.toLowerCase(Locale.ENGLISH);
            this.entries.put(str.toLowerCase(Locale.ENGLISH), secretKeyEntry);
        } catch (Exception e2) {
            throw new KeyStoreException("Key protection algorithm not found: " + ((Object) e2), e2);
        }
    }

    @Override // java.security.KeyStoreSpi
    public synchronized void engineSetKeyEntry(String str, byte[] bArr, Certificate[] certificateArr) throws KeyStoreException {
        try {
            new EncryptedPrivateKeyInfo(bArr);
            PrivateKeyEntry privateKeyEntry = new PrivateKeyEntry();
            privateKeyEntry.date = new Date();
            if (debug != null) {
                debug.println("Setting a protected private key at alias '" + str + PdfOps.SINGLE_QUOTE_TOKEN);
            }
            try {
                privateKeyEntry.keyId = ("Time " + privateKeyEntry.date.getTime()).getBytes(InternalZipConstants.CHARSET_UTF8);
            } catch (UnsupportedEncodingException e2) {
            }
            privateKeyEntry.alias = str.toLowerCase(Locale.ENGLISH);
            privateKeyEntry.protectedPrivKey = (byte[]) bArr.clone();
            if (certificateArr != null) {
                if (certificateArr.length > 1 && !validateChain(certificateArr)) {
                    throw new KeyStoreException("Certificate chain is not valid");
                }
                privateKeyEntry.chain = (Certificate[]) certificateArr.clone();
                this.certificateCount += certificateArr.length;
                if (debug != null) {
                    debug.println("Setting a " + privateKeyEntry.chain.length + "-certificate chain at alias '" + str + PdfOps.SINGLE_QUOTE_TOKEN);
                }
            }
            this.privateKeyCount++;
            this.entries.put(str.toLowerCase(Locale.ENGLISH), privateKeyEntry);
        } catch (IOException e3) {
            throw new KeyStoreException("Private key is not stored as PKCS#8 EncryptedPrivateKeyInfo: " + ((Object) e3), e3);
        }
    }

    private byte[] getSalt() {
        byte[] bArr = new byte[20];
        if (this.random == null) {
            this.random = new SecureRandom();
        }
        this.random.nextBytes(bArr);
        return bArr;
    }

    private AlgorithmParameters getPBEAlgorithmParameters(String str, int i2) throws IOException {
        PBEParameterSpec pBEParameterSpec = new PBEParameterSpec(getSalt(), i2);
        try {
            AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance(str);
            algorithmParameters.init(pBEParameterSpec);
            return algorithmParameters;
        } catch (Exception e2) {
            throw new IOException("getPBEAlgorithmParameters failed: " + e2.getMessage(), e2);
        }
    }

    private AlgorithmParameters parseAlgParameters(ObjectIdentifier objectIdentifier, DerInputStream derInputStream) throws IOException {
        DerValue derValue;
        AlgorithmParameters algorithmParameters = null;
        try {
            if (derInputStream.available() == 0) {
                derValue = null;
            } else {
                derValue = derInputStream.getDerValue();
                if (derValue.tag == 5) {
                    derValue = null;
                }
            }
            if (derValue != null) {
                if (objectIdentifier.equals((Object) pbes2_OID)) {
                    algorithmParameters = AlgorithmParameters.getInstance("PBES2");
                } else {
                    algorithmParameters = AlgorithmParameters.getInstance("PBE");
                }
                algorithmParameters.init(derValue.toByteArray());
            }
            return algorithmParameters;
        } catch (Exception e2) {
            throw new IOException("parseAlgParameters failed: " + e2.getMessage(), e2);
        }
    }

    private SecretKey getPBEKey(char[] cArr) throws IOException {
        try {
            PBEKeySpec pBEKeySpec = new PBEKeySpec(cArr);
            SecretKey secretKeyGenerateSecret = SecretKeyFactory.getInstance("PBE").generateSecret(pBEKeySpec);
            pBEKeySpec.clearPassword();
            return secretKeyGenerateSecret;
        } catch (Exception e2) {
            throw new IOException("getSecretKey failed: " + e2.getMessage(), e2);
        }
    }

    private byte[] encryptPrivateKey(byte[] bArr, KeyStore.PasswordProtection passwordProtection) throws UnrecoverableKeyException, NoSuchAlgorithmException, IOException {
        AlgorithmParameters pBEAlgorithmParameters;
        try {
            String protectionAlgorithm = passwordProtection.getProtectionAlgorithm();
            if (protectionAlgorithm != null) {
                AlgorithmParameterSpec protectionParameters = passwordProtection.getProtectionParameters();
                if (protectionParameters != null) {
                    pBEAlgorithmParameters = AlgorithmParameters.getInstance(protectionAlgorithm);
                    pBEAlgorithmParameters.init(protectionParameters);
                } else {
                    pBEAlgorithmParameters = getPBEAlgorithmParameters(protectionAlgorithm, defaultKeyPbeIterationCount());
                }
            } else {
                protectionAlgorithm = defaultKeyProtectionAlgorithm();
                pBEAlgorithmParameters = getPBEAlgorithmParameters(protectionAlgorithm, defaultKeyPbeIterationCount());
            }
            ObjectIdentifier objectIdentifierMapPBEAlgorithmToOID = mapPBEAlgorithmToOID(protectionAlgorithm);
            if (objectIdentifierMapPBEAlgorithmToOID == null) {
                throw new IOException("PBE algorithm '" + protectionAlgorithm + " 'is not supported for key entry protection");
            }
            SecretKey pBEKey = getPBEKey(passwordProtection.getPassword());
            Cipher cipher = Cipher.getInstance(protectionAlgorithm);
            cipher.init(1, pBEKey, pBEAlgorithmParameters);
            byte[] bArrDoFinal = cipher.doFinal(bArr);
            AlgorithmId algorithmId = new AlgorithmId(objectIdentifierMapPBEAlgorithmToOID, cipher.getParameters());
            if (debug != null) {
                debug.println("  (Cipher algorithm: " + cipher.getAlgorithm() + ")");
            }
            return new EncryptedPrivateKeyInfo(algorithmId, bArrDoFinal).getEncoded();
        } catch (Exception e2) {
            UnrecoverableKeyException unrecoverableKeyException = new UnrecoverableKeyException("Encrypt Private Key failed: " + e2.getMessage());
            unrecoverableKeyException.initCause(e2);
            throw unrecoverableKeyException;
        }
    }

    private static ObjectIdentifier mapPBEAlgorithmToOID(String str) throws NoSuchAlgorithmException {
        if (str.toLowerCase(Locale.ENGLISH).startsWith("pbewithhmacsha")) {
            return pbes2_OID;
        }
        return AlgorithmId.get(str).getOID();
    }

    private static String mapPBEParamsToAlgorithm(ObjectIdentifier objectIdentifier, AlgorithmParameters algorithmParameters) throws NoSuchAlgorithmException {
        if (objectIdentifier.equals((Object) pbes2_OID) && algorithmParameters != null) {
            return algorithmParameters.toString();
        }
        return new AlgorithmId(objectIdentifier).getName();
    }

    @Override // java.security.KeyStoreSpi
    public synchronized void engineSetCertificateEntry(String str, Certificate certificate) throws KeyStoreException {
        setCertEntry(str, certificate, null);
    }

    private void setCertEntry(String str, Certificate certificate, Set<KeyStore.Entry.Attribute> set) throws KeyStoreException {
        Entry entry = this.entries.get(str.toLowerCase(Locale.ENGLISH));
        if (entry != null && (entry instanceof KeyEntry)) {
            throw new KeyStoreException("Cannot overwrite own certificate");
        }
        CertEntry certEntry = new CertEntry((X509Certificate) certificate, null, str, AnyUsage, set);
        this.certificateCount++;
        this.entries.put(str.toLowerCase(Locale.ENGLISH), certEntry);
        if (debug != null) {
            debug.println("Setting a trusted certificate at alias '" + str + PdfOps.SINGLE_QUOTE_TOKEN);
        }
    }

    @Override // java.security.KeyStoreSpi
    public synchronized void engineDeleteEntry(String str) throws KeyStoreException {
        if (debug != null) {
            debug.println("Removing entry at alias '" + str + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        Entry entry = this.entries.get(str.toLowerCase(Locale.ENGLISH));
        if (entry instanceof PrivateKeyEntry) {
            PrivateKeyEntry privateKeyEntry = (PrivateKeyEntry) entry;
            if (privateKeyEntry.chain != null) {
                this.certificateCount -= privateKeyEntry.chain.length;
            }
            this.privateKeyCount--;
        } else if (entry instanceof CertEntry) {
            this.certificateCount--;
        } else if (entry instanceof SecretKeyEntry) {
            this.secretKeyCount--;
        }
        this.entries.remove(str.toLowerCase(Locale.ENGLISH));
    }

    @Override // java.security.KeyStoreSpi
    public Enumeration<String> engineAliases() {
        return Collections.enumeration(this.entries.keySet());
    }

    @Override // java.security.KeyStoreSpi
    public boolean engineContainsAlias(String str) {
        return this.entries.containsKey(str.toLowerCase(Locale.ENGLISH));
    }

    @Override // java.security.KeyStoreSpi
    public int engineSize() {
        return this.entries.size();
    }

    @Override // java.security.KeyStoreSpi
    public boolean engineIsKeyEntry(String str) {
        Entry entry = this.entries.get(str.toLowerCase(Locale.ENGLISH));
        if (entry != null && (entry instanceof KeyEntry)) {
            return true;
        }
        return false;
    }

    @Override // java.security.KeyStoreSpi
    public boolean engineIsCertificateEntry(String str) {
        Entry entry = this.entries.get(str.toLowerCase(Locale.ENGLISH));
        if (entry != null && (entry instanceof CertEntry) && ((CertEntry) entry).trustedKeyUsage != null) {
            return true;
        }
        return false;
    }

    @Override // java.security.KeyStoreSpi
    public boolean engineEntryInstanceOf(String str, Class<? extends KeyStore.Entry> cls) {
        if (cls == KeyStore.TrustedCertificateEntry.class) {
            return engineIsCertificateEntry(str);
        }
        Entry entry = this.entries.get(str.toLowerCase(Locale.ENGLISH));
        return cls == KeyStore.PrivateKeyEntry.class ? entry != null && (entry instanceof PrivateKeyEntry) : cls == KeyStore.SecretKeyEntry.class && entry != null && (entry instanceof SecretKeyEntry);
    }

    @Override // java.security.KeyStoreSpi
    public String engineGetCertificateAlias(Certificate certificate) {
        Certificate certificate2 = null;
        Enumeration<String> enumerationEngineAliases = engineAliases();
        while (enumerationEngineAliases.hasMoreElements()) {
            String strNextElement2 = enumerationEngineAliases.nextElement2();
            Entry entry = this.entries.get(strNextElement2);
            if (entry instanceof PrivateKeyEntry) {
                if (((PrivateKeyEntry) entry).chain != null) {
                    certificate2 = ((PrivateKeyEntry) entry).chain[0];
                }
            } else if ((entry instanceof CertEntry) && ((CertEntry) entry).trustedKeyUsage != null) {
                certificate2 = ((CertEntry) entry).cert;
            }
            if (certificate2 != null && certificate2.equals(certificate)) {
                return strNextElement2;
            }
        }
        return null;
    }

    @Override // java.security.KeyStoreSpi
    public synchronized void engineStore(OutputStream outputStream, char[] cArr) throws NoSuchAlgorithmException, IOException, CertificateException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.putInteger(3);
        derOutputStream.write(derOutputStream2.toByteArray());
        DerOutputStream derOutputStream3 = new DerOutputStream();
        DerOutputStream derOutputStream4 = new DerOutputStream();
        if (this.privateKeyCount > 0 || this.secretKeyCount > 0) {
            if (debug != null) {
                debug.println("Storing " + (this.privateKeyCount + this.secretKeyCount) + " protected key(s) in a PKCS#7 data");
            }
            new ContentInfo(createSafeContent()).encode(derOutputStream4);
        }
        if (this.certificateCount > 0) {
            if (this.certProtectionAlgorithm == null) {
                this.certProtectionAlgorithm = defaultCertProtectionAlgorithm();
            }
            if (this.certPbeIterationCount < 0) {
                this.certPbeIterationCount = defaultCertPbeIterationCount();
            }
            if (debug != null) {
                debug.println("Storing " + this.certificateCount + " certificate(s) in a PKCS#7 encryptedData");
            }
            byte[] bArrCreateEncryptedData = createEncryptedData(cArr);
            if (!this.certProtectionAlgorithm.equalsIgnoreCase("NONE")) {
                new ContentInfo(ContentInfo.ENCRYPTED_DATA_OID, new DerValue(bArrCreateEncryptedData)).encode(derOutputStream4);
            } else {
                new ContentInfo(bArrCreateEncryptedData).encode(derOutputStream4);
            }
        }
        DerOutputStream derOutputStream5 = new DerOutputStream();
        derOutputStream5.write((byte) 48, derOutputStream4);
        byte[] byteArray = derOutputStream5.toByteArray();
        new ContentInfo(byteArray).encode(derOutputStream3);
        derOutputStream.write(derOutputStream3.toByteArray());
        if (this.macAlgorithm == null) {
            this.macAlgorithm = defaultMacAlgorithm();
        }
        if (this.macIterationCount < 0) {
            this.macIterationCount = defaultMacIterationCount();
        }
        if (!this.macAlgorithm.equalsIgnoreCase("NONE")) {
            derOutputStream.write(calculateMac(cArr, byteArray));
        }
        DerOutputStream derOutputStream6 = new DerOutputStream();
        derOutputStream6.write((byte) 48, derOutputStream);
        outputStream.write(derOutputStream6.toByteArray());
        outputStream.flush();
    }

    @Override // java.security.KeyStoreSpi
    public KeyStore.Entry engineGetEntry(String str, KeyStore.ProtectionParameter protectionParameter) throws NoSuchAlgorithmException, IOException, KeyStoreException, UnrecoverableEntryException {
        if (!engineContainsAlias(str)) {
            return null;
        }
        Entry entry = this.entries.get(str.toLowerCase(Locale.ENGLISH));
        if (protectionParameter == null) {
            if (engineIsCertificateEntry(str)) {
                if ((entry instanceof CertEntry) && ((CertEntry) entry).trustedKeyUsage != null) {
                    if (debug != null) {
                        debug.println("Retrieved a trusted certificate at alias '" + str + PdfOps.SINGLE_QUOTE_TOKEN);
                    }
                    return new KeyStore.TrustedCertificateEntry(((CertEntry) entry).cert, getAttributes(entry));
                }
            } else {
                throw new UnrecoverableKeyException("requested entry requires a password");
            }
        }
        if (protectionParameter instanceof KeyStore.PasswordProtection) {
            if (engineIsCertificateEntry(str)) {
                throw new UnsupportedOperationException("trusted certificate entries are not password-protected");
            }
            if (engineIsKeyEntry(str)) {
                Key keyEngineGetKey = engineGetKey(str, ((KeyStore.PasswordProtection) protectionParameter).getPassword());
                if (keyEngineGetKey instanceof PrivateKey) {
                    return new KeyStore.PrivateKeyEntry((PrivateKey) keyEngineGetKey, engineGetCertificateChain(str), getAttributes(entry));
                }
                if (keyEngineGetKey instanceof SecretKey) {
                    return new KeyStore.SecretKeyEntry((SecretKey) keyEngineGetKey, getAttributes(entry));
                }
            } else if (!engineIsKeyEntry(str)) {
                throw new UnsupportedOperationException("untrusted certificate entries are not password-protected");
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override // java.security.KeyStoreSpi
    public synchronized void engineSetEntry(String str, KeyStore.Entry entry, KeyStore.ProtectionParameter protectionParameter) throws KeyStoreException {
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
            KeyStore.TrustedCertificateEntry trustedCertificateEntry = (KeyStore.TrustedCertificateEntry) entry;
            setCertEntry(str, trustedCertificateEntry.getTrustedCertificate(), trustedCertificateEntry.getAttributes());
            return;
        }
        if (entry instanceof KeyStore.PrivateKeyEntry) {
            if (passwordProtection == null || passwordProtection.getPassword() == null) {
                throw new KeyStoreException("non-null password required to create PrivateKeyEntry");
            }
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) entry;
            setKeyEntry(str, privateKeyEntry.getPrivateKey(), passwordProtection, privateKeyEntry.getCertificateChain(), privateKeyEntry.getAttributes());
            return;
        }
        if (entry instanceof KeyStore.SecretKeyEntry) {
            if (passwordProtection == null || passwordProtection.getPassword() == null) {
                throw new KeyStoreException("non-null password required to create SecretKeyEntry");
            }
            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) entry;
            setKeyEntry(str, secretKeyEntry.getSecretKey(), passwordProtection, (Certificate[]) null, secretKeyEntry.getAttributes());
            return;
        }
        throw new KeyStoreException("unsupported entry type: " + entry.getClass().getName());
    }

    private Set<KeyStore.Entry.Attribute> getAttributes(Entry entry) {
        ObjectIdentifier[] objectIdentifierArr;
        if (entry.attributes == null) {
            entry.attributes = new HashSet();
        }
        entry.attributes.add(new PKCS12Attribute(PKCS9FriendlyName_OID.toString(), entry.alias));
        byte[] bArr = entry.keyId;
        if (bArr != null) {
            entry.attributes.add(new PKCS12Attribute(PKCS9LocalKeyId_OID.toString(), Debug.toString(bArr)));
        }
        if ((entry instanceof CertEntry) && (objectIdentifierArr = ((CertEntry) entry).trustedKeyUsage) != null) {
            if (objectIdentifierArr.length == 1) {
                entry.attributes.add(new PKCS12Attribute(TrustedKeyUsage_OID.toString(), objectIdentifierArr[0].toString()));
            } else {
                entry.attributes.add(new PKCS12Attribute(TrustedKeyUsage_OID.toString(), Arrays.toString(objectIdentifierArr)));
            }
        }
        return entry.attributes;
    }

    private byte[] calculateMac(char[] cArr, byte[] bArr) throws IOException {
        String strSubstring = this.macAlgorithm.substring(7);
        try {
            byte[] salt = getSalt();
            Mac mac = Mac.getInstance(this.macAlgorithm);
            mac.init(getPBEKey(cArr), new PBEParameterSpec(salt, this.macIterationCount));
            mac.update(bArr);
            MacData macData = new MacData(strSubstring, mac.doFinal(), salt, this.macIterationCount);
            DerOutputStream derOutputStream = new DerOutputStream();
            derOutputStream.write(macData.getEncoded());
            return derOutputStream.toByteArray();
        } catch (Exception e2) {
            throw new IOException("calculateMac failed: " + ((Object) e2), e2);
        }
    }

    private boolean validateChain(Certificate[] certificateArr) {
        for (int i2 = 0; i2 < certificateArr.length - 1; i2++) {
            if (!((X509Certificate) certificateArr[i2]).getIssuerX500Principal().equals(((X509Certificate) certificateArr[i2 + 1]).getSubjectX500Principal())) {
                return false;
            }
        }
        return new HashSet(Arrays.asList(certificateArr)).size() == certificateArr.length;
    }

    private byte[] getBagAttributes(String str, byte[] bArr, Set<KeyStore.Entry.Attribute> set) throws IOException {
        return getBagAttributes(str, bArr, null, set);
    }

    private byte[] getBagAttributes(String str, byte[] bArr, ObjectIdentifier[] objectIdentifierArr, Set<KeyStore.Entry.Attribute> set) throws IOException {
        byte[] byteArray = null;
        byte[] byteArray2 = null;
        byte[] byteArray3 = null;
        if (str == null && bArr == null && 0 == 0) {
            return null;
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        if (str != null) {
            DerOutputStream derOutputStream2 = new DerOutputStream();
            derOutputStream2.putOID(PKCS9FriendlyName_OID);
            DerOutputStream derOutputStream3 = new DerOutputStream();
            DerOutputStream derOutputStream4 = new DerOutputStream();
            derOutputStream3.putBMPString(str);
            derOutputStream2.write((byte) 49, derOutputStream3);
            derOutputStream4.write((byte) 48, derOutputStream2);
            byteArray2 = derOutputStream4.toByteArray();
        }
        if (bArr != null) {
            DerOutputStream derOutputStream5 = new DerOutputStream();
            derOutputStream5.putOID(PKCS9LocalKeyId_OID);
            DerOutputStream derOutputStream6 = new DerOutputStream();
            DerOutputStream derOutputStream7 = new DerOutputStream();
            derOutputStream6.putOctetString(bArr);
            derOutputStream5.write((byte) 49, derOutputStream6);
            derOutputStream7.write((byte) 48, derOutputStream5);
            byteArray = derOutputStream7.toByteArray();
        }
        if (objectIdentifierArr != null) {
            DerOutputStream derOutputStream8 = new DerOutputStream();
            derOutputStream8.putOID(TrustedKeyUsage_OID);
            DerOutputStream derOutputStream9 = new DerOutputStream();
            DerOutputStream derOutputStream10 = new DerOutputStream();
            for (ObjectIdentifier objectIdentifier : objectIdentifierArr) {
                derOutputStream9.putOID(objectIdentifier);
            }
            derOutputStream8.write((byte) 49, derOutputStream9);
            derOutputStream10.write((byte) 48, derOutputStream8);
            byteArray3 = derOutputStream10.toByteArray();
        }
        DerOutputStream derOutputStream11 = new DerOutputStream();
        if (byteArray2 != null) {
            derOutputStream11.write(byteArray2);
        }
        if (byteArray != null) {
            derOutputStream11.write(byteArray);
        }
        if (byteArray3 != null) {
            derOutputStream11.write(byteArray3);
        }
        if (set != null) {
            for (KeyStore.Entry.Attribute attribute : set) {
                String name = attribute.getName();
                if (!CORE_ATTRIBUTES[0].equals(name) && !CORE_ATTRIBUTES[1].equals(name) && !CORE_ATTRIBUTES[2].equals(name)) {
                    derOutputStream11.write(((PKCS12Attribute) attribute).getEncoded());
                }
            }
        }
        derOutputStream.write((byte) 49, derOutputStream11);
        return derOutputStream.toByteArray();
    }

    private byte[] createEncryptedData(char[] cArr) throws IOException, CertificateException {
        Certificate[] certificateArr;
        byte[] bagAttributes;
        DerOutputStream derOutputStream = new DerOutputStream();
        Enumeration<String> enumerationEngineAliases = engineAliases();
        while (enumerationEngineAliases.hasMoreElements()) {
            Entry entry = this.entries.get(enumerationEngineAliases.nextElement2());
            if (entry instanceof PrivateKeyEntry) {
                PrivateKeyEntry privateKeyEntry = (PrivateKeyEntry) entry;
                if (privateKeyEntry.chain != null) {
                    certificateArr = privateKeyEntry.chain;
                } else {
                    certificateArr = new Certificate[0];
                }
            } else if (entry instanceof CertEntry) {
                certificateArr = new Certificate[]{((CertEntry) entry).cert};
            } else {
                certificateArr = new Certificate[0];
            }
            for (int i2 = 0; i2 < certificateArr.length; i2++) {
                DerOutputStream derOutputStream2 = new DerOutputStream();
                derOutputStream2.putOID(CertBag_OID);
                DerOutputStream derOutputStream3 = new DerOutputStream();
                derOutputStream3.putOID(PKCS9CertType_OID);
                DerOutputStream derOutputStream4 = new DerOutputStream();
                X509Certificate x509Certificate = (X509Certificate) certificateArr[i2];
                derOutputStream4.putOctetString(x509Certificate.getEncoded());
                derOutputStream3.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream4);
                DerOutputStream derOutputStream5 = new DerOutputStream();
                derOutputStream5.write((byte) 48, derOutputStream3);
                byte[] byteArray = derOutputStream5.toByteArray();
                DerOutputStream derOutputStream6 = new DerOutputStream();
                derOutputStream6.write(byteArray);
                derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream6);
                if (i2 == 0) {
                    if (entry instanceof KeyEntry) {
                        KeyEntry keyEntry = (KeyEntry) entry;
                        bagAttributes = getBagAttributes(keyEntry.alias, keyEntry.keyId, keyEntry.attributes);
                    } else {
                        CertEntry certEntry = (CertEntry) entry;
                        bagAttributes = getBagAttributes(certEntry.alias, certEntry.keyId, certEntry.trustedKeyUsage, certEntry.attributes);
                    }
                } else {
                    bagAttributes = getBagAttributes(x509Certificate.getSubjectX500Principal().getName(), null, entry.attributes);
                }
                if (bagAttributes != null) {
                    derOutputStream2.write(bagAttributes);
                }
                derOutputStream.write((byte) 48, derOutputStream2);
            }
        }
        DerOutputStream derOutputStream7 = new DerOutputStream();
        derOutputStream7.write((byte) 48, derOutputStream);
        byte[] byteArray2 = derOutputStream7.toByteArray();
        if (!this.certProtectionAlgorithm.equalsIgnoreCase("NONE")) {
            byte[] bArrEncryptContent = encryptContent(byteArray2, cArr);
            DerOutputStream derOutputStream8 = new DerOutputStream();
            DerOutputStream derOutputStream9 = new DerOutputStream();
            derOutputStream8.putInteger(0);
            derOutputStream8.write(bArrEncryptContent);
            derOutputStream9.write((byte) 48, derOutputStream8);
            return derOutputStream9.toByteArray();
        }
        return byteArray2;
    }

    private byte[] createSafeContent() throws IOException, CertificateException {
        DerOutputStream derOutputStream = new DerOutputStream();
        Enumeration<String> enumerationEngineAliases = engineAliases();
        while (enumerationEngineAliases.hasMoreElements()) {
            String strNextElement2 = enumerationEngineAliases.nextElement2();
            Entry entry = this.entries.get(strNextElement2);
            if (entry != null && (entry instanceof KeyEntry)) {
                DerOutputStream derOutputStream2 = new DerOutputStream();
                KeyEntry keyEntry = (KeyEntry) entry;
                if (keyEntry instanceof PrivateKeyEntry) {
                    derOutputStream2.putOID(PKCS8ShroudedKeyBag_OID);
                    try {
                        EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo(((PrivateKeyEntry) keyEntry).protectedPrivKey);
                        DerOutputStream derOutputStream3 = new DerOutputStream();
                        derOutputStream3.write(encryptedPrivateKeyInfo.getEncoded());
                        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream3);
                    } catch (IOException e2) {
                        throw new IOException("Private key not stored as PKCS#8 EncryptedPrivateKeyInfo" + e2.getMessage());
                    }
                } else if (keyEntry instanceof SecretKeyEntry) {
                    derOutputStream2.putOID(SecretBag_OID);
                    DerOutputStream derOutputStream4 = new DerOutputStream();
                    derOutputStream4.putOID(PKCS8ShroudedKeyBag_OID);
                    DerOutputStream derOutputStream5 = new DerOutputStream();
                    derOutputStream5.putOctetString(((SecretKeyEntry) keyEntry).protectedSecretKey);
                    derOutputStream4.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream5);
                    DerOutputStream derOutputStream6 = new DerOutputStream();
                    derOutputStream6.write((byte) 48, derOutputStream4);
                    byte[] byteArray = derOutputStream6.toByteArray();
                    DerOutputStream derOutputStream7 = new DerOutputStream();
                    derOutputStream7.write(byteArray);
                    derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), derOutputStream7);
                }
                derOutputStream2.write(getBagAttributes(strNextElement2, entry.keyId, entry.attributes));
                derOutputStream.write((byte) 48, derOutputStream2);
            }
        }
        DerOutputStream derOutputStream8 = new DerOutputStream();
        derOutputStream8.write((byte) 48, derOutputStream);
        return derOutputStream8.toByteArray();
    }

    private byte[] encryptContent(byte[] bArr, char[] cArr) throws IOException {
        try {
            AlgorithmParameters pBEAlgorithmParameters = getPBEAlgorithmParameters(this.certProtectionAlgorithm, this.certPbeIterationCount);
            DerOutputStream derOutputStream = new DerOutputStream();
            SecretKey pBEKey = getPBEKey(cArr);
            Cipher cipher = Cipher.getInstance(this.certProtectionAlgorithm);
            cipher.init(1, pBEKey, pBEAlgorithmParameters);
            byte[] bArrDoFinal = cipher.doFinal(bArr);
            new AlgorithmId(mapPBEAlgorithmToOID(this.certProtectionAlgorithm), cipher.getParameters()).encode(derOutputStream);
            byte[] byteArray = derOutputStream.toByteArray();
            if (debug != null) {
                debug.println("  (Cipher algorithm: " + cipher.getAlgorithm() + ")");
            }
            DerOutputStream derOutputStream2 = new DerOutputStream();
            derOutputStream2.putOID(ContentInfo.DATA_OID);
            derOutputStream2.write(byteArray);
            DerOutputStream derOutputStream3 = new DerOutputStream();
            derOutputStream3.putOctetString(bArrDoFinal);
            derOutputStream2.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 0), derOutputStream3);
            DerOutputStream derOutputStream4 = new DerOutputStream();
            derOutputStream4.write((byte) 48, derOutputStream2);
            return derOutputStream4.toByteArray();
        } catch (IOException e2) {
            throw e2;
        } catch (Exception e3) {
            throw new IOException("Failed to encrypt safe contents entry: " + ((Object) e3), e3);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:105:0x0453 A[LOOP:2: B:89:0x03d3->B:105:0x0453, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0464 A[EDGE_INSN: B:135:0x0464->B:106:0x0464 BREAK  A[LOOP:2: B:89:0x03d3->B:105:0x0453], SYNTHETIC] */
    @Override // java.security.KeyStoreSpi
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized void engineLoad(java.io.InputStream r9, char[] r10) throws java.security.NoSuchAlgorithmException, java.io.IOException, java.security.cert.CertificateException, java.security.InvalidAlgorithmParameterException {
        /*
            Method dump skipped, instructions count: 1257
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.pkcs12.PKCS12KeyStore.engineLoad(java.io.InputStream, char[]):void");
    }

    public static boolean isPasswordless(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        Throwable th = null;
        try {
            DerInputStream derInputStream = new DerValue(fileInputStream).toDerInputStream();
            derInputStream.getInteger();
            for (DerValue derValue : new DerInputStream(new ContentInfo(derInputStream).getData()).getSequence(2)) {
                if (new ContentInfo(new DerInputStream(derValue.toByteArray())).getContentType().equals(ContentInfo.ENCRYPTED_DATA_OID)) {
                    return false;
                }
            }
            if (derInputStream.available() > 0) {
                if (fileInputStream != null) {
                    if (0 != 0) {
                        try {
                            fileInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        fileInputStream.close();
                    }
                }
                return false;
            }
            if (fileInputStream == null) {
                return true;
            }
            if (0 == 0) {
                fileInputStream.close();
                return true;
            }
            try {
                fileInputStream.close();
                return true;
            } catch (Throwable th3) {
                th.addSuppressed(th3);
                return true;
            }
        } finally {
            if (fileInputStream != null) {
                if (0 != 0) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    fileInputStream.close();
                }
            }
        }
    }

    private X509Certificate findMatchedCertificate(PrivateKeyEntry privateKeyEntry) {
        CertEntry certEntry = null;
        CertEntry certEntry2 = null;
        Iterator<CertEntry> it = this.certEntries.iterator();
        while (it.hasNext()) {
            CertEntry next = it.next();
            if (Arrays.equals(privateKeyEntry.keyId, next.keyId)) {
                certEntry = next;
                if (privateKeyEntry.alias.equalsIgnoreCase(next.alias)) {
                    return next.cert;
                }
            } else if (privateKeyEntry.alias.equalsIgnoreCase(next.alias)) {
                certEntry2 = next;
            }
        }
        if (certEntry != null) {
            return certEntry.cert;
        }
        if (certEntry2 != null) {
            return certEntry2.cert;
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:122:0x0375 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0394  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x03ac  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x03c1  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x03df  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void loadSafeContents(sun.security.util.DerInputStream r9) throws java.security.NoSuchAlgorithmException, java.io.IOException, java.security.cert.CertificateException {
        /*
            Method dump skipped, instructions count: 1192
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.pkcs12.PKCS12KeyStore.loadSafeContents(sun.security.util.DerInputStream):void");
    }

    private String getUnfriendlyName() {
        this.counter++;
        return String.valueOf(this.counter);
    }

    private static String defaultCertProtectionAlgorithm() {
        String strPrivilegedGetOverridable = SecurityProperties.privilegedGetOverridable("keystore.pkcs12.certProtectionAlgorithm");
        return (strPrivilegedGetOverridable == null || strPrivilegedGetOverridable.isEmpty()) ? "PBEWithSHA1AndRC2_40" : strPrivilegedGetOverridable;
    }

    private static int defaultCertPbeIterationCount() {
        String strPrivilegedGetOverridable = SecurityProperties.privilegedGetOverridable("keystore.pkcs12.certPbeIterationCount");
        return (strPrivilegedGetOverridable == null || strPrivilegedGetOverridable.isEmpty()) ? StreamManager.DEFAULT_BLOCK_SIZE : string2IC("certPbeIterationCount", strPrivilegedGetOverridable);
    }

    private static String defaultKeyProtectionAlgorithm() {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.security.pkcs12.PKCS12KeyStore.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                String property = System.getProperty("keystore.pkcs12.keyProtectionAlgorithm");
                if (property != null) {
                    return property;
                }
                String property2 = System.getProperty("keystore.PKCS12.keyProtectionAlgorithm");
                if (property2 != null) {
                    return property2;
                }
                String property3 = Security.getProperty("keystore.pkcs12.keyProtectionAlgorithm");
                if (property3 != null) {
                    return property3;
                }
                return Security.getProperty("keystore.PKCS12.keyProtectionAlgorithm");
            }
        });
        return (str == null || str.isEmpty()) ? "PBEWithSHA1AndDESede" : str;
    }

    private static int defaultKeyPbeIterationCount() {
        String strPrivilegedGetOverridable = SecurityProperties.privilegedGetOverridable("keystore.pkcs12.keyPbeIterationCount");
        return (strPrivilegedGetOverridable == null || strPrivilegedGetOverridable.isEmpty()) ? StreamManager.DEFAULT_BLOCK_SIZE : string2IC("keyPbeIterationCount", strPrivilegedGetOverridable);
    }

    private static String defaultMacAlgorithm() {
        String strPrivilegedGetOverridable = SecurityProperties.privilegedGetOverridable("keystore.pkcs12.macAlgorithm");
        return (strPrivilegedGetOverridable == null || strPrivilegedGetOverridable.isEmpty()) ? "HmacPBESHA1" : strPrivilegedGetOverridable;
    }

    private static int defaultMacIterationCount() {
        String strPrivilegedGetOverridable = SecurityProperties.privilegedGetOverridable("keystore.pkcs12.macIterationCount");
        return (strPrivilegedGetOverridable == null || strPrivilegedGetOverridable.isEmpty()) ? Config.MAX_REPEAT_NUM : string2IC("macIterationCount", strPrivilegedGetOverridable);
    }

    private static int string2IC(String str, String str2) {
        try {
            int i2 = Integer.parseInt(str2);
            if (i2 <= 0 || i2 > MAX_ITERATION_COUNT) {
                throw new IllegalArgumentException("Invalid keystore.pkcs12." + str + ": " + str2);
            }
            return i2;
        } catch (NumberFormatException e2) {
            throw new IllegalArgumentException("keystore.pkcs12." + str + " is not a number: " + str2);
        }
    }
}
