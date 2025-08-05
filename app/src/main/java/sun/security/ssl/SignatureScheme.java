package sun.security.ssl;

import com.sun.glass.events.WindowEvent;
import com.sun.media.sound.DLSModulator;
import com.sun.xml.internal.fastinfoset.EncodingConstants;
import java.awt.Event;
import java.security.AlgorithmConstraints;
import java.security.CryptoPrimitive;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.icepdf.core.pobjects.graphics.Separation;
import sun.security.krb5.internal.ccache.FileCCacheConstants;
import sun.security.ssl.SupportedGroupsExtension;
import sun.security.ssl.X509Authentication;
import sun.security.util.KeyUtil;
import sun.security.util.SignatureUtil;

/* loaded from: jsse.jar:sun/security/ssl/SignatureScheme.class */
enum SignatureScheme {
    ED25519(2055, "ed25519", "ed25519", "ed25519", ProtocolVersion.PROTOCOLS_OF_13),
    ED448(2056, "ed448", "ed448", "ed448", ProtocolVersion.PROTOCOLS_OF_13),
    ECDSA_SECP256R1_SHA256(1027, "ecdsa_secp256r1_sha256", "SHA256withECDSA", "EC", SupportedGroupsExtension.NamedGroup.SECP256_R1, ProtocolVersion.PROTOCOLS_TO_13),
    ECDSA_SECP384R1_SHA384(FileCCacheConstants.KRB5_FCC_FVNO_3, "ecdsa_secp384r1_sha384", "SHA384withECDSA", "EC", SupportedGroupsExtension.NamedGroup.SECP384_R1, ProtocolVersion.PROTOCOLS_TO_13),
    ECDSA_SECP521R1_SHA512(1539, "ecdsa_secp521r1_sha512", "SHA512withECDSA", "EC", SupportedGroupsExtension.NamedGroup.SECP521_R1, ProtocolVersion.PROTOCOLS_TO_13),
    RSA_PSS_RSAE_SHA256(2052, "rsa_pss_rsae_sha256", "RSASSA-PSS", "RSA", SigAlgParamSpec.RSA_PSS_SHA256, 528, ProtocolVersion.PROTOCOLS_12_13),
    RSA_PSS_RSAE_SHA384(2053, "rsa_pss_rsae_sha384", "RSASSA-PSS", "RSA", SigAlgParamSpec.RSA_PSS_SHA384, DLSModulator.CONN_DST_EG2_HOLDTIME, ProtocolVersion.PROTOCOLS_12_13),
    RSA_PSS_RSAE_SHA512(2054, "rsa_pss_rsae_sha512", "RSASSA-PSS", "RSA", SigAlgParamSpec.RSA_PSS_SHA512, EncodingConstants.INTEGER_4TH_BIT_MEDIUM_LIMIT, ProtocolVersion.PROTOCOLS_12_13),
    RSA_PSS_PSS_SHA256(2057, "rsa_pss_pss_sha256", "RSASSA-PSS", "RSASSA-PSS", SigAlgParamSpec.RSA_PSS_SHA256, 528, ProtocolVersion.PROTOCOLS_12_13),
    RSA_PSS_PSS_SHA384(2058, "rsa_pss_pss_sha384", "RSASSA-PSS", "RSASSA-PSS", SigAlgParamSpec.RSA_PSS_SHA384, DLSModulator.CONN_DST_EG2_HOLDTIME, ProtocolVersion.PROTOCOLS_12_13),
    RSA_PSS_PSS_SHA512(2059, "rsa_pss_pss_sha512", "RSASSA-PSS", "RSASSA-PSS", SigAlgParamSpec.RSA_PSS_SHA512, EncodingConstants.INTEGER_4TH_BIT_MEDIUM_LIMIT, ProtocolVersion.PROTOCOLS_12_13),
    RSA_PKCS1_SHA256(Event.INSERT, "rsa_pkcs1_sha256", "SHA256withRSA", "RSA", null, null, WindowEvent.RESIZE, ProtocolVersion.PROTOCOLS_TO_13, ProtocolVersion.PROTOCOLS_TO_12),
    RSA_PKCS1_SHA384(1281, "rsa_pkcs1_sha384", "SHA384withRSA", "RSA", null, null, 768, ProtocolVersion.PROTOCOLS_TO_13, ProtocolVersion.PROTOCOLS_TO_12),
    RSA_PKCS1_SHA512(1537, "rsa_pkcs1_sha512", "SHA512withRSA", "RSA", null, null, 768, ProtocolVersion.PROTOCOLS_TO_13, ProtocolVersion.PROTOCOLS_TO_12),
    DSA_SHA256(1026, "dsa_sha256", "SHA256withDSA", "DSA", ProtocolVersion.PROTOCOLS_TO_12),
    ECDSA_SHA224(771, "ecdsa_sha224", "SHA224withECDSA", "EC", ProtocolVersion.PROTOCOLS_TO_12),
    RSA_SHA224(769, "rsa_sha224", "SHA224withRSA", "RSA", WindowEvent.RESIZE, ProtocolVersion.PROTOCOLS_TO_12),
    DSA_SHA224(770, "dsa_sha224", "SHA224withDSA", "DSA", ProtocolVersion.PROTOCOLS_TO_12),
    ECDSA_SHA1(515, "ecdsa_sha1", "SHA1withECDSA", "EC", ProtocolVersion.PROTOCOLS_TO_13),
    RSA_PKCS1_SHA1(513, "rsa_pkcs1_sha1", "SHA1withRSA", "RSA", null, null, WindowEvent.RESIZE, ProtocolVersion.PROTOCOLS_TO_13, ProtocolVersion.PROTOCOLS_TO_12),
    DSA_SHA1(514, "dsa_sha1", "SHA1withDSA", "DSA", ProtocolVersion.PROTOCOLS_TO_12),
    RSA_MD5(257, "rsa_md5", "MD5withRSA", "RSA", WindowEvent.RESIZE, ProtocolVersion.PROTOCOLS_TO_12);

    final int id;
    final String name;
    private final String algorithm;
    final String keyAlgorithm;
    private final AlgorithmParameterSpec signAlgParameter;
    private final SupportedGroupsExtension.NamedGroup namedGroup;
    final int minimalKeySize;
    final List<ProtocolVersion> supportedProtocols;
    final List<ProtocolVersion> handshakeSupportedProtocols;
    final boolean isAvailable;
    private static final String[] hashAlgorithms = {Separation.COLORANT_NONE, "md5", "sha1", "sha224", "sha256", "sha384", "sha512"};
    private static final String[] signatureAlgorithms = {"anonymous", "rsa", "dsa", "ecdsa"};
    private static final Set<CryptoPrimitive> SIGNATURE_PRIMITIVE_SET = Collections.unmodifiableSet(EnumSet.of(CryptoPrimitive.SIGNATURE));

    /* loaded from: jsse.jar:sun/security/ssl/SignatureScheme$SigAlgParamSpec.class */
    enum SigAlgParamSpec {
        RSA_PSS_SHA256("SHA-256", 32),
        RSA_PSS_SHA384("SHA-384", 48),
        RSA_PSS_SHA512("SHA-512", 64);

        private final AlgorithmParameterSpec parameterSpec;
        final boolean isAvailable;

        SigAlgParamSpec(String str, int i2) {
            PSSParameterSpec pSSParameterSpec = new PSSParameterSpec(str, "MGF1", new MGF1ParameterSpec(str), i2, 1);
            boolean z2 = true;
            try {
                JsseJce.getSignature("RSASSA-PSS").setParameter(pSSParameterSpec);
            } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException e2) {
                z2 = false;
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("RSASSA-PSS signature with " + str + " is not supported by the underlying providers", e2);
                }
            }
            this.isAvailable = z2;
            this.parameterSpec = z2 ? pSSParameterSpec : null;
        }

        AlgorithmParameterSpec getParameterSpec() {
            return this.parameterSpec;
        }
    }

    SignatureScheme(int i2, String str, String str2, String str3, ProtocolVersion[] protocolVersionArr) {
        this(i2, str, str2, str3, -1, protocolVersionArr);
    }

    SignatureScheme(int i2, String str, String str2, String str3, int i3, ProtocolVersion[] protocolVersionArr) {
        this(i2, str, str2, str3, null, i3, protocolVersionArr);
    }

    SignatureScheme(int i2, String str, String str2, String str3, SigAlgParamSpec sigAlgParamSpec, int i3, ProtocolVersion[] protocolVersionArr) {
        this(i2, str, str2, str3, sigAlgParamSpec, null, i3, protocolVersionArr, protocolVersionArr);
    }

    SignatureScheme(int i2, String str, String str2, String str3, SupportedGroupsExtension.NamedGroup namedGroup, ProtocolVersion[] protocolVersionArr) {
        this(i2, str, str2, str3, null, namedGroup, -1, protocolVersionArr, protocolVersionArr);
    }

    SignatureScheme(int i2, String str, String str2, String str3, SigAlgParamSpec sigAlgParamSpec, SupportedGroupsExtension.NamedGroup namedGroup, int i3, ProtocolVersion[] protocolVersionArr, ProtocolVersion[] protocolVersionArr2) {
        this.id = i2;
        this.name = str;
        this.algorithm = str2;
        this.keyAlgorithm = str3;
        this.signAlgParameter = sigAlgParamSpec != null ? sigAlgParamSpec.parameterSpec : null;
        this.namedGroup = namedGroup;
        this.minimalKeySize = i3;
        this.supportedProtocols = Arrays.asList(protocolVersionArr);
        this.handshakeSupportedProtocols = Arrays.asList(protocolVersionArr2);
        boolean zIsEcAvailable = "EC".equals(str3) ? JsseJce.isEcAvailable() : true;
        if (zIsEcAvailable) {
            if (sigAlgParamSpec != null) {
                zIsEcAvailable = sigAlgParamSpec.isAvailable;
            } else {
                try {
                    JsseJce.getSignature(str2);
                } catch (Exception e2) {
                    zIsEcAvailable = false;
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.warning("Signature algorithm, " + str2 + ", is not supported by the underlying providers", new Object[0]);
                    }
                }
            }
        }
        if (zIsEcAvailable && ((i2 >> 8) & 255) == 3 && Security.getProvider("SunMSCAPI") != null) {
            zIsEcAvailable = false;
        }
        this.isAvailable = zIsEcAvailable;
    }

    static SignatureScheme valueOf(int i2) {
        for (SignatureScheme signatureScheme : values()) {
            if (signatureScheme.id == i2) {
                return signatureScheme;
            }
        }
        return null;
    }

    static String nameOf(int i2) {
        for (SignatureScheme signatureScheme : values()) {
            if (signatureScheme.id == i2) {
                return signatureScheme.name;
            }
        }
        int i3 = (i2 >> 8) & 255;
        int i4 = i2 & 255;
        return (i4 >= signatureAlgorithms.length ? "UNDEFINED-SIGNATURE(" + i4 + ")" : signatureAlgorithms[i4]) + "_" + (i3 >= hashAlgorithms.length ? "UNDEFINED-HASH(" + i3 + ")" : hashAlgorithms[i3]);
    }

    static SignatureScheme nameOf(String str) {
        for (SignatureScheme signatureScheme : values()) {
            if (signatureScheme.name.equalsIgnoreCase(str)) {
                return signatureScheme;
            }
        }
        return null;
    }

    static int sizeInRecord() {
        return 2;
    }

    static List<SignatureScheme> getSupportedAlgorithms(SSLConfiguration sSLConfiguration, AlgorithmConstraints algorithmConstraints, List<ProtocolVersion> list) {
        LinkedList linkedList = new LinkedList();
        for (SignatureScheme signatureScheme : values()) {
            if (!signatureScheme.isAvailable || (!sSLConfiguration.signatureSchemes.isEmpty() && !sSLConfiguration.signatureSchemes.contains(signatureScheme))) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                    SSLLogger.finest("Ignore unsupported signature scheme: " + signatureScheme.name, new Object[0]);
                }
            } else {
                boolean z2 = false;
                Iterator<ProtocolVersion> it = list.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    if (signatureScheme.supportedProtocols.contains(it.next())) {
                        z2 = true;
                        break;
                    }
                }
                if (z2) {
                    if (algorithmConstraints.permits(SIGNATURE_PRIMITIVE_SET, signatureScheme.algorithm, null)) {
                        linkedList.add(signatureScheme);
                    } else if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                        SSLLogger.finest("Ignore disabled signature scheme: " + signatureScheme.name, new Object[0]);
                    }
                } else if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                    SSLLogger.finest("Ignore inactive signature scheme: " + signatureScheme.name, new Object[0]);
                }
            }
        }
        return linkedList;
    }

    static List<SignatureScheme> getSupportedAlgorithms(SSLConfiguration sSLConfiguration, AlgorithmConstraints algorithmConstraints, ProtocolVersion protocolVersion, int[] iArr) {
        LinkedList linkedList = new LinkedList();
        for (int i2 : iArr) {
            SignatureScheme signatureSchemeValueOf = valueOf(i2);
            if (signatureSchemeValueOf == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("Unsupported signature scheme: " + nameOf(i2), new Object[0]);
                }
            } else if (signatureSchemeValueOf.isAvailable && signatureSchemeValueOf.supportedProtocols.contains(protocolVersion) && ((sSLConfiguration.signatureSchemes.isEmpty() || sSLConfiguration.signatureSchemes.contains(signatureSchemeValueOf)) && algorithmConstraints.permits(SIGNATURE_PRIMITIVE_SET, signatureSchemeValueOf.algorithm, null))) {
                linkedList.add(signatureSchemeValueOf);
            } else if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.warning("Unsupported signature scheme: " + signatureSchemeValueOf.name, new Object[0]);
            }
        }
        return linkedList;
    }

    static SignatureScheme getPreferableAlgorithm(List<SignatureScheme> list, SignatureScheme signatureScheme, ProtocolVersion protocolVersion) {
        for (SignatureScheme signatureScheme2 : list) {
            if (signatureScheme2.isAvailable && signatureScheme2.handshakeSupportedProtocols.contains(protocolVersion) && signatureScheme.keyAlgorithm.equalsIgnoreCase(signatureScheme2.keyAlgorithm)) {
                return signatureScheme2;
            }
        }
        return null;
    }

    static Map.Entry<SignatureScheme, Signature> getSignerOfPreferableAlgorithm(List<SignatureScheme> list, X509Authentication.X509Possession x509Possession, ProtocolVersion protocolVersion) {
        int keySize;
        SupportedGroupsExtension.NamedGroup namedGroupValueOf;
        Signature signer;
        Signature signer2;
        PrivateKey privateKey = x509Possession.popPrivateKey;
        String algorithm = privateKey.getAlgorithm();
        if (algorithm.equalsIgnoreCase("RSA") || algorithm.equalsIgnoreCase("RSASSA-PSS")) {
            keySize = KeyUtil.getKeySize(privateKey);
        } else {
            keySize = Integer.MAX_VALUE;
        }
        for (SignatureScheme signatureScheme : list) {
            if (signatureScheme.isAvailable && keySize >= signatureScheme.minimalKeySize && signatureScheme.handshakeSupportedProtocols.contains(protocolVersion) && algorithm.equalsIgnoreCase(signatureScheme.keyAlgorithm)) {
                if (signatureScheme.namedGroup != null && signatureScheme.namedGroup.type == SupportedGroupsExtension.NamedGroupType.NAMED_GROUP_ECDHE) {
                    ECParameterSpec eCParameterSpec = x509Possession.getECParameterSpec();
                    if (eCParameterSpec != null && signatureScheme.namedGroup == SupportedGroupsExtension.NamedGroup.valueOf(eCParameterSpec) && (signer2 = signatureScheme.getSigner(privateKey)) != null) {
                        return new AbstractMap.SimpleImmutableEntry(signatureScheme, signer2);
                    }
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                        SSLLogger.finest("Ignore the signature algorithm (" + ((Object) signatureScheme) + "), unsupported EC parameter spec: " + ((Object) eCParameterSpec), new Object[0]);
                    }
                } else if ("EC".equals(signatureScheme.keyAlgorithm)) {
                    ECParameterSpec eCParameterSpec2 = x509Possession.getECParameterSpec();
                    if (eCParameterSpec2 != null && (namedGroupValueOf = SupportedGroupsExtension.NamedGroup.valueOf(eCParameterSpec2)) != null && SupportedGroupsExtension.SupportedGroups.isSupported(namedGroupValueOf) && (signer = signatureScheme.getSigner(privateKey)) != null) {
                        return new AbstractMap.SimpleImmutableEntry(signatureScheme, signer);
                    }
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                        SSLLogger.finest("Ignore the legacy signature algorithm (" + ((Object) signatureScheme) + "), unsupported EC parameter spec: " + ((Object) eCParameterSpec2), new Object[0]);
                    }
                } else {
                    Signature signer3 = signatureScheme.getSigner(privateKey);
                    if (signer3 != null) {
                        return new AbstractMap.SimpleImmutableEntry(signatureScheme, signer3);
                    }
                }
            }
        }
        return null;
    }

    static String[] getAlgorithmNames(Collection<SignatureScheme> collection) {
        if (collection != null) {
            ArrayList arrayList = new ArrayList(collection.size());
            Iterator<SignatureScheme> it = collection.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next().algorithm);
            }
            return (String[]) arrayList.toArray(new String[0]);
        }
        return new String[0];
    }

    Signature getVerifier(PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        if (!this.isAvailable) {
            return null;
        }
        Signature signature = Signature.getInstance(this.algorithm);
        SignatureUtil.initVerifyWithParam(signature, publicKey, this.signAlgParameter);
        return signature;
    }

    private Signature getSigner(PrivateKey privateKey) {
        if (!this.isAvailable) {
            return null;
        }
        try {
            Signature signature = Signature.getInstance(this.algorithm);
            SignatureUtil.initSignWithParam(signature, privateKey, this.signAlgParameter, null);
            return signature;
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException e2) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                SSLLogger.finest("Ignore unsupported signature algorithm (" + this.name + ")", e2);
                return null;
            }
            return null;
        }
    }
}
