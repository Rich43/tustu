package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_TOKEN_INFO.class */
public class CK_TOKEN_INFO {
    public char[] label;
    public char[] manufacturerID;
    public char[] model;
    public char[] serialNumber;
    public long flags;
    public long ulMaxSessionCount;
    public long ulSessionCount;
    public long ulMaxRwSessionCount;
    public long ulRwSessionCount;
    public long ulMaxPinLen;
    public long ulMinPinLen;
    public long ulTotalPublicMemory;
    public long ulFreePublicMemory;
    public long ulTotalPrivateMemory;
    public long ulFreePrivateMemory;
    public CK_VERSION hardwareVersion;
    public CK_VERSION firmwareVersion;
    public char[] utcTime;

    public CK_TOKEN_INFO(char[] cArr, char[] cArr2, char[] cArr3, char[] cArr4, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, long j10, long j11, long j12, CK_VERSION ck_version, CK_VERSION ck_version2, char[] cArr5) {
        this.label = cArr;
        this.manufacturerID = cArr2;
        this.model = cArr3;
        this.serialNumber = cArr4;
        this.flags = j2;
        this.ulMaxSessionCount = j3;
        this.ulSessionCount = j4;
        this.ulMaxRwSessionCount = j5;
        this.ulRwSessionCount = j6;
        this.ulMaxPinLen = j7;
        this.ulMinPinLen = j8;
        this.ulTotalPublicMemory = j9;
        this.ulFreePublicMemory = j10;
        this.ulTotalPrivateMemory = j11;
        this.ulFreePrivateMemory = j12;
        this.hardwareVersion = ck_version;
        this.firmwareVersion = ck_version2;
        this.utcTime = cArr5;
    }

    public String toString() {
        String strValueOf;
        String strValueOf2;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("label: ");
        stringBuffer.append(new String(this.label));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("manufacturerID: ");
        stringBuffer.append(new String(this.manufacturerID));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("model: ");
        stringBuffer.append(new String(this.model));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("serialNumber: ");
        stringBuffer.append(new String(this.serialNumber));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("flags: ");
        stringBuffer.append(Functions.tokenInfoFlagsToString(this.flags));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("ulMaxSessionCount: ");
        if (this.ulMaxSessionCount == 0) {
            strValueOf = "CK_EFFECTIVELY_INFINITE";
        } else {
            strValueOf = this.ulMaxSessionCount == -1 ? "CK_UNAVAILABLE_INFORMATION" : String.valueOf(this.ulMaxSessionCount);
        }
        stringBuffer.append(strValueOf);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("ulSessionCount: ");
        stringBuffer.append(this.ulSessionCount == -1 ? "CK_UNAVAILABLE_INFORMATION" : String.valueOf(this.ulSessionCount));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("ulMaxRwSessionCount: ");
        if (this.ulMaxRwSessionCount == 0) {
            strValueOf2 = "CK_EFFECTIVELY_INFINITE";
        } else {
            strValueOf2 = this.ulMaxRwSessionCount == -1 ? "CK_UNAVAILABLE_INFORMATION" : String.valueOf(this.ulMaxRwSessionCount);
        }
        stringBuffer.append(strValueOf2);
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("ulRwSessionCount: ");
        stringBuffer.append(this.ulRwSessionCount == -1 ? "CK_UNAVAILABLE_INFORMATION" : String.valueOf(this.ulRwSessionCount));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("ulMaxPinLen: ");
        stringBuffer.append(String.valueOf(this.ulMaxPinLen));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("ulMinPinLen: ");
        stringBuffer.append(String.valueOf(this.ulMinPinLen));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("ulTotalPublicMemory: ");
        stringBuffer.append(this.ulTotalPublicMemory == -1 ? "CK_UNAVAILABLE_INFORMATION" : String.valueOf(this.ulTotalPublicMemory));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("ulFreePublicMemory: ");
        stringBuffer.append(this.ulFreePublicMemory == -1 ? "CK_UNAVAILABLE_INFORMATION" : String.valueOf(this.ulFreePublicMemory));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("ulTotalPrivateMemory: ");
        stringBuffer.append(this.ulTotalPrivateMemory == -1 ? "CK_UNAVAILABLE_INFORMATION" : String.valueOf(this.ulTotalPrivateMemory));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("ulFreePrivateMemory: ");
        stringBuffer.append(this.ulFreePrivateMemory == -1 ? "CK_UNAVAILABLE_INFORMATION" : String.valueOf(this.ulFreePrivateMemory));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("hardwareVersion: ");
        stringBuffer.append(this.hardwareVersion.toString());
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("firmwareVersion: ");
        stringBuffer.append(this.firmwareVersion.toString());
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("utcTime: ");
        stringBuffer.append(new String(this.utcTime));
        return stringBuffer.toString();
    }
}
