package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import sun.misc.HexDumpEncoder;
import sun.security.ssl.SSLHandshake;
import sun.security.ssl.X509Authentication;

/* loaded from: jsse.jar:sun/security/ssl/CertificateVerify.class */
final class CertificateVerify {
    static final SSLConsumer s30HandshakeConsumer = new S30CertificateVerifyConsumer();
    static final HandshakeProducer s30HandshakeProducer = new S30CertificateVerifyProducer();
    static final SSLConsumer t10HandshakeConsumer = new T10CertificateVerifyConsumer();
    static final HandshakeProducer t10HandshakeProducer = new T10CertificateVerifyProducer();
    static final SSLConsumer t12HandshakeConsumer = new T12CertificateVerifyConsumer();
    static final HandshakeProducer t12HandshakeProducer = new T12CertificateVerifyProducer();
    static final SSLConsumer t13HandshakeConsumer = new T13CertificateVerifyConsumer();
    static final HandshakeProducer t13HandshakeProducer = new T13CertificateVerifyProducer();

    CertificateVerify() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateVerify$S30CertificateVerifyMessage.class */
    static final class S30CertificateVerifyMessage extends SSLHandshake.HandshakeMessage {
        private final byte[] signature;

        S30CertificateVerifyMessage(HandshakeContext handshakeContext, X509Authentication.X509Possession x509Possession) throws IOException {
            super(handshakeContext);
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) handshakeContext;
            String algorithm = x509Possession.popPrivateKey.getAlgorithm();
            try {
                Signature signature = getSignature(algorithm, x509Possession.popPrivateKey);
                signature.update(clientHandshakeContext.handshakeHash.digest(algorithm, clientHandshakeContext.handshakeSession.getMasterSecret()));
                this.signature = signature.sign();
            } catch (NoSuchAlgorithmException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Unsupported signature algorithm (" + algorithm + ") used in CertificateVerify handshake message", e2);
            } catch (GeneralSecurityException e3) {
                throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Cannot produce CertificateVerify signature", e3);
            }
        }

        S30CertificateVerifyMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            super(handshakeContext);
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) handshakeContext;
            if (byteBuffer.remaining() < 2) {
                throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid CertificateVerify message: no sufficient data");
            }
            this.signature = Record.getBytes16(byteBuffer);
            X509Authentication.X509Credentials x509Credentials = null;
            Iterator<SSLCredentials> it = serverHandshakeContext.handshakeCredentials.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLCredentials next = it.next();
                if (next instanceof X509Authentication.X509Credentials) {
                    x509Credentials = (X509Authentication.X509Credentials) next;
                    break;
                }
            }
            if (x509Credentials == null || x509Credentials.popPublicKey == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "No X509 credentials negotiated for CertificateVerify");
            }
            String algorithm = x509Credentials.popPublicKey.getAlgorithm();
            try {
                Signature signature = getSignature(algorithm, x509Credentials.popPublicKey);
                signature.update(serverHandshakeContext.handshakeHash.digest(algorithm, serverHandshakeContext.handshakeSession.getMasterSecret()));
                if (!signature.verify(this.signature)) {
                    throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Invalid CertificateVerify message: invalid signature");
                }
            } catch (NoSuchAlgorithmException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Unsupported signature algorithm (" + algorithm + ") used in CertificateVerify handshake message", e2);
            } catch (GeneralSecurityException e3) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Cannot verify CertificateVerify signature", e3);
            }
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public SSLHandshake handshakeType() {
            return SSLHandshake.CERTIFICATE_VERIFY;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public int messageLength() {
            return 2 + this.signature.length;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public void send(HandshakeOutStream handshakeOutStream) throws IOException {
            handshakeOutStream.putBytes16(this.signature);
        }

        public String toString() {
            return new MessageFormat("\"CertificateVerify\": '{'\n  \"signature\": '{'\n{0}\n  '}'\n'}'", Locale.ENGLISH).format(new Object[]{Utilities.indent(new HexDumpEncoder().encodeBuffer(this.signature), "    ")});
        }

        private static Signature getSignature(String str, Key key) throws GeneralSecurityException {
            Signature signature;
            switch (str) {
                case "RSA":
                    signature = JsseJce.getSignature("NONEwithRSA");
                    break;
                case "DSA":
                    signature = JsseJce.getSignature("RawDSA");
                    break;
                case "EC":
                    signature = JsseJce.getSignature("NONEwithECDSA");
                    break;
                default:
                    throw new SignatureException("Unrecognized algorithm: " + str);
            }
            if (signature != null) {
                if (key instanceof PublicKey) {
                    signature.initVerify((PublicKey) key);
                } else {
                    signature.initSign((PrivateKey) key);
                }
            }
            return signature;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateVerify$S30CertificateVerifyProducer.class */
    private static final class S30CertificateVerifyProducer implements HandshakeProducer {
        private S30CertificateVerifyProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            X509Authentication.X509Possession x509Possession = null;
            Iterator<SSLPossession> it = clientHandshakeContext.handshakePossessions.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLPossession next = it.next();
                if (next instanceof X509Authentication.X509Possession) {
                    x509Possession = (X509Authentication.X509Possession) next;
                    break;
                }
            }
            if (x509Possession == null || x509Possession.popPrivateKey == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("No X.509 credentials negotiated for CertificateVerify", new Object[0]);
                    return null;
                }
                return null;
            }
            S30CertificateVerifyMessage s30CertificateVerifyMessage = new S30CertificateVerifyMessage(clientHandshakeContext, x509Possession);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced CertificateVerify handshake message", s30CertificateVerifyMessage);
            }
            s30CertificateVerifyMessage.write(clientHandshakeContext.handshakeOutput);
            clientHandshakeContext.handshakeOutput.flush();
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateVerify$S30CertificateVerifyConsumer.class */
    private static final class S30CertificateVerifyConsumer implements SSLConsumer {
        private S30CertificateVerifyConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            serverHandshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.CERTIFICATE_VERIFY.id));
            if (serverHandshakeContext.handshakeConsumers.containsKey(Byte.valueOf(SSLHandshake.CLIENT_KEY_EXCHANGE.id))) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected CertificateVerify handshake message");
            }
            S30CertificateVerifyMessage s30CertificateVerifyMessage = new S30CertificateVerifyMessage(serverHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming CertificateVerify handshake message", s30CertificateVerifyMessage);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateVerify$T10CertificateVerifyMessage.class */
    static final class T10CertificateVerifyMessage extends SSLHandshake.HandshakeMessage {
        private final byte[] signature;

        T10CertificateVerifyMessage(HandshakeContext handshakeContext, X509Authentication.X509Possession x509Possession) throws IOException {
            super(handshakeContext);
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) handshakeContext;
            String algorithm = x509Possession.popPrivateKey.getAlgorithm();
            try {
                Signature signature = getSignature(algorithm, x509Possession.popPrivateKey);
                signature.update(clientHandshakeContext.handshakeHash.digest(algorithm));
                this.signature = signature.sign();
            } catch (NoSuchAlgorithmException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Unsupported signature algorithm (" + algorithm + ") used in CertificateVerify handshake message", e2);
            } catch (GeneralSecurityException e3) {
                throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Cannot produce CertificateVerify signature", e3);
            }
        }

        T10CertificateVerifyMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            super(handshakeContext);
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) handshakeContext;
            if (byteBuffer.remaining() < 2) {
                throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid CertificateVerify message: no sufficient data");
            }
            this.signature = Record.getBytes16(byteBuffer);
            X509Authentication.X509Credentials x509Credentials = null;
            Iterator<SSLCredentials> it = serverHandshakeContext.handshakeCredentials.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLCredentials next = it.next();
                if (next instanceof X509Authentication.X509Credentials) {
                    x509Credentials = (X509Authentication.X509Credentials) next;
                    break;
                }
            }
            if (x509Credentials == null || x509Credentials.popPublicKey == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "No X509 credentials negotiated for CertificateVerify");
            }
            String algorithm = x509Credentials.popPublicKey.getAlgorithm();
            try {
                Signature signature = getSignature(algorithm, x509Credentials.popPublicKey);
                signature.update(serverHandshakeContext.handshakeHash.digest(algorithm));
                if (!signature.verify(this.signature)) {
                    throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Invalid CertificateVerify message: invalid signature");
                }
            } catch (NoSuchAlgorithmException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Unsupported signature algorithm (" + algorithm + ") used in CertificateVerify handshake message", e2);
            } catch (GeneralSecurityException e3) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Cannot verify CertificateVerify signature", e3);
            }
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public SSLHandshake handshakeType() {
            return SSLHandshake.CERTIFICATE_VERIFY;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public int messageLength() {
            return 2 + this.signature.length;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public void send(HandshakeOutStream handshakeOutStream) throws IOException {
            handshakeOutStream.putBytes16(this.signature);
        }

        public String toString() {
            return new MessageFormat("\"CertificateVerify\": '{'\n  \"signature\": '{'\n{0}\n  '}'\n'}'", Locale.ENGLISH).format(new Object[]{Utilities.indent(new HexDumpEncoder().encodeBuffer(this.signature), "    ")});
        }

        private static Signature getSignature(String str, Key key) throws GeneralSecurityException {
            Signature signature;
            switch (str) {
                case "RSA":
                    signature = JsseJce.getSignature("NONEwithRSA");
                    break;
                case "DSA":
                    signature = JsseJce.getSignature("RawDSA");
                    break;
                case "EC":
                    signature = JsseJce.getSignature("NONEwithECDSA");
                    break;
                default:
                    throw new SignatureException("Unrecognized algorithm: " + str);
            }
            if (signature != null) {
                if (key instanceof PublicKey) {
                    signature.initVerify((PublicKey) key);
                } else {
                    signature.initSign((PrivateKey) key);
                }
            }
            return signature;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateVerify$T10CertificateVerifyProducer.class */
    private static final class T10CertificateVerifyProducer implements HandshakeProducer {
        private T10CertificateVerifyProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            X509Authentication.X509Possession x509Possession = null;
            Iterator<SSLPossession> it = clientHandshakeContext.handshakePossessions.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLPossession next = it.next();
                if (next instanceof X509Authentication.X509Possession) {
                    x509Possession = (X509Authentication.X509Possession) next;
                    break;
                }
            }
            if (x509Possession == null || x509Possession.popPrivateKey == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("No X.509 credentials negotiated for CertificateVerify", new Object[0]);
                    return null;
                }
                return null;
            }
            T10CertificateVerifyMessage t10CertificateVerifyMessage = new T10CertificateVerifyMessage(clientHandshakeContext, x509Possession);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced CertificateVerify handshake message", t10CertificateVerifyMessage);
            }
            t10CertificateVerifyMessage.write(clientHandshakeContext.handshakeOutput);
            clientHandshakeContext.handshakeOutput.flush();
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateVerify$T10CertificateVerifyConsumer.class */
    private static final class T10CertificateVerifyConsumer implements SSLConsumer {
        private T10CertificateVerifyConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            serverHandshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.CERTIFICATE_VERIFY.id));
            if (serverHandshakeContext.handshakeConsumers.containsKey(Byte.valueOf(SSLHandshake.CLIENT_KEY_EXCHANGE.id))) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected CertificateVerify handshake message");
            }
            T10CertificateVerifyMessage t10CertificateVerifyMessage = new T10CertificateVerifyMessage(serverHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming CertificateVerify handshake message", t10CertificateVerifyMessage);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateVerify$T12CertificateVerifyMessage.class */
    static final class T12CertificateVerifyMessage extends SSLHandshake.HandshakeMessage {
        private final SignatureScheme signatureScheme;
        private final byte[] signature;

        T12CertificateVerifyMessage(HandshakeContext handshakeContext, X509Authentication.X509Possession x509Possession) throws IOException {
            super(handshakeContext);
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) handshakeContext;
            Map.Entry<SignatureScheme, Signature> signerOfPreferableAlgorithm = SignatureScheme.getSignerOfPreferableAlgorithm(clientHandshakeContext.peerRequestedSignatureSchemes, x509Possession, clientHandshakeContext.negotiatedProtocol);
            if (signerOfPreferableAlgorithm == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "No supported CertificateVerify signature algorithm for " + x509Possession.popPrivateKey.getAlgorithm() + "  key");
            }
            this.signatureScheme = signerOfPreferableAlgorithm.getKey();
            try {
                Signature value = signerOfPreferableAlgorithm.getValue();
                value.update(clientHandshakeContext.handshakeHash.archived());
                this.signature = value.sign();
            } catch (SignatureException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Cannot produce CertificateVerify signature", e2);
            }
        }

        T12CertificateVerifyMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            super(handshakeContext);
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) handshakeContext;
            if (byteBuffer.remaining() < 4) {
                throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid CertificateVerify message: no sufficient data");
            }
            int int16 = Record.getInt16(byteBuffer);
            this.signatureScheme = SignatureScheme.valueOf(int16);
            if (this.signatureScheme == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Invalid signature algorithm (" + int16 + ") used in CertificateVerify handshake message");
            }
            if (!serverHandshakeContext.localSupportedSignAlgs.contains(this.signatureScheme)) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Unsupported signature algorithm (" + this.signatureScheme.name + ") used in CertificateVerify handshake message");
            }
            X509Authentication.X509Credentials x509Credentials = null;
            Iterator<SSLCredentials> it = serverHandshakeContext.handshakeCredentials.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLCredentials next = it.next();
                if (next instanceof X509Authentication.X509Credentials) {
                    x509Credentials = (X509Authentication.X509Credentials) next;
                    break;
                }
            }
            if (x509Credentials == null || x509Credentials.popPublicKey == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "No X509 credentials negotiated for CertificateVerify");
            }
            this.signature = Record.getBytes16(byteBuffer);
            try {
                Signature verifier = this.signatureScheme.getVerifier(x509Credentials.popPublicKey);
                verifier.update(serverHandshakeContext.handshakeHash.archived());
                if (!verifier.verify(this.signature)) {
                    throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Invalid CertificateVerify signature");
                }
            } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Unsupported signature algorithm (" + this.signatureScheme.name + ") used in CertificateVerify handshake message", e2);
            } catch (InvalidKeyException | SignatureException e3) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Cannot verify CertificateVerify signature", e3);
            }
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public SSLHandshake handshakeType() {
            return SSLHandshake.CERTIFICATE_VERIFY;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public int messageLength() {
            return 4 + this.signature.length;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public void send(HandshakeOutStream handshakeOutStream) throws IOException {
            handshakeOutStream.putInt16(this.signatureScheme.id);
            handshakeOutStream.putBytes16(this.signature);
        }

        public String toString() {
            return new MessageFormat("\"CertificateVerify\": '{'\n  \"signature algorithm\": {0}\n  \"signature\": '{'\n{1}\n  '}'\n'}'", Locale.ENGLISH).format(new Object[]{this.signatureScheme.name, Utilities.indent(new HexDumpEncoder().encodeBuffer(this.signature), "    ")});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateVerify$T12CertificateVerifyProducer.class */
    private static final class T12CertificateVerifyProducer implements HandshakeProducer {
        private T12CertificateVerifyProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            X509Authentication.X509Possession x509Possession = null;
            Iterator<SSLPossession> it = clientHandshakeContext.handshakePossessions.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLPossession next = it.next();
                if (next instanceof X509Authentication.X509Possession) {
                    x509Possession = (X509Authentication.X509Possession) next;
                    break;
                }
            }
            if (x509Possession == null || x509Possession.popPrivateKey == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("No X.509 credentials negotiated for CertificateVerify", new Object[0]);
                    return null;
                }
                return null;
            }
            T12CertificateVerifyMessage t12CertificateVerifyMessage = new T12CertificateVerifyMessage(clientHandshakeContext, x509Possession);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced CertificateVerify handshake message", t12CertificateVerifyMessage);
            }
            t12CertificateVerifyMessage.write(clientHandshakeContext.handshakeOutput);
            clientHandshakeContext.handshakeOutput.flush();
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateVerify$T12CertificateVerifyConsumer.class */
    private static final class T12CertificateVerifyConsumer implements SSLConsumer {
        private T12CertificateVerifyConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            serverHandshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.CERTIFICATE_VERIFY.id));
            if (serverHandshakeContext.handshakeConsumers.containsKey(Byte.valueOf(SSLHandshake.CLIENT_KEY_EXCHANGE.id))) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected CertificateVerify handshake message");
            }
            T12CertificateVerifyMessage t12CertificateVerifyMessage = new T12CertificateVerifyMessage(serverHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming CertificateVerify handshake message", t12CertificateVerifyMessage);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateVerify$T13CertificateVerifyMessage.class */
    static final class T13CertificateVerifyMessage extends SSLHandshake.HandshakeMessage {
        private static final byte[] serverSignHead = {32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 84, 76, 83, 32, 49, 46, 51, 44, 32, 115, 101, 114, 118, 101, 114, 32, 67, 101, 114, 116, 105, 102, 105, 99, 97, 116, 101, 86, 101, 114, 105, 102, 121, 0};
        private static final byte[] clientSignHead = {32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 84, 76, 83, 32, 49, 46, 51, 44, 32, 99, 108, 105, 101, 110, 116, 32, 67, 101, 114, 116, 105, 102, 105, 99, 97, 116, 101, 86, 101, 114, 105, 102, 121, 0};
        private final SignatureScheme signatureScheme;
        private final byte[] signature;

        T13CertificateVerifyMessage(HandshakeContext handshakeContext, X509Authentication.X509Possession x509Possession) throws IOException {
            byte[] bArrCopyOf;
            super(handshakeContext);
            Map.Entry<SignatureScheme, Signature> signerOfPreferableAlgorithm = SignatureScheme.getSignerOfPreferableAlgorithm(handshakeContext.peerRequestedSignatureSchemes, x509Possession, handshakeContext.negotiatedProtocol);
            if (signerOfPreferableAlgorithm == null) {
                throw handshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "No supported CertificateVerify signature algorithm for " + x509Possession.popPrivateKey.getAlgorithm() + "  key");
            }
            this.signatureScheme = signerOfPreferableAlgorithm.getKey();
            byte[] bArrDigest = handshakeContext.handshakeHash.digest();
            if (handshakeContext.sslConfig.isClientMode) {
                bArrCopyOf = Arrays.copyOf(clientSignHead, clientSignHead.length + bArrDigest.length);
                System.arraycopy(bArrDigest, 0, bArrCopyOf, clientSignHead.length, bArrDigest.length);
            } else {
                bArrCopyOf = Arrays.copyOf(serverSignHead, serverSignHead.length + bArrDigest.length);
                System.arraycopy(bArrDigest, 0, bArrCopyOf, serverSignHead.length, bArrDigest.length);
            }
            try {
                Signature value = signerOfPreferableAlgorithm.getValue();
                value.update(bArrCopyOf);
                this.signature = value.sign();
            } catch (SignatureException e2) {
                throw handshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Cannot produce CertificateVerify signature", e2);
            }
        }

        T13CertificateVerifyMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            byte[] bArrCopyOf;
            super(handshakeContext);
            if (byteBuffer.remaining() < 4) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid CertificateVerify message: no sufficient data");
            }
            int int16 = Record.getInt16(byteBuffer);
            this.signatureScheme = SignatureScheme.valueOf(int16);
            if (this.signatureScheme == null) {
                throw handshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Invalid signature algorithm (" + int16 + ") used in CertificateVerify handshake message");
            }
            if (!handshakeContext.localSupportedSignAlgs.contains(this.signatureScheme)) {
                throw handshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Unsupported signature algorithm (" + this.signatureScheme.name + ") used in CertificateVerify handshake message");
            }
            X509Authentication.X509Credentials x509Credentials = null;
            Iterator<SSLCredentials> it = handshakeContext.handshakeCredentials.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLCredentials next = it.next();
                if (next instanceof X509Authentication.X509Credentials) {
                    x509Credentials = (X509Authentication.X509Credentials) next;
                    break;
                }
            }
            if (x509Credentials == null || x509Credentials.popPublicKey == null) {
                throw handshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "No X509 credentials negotiated for CertificateVerify");
            }
            this.signature = Record.getBytes16(byteBuffer);
            byte[] bArrDigest = handshakeContext.handshakeHash.digest();
            if (handshakeContext.sslConfig.isClientMode) {
                bArrCopyOf = Arrays.copyOf(serverSignHead, serverSignHead.length + bArrDigest.length);
                System.arraycopy(bArrDigest, 0, bArrCopyOf, serverSignHead.length, bArrDigest.length);
            } else {
                bArrCopyOf = Arrays.copyOf(clientSignHead, clientSignHead.length + bArrDigest.length);
                System.arraycopy(bArrDigest, 0, bArrCopyOf, clientSignHead.length, bArrDigest.length);
            }
            try {
                Signature verifier = this.signatureScheme.getVerifier(x509Credentials.popPublicKey);
                verifier.update(bArrCopyOf);
                if (!verifier.verify(this.signature)) {
                    throw handshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Invalid CertificateVerify signature");
                }
            } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException e2) {
                throw handshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Unsupported signature algorithm (" + this.signatureScheme.name + ") used in CertificateVerify handshake message", e2);
            } catch (InvalidKeyException | SignatureException e3) {
                throw handshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Cannot verify CertificateVerify signature", e3);
            }
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public SSLHandshake handshakeType() {
            return SSLHandshake.CERTIFICATE_VERIFY;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public int messageLength() {
            return 4 + this.signature.length;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public void send(HandshakeOutStream handshakeOutStream) throws IOException {
            handshakeOutStream.putInt16(this.signatureScheme.id);
            handshakeOutStream.putBytes16(this.signature);
        }

        public String toString() {
            return new MessageFormat("\"CertificateVerify\": '{'\n  \"signature algorithm\": {0}\n  \"signature\": '{'\n{1}\n  '}'\n'}'", Locale.ENGLISH).format(new Object[]{this.signatureScheme.name, Utilities.indent(new HexDumpEncoder().encodeBuffer(this.signature), "    ")});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateVerify$T13CertificateVerifyProducer.class */
    private static final class T13CertificateVerifyProducer implements HandshakeProducer {
        private T13CertificateVerifyProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            HandshakeContext handshakeContext = (HandshakeContext) connectionContext;
            X509Authentication.X509Possession x509Possession = null;
            Iterator<SSLPossession> it = handshakeContext.handshakePossessions.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLPossession next = it.next();
                if (next instanceof X509Authentication.X509Possession) {
                    x509Possession = (X509Authentication.X509Possession) next;
                    break;
                }
            }
            if (x509Possession == null || x509Possession.popPrivateKey == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("No X.509 credentials negotiated for CertificateVerify", new Object[0]);
                    return null;
                }
                return null;
            }
            if (handshakeContext.sslConfig.isClientMode) {
                return onProduceCertificateVerify((ClientHandshakeContext) connectionContext, x509Possession);
            }
            return onProduceCertificateVerify((ServerHandshakeContext) connectionContext, x509Possession);
        }

        private byte[] onProduceCertificateVerify(ServerHandshakeContext serverHandshakeContext, X509Authentication.X509Possession x509Possession) throws IOException {
            T13CertificateVerifyMessage t13CertificateVerifyMessage = new T13CertificateVerifyMessage(serverHandshakeContext, x509Possession);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced server CertificateVerify handshake message", t13CertificateVerifyMessage);
            }
            t13CertificateVerifyMessage.write(serverHandshakeContext.handshakeOutput);
            serverHandshakeContext.handshakeOutput.flush();
            return null;
        }

        private byte[] onProduceCertificateVerify(ClientHandshakeContext clientHandshakeContext, X509Authentication.X509Possession x509Possession) throws IOException {
            T13CertificateVerifyMessage t13CertificateVerifyMessage = new T13CertificateVerifyMessage(clientHandshakeContext, x509Possession);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced client CertificateVerify handshake message", t13CertificateVerifyMessage);
            }
            t13CertificateVerifyMessage.write(clientHandshakeContext.handshakeOutput);
            clientHandshakeContext.handshakeOutput.flush();
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CertificateVerify$T13CertificateVerifyConsumer.class */
    private static final class T13CertificateVerifyConsumer implements SSLConsumer {
        private T13CertificateVerifyConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            HandshakeContext handshakeContext = (HandshakeContext) connectionContext;
            handshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.CERTIFICATE_VERIFY.id));
            T13CertificateVerifyMessage t13CertificateVerifyMessage = new T13CertificateVerifyMessage(handshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming CertificateVerify handshake message", t13CertificateVerifyMessage);
            }
        }
    }
}
