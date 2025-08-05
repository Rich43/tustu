package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Locale;
import javax.crypto.SecretKey;
import sun.misc.HexDumpEncoder;
import sun.security.ssl.RSAKeyExchange;
import sun.security.ssl.SSLHandshake;
import sun.security.ssl.X509Authentication;

/* loaded from: jsse.jar:sun/security/ssl/RSAClientKeyExchange.class */
final class RSAClientKeyExchange {
    static final SSLConsumer rsaHandshakeConsumer = new RSAClientKeyExchangeConsumer();
    static final HandshakeProducer rsaHandshakeProducer = new RSAClientKeyExchangeProducer();

    RSAClientKeyExchange() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/RSAClientKeyExchange$RSAClientKeyExchangeMessage.class */
    private static final class RSAClientKeyExchangeMessage extends SSLHandshake.HandshakeMessage {
        final int protocolVersion;
        final boolean useTLS10PlusSpec;
        final byte[] encrypted;

        RSAClientKeyExchangeMessage(HandshakeContext handshakeContext, RSAKeyExchange.RSAPremasterSecret rSAPremasterSecret, PublicKey publicKey) throws GeneralSecurityException {
            super(handshakeContext);
            this.protocolVersion = handshakeContext.clientHelloVersion;
            this.encrypted = rSAPremasterSecret.getEncoded(publicKey, handshakeContext.sslContext.getSecureRandom());
            this.useTLS10PlusSpec = ProtocolVersion.useTLS10PlusSpec(this.protocolVersion);
        }

        RSAClientKeyExchangeMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            super(handshakeContext);
            if (byteBuffer.remaining() < 2) {
                throw handshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Invalid RSA ClientKeyExchange message: insufficient data");
            }
            this.protocolVersion = handshakeContext.clientHelloVersion;
            this.useTLS10PlusSpec = ProtocolVersion.useTLS10PlusSpec(this.protocolVersion);
            if (this.useTLS10PlusSpec) {
                this.encrypted = Record.getBytes16(byteBuffer);
            } else {
                this.encrypted = new byte[byteBuffer.remaining()];
                byteBuffer.get(this.encrypted);
            }
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public SSLHandshake handshakeType() {
            return SSLHandshake.CLIENT_KEY_EXCHANGE;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public int messageLength() {
            if (this.useTLS10PlusSpec) {
                return this.encrypted.length + 2;
            }
            return this.encrypted.length;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public void send(HandshakeOutStream handshakeOutStream) throws IOException {
            if (this.useTLS10PlusSpec) {
                handshakeOutStream.putBytes16(this.encrypted);
            } else {
                handshakeOutStream.write(this.encrypted);
            }
        }

        public String toString() {
            return new MessageFormat("\"RSA ClientKeyExchange\": '{'\n  \"client_version\":  {0}\n  \"encncrypted\": '{'\n{1}\n  '}'\n'}'", Locale.ENGLISH).format(new Object[]{ProtocolVersion.nameOf(this.protocolVersion), Utilities.indent(new HexDumpEncoder().encodeBuffer(this.encrypted), "    ")});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/RSAClientKeyExchange$RSAClientKeyExchangeProducer.class */
    private static final class RSAClientKeyExchangeProducer implements HandshakeProducer {
        private RSAClientKeyExchangeProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            RSAKeyExchange.EphemeralRSACredentials ephemeralRSACredentials = null;
            X509Authentication.X509Credentials x509Credentials = null;
            for (SSLCredentials sSLCredentials : clientHandshakeContext.handshakeCredentials) {
                if (sSLCredentials instanceof RSAKeyExchange.EphemeralRSACredentials) {
                    ephemeralRSACredentials = (RSAKeyExchange.EphemeralRSACredentials) sSLCredentials;
                    if (x509Credentials != null) {
                        break;
                    }
                } else if (sSLCredentials instanceof X509Authentication.X509Credentials) {
                    x509Credentials = (X509Authentication.X509Credentials) sSLCredentials;
                    if (ephemeralRSACredentials != null) {
                        break;
                    }
                } else {
                    continue;
                }
            }
            if (ephemeralRSACredentials == null && x509Credentials == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "No RSA credentials negotiated for client key exchange");
            }
            PublicKey publicKey = ephemeralRSACredentials != null ? ephemeralRSACredentials.popPublicKey : x509Credentials.popPublicKey;
            if (!publicKey.getAlgorithm().equals("RSA")) {
                throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Not RSA public key for client key exchange");
            }
            try {
                RSAKeyExchange.RSAPremasterSecret rSAPremasterSecretCreatePremasterSecret = RSAKeyExchange.RSAPremasterSecret.createPremasterSecret(clientHandshakeContext);
                clientHandshakeContext.handshakePossessions.add(rSAPremasterSecretCreatePremasterSecret);
                RSAClientKeyExchangeMessage rSAClientKeyExchangeMessage = new RSAClientKeyExchangeMessage(clientHandshakeContext, rSAPremasterSecretCreatePremasterSecret, publicKey);
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Produced RSA ClientKeyExchange handshake message", rSAClientKeyExchangeMessage);
                }
                rSAClientKeyExchangeMessage.write(clientHandshakeContext.handshakeOutput);
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
            } catch (GeneralSecurityException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Cannot generate RSA premaster secret", e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/RSAClientKeyExchange$RSAClientKeyExchangeConsumer.class */
    private static final class RSAClientKeyExchangeConsumer implements SSLConsumer {
        private RSAClientKeyExchangeConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            RSAKeyExchange.EphemeralRSAPossession ephemeralRSAPossession = null;
            X509Authentication.X509Possession x509Possession = null;
            Iterator<SSLPossession> it = serverHandshakeContext.handshakePossessions.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLPossession next = it.next();
                if (next instanceof RSAKeyExchange.EphemeralRSAPossession) {
                    ephemeralRSAPossession = (RSAKeyExchange.EphemeralRSAPossession) next;
                    break;
                } else if (next instanceof X509Authentication.X509Possession) {
                    x509Possession = (X509Authentication.X509Possession) next;
                    if (0 != 0) {
                        break;
                    }
                }
            }
            if (ephemeralRSAPossession == null && x509Possession == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "No RSA possessions negotiated for client key exchange");
            }
            PrivateKey privateKey = ephemeralRSAPossession != null ? ephemeralRSAPossession.popPrivateKey : x509Possession.popPrivateKey;
            if (!privateKey.getAlgorithm().equals("RSA")) {
                throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Not RSA private key for client key exchange");
            }
            RSAClientKeyExchangeMessage rSAClientKeyExchangeMessage = new RSAClientKeyExchangeMessage(serverHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming RSA ClientKeyExchange handshake message", rSAClientKeyExchangeMessage);
            }
            try {
                serverHandshakeContext.handshakeCredentials.add(RSAKeyExchange.RSAPremasterSecret.decode(serverHandshakeContext, privateKey, rSAClientKeyExchangeMessage.encrypted));
                SSLKeyExchange sSLKeyExchangeValueOf = SSLKeyExchange.valueOf(serverHandshakeContext.negotiatedCipherSuite.keyExchange, serverHandshakeContext.negotiatedProtocol);
                if (sSLKeyExchangeValueOf == null) {
                    throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key exchange type");
                }
                SecretKey secretKeyDeriveKey = sSLKeyExchangeValueOf.createKeyDerivation(serverHandshakeContext).deriveKey("MasterSecret", null);
                serverHandshakeContext.handshakeSession.setMasterSecret(secretKeyDeriveKey);
                SSLTrafficKeyDerivation sSLTrafficKeyDerivationValueOf = SSLTrafficKeyDerivation.valueOf(serverHandshakeContext.negotiatedProtocol);
                if (sSLTrafficKeyDerivationValueOf == null) {
                    throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Not supported key derivation: " + ((Object) serverHandshakeContext.negotiatedProtocol));
                }
                serverHandshakeContext.handshakeKeyDerivation = sSLTrafficKeyDerivationValueOf.createKeyDerivation(serverHandshakeContext, secretKeyDeriveKey);
            } catch (GeneralSecurityException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Cannot decode RSA premaster secret", e2);
            }
        }
    }
}
