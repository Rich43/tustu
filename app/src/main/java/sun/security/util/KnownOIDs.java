package sun.security.util;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509SKI;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/* loaded from: rt.jar:sun/security/util/KnownOIDs.class */
public enum KnownOIDs {
    CommonName("2.5.4.3"),
    Surname("2.5.4.4"),
    SerialNumber("2.5.4.5"),
    CountryName("2.5.4.6"),
    LocalityName("2.5.4.7"),
    StateName("2.5.4.8"),
    StreetAddress("2.5.4.9"),
    OrgName("2.5.4.10"),
    OrgUnitName("2.5.4.11"),
    Title("2.5.4.12"),
    GivenName("2.5.4.42"),
    Initials("2.5.4.43"),
    GenerationQualifier("2.5.4.44"),
    DNQualifier("2.5.4.46"),
    SubjectDirectoryAttributes("2.5.29.9"),
    SubjectKeyID(XMLX509SKI.SKI_OID),
    KeyUsage("2.5.29.15"),
    PrivateKeyUsage("2.5.29.16"),
    SubjectAlternativeName("2.5.29.17"),
    IssuerAlternativeName("2.5.29.18"),
    BasicConstraints("2.5.29.19"),
    CRLNumber("2.5.29.20"),
    ReasonCode("2.5.29.21"),
    HoldInstructionCode("2.5.29.23"),
    InvalidityDate("2.5.29.24"),
    DeltaCRLIndicator("2.5.29.27"),
    IssuingDistributionPoint("2.5.29.28"),
    CertificateIssuer("2.5.29.29"),
    NameConstraints("2.5.29.30"),
    CRLDistributionPoints("2.5.29.31"),
    CertificatePolicies("2.5.29.32"),
    CE_CERT_POLICIES_ANY("2.5.29.32.0"),
    PolicyMappings("2.5.29.33"),
    AuthorityKeyID("2.5.29.35"),
    PolicyConstraints("2.5.29.36"),
    extendedKeyUsage("2.5.29.37"),
    anyExtendedKeyUsage("2.5.29.37.0"),
    FreshestCRL("2.5.29.46"),
    InhibitAnyPolicy("2.5.29.54"),
    AuthInfoAccess("1.3.6.1.5.5.7.1.1"),
    SubjectInfoAccess("1.3.6.1.5.5.7.1.11"),
    serverAuth("1.3.6.1.5.5.7.3.1"),
    clientAuth("1.3.6.1.5.5.7.3.2"),
    codeSigning("1.3.6.1.5.5.7.3.3"),
    emailProtection("1.3.6.1.5.5.7.3.4"),
    ipsecEndSystem("1.3.6.1.5.5.7.3.5"),
    ipsecTunnel("1.3.6.1.5.5.7.3.6"),
    ipsecUser("1.3.6.1.5.5.7.3.7"),
    KP_TimeStamping("1.3.6.1.5.5.7.3.8", "timeStamping", new String[0]) { // from class: sun.security.util.KnownOIDs.1
        @Override // sun.security.util.KnownOIDs
        boolean registerNames() {
            return false;
        }
    },
    OCSPSigning("1.3.6.1.5.5.7.3.9"),
    OCSP("1.3.6.1.5.5.7.48.1"),
    OCSPBasicResponse("1.3.6.1.5.5.7.48.1.1"),
    OCSPNonceExt("1.3.6.1.5.5.7.48.1.2"),
    OCSPNoCheck("1.3.6.1.5.5.7.48.1.5"),
    caIssuers("1.3.6.1.5.5.7.48.2"),
    AD_TimeStamping("1.3.6.1.5.5.7.48.3", "timeStamping", new String[0]) { // from class: sun.security.util.KnownOIDs.2
        @Override // sun.security.util.KnownOIDs
        boolean registerNames() {
            return false;
        }
    },
    caRepository("1.3.6.1.5.5.7.48.5", "caRepository", new String[0]),
    AES("2.16.840.1.101.3.4.1"),
    AES_128$ECB$NoPadding("2.16.840.1.101.3.4.1.1", "AES_128/ECB/NoPadding", new String[0]),
    AES_128$CBC$NoPadding("2.16.840.1.101.3.4.1.2", "AES_128/CBC/NoPadding", new String[0]),
    AES_128$OFB$NoPadding("2.16.840.1.101.3.4.1.3", "AES_128/OFB/NoPadding", new String[0]),
    AES_128$CFB$NoPadding("2.16.840.1.101.3.4.1.4", "AES_128/CFB/NoPadding", new String[0]),
    AESWRAP_128("2.16.840.1.101.3.4.1.5"),
    AES_128$GCM$NoPadding("2.16.840.1.101.3.4.1.6", "AES_128/GCM/NoPadding", new String[0]),
    AES_192$ECB$NoPadding("2.16.840.1.101.3.4.1.21", "AES_192/ECB/NoPadding", new String[0]),
    AES_192$CBC$NoPadding("2.16.840.1.101.3.4.1.22", "AES_192/CBC/NoPadding", new String[0]),
    AES_192$OFB$NoPadding("2.16.840.1.101.3.4.1.23", "AES_192/OFB/NoPadding", new String[0]),
    AES_192$CFB$NoPadding("2.16.840.1.101.3.4.1.24", "AES_192/CFB/NoPadding", new String[0]),
    AESWRAP_192("2.16.840.1.101.3.4.1.25"),
    AES_192$GCM$NoPadding("2.16.840.1.101.3.4.1.26", "AES_192/GCM/NoPadding", new String[0]),
    AES_256$ECB$NoPadding("2.16.840.1.101.3.4.1.41", "AES_256/ECB/NoPadding", new String[0]),
    AES_256$CBC$NoPadding("2.16.840.1.101.3.4.1.42", "AES_256/CBC/NoPadding", new String[0]),
    AES_256$OFB$NoPadding("2.16.840.1.101.3.4.1.43", "AES_256/OFB/NoPadding", new String[0]),
    AES_256$CFB$NoPadding("2.16.840.1.101.3.4.1.44", "AES_256/CFB/NoPadding", new String[0]),
    AESWRAP_256("2.16.840.1.101.3.4.1.45"),
    AES_256$GCM$NoPadding("2.16.840.1.101.3.4.1.46", "AES_256/GCM/NoPadding", new String[0]),
    SHA_256("2.16.840.1.101.3.4.2.1", "SHA-256", "SHA256"),
    SHA_384("2.16.840.1.101.3.4.2.2", "SHA-384", "SHA384"),
    SHA_512("2.16.840.1.101.3.4.2.3", "SHA-512", "SHA512"),
    SHA_224("2.16.840.1.101.3.4.2.4", "SHA-224", "SHA224"),
    SHA_512$224("2.16.840.1.101.3.4.2.5", "SHA-512/224", "SHA512/224"),
    SHA_512$256("2.16.840.1.101.3.4.2.6", "SHA-512/256", "SHA512/256"),
    SHA3_224("2.16.840.1.101.3.4.2.7", "SHA3-224", new String[0]),
    SHA3_256("2.16.840.1.101.3.4.2.8", "SHA3-256", new String[0]),
    SHA3_384("2.16.840.1.101.3.4.2.9", "SHA3-384", new String[0]),
    SHA3_512("2.16.840.1.101.3.4.2.10", "SHA3-512", new String[0]),
    SHAKE128("2.16.840.1.101.3.4.2.11"),
    SHAKE256("2.16.840.1.101.3.4.2.12"),
    HmacSHA3_224("2.16.840.1.101.3.4.2.13", "HmacSHA3-224", new String[0]),
    HmacSHA3_256("2.16.840.1.101.3.4.2.14", "HmacSHA3-256", new String[0]),
    HmacSHA3_384("2.16.840.1.101.3.4.2.15", "HmacSHA3-384", new String[0]),
    HmacSHA3_512("2.16.840.1.101.3.4.2.16", "HmacSHA3-512", new String[0]),
    SHA224withDSA("2.16.840.1.101.3.4.3.1"),
    SHA256withDSA("2.16.840.1.101.3.4.3.2"),
    SHA384withDSA("2.16.840.1.101.3.4.3.3"),
    SHA512withDSA("2.16.840.1.101.3.4.3.4"),
    SHA3_224withRSA("2.16.840.1.101.3.4.3.13", "SHA3-224withRSA", new String[0]),
    SHA3_256withRSA("2.16.840.1.101.3.4.3.14", "SHA3-256withRSA", new String[0]),
    SHA3_384withRSA("2.16.840.1.101.3.4.3.15", "SHA3-384withRSA", new String[0]),
    SHA3_512withRSA("2.16.840.1.101.3.4.3.16", "SHA3-512withRSA", new String[0]),
    PKCS1("1.2.840.113549.1.1", "RSA", new String[0]) { // from class: sun.security.util.KnownOIDs.3
        @Override // sun.security.util.KnownOIDs
        boolean registerNames() {
            return false;
        }
    },
    RSA("1.2.840.113549.1.1.1"),
    MD2withRSA("1.2.840.113549.1.1.2"),
    MD5withRSA("1.2.840.113549.1.1.4"),
    SHA1withRSA("1.2.840.113549.1.1.5"),
    OAEP("1.2.840.113549.1.1.7"),
    MGF1("1.2.840.113549.1.1.8"),
    PSpecified("1.2.840.113549.1.1.9"),
    RSASSA_PSS("1.2.840.113549.1.1.10", "RSASSA-PSS", new String[0]),
    SHA256withRSA("1.2.840.113549.1.1.11"),
    SHA384withRSA("1.2.840.113549.1.1.12"),
    SHA512withRSA("1.2.840.113549.1.1.13"),
    SHA224withRSA("1.2.840.113549.1.1.14"),
    SHA512$224withRSA("1.2.840.113549.1.1.15", "SHA512/224withRSA", new String[0]),
    SHA512$256withRSA("1.2.840.113549.1.1.16", "SHA512/256withRSA", new String[0]),
    DiffieHellman("1.2.840.113549.1.3.1", "DiffieHellman", "DH"),
    PBEWithMD5AndDES("1.2.840.113549.1.5.3"),
    PBEWithMD5AndRC2("1.2.840.113549.1.5.6"),
    PBEWithSHA1AndDES("1.2.840.113549.1.5.10"),
    PBEWithSHA1AndRC2("1.2.840.113549.1.5.11"),
    PBKDF2WithHmacSHA1("1.2.840.113549.1.5.12"),
    PBES2("1.2.840.113549.1.5.13"),
    PKCS7("1.2.840.113549.1.7"),
    Data("1.2.840.113549.1.7.1"),
    SignedData("1.2.840.113549.1.7.2"),
    JDK_OLD_Data("1.2.840.1113549.1.7.1"),
    JDK_OLD_SignedData("1.2.840.1113549.1.7.2"),
    EnvelopedData("1.2.840.113549.1.7.3"),
    SignedAndEnvelopedData("1.2.840.113549.1.7.4"),
    DigestedData("1.2.840.113549.1.7.5"),
    EncryptedData("1.2.840.113549.1.7.6"),
    EmailAddress("1.2.840.113549.1.9.1"),
    UnstructuredName("1.2.840.113549.1.9.2"),
    ContentType("1.2.840.113549.1.9.3"),
    MessageDigest("1.2.840.113549.1.9.4"),
    SigningTime("1.2.840.113549.1.9.5"),
    CounterSignature("1.2.840.113549.1.9.6"),
    ChallengePassword("1.2.840.113549.1.9.7"),
    UnstructuredAddress("1.2.840.113549.1.9.8"),
    ExtendedCertificateAttributes("1.2.840.113549.1.9.9"),
    IssuerAndSerialNumber("1.2.840.113549.1.9.10"),
    ExtensionRequest("1.2.840.113549.1.9.14"),
    SMIMECapability("1.2.840.113549.1.9.15"),
    TimeStampTokenInfo("1.2.840.113549.1.9.16.1.4"),
    SigningCertificate("1.2.840.113549.1.9.16.2.12"),
    SignatureTimestampToken("1.2.840.113549.1.9.16.2.14"),
    CHACHA20_POLY1305("1.2.840.113549.1.9.16.3.18", "CHACHA20-POLY1305", new String[0]),
    FriendlyName("1.2.840.113549.1.9.20"),
    LocalKeyID("1.2.840.113549.1.9.21"),
    CertTypeX509("1.2.840.113549.1.9.22.1"),
    PBEWithSHA1AndRC4_128("1.2.840.113549.1.12.1.1"),
    PBEWithSHA1AndRC4_40("1.2.840.113549.1.12.1.2"),
    PBEWithSHA1AndDESede("1.2.840.113549.1.12.1.3"),
    PBEWithSHA1AndRC2_128("1.2.840.113549.1.12.1.5"),
    PBEWithSHA1AndRC2_40("1.2.840.113549.1.12.1.6"),
    PKCS8ShroudedKeyBag("1.2.840.113549.1.12.10.1.2"),
    CertBag("1.2.840.113549.1.12.10.1.3"),
    SecretBag("1.2.840.113549.1.12.10.1.5"),
    MD2("1.2.840.113549.2.2"),
    MD5("1.2.840.113549.2.5"),
    HmacSHA1("1.2.840.113549.2.7"),
    HmacSHA224("1.2.840.113549.2.8"),
    HmacSHA256("1.2.840.113549.2.9"),
    HmacSHA384("1.2.840.113549.2.10"),
    HmacSHA512("1.2.840.113549.2.11"),
    HmacSHA512$224("1.2.840.113549.2.12", "HmacSHA512/224", new String[0]),
    HmacSHA512$256("1.2.840.113549.2.13", "HmacSHA512/256", new String[0]),
    RC2$CBC$PKCS5Padding("1.2.840.113549.3.2", "RC2/CBC/PKCS5Padding", new String[0]),
    ARCFOUR("1.2.840.113549.3.4", "ARCFOUR", "RC4"),
    DESede$CBC$NoPadding("1.2.840.113549.3.7", "DESede/CBC/NoPadding", new String[0]),
    RC5$CBC$PKCS5Padding("1.2.840.113549.3.9", "RC5/CBC/PKCS5Padding", new String[0]),
    DSA("1.2.840.10040.4.1"),
    SHA1withDSA("1.2.840.10040.4.3", "SHA1withDSA", "DSS"),
    EC("1.2.840.10045.2.1"),
    c2tnb191v1("1.2.840.10045.3.0.5", "X9.62 c2tnb191v1", new String[0]),
    c2tnb191v2("1.2.840.10045.3.0.6", "X9.62 c2tnb191v2", new String[0]),
    c2tnb191v3("1.2.840.10045.3.0.7", "X9.62 c2tnb191v3", new String[0]),
    c2tnb239v1("1.2.840.10045.3.0.11", "X9.62 c2tnb239v1", new String[0]),
    c2tnb239v2("1.2.840.10045.3.0.12", "X9.62 c2tnb239v2", new String[0]),
    c2tnb239v3("1.2.840.10045.3.0.13", "X9.62 c2tnb239v3", new String[0]),
    c2tnb359v1("1.2.840.10045.3.0.18", "X9.62 c2tnb359v1", new String[0]),
    c2tnb431r1("1.2.840.10045.3.0.20", "X9.62 c2tnb431r1", new String[0]),
    secp192r1("1.2.840.10045.3.1.1", "secp192r1", "NIST P-192", "X9.62 prime192v1"),
    prime192v2("1.2.840.10045.3.1.2", "X9.62 prime192v2", new String[0]),
    prime192v3("1.2.840.10045.3.1.3", "X9.62 prime192v3", new String[0]),
    prime239v1("1.2.840.10045.3.1.4", "X9.62 prime239v1", new String[0]),
    prime239v2("1.2.840.10045.3.1.5", "X9.62 prime239v2", new String[0]),
    prime239v3("1.2.840.10045.3.1.6", "X9.62 prime239v3", new String[0]),
    secp256r1("1.2.840.10045.3.1.7", "secp256r1", "NIST P-256", "X9.62 prime256v1"),
    SHA1withECDSA("1.2.840.10045.4.1"),
    SHA224withECDSA("1.2.840.10045.4.3.1"),
    SHA256withECDSA("1.2.840.10045.4.3.2"),
    SHA384withECDSA("1.2.840.10045.4.3.3"),
    SHA512withECDSA("1.2.840.10045.4.3.4"),
    SpecifiedSHA2withECDSA("1.2.840.10045.4.3"),
    X942_DH("1.2.840.10046.2.1", "DiffieHellman", new String[0]) { // from class: sun.security.util.KnownOIDs.4
        @Override // sun.security.util.KnownOIDs
        boolean registerNames() {
            return false;
        }
    },
    brainpoolP160r1("1.3.36.3.3.2.8.1.1.1"),
    brainpoolP192r1("1.3.36.3.3.2.8.1.1.3"),
    brainpoolP224r1("1.3.36.3.3.2.8.1.1.5"),
    brainpoolP256r1("1.3.36.3.3.2.8.1.1.7"),
    brainpoolP320r1("1.3.36.3.3.2.8.1.1.9"),
    brainpoolP384r1("1.3.36.3.3.2.8.1.1.11"),
    brainpoolP512r1("1.3.36.3.3.2.8.1.1.13"),
    sect163k1("1.3.132.0.1", "sect163k1", "NIST K-163"),
    sect163r1("1.3.132.0.2"),
    sect239k1("1.3.132.0.3"),
    sect113r1("1.3.132.0.4"),
    sect113r2("1.3.132.0.5"),
    secp112r1("1.3.132.0.6"),
    secp112r2("1.3.132.0.7"),
    secp160r1("1.3.132.0.8"),
    secp160k1("1.3.132.0.9"),
    secp256k1("1.3.132.0.10"),
    sect163r2("1.3.132.0.15", "sect163r2", "NIST B-163"),
    sect283k1("1.3.132.0.16", "sect283k1", "NIST K-283"),
    sect283r1("1.3.132.0.17", "sect283r1", "NIST B-283"),
    sect131r1("1.3.132.0.22"),
    sect131r2("1.3.132.0.23"),
    sect193r1("1.3.132.0.24"),
    sect193r2("1.3.132.0.25"),
    sect233k1("1.3.132.0.26", "sect233k1", "NIST K-233"),
    sect233r1("1.3.132.0.27", "sect233r1", "NIST B-233"),
    secp128r1("1.3.132.0.28"),
    secp128r2("1.3.132.0.29"),
    secp160r2("1.3.132.0.30"),
    secp192k1("1.3.132.0.31"),
    secp224k1("1.3.132.0.32"),
    secp224r1("1.3.132.0.33", "secp224r1", "NIST P-224"),
    secp384r1("1.3.132.0.34", "secp384r1", "NIST P-384"),
    secp521r1("1.3.132.0.35", "secp521r1", "NIST P-521"),
    sect409k1("1.3.132.0.36", "sect409k1", "NIST K-409"),
    sect409r1("1.3.132.0.37", "sect409r1", "NIST B-409"),
    sect571k1("1.3.132.0.38", "sect571k1", "NIST K-571"),
    sect571r1("1.3.132.0.39", "sect571r1", "NIST B-571"),
    ECDH("1.3.132.1.12"),
    OIW_DES_CBC("1.3.14.3.2.7", "DES/CBC", new String[0]),
    OIW_DSA("1.3.14.3.2.12", "DSA", new String[0]) { // from class: sun.security.util.KnownOIDs.5
        @Override // sun.security.util.KnownOIDs
        boolean registerNames() {
            return false;
        }
    },
    OIW_JDK_SHA1withDSA("1.3.14.3.2.13", "SHA1withDSA", new String[0]) { // from class: sun.security.util.KnownOIDs.6
        @Override // sun.security.util.KnownOIDs
        boolean registerNames() {
            return false;
        }
    },
    SHA_1("1.3.14.3.2.26", "SHA-1", "SHA", "SHA1"),
    OIW_SHA1withDSA("1.3.14.3.2.27", "SHA1withDSA", new String[0]) { // from class: sun.security.util.KnownOIDs.7
        @Override // sun.security.util.KnownOIDs
        boolean registerNames() {
            return false;
        }
    },
    OIW_SHA1withRSA("1.3.14.3.2.29", "SHA1withRSA", new String[0]) { // from class: sun.security.util.KnownOIDs.8
        @Override // sun.security.util.KnownOIDs
        boolean registerNames() {
            return false;
        }
    },
    X25519("1.3.101.110"),
    X448("1.3.101.111"),
    Ed25519("1.3.101.112"),
    Ed448("1.3.101.113"),
    UCL_UserID("0.9.2342.19200300.100.1.1"),
    UCL_DomainComponent("0.9.2342.19200300.100.1.25"),
    NETSCAPE_CertType("2.16.840.1.113730.1.1"),
    NETSCAPE_CertSequence("2.16.840.1.113730.2.5"),
    NETSCAPE_ExportApproved("2.16.840.1.113730.4.1"),
    ORACLE_TrustedKeyUsage("2.16.840.1.113894.746875.1.1"),
    ITUX509_RSA("2.5.8.1.1", "RSA", new String[0]) { // from class: sun.security.util.KnownOIDs.9
        @Override // sun.security.util.KnownOIDs
        boolean registerNames() {
            return false;
        }
    },
    SkipIPAddress("1.3.6.1.4.1.42.2.11.2.1"),
    JAVASOFT_JDKKeyProtector("1.3.6.1.4.1.42.2.17.1.1"),
    JAVASOFT_JCEKeyProtector("1.3.6.1.4.1.42.2.19.1"),
    MICROSOFT_ExportApproved("1.3.6.1.4.1.311.10.3.3");

    private String stdName;
    private String oid;
    private String[] aliases;
    private static final Debug debug = Debug.getInstance("jca");
    private static final ConcurrentHashMap<String, KnownOIDs> name2enum = new ConcurrentHashMap<>();

    static {
        if (debug != null) {
            debug.println("Setting up name2enum:");
        }
        Arrays.asList(values()).forEach(new Consumer<KnownOIDs>() { // from class: sun.security.util.KnownOIDs.10
            @Override // java.util.function.Consumer
            public void accept(KnownOIDs knownOIDs) {
                KnownOIDs.register(knownOIDs);
            }
        });
    }

    public static KnownOIDs findMatch(String str) {
        String upperCase = str.toUpperCase(Locale.ENGLISH);
        KnownOIDs knownOIDs = name2enum.get(upperCase);
        if (knownOIDs == null && debug != null) {
            debug.println("No KnownOIDs enum found for " + upperCase);
        }
        return knownOIDs;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void register(KnownOIDs knownOIDs) {
        KnownOIDs knownOIDsPut = name2enum.put(knownOIDs.oid, knownOIDs);
        if (knownOIDsPut != null) {
            throw new RuntimeException("ERROR: Duplicate " + knownOIDs.oid + " between " + ((Object) knownOIDs) + " and " + ((Object) knownOIDsPut));
        }
        if (debug != null) {
            debug.println(knownOIDs.oid + " => " + knownOIDs.name());
        }
        if (knownOIDs.registerNames()) {
            String upperCase = knownOIDs.stdName.toUpperCase(Locale.ENGLISH);
            if (Objects.nonNull(name2enum.put(upperCase, knownOIDs))) {
                throw new RuntimeException("ERROR: Duplicate " + upperCase + " exists already");
            }
            if (debug != null) {
                debug.println(upperCase + " => " + knownOIDs.name());
            }
            for (String str : knownOIDs.aliases) {
                String upperCase2 = str.toUpperCase(Locale.ENGLISH);
                if (Objects.nonNull(name2enum.put(upperCase2, knownOIDs))) {
                    throw new RuntimeException("ERROR: Duplicate " + upperCase2 + " exists already");
                }
                if (debug != null) {
                    debug.println(upperCase2 + " => " + knownOIDs.name());
                }
            }
        }
    }

    KnownOIDs(String str) {
        this.oid = str;
        this.stdName = name();
        this.aliases = new String[0];
    }

    KnownOIDs(String str, String str2, String... strArr) {
        this.oid = str;
        this.stdName = str2;
        this.aliases = strArr;
    }

    public String value() {
        return this.oid;
    }

    public String stdName() {
        return this.stdName;
    }

    public String[] aliases() {
        return this.aliases;
    }

    boolean registerNames() {
        return true;
    }
}
