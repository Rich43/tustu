package sun.security.ssl;

import java.util.Map;

/* loaded from: jsse.jar:sun/security/ssl/SSLHandshakeBinding.class */
interface SSLHandshakeBinding {
    default SSLHandshake[] getRelatedHandshakers(HandshakeContext handshakeContext) {
        return new SSLHandshake[0];
    }

    default Map.Entry<Byte, HandshakeProducer>[] getHandshakeProducers(HandshakeContext handshakeContext) {
        return new Map.Entry[0];
    }

    default Map.Entry<Byte, SSLConsumer>[] getHandshakeConsumers(HandshakeContext handshakeContext) {
        return new Map.Entry[0];
    }
}
