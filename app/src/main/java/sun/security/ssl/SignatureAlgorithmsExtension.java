package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.SSLProtocolException;
import sun.security.ssl.SSLExtension;
import sun.security.ssl.SSLHandshake;

/* loaded from: jsse.jar:sun/security/ssl/SignatureAlgorithmsExtension.class */
final class SignatureAlgorithmsExtension {
    static final HandshakeProducer chNetworkProducer = new CHSignatureSchemesProducer();
    static final SSLExtension.ExtensionConsumer chOnLoadConsumer = new CHSignatureSchemesConsumer();
    static final HandshakeAbsence chOnLoadAbsence = new CHSignatureSchemesOnLoadAbsence();
    static final HandshakeConsumer chOnTradeConsumer = new CHSignatureSchemesUpdate();
    static final HandshakeAbsence chOnTradeAbsence = new CHSignatureSchemesOnTradeAbsence();
    static final HandshakeProducer crNetworkProducer = new CRSignatureSchemesProducer();
    static final SSLExtension.ExtensionConsumer crOnLoadConsumer = new CRSignatureSchemesConsumer();
    static final HandshakeAbsence crOnLoadAbsence = new CRSignatureSchemesAbsence();
    static final HandshakeConsumer crOnTradeConsumer = new CRSignatureSchemesUpdate();
    static final SSLStringizer ssStringizer = new SignatureSchemesStringizer();

    SignatureAlgorithmsExtension() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/SignatureAlgorithmsExtension$SignatureSchemesSpec.class */
    static final class SignatureSchemesSpec implements SSLExtension.SSLExtensionSpec {
        final int[] signatureSchemes;

        SignatureSchemesSpec(List<SignatureScheme> list) {
            if (list != null) {
                this.signatureSchemes = new int[list.size()];
                int i2 = 0;
                Iterator<SignatureScheme> it = list.iterator();
                while (it.hasNext()) {
                    int i3 = i2;
                    i2++;
                    this.signatureSchemes[i3] = it.next().id;
                }
                return;
            }
            this.signatureSchemes = new int[0];
        }

        SignatureSchemesSpec(ByteBuffer byteBuffer) throws IOException {
            if (byteBuffer.remaining() < 2) {
                throw new SSLProtocolException("Invalid signature_algorithms: insufficient data");
            }
            byte[] bytes16 = Record.getBytes16(byteBuffer);
            if (byteBuffer.hasRemaining()) {
                throw new SSLProtocolException("Invalid signature_algorithms: unknown extra data");
            }
            if (bytes16 == null || bytes16.length == 0 || (bytes16.length & 1) != 0) {
                throw new SSLProtocolException("Invalid signature_algorithms: incomplete data");
            }
            int[] iArr = new int[bytes16.length / 2];
            int i2 = 0;
            int i3 = 0;
            while (i2 < bytes16.length) {
                int i4 = i2;
                int i5 = i2 + 1;
                byte b2 = bytes16[i4];
                i2 = i5 + 1;
                int i6 = i3;
                i3++;
                iArr[i6] = ((b2 & 255) << 8) | (bytes16[i5] & 255);
            }
            this.signatureSchemes = iArr;
        }

        public String toString() {
            MessageFormat messageFormat = new MessageFormat("\"signature schemes\": '['{0}']'", Locale.ENGLISH);
            if (this.signatureSchemes == null || this.signatureSchemes.length == 0) {
                return messageFormat.format(new Object[]{"<no supported signature schemes specified>"});
            }
            StringBuilder sb = new StringBuilder(512);
            boolean z2 = true;
            for (int i2 : this.signatureSchemes) {
                if (z2) {
                    z2 = false;
                } else {
                    sb.append(", ");
                }
                sb.append(SignatureScheme.nameOf(i2));
            }
            return messageFormat.format(new Object[]{sb.toString()});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SignatureAlgorithmsExtension$SignatureSchemesStringizer.class */
    private static final class SignatureSchemesStringizer implements SSLStringizer {
        private SignatureSchemesStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new SignatureSchemesSpec(byteBuffer).toString();
            } catch (IOException e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SignatureAlgorithmsExtension$CHSignatureSchemesProducer.class */
    private static final class CHSignatureSchemesProducer implements HandshakeProducer {
        private CHSignatureSchemesProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_SIGNATURE_ALGORITHMS)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable signature_algorithms extension", new Object[0]);
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
            clientHandshakeContext.handshakeExtensions.put(SSLExtension.CH_SIGNATURE_ALGORITHMS, new SignatureSchemesSpec(clientHandshakeContext.localSupportedSignAlgs));
            return bArr;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SignatureAlgorithmsExtension$CHSignatureSchemesConsumer.class */
    private static final class CHSignatureSchemesConsumer implements SSLExtension.ExtensionConsumer {
        private CHSignatureSchemesConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_SIGNATURE_ALGORITHMS)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable signature_algorithms extension", new Object[0]);
                    return;
                }
                return;
            }
            try {
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.CH_SIGNATURE_ALGORITHMS, new SignatureSchemesSpec(byteBuffer));
            } catch (IOException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SignatureAlgorithmsExtension$CHSignatureSchemesUpdate.class */
    private static final class CHSignatureSchemesUpdate implements HandshakeConsumer {
        private CHSignatureSchemesUpdate() {
        }

        @Override // sun.security.ssl.HandshakeConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            SignatureSchemesSpec signatureSchemesSpec = (SignatureSchemesSpec) serverHandshakeContext.handshakeExtensions.get(SSLExtension.CH_SIGNATURE_ALGORITHMS);
            if (signatureSchemesSpec == null) {
                return;
            }
            List<SignatureScheme> supportedAlgorithms = SignatureScheme.getSupportedAlgorithms(serverHandshakeContext.sslConfig, serverHandshakeContext.algorithmConstraints, serverHandshakeContext.negotiatedProtocol, signatureSchemesSpec.signatureSchemes);
            serverHandshakeContext.peerRequestedSignatureSchemes = supportedAlgorithms;
            if (((SignatureSchemesSpec) serverHandshakeContext.handshakeExtensions.get(SSLExtension.CH_SIGNATURE_ALGORITHMS_CERT)) == null) {
                serverHandshakeContext.peerRequestedCertSignSchemes = supportedAlgorithms;
                serverHandshakeContext.handshakeSession.setPeerSupportedSignatureAlgorithms(supportedAlgorithms);
            }
            if (!serverHandshakeContext.isResumption && serverHandshakeContext.negotiatedProtocol.useTLS13PlusSpec()) {
                if (serverHandshakeContext.sslConfig.clientAuthType != ClientAuthType.CLIENT_AUTH_NONE) {
                    serverHandshakeContext.handshakeProducers.putIfAbsent(Byte.valueOf(SSLHandshake.CERTIFICATE_REQUEST.id), SSLHandshake.CERTIFICATE_REQUEST);
                }
                serverHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.CERTIFICATE.id), SSLHandshake.CERTIFICATE);
                serverHandshakeContext.handshakeProducers.putIfAbsent(Byte.valueOf(SSLHandshake.CERTIFICATE_VERIFY.id), SSLHandshake.CERTIFICATE_VERIFY);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SignatureAlgorithmsExtension$CHSignatureSchemesOnLoadAbsence.class */
    private static final class CHSignatureSchemesOnLoadAbsence implements HandshakeAbsence {
        private CHSignatureSchemesOnLoadAbsence() {
        }

        @Override // sun.security.ssl.HandshakeAbsence
        public void absent(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (serverHandshakeContext.negotiatedProtocol.useTLS13PlusSpec()) {
                throw serverHandshakeContext.conContext.fatal(Alert.MISSING_EXTENSION, "No mandatory signature_algorithms extension in the received CertificateRequest handshake message");
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SignatureAlgorithmsExtension$CHSignatureSchemesOnTradeAbsence.class */
    private static final class CHSignatureSchemesOnTradeAbsence implements HandshakeAbsence {
        private CHSignatureSchemesOnTradeAbsence() {
        }

        @Override // sun.security.ssl.HandshakeAbsence
        public void absent(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (serverHandshakeContext.negotiatedProtocol.useTLS12PlusSpec()) {
                List<SignatureScheme> listAsList = Arrays.asList(SignatureScheme.RSA_PKCS1_SHA1, SignatureScheme.DSA_SHA1, SignatureScheme.ECDSA_SHA1);
                serverHandshakeContext.peerRequestedSignatureSchemes = listAsList;
                if (serverHandshakeContext.peerRequestedCertSignSchemes == null || serverHandshakeContext.peerRequestedCertSignSchemes.isEmpty()) {
                    serverHandshakeContext.peerRequestedCertSignSchemes = listAsList;
                }
                serverHandshakeContext.handshakeSession.setUseDefaultPeerSignAlgs();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SignatureAlgorithmsExtension$CRSignatureSchemesProducer.class */
    private static final class CRSignatureSchemesProducer implements HandshakeProducer {
        private CRSignatureSchemesProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.CR_SIGNATURE_ALGORITHMS)) {
                throw serverHandshakeContext.conContext.fatal(Alert.MISSING_EXTENSION, "No available signature_algorithms extension for client certificate authentication");
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
            serverHandshakeContext.handshakeExtensions.put(SSLExtension.CR_SIGNATURE_ALGORITHMS, new SignatureSchemesSpec(serverHandshakeContext.localSupportedSignAlgs));
            return bArr;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SignatureAlgorithmsExtension$CRSignatureSchemesConsumer.class */
    private static final class CRSignatureSchemesConsumer implements SSLExtension.ExtensionConsumer {
        private CRSignatureSchemesConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.CR_SIGNATURE_ALGORITHMS)) {
                throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "No available signature_algorithms extension for client certificate authentication");
            }
            try {
                SignatureSchemesSpec signatureSchemesSpec = new SignatureSchemesSpec(byteBuffer);
                LinkedList linkedList = new LinkedList();
                for (int i2 : signatureSchemesSpec.signatureSchemes) {
                    SignatureScheme signatureSchemeValueOf = SignatureScheme.valueOf(i2);
                    if (signatureSchemeValueOf != null) {
                        linkedList.add(signatureSchemeValueOf);
                    }
                }
                clientHandshakeContext.handshakeExtensions.put(SSLExtension.CR_SIGNATURE_ALGORITHMS, signatureSchemesSpec);
            } catch (IOException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SignatureAlgorithmsExtension$CRSignatureSchemesUpdate.class */
    private static final class CRSignatureSchemesUpdate implements HandshakeConsumer {
        private CRSignatureSchemesUpdate() {
        }

        @Override // sun.security.ssl.HandshakeConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            SignatureSchemesSpec signatureSchemesSpec = (SignatureSchemesSpec) clientHandshakeContext.handshakeExtensions.get(SSLExtension.CR_SIGNATURE_ALGORITHMS);
            if (signatureSchemesSpec == null) {
                return;
            }
            List<SignatureScheme> supportedAlgorithms = SignatureScheme.getSupportedAlgorithms(clientHandshakeContext.sslConfig, clientHandshakeContext.algorithmConstraints, clientHandshakeContext.negotiatedProtocol, signatureSchemesSpec.signatureSchemes);
            clientHandshakeContext.peerRequestedSignatureSchemes = supportedAlgorithms;
            if (((SignatureSchemesSpec) clientHandshakeContext.handshakeExtensions.get(SSLExtension.CR_SIGNATURE_ALGORITHMS_CERT)) == null) {
                clientHandshakeContext.peerRequestedCertSignSchemes = supportedAlgorithms;
                clientHandshakeContext.handshakeSession.setPeerSupportedSignatureAlgorithms(supportedAlgorithms);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SignatureAlgorithmsExtension$CRSignatureSchemesAbsence.class */
    private static final class CRSignatureSchemesAbsence implements HandshakeAbsence {
        private CRSignatureSchemesAbsence() {
        }

        @Override // sun.security.ssl.HandshakeAbsence
        public void absent(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            throw ((ClientHandshakeContext) connectionContext).conContext.fatal(Alert.MISSING_EXTENSION, "No mandatory signature_algorithms extension in the received CertificateRequest handshake message");
        }
    }
}
