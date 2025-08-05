package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_TLS_PRF_PARAMS.class */
public class CK_TLS_PRF_PARAMS {
    public byte[] pSeed;
    public byte[] pLabel;
    public byte[] pOutput;

    public CK_TLS_PRF_PARAMS(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        this.pSeed = bArr;
        this.pLabel = bArr2;
        this.pOutput = bArr3;
    }
}
