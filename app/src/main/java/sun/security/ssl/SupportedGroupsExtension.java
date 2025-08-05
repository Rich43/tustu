package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.AlgorithmConstraints;
import java.security.AlgorithmParameters;
import java.security.CryptoPrimitive;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.crypto.spec.DHParameterSpec;
import javax.net.ssl.SSLProtocolException;
import sun.security.action.GetPropertyAction;
import sun.security.ssl.SSLExtension;
import sun.security.ssl.SSLHandshake;

/* loaded from: jsse.jar:sun/security/ssl/SupportedGroupsExtension.class */
final class SupportedGroupsExtension {
    static final HandshakeProducer chNetworkProducer = new CHSupportedGroupsProducer();
    static final SSLExtension.ExtensionConsumer chOnLoadConsumer = new CHSupportedGroupsConsumer();
    static final SSLStringizer sgsStringizer = new SupportedGroupsStringizer();
    static final HandshakeProducer eeNetworkProducer = new EESupportedGroupsProducer();
    static final SSLExtension.ExtensionConsumer eeOnLoadConsumer = new EESupportedGroupsConsumer();

    SupportedGroupsExtension() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/SupportedGroupsExtension$SupportedGroupsSpec.class */
    static final class SupportedGroupsSpec implements SSLExtension.SSLExtensionSpec {
        final int[] namedGroupsIds;

        private SupportedGroupsSpec(int[] iArr) {
            this.namedGroupsIds = iArr;
        }

        private SupportedGroupsSpec(List<NamedGroup> list) {
            this.namedGroupsIds = new int[list.size()];
            int i2 = 0;
            Iterator<NamedGroup> it = list.iterator();
            while (it.hasNext()) {
                int i3 = i2;
                i2++;
                this.namedGroupsIds[i3] = it.next().id;
            }
        }

        private SupportedGroupsSpec(ByteBuffer byteBuffer) throws IOException {
            if (byteBuffer.remaining() < 2) {
                throw new SSLProtocolException("Invalid supported_groups extension: insufficient data");
            }
            byte[] bytes16 = Record.getBytes16(byteBuffer);
            if (byteBuffer.hasRemaining()) {
                throw new SSLProtocolException("Invalid supported_groups extension: unknown extra data");
            }
            if (bytes16 == null || bytes16.length == 0 || bytes16.length % 2 != 0) {
                throw new SSLProtocolException("Invalid supported_groups extension: incomplete data");
            }
            int[] iArr = new int[bytes16.length / 2];
            int i2 = 0;
            int i3 = 0;
            while (i2 < bytes16.length) {
                int i4 = i3;
                i3++;
                int i5 = i2;
                int i6 = i2 + 1;
                i2 = i6 + 1;
                iArr[i4] = ((bytes16[i5] & 255) << 8) | (bytes16[i6] & 255);
            }
            this.namedGroupsIds = iArr;
        }

        public String toString() {
            MessageFormat messageFormat = new MessageFormat("\"versions\": '['{0}']'", Locale.ENGLISH);
            if (this.namedGroupsIds == null || this.namedGroupsIds.length == 0) {
                return messageFormat.format(new Object[]{"<no supported named group specified>"});
            }
            StringBuilder sb = new StringBuilder(512);
            boolean z2 = true;
            for (int i2 : this.namedGroupsIds) {
                if (z2) {
                    z2 = false;
                } else {
                    sb.append(", ");
                }
                sb.append(NamedGroup.nameOf(i2));
            }
            return messageFormat.format(new Object[]{sb.toString()});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SupportedGroupsExtension$SupportedGroupsStringizer.class */
    private static final class SupportedGroupsStringizer implements SSLStringizer {
        private SupportedGroupsStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new SupportedGroupsSpec(byteBuffer).toString();
            } catch (IOException e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SupportedGroupsExtension$NamedGroupType.class */
    enum NamedGroupType {
        NAMED_GROUP_ECDHE,
        NAMED_GROUP_FFDHE,
        NAMED_GROUP_XDH,
        NAMED_GROUP_ARBITRARY,
        NAMED_GROUP_NONE;

        boolean isSupported(List<CipherSuite> list) {
            for (CipherSuite cipherSuite : list) {
                if (cipherSuite.keyExchange == null || cipherSuite.keyExchange.groupType == this) {
                    return true;
                }
            }
            return false;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SupportedGroupsExtension$NamedGroup.class */
    enum NamedGroup {
        SECT163_K1(1, "sect163k1", "1.3.132.0.1", true, ProtocolVersion.PROTOCOLS_TO_12),
        SECT163_R1(2, "sect163r1", "1.3.132.0.2", false, ProtocolVersion.PROTOCOLS_TO_12),
        SECT163_R2(3, "sect163r2", "1.3.132.0.15", true, ProtocolVersion.PROTOCOLS_TO_12),
        SECT193_R1(4, "sect193r1", "1.3.132.0.24", false, ProtocolVersion.PROTOCOLS_TO_12),
        SECT193_R2(5, "sect193r2", "1.3.132.0.25", false, ProtocolVersion.PROTOCOLS_TO_12),
        SECT233_K1(6, "sect233k1", "1.3.132.0.26", true, ProtocolVersion.PROTOCOLS_TO_12),
        SECT233_R1(7, "sect233r1", "1.3.132.0.27", true, ProtocolVersion.PROTOCOLS_TO_12),
        SECT239_K1(8, "sect239k1", "1.3.132.0.3", false, ProtocolVersion.PROTOCOLS_TO_12),
        SECT283_K1(9, "sect283k1", "1.3.132.0.16", true, ProtocolVersion.PROTOCOLS_TO_12),
        SECT283_R1(10, "sect283r1", "1.3.132.0.17", true, ProtocolVersion.PROTOCOLS_TO_12),
        SECT409_K1(11, "sect409k1", "1.3.132.0.36", true, ProtocolVersion.PROTOCOLS_TO_12),
        SECT409_R1(12, "sect409r1", "1.3.132.0.37", true, ProtocolVersion.PROTOCOLS_TO_12),
        SECT571_K1(13, "sect571k1", "1.3.132.0.38", true, ProtocolVersion.PROTOCOLS_TO_12),
        SECT571_R1(14, "sect571r1", "1.3.132.0.39", true, ProtocolVersion.PROTOCOLS_TO_12),
        SECP160_K1(15, "secp160k1", "1.3.132.0.9", false, ProtocolVersion.PROTOCOLS_TO_12),
        SECP160_R1(16, "secp160r1", "1.3.132.0.8", false, ProtocolVersion.PROTOCOLS_TO_12),
        SECP160_R2(17, "secp160r2", "1.3.132.0.30", false, ProtocolVersion.PROTOCOLS_TO_12),
        SECP192_K1(18, "secp192k1", "1.3.132.0.31", false, ProtocolVersion.PROTOCOLS_TO_12),
        SECP192_R1(19, "secp192r1", "1.2.840.10045.3.1.1", true, ProtocolVersion.PROTOCOLS_TO_12),
        SECP224_K1(20, "secp224k1", "1.3.132.0.32", false, ProtocolVersion.PROTOCOLS_TO_12),
        SECP224_R1(21, "secp224r1", "1.3.132.0.33", true, ProtocolVersion.PROTOCOLS_TO_12),
        SECP256_K1(22, "secp256k1", "1.3.132.0.10", false, ProtocolVersion.PROTOCOLS_TO_12),
        SECP256_R1(23, "secp256r1", "1.2.840.10045.3.1.7", true, ProtocolVersion.PROTOCOLS_TO_13),
        SECP384_R1(24, "secp384r1", "1.3.132.0.34", true, ProtocolVersion.PROTOCOLS_TO_13),
        SECP521_R1(25, "secp521r1", "1.3.132.0.35", true, ProtocolVersion.PROTOCOLS_TO_13),
        X25519(29, "x25519", true, "x25519", ProtocolVersion.PROTOCOLS_TO_13),
        X448(30, "x448", true, "x448", ProtocolVersion.PROTOCOLS_TO_13),
        FFDHE_2048(256, "ffdhe2048", true, ProtocolVersion.PROTOCOLS_TO_13),
        FFDHE_3072(257, "ffdhe3072", true, ProtocolVersion.PROTOCOLS_TO_13),
        FFDHE_4096(258, "ffdhe4096", true, ProtocolVersion.PROTOCOLS_TO_13),
        FFDHE_6144(259, "ffdhe6144", true, ProtocolVersion.PROTOCOLS_TO_13),
        FFDHE_8192(260, "ffdhe8192", true, ProtocolVersion.PROTOCOLS_TO_13),
        ARBITRARY_PRIME(65281, "arbitrary_explicit_prime_curves", ProtocolVersion.PROTOCOLS_TO_12),
        ARBITRARY_CHAR2(65282, "arbitrary_explicit_char2_curves", ProtocolVersion.PROTOCOLS_TO_12);

        final int id;
        final NamedGroupType type;
        final String name;
        final String oid;
        final String algorithm;
        final boolean isFips;
        final ProtocolVersion[] supportedProtocols;
        final boolean isEcAvailable;

        NamedGroup(int i2, String str, String str2, boolean z2, ProtocolVersion[] protocolVersionArr) {
            this.id = i2;
            this.type = NamedGroupType.NAMED_GROUP_ECDHE;
            this.name = str;
            this.oid = str2;
            this.algorithm = "EC";
            this.isFips = z2;
            this.supportedProtocols = protocolVersionArr;
            this.isEcAvailable = JsseJce.isEcAvailable();
        }

        NamedGroup(int i2, String str, boolean z2, String str2, ProtocolVersion[] protocolVersionArr) {
            this.id = i2;
            this.type = NamedGroupType.NAMED_GROUP_XDH;
            this.name = str;
            this.oid = null;
            this.algorithm = str2;
            this.isFips = z2;
            this.supportedProtocols = protocolVersionArr;
            this.isEcAvailable = true;
        }

        NamedGroup(int i2, String str, boolean z2, ProtocolVersion[] protocolVersionArr) {
            this.id = i2;
            this.type = NamedGroupType.NAMED_GROUP_FFDHE;
            this.name = str;
            this.oid = null;
            this.algorithm = "DiffieHellman";
            this.isFips = z2;
            this.supportedProtocols = protocolVersionArr;
            this.isEcAvailable = true;
        }

        NamedGroup(int i2, String str, ProtocolVersion[] protocolVersionArr) {
            this.id = i2;
            this.type = NamedGroupType.NAMED_GROUP_ARBITRARY;
            this.name = str;
            this.oid = null;
            this.algorithm = "EC";
            this.isFips = false;
            this.supportedProtocols = protocolVersionArr;
            this.isEcAvailable = true;
        }

        static NamedGroup valueOf(int i2) {
            for (NamedGroup namedGroup : values()) {
                if (namedGroup.id == i2) {
                    return namedGroup;
                }
            }
            return null;
        }

        static NamedGroup valueOf(ECParameterSpec eCParameterSpec) {
            String namedCurveOid = JsseJce.getNamedCurveOid(eCParameterSpec);
            if (namedCurveOid != null && !namedCurveOid.isEmpty()) {
                for (NamedGroup namedGroup : values()) {
                    if (namedGroup.type == NamedGroupType.NAMED_GROUP_ECDHE && namedCurveOid.equals(namedGroup.oid)) {
                        return namedGroup;
                    }
                }
                return null;
            }
            return null;
        }

        static NamedGroup valueOf(DHParameterSpec dHParameterSpec) {
            for (Map.Entry<NamedGroup, AlgorithmParameters> entry : SupportedGroups.namedGroupParams.entrySet()) {
                NamedGroup key = entry.getKey();
                if (key.type == NamedGroupType.NAMED_GROUP_FFDHE) {
                    DHParameterSpec dHParameterSpec2 = null;
                    try {
                        dHParameterSpec2 = (DHParameterSpec) entry.getValue().getParameterSpec(DHParameterSpec.class);
                    } catch (InvalidParameterSpecException e2) {
                    }
                    if (dHParameterSpec2 != null && dHParameterSpec2.getP().equals(dHParameterSpec.getP()) && dHParameterSpec2.getG().equals(dHParameterSpec.getG())) {
                        return key;
                    }
                }
            }
            return null;
        }

        static NamedGroup nameOf(String str) {
            for (NamedGroup namedGroup : values()) {
                if (namedGroup.name.equals(str)) {
                    return namedGroup;
                }
            }
            return null;
        }

        static String nameOf(int i2) {
            for (NamedGroup namedGroup : values()) {
                if (namedGroup.id == i2) {
                    return namedGroup.name;
                }
            }
            return "UNDEFINED-NAMED-GROUP(" + i2 + ")";
        }

        boolean isAvailable(List<ProtocolVersion> list) {
            if (this.isEcAvailable) {
                for (ProtocolVersion protocolVersion : this.supportedProtocols) {
                    if (list.contains(protocolVersion)) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        boolean isAvailable(ProtocolVersion protocolVersion) {
            if (this.isEcAvailable) {
                for (ProtocolVersion protocolVersion2 : this.supportedProtocols) {
                    if (protocolVersion == protocolVersion2) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        boolean isSupported(List<CipherSuite> list) {
            for (CipherSuite cipherSuite : list) {
                if (isAvailable(cipherSuite.supportedProtocols) && (cipherSuite.keyExchange == null || cipherSuite.keyExchange.groupType == this.type)) {
                    return true;
                }
            }
            return false;
        }

        AlgorithmParameters getParameters() {
            return SupportedGroups.namedGroupParams.get(this);
        }

        AlgorithmParameterSpec getParameterSpec() {
            if (this.type == NamedGroupType.NAMED_GROUP_ECDHE) {
                return SupportedGroups.getECGenParamSpec(this);
            }
            if (this.type == NamedGroupType.NAMED_GROUP_FFDHE) {
                return SupportedGroups.getDHParameterSpec(this);
            }
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SupportedGroupsExtension$SupportedGroups.class */
    static class SupportedGroups {
        static final boolean enableFFDHE = Utilities.getBooleanProperty("jsse.enableFFDHE", true);
        static final Map<NamedGroup, AlgorithmParameters> namedGroupParams = new HashMap();
        static final NamedGroup[] supportedNamedGroups;

        SupportedGroups() {
        }

        static {
            NamedGroup[] namedGroupArr;
            ArrayList arrayList;
            NamedGroup namedGroupNameOf;
            boolean zIsFIPS = SunJSSE.isFIPS();
            String strPrivilegedGetProperty = GetPropertyAction.privilegedGetProperty("jdk.tls.namedGroups");
            if (strPrivilegedGetProperty != null && !strPrivilegedGetProperty.isEmpty() && strPrivilegedGetProperty.length() > 1 && strPrivilegedGetProperty.charAt(0) == '\"' && strPrivilegedGetProperty.charAt(strPrivilegedGetProperty.length() - 1) == '\"') {
                strPrivilegedGetProperty = strPrivilegedGetProperty.substring(1, strPrivilegedGetProperty.length() - 1);
            }
            if (strPrivilegedGetProperty != null && !strPrivilegedGetProperty.isEmpty()) {
                String[] strArrSplit = strPrivilegedGetProperty.split(",");
                arrayList = new ArrayList(strArrSplit.length);
                for (String str : strArrSplit) {
                    String strTrim = str.trim();
                    if (!strTrim.isEmpty() && (namedGroupNameOf = NamedGroup.nameOf(strTrim)) != null && ((!zIsFIPS || namedGroupNameOf.isFips) && isAvailableGroup(namedGroupNameOf))) {
                        arrayList.add(namedGroupNameOf);
                    }
                }
                if (arrayList.isEmpty()) {
                    throw new IllegalArgumentException("System property jdk.tls.namedGroups(" + strPrivilegedGetProperty + ") contains no supported named groups");
                }
            } else {
                if (zIsFIPS) {
                    namedGroupArr = new NamedGroup[]{NamedGroup.SECP256_R1, NamedGroup.SECP384_R1, NamedGroup.SECP521_R1, NamedGroup.FFDHE_2048, NamedGroup.FFDHE_3072, NamedGroup.FFDHE_4096, NamedGroup.FFDHE_6144, NamedGroup.FFDHE_8192};
                } else {
                    namedGroupArr = new NamedGroup[]{NamedGroup.SECP256_R1, NamedGroup.SECP384_R1, NamedGroup.SECP521_R1, NamedGroup.FFDHE_2048, NamedGroup.FFDHE_3072, NamedGroup.FFDHE_4096, NamedGroup.FFDHE_6144, NamedGroup.FFDHE_8192};
                }
                arrayList = new ArrayList(namedGroupArr.length);
                for (NamedGroup namedGroup : namedGroupArr) {
                    if (isAvailableGroup(namedGroup)) {
                        arrayList.add(namedGroup);
                    }
                }
                if (arrayList.isEmpty() && SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.warning("No default named groups", new Object[0]);
                }
            }
            supportedNamedGroups = new NamedGroup[arrayList.size()];
            int i2 = 0;
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                int i3 = i2;
                i2++;
                supportedNamedGroups[i3] = (NamedGroup) it.next();
            }
        }

        private static boolean isAvailableGroup(NamedGroup namedGroup) {
            AlgorithmParameters algorithmParameters = null;
            AlgorithmParameterSpec fFDHEDHParameterSpec = null;
            if (namedGroup.type == NamedGroupType.NAMED_GROUP_ECDHE) {
                if (namedGroup.oid != null) {
                    try {
                        algorithmParameters = JsseJce.getAlgorithmParameters("EC");
                        fFDHEDHParameterSpec = new ECGenParameterSpec(namedGroup.oid);
                    } catch (NoSuchAlgorithmException e2) {
                        return false;
                    }
                }
            } else if (namedGroup.type == NamedGroupType.NAMED_GROUP_FFDHE) {
                try {
                    algorithmParameters = JsseJce.getAlgorithmParameters("DiffieHellman");
                    fFDHEDHParameterSpec = getFFDHEDHParameterSpec(namedGroup);
                } catch (NoSuchAlgorithmException e3) {
                    return false;
                }
            }
            if (algorithmParameters != null && fFDHEDHParameterSpec != null) {
                try {
                    algorithmParameters.init(fFDHEDHParameterSpec);
                    namedGroupParams.put(namedGroup, algorithmParameters);
                    return true;
                } catch (InvalidParameterSpecException e4) {
                    return false;
                }
            }
            return false;
        }

        private static DHParameterSpec getFFDHEDHParameterSpec(NamedGroup namedGroup) {
            DHParameterSpec dHParameterSpec = null;
            switch (namedGroup) {
                case FFDHE_2048:
                    dHParameterSpec = PredefinedDHParameterSpecs.ffdheParams.get(2048);
                    break;
                case FFDHE_3072:
                    dHParameterSpec = PredefinedDHParameterSpecs.ffdheParams.get(3072);
                    break;
                case FFDHE_4096:
                    dHParameterSpec = PredefinedDHParameterSpecs.ffdheParams.get(4096);
                    break;
                case FFDHE_6144:
                    dHParameterSpec = PredefinedDHParameterSpecs.ffdheParams.get(6144);
                    break;
                case FFDHE_8192:
                    dHParameterSpec = PredefinedDHParameterSpecs.ffdheParams.get(8192);
                    break;
            }
            return dHParameterSpec;
        }

        private static DHParameterSpec getPredefinedDHParameterSpec(NamedGroup namedGroup) {
            DHParameterSpec dHParameterSpec = null;
            switch (namedGroup) {
                case FFDHE_2048:
                    dHParameterSpec = PredefinedDHParameterSpecs.definedParams.get(2048);
                    break;
                case FFDHE_3072:
                    dHParameterSpec = PredefinedDHParameterSpecs.definedParams.get(3072);
                    break;
                case FFDHE_4096:
                    dHParameterSpec = PredefinedDHParameterSpecs.definedParams.get(4096);
                    break;
                case FFDHE_6144:
                    dHParameterSpec = PredefinedDHParameterSpecs.definedParams.get(6144);
                    break;
                case FFDHE_8192:
                    dHParameterSpec = PredefinedDHParameterSpecs.definedParams.get(8192);
                    break;
            }
            return dHParameterSpec;
        }

        static ECGenParameterSpec getECGenParamSpec(NamedGroup namedGroup) {
            if (namedGroup.type != NamedGroupType.NAMED_GROUP_ECDHE) {
                throw new RuntimeException("Not a named EC group: " + ((Object) namedGroup));
            }
            AlgorithmParameters algorithmParameters = namedGroupParams.get(namedGroup);
            if (algorithmParameters == null) {
                throw new RuntimeException("Not a supported EC named group: " + ((Object) namedGroup));
            }
            try {
                return (ECGenParameterSpec) algorithmParameters.getParameterSpec(ECGenParameterSpec.class);
            } catch (InvalidParameterSpecException e2) {
                return new ECGenParameterSpec(namedGroup.oid);
            }
        }

        static DHParameterSpec getDHParameterSpec(NamedGroup namedGroup) {
            if (namedGroup.type != NamedGroupType.NAMED_GROUP_FFDHE) {
                throw new RuntimeException("Not a named DH group: " + ((Object) namedGroup));
            }
            AlgorithmParameters algorithmParameters = namedGroupParams.get(namedGroup);
            if (algorithmParameters == null) {
                throw new RuntimeException("Not a supported DH named group: " + ((Object) namedGroup));
            }
            try {
                return (DHParameterSpec) algorithmParameters.getParameterSpec(DHParameterSpec.class);
            } catch (InvalidParameterSpecException e2) {
                return getPredefinedDHParameterSpec(namedGroup);
            }
        }

        static boolean isActivatable(AlgorithmConstraints algorithmConstraints, NamedGroupType namedGroupType) {
            boolean z2 = false;
            for (NamedGroup namedGroup : supportedNamedGroups) {
                if (namedGroup.type == namedGroupType) {
                    if (algorithmConstraints.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), namedGroup.algorithm, namedGroupParams.get(namedGroup))) {
                        return true;
                    }
                    if (!z2 && namedGroupType == NamedGroupType.NAMED_GROUP_FFDHE) {
                        z2 = true;
                    }
                }
            }
            return !z2 && namedGroupType == NamedGroupType.NAMED_GROUP_FFDHE;
        }

        static boolean isActivatable(AlgorithmConstraints algorithmConstraints, NamedGroup namedGroup) {
            if (!isSupported(namedGroup)) {
                return false;
            }
            return algorithmConstraints.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), namedGroup.algorithm, namedGroupParams.get(namedGroup));
        }

        static boolean isSupported(NamedGroup namedGroup) {
            for (NamedGroup namedGroup2 : supportedNamedGroups) {
                if (namedGroup.id == namedGroup2.id) {
                    return true;
                }
            }
            return false;
        }

        static NamedGroup getPreferredGroup(ProtocolVersion protocolVersion, AlgorithmConstraints algorithmConstraints, NamedGroupType namedGroupType, List<NamedGroup> list) {
            for (NamedGroup namedGroup : list) {
                if (namedGroup.type == namedGroupType && namedGroup.isAvailable(protocolVersion) && isSupported(namedGroup) && algorithmConstraints.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), namedGroup.algorithm, namedGroupParams.get(namedGroup))) {
                    return namedGroup;
                }
            }
            return null;
        }

        static NamedGroup getPreferredGroup(ProtocolVersion protocolVersion, AlgorithmConstraints algorithmConstraints, NamedGroupType namedGroupType) {
            for (NamedGroup namedGroup : supportedNamedGroups) {
                if (namedGroup.type == namedGroupType && namedGroup.isAvailable(protocolVersion) && algorithmConstraints.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), namedGroup.algorithm, namedGroupParams.get(namedGroup))) {
                    return namedGroup;
                }
            }
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SupportedGroupsExtension$CHSupportedGroupsProducer.class */
    private static final class CHSupportedGroupsProducer extends SupportedGroups implements HandshakeProducer {
        private CHSupportedGroupsProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_SUPPORTED_GROUPS)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable supported_groups extension", new Object[0]);
                    return null;
                }
                return null;
            }
            ArrayList arrayList = new ArrayList(SupportedGroups.supportedNamedGroups.length);
            for (NamedGroup namedGroup : SupportedGroups.supportedNamedGroups) {
                if (SupportedGroups.enableFFDHE || namedGroup.type != NamedGroupType.NAMED_GROUP_FFDHE) {
                    if (namedGroup.isAvailable(clientHandshakeContext.activeProtocols) && namedGroup.isSupported(clientHandshakeContext.activeCipherSuites) && clientHandshakeContext.algorithmConstraints.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), namedGroup.algorithm, namedGroupParams.get(namedGroup))) {
                        arrayList.add(namedGroup);
                    } else if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.fine("Ignore inactive or disabled named group: " + namedGroup.name, new Object[0]);
                    }
                }
            }
            if (arrayList.isEmpty()) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("no available named group", new Object[0]);
                    return null;
                }
                return null;
            }
            int size = arrayList.size() << 1;
            byte[] bArr = new byte[size + 2];
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
            Record.putInt16(byteBufferWrap, size);
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                Record.putInt16(byteBufferWrap, ((NamedGroup) it.next()).id);
            }
            clientHandshakeContext.clientRequestedNamedGroups = Collections.unmodifiableList(arrayList);
            clientHandshakeContext.handshakeExtensions.put(SSLExtension.CH_SUPPORTED_GROUPS, new SupportedGroupsSpec(arrayList));
            return bArr;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SupportedGroupsExtension$CHSupportedGroupsConsumer.class */
    private static final class CHSupportedGroupsConsumer implements SSLExtension.ExtensionConsumer {
        private CHSupportedGroupsConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_SUPPORTED_GROUPS)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable supported_groups extension", new Object[0]);
                    return;
                }
                return;
            }
            try {
                SupportedGroupsSpec supportedGroupsSpec = new SupportedGroupsSpec(byteBuffer);
                LinkedList linkedList = new LinkedList();
                for (int i2 : supportedGroupsSpec.namedGroupsIds) {
                    NamedGroup namedGroupValueOf = NamedGroup.valueOf(i2);
                    if (namedGroupValueOf != null) {
                        linkedList.add(namedGroupValueOf);
                    }
                }
                serverHandshakeContext.clientRequestedNamedGroups = linkedList;
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.CH_SUPPORTED_GROUPS, supportedGroupsSpec);
            } catch (IOException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SupportedGroupsExtension$EESupportedGroupsProducer.class */
    private static final class EESupportedGroupsProducer extends SupportedGroups implements HandshakeProducer {
        private EESupportedGroupsProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.EE_SUPPORTED_GROUPS)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable supported_groups extension", new Object[0]);
                    return null;
                }
                return null;
            }
            ArrayList arrayList = new ArrayList(SupportedGroups.supportedNamedGroups.length);
            for (NamedGroup namedGroup : SupportedGroups.supportedNamedGroups) {
                if (SupportedGroups.enableFFDHE || namedGroup.type != NamedGroupType.NAMED_GROUP_FFDHE) {
                    if (namedGroup.isAvailable(serverHandshakeContext.activeProtocols) && namedGroup.isSupported(serverHandshakeContext.activeCipherSuites) && serverHandshakeContext.algorithmConstraints.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), namedGroup.algorithm, namedGroupParams.get(namedGroup))) {
                        arrayList.add(namedGroup);
                    } else if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.fine("Ignore inactive or disabled named group: " + namedGroup.name, new Object[0]);
                    }
                }
            }
            if (arrayList.isEmpty()) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.warning("no available named group", new Object[0]);
                    return null;
                }
                return null;
            }
            int size = arrayList.size() << 1;
            byte[] bArr = new byte[size + 2];
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
            Record.putInt16(byteBufferWrap, size);
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                Record.putInt16(byteBufferWrap, ((NamedGroup) it.next()).id);
            }
            serverHandshakeContext.conContext.serverRequestedNamedGroups = Collections.unmodifiableList(arrayList);
            serverHandshakeContext.handshakeExtensions.put(SSLExtension.EE_SUPPORTED_GROUPS, new SupportedGroupsSpec(arrayList));
            return bArr;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SupportedGroupsExtension$EESupportedGroupsConsumer.class */
    private static final class EESupportedGroupsConsumer implements SSLExtension.ExtensionConsumer {
        private EESupportedGroupsConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.EE_SUPPORTED_GROUPS)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable supported_groups extension", new Object[0]);
                    return;
                }
                return;
            }
            try {
                SupportedGroupsSpec supportedGroupsSpec = new SupportedGroupsSpec(byteBuffer);
                ArrayList arrayList = new ArrayList(supportedGroupsSpec.namedGroupsIds.length);
                for (int i2 : supportedGroupsSpec.namedGroupsIds) {
                    NamedGroup namedGroupValueOf = NamedGroup.valueOf(i2);
                    if (namedGroupValueOf != null) {
                        arrayList.add(namedGroupValueOf);
                    }
                }
                clientHandshakeContext.conContext.serverRequestedNamedGroups = arrayList;
                clientHandshakeContext.handshakeExtensions.put(SSLExtension.EE_SUPPORTED_GROUPS, supportedGroupsSpec);
            } catch (IOException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }
}
