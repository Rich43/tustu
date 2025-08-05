package sun.security.ssl;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.CryptoPrimitive;
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
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;
import org.icepdf.ri.common.utility.annotation.GoToActionDialog;
import sun.misc.HexDumpEncoder;
import sun.security.ssl.DHKeyExchange;
import sun.security.ssl.SSLHandshake;
import sun.security.ssl.SupportedGroupsExtension;
import sun.security.ssl.X509Authentication;
import sun.security.util.KeyUtil;

/* loaded from: jsse.jar:sun/security/ssl/DHServerKeyExchange.class */
final class DHServerKeyExchange {
    static final SSLConsumer dhHandshakeConsumer = new DHServerKeyExchangeConsumer();
    static final HandshakeProducer dhHandshakeProducer = new DHServerKeyExchangeProducer();

    DHServerKeyExchange() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/DHServerKeyExchange$DHServerKeyExchangeMessage.class */
    private static final class DHServerKeyExchangeMessage extends SSLHandshake.HandshakeMessage {

        /* renamed from: p, reason: collision with root package name */
        private final byte[] f13662p;

        /* renamed from: g, reason: collision with root package name */
        private final byte[] f13663g;

        /* renamed from: y, reason: collision with root package name */
        private final byte[] f13664y;
        private final boolean useExplicitSigAlgorithm;
        private final SignatureScheme signatureScheme;
        private final byte[] paramsSignature;

        DHServerKeyExchangeMessage(HandshakeContext handshakeContext) throws IOException {
            Signature signature;
            super(handshakeContext);
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) handshakeContext;
            DHKeyExchange.DHEPossession dHEPossession = null;
            X509Authentication.X509Possession x509Possession = null;
            for (SSLPossession sSLPossession : serverHandshakeContext.handshakePossessions) {
                if (sSLPossession instanceof DHKeyExchange.DHEPossession) {
                    dHEPossession = (DHKeyExchange.DHEPossession) sSLPossession;
                    if (x509Possession != null) {
                        break;
                    }
                } else if (sSLPossession instanceof X509Authentication.X509Possession) {
                    x509Possession = (X509Authentication.X509Possession) sSLPossession;
                    if (dHEPossession != null) {
                        break;
                    }
                } else {
                    continue;
                }
            }
            if (dHEPossession == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "No DHE credentials negotiated for server key exchange");
            }
            DHPublicKey dHPublicKey = dHEPossession.publicKey;
            DHParameterSpec params = dHPublicKey.getParams();
            this.f13662p = Utilities.toByteArray(params.getP());
            this.f13663g = Utilities.toByteArray(params.getG());
            this.f13664y = Utilities.toByteArray(dHPublicKey.getY());
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
                updateSignature(signature, serverHandshakeContext.clientHelloRandom.randomBytes, serverHandshakeContext.serverHelloRandom.randomBytes);
                this.paramsSignature = signature.sign();
            } catch (SignatureException e3) {
                throw serverHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Failed to sign dhe parameters: " + x509Possession.popPrivateKey.getAlgorithm(), e3);
            }
        }

        DHServerKeyExchangeMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            Signature signature;
            super(handshakeContext);
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) handshakeContext;
            this.f13662p = Record.getBytes16(byteBuffer);
            this.f13663g = Record.getBytes16(byteBuffer);
            this.f13664y = Record.getBytes16(byteBuffer);
            try {
                KeyUtil.validate(new DHPublicKeySpec(new BigInteger(1, this.f13664y), new BigInteger(1, this.f13662p), new BigInteger(1, this.f13662p)));
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
                    int int16 = Record.getInt16(byteBuffer);
                    this.signatureScheme = SignatureScheme.valueOf(int16);
                    if (this.signatureScheme == null) {
                        throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Invalid signature algorithm (" + int16 + ") used in DH ServerKeyExchange handshake message");
                    }
                    if (!clientHandshakeContext.localSupportedSignAlgs.contains(this.signatureScheme)) {
                        throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Unsupported signature algorithm (" + this.signatureScheme.name + ") used in DH ServerKeyExchange handshake message");
                    }
                } else {
                    this.signatureScheme = null;
                }
                this.paramsSignature = Record.getBytes16(byteBuffer);
                if (this.useExplicitSigAlgorithm) {
                    try {
                        signature = this.signatureScheme.getVerifier(x509Credentials.popPublicKey);
                    } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException e2) {
                        throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Unsupported signature algorithm: " + this.signatureScheme.name, e2);
                    }
                } else {
                    try {
                        signature = getSignature(x509Credentials.popPublicKey.getAlgorithm(), x509Credentials.popPublicKey);
                    } catch (InvalidKeyException | NoSuchAlgorithmException e3) {
                        throw clientHandshakeContext.conContext.fatal(Alert.INTERNAL_ERROR, "Unsupported signature algorithm: " + x509Credentials.popPublicKey.getAlgorithm(), e3);
                    }
                }
                try {
                    updateSignature(signature, clientHandshakeContext.clientHelloRandom.randomBytes, clientHandshakeContext.serverHelloRandom.randomBytes);
                    if (!signature.verify(this.paramsSignature)) {
                        throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Invalid signature on DH ServerKeyExchange message");
                    }
                } catch (SignatureException e4) {
                    throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Cannot verify DH ServerKeyExchange signature", e4);
                }
            } catch (InvalidKeyException e5) {
                throw clientHandshakeContext.conContext.fatal(Alert.HANDSHAKE_FAILURE, "Invalid DH ServerKeyExchange: invalid parameters", e5);
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
            return 6 + this.f13662p.length + this.f13663g.length + this.f13664y.length + length;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public void send(HandshakeOutStream handshakeOutStream) throws IOException {
            handshakeOutStream.putBytes16(this.f13662p);
            handshakeOutStream.putBytes16(this.f13663g);
            handshakeOutStream.putBytes16(this.f13664y);
            if (this.paramsSignature != null) {
                if (this.useExplicitSigAlgorithm) {
                    handshakeOutStream.putInt16(this.signatureScheme.id);
                }
                handshakeOutStream.putBytes16(this.paramsSignature);
            }
        }

        public String toString() {
            if (this.paramsSignature == null) {
                MessageFormat messageFormat = new MessageFormat("\"DH ServerKeyExchange\": '{'\n  \"parameters\": '{'\n    \"dh_p\": '{'\n{0}\n    '}',\n    \"dh_g\": '{'\n{1}\n    '}',\n    \"dh_Ys\": '{'\n{2}\n    '}',\n  '}'\n'}'", Locale.ENGLISH);
                HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
                return messageFormat.format(new Object[]{Utilities.indent(hexDumpEncoder.encodeBuffer(this.f13662p), GoToActionDialog.EMPTY_DESTINATION), Utilities.indent(hexDumpEncoder.encodeBuffer(this.f13663g), GoToActionDialog.EMPTY_DESTINATION), Utilities.indent(hexDumpEncoder.encodeBuffer(this.f13664y), GoToActionDialog.EMPTY_DESTINATION)});
            }
            if (this.useExplicitSigAlgorithm) {
                MessageFormat messageFormat2 = new MessageFormat("\"DH ServerKeyExchange\": '{'\n  \"parameters\": '{'\n    \"dh_p\": '{'\n{0}\n    '}',\n    \"dh_g\": '{'\n{1}\n    '}',\n    \"dh_Ys\": '{'\n{2}\n    '}',\n  '}',\n  \"digital signature\":  '{'\n    \"signature algorithm\": \"{3}\"\n    \"signature\": '{'\n{4}\n    '}',\n  '}'\n'}'", Locale.ENGLISH);
                HexDumpEncoder hexDumpEncoder2 = new HexDumpEncoder();
                return messageFormat2.format(new Object[]{Utilities.indent(hexDumpEncoder2.encodeBuffer(this.f13662p), GoToActionDialog.EMPTY_DESTINATION), Utilities.indent(hexDumpEncoder2.encodeBuffer(this.f13663g), GoToActionDialog.EMPTY_DESTINATION), Utilities.indent(hexDumpEncoder2.encodeBuffer(this.f13664y), GoToActionDialog.EMPTY_DESTINATION), this.signatureScheme.name, Utilities.indent(hexDumpEncoder2.encodeBuffer(this.paramsSignature), GoToActionDialog.EMPTY_DESTINATION)});
            }
            MessageFormat messageFormat3 = new MessageFormat("\"DH ServerKeyExchange\": '{'\n  \"parameters\": '{'\n    \"dh_p\": '{'\n{0}\n    '}',\n    \"dh_g\": '{'\n{1}\n    '}',\n    \"dh_Ys\": '{'\n{2}\n    '}',\n  '}',\n  \"signature\": '{'\n{3}\n  '}'\n'}'", Locale.ENGLISH);
            HexDumpEncoder hexDumpEncoder3 = new HexDumpEncoder();
            return messageFormat3.format(new Object[]{Utilities.indent(hexDumpEncoder3.encodeBuffer(this.f13662p), GoToActionDialog.EMPTY_DESTINATION), Utilities.indent(hexDumpEncoder3.encodeBuffer(this.f13663g), GoToActionDialog.EMPTY_DESTINATION), Utilities.indent(hexDumpEncoder3.encodeBuffer(this.f13664y), GoToActionDialog.EMPTY_DESTINATION), Utilities.indent(hexDumpEncoder3.encodeBuffer(this.paramsSignature), "    ")});
        }

        private static Signature getSignature(String str, Key key) throws NoSuchAlgorithmException, InvalidKeyException {
            Signature rSASignature;
            switch (str) {
                case "DSA":
                    rSASignature = JsseJce.getSignature("DSA");
                    break;
                case "RSA":
                    rSASignature = RSASignature.getInstance();
                    break;
                default:
                    throw new NoSuchAlgorithmException("neither an RSA or a DSA key : " + str);
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

        private void updateSignature(Signature signature, byte[] bArr, byte[] bArr2) throws SignatureException {
            signature.update(bArr);
            signature.update(bArr2);
            signature.update((byte) (this.f13662p.length >> 8));
            signature.update((byte) (this.f13662p.length & 255));
            signature.update(this.f13662p);
            signature.update((byte) (this.f13663g.length >> 8));
            signature.update((byte) (this.f13663g.length & 255));
            signature.update(this.f13663g);
            signature.update((byte) (this.f13664y.length >> 8));
            signature.update((byte) (this.f13664y.length & 255));
            signature.update(this.f13664y);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/DHServerKeyExchange$DHServerKeyExchangeProducer.class */
    static final class DHServerKeyExchangeProducer implements HandshakeProducer {
        private DHServerKeyExchangeProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            DHServerKeyExchangeMessage dHServerKeyExchangeMessage = new DHServerKeyExchangeMessage(serverHandshakeContext);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced DH ServerKeyExchange handshake message", dHServerKeyExchangeMessage);
            }
            dHServerKeyExchangeMessage.write(serverHandshakeContext.handshakeOutput);
            serverHandshakeContext.handshakeOutput.flush();
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/DHServerKeyExchange$DHServerKeyExchangeConsumer.class */
    static final class DHServerKeyExchangeConsumer implements SSLConsumer {
        private DHServerKeyExchangeConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            DHServerKeyExchangeMessage dHServerKeyExchangeMessage = new DHServerKeyExchangeMessage(clientHandshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming DH ServerKeyExchange handshake message", dHServerKeyExchangeMessage);
            }
            try {
                DHPublicKey dHPublicKey = (DHPublicKey) JsseJce.getKeyFactory("DiffieHellman").generatePublic(new DHPublicKeySpec(new BigInteger(1, dHServerKeyExchangeMessage.f13664y), new BigInteger(1, dHServerKeyExchangeMessage.f13662p), new BigInteger(1, dHServerKeyExchangeMessage.f13663g)));
                if (!clientHandshakeContext.algorithmConstraints.permits(EnumSet.of(CryptoPrimitive.KEY_AGREEMENT), dHPublicKey)) {
                    throw clientHandshakeContext.conContext.fatal(Alert.INSUFFICIENT_SECURITY, "DH ServerKeyExchange does not comply to algorithm constraints");
                }
                clientHandshakeContext.handshakeCredentials.add(new DHKeyExchange.DHECredentials(dHPublicKey, SupportedGroupsExtension.NamedGroup.valueOf(dHPublicKey.getParams())));
            } catch (GeneralSecurityException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.INSUFFICIENT_SECURITY, "Could not generate DHPublicKey", e2);
            }
        }
    }
}
