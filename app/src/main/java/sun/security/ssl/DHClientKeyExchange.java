package sun.security.ssl;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.CryptoPrimitive;
import java.security.GeneralSecurityException;
import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Locale;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;
import javax.net.ssl.SSLHandshakeException;
import org.icepdf.ri.common.utility.annotation.GoToActionDialog;
import sun.misc.HexDumpEncoder;
import sun.security.ssl.DHKeyExchange;
import sun.security.ssl.SSLHandshake;
import sun.security.ssl.SupportedGroupsExtension;

/* loaded from: jsse.jar:sun/security/ssl/DHClientKeyExchange.class */
final class DHClientKeyExchange {
    static final DHClientKeyExchangeConsumer dhHandshakeConsumer = new DHClientKeyExchangeConsumer();
    static final DHClientKeyExchangeProducer dhHandshakeProducer = new DHClientKeyExchangeProducer();

    DHClientKeyExchange() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/DHClientKeyExchange$DHClientKeyExchangeMessage.class */
    private static final class DHClientKeyExchangeMessage extends SSLHandshake.HandshakeMessage {

        /* renamed from: y, reason: collision with root package name */
        private byte[] f13661y;

        DHClientKeyExchangeMessage(HandshakeContext handshakeContext) throws IOException {
            super(handshakeContext);
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) handshakeContext;
            DHKeyExchange.DHEPossession dHEPossession = null;
            Iterator<SSLPossession> it = clientHandshakeContext.handshakePossessions.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLPossession next = it.next();
                if (next instanceof DHKeyExchange.DHEPossession) {
                    dHEPossession = (DHKeyExchange.DHEPossession) next;
                    break;
                }
            }
            if (dHEPossession == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "No DHE credentials negotiated for client key exchange");
            }
            DHPublicKey dHPublicKey = dHEPossession.publicKey;
            dHPublicKey.getParams();
            this.f13661y = Utilities.toByteArray(dHPublicKey.getY());
        }

        DHClientKeyExchangeMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            super(handshakeContext);
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) handshakeContext;
            if (byteBuffer.remaining() < 3) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Invalid DH ClientKeyExchange message: insufficient data");
            }
            this.f13661y = Record.getBytes16(byteBuffer);
            if (byteBuffer.hasRemaining()) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Invalid DH ClientKeyExchange message: unknown extra data");
            }
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public SSLHandshake handshakeType() {
            return SSLHandshake.CLIENT_KEY_EXCHANGE;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public int messageLength() {
            return this.f13661y.length + 2;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public void send(HandshakeOutStream handshakeOutStream) throws IOException {
            handshakeOutStream.putBytes16(this.f13661y);
        }

        public String toString() {
            return new MessageFormat("\"DH ClientKeyExchange\": '{'\n  \"parameters\": '{'\n    \"dh_Yc\": '{'\n{0}\n    '}',\n  '}'\n'}'", Locale.ENGLISH).format(new Object[]{Utilities.indent(new HexDumpEncoder().encodeBuffer(this.f13661y), GoToActionDialog.EMPTY_DESTINATION)});
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: jsse.jar:sun/security/ssl/DHClientKeyExchange$DHClientKeyExchangeProducer.class */
    static final class DHClientKeyExchangeProducer implements HandshakeProducer {
        private DHClientKeyExchangeProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            DHKeyExchange.DHECredentials dHECredentials = null;
            Iterator<SSLCredentials> it = clientHandshakeContext.handshakeCredentials.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLCredentials next = it.next();
                if (next instanceof DHKeyExchange.DHECredentials) {
                    dHECredentials = (DHKeyExchange.DHECredentials) next;
                    break;
                }
            }
            if (dHECredentials == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "No DHE credentials negotiated for client key exchange");
            }
            clientHandshakeContext.handshakePossessions.add(new DHKeyExchange.DHEPossession(dHECredentials, clientHandshakeContext.sslContext.getSecureRandom()));
            DHClientKeyExchangeMessage dHClientKeyExchangeMessage = new DHClientKeyExchangeMessage(clientHandshakeContext);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced DH ClientKeyExchange handshake message", dHClientKeyExchangeMessage);
            }
            dHClientKeyExchangeMessage.write(clientHandshakeContext.handshakeOutput);
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

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: jsse.jar:sun/security/ssl/DHClientKeyExchange$DHClientKeyExchangeConsumer.class */
    static final class DHClientKeyExchangeConsumer implements SSLConsumer {
        private DHClientKeyExchangeConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            DHKeyExchange.DHEPossession dHEPossession = null;
            Iterator<SSLPossession> it = serverHandshakeContext.handshakePossessions.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLPossession next = it.next();
                if (next instanceof DHKeyExchange.DHEPossession) {
                    dHEPossession = (DHKeyExchange.DHEPossession) next;
                    break;
                }
            }
            if (dHEPossession == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "No expected DHE possessions for client key exchange");
            }
            SSLKeyExchange sSLKeyExchangeValueOf = SSLKeyExchange.valueOf(serverHandshakeContext.negotiatedCipherSuite.keyExchange, serverHandshakeContext.negotiatedProtocol);
            if (sSLKeyExchangeValueOf == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key exchange type");
            }
            DHClientKeyExchangeMessage dHClientKeyExchangeMessage = new DHClientKeyExchangeMessage(serverHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming DH ClientKeyExchange handshake message", dHClientKeyExchangeMessage);
            }
            try {
                DHParameterSpec params = dHEPossession.publicKey.getParams();
                DHPublicKey dHPublicKey = (DHPublicKey) JsseJce.getKeyFactory("DiffieHellman").generatePublic(new DHPublicKeySpec(new BigInteger(1, dHClientKeyExchangeMessage.f13661y), params.getP(), params.getG()));
                if (!serverHandshakeContext.algorithmConstraints.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), dHPublicKey)) {
                    throw new SSLHandshakeException("DHPublicKey does not comply to algorithm constraints");
                }
                serverHandshakeContext.handshakeCredentials.add(new DHKeyExchange.DHECredentials(dHPublicKey, SupportedGroupsExtension.NamedGroup.valueOf(params)));
                SecretKey secretKeyDeriveKey = sSLKeyExchangeValueOf.createKeyDerivation(serverHandshakeContext).deriveKey("MasterSecret", null);
                serverHandshakeContext.handshakeSession.setMasterSecret(secretKeyDeriveKey);
                SSLTrafficKeyDerivation sSLTrafficKeyDerivationValueOf = SSLTrafficKeyDerivation.valueOf(serverHandshakeContext.negotiatedProtocol);
                if (sSLTrafficKeyDerivationValueOf == null) {
                    throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key derivation: " + ((Object) serverHandshakeContext.negotiatedProtocol));
                }
                serverHandshakeContext.handshakeKeyDerivation = sSLTrafficKeyDerivationValueOf.createKeyDerivation(serverHandshakeContext, secretKeyDeriveKey);
            } catch (IOException | GeneralSecurityException e2) {
                throw ((SSLHandshakeException) new SSLHandshakeException("Could not generate DHPublicKey").initCause(e2));
            }
        }
    }
}
