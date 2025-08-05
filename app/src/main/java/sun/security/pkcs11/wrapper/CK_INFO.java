package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_INFO.class */
public class CK_INFO {
    public CK_VERSION cryptokiVersion;
    public char[] manufacturerID;
    public long flags;
    public char[] libraryDescription;
    public CK_VERSION libraryVersion;

    public CK_INFO(CK_VERSION ck_version, char[] cArr, long j2, char[] cArr2, CK_VERSION ck_version2) {
        this.cryptokiVersion = ck_version;
        this.manufacturerID = cArr;
        this.flags = j2;
        this.libraryDescription = cArr2;
        this.libraryVersion = ck_version2;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("cryptokiVersion: ");
        stringBuffer.append(this.cryptokiVersion.toString());
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("manufacturerID: ");
        stringBuffer.append(new String(this.manufacturerID));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("flags: ");
        stringBuffer.append(Functions.toBinaryString(this.flags));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("libraryDescription: ");
        stringBuffer.append(new String(this.libraryDescription));
        stringBuffer.append(Constants.NEWLINE);
        stringBuffer.append(Constants.INDENT);
        stringBuffer.append("libraryVersion: ");
        stringBuffer.append(this.libraryVersion.toString());
        return stringBuffer.toString();
    }
}
