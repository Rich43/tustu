package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_TLS_MAC_PARAMS.class */
public class CK_TLS_MAC_PARAMS {
    public long prfMechanism;
    public long ulMacLength;
    public long ulServerOrClient;

    public CK_TLS_MAC_PARAMS(long j2, long j3, long j4) {
        this.prfMechanism = j2;
        this.ulMacLength = j3;
        this.ulServerOrClient = j4;
    }
}
