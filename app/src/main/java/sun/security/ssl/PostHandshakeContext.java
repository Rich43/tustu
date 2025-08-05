package sun.security.ssl;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/* loaded from: jsse.jar:sun/security/ssl/PostHandshakeContext.class */
final class PostHandshakeContext extends HandshakeContext {
    PostHandshakeContext(TransportContext transportContext) throws IOException {
        super(transportContext);
        if (!this.negotiatedProtocol.useTLS13PlusSpec()) {
            throw this.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Post-handshake not supported in " + this.negotiatedProtocol.name);
        }
        this.localSupportedSignAlgs = new ArrayList(transportContext.conSession.getLocalSupportedSignatureSchemes());
        if (transportContext.sslConfig.isClientMode) {
            this.handshakeConsumers.putIfAbsent(Byte.valueOf(SSLHandshake.KEY_UPDATE.id), SSLHandshake.KEY_UPDATE);
            this.handshakeConsumers.putIfAbsent(Byte.valueOf(SSLHandshake.NEW_SESSION_TICKET.id), SSLHandshake.NEW_SESSION_TICKET);
        } else {
            this.handshakeConsumers.putIfAbsent(Byte.valueOf(SSLHandshake.KEY_UPDATE.id), SSLHandshake.KEY_UPDATE);
        }
        this.handshakeFinished = true;
    }

    @Override // sun.security.ssl.HandshakeContext
    void kickstart() throws IOException {
        SSLHandshake.kickstart(this);
    }

    @Override // sun.security.ssl.HandshakeContext
    void dispatch(byte b2, ByteBuffer byteBuffer) throws IOException {
        SSLConsumer sSLConsumer = this.handshakeConsumers.get(Byte.valueOf(b2));
        if (sSLConsumer == null) {
            throw this.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unexpected post-handshake message: " + SSLHandshake.nameOf(b2));
        }
        try {
            sSLConsumer.consume(this, byteBuffer);
        } catch (UnsupportedOperationException e2) {
            throw this.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "Unsupported post-handshake message: " + SSLHandshake.nameOf(b2), e2);
        } catch (BufferOverflowException | BufferUnderflowException e3) {
            throw this.conContext.fatal(Alert.DECODE_ERROR, "Illegal handshake message: " + SSLHandshake.nameOf(b2), e3);
        }
    }

    static boolean isConsumable(TransportContext transportContext, byte b2) {
        if (b2 == SSLHandshake.KEY_UPDATE.id) {
            return transportContext.protocolVersion.useTLS13PlusSpec();
        }
        if (b2 == SSLHandshake.NEW_SESSION_TICKET.id) {
            return transportContext.sslConfig.isClientMode;
        }
        return false;
    }
}
