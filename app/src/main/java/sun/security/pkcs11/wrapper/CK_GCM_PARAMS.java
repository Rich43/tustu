package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_GCM_PARAMS.class */
public class CK_GCM_PARAMS {
    private final byte[] iv;
    private final byte[] aad;
    private final long tagBits;

    public CK_GCM_PARAMS(int i2, byte[] bArr, byte[] bArr2) {
        this.iv = bArr;
        this.aad = bArr2;
        this.tagBits = i2;
    }

    public String toString() {
        return Constants.INDENT + "iv: " + Functions.toHexString(this.iv) + Constants.NEWLINE + Constants.INDENT + "aad: " + Functions.toHexString(this.aad) + Constants.NEWLINE + Constants.INDENT + "tagLen(in bits): " + this.tagBits;
    }
}
