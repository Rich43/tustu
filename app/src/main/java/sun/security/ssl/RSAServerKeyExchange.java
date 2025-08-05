package sun.security.ssl;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.CryptoPrimitive;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Locale;
import org.icepdf.ri.common.utility.annotation.GoToActionDialog;
import sun.misc.HexDumpEncoder;
import sun.security.ssl.RSAKeyExchange;
import sun.security.ssl.SSLHandshake;
import sun.security.ssl.X509Authentication;

/* loaded from: jsse.jar:sun/security/ssl/RSAServerKeyExchange.class */
final class RSAServerKeyExchange {
    static final SSLConsumer rsaHandshakeConsumer = new RSAServerKeyExchangeConsumer();
    static final HandshakeProducer rsaHandshakeProducer = new RSAServerKeyExchangeProducer();

    RSAServerKeyExchange() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/RSAServerKeyExchange$RSAServerKeyExchangeMessage.class */
    private static final class RSAServerKeyExchangeMessage extends SSLHandshake.HandshakeMessage {
        private final byte[] modulus;
        private final byte[] exponent;
        private final byte[] paramsSignature;

        private RSAServerKeyExchangeMessage(HandshakeContext handshakeContext, X509Authentication.X509Possession x509Possession, RSAKeyExchange.EphemeralRSAPossession ephemeralRSAPossession) throws IOException {
            super(handshakeContext);
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) handshakeContext;
            RSAPublicKeySpec rSAPublicKeySpec = JsseJce.getRSAPublicKeySpec(ephemeralRSAPossession.popPublicKey);
            this.modulus = Utilities.toByteArray(rSAPublicKeySpec.getModulus());
            this.exponent = Utilities.toByteArray(rSAPublicKeySpec.getPublicExponent());
            try {
                Signature rSASignature = RSASignature.getInstance();
                rSASignature.initSign(x509Possession.popPrivateKey, serverHandshakeContext.sslContext.getSecureRandom());
                updateSignature(rSASignature, serverHandshakeContext.clientHelloRandom.randomBytes, serverHandshakeContext.serverHelloRandom.randomBytes);
                this.paramsSignature = rSASignature.sign();
            } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Failed to sign ephemeral RSA parameters", e2);
            }
        }

        RSAServerKeyExchangeMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            super(handshakeContext);
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) handshakeContext;
            this.modulus = Record.getBytes16(byteBuffer);
            this.exponent = Record.getBytes16(byteBuffer);
            this.paramsSignature = Record.getBytes16(byteBuffer);
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
                throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "No RSA credentials negotiated for server key exchange");
            }
            try {
                Signature rSASignature = RSASignature.getInstance();
                rSASignature.initVerify(x509Credentials.popPublicKey);
                updateSignature(rSASignature, clientHandshakeContext.clientHelloRandom.randomBytes, clientHandshakeContext.serverHelloRandom.randomBytes);
                if (!rSASignature.verify(this.paramsSignature)) {
                    throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Invalid signature of RSA ServerKeyExchange message");
                }
            } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Failed to sign ephemeral RSA parameters", e2);
            }
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        SSLHandshake handshakeType() {
            return SSLHandshake.SERVER_KEY_EXCHANGE;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        int messageLength() {
            return 6 + this.modulus.length + this.exponent.length + this.paramsSignature.length;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        void send(HandshakeOutStream handshakeOutStream) throws IOException {
            handshakeOutStream.putBytes16(this.modulus);
            handshakeOutStream.putBytes16(this.exponent);
            handshakeOutStream.putBytes16(this.paramsSignature);
        }

        public String toString() {
            MessageFormat messageFormat = new MessageFormat("\"RSA ServerKeyExchange\": '{'\n  \"parameters\": '{'\n    \"rsa_modulus\": '{'\n{0}\n    '}',\n    \"rsa_exponent\": '{'\n{1}\n    '}'\n  '}',\n  \"digital signature\":  '{'\n    \"signature\": '{'\n{2}\n    '}',\n  '}'\n'}'", Locale.ENGLISH);
            HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
            return messageFormat.format(new Object[]{Utilities.indent(hexDumpEncoder.encodeBuffer(this.modulus), GoToActionDialog.EMPTY_DESTINATION), Utilities.indent(hexDumpEncoder.encodeBuffer(this.exponent), GoToActionDialog.EMPTY_DESTINATION), Utilities.indent(hexDumpEncoder.encodeBuffer(this.paramsSignature), GoToActionDialog.EMPTY_DESTINATION)});
        }

        private void updateSignature(Signature signature, byte[] bArr, byte[] bArr2) throws SignatureException {
            signature.update(bArr);
            signature.update(bArr2);
            signature.update((byte) (this.modulus.length >> 8));
            signature.update((byte) (this.modulus.length & 255));
            signature.update(this.modulus);
            signature.update((byte) (this.exponent.length >> 8));
            signature.update((byte) (this.exponent.length & 255));
            signature.update(this.exponent);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/RSAServerKeyExchange$RSAServerKeyExchangeProducer.class */
    private static final class RSAServerKeyExchangeProducer implements HandshakeProducer {
        private RSAServerKeyExchangeProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            RSAKeyExchange.EphemeralRSAPossession ephemeralRSAPossession = null;
            X509Authentication.X509Possession x509Possession = null;
            for (SSLPossession sSLPossession : serverHandshakeContext.handshakePossessions) {
                if (sSLPossession instanceof RSAKeyExchange.EphemeralRSAPossession) {
                    ephemeralRSAPossession = (RSAKeyExchange.EphemeralRSAPossession) sSLPossession;
                    if (x509Possession != null) {
                        break;
                    }
                } else if (sSLPossession instanceof X509Authentication.X509Possession) {
                    x509Possession = (X509Authentication.X509Possession) sSLPossession;
                    if (ephemeralRSAPossession != null) {
                        break;
                    }
                } else {
                    continue;
                }
            }
            if (ephemeralRSAPossession == null) {
                return null;
            }
            if (x509Possession == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "No RSA certificate negotiated for server key exchange");
            }
            if (!"RSA".equals(x509Possession.popPrivateKey.getAlgorithm())) {
                throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "No X.509 possession can be used for ephemeral RSA ServerKeyExchange");
            }
            RSAServerKeyExchangeMessage rSAServerKeyExchangeMessage = new RSAServerKeyExchangeMessage(serverHandshakeContext, x509Possession, ephemeralRSAPossession);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced RSA ServerKeyExchange handshake message", rSAServerKeyExchangeMessage);
            }
            rSAServerKeyExchangeMessage.write(serverHandshakeContext.handshakeOutput);
            serverHandshakeContext.handshakeOutput.flush();
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/RSAServerKeyExchange$RSAServerKeyExchangeConsumer.class */
    private static final class RSAServerKeyExchangeConsumer implements SSLConsumer {
        private RSAServerKeyExchangeConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            RSAServerKeyExchangeMessage rSAServerKeyExchangeMessage = new RSAServerKeyExchangeMessage(clientHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming RSA ServerKeyExchange handshake message", rSAServerKeyExchangeMessage);
            }
            try {
                RSAPublicKey rSAPublicKey = (RSAPublicKey) JsseJce.getKeyFactory("RSA").generatePublic(new RSAPublicKeySpec(new BigInteger(1, rSAServerKeyExchangeMessage.modulus), new BigInteger(1, rSAServerKeyExchangeMessage.exponent)));
                if (!clientHandshakeContext.algorithmConstraints.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), rSAPublicKey)) {
                    throw clientHandshakeContext.conContext.fatal(Alert.INSUFFICIENT_SECURITY, "RSA ServerKeyExchange does not comply to algorithm constraints");
                }
                clientHandshakeContext.handshakeCredentials.add(new RSAKeyExchange.EphemeralRSACredentials(rSAPublicKey));
            } catch (GeneralSecurityException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.INSUFFICIENT_SECURITY, "Could not generate RSAPublicKey", e2);
            }
        }
    }
}
