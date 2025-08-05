package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.SSLHandshakeException;
import sun.security.provider.certpath.OCSPResponse;
import sun.security.ssl.CertStatusExtension;
import sun.security.ssl.CertificateMessage;
import sun.security.ssl.SSLHandshake;
import sun.security.ssl.StatusResponseManager;

/* loaded from: jsse.jar:sun/security/ssl/CertificateStatus.class */
final class CertificateStatus {
    static final SSLConsumer handshakeConsumer = new CertificateStatusConsumer();
    static final HandshakeProducer handshakeProducer = new CertificateStatusProducer();
    static final HandshakeAbsence handshakeAbsence = new CertificateStatusAbsence();

    CertificateStatus() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateStatus$CertificateStatusMessage.class */
    static final class CertificateStatusMessage extends SSLHandshake.HandshakeMessage {
        final CertStatusExtension.CertStatusRequestType statusType;
        int encodedResponsesLen;
        int messageLength;
        final List<byte[]> encodedResponses;

        CertificateStatusMessage(HandshakeContext handshakeContext) {
            super(handshakeContext);
            this.encodedResponsesLen = 0;
            this.messageLength = -1;
            this.encodedResponses = new ArrayList();
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) handshakeContext;
            StatusResponseManager.StaplingParameters staplingParameters = serverHandshakeContext.stapleParams;
            if (staplingParameters == null) {
                throw new IllegalArgumentException("Unexpected null stapling parameters");
            }
            X509Certificate[] x509CertificateArr = (X509Certificate[]) serverHandshakeContext.handshakeSession.getLocalCertificates();
            if (x509CertificateArr == null) {
                throw new IllegalArgumentException("Unexpected null certificate chain");
            }
            this.statusType = staplingParameters.statReqType;
            if (this.statusType == CertStatusExtension.CertStatusRequestType.OCSP) {
                byte[] bArr = staplingParameters.responseMap.get(x509CertificateArr[0]);
                bArr = bArr == null ? new byte[0] : bArr;
                this.encodedResponses.add(bArr);
                this.encodedResponsesLen += bArr.length + 3;
            } else {
                if (this.statusType != CertStatusExtension.CertStatusRequestType.OCSP_MULTI) {
                    throw new IllegalArgumentException("Unsupported StatusResponseType: " + ((Object) this.statusType));
                }
                for (X509Certificate x509Certificate : x509CertificateArr) {
                    byte[] bArr2 = staplingParameters.responseMap.get(x509Certificate);
                    if (bArr2 == null) {
                        bArr2 = new byte[0];
                    }
                    this.encodedResponses.add(bArr2);
                    this.encodedResponsesLen += bArr2.length + 3;
                }
            }
            this.messageLength = messageLength();
        }

        CertificateStatusMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            super(handshakeContext);
            this.encodedResponsesLen = 0;
            this.messageLength = -1;
            this.encodedResponses = new ArrayList();
            this.statusType = CertStatusExtension.CertStatusRequestType.valueOf((byte) Record.getInt8(byteBuffer));
            if (this.statusType == CertStatusExtension.CertStatusRequestType.OCSP) {
                byte[] bytes24 = Record.getBytes24(byteBuffer);
                if (bytes24.length > 0) {
                    this.encodedResponses.add(bytes24);
                    this.encodedResponsesLen = 3 + bytes24.length;
                } else {
                    throw handshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Zero-length OCSP Response");
                }
            } else if (this.statusType == CertStatusExtension.CertStatusRequestType.OCSP_MULTI) {
                int int24 = Record.getInt24(byteBuffer);
                this.encodedResponsesLen = int24;
                while (int24 > 0) {
                    byte[] bytes242 = Record.getBytes24(byteBuffer);
                    this.encodedResponses.add(bytes242);
                    int24 -= bytes242.length + 3;
                }
                if (int24 != 0) {
                    throw handshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Bad OCSP response list length");
                }
            } else {
                throw handshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Unsupported StatusResponseType: " + ((Object) this.statusType));
            }
            this.messageLength = messageLength();
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public SSLHandshake handshakeType() {
            return SSLHandshake.CERTIFICATE_STATUS;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public int messageLength() {
            int i2 = 1;
            if (this.messageLength == -1) {
                if (this.statusType == CertStatusExtension.CertStatusRequestType.OCSP) {
                    i2 = 1 + this.encodedResponsesLen;
                } else if (this.statusType == CertStatusExtension.CertStatusRequestType.OCSP_MULTI) {
                    i2 = 1 + 3 + this.encodedResponsesLen;
                }
                this.messageLength = i2;
            }
            return this.messageLength;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public void send(HandshakeOutStream handshakeOutStream) throws IOException {
            handshakeOutStream.putInt8(this.statusType.id);
            if (this.statusType == CertStatusExtension.CertStatusRequestType.OCSP) {
                handshakeOutStream.putBytes24(this.encodedResponses.get(0));
                return;
            }
            if (this.statusType == CertStatusExtension.CertStatusRequestType.OCSP_MULTI) {
                handshakeOutStream.putInt24(this.encodedResponsesLen);
                for (byte[] bArr : this.encodedResponses) {
                    if (bArr != null) {
                        handshakeOutStream.putBytes24(bArr);
                    } else {
                        handshakeOutStream.putBytes24(null);
                    }
                }
                return;
            }
            throw new SSLHandshakeException("Unsupported status_type: " + ((int) this.statusType.id));
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (byte[] bArr : this.encodedResponses) {
                if (bArr.length > 0) {
                    try {
                        sb.append(new OCSPResponse(bArr).toString()).append("\n");
                    } catch (IOException e2) {
                        sb.append("OCSP Response Exception: ").append((Object) e2).append("\n");
                    }
                } else {
                    sb.append("<Zero-length entry>\n");
                }
            }
            return new MessageFormat("\"CertificateStatus\": '{'\n  \"type\"                : \"{0}\",\n  \"responses \"          : [\n{1}\n  ]\n'}'", Locale.ENGLISH).format(new Object[]{this.statusType.name, Utilities.indent(Utilities.indent(sb.toString()))});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateStatus$CertificateStatusConsumer.class */
    private static final class CertificateStatusConsumer implements SSLConsumer {
        private CertificateStatusConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException, CertificateException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            CertificateStatusMessage certificateStatusMessage = new CertificateStatusMessage(clientHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming server CertificateStatus handshake message", certificateStatusMessage);
            }
            clientHandshakeContext.handshakeSession.setStatusResponses(certificateStatusMessage.encodedResponses);
            CertificateMessage.T12CertificateConsumer.checkServerCerts(clientHandshakeContext, clientHandshakeContext.deferredCerts);
            clientHandshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.CERTIFICATE_STATUS.id));
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateStatus$CertificateStatusProducer.class */
    private static final class CertificateStatusProducer implements HandshakeProducer {
        private CertificateStatusProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.staplingActive) {
                return null;
            }
            CertificateStatusMessage certificateStatusMessage = new CertificateStatusMessage(serverHandshakeContext);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced server CertificateStatus handshake message", certificateStatusMessage);
            }
            certificateStatusMessage.write(serverHandshakeContext.handshakeOutput);
            serverHandshakeContext.handshakeOutput.flush();
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateStatus$CertificateStatusAbsence.class */
    private static final class CertificateStatusAbsence implements HandshakeAbsence {
        private CertificateStatusAbsence() {
        }

        @Override // sun.security.ssl.HandshakeAbsence
        public void absent(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException, CertificateException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (clientHandshakeContext.staplingActive) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Server did not send CertificateStatus, checking cert chain without status info.", new Object[0]);
                }
                CertificateMessage.T12CertificateConsumer.checkServerCerts(clientHandshakeContext, clientHandshakeContext.deferredCerts);
            }
        }
    }
}
