package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import sun.security.ssl.SSLExtension;
import sun.security.ssl.SSLHandshake;
import sun.security.ssl.SignatureAlgorithmsExtension;

/* loaded from: jsse.jar:sun/security/ssl/CertSignAlgsExtension.class */
final class CertSignAlgsExtension {
    static final HandshakeProducer chNetworkProducer = new CHCertSignatureSchemesProducer();
    static final SSLExtension.ExtensionConsumer chOnLoadConsumer = new CHCertSignatureSchemesConsumer();
    static final HandshakeConsumer chOnTradeConsumer = new CHCertSignatureSchemesUpdate();
    static final HandshakeProducer crNetworkProducer = new CRCertSignatureSchemesProducer();
    static final SSLExtension.ExtensionConsumer crOnLoadConsumer = new CRCertSignatureSchemesConsumer();
    static final HandshakeConsumer crOnTradeConsumer = new CRCertSignatureSchemesUpdate();
    static final SSLStringizer ssStringizer = new CertSignatureSchemesStringizer();

    CertSignAlgsExtension() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertSignAlgsExtension$CertSignatureSchemesStringizer.class */
    private static final class CertSignatureSchemesStringizer implements SSLStringizer {
        private CertSignatureSchemesStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new SignatureAlgorithmsExtension.SignatureSchemesSpec(byteBuffer).toString();
            } catch (IOException e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertSignAlgsExtension$CHCertSignatureSchemesProducer.class */
    private static final class CHCertSignatureSchemesProducer implements HandshakeProducer {
        private CHCertSignatureSchemesProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_SIGNATURE_ALGORITHMS_CERT)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable signature_algorithms_cert extension", new Object[0]);
                    return null;
                }
                return null;
            }
            if (clientHandshakeContext.localSupportedSignAlgs == null) {
                clientHandshakeContext.localSupportedSignAlgs = SignatureScheme.getSupportedAlgorithms(clientHandshakeContext.sslConfig, clientHandshakeContext.algorithmConstraints, clientHandshakeContext.activeProtocols);
            }
            int iSizeInRecord = SignatureScheme.sizeInRecord() * clientHandshakeContext.localSupportedSignAlgs.size();
            byte[] bArr = new byte[iSizeInRecord + 2];
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
            Record.putInt16(byteBufferWrap, iSizeInRecord);
            Iterator<SignatureScheme> it = clientHandshakeContext.localSupportedSignAlgs.iterator();
            while (it.hasNext()) {
                Record.putInt16(byteBufferWrap, it.next().id);
            }
            clientHandshakeContext.handshakeExtensions.put(SSLExtension.CH_SIGNATURE_ALGORITHMS_CERT, new SignatureAlgorithmsExtension.SignatureSchemesSpec(clientHandshakeContext.localSupportedSignAlgs));
            return bArr;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertSignAlgsExtension$CHCertSignatureSchemesConsumer.class */
    private static final class CHCertSignatureSchemesConsumer implements SSLExtension.ExtensionConsumer {
        private CHCertSignatureSchemesConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_SIGNATURE_ALGORITHMS_CERT)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable signature_algorithms_cert extension", new Object[0]);
                    return;
                }
                return;
            }
            try {
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.CH_SIGNATURE_ALGORITHMS_CERT, new SignatureAlgorithmsExtension.SignatureSchemesSpec(byteBuffer));
            } catch (IOException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertSignAlgsExtension$CHCertSignatureSchemesUpdate.class */
    private static final class CHCertSignatureSchemesUpdate implements HandshakeConsumer {
        private CHCertSignatureSchemesUpdate() {
        }

        @Override // sun.security.ssl.HandshakeConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            SignatureAlgorithmsExtension.SignatureSchemesSpec signatureSchemesSpec = (SignatureAlgorithmsExtension.SignatureSchemesSpec) serverHandshakeContext.handshakeExtensions.get(SSLExtension.CH_SIGNATURE_ALGORITHMS_CERT);
            if (signatureSchemesSpec == null) {
                return;
            }
            List<SignatureScheme> supportedAlgorithms = SignatureScheme.getSupportedAlgorithms(serverHandshakeContext.sslConfig, serverHandshakeContext.algorithmConstraints, serverHandshakeContext.negotiatedProtocol, signatureSchemesSpec.signatureSchemes);
            serverHandshakeContext.peerRequestedCertSignSchemes = supportedAlgorithms;
            serverHandshakeContext.handshakeSession.setPeerSupportedSignatureAlgorithms(supportedAlgorithms);
            if (!serverHandshakeContext.isResumption && serverHandshakeContext.negotiatedProtocol.useTLS13PlusSpec()) {
                if (serverHandshakeContext.sslConfig.clientAuthType != ClientAuthType.CLIENT_AUTH_NONE) {
                    serverHandshakeContext.handshakeProducers.putIfAbsent(Byte.valueOf(SSLHandshake.CERTIFICATE_REQUEST.id), SSLHandshake.CERTIFICATE_REQUEST);
                }
                serverHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.CERTIFICATE.id), SSLHandshake.CERTIFICATE);
                serverHandshakeContext.handshakeProducers.putIfAbsent(Byte.valueOf(SSLHandshake.CERTIFICATE_VERIFY.id), SSLHandshake.CERTIFICATE_VERIFY);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertSignAlgsExtension$CRCertSignatureSchemesProducer.class */
    private static final class CRCertSignatureSchemesProducer implements HandshakeProducer {
        private CRCertSignatureSchemesProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_SIGNATURE_ALGORITHMS_CERT)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable signature_algorithms_cert extension", new Object[0]);
                    return null;
                }
                return null;
            }
            List<SignatureScheme> supportedAlgorithms = SignatureScheme.getSupportedAlgorithms(serverHandshakeContext.sslConfig, serverHandshakeContext.algorithmConstraints, Collections.unmodifiableList(Arrays.asList(serverHandshakeContext.negotiatedProtocol)));
            int iSizeInRecord = SignatureScheme.sizeInRecord() * supportedAlgorithms.size();
            byte[] bArr = new byte[iSizeInRecord + 2];
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
            Record.putInt16(byteBufferWrap, iSizeInRecord);
            Iterator<SignatureScheme> it = supportedAlgorithms.iterator();
            while (it.hasNext()) {
                Record.putInt16(byteBufferWrap, it.next().id);
            }
            serverHandshakeContext.handshakeExtensions.put(SSLExtension.CR_SIGNATURE_ALGORITHMS_CERT, new SignatureAlgorithmsExtension.SignatureSchemesSpec(serverHandshakeContext.localSupportedSignAlgs));
            return bArr;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertSignAlgsExtension$CRCertSignatureSchemesConsumer.class */
    private static final class CRCertSignatureSchemesConsumer implements SSLExtension.ExtensionConsumer {
        private CRCertSignatureSchemesConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_SIGNATURE_ALGORITHMS_CERT)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable signature_algorithms_cert extension", new Object[0]);
                    return;
                }
                return;
            }
            try {
                clientHandshakeContext.handshakeExtensions.put(SSLExtension.CR_SIGNATURE_ALGORITHMS_CERT, new SignatureAlgorithmsExtension.SignatureSchemesSpec(byteBuffer));
            } catch (IOException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertSignAlgsExtension$CRCertSignatureSchemesUpdate.class */
    private static final class CRCertSignatureSchemesUpdate implements HandshakeConsumer {
        private CRCertSignatureSchemesUpdate() {
        }

        @Override // sun.security.ssl.HandshakeConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            SignatureAlgorithmsExtension.SignatureSchemesSpec signatureSchemesSpec = (SignatureAlgorithmsExtension.SignatureSchemesSpec) clientHandshakeContext.handshakeExtensions.get(SSLExtension.CR_SIGNATURE_ALGORITHMS_CERT);
            if (signatureSchemesSpec == null) {
                return;
            }
            List<SignatureScheme> supportedAlgorithms = SignatureScheme.getSupportedAlgorithms(clientHandshakeContext.sslConfig, clientHandshakeContext.algorithmConstraints, clientHandshakeContext.negotiatedProtocol, signatureSchemesSpec.signatureSchemes);
            clientHandshakeContext.peerRequestedCertSignSchemes = supportedAlgorithms;
            clientHandshakeContext.handshakeSession.setPeerSupportedSignatureAlgorithms(supportedAlgorithms);
        }
    }
}
