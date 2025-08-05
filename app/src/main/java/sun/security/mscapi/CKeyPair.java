package sun.security.mscapi;

import sun.security.mscapi.CKey;

/* loaded from: sunmscapi.jar:sun/security/mscapi/CKeyPair.class */
class CKeyPair {
    private final CPrivateKey privateKey;
    private final CPublicKey publicKey;

    CKeyPair(String str, long j2, long j3, int i2) {
        CKey.NativeHandles nativeHandles = new CKey.NativeHandles(j2, j3);
        this.privateKey = CPrivateKey.of(str, nativeHandles, i2);
        this.publicKey = CPublicKey.of(str, nativeHandles, i2);
    }

    public CPrivateKey getPrivate() {
        return this.privateKey;
    }

    public CPublicKey getPublic() {
        return this.publicKey;
    }
}
