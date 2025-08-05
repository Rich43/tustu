package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_MECHANISM_INFO.class */
public class CK_MECHANISM_INFO {
    public long ulMinKeySize;
    public final int iMinKeySize;
    public long ulMaxKeySize;
    public final int iMaxKeySize;
    public long flags;

    public CK_MECHANISM_INFO(long j2, long j3, long j4) {
        this.ulMinKeySize = j2;
        this.ulMaxKeySize = j3;
        this.iMinKeySize = (j2 >= 2147483647L || j2 <= 0) ? 0 : (int) j2;
        this.iMaxKeySize = (j3 >= 2147483647L || j3 <= 0) ? Integer.MAX_VALUE : (int) j3;
        this.flags = j4;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("ulMinKeySize: ");
        stringBuffer.append(String.valueOf(this.ulMinKeySize));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("ulMaxKeySize: ");
        stringBuffer.append(String.valueOf(this.ulMaxKeySize));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("flags: ");
        stringBuffer.append(String.valueOf(this.flags));
        stringBuffer.append(" = ");
        stringBuffer.append(Functions.mechanismInfoFlagsToString(this.flags));
        return stringBuffer.toString();
    }
}
