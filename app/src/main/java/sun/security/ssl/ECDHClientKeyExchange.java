package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.AlgorithmConstraints;
import java.security.CryptoPrimitive;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPublicKeySpec;
import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Locale;
import javax.crypto.SecretKey;
import javax.net.ssl.SSLHandshakeException;
import sun.misc.HexDumpEncoder;
import sun.security.ssl.ECDHKeyExchange;
import sun.security.ssl.SSLHandshake;
import sun.security.ssl.SupportedGroupsExtension;
import sun.security.ssl.X509Authentication;

/* loaded from: jsse.jar:sun/security/ssl/ECDHClientKeyExchange.class */
final class ECDHClientKeyExchange {
    static final SSLConsumer ecdhHandshakeConsumer = new ECDHClientKeyExchangeConsumer();
    static final HandshakeProducer ecdhHandshakeProducer = new ECDHClientKeyExchangeProducer();
    static final SSLConsumer ecdheHandshakeConsumer = new ECDHEClientKeyExchangeConsumer();
    static final HandshakeProducer ecdheHandshakeProducer = new ECDHEClientKeyExchangeProducer();

    ECDHClientKeyExchange() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/ECDHClientKeyExchange$ECDHClientKeyExchangeMessage.class */
    private static final class ECDHClientKeyExchangeMessage extends SSLHandshake.HandshakeMessage {
        private final byte[] encodedPoint;

        ECDHClientKeyExchangeMessage(HandshakeContext handshakeContext, ECPublicKey eCPublicKey) {
            super(handshakeContext);
            this.encodedPoint = JsseJce.encodePoint(eCPublicKey.getW(), eCPublicKey.getParams().getCurve());
        }

        ECDHClientKeyExchangeMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            super(handshakeContext);
            if (byteBuffer.remaining() != 0) {
                this.encodedPoint = Record.getBytes8(byteBuffer);
            } else {
                this.encodedPoint = new byte[0];
            }
        }

        static void checkConstraints(AlgorithmConstraints algorithmConstraints, ECPublicKey eCPublicKey, byte[] bArr) throws SSLHandshakeException {
            try {
                ECParameterSpec params = eCPublicKey.getParams();
                ECPublicKey eCPublicKey2 = (ECPublicKey) JsseJce.getKeyFactory("EC").generatePublic(new ECPublicKeySpec(JsseJce.decodePoint(bArr, params.getCurve()), params));
                if (algorithmConstraints != null && !algorithmConstraints.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), eCPublicKey2)) {
                    throw new SSLHandshakeException("ECPublicKey does not comply to algorithm constraints");
                }
            } catch (IOException | GeneralSecurityException e2) {
                throw ((SSLHandshakeException) new SSLHandshakeException("Could not generate ECPublicKey").initCause(e2));
            }
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public SSLHandshake handshakeType() {
            return SSLHandshake.CLIENT_KEY_EXCHANGE;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public int messageLength() {
            if (this.encodedPoint == null || this.encodedPoint.length == 0) {
                return 0;
            }
            return 1 + this.encodedPoint.length;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public void send(HandshakeOutStream handshakeOutStream) throws IOException {
            if (this.encodedPoint != null && this.encodedPoint.length != 0) {
                handshakeOutStream.putBytes8(this.encodedPoint);
            }
        }

        public String toString() {
            MessageFormat messageFormat = new MessageFormat("\"ECDH ClientKeyExchange\": '{'\n  \"ecdh public\": '{'\n{0}\n  '}',\n'}'", Locale.ENGLISH);
            if (this.encodedPoint == null || this.encodedPoint.length == 0) {
                return messageFormat.format(new Object[]{"    <implicit>"});
            }
            return messageFormat.format(new Object[]{Utilities.indent(new HexDumpEncoder().encodeBuffer(this.encodedPoint), "    ")});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ECDHClientKeyExchange$ECDHClientKeyExchangeProducer.class */
    private static final class ECDHClientKeyExchangeProducer implements HandshakeProducer {
        private ECDHClientKeyExchangeProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            X509Authentication.X509Credentials x509Credentials = null;
            Iterator<SSLCredentials> it = clientHandshakeContext.handshakeCredentials.iterator();
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
            if (x509Credentials == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "No server certificate for ECDH client key exchange");
            }
            PublicKey publicKey = x509Credentials.popPublicKey;
            if (!publicKey.getAlgorithm().equals("EC")) {
                throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Not EC server certificate for ECDH client key exchange");
            }
            SupportedGroupsExtension.NamedGroup namedGroupValueOf = SupportedGroupsExtension.NamedGroup.valueOf(((ECPublicKey) publicKey).getParams());
            if (namedGroupValueOf == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Unsupported EC server cert for ECDH client key exchange");
            }
            ECDHKeyExchange.ECDHEPossession eCDHEPossession = new ECDHKeyExchange.ECDHEPossession(namedGroupValueOf, clientHandshakeContext.sslContext.getSecureRandom());
            clientHandshakeContext.handshakePossessions.add(eCDHEPossession);
            ECDHClientKeyExchangeMessage eCDHClientKeyExchangeMessage = new ECDHClientKeyExchangeMessage(clientHandshakeContext, eCDHEPossession.publicKey);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced ECDH ClientKeyExchange handshake message", eCDHClientKeyExchangeMessage);
            }
            eCDHClientKeyExchangeMessage.write(clientHandshakeContext.handshakeOutput);
            clientHandshakeContext.handshakeOutput.flush();
            SSLKeyExchange sSLKeyExchangeValueOf = SSLKeyExchange.valueOf(clientHandshakeContext.negotiatedCipherSuite.keyExchange, clientHandshakeContext.negotiatedProtocol);
            if (sSLKeyExchangeValueOf == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key exchange type");
            }
            SecretKey secretKeyDeriveKey = sSLKeyExchangeValueOf.createKeyDerivation(clientHandshakeContext).deriveKey("MasterSecret", null);
            clientHandshakeContext.handshakeSession.setMasterSecret(secretKeyDeriveKey);
            SSLTrafficKeyDerivation sSLTrafficKeyDerivationValueOf = SSLTrafficKeyDerivation.valueOf(clientHandshakeContext.negotiatedProtocol);
            if (sSLTrafficKeyDerivationValueOf == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key derivation: " + ((Object) clientHandshakeContext.negotiatedProtocol));
            }
            clientHandshakeContext.handshakeKeyDerivation = sSLTrafficKeyDerivationValueOf.createKeyDerivation(clientHandshakeContext, secretKeyDeriveKey);
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ECDHClientKeyExchange$ECDHClientKeyExchangeConsumer.class */
    private static final class ECDHClientKeyExchangeConsumer implements SSLConsumer {
        private ECDHClientKeyExchangeConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            X509Authentication.X509Possession x509Possession = null;
            Iterator<SSLPossession> it = serverHandshakeContext.handshakePossessions.iterator();
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
            if (x509Possession == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "No expected EC server cert for ECDH client key exchange");
            }
            ECParameterSpec eCParameterSpec = x509Possession.getECParameterSpec();
            if (eCParameterSpec == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Not EC server cert for ECDH client key exchange");
            }
            SupportedGroupsExtension.NamedGroup namedGroupValueOf = SupportedGroupsExtension.NamedGroup.valueOf(eCParameterSpec);
            if (namedGroupValueOf == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Unsupported EC server cert for ECDH client key exchange");
            }
            SSLKeyExchange sSLKeyExchangeValueOf = SSLKeyExchange.valueOf(serverHandshakeContext.negotiatedCipherSuite.keyExchange, serverHandshakeContext.negotiatedProtocol);
            if (sSLKeyExchangeValueOf == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key exchange type");
            }
            ECDHClientKeyExchangeMessage eCDHClientKeyExchangeMessage = new ECDHClientKeyExchangeMessage(serverHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming ECDH ClientKeyExchange handshake message", eCDHClientKeyExchangeMessage);
            }
            try {
                ECPublicKey eCPublicKey = (ECPublicKey) JsseJce.getKeyFactory("EC").generatePublic(new ECPublicKeySpec(JsseJce.decodePoint(eCDHClientKeyExchangeMessage.encodedPoint, eCParameterSpec.getCurve()), eCParameterSpec));
                if (serverHandshakeContext.algorithmConstraints != null && !serverHandshakeContext.algorithmConstraints.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), eCPublicKey)) {
                    throw new SSLHandshakeException("ECPublicKey does not comply to algorithm constraints");
                }
                serverHandshakeContext.handshakeCredentials.add(new ECDHKeyExchange.ECDHECredentials(eCPublicKey, namedGroupValueOf));
                SecretKey secretKeyDeriveKey = sSLKeyExchangeValueOf.createKeyDerivation(serverHandshakeContext).deriveKey("MasterSecret", null);
                serverHandshakeContext.handshakeSession.setMasterSecret(secretKeyDeriveKey);
                SSLTrafficKeyDerivation sSLTrafficKeyDerivationValueOf = SSLTrafficKeyDerivation.valueOf(serverHandshakeContext.negotiatedProtocol);
                if (sSLTrafficKeyDerivationValueOf == null) {
                    throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key derivation: " + ((Object) serverHandshakeContext.negotiatedProtocol));
                }
                serverHandshakeContext.handshakeKeyDerivation = sSLTrafficKeyDerivationValueOf.createKeyDerivation(serverHandshakeContext, secretKeyDeriveKey);
            } catch (IOException | GeneralSecurityException e2) {
                throw ((SSLHandshakeException) new SSLHandshakeException("Could not generate ECPublicKey").initCause(e2));
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ECDHClientKeyExchange$ECDHEClientKeyExchangeProducer.class */
    private static final class ECDHEClientKeyExchangeProducer implements HandshakeProducer {
        private ECDHEClientKeyExchangeProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            ECDHKeyExchange.ECDHECredentials eCDHECredentials = null;
            Iterator<SSLCredentials> it = clientHandshakeContext.handshakeCredentials.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLCredentials next = it.next();
                if (next instanceof ECDHKeyExchange.ECDHECredentials) {
                    eCDHECredentials = (ECDHKeyExchange.ECDHECredentials) next;
                    break;
                }
            }
            if (eCDHECredentials == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "No ECDHE credentials negotiated for client key exchange");
            }
            ECDHKeyExchange.ECDHEPossession eCDHEPossession = new ECDHKeyExchange.ECDHEPossession(eCDHECredentials, clientHandshakeContext.sslContext.getSecureRandom());
            clientHandshakeContext.handshakePossessions.add(eCDHEPossession);
            ECDHClientKeyExchangeMessage eCDHClientKeyExchangeMessage = new ECDHClientKeyExchangeMessage(clientHandshakeContext, eCDHEPossession.publicKey);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced ECDHE ClientKeyExchange handshake message", eCDHClientKeyExchangeMessage);
            }
            eCDHClientKeyExchangeMessage.write(clientHandshakeContext.handshakeOutput);
            clientHandshakeContext.handshakeOutput.flush();
            SSLKeyExchange sSLKeyExchangeValueOf = SSLKeyExchange.valueOf(clientHandshakeContext.negotiatedCipherSuite.keyExchange, clientHandshakeContext.negotiatedProtocol);
            if (sSLKeyExchangeValueOf == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key exchange type");
            }
            SecretKey secretKeyDeriveKey = sSLKeyExchangeValueOf.createKeyDerivation(clientHandshakeContext).deriveKey("MasterSecret", null);
            clientHandshakeContext.handshakeSession.setMasterSecret(secretKeyDeriveKey);
            SSLTrafficKeyDerivation sSLTrafficKeyDerivationValueOf = SSLTrafficKeyDerivation.valueOf(clientHandshakeContext.negotiatedProtocol);
            if (sSLTrafficKeyDerivationValueOf == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key derivation: " + ((Object) clientHandshakeContext.negotiatedProtocol));
            }
            clientHandshakeContext.handshakeKeyDerivation = sSLTrafficKeyDerivationValueOf.createKeyDerivation(clientHandshakeContext, secretKeyDeriveKey);
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ECDHClientKeyExchange$ECDHEClientKeyExchangeConsumer.class */
    private static final class ECDHEClientKeyExchangeConsumer implements SSLConsumer {
        private ECDHEClientKeyExchangeConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            ECDHKeyExchange.ECDHEPossession eCDHEPossession = null;
            Iterator<SSLPossession> it = serverHandshakeContext.handshakePossessions.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLPossession next = it.next();
                if (next instanceof ECDHKeyExchange.ECDHEPossession) {
                    eCDHEPossession = (ECDHKeyExchange.ECDHEPossession) next;
                    break;
                }
            }
            if (eCDHEPossession == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "No expected ECDHE possessions for client key exchange");
            }
            ECParameterSpec params = eCDHEPossession.publicKey.getParams();
            SupportedGroupsExtension.NamedGroup namedGroupValueOf = SupportedGroupsExtension.NamedGroup.valueOf(params);
            if (namedGroupValueOf == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Unsupported EC server cert for ECDHE client key exchange");
            }
            SSLKeyExchange sSLKeyExchangeValueOf = SSLKeyExchange.valueOf(serverHandshakeContext.negotiatedCipherSuite.keyExchange, serverHandshakeContext.negotiatedProtocol);
            if (sSLKeyExchangeValueOf == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key exchange type");
            }
            ECDHClientKeyExchangeMessage eCDHClientKeyExchangeMessage = new ECDHClientKeyExchangeMessage(serverHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming ECDHE ClientKeyExchange handshake message", eCDHClientKeyExchangeMessage);
            }
            try {
                ECPublicKey eCPublicKey = (ECPublicKey) JsseJce.getKeyFactory("EC").generatePublic(new ECPublicKeySpec(JsseJce.decodePoint(eCDHClientKeyExchangeMessage.encodedPoint, params.getCurve()), params));
                if (serverHandshakeContext.algorithmConstraints != null && !serverHandshakeContext.algorithmConstraints.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), eCPublicKey)) {
                    throw new SSLHandshakeException("ECPublicKey does not comply to algorithm constraints");
                }
                serverHandshakeContext.handshakeCredentials.add(new ECDHKeyExchange.ECDHECredentials(eCPublicKey, namedGroupValueOf));
                SecretKey secretKeyDeriveKey = sSLKeyExchangeValueOf.createKeyDerivation(serverHandshakeContext).deriveKey("MasterSecret", null);
                serverHandshakeContext.handshakeSession.setMasterSecret(secretKeyDeriveKey);
                SSLTrafficKeyDerivation sSLTrafficKeyDerivationValueOf = SSLTrafficKeyDerivation.valueOf(serverHandshakeContext.negotiatedProtocol);
                if (sSLTrafficKeyDerivationValueOf == null) {
                    throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key derivation: " + ((Object) serverHandshakeContext.negotiatedProtocol));
                }
                serverHandshakeContext.handshakeKeyDerivation = sSLTrafficKeyDerivationValueOf.createKeyDerivation(serverHandshakeContext, secretKeyDeriveKey);
            } catch (IOException | GeneralSecurityException e2) {
                throw ((SSLHandshakeException) new SSLHandshakeException("Could not generate ECPublicKey").initCause(e2));
            }
        }
    }
}
