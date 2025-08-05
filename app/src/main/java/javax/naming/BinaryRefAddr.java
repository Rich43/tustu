package javax.naming;

/* loaded from: rt.jar:javax/naming/BinaryRefAddr.class */
public class BinaryRefAddr extends RefAddr {
    private byte[] buf;
    private static final long serialVersionUID = -3415254970957330361L;

    public BinaryRefAddr(String str, byte[] bArr) {
        this(str, bArr, 0, bArr.length);
    }

    public BinaryRefAddr(String str, byte[] bArr, int i2, int i3) {
        super(str);
        this.buf = null;
        this.buf = new byte[i3];
        System.arraycopy(bArr, i2, this.buf, 0, i3);
    }

    @Override // javax.naming.RefAddr
    public Object getContent() {
        return this.buf;
    }

    @Override // javax.naming.RefAddr
    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof BinaryRefAddr)) {
            BinaryRefAddr binaryRefAddr = (BinaryRefAddr) obj;
            if (this.addrType.compareTo(binaryRefAddr.addrType) == 0) {
                if (this.buf == null && binaryRefAddr.buf == null) {
                    return true;
                }
                if (this.buf == null || binaryRefAddr.buf == null || this.buf.length != binaryRefAddr.buf.length) {
                    return false;
                }
                for (int i2 = 0; i2 < this.buf.length; i2++) {
                    if (this.buf[i2] != binaryRefAddr.buf[i2]) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // javax.naming.RefAddr
    public int hashCode() {
        int iHashCode = this.addrType.hashCode();
        for (int i2 = 0; i2 < this.buf.length; i2++) {
            iHashCode += this.buf[i2];
        }
        return iHashCode;
    }

    @Override // javax.naming.RefAddr
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("Address Type: " + this.addrType + "\n");
        stringBuffer.append("AddressContents: ");
        for (int i2 = 0; i2 < this.buf.length && i2 < 32; i2++) {
            stringBuffer.append(Integer.toHexString(this.buf[i2]) + " ");
        }
        if (this.buf.length >= 32) {
            stringBuffer.append(" ...\n");
        }
        return stringBuffer.toString();
    }
}
