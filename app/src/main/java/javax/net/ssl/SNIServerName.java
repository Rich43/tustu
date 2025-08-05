package javax.net.ssl;

import java.util.Arrays;
import net.lingala.zip4j.crypto.PBKDF2.BinTools;

/* loaded from: rt.jar:javax/net/ssl/SNIServerName.class */
public abstract class SNIServerName {
    private final int type;
    private final byte[] encoded;
    private static final char[] HEXES = BinTools.hex.toCharArray();

    protected SNIServerName(int i2, byte[] bArr) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Server name type cannot be less than zero");
        }
        if (i2 > 255) {
            throw new IllegalArgumentException("Server name type cannot be greater than 255");
        }
        this.type = i2;
        if (bArr == null) {
            throw new NullPointerException("Server name encoded value cannot be null");
        }
        this.encoded = (byte[]) bArr.clone();
    }

    public final int getType() {
        return this.type;
    }

    public final byte[] getEncoded() {
        return (byte[]) this.encoded.clone();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SNIServerName sNIServerName = (SNIServerName) obj;
        return this.type == sNIServerName.type && Arrays.equals(this.encoded, sNIServerName.encoded);
    }

    public int hashCode() {
        return (31 * ((31 * 17) + this.type)) + Arrays.hashCode(this.encoded);
    }

    public String toString() {
        if (this.type == 0) {
            return "type=host_name (0), value=" + toHexString(this.encoded);
        }
        return "type=(" + this.type + "), value=" + toHexString(this.encoded);
    }

    private static String toHexString(byte[] bArr) {
        if (bArr.length == 0) {
            return "(empty)";
        }
        StringBuilder sb = new StringBuilder((bArr.length * 3) - 1);
        boolean z2 = true;
        for (byte b2 : bArr) {
            if (z2) {
                z2 = false;
            } else {
                sb.append(':');
            }
            int i2 = b2 & 255;
            sb.append(HEXES[i2 >>> 4]);
            sb.append(HEXES[i2 & 15]);
        }
        return sb.toString();
    }
}
