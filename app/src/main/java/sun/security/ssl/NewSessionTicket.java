package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.Locale;
import javax.crypto.SecretKey;
import javax.net.ssl.SSLHandshakeException;
import sun.security.ssl.CipherSuite;
import sun.security.ssl.PskKeyExchangeModesExtension;
import sun.security.ssl.SSLHandshake;

/* loaded from: jsse.jar:sun/security/ssl/NewSessionTicket.class */
final class NewSessionTicket {
    private static final int MAX_TICKET_LIFETIME = 604800;
    static final SSLConsumer handshakeConsumer = new NewSessionTicketConsumer();
    static final SSLProducer kickstartProducer = new NewSessionTicketKickstartProducer();
    static final HandshakeProducer handshakeProducer = new NewSessionTicketProducer();

    NewSessionTicket() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/NewSessionTicket$NewSessionTicketMessage.class */
    static final class NewSessionTicketMessage extends SSLHandshake.HandshakeMessage {
        final int ticketLifetime;
        final int ticketAgeAdd;
        final byte[] ticketNonce;
        final byte[] ticket;
        final SSLExtensions extensions;

        NewSessionTicketMessage(HandshakeContext handshakeContext, int i2, SecureRandom secureRandom, byte[] bArr, byte[] bArr2) {
            super(handshakeContext);
            this.ticketLifetime = i2;
            this.ticketAgeAdd = secureRandom.nextInt();
            this.ticketNonce = bArr;
            this.ticket = bArr2;
            this.extensions = new SSLExtensions(this);
        }

        NewSessionTicketMessage(HandshakeContext handshakeContext, ByteBuffer byteBuffer) throws IOException {
            super(handshakeContext);
            if (byteBuffer.remaining() < 14) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid NewSessionTicket message: no sufficient data");
            }
            this.ticketLifetime = Record.getInt32(byteBuffer);
            this.ticketAgeAdd = Record.getInt32(byteBuffer);
            this.ticketNonce = Record.getBytes8(byteBuffer);
            if (byteBuffer.remaining() < 5) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid NewSessionTicket message: no sufficient data");
            }
            this.ticket = Record.getBytes16(byteBuffer);
            if (this.ticket.length == 0) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "No ticket in the NewSessionTicket handshake message");
            }
            if (byteBuffer.remaining() < 2) {
                throw handshakeContext.conContext.fatal(Alert.ILLEGAL_PARAMETER, "Invalid NewSessionTicket message: no sufficient data");
            }
            this.extensions = new SSLExtensions(this, byteBuffer, handshakeContext.sslConfig.getEnabledExtensions(SSLHandshake.NEW_SESSION_TICKET));
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public SSLHandshake handshakeType() {
            return SSLHandshake.NEW_SESSION_TICKET;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public int messageLength() {
            int length = this.extensions.length();
            if (length == 0) {
                length = 2;
            }
            return 8 + this.ticketNonce.length + 1 + this.ticket.length + 2 + length;
        }

        @Override // sun.security.ssl.SSLHandshake.HandshakeMessage
        public void send(HandshakeOutStream handshakeOutStream) throws IOException {
            handshakeOutStream.putInt32(this.ticketLifetime);
            handshakeOutStream.putInt32(this.ticketAgeAdd);
            handshakeOutStream.putBytes8(this.ticketNonce);
            handshakeOutStream.putBytes16(this.ticket);
            if (this.extensions.length() == 0) {
                handshakeOutStream.putInt16(0);
            } else {
                this.extensions.send(handshakeOutStream);
            }
        }

        public String toString() {
            return new MessageFormat("\"NewSessionTicket\": '{'\n  \"ticket_lifetime\"      : \"{0}\",\n  \"ticket_age_add\"       : \"{1}\",\n  \"ticket_nonce\"         : \"{2}\",\n  \"ticket\"               : \"{3}\",\n  \"extensions\"           : [\n{4}\n  ]\n'}'", Locale.ENGLISH).format(new Object[]{Integer.valueOf(this.ticketLifetime), "<omitted>", Utilities.toHexString(this.ticketNonce), Utilities.toHexString(this.ticket), Utilities.indent(this.extensions.toString(), "    ")});
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static SecretKey derivePreSharedKey(CipherSuite.HashAlg hashAlg, SecretKey secretKey, byte[] bArr) throws IOException {
        try {
            return new HKDF(hashAlg.name).expand(secretKey, SSLSecretDerivation.createHkdfInfo("tls13 resumption".getBytes(), bArr, hashAlg.hashLength), hashAlg.hashLength, "TlsPreSharedKey");
        } catch (GeneralSecurityException e2) {
            throw ((SSLHandshakeException) new SSLHandshakeException("Could not derive PSK").initCause(e2));
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/NewSessionTicket$NewSessionTicketKickstartProducer.class */
    private static final class NewSessionTicketKickstartProducer implements SSLProducer {
        private NewSessionTicketKickstartProducer() {
        }

        @Override // sun.security.ssl.SSLProducer
        public byte[] produce(ConnectionContext connectionContext) throws IOException {
            PskKeyExchangeModesExtension.PskKeyExchangeModesSpec pskKeyExchangeModesSpec;
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.handshakeSession.isRejoinable() || (pskKeyExchangeModesSpec = (PskKeyExchangeModesExtension.PskKeyExchangeModesSpec) serverHandshakeContext.handshakeExtensions.get(SSLExtension.PSK_KEY_EXCHANGE_MODES)) == null || !pskKeyExchangeModesSpec.contains(PskKeyExchangeModesExtension.PskKeyExchangeMode.PSK_DHE_KE)) {
                return null;
            }
            SSLSessionContextImpl sSLSessionContextImpl = (SSLSessionContextImpl) serverHandshakeContext.sslContext.engineGetServerSessionContext();
            SessionId sessionId = new SessionId(true, serverHandshakeContext.sslContext.getSecureRandom());
            SecretKey resumptionMasterSecret = serverHandshakeContext.handshakeSession.getResumptionMasterSecret();
            if (resumptionMasterSecret == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Session has no resumption secret. No ticket sent.", new Object[0]);
                    return null;
                }
                return null;
            }
            byte[] byteArray = serverHandshakeContext.handshakeSession.incrTicketNonceCounter().toByteArray();
            SecretKey secretKeyDerivePreSharedKey = NewSessionTicket.derivePreSharedKey(serverHandshakeContext.negotiatedCipherSuite.hashAlg, resumptionMasterSecret, byteArray);
            int sessionTimeout = sSLSessionContextImpl.getSessionTimeout();
            if (sessionTimeout > 604800) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Session timeout is too long. No ticket sent.", new Object[0]);
                    return null;
                }
                return null;
            }
            NewSessionTicketMessage newSessionTicketMessage = new NewSessionTicketMessage(serverHandshakeContext, sessionTimeout, serverHandshakeContext.sslContext.getSecureRandom(), byteArray, sessionId.getId());
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Produced NewSessionTicket handshake message", newSessionTicketMessage);
            }
            SSLSessionImpl sSLSessionImpl = new SSLSessionImpl(serverHandshakeContext.handshakeSession, sessionId);
            serverHandshakeContext.handshakeSession.addChild(sSLSessionImpl);
            sSLSessionImpl.setPreSharedKey(secretKeyDerivePreSharedKey);
            sSLSessionImpl.setPskIdentity(sessionId.getId());
            sSLSessionImpl.setTicketAgeAdd(newSessionTicketMessage.ticketAgeAdd);
            sSLSessionContextImpl.put(sSLSessionImpl);
            newSessionTicketMessage.write(serverHandshakeContext.handshakeOutput);
            serverHandshakeContext.handshakeOutput.flush();
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/NewSessionTicket$NewSessionTicketProducer.class */
    private static final class NewSessionTicketProducer implements HandshakeProducer {
        private NewSessionTicketProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            throw new ProviderException("NewSessionTicket handshake producer not implemented");
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/NewSessionTicket$NewSessionTicketConsumer.class */
    private static final class NewSessionTicketConsumer implements SSLConsumer {
        private NewSessionTicketConsumer() {
        }

        @Override // sun.security.ssl.SSLConsumer
        public void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException {
            HandshakeContext handshakeContext = (HandshakeContext) connectionContext;
            NewSessionTicketMessage newSessionTicketMessage = new NewSessionTicketMessage(handshakeContext, byteBuffer);
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Consuming NewSessionTicket message", newSessionTicketMessage);
            }
            if (newSessionTicketMessage.ticketLifetime <= 0 || newSessionTicketMessage.ticketLifetime > 604800) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Discarding NewSessionTicket with lifetime " + newSessionTicketMessage.ticketLifetime, newSessionTicketMessage);
                    return;
                }
                return;
            }
            SSLSessionContextImpl sSLSessionContextImpl = (SSLSessionContextImpl) handshakeContext.sslContext.engineGetClientSessionContext();
            if (sSLSessionContextImpl.getSessionTimeout() > 604800) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Session cache lifetime is too long. Discarding ticket.", new Object[0]);
                    return;
                }
                return;
            }
            SSLSessionImpl sSLSessionImpl = handshakeContext.conContext.conSession;
            SecretKey resumptionMasterSecret = sSLSessionImpl.getResumptionMasterSecret();
            if (resumptionMasterSecret != null) {
                SecretKey secretKeyDerivePreSharedKey = NewSessionTicket.derivePreSharedKey(sSLSessionImpl.getSuite().hashAlg, resumptionMasterSecret, newSessionTicketMessage.ticketNonce);
                SSLSessionImpl sSLSessionImpl2 = new SSLSessionImpl(sSLSessionImpl, new SessionId(true, handshakeContext.sslContext.getSecureRandom()));
                sSLSessionImpl.addChild(sSLSessionImpl2);
                sSLSessionImpl2.setPreSharedKey(secretKeyDerivePreSharedKey);
                sSLSessionImpl2.setTicketAgeAdd(newSessionTicketMessage.ticketAgeAdd);
                sSLSessionImpl2.setPskIdentity(newSessionTicketMessage.ticket);
                sSLSessionContextImpl.put(sSLSessionImpl2);
                handshakeContext.conContext.finishPostHandshake();
                return;
            }
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Session has no resumption master secret. Ignoring ticket.", new Object[0]);
            }
        }
    }
}
