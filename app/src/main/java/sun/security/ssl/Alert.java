package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.Locale;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLProtocolException;

/* loaded from: jsse.jar:sun/security/ssl/Alert.class */
enum Alert {
    CLOSE_NOTIFY((byte) 0, "close_notify", false),
    UNEXPECTED_MESSAGE((byte) 10, "unexpected_message", false),
    BAD_RECORD_MAC((byte) 20, "bad_record_mac", false),
    DECRYPTION_FAILED((byte) 21, "decryption_failed", false),
    RECORD_OVERFLOW((byte) 22, "record_overflow", false),
    DECOMPRESSION_FAILURE((byte) 30, "decompression_failure", false),
    HANDSHAKE_FAILURE((byte) 40, "handshake_failure", true),
    NO_CERTIFICATE((byte) 41, "no_certificate", true),
    BAD_CERTIFICATE((byte) 42, "bad_certificate", true),
    UNSUPPORTED_CERTIFICATE((byte) 43, "unsupported_certificate", true),
    CERTIFICATE_REVOKED((byte) 44, "certificate_revoked", true),
    CERTIFICATE_EXPIRED((byte) 45, "certificate_expired", true),
    CERTIFICATE_UNKNOWN((byte) 46, "certificate_unknown", true),
    ILLEGAL_PARAMETER((byte) 47, "illegal_parameter", true),
    UNKNOWN_CA((byte) 48, "unknown_ca", true),
    ACCESS_DENIED((byte) 49, "access_denied", true),
    DECODE_ERROR((byte) 50, "decode_error", true),
    DECRYPT_ERROR((byte) 51, "decrypt_error", true),
    EXPORT_RESTRICTION((byte) 60, "export_restriction", true),
    PROTOCOL_VERSION((byte) 70, "protocol_version", true),
    INSUFFICIENT_SECURITY((byte) 71, "insufficient_security", true),
    INTERNAL_ERROR((byte) 80, "internal_error", false),
    INAPPROPRIATE_FALLBACK((byte) 86, "inappropriate_fallback", false),
    USER_CANCELED((byte) 90, "user_canceled", false),
    NO_RENEGOTIATION((byte) 100, "no_renegotiation", true),
    MISSING_EXTENSION((byte) 109, "missing_extension", true),
    UNSUPPORTED_EXTENSION((byte) 110, "unsupported_extension", true),
    CERT_UNOBTAINABLE((byte) 111, "certificate_unobtainable", true),
    UNRECOGNIZED_NAME((byte) 112, "unrecognized_name", true),
    BAD_CERT_STATUS_RESPONSE((byte) 113, "bad_certificate_status_response", true),
    BAD_CERT_HASH_VALUE((byte) 114, "bad_certificate_hash_value", true),
    UNKNOWN_PSK_IDENTITY((byte) 115, "unknown_psk_identity", true),
    CERTIFICATE_REQUIRED((byte) 116, "certificate_required", true),
    NO_APPLICATION_PROTOCOL((byte) 120, "no_application_protocol", true);

    final byte id;
    final String description;
    final boolean handshakeOnly;
    static final SSLConsumer alertConsumer = new AlertConsumer();

    Alert(byte b2, String str, boolean z2) {
        this.id = b2;
        this.description = str;
        this.handshakeOnly = z2;
    }

    static Alert valueOf(byte b2) {
        for (Alert alert : values()) {
            if (alert.id == b2) {
                return alert;
            }
        }
        return null;
    }

    static String nameOf(byte b2) {
        for (Alert alert : values()) {
            if (alert.id == b2) {
                return alert.description;
            }
        }
        return "UNKNOWN ALERT (" + (b2 & 255) + ")";
    }

    SSLException createSSLException(String str) {
        return createSSLException(str, null);
    }

    SSLException createSSLException(String str, Throwable th) {
        SSLException sSLException;
        if (str == null) {
            str = th != null ? th.getMessage() : "";
        }
        if (th != null && (th instanceof IOException)) {
            sSLException = new SSLException(str);
        } else if (this == UNEXPECTED_MESSAGE) {
            sSLException = new SSLProtocolException(str);
        } else if (this.handshakeOnly) {
            sSLException = new SSLHandshakeException(str);
        } else {
            sSLException = new SSLException(str);
        }
        if (th != null) {
            sSLException.initCause(th);
        }
        return sSLException;
    }

    /* loaded from: jsse.jar:sun/security/ssl/Alert$Level.class */
    enum Level {
        WARNING((byte) 1, "warning"),
        FATAL((byte) 2, "fatal");

        final byte level;
        final String description;

        Level(byte b2, String str) {
            this.level = b2;
            this.description = str;
        }

        static Level valueOf(byte b2) {
            for (Level level : values()) {
                if (level.level == b2) {
                    return level;
                }
            }
            return null;
        }

        static String nameOf(byte b2) {
            for (Level level : values()) {
                if (level.level == b2) {
                    return level.description;
                }
            }
            return "UNKNOWN ALERT LEVEL (" + (b2 & 255) + ")";
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/Alert$AlertMessage.class */
    private static final class AlertMessage {
        private final byte level;
        private final byte id;

        AlertMessage(TransportContext transportContext, ByteBuffer byteBuffer) throws IOException {
            if (byteBuffer.remaining() != 2) {
                throw transportContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid Alert message: no sufficient data");
            }
            this.level = byteBuffer.get();
            this.id = byteBuffer.get();
        }

        public String toString() {
            return new MessageFormat("\"Alert\": '{'\n  \"level\"      : \"{0}\",\n  \"description\": \"{1}\"\n'}'", Locale.ENGLISH).format(new Object[]{Level.nameOf(this.level), Alert.nameOf(this.id)});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/Alert$AlertConsumer.class */
    private static final class AlertConsumer implements SSLConsumer {
        private AlertConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            String str;
            TransportContext transportContext = (TransportContext) connectionContext;
            AlertMessage alertMessage = new AlertMessage(transportContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                SSLLogger.fine("Received alert message", alertMessage);
            }
            Level levelValueOf = Level.valueOf(alertMessage.level);
            Alert alertValueOf = Alert.valueOf(alertMessage.id);
            if (alertValueOf == Alert.CLOSE_NOTIFY) {
                transportContext.isInputCloseNotified = true;
                transportContext.closeInbound();
                if (transportContext.peerUserCanceled) {
                    transportContext.closeOutbound();
                    return;
                } else {
                    if (transportContext.handshakeContext != null) {
                        throw transportContext.fatal(Alert.UNEXPECTED_MESSAGE, "Received close_notify during handshake");
                    }
                    return;
                }
            }
            if (alertValueOf == Alert.USER_CANCELED) {
                if (levelValueOf == Level.WARNING) {
                    transportContext.peerUserCanceled = true;
                    return;
                }
                throw transportContext.fatal(alertValueOf, "Received fatal close_notify alert", true, null);
            }
            if (levelValueOf == Level.WARNING && alertValueOf != null) {
                if (alertValueOf.handshakeOnly && transportContext.handshakeContext != null) {
                    if (transportContext.sslConfig.isClientMode || alertValueOf != Alert.NO_CERTIFICATE || transportContext.sslConfig.clientAuthType != ClientAuthType.CLIENT_AUTH_REQUESTED) {
                        throw transportContext.fatal(Alert.HANDSHAKE_FAILURE, "received handshake warning: " + alertValueOf.description);
                    }
                    transportContext.handshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.CERTIFICATE.id));
                    transportContext.handshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.CERTIFICATE_VERIFY.id));
                    return;
                }
                return;
            }
            if (alertValueOf == null) {
                alertValueOf = Alert.UNEXPECTED_MESSAGE;
                str = "Unknown alert description (" + ((int) alertMessage.id) + ")";
            } else {
                str = "Received fatal alert: " + alertValueOf.description;
            }
            throw transportContext.fatal(alertValueOf, str, true, null);
        }
    }
}
