package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_RSA_PKCS_OAEP_PARAMS.class */
public class CK_RSA_PKCS_OAEP_PARAMS {
    public long hashAlg;
    public long mgf;
    public long source;
    public byte[] pSourceData;

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("hashAlg: ");
        stringBuffer.append(this.hashAlg);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("mgf: ");
        stringBuffer.append(this.mgf);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("source: ");
        stringBuffer.append(this.source);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("pSourceData: ");
        stringBuffer.append(this.pSourceData.toString());
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("pSourceDataLen: ");
        stringBuffer.append(Functions.toHexString(this.pSourceData));
        return stringBuffer.toString();
    }
}
