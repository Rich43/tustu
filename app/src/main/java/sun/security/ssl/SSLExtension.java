package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Locale;
import sun.misc.HexDumpEncoder;
import sun.security.ssl.SSLHandshake;

/* loaded from: jsse.jar:sun/security/ssl/SSLExtension.class */
enum SSLExtension implements SSLStringizer {
    CH_SERVER_NAME(0, "server_name", SSLHandshake.CLIENT_HELLO, ProtocolVersion.PROTOCOLS_TO_13, ServerNameExtension.chNetworkProducer, ServerNameExtension.chOnLoadConsumer, null, null, null, ServerNameExtension.chStringizer),
    SH_SERVER_NAME(0, "server_name", SSLHandshake.SERVER_HELLO, ProtocolVersion.PROTOCOLS_TO_12, ServerNameExtension.shNetworkProducer, ServerNameExtension.shOnLoadConsumer, null, null, null, ServerNameExtension.shStringizer),
    EE_SERVER_NAME(0, "server_name", SSLHandshake.ENCRYPTED_EXTENSIONS, ProtocolVersion.PROTOCOLS_OF_13, ServerNameExtension.eeNetworkProducer, ServerNameExtension.eeOnLoadConsumer, null, null, null, ServerNameExtension.shStringizer),
    CH_MAX_FRAGMENT_LENGTH(1, "max_fragment_length", SSLHandshake.CLIENT_HELLO, ProtocolVersion.PROTOCOLS_TO_13, MaxFragExtension.chNetworkProducer, MaxFragExtension.chOnLoadConsumer, null, null, null, MaxFragExtension.maxFragLenStringizer),
    SH_MAX_FRAGMENT_LENGTH(1, "max_fragment_length", SSLHandshake.SERVER_HELLO, ProtocolVersion.PROTOCOLS_TO_12, MaxFragExtension.shNetworkProducer, MaxFragExtension.shOnLoadConsumer, null, MaxFragExtension.shOnTradeConsumer, null, MaxFragExtension.maxFragLenStringizer),
    EE_MAX_FRAGMENT_LENGTH(1, "max_fragment_length", SSLHandshake.ENCRYPTED_EXTENSIONS, ProtocolVersion.PROTOCOLS_OF_13, MaxFragExtension.eeNetworkProducer, MaxFragExtension.eeOnLoadConsumer, null, MaxFragExtension.eeOnTradeConsumer, null, MaxFragExtension.maxFragLenStringizer),
    CLIENT_CERTIFICATE_URL(2, "client_certificate_url"),
    TRUSTED_CA_KEYS(3, "trusted_ca_keys"),
    TRUNCATED_HMAC(4, "truncated_hmac"),
    CH_STATUS_REQUEST(5, "status_request", SSLHandshake.CLIENT_HELLO, ProtocolVersion.PROTOCOLS_TO_13, CertStatusExtension.chNetworkProducer, CertStatusExtension.chOnLoadConsumer, null, null, null, CertStatusExtension.certStatusReqStringizer),
    SH_STATUS_REQUEST(5, "status_request", SSLHandshake.SERVER_HELLO, ProtocolVersion.PROTOCOLS_TO_12, CertStatusExtension.shNetworkProducer, CertStatusExtension.shOnLoadConsumer, null, null, null, CertStatusExtension.certStatusReqStringizer),
    CR_STATUS_REQUEST(5, "status_request"),
    CT_STATUS_REQUEST(5, "status_request", SSLHandshake.CERTIFICATE, ProtocolVersion.PROTOCOLS_OF_13, CertStatusExtension.ctNetworkProducer, CertStatusExtension.ctOnLoadConsumer, null, null, null, CertStatusExtension.certStatusRespStringizer),
    USER_MAPPING(6, "user_mapping"),
    CLIENT_AUTHZ(7, "client_authz"),
    SERVER_AUTHZ(8, "server_authz"),
    CERT_TYPE(9, "cert_type"),
    CH_SUPPORTED_GROUPS(10, "supported_groups", SSLHandshake.CLIENT_HELLO, ProtocolVersion.PROTOCOLS_TO_13, SupportedGroupsExtension.chNetworkProducer, SupportedGroupsExtension.chOnLoadConsumer, null, null, null, SupportedGroupsExtension.sgsStringizer),
    EE_SUPPORTED_GROUPS(10, "supported_groups", SSLHandshake.ENCRYPTED_EXTENSIONS, ProtocolVersion.PROTOCOLS_OF_13, SupportedGroupsExtension.eeNetworkProducer, SupportedGroupsExtension.eeOnLoadConsumer, null, null, null, SupportedGroupsExtension.sgsStringizer),
    CH_EC_POINT_FORMATS(11, "ec_point_formats", SSLHandshake.CLIENT_HELLO, ProtocolVersion.PROTOCOLS_TO_12, ECPointFormatsExtension.chNetworkProducer, ECPointFormatsExtension.chOnLoadConsumer, null, null, null, ECPointFormatsExtension.epfStringizer),
    SH_EC_POINT_FORMATS(11, "ec_point_formats", SSLHandshake.SERVER_HELLO, ProtocolVersion.PROTOCOLS_TO_12, null, ECPointFormatsExtension.shOnLoadConsumer, null, null, null, ECPointFormatsExtension.epfStringizer),
    SRP(12, "srp"),
    CH_SIGNATURE_ALGORITHMS(13, "signature_algorithms", SSLHandshake.CLIENT_HELLO, ProtocolVersion.PROTOCOLS_12_13, SignatureAlgorithmsExtension.chNetworkProducer, SignatureAlgorithmsExtension.chOnLoadConsumer, SignatureAlgorithmsExtension.chOnLoadAbsence, SignatureAlgorithmsExtension.chOnTradeConsumer, SignatureAlgorithmsExtension.chOnTradeAbsence, SignatureAlgorithmsExtension.ssStringizer),
    CR_SIGNATURE_ALGORITHMS(13, "signature_algorithms", SSLHandshake.CERTIFICATE_REQUEST, ProtocolVersion.PROTOCOLS_OF_13, SignatureAlgorithmsExtension.crNetworkProducer, SignatureAlgorithmsExtension.crOnLoadConsumer, SignatureAlgorithmsExtension.crOnLoadAbsence, SignatureAlgorithmsExtension.crOnTradeConsumer, null, SignatureAlgorithmsExtension.ssStringizer),
    CH_SIGNATURE_ALGORITHMS_CERT(50, "signature_algorithms_cert", SSLHandshake.CLIENT_HELLO, ProtocolVersion.PROTOCOLS_12_13, CertSignAlgsExtension.chNetworkProducer, CertSignAlgsExtension.chOnLoadConsumer, null, CertSignAlgsExtension.chOnTradeConsumer, null, CertSignAlgsExtension.ssStringizer),
    CR_SIGNATURE_ALGORITHMS_CERT(50, "signature_algorithms_cert", SSLHandshake.CERTIFICATE_REQUEST, ProtocolVersion.PROTOCOLS_OF_13, CertSignAlgsExtension.crNetworkProducer, CertSignAlgsExtension.crOnLoadConsumer, null, CertSignAlgsExtension.crOnTradeConsumer, null, CertSignAlgsExtension.ssStringizer),
    USE_SRTP(14, "use_srtp"),
    HEARTBEAT(14, "heartbeat"),
    CH_ALPN(16, "application_layer_protocol_negotiation", SSLHandshake.CLIENT_HELLO, ProtocolVersion.PROTOCOLS_TO_13, AlpnExtension.chNetworkProducer, AlpnExtension.chOnLoadConsumer, AlpnExtension.chOnLoadAbsence, null, null, AlpnExtension.alpnStringizer),
    SH_ALPN(16, "application_layer_protocol_negotiation", SSLHandshake.SERVER_HELLO, ProtocolVersion.PROTOCOLS_TO_12, AlpnExtension.shNetworkProducer, AlpnExtension.shOnLoadConsumer, AlpnExtension.shOnLoadAbsence, null, null, AlpnExtension.alpnStringizer),
    EE_ALPN(16, "application_layer_protocol_negotiation", SSLHandshake.ENCRYPTED_EXTENSIONS, ProtocolVersion.PROTOCOLS_OF_13, AlpnExtension.shNetworkProducer, AlpnExtension.shOnLoadConsumer, AlpnExtension.shOnLoadAbsence, null, null, AlpnExtension.alpnStringizer),
    CH_STATUS_REQUEST_V2(17, "status_request_v2", SSLHandshake.CLIENT_HELLO, ProtocolVersion.PROTOCOLS_TO_12, CertStatusExtension.chV2NetworkProducer, CertStatusExtension.chV2OnLoadConsumer, null, null, null, CertStatusExtension.certStatusReqV2Stringizer),
    SH_STATUS_REQUEST_V2(17, "status_request_v2", SSLHandshake.SERVER_HELLO, ProtocolVersion.PROTOCOLS_TO_12, CertStatusExtension.shV2NetworkProducer, CertStatusExtension.shV2OnLoadConsumer, null, null, null, CertStatusExtension.certStatusReqV2Stringizer),
    SIGNED_CERT_TIMESTAMP(18, "signed_certificate_timestamp"),
    CLIENT_CERT_TYPE(19, "padding"),
    SERVER_CERT_TYPE(20, "server_certificate_type"),
    PADDING(21, "client_certificate_type"),
    ENCRYPT_THEN_MAC(22, "encrypt_then_mac"),
    CH_EXTENDED_MASTER_SECRET(23, "extended_master_secret", SSLHandshake.CLIENT_HELLO, ProtocolVersion.PROTOCOLS_TO_12, ExtendedMasterSecretExtension.chNetworkProducer, ExtendedMasterSecretExtension.chOnLoadConsumer, ExtendedMasterSecretExtension.chOnLoadAbsence, null, null, ExtendedMasterSecretExtension.emsStringizer),
    SH_EXTENDED_MASTER_SECRET(23, "extended_master_secret", SSLHandshake.SERVER_HELLO, ProtocolVersion.PROTOCOLS_TO_12, ExtendedMasterSecretExtension.shNetworkProducer, ExtendedMasterSecretExtension.shOnLoadConsumer, ExtendedMasterSecretExtension.shOnLoadAbsence, null, null, ExtendedMasterSecretExtension.emsStringizer),
    TOKEN_BINDING(24, "token_binding "),
    CACHED_INFO(25, "cached_info"),
    SESSION_TICKET(35, "session_ticket"),
    CH_EARLY_DATA(42, "early_data"),
    EE_EARLY_DATA(42, "early_data"),
    NST_EARLY_DATA(42, "early_data"),
    CH_SUPPORTED_VERSIONS(43, "supported_versions", SSLHandshake.CLIENT_HELLO, ProtocolVersion.PROTOCOLS_TO_13, SupportedVersionsExtension.chNetworkProducer, SupportedVersionsExtension.chOnLoadConsumer, null, null, null, SupportedVersionsExtension.chStringizer),
    SH_SUPPORTED_VERSIONS(43, "supported_versions", SSLHandshake.SERVER_HELLO, ProtocolVersion.PROTOCOLS_OF_13, SupportedVersionsExtension.shNetworkProducer, SupportedVersionsExtension.shOnLoadConsumer, null, null, null, SupportedVersionsExtension.shStringizer),
    HRR_SUPPORTED_VERSIONS(43, "supported_versions", SSLHandshake.HELLO_RETRY_REQUEST, ProtocolVersion.PROTOCOLS_OF_13, SupportedVersionsExtension.hrrNetworkProducer, SupportedVersionsExtension.hrrOnLoadConsumer, null, null, null, SupportedVersionsExtension.hrrStringizer),
    MH_SUPPORTED_VERSIONS(43, "supported_versions", SSLHandshake.MESSAGE_HASH, ProtocolVersion.PROTOCOLS_OF_13, SupportedVersionsExtension.hrrReproducer, null, null, null, null, SupportedVersionsExtension.hrrStringizer),
    CH_COOKIE(44, "cookie", SSLHandshake.CLIENT_HELLO, ProtocolVersion.PROTOCOLS_OF_13, CookieExtension.chNetworkProducer, CookieExtension.chOnLoadConsumer, null, CookieExtension.chOnTradeConsumer, null, CookieExtension.cookieStringizer),
    HRR_COOKIE(44, "cookie", SSLHandshake.HELLO_RETRY_REQUEST, ProtocolVersion.PROTOCOLS_OF_13, CookieExtension.hrrNetworkProducer, CookieExtension.hrrOnLoadConsumer, null, null, null, CookieExtension.cookieStringizer),
    MH_COOKIE(44, "cookie", SSLHandshake.MESSAGE_HASH, ProtocolVersion.PROTOCOLS_OF_13, CookieExtension.hrrNetworkReproducer, null, null, null, null, CookieExtension.cookieStringizer),
    PSK_KEY_EXCHANGE_MODES(45, "psk_key_exchange_modes", SSLHandshake.CLIENT_HELLO, ProtocolVersion.PROTOCOLS_OF_13, PskKeyExchangeModesExtension.chNetworkProducer, PskKeyExchangeModesExtension.chOnLoadConsumer, PskKeyExchangeModesExtension.chOnLoadAbsence, null, PskKeyExchangeModesExtension.chOnTradeAbsence, PskKeyExchangeModesExtension.pkemStringizer),
    CH_CERTIFICATE_AUTHORITIES(47, "certificate_authorities", SSLHandshake.CLIENT_HELLO, ProtocolVersion.PROTOCOLS_OF_13, CertificateAuthoritiesExtension.chNetworkProducer, CertificateAuthoritiesExtension.chOnLoadConsumer, null, null, null, CertificateAuthoritiesExtension.ssStringizer),
    CR_CERTIFICATE_AUTHORITIES(47, "certificate_authorities", SSLHandshake.CERTIFICATE_REQUEST, ProtocolVersion.PROTOCOLS_OF_13, CertificateAuthoritiesExtension.crNetworkProducer, CertificateAuthoritiesExtension.crOnLoadConsumer, null, null, null, CertificateAuthoritiesExtension.ssStringizer),
    OID_FILTERS(48, "oid_filters"),
    POST_HANDSHAKE_AUTH(48, "post_handshake_auth"),
    CH_KEY_SHARE(51, "key_share", SSLHandshake.CLIENT_HELLO, ProtocolVersion.PROTOCOLS_OF_13, KeyShareExtension.chNetworkProducer, KeyShareExtension.chOnLoadConsumer, null, null, null, KeyShareExtension.chStringizer),
    SH_KEY_SHARE(51, "key_share", SSLHandshake.SERVER_HELLO, ProtocolVersion.PROTOCOLS_OF_13, KeyShareExtension.shNetworkProducer, KeyShareExtension.shOnLoadConsumer, KeyShareExtension.shOnLoadAbsence, null, null, KeyShareExtension.shStringizer),
    HRR_KEY_SHARE(51, "key_share", SSLHandshake.HELLO_RETRY_REQUEST, ProtocolVersion.PROTOCOLS_OF_13, KeyShareExtension.hrrNetworkProducer, KeyShareExtension.hrrOnLoadConsumer, null, null, null, KeyShareExtension.hrrStringizer),
    MH_KEY_SHARE(51, "key_share", SSLHandshake.MESSAGE_HASH, ProtocolVersion.PROTOCOLS_OF_13, KeyShareExtension.hrrNetworkReproducer, null, null, null, null, KeyShareExtension.hrrStringizer),
    CH_RENEGOTIATION_INFO(65281, "renegotiation_info", SSLHandshake.CLIENT_HELLO, ProtocolVersion.PROTOCOLS_TO_12, RenegoInfoExtension.chNetworkProducer, RenegoInfoExtension.chOnLoadConsumer, RenegoInfoExtension.chOnLoadAbsence, null, null, RenegoInfoExtension.rniStringizer),
    SH_RENEGOTIATION_INFO(65281, "renegotiation_info", SSLHandshake.SERVER_HELLO, ProtocolVersion.PROTOCOLS_TO_12, RenegoInfoExtension.shNetworkProducer, RenegoInfoExtension.shOnLoadConsumer, RenegoInfoExtension.shOnLoadAbsence, null, null, RenegoInfoExtension.rniStringizer),
    CH_PRE_SHARED_KEY(41, "pre_shared_key", SSLHandshake.CLIENT_HELLO, ProtocolVersion.PROTOCOLS_OF_13, PreSharedKeyExtension.chNetworkProducer, PreSharedKeyExtension.chOnLoadConsumer, PreSharedKeyExtension.chOnLoadAbsence, PreSharedKeyExtension.chOnTradeConsumer, null, PreSharedKeyExtension.chStringizer),
    SH_PRE_SHARED_KEY(41, "pre_shared_key", SSLHandshake.SERVER_HELLO, ProtocolVersion.PROTOCOLS_OF_13, PreSharedKeyExtension.shNetworkProducer, PreSharedKeyExtension.shOnLoadConsumer, PreSharedKeyExtension.shOnLoadAbsence, null, null, PreSharedKeyExtension.shStringizer);

    final int id;
    final SSLHandshake handshakeType;
    final String name;
    final ProtocolVersion[] supportedProtocols;
    final HandshakeProducer networkProducer;
    final ExtensionConsumer onLoadConsumer;
    final HandshakeAbsence onLoadAbsence;
    final HandshakeConsumer onTradeConsumer;
    final HandshakeAbsence onTradeAbsence;
    final SSLStringizer stringizer;

    /* loaded from: jsse.jar:sun/security/ssl/SSLExtension$ExtensionConsumer.class */
    interface ExtensionConsumer {
        void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException;
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLExtension$SSLExtensionSpec.class */
    interface SSLExtensionSpec {
    }

    SSLExtension(int i2, String str) {
        this.id = i2;
        this.handshakeType = SSLHandshake.NOT_APPLICABLE;
        this.name = str;
        this.supportedProtocols = new ProtocolVersion[0];
        this.networkProducer = null;
        this.onLoadConsumer = null;
        this.onLoadAbsence = null;
        this.onTradeConsumer = null;
        this.onTradeAbsence = null;
        this.stringizer = null;
    }

    SSLExtension(int i2, String str, SSLHandshake sSLHandshake, ProtocolVersion[] protocolVersionArr, HandshakeProducer handshakeProducer, ExtensionConsumer extensionConsumer, HandshakeAbsence handshakeAbsence, HandshakeConsumer handshakeConsumer, HandshakeAbsence handshakeAbsence2, SSLStringizer sSLStringizer) {
        this.id = i2;
        this.handshakeType = sSLHandshake;
        this.name = str;
        this.supportedProtocols = protocolVersionArr;
        this.networkProducer = handshakeProducer;
        this.onLoadConsumer = extensionConsumer;
        this.onLoadAbsence = handshakeAbsence;
        this.onTradeConsumer = handshakeConsumer;
        this.onTradeAbsence = handshakeAbsence2;
        this.stringizer = sSLStringizer;
    }

    static SSLExtension valueOf(SSLHandshake sSLHandshake, int i2) {
        for (SSLExtension sSLExtension : values()) {
            if (sSLExtension.id == i2 && sSLExtension.handshakeType == sSLHandshake) {
                return sSLExtension;
            }
        }
        return null;
    }

    static String nameOf(int i2) {
        for (SSLExtension sSLExtension : values()) {
            if (sSLExtension.id == i2) {
                return sSLExtension.name;
            }
        }
        return "unknown extension";
    }

    static boolean isConsumable(int i2) {
        for (SSLExtension sSLExtension : values()) {
            if (sSLExtension.id == i2 && sSLExtension.onLoadConsumer != null) {
                return true;
            }
        }
        return false;
    }

    public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
        if (this.networkProducer != null) {
            return this.networkProducer.produce(connectionContext, handshakeMessage);
        }
        throw new UnsupportedOperationException("Not yet supported extension producing.");
    }

    public void consumeOnLoad(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
        if (this.onLoadConsumer != null) {
            this.onLoadConsumer.consume(connectionContext, handshakeMessage, byteBuffer);
            return;
        }
        throw new UnsupportedOperationException("Not yet supported extension loading.");
    }

    public void consumeOnTrade(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
        if (this.onTradeConsumer != null) {
            this.onTradeConsumer.consume(connectionContext, handshakeMessage);
            return;
        }
        throw new UnsupportedOperationException("Not yet supported extension processing.");
    }

    void absentOnLoad(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
        if (this.onLoadAbsence != null) {
            this.onLoadAbsence.absent(connectionContext, handshakeMessage);
            return;
        }
        throw new UnsupportedOperationException("Not yet supported extension absence processing.");
    }

    void absentOnTrade(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
        if (this.onTradeAbsence != null) {
            this.onTradeAbsence.absent(connectionContext, handshakeMessage);
            return;
        }
        throw new UnsupportedOperationException("Not yet supported extension absence processing.");
    }

    public boolean isAvailable(ProtocolVersion protocolVersion) {
        for (int i2 = 0; i2 < this.supportedProtocols.length; i2++) {
            if (this.supportedProtocols[i2] == protocolVersion) {
                return true;
            }
        }
        return false;
    }

    @Override // java.lang.Enum
    public String toString() {
        return this.name;
    }

    @Override // sun.security.ssl.SSLStringizer
    public String toString(ByteBuffer byteBuffer) {
        String string;
        MessageFormat messageFormat = new MessageFormat("\"{0} ({1})\": '{'\n{2}\n'}'", Locale.ENGLISH);
        if (this.stringizer == null) {
            string = new HexDumpEncoder().encode(byteBuffer.duplicate());
        } else {
            string = this.stringizer.toString(byteBuffer);
        }
        return messageFormat.format(new Object[]{this.name, Integer.valueOf(this.id), Utilities.indent(string)});
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLExtension$ClientExtensions.class */
    static final class ClientExtensions {
        static final Collection<SSLExtension> defaults;

        ClientExtensions() {
        }

        static {
            LinkedList linkedList = new LinkedList();
            for (SSLExtension sSLExtension : SSLExtension.values()) {
                if (sSLExtension.handshakeType != SSLHandshake.NOT_APPLICABLE) {
                    linkedList.add(sSLExtension);
                }
            }
            if (!Utilities.getBooleanProperty("jsse.enableSNIExtension", true)) {
                linkedList.remove(SSLExtension.CH_SERVER_NAME);
            }
            if (!(Utilities.getBooleanProperty("jsse.enableMFLNExtension", false) || Utilities.getBooleanProperty("jsse.enableMFLExtension", false))) {
                linkedList.remove(SSLExtension.CH_MAX_FRAGMENT_LENGTH);
            }
            if (!Utilities.getBooleanProperty("jdk.tls.client.enableCAExtension", false)) {
                linkedList.remove(SSLExtension.CH_CERTIFICATE_AUTHORITIES);
            }
            defaults = Collections.unmodifiableCollection(linkedList);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLExtension$ServerExtensions.class */
    static final class ServerExtensions {
        static final Collection<SSLExtension> defaults;

        ServerExtensions() {
        }

        static {
            LinkedList linkedList = new LinkedList();
            for (SSLExtension sSLExtension : SSLExtension.values()) {
                if (sSLExtension.handshakeType != SSLHandshake.NOT_APPLICABLE) {
                    linkedList.add(sSLExtension);
                }
            }
            defaults = Collections.unmodifiableCollection(linkedList);
        }
    }
}
