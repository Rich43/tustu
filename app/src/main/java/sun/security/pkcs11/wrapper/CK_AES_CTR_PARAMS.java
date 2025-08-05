package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_AES_CTR_PARAMS.class */
public class CK_AES_CTR_PARAMS {
    private final long ulCounterBits = 128;
    private final byte[] cb;

    public CK_AES_CTR_PARAMS(byte[] bArr) {
        this.cb = (byte[]) bArr.clone();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("ulCounterBits: ");
        stringBuffer.append(this.ulCounterBits);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("cb: ");
        stringBuffer.append(Functions.toHexString(this.cb));
        return stringBuffer.toString();
    }
}
