package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_ECDH1_DERIVE_PARAMS.class */
public class CK_ECDH1_DERIVE_PARAMS {
    public long kdf;
    public byte[] pSharedData;
    public byte[] pPublicData;

    public CK_ECDH1_DERIVE_PARAMS(long j2, byte[] bArr, byte[] bArr2) {
        this.kdf = j2;
        this.pSharedData = bArr;
        this.pPublicData = bArr2;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("kdf: 0x");
        stringBuffer.append(Functions.toFullHexString(this.kdf));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("pSharedDataLen: ");
        stringBuffer.append(this.pSharedData.length);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("pSharedData: ");
        stringBuffer.append(Functions.toHexString(this.pSharedData));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("pPublicDataLen: ");
        stringBuffer.append(this.pPublicData.length);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("pPublicData: ");
        stringBuffer.append(Functions.toHexString(this.pPublicData));
        return stringBuffer.toString();
    }
}
