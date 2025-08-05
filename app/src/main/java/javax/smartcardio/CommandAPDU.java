package javax.smartcardio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;

/* loaded from: rt.jar:javax/smartcardio/CommandAPDU.class */
public final class CommandAPDU implements Serializable {
    private static final long serialVersionUID = 398698301286670877L;
    private static final int MAX_APDU_SIZE = 65544;
    private byte[] apdu;
    private transient int nc;
    private transient int ne;
    private transient int dataOffset;

    public CommandAPDU(byte[] bArr) {
        this.apdu = (byte[]) bArr.clone();
        parse();
    }

    public CommandAPDU(byte[] bArr, int i2, int i3) {
        checkArrayBounds(bArr, i2, i3);
        this.apdu = new byte[i3];
        System.arraycopy(bArr, i2, this.apdu, 0, i3);
        parse();
    }

    private void checkArrayBounds(byte[] bArr, int i2, int i3) {
        if (i2 < 0 || i3 < 0) {
            throw new IllegalArgumentException("Offset and length must not be negative");
        }
        if (bArr == null) {
            if (i2 != 0 && i3 != 0) {
                throw new IllegalArgumentException("offset and length must be 0 if array is null");
            }
        } else if (i2 > bArr.length - i3) {
            throw new IllegalArgumentException("Offset plus length exceed array size");
        }
    }

    public CommandAPDU(ByteBuffer byteBuffer) {
        this.apdu = new byte[byteBuffer.remaining()];
        byteBuffer.get(this.apdu);
        parse();
    }

    public CommandAPDU(int i2, int i3, int i4, int i5) {
        this(i2, i3, i4, i5, null, 0, 0, 0);
    }

    public CommandAPDU(int i2, int i3, int i4, int i5, int i6) {
        this(i2, i3, i4, i5, null, 0, 0, i6);
    }

    public CommandAPDU(int i2, int i3, int i4, int i5, byte[] bArr) {
        this(i2, i3, i4, i5, bArr, 0, arrayLength(bArr), 0);
    }

    public CommandAPDU(int i2, int i3, int i4, int i5, byte[] bArr, int i6, int i7) {
        this(i2, i3, i4, i5, bArr, i6, i7, 0);
    }

    public CommandAPDU(int i2, int i3, int i4, int i5, byte[] bArr, int i6) {
        this(i2, i3, i4, i5, bArr, 0, arrayLength(bArr), i6);
    }

    private static int arrayLength(byte[] bArr) {
        if (bArr != null) {
            return bArr.length;
        }
        return 0;
    }

    private void parse() {
        if (this.apdu.length < 4) {
            throw new IllegalArgumentException("apdu must be at least 4 bytes long");
        }
        if (this.apdu.length == 4) {
            return;
        }
        int i2 = this.apdu[4] & 255;
        if (this.apdu.length == 5) {
            this.ne = i2 == 0 ? 256 : i2;
            return;
        }
        if (i2 != 0) {
            if (this.apdu.length == 5 + i2) {
                this.nc = i2;
                this.dataOffset = 5;
                return;
            } else {
                if (this.apdu.length == 6 + i2) {
                    this.nc = i2;
                    this.dataOffset = 5;
                    int i3 = this.apdu[this.apdu.length - 1] & 255;
                    this.ne = i3 == 0 ? 256 : i3;
                    return;
                }
                throw new IllegalArgumentException("Invalid APDU: length=" + this.apdu.length + ", b1=" + i2);
            }
        }
        if (this.apdu.length < 7) {
            throw new IllegalArgumentException("Invalid APDU: length=" + this.apdu.length + ", b1=" + i2);
        }
        int i4 = ((this.apdu[5] & 255) << 8) | (this.apdu[6] & 255);
        if (this.apdu.length == 7) {
            this.ne = i4 == 0 ? 65536 : i4;
            return;
        }
        if (i4 == 0) {
            throw new IllegalArgumentException("Invalid APDU: length=" + this.apdu.length + ", b1=" + i2 + ", b2||b3=" + i4);
        }
        if (this.apdu.length == 7 + i4) {
            this.nc = i4;
            this.dataOffset = 7;
        } else {
            if (this.apdu.length == 9 + i4) {
                this.nc = i4;
                this.dataOffset = 7;
                int length = this.apdu.length - 2;
                int i5 = ((this.apdu[length] & 255) << 8) | (this.apdu[length + 1] & 255);
                this.ne = i5 == 0 ? 65536 : i5;
                return;
            }
            throw new IllegalArgumentException("Invalid APDU: length=" + this.apdu.length + ", b1=" + i2 + ", b2||b3=" + i4);
        }
    }

    public CommandAPDU(int i2, int i3, int i4, int i5, byte[] bArr, int i6, int i7, int i8) {
        byte b2;
        byte b3;
        checkArrayBounds(bArr, i6, i7);
        if (i7 > 65535) {
            throw new IllegalArgumentException("dataLength is too large");
        }
        if (i8 < 0) {
            throw new IllegalArgumentException("ne must not be negative");
        }
        if (i8 > 65536) {
            throw new IllegalArgumentException("ne is too large");
        }
        this.ne = i8;
        this.nc = i7;
        if (i7 == 0) {
            if (i8 == 0) {
                this.apdu = new byte[4];
                setHeader(i2, i3, i4, i5);
                return;
            }
            if (i8 <= 256) {
                byte b4 = i8 != 256 ? (byte) i8 : (byte) 0;
                this.apdu = new byte[5];
                setHeader(i2, i3, i4, i5);
                this.apdu[4] = b4;
                return;
            }
            if (i8 == 65536) {
                b2 = 0;
                b3 = 0;
            } else {
                b2 = (byte) (i8 >> 8);
                b3 = (byte) i8;
            }
            this.apdu = new byte[7];
            setHeader(i2, i3, i4, i5);
            this.apdu[5] = b2;
            this.apdu[6] = b3;
            return;
        }
        if (i8 == 0) {
            if (i7 <= 255) {
                this.apdu = new byte[5 + i7];
                setHeader(i2, i3, i4, i5);
                this.apdu[4] = (byte) i7;
                this.dataOffset = 5;
                System.arraycopy(bArr, i6, this.apdu, 5, i7);
                return;
            }
            this.apdu = new byte[7 + i7];
            setHeader(i2, i3, i4, i5);
            this.apdu[4] = 0;
            this.apdu[5] = (byte) (i7 >> 8);
            this.apdu[6] = (byte) i7;
            this.dataOffset = 7;
            System.arraycopy(bArr, i6, this.apdu, 7, i7);
            return;
        }
        if (i7 <= 255 && i8 <= 256) {
            this.apdu = new byte[6 + i7];
            setHeader(i2, i3, i4, i5);
            this.apdu[4] = (byte) i7;
            this.dataOffset = 5;
            System.arraycopy(bArr, i6, this.apdu, 5, i7);
            this.apdu[this.apdu.length - 1] = i8 != 256 ? (byte) i8 : (byte) 0;
            return;
        }
        this.apdu = new byte[9 + i7];
        setHeader(i2, i3, i4, i5);
        this.apdu[4] = 0;
        this.apdu[5] = (byte) (i7 >> 8);
        this.apdu[6] = (byte) i7;
        this.dataOffset = 7;
        System.arraycopy(bArr, i6, this.apdu, 7, i7);
        if (i8 != 65536) {
            int length = this.apdu.length - 2;
            this.apdu[length] = (byte) (i8 >> 8);
            this.apdu[length + 1] = (byte) i8;
        }
    }

    private void setHeader(int i2, int i3, int i4, int i5) {
        this.apdu[0] = (byte) i2;
        this.apdu[1] = (byte) i3;
        this.apdu[2] = (byte) i4;
        this.apdu[3] = (byte) i5;
    }

    public int getCLA() {
        return this.apdu[0] & 255;
    }

    public int getINS() {
        return this.apdu[1] & 255;
    }

    public int getP1() {
        return this.apdu[2] & 255;
    }

    public int getP2() {
        return this.apdu[3] & 255;
    }

    public int getNc() {
        return this.nc;
    }

    public byte[] getData() {
        byte[] bArr = new byte[this.nc];
        System.arraycopy(this.apdu, this.dataOffset, bArr, 0, this.nc);
        return bArr;
    }

    public int getNe() {
        return this.ne;
    }

    public byte[] getBytes() {
        return (byte[]) this.apdu.clone();
    }

    public String toString() {
        return "CommmandAPDU: " + this.apdu.length + " bytes, nc=" + this.nc + ", ne=" + this.ne;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CommandAPDU)) {
            return false;
        }
        return Arrays.equals(this.apdu, ((CommandAPDU) obj).apdu);
    }

    public int hashCode() {
        return Arrays.hashCode(this.apdu);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.apdu = (byte[]) objectInputStream.readUnshared();
        parse();
    }
}
