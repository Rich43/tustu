package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_SSL3_KEY_MAT_PARAMS.class */
public class CK_SSL3_KEY_MAT_PARAMS {
    public long ulMacSizeInBits;
    public long ulKeySizeInBits;
    public long ulIVSizeInBits;
    public boolean bIsExport;
    public CK_SSL3_RANDOM_DATA RandomInfo;
    public CK_SSL3_KEY_MAT_OUT pReturnedKeyMaterial = new CK_SSL3_KEY_MAT_OUT();

    public CK_SSL3_KEY_MAT_PARAMS(int i2, int i3, int i4, boolean z2, CK_SSL3_RANDOM_DATA ck_ssl3_random_data) {
        this.ulMacSizeInBits = i2;
        this.ulKeySizeInBits = i3;
        this.ulIVSizeInBits = i4;
        this.bIsExport = z2;
        this.RandomInfo = ck_ssl3_random_data;
        if (i4 != 0) {
            int i5 = i4 >> 3;
            this.pReturnedKeyMaterial.pIVClient = new byte[i5];
            this.pReturnedKeyMaterial.pIVServer = new byte[i5];
        }
    }

    public String toString() {
        return Constants.INDENT + "ulMacSizeInBits: " + this.ulMacSizeInBits + Constants.NEWLINE + Constants.INDENT + "ulKeySizeInBits: " + this.ulKeySizeInBits + Constants.NEWLINE + Constants.INDENT + "ulIVSizeInBits: " + this.ulIVSizeInBits + Constants.NEWLINE + Constants.INDENT + "bIsExport: " + this.bIsExport + Constants.NEWLINE + Constants.INDENT + "RandomInfo: " + ((Object) this.RandomInfo) + Constants.NEWLINE + Constants.INDENT + "pReturnedKeyMaterial: " + ((Object) this.pReturnedKeyMaterial);
    }
}
