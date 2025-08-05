package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_SESSION_INFO.class */
public class CK_SESSION_INFO {
    public long slotID;
    public long state;
    public long flags;
    public long ulDeviceError;

    public CK_SESSION_INFO(long j2, long j3, long j4, long j5) {
        this.slotID = j2;
        this.state = j3;
        this.flags = j4;
        this.ulDeviceError = j5;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("slotID: ");
        stringBuffer.append(String.valueOf(this.slotID));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("state: ");
        stringBuffer.append(Functions.sessionStateToString(this.state));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("flags: ");
        stringBuffer.append(Functions.sessionInfoFlagsToString(this.flags));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("ulDeviceError: ");
        stringBuffer.append(Functions.toHexString(this.ulDeviceError));
        return stringBuffer.toString();
    }
}
