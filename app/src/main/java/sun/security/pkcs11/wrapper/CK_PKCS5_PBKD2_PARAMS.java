package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_PKCS5_PBKD2_PARAMS.class */
public class CK_PKCS5_PBKD2_PARAMS {
    public long saltSource;
    public byte[] pSaltSourceData;
    public long iterations;
    public long prf;
    public byte[] pPrfData;

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("saltSource: ");
        stringBuffer.append(this.saltSource);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("pSaltSourceData: ");
        stringBuffer.append(Functions.toHexString(this.pSaltSourceData));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("ulSaltSourceDataLen: ");
        stringBuffer.append(this.pSaltSourceData.length);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("iterations: ");
        stringBuffer.append(this.iterations);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("prf: ");
        stringBuffer.append(this.prf);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("pPrfData: ");
        stringBuffer.append(Functions.toHexString(this.pPrfData));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("ulPrfDataLen: ");
        stringBuffer.append(this.pPrfData.length);
        return stringBuffer.toString();
    }
}
