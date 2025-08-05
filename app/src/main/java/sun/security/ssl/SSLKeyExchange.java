package sun.security.ssl;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import sun.security.ssl.CipherSuite;
import sun.security.ssl.DHKeyExchange;
import sun.security.ssl.ECDHKeyExchange;
import sun.security.ssl.SupportedGroupsExtension;
import sun.security.ssl.X509Authentication;

/* loaded from: jsse.jar:sun/security/ssl/SSLKeyExchange.class */
final class SSLKeyExchange implements SSLKeyAgreementGenerator, SSLHandshakeBinding {
    private final SSLAuthentication authentication;
    private final SSLKeyAgreement keyAgreement;

    SSLKeyExchange(X509Authentication x509Authentication, SSLKeyAgreement sSLKeyAgreement) {
        this.authentication = x509Authentication;
        this.keyAgreement = sSLKeyAgreement;
    }

    SSLPossession[] createPossessions(HandshakeContext handshakeContext) {
        SSLPossession sSLPossessionCreatePossession = null;
        if (this.authentication != null) {
            sSLPossessionCreatePossession = this.authentication.createPossession(handshakeContext);
            if (sSLPossessionCreatePossession == null) {
                return new SSLPossession[0];
            }
            if (handshakeContext instanceof ServerHandshakeContext) {
                ((ServerHandshakeContext) handshakeContext).interimAuthn = sSLPossessionCreatePossession;
            }
        }
        if (this.keyAgreement == T12KeyAgreement.RSA_EXPORT) {
            if (JsseJce.getRSAKeyLength(((X509Authentication.X509Possession) sSLPossessionCreatePossession).popCerts[0].getPublicKey()) <= 512) {
                return this.authentication != null ? new SSLPossession[]{sSLPossessionCreatePossession} : new SSLPossession[0];
            }
            SSLPossession sSLPossessionCreatePossession2 = this.keyAgreement.createPossession(handshakeContext);
            if (sSLPossessionCreatePossession2 == null) {
                return new SSLPossession[0];
            }
            return this.authentication != null ? new SSLPossession[]{sSLPossessionCreatePossession, sSLPossessionCreatePossession2} : new SSLPossession[]{sSLPossessionCreatePossession2};
        }
        SSLPossession sSLPossessionCreatePossession3 = this.keyAgreement.createPossession(handshakeContext);
        if (sSLPossessionCreatePossession3 != null) {
            return this.authentication != null ? new SSLPossession[]{sSLPossessionCreatePossession, sSLPossessionCreatePossession3} : new SSLPossession[]{sSLPossessionCreatePossession3};
        }
        if (this.keyAgreement == T12KeyAgreement.RSA || this.keyAgreement == T12KeyAgreement.ECDH) {
            return this.authentication != null ? new SSLPossession[]{sSLPossessionCreatePossession} : new SSLPossession[0];
        }
        return new SSLPossession[0];
    }

    @Override // sun.security.ssl.SSLKeyAgreementGenerator
    public SSLKeyDerivation createKeyDerivation(HandshakeContext handshakeContext) throws IOException {
        return this.keyAgreement.createKeyDerivation(handshakeContext);
    }

    @Override // sun.security.ssl.SSLHandshakeBinding
    public SSLHandshake[] getRelatedHandshakers(HandshakeContext handshakeContext) {
        SSLHandshake[] relatedHandshakers;
        if (this.authentication != null) {
            relatedHandshakers = this.authentication.getRelatedHandshakers(handshakeContext);
        } else {
            relatedHandshakers = null;
        }
        SSLHandshake[] relatedHandshakers2 = this.keyAgreement.getRelatedHandshakers(handshakeContext);
        if (relatedHandshakers == null || relatedHandshakers.length == 0) {
            return relatedHandshakers2;
        }
        if (relatedHandshakers2 == null || relatedHandshakers2.length == 0) {
            return relatedHandshakers;
        }
        SSLHandshake[] sSLHandshakeArr = (SSLHandshake[]) Arrays.copyOf(relatedHandshakers, relatedHandshakers.length + relatedHandshakers2.length);
        System.arraycopy(relatedHandshakers2, 0, sSLHandshakeArr, relatedHandshakers.length, relatedHandshakers2.length);
        return sSLHandshakeArr;
    }

    @Override // sun.security.ssl.SSLHandshakeBinding
    public Map.Entry<Byte, HandshakeProducer>[] getHandshakeProducers(HandshakeContext handshakeContext) {
        Map.Entry<Byte, HandshakeProducer>[] handshakeProducers;
        if (this.authentication != null) {
            handshakeProducers = this.authentication.getHandshakeProducers(handshakeContext);
        } else {
            handshakeProducers = null;
        }
        Map.Entry<Byte, HandshakeProducer>[] handshakeProducers2 = this.keyAgreement.getHandshakeProducers(handshakeContext);
        if (handshakeProducers == null || handshakeProducers.length == 0) {
            return handshakeProducers2;
        }
        if (handshakeProducers2 == null || handshakeProducers2.length == 0) {
            return handshakeProducers;
        }
        Map.Entry<Byte, HandshakeProducer>[] entryArr = (Map.Entry[]) Arrays.copyOf(handshakeProducers, handshakeProducers.length + handshakeProducers2.length);
        System.arraycopy(handshakeProducers2, 0, entryArr, handshakeProducers.length, handshakeProducers2.length);
        return entryArr;
    }

    @Override // sun.security.ssl.SSLHandshakeBinding
    public Map.Entry<Byte, SSLConsumer>[] getHandshakeConsumers(HandshakeContext handshakeContext) {
        Map.Entry<Byte, SSLConsumer>[] handshakeConsumers;
        if (this.authentication != null) {
            handshakeConsumers = this.authentication.getHandshakeConsumers(handshakeContext);
        } else {
            handshakeConsumers = null;
        }
        Map.Entry<Byte, SSLConsumer>[] handshakeConsumers2 = this.keyAgreement.getHandshakeConsumers(handshakeContext);
        if (handshakeConsumers == null || handshakeConsumers.length == 0) {
            return handshakeConsumers2;
        }
        if (handshakeConsumers2 == null || handshakeConsumers2.length == 0) {
            return handshakeConsumers;
        }
        Map.Entry<Byte, SSLConsumer>[] entryArr = (Map.Entry[]) Arrays.copyOf(handshakeConsumers, handshakeConsumers.length + handshakeConsumers2.length);
        System.arraycopy(handshakeConsumers2, 0, entryArr, handshakeConsumers.length, handshakeConsumers2.length);
        return entryArr;
    }

    static SSLKeyExchange valueOf(CipherSuite.KeyExchange keyExchange, ProtocolVersion protocolVersion) {
        if (keyExchange == null || protocolVersion == null) {
            return null;
        }
        switch (keyExchange) {
            case K_RSA:
                return SSLKeyExRSA.KE;
            case K_RSA_EXPORT:
                return SSLKeyExRSAExport.KE;
            case K_DHE_DSS:
                return SSLKeyExDHEDSS.KE;
            case K_DHE_DSS_EXPORT:
                return SSLKeyExDHEDSSExport.KE;
            case K_DHE_RSA:
                if (!protocolVersion.useTLS12PlusSpec()) {
                    return SSLKeyExDHERSA.KE;
                }
                return SSLKeyExDHERSAOrPSS.KE;
            case K_DHE_RSA_EXPORT:
                return SSLKeyExDHERSAExport.KE;
            case K_DH_ANON:
                return SSLKeyExDHANON.KE;
            case K_DH_ANON_EXPORT:
                return SSLKeyExDHANONExport.KE;
            case K_ECDH_ECDSA:
                return SSLKeyExECDHECDSA.KE;
            case K_ECDH_RSA:
                return SSLKeyExECDHRSA.KE;
            case K_ECDHE_ECDSA:
                return SSLKeyExECDHEECDSA.KE;
            case K_ECDHE_RSA:
                if (!protocolVersion.useTLS12PlusSpec()) {
                    return SSLKeyExECDHERSA.KE;
                }
                return SSLKeyExECDHERSAOrPSS.KE;
            case K_ECDH_ANON:
                return SSLKeyExECDHANON.KE;
            case K_KRB5:
                return SSLKeyExKRB5.KE;
            case K_KRB5_EXPORT:
                return SSLKeyExKRB5EXPORT.KE;
            default:
                return null;
        }
    }

    static SSLKeyExchange valueOf(SupportedGroupsExtension.NamedGroup namedGroup) {
        if (T13KeyAgreement.valueOf(namedGroup) != null) {
            return new SSLKeyExchange(null, T13KeyAgreement.valueOf(namedGroup));
        }
        return null;
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLKeyExchange$SSLKeyExRSA.class */
    private static class SSLKeyExRSA {
        private static SSLKeyExchange KE = new SSLKeyExchange(X509Authentication.RSA, T12KeyAgreement.RSA);

        private SSLKeyExRSA() {
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLKeyExchange$SSLKeyExRSAExport.class */
    private static class SSLKeyExRSAExport {
        private static SSLKeyExchange KE = new SSLKeyExchange(X509Authentication.RSA, T12KeyAgreement.RSA_EXPORT);

        private SSLKeyExRSAExport() {
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLKeyExchange$SSLKeyExDHEDSS.class */
    private static class SSLKeyExDHEDSS {
        private static SSLKeyExchange KE = new SSLKeyExchange(X509Authentication.DSA, T12KeyAgreement.DHE);

        private SSLKeyExDHEDSS() {
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLKeyExchange$SSLKeyExDHEDSSExport.class */
    private static class SSLKeyExDHEDSSExport {
        private static SSLKeyExchange KE = new SSLKeyExchange(X509Authentication.DSA, T12KeyAgreement.DHE_EXPORT);

        private SSLKeyExDHEDSSExport() {
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLKeyExchange$SSLKeyExDHERSA.class */
    private static class SSLKeyExDHERSA {
        private static SSLKeyExchange KE = new SSLKeyExchange(X509Authentication.RSA, T12KeyAgreement.DHE);

        private SSLKeyExDHERSA() {
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLKeyExchange$SSLKeyExDHERSAOrPSS.class */
    private static class SSLKeyExDHERSAOrPSS {
        private static SSLKeyExchange KE = new SSLKeyExchange(X509Authentication.RSA_OR_PSS, T12KeyAgreement.DHE);

        private SSLKeyExDHERSAOrPSS() {
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLKeyExchange$SSLKeyExDHERSAExport.class */
    private static class SSLKeyExDHERSAExport {
        private static SSLKeyExchange KE = new SSLKeyExchange(X509Authentication.RSA, T12KeyAgreement.DHE_EXPORT);

        private SSLKeyExDHERSAExport() {
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLKeyExchange$SSLKeyExDHANON.class */
    private static class SSLKeyExDHANON {
        private static SSLKeyExchange KE = new SSLKeyExchange(null, T12KeyAgreement.DHE);

        private SSLKeyExDHANON() {
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLKeyExchange$SSLKeyExDHANONExport.class */
    private static class SSLKeyExDHANONExport {
        private static SSLKeyExchange KE = new SSLKeyExchange(null, T12KeyAgreement.DHE_EXPORT);

        private SSLKeyExDHANONExport() {
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLKeyExchange$SSLKeyExECDHECDSA.class */
    private static class SSLKeyExECDHECDSA {
        private static SSLKeyExchange KE = new SSLKeyExchange(X509Authentication.EC, T12KeyAgreement.ECDH);

        private SSLKeyExECDHECDSA() {
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLKeyExchange$SSLKeyExECDHRSA.class */
    private static class SSLKeyExECDHRSA {
        private static SSLKeyExchange KE = new SSLKeyExchange(X509Authentication.EC, T12KeyAgreement.ECDH);

        private SSLKeyExECDHRSA() {
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLKeyExchange$SSLKeyExECDHEECDSA.class */
    private static class SSLKeyExECDHEECDSA {
        private static SSLKeyExchange KE = new SSLKeyExchange(X509Authentication.EC, T12KeyAgreement.ECDHE);

        private SSLKeyExECDHEECDSA() {
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLKeyExchange$SSLKeyExECDHERSA.class */
    private static class SSLKeyExECDHERSA {
        private static SSLKeyExchange KE = new SSLKeyExchange(X509Authentication.RSA, T12KeyAgreement.ECDHE);

        private SSLKeyExECDHERSA() {
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLKeyExchange$SSLKeyExECDHERSAOrPSS.class */
    private static class SSLKeyExECDHERSAOrPSS {
        private static SSLKeyExchange KE = new SSLKeyExchange(X509Authentication.RSA_OR_PSS, T12KeyAgreement.ECDHE);

        private SSLKeyExECDHERSAOrPSS() {
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLKeyExchange$SSLKeyExECDHANON.class */
    private static class SSLKeyExECDHANON {
        private static SSLKeyExchange KE = new SSLKeyExchange(null, T12KeyAgreement.ECDHE);

        private SSLKeyExECDHANON() {
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLKeyExchange$SSLKeyExKRB5.class */
    private static class SSLKeyExKRB5 {
        private static SSLKeyExchange KE = new SSLKeyExchange(null, T12KeyAgreement.KRB5);

        private SSLKeyExKRB5() {
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLKeyExchange$SSLKeyExKRB5EXPORT.class */
    private static class SSLKeyExKRB5EXPORT {
        private static SSLKeyExchange KE = new SSLKeyExchange(null, T12KeyAgreement.KRB5_EXPORT);

        private SSLKeyExKRB5EXPORT() {
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLKeyExchange$T12KeyAgreement.class */
    private enum T12KeyAgreement implements SSLKeyAgreement {
        RSA("rsa", null, RSAKeyExchange.kaGenerator),
        RSA_EXPORT("rsa_export", RSAKeyExchange.poGenerator, RSAKeyExchange.kaGenerator),
        DHE("dhe", DHKeyExchange.poGenerator, DHKeyExchange.kaGenerator),
        DHE_EXPORT("dhe_export", DHKeyExchange.poExportableGenerator, DHKeyExchange.kaGenerator),
        ECDH("ecdh", null, ECDHKeyExchange.ecdhKAGenerator),
        ECDHE("ecdhe", ECDHKeyExchange.poGenerator, ECDHKeyExchange.ecdheKAGenerator),
        KRB5("krb5", KrbKeyExchange.poGenerator, KrbKeyExchange.kaGenerator),
        KRB5_EXPORT("krb5_export", KrbKeyExchange.poGenerator, KrbKeyExchange.kaGenerator);

        final String name;
        final SSLPossessionGenerator possessionGenerator;
        final SSLKeyAgreementGenerator keyAgreementGenerator;

        T12KeyAgreement(String str, SSLPossessionGenerator sSLPossessionGenerator, SSLKeyAgreementGenerator sSLKeyAgreementGenerator) {
            this.name = str;
            this.possessionGenerator = sSLPossessionGenerator;
            this.keyAgreementGenerator = sSLKeyAgreementGenerator;
        }

        @Override // sun.security.ssl.SSLPossessionGenerator
        public SSLPossession createPossession(HandshakeContext handshakeContext) {
            if (this.possessionGenerator != null) {
                return this.possessionGenerator.createPossession(handshakeContext);
            }
            return null;
        }

        @Override // sun.security.ssl.SSLKeyAgreementGenerator
        public SSLKeyDerivation createKeyDerivation(HandshakeContext handshakeContext) throws IOException {
            return this.keyAgreementGenerator.createKeyDerivation(handshakeContext);
        }

        @Override // sun.security.ssl.SSLHandshakeBinding
        public SSLHandshake[] getRelatedHandshakers(HandshakeContext handshakeContext) {
            if (!handshakeContext.negotiatedProtocol.useTLS13PlusSpec() && this.possessionGenerator != null) {
                return new SSLHandshake[]{SSLHandshake.SERVER_KEY_EXCHANGE};
            }
            return new SSLHandshake[0];
        }

        @Override // sun.security.ssl.SSLHandshakeBinding
        public Map.Entry<Byte, HandshakeProducer>[] getHandshakeProducers(HandshakeContext handshakeContext) {
            if (handshakeContext.negotiatedProtocol.useTLS13PlusSpec()) {
                return new Map.Entry[0];
            }
            if (handshakeContext.sslConfig.isClientMode) {
                switch (this) {
                    case RSA:
                    case RSA_EXPORT:
                        return new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(Byte.valueOf(SSLHandshake.CLIENT_KEY_EXCHANGE.id), RSAClientKeyExchange.rsaHandshakeProducer)};
                    case DHE:
                    case DHE_EXPORT:
                        return new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(Byte.valueOf(SSLHandshake.CLIENT_KEY_EXCHANGE.id), DHClientKeyExchange.dhHandshakeProducer)};
                    case ECDH:
                        return new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(Byte.valueOf(SSLHandshake.CLIENT_KEY_EXCHANGE.id), ECDHClientKeyExchange.ecdhHandshakeProducer)};
                    case ECDHE:
                        return new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(Byte.valueOf(SSLHandshake.CLIENT_KEY_EXCHANGE.id), ECDHClientKeyExchange.ecdheHandshakeProducer)};
                    case KRB5:
                    case KRB5_EXPORT:
                        return new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(Byte.valueOf(SSLHandshake.CLIENT_KEY_EXCHANGE.id), KrbClientKeyExchange.krbHandshakeProducer)};
                }
            }
            switch (this) {
                case RSA_EXPORT:
                    return new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(Byte.valueOf(SSLHandshake.SERVER_KEY_EXCHANGE.id), RSAServerKeyExchange.rsaHandshakeProducer)};
                case DHE:
                case DHE_EXPORT:
                    return new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(Byte.valueOf(SSLHandshake.SERVER_KEY_EXCHANGE.id), DHServerKeyExchange.dhHandshakeProducer)};
                case ECDHE:
                    return new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(Byte.valueOf(SSLHandshake.SERVER_KEY_EXCHANGE.id), ECDHServerKeyExchange.ecdheHandshakeProducer)};
            }
            return new Map.Entry[0];
        }

        @Override // sun.security.ssl.SSLHandshakeBinding
        public Map.Entry<Byte, SSLConsumer>[] getHandshakeConsumers(HandshakeContext handshakeContext) {
            if (handshakeContext.negotiatedProtocol.useTLS13PlusSpec()) {
                return new Map.Entry[0];
            }
            if (handshakeContext.sslConfig.isClientMode) {
                switch (this) {
                    case RSA_EXPORT:
                        return new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(Byte.valueOf(SSLHandshake.SERVER_KEY_EXCHANGE.id), RSAServerKeyExchange.rsaHandshakeConsumer)};
                    case DHE:
                    case DHE_EXPORT:
                        return new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(Byte.valueOf(SSLHandshake.SERVER_KEY_EXCHANGE.id), DHServerKeyExchange.dhHandshakeConsumer)};
                    case ECDHE:
                        return new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(Byte.valueOf(SSLHandshake.SERVER_KEY_EXCHANGE.id), ECDHServerKeyExchange.ecdheHandshakeConsumer)};
                }
            }
            switch (this) {
                case RSA:
                case RSA_EXPORT:
                    return new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(Byte.valueOf(SSLHandshake.CLIENT_KEY_EXCHANGE.id), RSAClientKeyExchange.rsaHandshakeConsumer)};
                case DHE:
                case DHE_EXPORT:
                    return new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(Byte.valueOf(SSLHandshake.CLIENT_KEY_EXCHANGE.id), DHClientKeyExchange.dhHandshakeConsumer)};
                case ECDH:
                    return new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(Byte.valueOf(SSLHandshake.CLIENT_KEY_EXCHANGE.id), ECDHClientKeyExchange.ecdhHandshakeConsumer)};
                case ECDHE:
                    return new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(Byte.valueOf(SSLHandshake.CLIENT_KEY_EXCHANGE.id), ECDHClientKeyExchange.ecdheHandshakeConsumer)};
                case KRB5:
                case KRB5_EXPORT:
                    return new Map.Entry[]{new AbstractMap.SimpleImmutableEntry(Byte.valueOf(SSLHandshake.CLIENT_KEY_EXCHANGE.id), KrbClientKeyExchange.krbHandshakeConsumer)};
            }
            return new Map.Entry[0];
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLKeyExchange$T13KeyAgreement.class */
    private static final class T13KeyAgreement implements SSLKeyAgreement {
        private final SupportedGroupsExtension.NamedGroup namedGroup;
        static final Map<SupportedGroupsExtension.NamedGroup, T13KeyAgreement> supportedKeyShares = new HashMap();

        static {
            for (SupportedGroupsExtension.NamedGroup namedGroup : SupportedGroupsExtension.SupportedGroups.supportedNamedGroups) {
                supportedKeyShares.put(namedGroup, new T13KeyAgreement(namedGroup));
            }
        }

        private T13KeyAgreement(SupportedGroupsExtension.NamedGroup namedGroup) {
            this.namedGroup = namedGroup;
        }

        static T13KeyAgreement valueOf(SupportedGroupsExtension.NamedGroup namedGroup) {
            return supportedKeyShares.get(namedGroup);
        }

        @Override // sun.security.ssl.SSLPossessionGenerator
        public SSLPossession createPossession(HandshakeContext handshakeContext) {
            if (this.namedGroup.type == SupportedGroupsExtension.NamedGroupType.NAMED_GROUP_ECDHE) {
                return new ECDHKeyExchange.ECDHEPossession(this.namedGroup, handshakeContext.sslContext.getSecureRandom());
            }
            if (this.namedGroup.type == SupportedGroupsExtension.NamedGroupType.NAMED_GROUP_FFDHE) {
                return new DHKeyExchange.DHEPossession(this.namedGroup, handshakeContext.sslContext.getSecureRandom());
            }
            return null;
        }

        @Override // sun.security.ssl.SSLKeyAgreementGenerator
        public SSLKeyDerivation createKeyDerivation(HandshakeContext handshakeContext) throws IOException {
            if (this.namedGroup.type == SupportedGroupsExtension.NamedGroupType.NAMED_GROUP_ECDHE) {
                return ECDHKeyExchange.ecdheKAGenerator.createKeyDerivation(handshakeContext);
            }
            if (this.namedGroup.type == SupportedGroupsExtension.NamedGroupType.NAMED_GROUP_FFDHE) {
                return DHKeyExchange.kaGenerator.createKeyDerivation(handshakeContext);
            }
            return null;
        }
    }
}
