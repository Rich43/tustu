package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_PBE_PARAMS.class */
public class CK_PBE_PARAMS {
    public char[] pInitVector;
    public char[] pPassword;
    public char[] pSalt;
    public long ulIteration;

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("pInitVector: ");
        stringBuffer.append(this.pInitVector);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("ulPasswordLen: ");
        stringBuffer.append(this.pPassword.length);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("pPassword: ");
        stringBuffer.append(this.pPassword);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("ulSaltLen: ");
        stringBuffer.append(this.pSalt.length);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("pSalt: ");
        stringBuffer.append(this.pSalt);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("ulIteration: ");
        stringBuffer.append(this.ulIteration);
        return stringBuffer.toString();
    }
}
