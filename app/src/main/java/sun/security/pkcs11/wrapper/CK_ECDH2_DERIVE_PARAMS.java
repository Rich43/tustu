package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_ECDH2_DERIVE_PARAMS.class */
public class CK_ECDH2_DERIVE_PARAMS {
    public long kdf;
    public byte[] pSharedData;
    public byte[] pPublicData;
    public long ulPrivateDataLen;
    public long hPrivateData;
    public byte[] pPublicData2;

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
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("ulPrivateDataLen: ");
        stringBuffer.append(this.ulPrivateDataLen);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("hPrivateData: ");
        stringBuffer.append(this.hPrivateData);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("pPublicDataLen2: ");
        stringBuffer.append(this.pPublicData2.length);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("pPublicData2: ");
        stringBuffer.append(Functions.toHexString(this.pPublicData2));
        return stringBuffer.toString();
    }
}
