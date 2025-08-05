package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.CryptoPrimitive;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.icepdf.ri.common.utility.annotation.GoToActionDialog;
import sun.misc.HexDumpEncoder;
import sun.security.ssl.ECDHKeyExchange;
import sun.security.ssl.SSLHandshake;
import sun.security.ssl.SupportedGroupsExtension;
import sun.security.ssl.X509Authentication;

/* loaded from: jsse.jar:sun/security/ssl/ECDHServerKeyExchange.class */
final class ECDHServerKeyExchange {
    static final SSLConsumer ecdheHandshakeConsumer = new ECDHServerKeyExchangeConsumer();
    static final HandshakeProducer ecdheHandshakeProducer = new ECDHServerKeyExchangeProducer();

    ECDHServerKeyExchange() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/ECDHServerKeyExchange$ECDHServerKeyExchangeMessage.class */
    private static final class ECDHServerKeyExchangeMessage extends SSLHandshake.HandshakeMessage {
        private static final byte CURVE_NAMED_CURVE = 3;
        private final SupportedGroupsExtension.NamedGroup namedGroup;
        private final byte[] publicPoint;
        private final byte[] paramsSignature;
        private final ECPublicKey publicKey;
        private final boolean useExplicitSigAlgorithm;
        private final SignatureScheme signatureScheme;

        ECDHServerKeyExchangeMessage(HandshakeContext handshakeContext) throws IOException {
            Signature signature;
            super(handshakeContext);
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) handshakeContext;
            ECDHKeyExchange.ECDHEPossession eCDHEPossession = null;
            X509Authentication.X509Possession x509Possession = null;
            for (SSLPossession sSLPossession : serverHandshakeContext.handshakePossessions) {
                if (sSLPossession instanceof ECDHKeyExchange.ECDHEPossession) {
                    eCDHEPossession = (ECDHKeyExchange.ECDHEPossession) sSLPossession;
                    if (x509Possession != null) {
                        break;
                    }
                } else if (sSLPossession instanceof X509Authentication.X509Possession) {
                    x509Possession = (X509Authentication.X509Possession) sSLPossession;
                    if (eCDHEPossession != null) {
                        break;
                    }
                } else {
                    continue;
                }
            }
            if (eCDHEPossession == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "No ECDHE credentials negotiated for server key exchange");
            }
            this.publicKey = eCDHEPossession.publicKey;
            ECParameterSpec params = this.publicKey.getParams();
            this.publicPoint = JsseJce.encodePoint(this.publicKey.getW(), params.getCurve());
            this.namedGroup = SupportedGroupsExtension.NamedGroup.valueOf(params);
            if (this.namedGroup == null || this.namedGroup.oid == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Unnamed EC parameter spec: " + ((Object) params));
            }
            if (x509Possession == null) {
                this.paramsSignature = null;
                this.signatureScheme = null;
                this.useExplicitSigAlgorithm = false;
                return;
            }
            this.useExplicitSigAlgorithm = serverHandshakeContext.negotiatedProtocol.useTLS12PlusSpec();
            if (this.useExplicitSigAlgorithm) {
                Map.Entry<SignatureScheme, Signature> signerOfPreferableAlgorithm = SignatureScheme.getSignerOfPreferableAlgorithm(serverHandshakeContext.peerRequestedSignatureSchemes, x509Possession, serverHandshakeContext.negotiatedProtocol);
                if (signerOfPreferableAlgorithm == null) {
                    throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "No supported signature algorithm for " + x509Possession.popPrivateKey.getAlgorithm() + "  key");
                }
                this.signatureScheme = signerOfPreferableAlgorithm.getKey();
                signature = signerOfPreferableAlgorithm.getValue();
            } else {
                this.signatureScheme = null;
                try {
                    signature = getSignature(x509Possession.popPrivateKey.getAlgorithm(), x509Possession.popPrivateKey);
                } catch (InvalidKeyException | NoSuchAlgorithmException e2) {
                    throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Unsupported signature algorithm: " + x509Possession.popPrivateKey.getAlgorithm(), e2);
                }
            }
            try {
                updateSignature(signature, serverHandshakeContext.clientHelloRandom.randomBytes, serverHandshakeContext.serverHelloRandom.randomBytes, this.namedGroup.id, this.publicPoint);
                this.paramsSignature = signature.sign();
            } catch (SignatureException e3) {
                throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Failed to sign ecdhe parameters: " + x509Possession.popPrivateKey.getAlgorithm(), e3);
            }
        }

        ECDHServerKeyExchangeMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            Signature verifier;
            super(handshakeContext);
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) handshakeContext;
            byte int8 = (byte) Record.getInt8(byteBuffer);
            if (int8 != 3) {
                throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Unsupported ECCurveType: " + ((int) int8));
            }
            int int16 = Record.getInt16(byteBuffer);
            this.namedGroup = SupportedGroupsExtension.NamedGroup.valueOf(int16);
            if (this.namedGroup == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Unknown named group ID: " + int16);
            }
            if (!SupportedGroupsExtension.SupportedGroups.isSupported(this.namedGroup)) {
                throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Unsupported named group: " + ((Object) this.namedGroup));
            }
            if (this.namedGroup.oid == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Unknown named EC curve: " + ((Object) this.namedGroup));
            }
            ECParameterSpec eCParameterSpec = JsseJce.getECParameterSpec(this.namedGroup.oid);
            if (eCParameterSpec == null) {
                throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "No supported EC parameter: " + ((Object) this.namedGroup));
            }
            this.publicPoint = Record.getBytes8(byteBuffer);
            if (this.publicPoint.length == 0) {
                throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Insufficient ECPoint data: " + ((Object) this.namedGroup));
            }
            try {
                this.publicKey = (ECPublicKey) JsseJce.getKeyFactory("EC").generatePublic(new ECPublicKeySpec(JsseJce.decodePoint(this.publicPoint, eCParameterSpec.getCurve()), eCParameterSpec));
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
                    if (byteBuffer.hasRemaining()) {
                        throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Invalid DH ServerKeyExchange: unknown extra data");
                    }
                    this.signatureScheme = null;
                    this.paramsSignature = null;
                    this.useExplicitSigAlgorithm = false;
                    return;
                }
                this.useExplicitSigAlgorithm = clientHandshakeContext.negotiatedProtocol.useTLS12PlusSpec();
                if (this.useExplicitSigAlgorithm) {
                    int int162 = Record.getInt16(byteBuffer);
                    this.signatureScheme = SignatureScheme.valueOf(int162);
                    if (this.signatureScheme == null) {
                        throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Invalid signature algorithm (" + int162 + ") used in ECDH ServerKeyExchange handshake message");
                    }
                    if (!clientHandshakeContext.localSupportedSignAlgs.contains(this.signatureScheme)) {
                        throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Unsupported signature algorithm (" + this.signatureScheme.name + ") used in ECDH ServerKeyExchange handshake message");
                    }
                } else {
                    this.signatureScheme = null;
                }
                this.paramsSignature = Record.getBytes16(byteBuffer);
                if (this.useExplicitSigAlgorithm) {
                    try {
                        verifier = this.signatureScheme.getVerifier(x509Credentials.popPublicKey);
                    } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException e2) {
                        throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Unsupported signature algorithm: " + this.signatureScheme.name, e2);
                    }
                } else {
                    try {
                        verifier = getSignature(x509Credentials.popPublicKey.getAlgorithm(), x509Credentials.popPublicKey);
                    } catch (InvalidKeyException | NoSuchAlgorithmException e3) {
                        throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Unsupported signature algorithm: " + x509Credentials.popPublicKey.getAlgorithm(), e3);
                    }
                }
                try {
                    updateSignature(verifier, clientHandshakeContext.clientHelloRandom.randomBytes, clientHandshakeContext.serverHelloRandom.randomBytes, this.namedGroup.id, this.publicPoint);
                    if (!verifier.verify(this.paramsSignature)) {
                        throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Invalid ECDH ServerKeyExchange signature");
                    }
                } catch (SignatureException e4) {
                    throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Cannot verify ECDH ServerKeyExchange signature", e4);
                }
            } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e5) {
                throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid ECPoint: " + ((Object) this.namedGroup), e5);
            }
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public SSLHandshake handshakeType() {
            return SSLHandshake.SERVER_KEY_EXCHANGE;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public int messageLength() {
            int length = 0;
            if (this.paramsSignature != null) {
                length = 2 + this.paramsSignature.length;
                if (this.useExplicitSigAlgorithm) {
                    length += SignatureScheme.sizeInRecord();
                }
            }
            return 4 + this.publicPoint.length + length;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public void send(HandshakeOutStream handshakeOutStream) throws IOException {
            handshakeOutStream.putInt8(3);
            handshakeOutStream.putInt16(this.namedGroup.id);
            handshakeOutStream.putBytes8(this.publicPoint);
            if (this.paramsSignature != null) {
                if (this.useExplicitSigAlgorithm) {
                    handshakeOutStream.putInt16(this.signatureScheme.id);
                }
                handshakeOutStream.putBytes16(this.paramsSignature);
            }
        }

        public String toString() {
            if (this.useExplicitSigAlgorithm) {
                MessageFormat messageFormat = new MessageFormat("\"ECDH ServerKeyExchange\": '{'\n  \"parameters\": '{'\n    \"named group\": \"{0}\"\n    \"ecdh public\": '{'\n{1}\n    '}',\n  '}',\n  \"digital signature\":  '{'\n    \"signature algorithm\": \"{2}\"\n    \"signature\": '{'\n{3}\n    '}',\n  '}'\n'}'", Locale.ENGLISH);
                HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
                return messageFormat.format(new Object[]{this.namedGroup.name, Utilities.indent(hexDumpEncoder.encodeBuffer(this.publicPoint), GoToActionDialog.EMPTY_DESTINATION), this.signatureScheme.name, Utilities.indent(hexDumpEncoder.encodeBuffer(this.paramsSignature), GoToActionDialog.EMPTY_DESTINATION)});
            }
            if (this.paramsSignature != null) {
                MessageFormat messageFormat2 = new MessageFormat("\"ECDH ServerKeyExchange\": '{'\n  \"parameters\":  '{'\n    \"named group\": \"{0}\"\n    \"ecdh public\": '{'\n{1}\n    '}',\n  '}',\n  \"signature\": '{'\n{2}\n  '}'\n'}'", Locale.ENGLISH);
                HexDumpEncoder hexDumpEncoder2 = new HexDumpEncoder();
                return messageFormat2.format(new Object[]{this.namedGroup.name, Utilities.indent(hexDumpEncoder2.encodeBuffer(this.publicPoint), GoToActionDialog.EMPTY_DESTINATION), Utilities.indent(hexDumpEncoder2.encodeBuffer(this.paramsSignature), "    ")});
            }
            return new MessageFormat("\"ECDH ServerKeyExchange\": '{'\n  \"parameters\":  '{'\n    \"named group\": \"{0}\"\n    \"ecdh public\": '{'\n{1}\n    '}',\n  '}'\n'}'", Locale.ENGLISH).format(new Object[]{this.namedGroup.name, Utilities.indent(new HexDumpEncoder().encodeBuffer(this.publicPoint), GoToActionDialog.EMPTY_DESTINATION)});
        }

        private static Signature getSignature(String str, Key key) throws NoSuchAlgorithmException, InvalidKeyException {
            Signature rSASignature;
            switch (str) {
                case "EC":
                    rSASignature = JsseJce.getSignature("SHA1withECDSA");
                    break;
                case "RSA":
                    rSASignature = RSASignature.getInstance();
                    break;
                default:
                    throw new NoSuchAlgorithmException("neither an RSA or a EC key : " + str);
            }
            if (rSASignature != null) {
                if (key instanceof PublicKey) {
                    rSASignature.initVerify((PublicKey) key);
                } else {
                    rSASignature.initSign((PrivateKey) key);
                }
            }
            return rSASignature;
        }

        private static void updateSignature(Signature signature, byte[] bArr, byte[] bArr2, int i2, byte[] bArr3) throws SignatureException {
            signature.update(bArr);
            signature.update(bArr2);
            signature.update((byte) 3);
            signature.update((byte) ((i2 >> 8) & 255));
            signature.update((byte) (i2 & 255));
            signature.update((byte) bArr3.length);
            signature.update(bArr3);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ECDHServerKeyExchange$ECDHServerKeyExchangeProducer.class */
    private static final class ECDHServerKeyExchangeProducer implements HandshakeProducer {
        private ECDHServerKeyExchangeProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            ECDHServerKeyExchangeMessage eCDHServerKeyExchangeMessage = new ECDHServerKeyExchangeMessage(serverHandshakeContext);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced ECDH ServerKeyExchange handshake message", eCDHServerKeyExchangeMessage);
            }
            eCDHServerKeyExchangeMessage.write(serverHandshakeContext.handshakeOutput);
            serverHandshakeContext.handshakeOutput.flush();
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/ECDHServerKeyExchange$ECDHServerKeyExchangeConsumer.class */
    private static final class ECDHServerKeyExchangeConsumer implements SSLConsumer {
        private ECDHServerKeyExchangeConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            ECDHServerKeyExchangeMessage eCDHServerKeyExchangeMessage = new ECDHServerKeyExchangeMessage(clientHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming ECDH ServerKeyExchange handshake message", eCDHServerKeyExchangeMessage);
            }
            if (clientHandshakeContext.algorithmConstraints != null && !clientHandshakeContext.algorithmConstraints.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), eCDHServerKeyExchangeMessage.publicKey)) {
                throw clientHandshakeContext.conContext.fatal(Alert.INSUFFICIENT_SECURITY, "ECDH ServerKeyExchange does not comply to algorithm constraints");
            }
            clientHandshakeContext.handshakeCredentials.add(new ECDHKeyExchange.ECDHECredentials(eCDHServerKeyExchangeMessage.publicKey, eCDHServerKeyExchangeMessage.namedGroup));
        }
    }
}
