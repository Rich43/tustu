package sun.security.ssl;

/* loaded from: jsse.jar:sun/security/ssl/SSLPossessionGenerator.class */
interface SSLPossessionGenerator {
    SSLPossession createPossession(HandshakeContext handshakeContext);
}
