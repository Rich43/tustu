package java.rmi.dgc;

import java.io.Serializable;
import java.rmi.server.UID;
import java.security.SecureRandom;

/* loaded from: rt.jar:java/rmi/dgc/VMID.class */
public final class VMID implements Serializable {
    private static final byte[] randomBytes;
    private byte[] addr = randomBytes;
    private UID uid = new UID();
    private static final long serialVersionUID = -538642295484486218L;

    static {
        byte[] bArr = new byte[8];
        new SecureRandom().nextBytes(bArr);
        randomBytes = bArr;
    }

    @Deprecated
    public static boolean isUnique() {
        return true;
    }

    public int hashCode() {
        return this.uid.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof VMID) {
            VMID vmid = (VMID) obj;
            if (!this.uid.equals(vmid.uid)) {
                return false;
            }
            if ((this.addr == null) ^ (vmid.addr == null)) {
                return false;
            }
            if (this.addr != null) {
                if (this.addr.length != vmid.addr.length) {
                    return false;
                }
                for (int i2 = 0; i2 < this.addr.length; i2++) {
                    if (this.addr[i2] != vmid.addr[i2]) {
                        return false;
                    }
                }
                return true;
            }
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.addr != null) {
            for (int i2 = 0; i2 < this.addr.length; i2++) {
                int i3 = this.addr[i2] & 255;
                stringBuffer.append((i3 < 16 ? "0" : "") + Integer.toString(i3, 16));
            }
        }
        stringBuffer.append(':');
        stringBuffer.append(this.uid.toString());
        return stringBuffer.toString();
    }
}
