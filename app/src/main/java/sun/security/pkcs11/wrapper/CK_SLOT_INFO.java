package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_SLOT_INFO.class */
public class CK_SLOT_INFO {
    public char[] slotDescription;
    public char[] manufacturerID;
    public long flags;
    public CK_VERSION hardwareVersion;
    public CK_VERSION firmwareVersion;

    public CK_SLOT_INFO(char[] cArr, char[] cArr2, long j2, CK_VERSION ck_version, CK_VERSION ck_version2) {
        this.slotDescription = cArr;
        this.manufacturerID = cArr2;
        this.flags = j2;
        this.hardwareVersion = ck_version;
        this.firmwareVersion = ck_version2;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("slotDescription: ");
        stringBuffer.append(new String(this.slotDescription));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("manufacturerID: ");
        stringBuffer.append(new String(this.manufacturerID));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("flags: ");
        stringBuffer.append(Functions.slotInfoFlagsToString(this.flags));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("hardwareVersion: ");
        stringBuffer.append(this.hardwareVersion.toString());
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("firmwareVersion: ");
        stringBuffer.append(this.firmwareVersion.toString());
        return stringBuffer.toString();
    }
}
