package sun.security.jgss;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.ietf.jgss.GSSException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

/* loaded from: rt.jar:sun/security/jgss/GSSHeader.class */
public class GSSHeader {
    private ObjectIdentifier mechOid;
    private byte[] mechOidBytes;
    private int mechTokenLength;
    public static final int TOKEN_ID = 96;

    public GSSHeader(ObjectIdentifier objectIdentifier, int i2) throws IOException {
        this.mechOid = null;
        this.mechOidBytes = null;
        this.mechTokenLength = 0;
        this.mechOid = objectIdentifier;
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putOID(objectIdentifier);
        this.mechOidBytes = derOutputStream.toByteArray();
        this.mechTokenLength = i2;
    }

    public GSSHeader(InputStream inputStream) throws IOException, GSSException {
        this.mechOid = null;
        this.mechOidBytes = null;
        this.mechTokenLength = 0;
        if (inputStream.read() != 96) {
            throw new GSSException(10, -1, "GSSHeader did not find the right tag");
        }
        int length = getLength(inputStream);
        DerValue derValue = new DerValue(inputStream);
        this.mechOidBytes = derValue.toByteArray();
        this.mechOid = derValue.getOID();
        this.mechTokenLength = length - this.mechOidBytes.length;
    }

    public ObjectIdentifier getOid() {
        return this.mechOid;
    }

    public int getMechTokenLength() {
        return this.mechTokenLength;
    }

    public int getLength() {
        return 1 + getLenFieldSize(this.mechOidBytes.length + this.mechTokenLength) + this.mechOidBytes.length;
    }

    public static int getMaxMechTokenSize(ObjectIdentifier objectIdentifier, int i2) {
        int length = 0;
        try {
            DerOutputStream derOutputStream = new DerOutputStream();
            derOutputStream.putOID(objectIdentifier);
            length = derOutputStream.toByteArray().length;
        } catch (IOException e2) {
        }
        return (i2 - (1 + length)) - 5;
    }

    private int getLenFieldSize(int i2) {
        int i3;
        if (i2 < 128) {
            i3 = 1;
        } else if (i2 < 256) {
            i3 = 2;
        } else if (i2 < 65536) {
            i3 = 3;
        } else if (i2 < 16777216) {
            i3 = 4;
        } else {
            i3 = 5;
        }
        return i3;
    }

    public int encode(OutputStream outputStream) throws IOException {
        int length = 1 + this.mechOidBytes.length;
        outputStream.write(96);
        int iPutLength = length + putLength(this.mechOidBytes.length + this.mechTokenLength, outputStream);
        outputStream.write(this.mechOidBytes);
        return iPutLength;
    }

    private int getLength(InputStream inputStream) throws IOException {
        return getLength(inputStream.read(), inputStream);
    }

    private int getLength(int i2, InputStream inputStream) throws IOException {
        int i3;
        if ((i2 & 128) == 0) {
            i3 = i2;
        } else {
            int i4 = i2 & 127;
            if (i4 == 0) {
                return -1;
            }
            if (i4 < 0 || i4 > 4) {
                throw new IOException("DerInputStream.getLength(): lengthTag=" + i4 + ", " + (i4 < 0 ? "incorrect DER encoding." : "too big."));
            }
            i3 = 0;
            while (i4 > 0) {
                i3 = (i3 << 8) + (255 & inputStream.read());
                i4--;
            }
            if (i3 < 0) {
                throw new IOException("Invalid length bytes");
            }
        }
        return i3;
    }

    private int putLength(int i2, OutputStream outputStream) throws IOException {
        int i3;
        if (i2 < 128) {
            outputStream.write((byte) i2);
            i3 = 1;
        } else if (i2 < 256) {
            outputStream.write(-127);
            outputStream.write((byte) i2);
            i3 = 2;
        } else if (i2 < 65536) {
            outputStream.write(-126);
            outputStream.write((byte) (i2 >> 8));
            outputStream.write((byte) i2);
            i3 = 3;
        } else if (i2 < 16777216) {
            outputStream.write(-125);
            outputStream.write((byte) (i2 >> 16));
            outputStream.write((byte) (i2 >> 8));
            outputStream.write((byte) i2);
            i3 = 4;
        } else {
            outputStream.write(-124);
            outputStream.write((byte) (i2 >> 24));
            outputStream.write((byte) (i2 >> 16));
            outputStream.write((byte) (i2 >> 8));
            outputStream.write((byte) i2);
            i3 = 5;
        }
        return i3;
    }

    private void debug(String str) {
        System.err.print(str);
    }

    private String getHexBytes(byte[] bArr, int i2) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = (bArr[i3] >> 4) & 15;
            int i5 = bArr[i3] & 15;
            stringBuffer.append(Integer.toHexString(i4));
            stringBuffer.append(Integer.toHexString(i5));
            stringBuffer.append(' ');
        }
        return stringBuffer.toString();
    }
}
