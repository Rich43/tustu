package sun.security.ssl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Locale;
import javax.crypto.SecretKey;
import javax.net.ssl.SNIHostName;
import org.icepdf.ri.common.utility.annotation.GoToActionDialog;
import sun.misc.HexDumpEncoder;
import sun.security.pkcs11.wrapper.Constants;
import sun.security.ssl.KrbKeyExchange;
import sun.security.ssl.SSLHandshake;

/* loaded from: jsse.jar:sun/security/ssl/KrbClientKeyExchange.class */
final class KrbClientKeyExchange {
    static final SSLConsumer krbHandshakeConsumer = new KrbClientKeyExchangeConsumer();
    static final HandshakeProducer krbHandshakeProducer = new KrbClientKeyExchangeProducer();

    KrbClientKeyExchange() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/KrbClientKeyExchange$KrbClientKeyExchangeMessage.class */
    private static final class KrbClientKeyExchangeMessage extends SSLHandshake.HandshakeMessage {
        private static final String KRB5_CLASS_NAME = "sun.security.ssl.krb5.KrbClientKeyExchangeHelperImpl";
        private static final Class<?> krb5Class = (Class) AccessController.doPrivileged(new PrivilegedAction<Class<?>>() { // from class: sun.security.ssl.KrbClientKeyExchange.KrbClientKeyExchangeMessage.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Class<?> run2() {
                try {
                    return Class.forName(KrbClientKeyExchangeMessage.KRB5_CLASS_NAME, true, null);
                } catch (ClassNotFoundException e2) {
                    return null;
                }
            }
        });
        private final KrbClientKeyExchangeHelper krb5Helper;

        private static KrbClientKeyExchangeHelper newKrb5Instance() {
            if (krb5Class != null) {
                try {
                    return (KrbClientKeyExchangeHelper) krb5Class.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e2) {
                    throw new AssertionError(e2);
                }
            }
            return null;
        }

        private KrbClientKeyExchangeMessage(HandshakeContext handshakeContext) {
            super(handshakeContext);
            KrbClientKeyExchangeHelper krbClientKeyExchangeHelperNewKrb5Instance = newKrb5Instance();
            this.krb5Helper = krbClientKeyExchangeHelperNewKrb5Instance;
            if (krbClientKeyExchangeHelperNewKrb5Instance == null) {
                throw new IllegalStateException("Kerberos is unavailable");
            }
        }

        KrbClientKeyExchangeMessage(HandshakeContext handshakeContext, byte[] bArr, String str, AccessControlContext accessControlContext) throws IOException {
            this(handshakeContext);
            this.krb5Helper.init(bArr, str, accessControlContext);
        }

        KrbClientKeyExchangeMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer, Object obj, AccessControlContext accessControlContext) throws IOException {
            this(handshakeContext);
            byte[] bytes16 = Record.getBytes16(byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("encoded Kerberos service ticket", bytes16);
            }
            Record.getBytes16(byteBuffer);
            byte[] bytes162 = Record.getBytes16(byteBuffer);
            if (bytes162 != null && SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("encrypted Kerberos pre-master secret", bytes162);
            }
            this.krb5Helper.init(bytes16, bytes162, obj, accessControlContext);
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        SSLHandshake handshakeType() {
            return SSLHandshake.CLIENT_KEY_EXCHANGE;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        int messageLength() {
            return 6 + this.krb5Helper.getEncodedTicket().length + this.krb5Helper.getEncryptedPreMasterSecret().length;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        void send(HandshakeOutStream handshakeOutStream) throws IOException {
            handshakeOutStream.putBytes16(this.krb5Helper.getEncodedTicket());
            handshakeOutStream.putBytes16(null);
            handshakeOutStream.putBytes16(this.krb5Helper.getEncryptedPreMasterSecret());
        }

        byte[] getPlainPreMasterSecret() {
            return this.krb5Helper.getPlainPreMasterSecret();
        }

        Principal getPeerPrincipal() {
            return this.krb5Helper.getPeerPrincipal();
        }

        Principal getLocalPrincipal() {
            return this.krb5Helper.getLocalPrincipal();
        }

        public String toString() {
            MessageFormat messageFormat = new MessageFormat("\"KRB5 ClientKeyExchange\": '{'\n  \"ticket\": '{'\n{0}\n  '}'\n  \"pre-master\": '{'\n    \"plain\": '{'\n{1}\n    '}'\n    \"encrypted\": '{'\n{2}\n    '}'\n  '}'\n'}'", Locale.ENGLISH);
            HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
            return messageFormat.format(new Object[]{Utilities.indent(hexDumpEncoder.encodeBuffer(this.krb5Helper.getEncodedTicket()), Constants.INDENT), Utilities.indent(hexDumpEncoder.encodeBuffer(this.krb5Helper.getPlainPreMasterSecret()), GoToActionDialog.EMPTY_DESTINATION), Utilities.indent(hexDumpEncoder.encodeBuffer(this.krb5Helper.getEncryptedPreMasterSecret()), GoToActionDialog.EMPTY_DESTINATION)});
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KrbClientKeyExchange$KrbClientKeyExchangeProducer.class */
    private static final class KrbClientKeyExchangeProducer implements HandshakeProducer {
        private KrbClientKeyExchangeProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            String peerHost = null;
            if (clientHandshakeContext.negotiatedServerName != null) {
                if (clientHandshakeContext.negotiatedServerName.getType() == 0) {
                    SNIHostName sNIHostName = null;
                    if (clientHandshakeContext.negotiatedServerName instanceof SNIHostName) {
                        sNIHostName = (SNIHostName) clientHandshakeContext.negotiatedServerName;
                    } else {
                        try {
                            sNIHostName = new SNIHostName(clientHandshakeContext.negotiatedServerName.getEncoded());
                        } catch (IllegalArgumentException e2) {
                        }
                    }
                    if (sNIHostName != null) {
                        peerHost = sNIHostName.getAsciiName();
                    }
                }
            } else {
                peerHost = clientHandshakeContext.handshakeSession.getPeerHost();
            }
            try {
                KrbKeyExchange.KrbPremasterSecret krbPremasterSecretCreatePremasterSecret = KrbKeyExchange.KrbPremasterSecret.createPremasterSecret(clientHandshakeContext.negotiatedProtocol, clientHandshakeContext.sslContext.getSecureRandom());
                KrbClientKeyExchangeMessage krbClientKeyExchangeMessage = new KrbClientKeyExchangeMessage(clientHandshakeContext, krbPremasterSecretCreatePremasterSecret.preMaster, peerHost, clientHandshakeContext.conContext.acc);
                clientHandshakeContext.handshakePossessions.add(krbPremasterSecretCreatePremasterSecret);
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Produced KRB5 ClientKeyExchange handshake message", krbClientKeyExchangeMessage);
                }
                clientHandshakeContext.handshakeSession.setPeerPrincipal(krbClientKeyExchangeMessage.getPeerPrincipal());
                clientHandshakeContext.handshakeSession.setLocalPrincipal(krbClientKeyExchangeMessage.getLocalPrincipal());
                krbClientKeyExchangeMessage.write(clientHandshakeContext.handshakeOutput);
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
            } catch (IOException e3) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Error generating KRB premaster secret. Hostname: " + peerHost + " - Negotiated server name: " + ((Object) clientHandshakeContext.negotiatedServerName), new Object[0]);
                }
                throw clientHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Cannot generate KRB premaster secret", e3);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KrbClientKeyExchange$KrbClientKeyExchangeConsumer.class */
    private static final class KrbClientKeyExchangeConsumer implements SSLConsumer {
        private KrbClientKeyExchangeConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            Object obj = null;
            Iterator<SSLPossession> it = serverHandshakeContext.handshakePossessions.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                SSLPossession next = it.next();
                if (next instanceof KrbKeyExchange.KrbServiceCreds) {
                    obj = ((KrbKeyExchange.KrbServiceCreds) next).serviceCreds;
                    break;
                }
            }
            if (obj == null) {
                throw serverHandshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "No Kerberos service credentials for KRB Client Key Exchange");
            }
            KrbClientKeyExchangeMessage krbClientKeyExchangeMessage = new KrbClientKeyExchangeMessage(serverHandshakeContext, byteBuffer, obj, serverHandshakeContext.conContext.acc);
            KrbKeyExchange.KrbPremasterSecret krbPremasterSecretDecode = KrbKeyExchange.KrbPremasterSecret.decode(serverHandshakeContext.negotiatedProtocol, ProtocolVersion.valueOf(serverHandshakeContext.clientHelloVersion), krbClientKeyExchangeMessage.getPlainPreMasterSecret(), serverHandshakeContext.sslContext.getSecureRandom());
            serverHandshakeContext.handshakeSession.setPeerPrincipal(krbClientKeyExchangeMessage.getPeerPrincipal());
            serverHandshakeContext.handshakeSession.setLocalPrincipal(krbClientKeyExchangeMessage.getLocalPrincipal());
            serverHandshakeContext.handshakeCredentials.add(krbPremasterSecretDecode);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming KRB5 ClientKeyExchange handshake message", krbClientKeyExchangeMessage);
            }
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
        }
    }
}
