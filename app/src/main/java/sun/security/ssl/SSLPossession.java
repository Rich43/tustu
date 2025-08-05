package sun.security.ssl;

/* loaded from: jsse.jar:sun/security/ssl/SSLPossession.class */
interface SSLPossession {
    default byte[] encode() {
        return new byte[0];
    }
}
