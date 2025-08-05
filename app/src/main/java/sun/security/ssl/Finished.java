package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.text.MessageFormat;
import java.util.Locale;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.HexDumpEncoder;
import sun.security.internal.spec.TlsPrfParameterSpec;
import sun.security.ssl.CipherSuite;
import sun.security.ssl.SSLBasicKeyDerivation;
import sun.security.ssl.SSLCipher;
import sun.security.ssl.SSLHandshake;
import sun.util.locale.LanguageTag;

/* loaded from: jsse.jar:sun/security/ssl/Finished.class */
final class Finished {
    static final SSLConsumer t12HandshakeConsumer = new T12FinishedConsumer();
    static final HandshakeProducer t12HandshakeProducer = new T12FinishedProducer();
    static final SSLConsumer t13HandshakeConsumer = new T13FinishedConsumer();
    static final HandshakeProducer t13HandshakeProducer = new T13FinishedProducer();

    /* loaded from: jsse.jar:sun/security/ssl/Finished$VerifyDataGenerator.class */
    interface VerifyDataGenerator {
        byte[] createVerifyData(HandshakeContext handshakeContext, boolean z2) throws IOException;
    }

    Finished() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/Finished$FinishedMessage.class */
    private static final class FinishedMessage extends SSLHandshake.HandshakeMessage {
        private final byte[] verifyData;

        FinishedMessage(HandshakeContext handshakeContext) throws IOException {
            super(handshakeContext);
            try {
                this.verifyData = VerifyDataScheme.valueOf(handshakeContext.negotiatedProtocol).createVerifyData(handshakeContext, false);
            } catch (IOException e2) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Failed to generate verify_data", e2);
            }
        }

        FinishedMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            super(handshakeContext);
            int i2 = 12;
            if (handshakeContext.negotiatedProtocol == ProtocolVersion.SSL30) {
                i2 = 36;
            } else if (handshakeContext.negotiatedProtocol.useTLS13PlusSpec()) {
                i2 = handshakeContext.negotiatedCipherSuite.hashAlg.hashLength;
            }
            if (byteBuffer.remaining() != i2) {
                throw handshakeContext.conContext.fatal(Alert.DECODE_ERROR, "Inappropriate finished message: need " + i2 + " but remaining " + byteBuffer.remaining() + " bytes verify_data");
            }
            this.verifyData = new byte[i2];
            byteBuffer.get(this.verifyData);
            try {
                if (!MessageDigest.isEqual(VerifyDataScheme.valueOf(handshakeContext.negotiatedProtocol).createVerifyData(handshakeContext, true), this.verifyData)) {
                    throw handshakeContext.conContext.fatal(Alert.DECRYPT_ERROR, "The Finished message cannot be verified.");
                }
            } catch (IOException e2) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Failed to generate verify_data", e2);
            }
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public SSLHandshake handshakeType() {
            return SSLHandshake.FINISHED;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public int messageLength() {
            return this.verifyData.length;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public void send(HandshakeOutStream handshakeOutStream) throws IOException {
            handshakeOutStream.write(this.verifyData);
        }

        public String toString() {
            return new MessageFormat("\"Finished\": '{'\n  \"verify data\": '{'\n{0}\n  '}''}'", Locale.ENGLISH).format(new Object[]{Utilities.indent(new HexDumpEncoder().encode(this.verifyData), "    ")});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/Finished$VerifyDataScheme.class */
    enum VerifyDataScheme {
        SSL30("kdf_ssl30", new S30VerifyDataGenerator()),
        TLS10("kdf_tls10", new T10VerifyDataGenerator()),
        TLS12("kdf_tls12", new T12VerifyDataGenerator()),
        TLS13("kdf_tls13", new T13VerifyDataGenerator());

        final String name;
        final VerifyDataGenerator generator;

        VerifyDataScheme(String str, VerifyDataGenerator verifyDataGenerator) {
            this.name = str;
            this.generator = verifyDataGenerator;
        }

        static VerifyDataScheme valueOf(ProtocolVersion protocolVersion) {
            switch (protocolVersion) {
                case SSL30:
                    return SSL30;
                case TLS10:
                case TLS11:
                    return TLS10;
                case TLS12:
                    return TLS12;
                case TLS13:
                    return TLS13;
                default:
                    return null;
            }
        }

        public byte[] createVerifyData(HandshakeContext handshakeContext, boolean z2) throws IOException {
            if (this.generator != null) {
                return this.generator.createVerifyData(handshakeContext, z2);
            }
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/Finished$S30VerifyDataGenerator.class */
    private static final class S30VerifyDataGenerator implements VerifyDataGenerator {
        private S30VerifyDataGenerator() {
        }

        @Override // sun.security.ssl.Finished.VerifyDataGenerator
        public byte[] createVerifyData(HandshakeContext handshakeContext, boolean z2) throws IOException {
            return handshakeContext.handshakeHash.digest((handshakeContext.sslConfig.isClientMode && !z2) || (!handshakeContext.sslConfig.isClientMode && z2), handshakeContext.handshakeSession.getMasterSecret());
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/Finished$T10VerifyDataGenerator.class */
    private static final class T10VerifyDataGenerator implements VerifyDataGenerator {
        private T10VerifyDataGenerator() {
        }

        @Override // sun.security.ssl.Finished.VerifyDataGenerator
        public byte[] createVerifyData(HandshakeContext handshakeContext, boolean z2) throws IOException {
            String str;
            HandshakeHash handshakeHash = handshakeContext.handshakeHash;
            SecretKey masterSecret = handshakeContext.handshakeSession.getMasterSecret();
            if ((handshakeContext.sslConfig.isClientMode && !z2) || (!handshakeContext.sslConfig.isClientMode && z2)) {
                str = "client finished";
            } else {
                str = "server finished";
            }
            try {
                byte[] bArrDigest = handshakeHash.digest();
                CipherSuite.HashAlg hashAlg = CipherSuite.HashAlg.H_NONE;
                TlsPrfParameterSpec tlsPrfParameterSpec = new TlsPrfParameterSpec(masterSecret, str, bArrDigest, 12, hashAlg.name, hashAlg.hashLength, hashAlg.blockSize);
                KeyGenerator keyGenerator = JsseJce.getKeyGenerator("SunTlsPrf");
                keyGenerator.init(tlsPrfParameterSpec);
                SecretKey secretKeyGenerateKey = keyGenerator.generateKey();
                if (!"RAW".equals(secretKeyGenerateKey.getFormat())) {
                    throw new ProviderException("Invalid PRF output, format must be RAW. Format received: " + secretKeyGenerateKey.getFormat());
                }
                return secretKeyGenerateKey.getEncoded();
            } catch (GeneralSecurityException e2) {
                throw new RuntimeException("PRF failed", e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/Finished$T12VerifyDataGenerator.class */
    private static final class T12VerifyDataGenerator implements VerifyDataGenerator {
        private T12VerifyDataGenerator() {
        }

        @Override // sun.security.ssl.Finished.VerifyDataGenerator
        public byte[] createVerifyData(HandshakeContext handshakeContext, boolean z2) throws IOException {
            String str;
            CipherSuite cipherSuite = handshakeContext.negotiatedCipherSuite;
            HandshakeHash handshakeHash = handshakeContext.handshakeHash;
            SecretKey masterSecret = handshakeContext.handshakeSession.getMasterSecret();
            if ((handshakeContext.sslConfig.isClientMode && !z2) || (!handshakeContext.sslConfig.isClientMode && z2)) {
                str = "client finished";
            } else {
                str = "server finished";
            }
            try {
                byte[] bArrDigest = handshakeHash.digest();
                CipherSuite.HashAlg hashAlg = cipherSuite.hashAlg;
                TlsPrfParameterSpec tlsPrfParameterSpec = new TlsPrfParameterSpec(masterSecret, str, bArrDigest, 12, hashAlg.name, hashAlg.hashLength, hashAlg.blockSize);
                KeyGenerator keyGenerator = JsseJce.getKeyGenerator("SunTls12Prf");
                keyGenerator.init(tlsPrfParameterSpec);
                SecretKey secretKeyGenerateKey = keyGenerator.generateKey();
                if (!"RAW".equals(secretKeyGenerateKey.getFormat())) {
                    throw new ProviderException("Invalid PRF output, format must be RAW. Format received: " + secretKeyGenerateKey.getFormat());
                }
                return secretKeyGenerateKey.getEncoded();
            } catch (GeneralSecurityException e2) {
                throw new RuntimeException("PRF failed", e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/Finished$T13VerifyDataGenerator.class */
    private static final class T13VerifyDataGenerator implements VerifyDataGenerator {
        private static final byte[] hkdfLabel = "tls13 finished".getBytes();
        private static final byte[] hkdfContext = new byte[0];

        private T13VerifyDataGenerator() {
        }

        @Override // sun.security.ssl.Finished.VerifyDataGenerator
        public byte[] createVerifyData(HandshakeContext handshakeContext, boolean z2) throws IOException {
            CipherSuite.HashAlg hashAlg = handshakeContext.negotiatedCipherSuite.hashAlg;
            SecretKey secretKeyDeriveKey = new SSLBasicKeyDerivation(z2 ? handshakeContext.baseReadSecret : handshakeContext.baseWriteSecret, hashAlg.name, hkdfLabel, hkdfContext, hashAlg.hashLength).deriveKey("TlsFinishedSecret", new SSLBasicKeyDerivation.SecretSizeSpec(hashAlg.hashLength));
            try {
                Mac mac = JsseJce.getMac("Hmac" + hashAlg.name.replace(LanguageTag.SEP, ""));
                mac.init(secretKeyDeriveKey);
                return mac.doFinal(handshakeContext.handshakeHash.digest());
            } catch (InvalidKeyException | NoSuchAlgorithmException e2) {
                throw new ProviderException("Failed to generate verify_data", e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/Finished$T12FinishedProducer.class */
    private static final class T12FinishedProducer implements HandshakeProducer {
        private T12FinishedProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            if (((HandshakeContext) connectionContext).sslConfig.isClientMode) {
                return onProduceFinished((ClientHandshakeContext) connectionContext, handshakeMessage);
            }
            return onProduceFinished((ServerHandshakeContext) connectionContext, handshakeMessage);
        }

        private byte[] onProduceFinished(ClientHandshakeContext clientHandshakeContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            clientHandshakeContext.handshakeHash.update();
            FinishedMessage finishedMessage = new FinishedMessage(clientHandshakeContext);
            ChangeCipherSpec.t10Producer.produce(clientHandshakeContext, handshakeMessage);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced client Finished handshake message", finishedMessage);
            }
            finishedMessage.write(clientHandshakeContext.handshakeOutput);
            clientHandshakeContext.handshakeOutput.flush();
            if (clientHandshakeContext.conContext.secureRenegotiation) {
                clientHandshakeContext.conContext.clientVerifyData = finishedMessage.verifyData;
            }
            if (!clientHandshakeContext.isResumption) {
                clientHandshakeContext.conContext.consumers.put(Byte.valueOf(ContentType.CHANGE_CIPHER_SPEC.id), ChangeCipherSpec.t10Consumer);
                clientHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.FINISHED.id), SSLHandshake.FINISHED);
                return null;
            }
            if (clientHandshakeContext.handshakeSession.isRejoinable()) {
                ((SSLSessionContextImpl) clientHandshakeContext.sslContext.engineGetClientSessionContext()).put(clientHandshakeContext.handshakeSession);
            }
            clientHandshakeContext.conContext.conSession = clientHandshakeContext.handshakeSession.finish();
            clientHandshakeContext.conContext.protocolVersion = clientHandshakeContext.negotiatedProtocol;
            clientHandshakeContext.handshakeFinished = true;
            clientHandshakeContext.conContext.finishHandshake();
            return null;
        }

        private byte[] onProduceFinished(ServerHandshakeContext serverHandshakeContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            serverHandshakeContext.handshakeHash.update();
            FinishedMessage finishedMessage = new FinishedMessage(serverHandshakeContext);
            ChangeCipherSpec.t10Producer.produce(serverHandshakeContext, handshakeMessage);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced server Finished handshake message", finishedMessage);
            }
            finishedMessage.write(serverHandshakeContext.handshakeOutput);
            serverHandshakeContext.handshakeOutput.flush();
            if (serverHandshakeContext.conContext.secureRenegotiation) {
                serverHandshakeContext.conContext.serverVerifyData = finishedMessage.verifyData;
            }
            if (serverHandshakeContext.isResumption) {
                serverHandshakeContext.conContext.consumers.put(Byte.valueOf(ContentType.CHANGE_CIPHER_SPEC.id), ChangeCipherSpec.t10Consumer);
                serverHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.FINISHED.id), SSLHandshake.FINISHED);
                return null;
            }
            if (serverHandshakeContext.handshakeSession.isRejoinable()) {
                ((SSLSessionContextImpl) serverHandshakeContext.sslContext.engineGetServerSessionContext()).put(serverHandshakeContext.handshakeSession);
            }
            serverHandshakeContext.conContext.conSession = serverHandshakeContext.handshakeSession.finish();
            serverHandshakeContext.conContext.protocolVersion = serverHandshakeContext.negotiatedProtocol;
            serverHandshakeContext.handshakeFinished = true;
            serverHandshakeContext.conContext.finishHandshake();
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/Finished$T12FinishedConsumer.class */
    private static final class T12FinishedConsumer implements SSLConsumer {
        private T12FinishedConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            HandshakeContext handshakeContext = (HandshakeContext) connectionContext;
            handshakeContext.handshakeConsumers.remove(Byte.valueOf(SSLHandshake.FINISHED.id));
            if (handshakeContext.conContext.consumers.containsKey(Byte.valueOf(ContentType.CHANGE_CIPHER_SPEC.id))) {
                throw handshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Missing ChangeCipherSpec message");
            }
            if (handshakeContext.sslConfig.isClientMode) {
                onConsumeFinished((ClientHandshakeContext) connectionContext, byteBuffer);
            } else {
                onConsumeFinished((ServerHandshakeContext) connectionContext, byteBuffer);
            }
        }

        private void onConsumeFinished(ClientHandshakeContext clientHandshakeContext, ByteBuffer byteBuffer) throws IOException {
            FinishedMessage finishedMessage = new FinishedMessage(clientHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming server Finished handshake message", finishedMessage);
            }
            if (clientHandshakeContext.conContext.secureRenegotiation) {
                clientHandshakeContext.conContext.serverVerifyData = finishedMessage.verifyData;
            }
            if (!clientHandshakeContext.isResumption) {
                if (clientHandshakeContext.handshakeSession.isRejoinable()) {
                    ((SSLSessionContextImpl) clientHandshakeContext.sslContext.engineGetClientSessionContext()).put(clientHandshakeContext.handshakeSession);
                }
                clientHandshakeContext.conContext.conSession = clientHandshakeContext.handshakeSession.finish();
                clientHandshakeContext.conContext.protocolVersion = clientHandshakeContext.negotiatedProtocol;
                clientHandshakeContext.handshakeFinished = true;
                clientHandshakeContext.conContext.finishHandshake();
            } else {
                clientHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.FINISHED.id), SSLHandshake.FINISHED);
            }
            for (SSLHandshake sSLHandshake : new SSLHandshake[]{SSLHandshake.FINISHED}) {
                HandshakeProducer handshakeProducerRemove = clientHandshakeContext.handshakeProducers.remove(Byte.valueOf(sSLHandshake.id));
                if (handshakeProducerRemove != null) {
                    handshakeProducerRemove.produce(clientHandshakeContext, finishedMessage);
                }
            }
        }

        private void onConsumeFinished(ServerHandshakeContext serverHandshakeContext, ByteBuffer byteBuffer) throws IOException {
            if (!serverHandshakeContext.isResumption && serverHandshakeContext.handshakeConsumers.containsKey(Byte.valueOf(SSLHandshake.CERTIFICATE_VERIFY.id))) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected Finished handshake message");
            }
            FinishedMessage finishedMessage = new FinishedMessage(serverHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming client Finished handshake message", finishedMessage);
            }
            if (serverHandshakeContext.conContext.secureRenegotiation) {
                serverHandshakeContext.conContext.clientVerifyData = finishedMessage.verifyData;
            }
            if (serverHandshakeContext.isResumption) {
                if (serverHandshakeContext.handshakeSession.isRejoinable()) {
                    ((SSLSessionContextImpl) serverHandshakeContext.sslContext.engineGetServerSessionContext()).put(serverHandshakeContext.handshakeSession);
                }
                serverHandshakeContext.conContext.conSession = serverHandshakeContext.handshakeSession.finish();
                serverHandshakeContext.conContext.protocolVersion = serverHandshakeContext.negotiatedProtocol;
                serverHandshakeContext.handshakeFinished = true;
                serverHandshakeContext.conContext.finishHandshake();
            } else {
                serverHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.FINISHED.id), SSLHandshake.FINISHED);
            }
            for (SSLHandshake sSLHandshake : new SSLHandshake[]{SSLHandshake.FINISHED}) {
                HandshakeProducer handshakeProducerRemove = serverHandshakeContext.handshakeProducers.remove(Byte.valueOf(sSLHandshake.id));
                if (handshakeProducerRemove != null) {
                    handshakeProducerRemove.produce(serverHandshakeContext, finishedMessage);
                }
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/Finished$T13FinishedProducer.class */
    private static final class T13FinishedProducer implements HandshakeProducer {
        private T13FinishedProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            if (((HandshakeContext) connectionContext).sslConfig.isClientMode) {
                return onProduceFinished((ClientHandshakeContext) connectionContext, handshakeMessage);
            }
            return onProduceFinished((ServerHandshakeContext) connectionContext, handshakeMessage);
        }

        private byte[] onProduceFinished(ClientHandshakeContext clientHandshakeContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            clientHandshakeContext.handshakeHash.update();
            FinishedMessage finishedMessage = new FinishedMessage(clientHandshakeContext);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced client Finished handshake message", finishedMessage);
            }
            finishedMessage.write(clientHandshakeContext.handshakeOutput);
            clientHandshakeContext.handshakeOutput.flush();
            if (clientHandshakeContext.conContext.secureRenegotiation) {
                clientHandshakeContext.conContext.clientVerifyData = finishedMessage.verifyData;
            }
            SSLKeyDerivation sSLKeyDerivation = clientHandshakeContext.handshakeKeyDerivation;
            if (sSLKeyDerivation == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "no key derivation");
            }
            SSLTrafficKeyDerivation sSLTrafficKeyDerivationValueOf = SSLTrafficKeyDerivation.valueOf(clientHandshakeContext.negotiatedProtocol);
            if (sSLTrafficKeyDerivationValueOf == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key derivation: " + ((Object) clientHandshakeContext.negotiatedProtocol));
            }
            try {
                SecretKey secretKeyDeriveKey = sSLKeyDerivation.deriveKey("TlsClientAppTrafficSecret", null);
                SSLKeyDerivation sSLKeyDerivationCreateKeyDerivation = sSLTrafficKeyDerivationValueOf.createKeyDerivation(clientHandshakeContext, secretKeyDeriveKey);
                SSLCipher.SSLWriteCipher sSLWriteCipherCreateWriteCipher = clientHandshakeContext.negotiatedCipherSuite.bulkCipher.createWriteCipher(Authenticator.valueOf(clientHandshakeContext.negotiatedProtocol), clientHandshakeContext.negotiatedProtocol, sSLKeyDerivationCreateKeyDerivation.deriveKey("TlsKey", null), new IvParameterSpec(sSLKeyDerivationCreateKeyDerivation.deriveKey("TlsIv", null).getEncoded()), clientHandshakeContext.sslContext.getSecureRandom());
                if (sSLWriteCipherCreateWriteCipher == null) {
                    throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Illegal cipher suite (" + ((Object) clientHandshakeContext.negotiatedCipherSuite) + ") and protocol version (" + ((Object) clientHandshakeContext.negotiatedProtocol) + ")");
                }
                clientHandshakeContext.baseWriteSecret = secretKeyDeriveKey;
                clientHandshakeContext.conContext.outputRecord.changeWriteCiphers(sSLWriteCipherCreateWriteCipher, false);
                clientHandshakeContext.handshakeSession.setResumptionMasterSecret(((SSLSecretDerivation) sSLKeyDerivation).forContext(clientHandshakeContext).deriveKey("TlsResumptionMasterSecret", null));
                clientHandshakeContext.conContext.conSession = clientHandshakeContext.handshakeSession.finish();
                clientHandshakeContext.conContext.protocolVersion = clientHandshakeContext.negotiatedProtocol;
                clientHandshakeContext.handshakeFinished = true;
                clientHandshakeContext.conContext.finishHandshake();
                return null;
            } catch (GeneralSecurityException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Failure to derive application secrets", e2);
            }
        }

        private byte[] onProduceFinished(ServerHandshakeContext serverHandshakeContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            serverHandshakeContext.handshakeHash.update();
            FinishedMessage finishedMessage = new FinishedMessage(serverHandshakeContext);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced server Finished handshake message", finishedMessage);
            }
            finishedMessage.write(serverHandshakeContext.handshakeOutput);
            serverHandshakeContext.handshakeOutput.flush();
            SSLKeyDerivation sSLKeyDerivation = serverHandshakeContext.handshakeKeyDerivation;
            if (sSLKeyDerivation == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "no key derivation");
            }
            SSLTrafficKeyDerivation sSLTrafficKeyDerivationValueOf = SSLTrafficKeyDerivation.valueOf(serverHandshakeContext.negotiatedProtocol);
            if (sSLTrafficKeyDerivationValueOf == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key derivation: " + ((Object) serverHandshakeContext.negotiatedProtocol));
            }
            try {
                SecretKey secretKeyDeriveKey = sSLKeyDerivation.deriveKey("TlsSaltSecret", null);
                CipherSuite.HashAlg hashAlg = serverHandshakeContext.negotiatedCipherSuite.hashAlg;
                SSLSecretDerivation sSLSecretDerivation = new SSLSecretDerivation(serverHandshakeContext, new HKDF(hashAlg.name).extract(secretKeyDeriveKey, new SecretKeySpec(new byte[hashAlg.hashLength], "TlsZeroSecret"), "TlsMasterSecret"));
                SecretKey secretKeyDeriveKey2 = sSLSecretDerivation.deriveKey("TlsServerAppTrafficSecret", null);
                SSLKeyDerivation sSLKeyDerivationCreateKeyDerivation = sSLTrafficKeyDerivationValueOf.createKeyDerivation(serverHandshakeContext, secretKeyDeriveKey2);
                SSLCipher.SSLWriteCipher sSLWriteCipherCreateWriteCipher = serverHandshakeContext.negotiatedCipherSuite.bulkCipher.createWriteCipher(Authenticator.valueOf(serverHandshakeContext.negotiatedProtocol), serverHandshakeContext.negotiatedProtocol, sSLKeyDerivationCreateKeyDerivation.deriveKey("TlsKey", null), new IvParameterSpec(sSLKeyDerivationCreateKeyDerivation.deriveKey("TlsIv", null).getEncoded()), serverHandshakeContext.sslContext.getSecureRandom());
                if (sSLWriteCipherCreateWriteCipher == null) {
                    throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Illegal cipher suite (" + ((Object) serverHandshakeContext.negotiatedCipherSuite) + ") and protocol version (" + ((Object) serverHandshakeContext.negotiatedProtocol) + ")");
                }
                serverHandshakeContext.baseWriteSecret = secretKeyDeriveKey2;
                serverHandshakeContext.conContext.outputRecord.changeWriteCiphers(sSLWriteCipherCreateWriteCipher, false);
                serverHandshakeContext.handshakeKeyDerivation = sSLSecretDerivation;
                if (serverHandshakeContext.conContext.secureRenegotiation) {
                    serverHandshakeContext.conContext.serverVerifyData = finishedMessage.verifyData;
                }
                serverHandshakeContext.handshakeConsumers.put(Byte.valueOf(SSLHandshake.FINISHED.id), SSLHandshake.FINISHED);
                return null;
            } catch (GeneralSecurityException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Failure to derive application secrets", e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/Finished$T13FinishedConsumer.class */
    private static final class T13FinishedConsumer implements SSLConsumer {
        private T13FinishedConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            if (((HandshakeContext) connectionContext).sslConfig.isClientMode) {
                onConsumeFinished((ClientHandshakeContext) connectionContext, byteBuffer);
            } else {
                onConsumeFinished((ServerHandshakeContext) connectionContext, byteBuffer);
            }
        }

        private void onConsumeFinished(ClientHandshakeContext clientHandshakeContext, ByteBuffer byteBuffer) throws IOException {
            if (!clientHandshakeContext.isResumption && (clientHandshakeContext.handshakeConsumers.containsKey(Byte.valueOf(SSLHandshake.CERTIFICATE.id)) || clientHandshakeContext.handshakeConsumers.containsKey(Byte.valueOf(SSLHandshake.CERTIFICATE_VERIFY.id)))) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected Finished handshake message");
            }
            FinishedMessage finishedMessage = new FinishedMessage(clientHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming server Finished handshake message", finishedMessage);
            }
            if (clientHandshakeContext.conContext.secureRenegotiation) {
                clientHandshakeContext.conContext.serverVerifyData = finishedMessage.verifyData;
            }
            clientHandshakeContext.conContext.consumers.remove(Byte.valueOf(ContentType.CHANGE_CIPHER_SPEC.id));
            clientHandshakeContext.handshakeHash.update();
            SSLKeyDerivation sSLKeyDerivation = clientHandshakeContext.handshakeKeyDerivation;
            if (sSLKeyDerivation == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "no key derivation");
            }
            SSLTrafficKeyDerivation sSLTrafficKeyDerivationValueOf = SSLTrafficKeyDerivation.valueOf(clientHandshakeContext.negotiatedProtocol);
            if (sSLTrafficKeyDerivationValueOf == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key derivation: " + ((Object) clientHandshakeContext.negotiatedProtocol));
            }
            if (!clientHandshakeContext.isResumption && clientHandshakeContext.handshakeSession.isRejoinable()) {
                ((SSLSessionContextImpl) clientHandshakeContext.sslContext.engineGetClientSessionContext()).put(clientHandshakeContext.handshakeSession);
            }
            try {
                SecretKey secretKeyDeriveKey = sSLKeyDerivation.deriveKey("TlsSaltSecret", null);
                CipherSuite.HashAlg hashAlg = clientHandshakeContext.negotiatedCipherSuite.hashAlg;
                SSLSecretDerivation sSLSecretDerivation = new SSLSecretDerivation(clientHandshakeContext, new HKDF(hashAlg.name).extract(secretKeyDeriveKey, new SecretKeySpec(new byte[hashAlg.hashLength], "TlsZeroSecret"), "TlsMasterSecret"));
                SecretKey secretKeyDeriveKey2 = sSLSecretDerivation.deriveKey("TlsServerAppTrafficSecret", null);
                SSLKeyDerivation sSLKeyDerivationCreateKeyDerivation = sSLTrafficKeyDerivationValueOf.createKeyDerivation(clientHandshakeContext, secretKeyDeriveKey2);
                SSLCipher.SSLReadCipher sSLReadCipherCreateReadCipher = clientHandshakeContext.negotiatedCipherSuite.bulkCipher.createReadCipher(Authenticator.valueOf(clientHandshakeContext.negotiatedProtocol), clientHandshakeContext.negotiatedProtocol, sSLKeyDerivationCreateKeyDerivation.deriveKey("TlsKey", null), new IvParameterSpec(sSLKeyDerivationCreateKeyDerivation.deriveKey("TlsIv", null).getEncoded()), clientHandshakeContext.sslContext.getSecureRandom());
                if (sSLReadCipherCreateReadCipher == null) {
                    throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Illegal cipher suite (" + ((Object) clientHandshakeContext.negotiatedCipherSuite) + ") and protocol version (" + ((Object) clientHandshakeContext.negotiatedProtocol) + ")");
                }
                clientHandshakeContext.baseReadSecret = secretKeyDeriveKey2;
                clientHandshakeContext.conContext.inputRecord.changeReadCiphers(sSLReadCipherCreateReadCipher);
                clientHandshakeContext.handshakeKeyDerivation = sSLSecretDerivation;
                clientHandshakeContext.handshakeProducers.put(Byte.valueOf(SSLHandshake.FINISHED.id), SSLHandshake.FINISHED);
                for (SSLHandshake sSLHandshake : new SSLHandshake[]{SSLHandshake.CERTIFICATE, SSLHandshake.CERTIFICATE_VERIFY, SSLHandshake.FINISHED}) {
                    HandshakeProducer handshakeProducerRemove = clientHandshakeContext.handshakeProducers.remove(Byte.valueOf(sSLHandshake.id));
                    if (handshakeProducerRemove != null) {
                        handshakeProducerRemove.produce(clientHandshakeContext, null);
                    }
                }
            } catch (GeneralSecurityException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Failure to derive application secrets", e2);
            }
        }

        private void onConsumeFinished(ServerHandshakeContext serverHandshakeContext, ByteBuffer byteBuffer) throws IOException {
            if (!serverHandshakeContext.isResumption && (serverHandshakeContext.handshakeConsumers.containsKey(Byte.valueOf(SSLHandshake.CERTIFICATE.id)) || serverHandshakeContext.handshakeConsumers.containsKey(Byte.valueOf(SSLHandshake.CERTIFICATE_VERIFY.id)))) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected Finished handshake message");
            }
            FinishedMessage finishedMessage = new FinishedMessage(serverHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming client Finished handshake message", finishedMessage);
            }
            if (serverHandshakeContext.conContext.secureRenegotiation) {
                serverHandshakeContext.conContext.clientVerifyData = finishedMessage.verifyData;
            }
            SSLKeyDerivation sSLKeyDerivation = serverHandshakeContext.handshakeKeyDerivation;
            if (sSLKeyDerivation == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "no key derivation");
            }
            SSLTrafficKeyDerivation sSLTrafficKeyDerivationValueOf = SSLTrafficKeyDerivation.valueOf(serverHandshakeContext.negotiatedProtocol);
            if (sSLTrafficKeyDerivationValueOf == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key derivation: " + ((Object) serverHandshakeContext.negotiatedProtocol));
            }
            if (!serverHandshakeContext.isResumption && serverHandshakeContext.handshakeSession.isRejoinable()) {
                ((SSLSessionContextImpl) serverHandshakeContext.sslContext.engineGetServerSessionContext()).put(serverHandshakeContext.handshakeSession);
            }
            try {
                SecretKey secretKeyDeriveKey = sSLKeyDerivation.deriveKey("TlsClientAppTrafficSecret", null);
                SSLKeyDerivation sSLKeyDerivationCreateKeyDerivation = sSLTrafficKeyDerivationValueOf.createKeyDerivation(serverHandshakeContext, secretKeyDeriveKey);
                SSLCipher.SSLReadCipher sSLReadCipherCreateReadCipher = serverHandshakeContext.negotiatedCipherSuite.bulkCipher.createReadCipher(Authenticator.valueOf(serverHandshakeContext.negotiatedProtocol), serverHandshakeContext.negotiatedProtocol, sSLKeyDerivationCreateKeyDerivation.deriveKey("TlsKey", null), new IvParameterSpec(sSLKeyDerivationCreateKeyDerivation.deriveKey("TlsIv", null).getEncoded()), serverHandshakeContext.sslContext.getSecureRandom());
                if (sSLReadCipherCreateReadCipher == null) {
                    throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Illegal cipher suite (" + ((Object) serverHandshakeContext.negotiatedCipherSuite) + ") and protocol version (" + ((Object) serverHandshakeContext.negotiatedProtocol) + ")");
                }
                serverHandshakeContext.baseReadSecret = secretKeyDeriveKey;
                serverHandshakeContext.conContext.inputRecord.changeReadCiphers(sSLReadCipherCreateReadCipher);
                serverHandshakeContext.handshakeHash.update();
                serverHandshakeContext.handshakeSession.setResumptionMasterSecret(((SSLSecretDerivation) sSLKeyDerivation).forContext(serverHandshakeContext).deriveKey("TlsResumptionMasterSecret", null));
                serverHandshakeContext.conContext.conSession = serverHandshakeContext.handshakeSession.finish();
                serverHandshakeContext.conContext.protocolVersion = serverHandshakeContext.negotiatedProtocol;
                serverHandshakeContext.handshakeFinished = true;
                serverHandshakeContext.conContext.finishHandshake();
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Sending new session ticket", new Object[0]);
                }
                NewSessionTicket.kickstartProducer.produce(serverHandshakeContext);
            } catch (GeneralSecurityException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Failure to derive application secrets", e2);
            }
        }
    }
}
