package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_X9_42_DH1_DERIVE_PARAMS.class */
public class CK_X9_42_DH1_DERIVE_PARAMS {
    public long kdf;
    public byte[] pOtherInfo;
    public byte[] pPublicData;

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("kdf: 0x");
        stringBuffer.append(Functions.toFullHexString(this.kdf));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("pOtherInfoLen: ");
        stringBuffer.append(this.pOtherInfo.length);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("pOtherInfo: ");
        stringBuffer.append(Functions.toHexString(this.pOtherInfo));
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
