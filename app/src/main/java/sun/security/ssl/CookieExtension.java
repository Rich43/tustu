package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.Locale;
import javax.net.ssl.SSLProtocolException;
import sun.misc.HexDumpEncoder;
import sun.security.ssl.ClientHello;
import sun.security.ssl.SSLExtension;
import sun.security.ssl.SSLHandshake;
import sun.security.ssl.ServerHello;

/* loaded from: jsse.jar:sun/security/ssl/CookieExtension.class */
public class CookieExtension {
    static final HandshakeProducer chNetworkProducer = new CHCookieProducer();
    static final SSLExtension.ExtensionConsumer chOnLoadConsumer = new CHCookieConsumer();
    static final HandshakeConsumer chOnTradeConsumer = new CHCookieUpdate();
    static final HandshakeProducer hrrNetworkProducer = new HRRCookieProducer();
    static final SSLExtension.ExtensionConsumer hrrOnLoadConsumer = new HRRCookieConsumer();
    static final HandshakeProducer hrrNetworkReproducer = new HRRCookieReproducer();
    static final CookieStringizer cookieStringizer = new CookieStringizer();

    /* loaded from: jsse.jar:sun/security/ssl/CookieExtension$CookieSpec.class */
    static class CookieSpec implements SSLExtension.SSLExtensionSpec {
        final byte[] cookie;

        private CookieSpec(ByteBuffer byteBuffer) throws IOException {
            if (byteBuffer.remaining() < 3) {
                throw new SSLProtocolException("Invalid cookie extension: insufficient data");
            }
            this.cookie = Record.getBytes16(byteBuffer);
        }

        public String toString() {
            return new MessageFormat("\"cookie\": '{'\n{0}\n'}',", Locale.ENGLISH).format(new Object[]{Utilities.indent(new HexDumpEncoder().encode(this.cookie))});
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: jsse.jar:sun/security/ssl/CookieExtension$CookieStringizer.class */
    static final class CookieStringizer implements SSLStringizer {
        private CookieStringizer() {
        }

        @Override // sun.security.ssl.SSLStringizer
        public String toString(ByteBuffer byteBuffer) {
            try {
                return new CookieSpec(byteBuffer).toString();
            } catch (IOException e2) {
                return e2.getMessage();
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CookieExtension$CHCookieProducer.class */
    private static final class CHCookieProducer implements HandshakeProducer {
        private CHCookieProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_COOKIE)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable cookie extension", new Object[0]);
                    return null;
                }
                return null;
            }
            CookieSpec cookieSpec = (CookieSpec) clientHandshakeContext.handshakeExtensions.get(SSLExtension.HRR_COOKIE);
            if (cookieSpec != null && cookieSpec.cookie != null && cookieSpec.cookie.length != 0) {
                byte[] bArr = new byte[cookieSpec.cookie.length + 2];
                Record.putBytes16(ByteBuffer.wrap(bArr), cookieSpec.cookie);
                return bArr;
            }
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CookieExtension$CHCookieConsumer.class */
    private static final class CHCookieConsumer implements SSLExtension.ExtensionConsumer {
        private CHCookieConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.CH_COOKIE)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable cookie extension", new Object[0]);
                    return;
                }
                return;
            }
            try {
                serverHandshakeContext.handshakeExtensions.put(SSLExtension.CH_COOKIE, new CookieSpec(byteBuffer));
            } catch (IOException e2) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CookieExtension$CHCookieUpdate.class */
    private static final class CHCookieUpdate implements HandshakeConsumer {
        private CHCookieUpdate() {
        }

        @Override // sun.security.ssl.HandshakeConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            ClientHello.ClientHelloMessage clientHelloMessage = (ClientHello.ClientHelloMessage) handshakeMessage;
            CookieSpec cookieSpec = (CookieSpec) serverHandshakeContext.handshakeExtensions.get(SSLExtension.CH_COOKIE);
            if (cookieSpec != null && !serverHandshakeContext.sslContext.getHelloCookieManager(serverHandshakeContext.negotiatedProtocol).isCookieValid(serverHandshakeContext, clientHelloMessage, cookieSpec.cookie)) {
                throw serverHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, "unrecognized cookie");
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CookieExtension$HRRCookieProducer.class */
    private static final class HRRCookieProducer implements HandshakeProducer {
        private HRRCookieProducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            ServerHello.ServerHelloMessage serverHelloMessage = (ServerHello.ServerHelloMessage) handshakeMessage;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.HRR_COOKIE)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable cookie extension", new Object[0]);
                    return null;
                }
                return null;
            }
            byte[] bArrCreateCookie = serverHandshakeContext.sslContext.getHelloCookieManager(serverHandshakeContext.negotiatedProtocol).createCookie(serverHandshakeContext, serverHelloMessage.clientHello);
            byte[] bArr = new byte[bArrCreateCookie.length + 2];
            Record.putBytes16(ByteBuffer.wrap(bArr), bArrCreateCookie);
            return bArr;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CookieExtension$HRRCookieConsumer.class */
    private static final class HRRCookieConsumer implements SSLExtension.ExtensionConsumer {
        private HRRCookieConsumer() {
        }

        @Override // sun.security.ssl.SSLExtension.ExtensionConsumer
        public void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage, ByteBuffer byteBuffer) throws IOException {
            ClientHandshakeContext clientHandshakeContext = (ClientHandshakeContext) connectionContext;
            if (!clientHandshakeContext.sslConfig.isAvailable(SSLExtension.HRR_COOKIE)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable cookie extension", new Object[0]);
                    return;
                }
                return;
            }
            try {
                clientHandshakeContext.handshakeExtensions.put(SSLExtension.HRR_COOKIE, new CookieSpec(byteBuffer));
            } catch (IOException e2) {
                throw clientHandshakeContext.conContext.fatal(Alert.UNEXPECTED_MESSAGE, e2);
            }
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/CookieExtension$HRRCookieReproducer.class */
    private static final class HRRCookieReproducer implements HandshakeProducer {
        private HRRCookieReproducer() {
        }

        @Override // sun.security.ssl.HandshakeProducer
        public byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException {
            ServerHandshakeContext serverHandshakeContext = (ServerHandshakeContext) connectionContext;
            if (!serverHandshakeContext.sslConfig.isAvailable(SSLExtension.HRR_COOKIE)) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.fine("Ignore unavailable cookie extension", new Object[0]);
                    return null;
                }
                return null;
            }
            CookieSpec cookieSpec = (CookieSpec) serverHandshakeContext.handshakeExtensions.get(SSLExtension.CH_COOKIE);
            if (cookieSpec != null && cookieSpec.cookie != null && cookieSpec.cookie.length != 0) {
                byte[] bArr = new byte[cookieSpec.cookie.length + 2];
                Record.putBytes16(ByteBuffer.wrap(bArr), cookieSpec.cookie);
                return bArr;
            }
            return null;
        }
    }
}
