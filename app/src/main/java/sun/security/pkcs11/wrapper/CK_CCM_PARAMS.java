package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_CCM_PARAMS.class */
public class CK_CCM_PARAMS {
    private final long dataLen;
    private final byte[] nonce;
    private final byte[] aad;
    private final long macLen;

    public CK_CCM_PARAMS(int i2, byte[] bArr, byte[] bArr2, int i3) {
        this.dataLen = i3;
        this.nonce = bArr;
        this.aad = bArr2;
        this.macLen = i2;
    }

    public String toString() {
        return Constants.INDENT + "ulDataLen: " + this.dataLen + Constants.NEWLINE + Constants.INDENT + "iv: " + Functions.toHexString(this.nonce) + Constants.NEWLINE + Constants.INDENT + "aad: " + Functions.toHexString(this.aad) + Constants.NEWLINE + Constants.INDENT + "tagLen: " + this.macLen;
    }
}
