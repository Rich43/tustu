package sun.security.pkcs11.wrapper;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/CK_VERSION.class */
public class CK_VERSION {
    public byte major;
    public byte minor;

    public CK_VERSION(int i2, int i3) {
        this.major = (byte) i2;
        this.minor = (byte) i3;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.major & 255);
        sb.append('.');
        int i2 = this.minor & 255;
        if (i2 < 10) {
            sb.append('0');
        }
        sb.append(i2);
        return sb.toString();
    }
}
