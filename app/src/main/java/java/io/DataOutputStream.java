package java.io;

/* loaded from: rt.jar:java/io/DataOutputStream.class */
public class DataOutputStream extends FilterOutputStream implements DataOutput {
    protected int written;
    private byte[] bytearr;
    private byte[] writeBuffer;

    public DataOutputStream(OutputStream outputStream) {
        super(outputStream);
        this.bytearr = null;
        this.writeBuffer = new byte[8];
    }

    private void incCount(int i2) {
        int i3 = this.written + i2;
        if (i3 < 0) {
            i3 = Integer.MAX_VALUE;
        }
        this.written = i3;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public synchronized void write(int i2) throws IOException {
        this.out.write(i2);
        incCount(1);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public synchronized void write(byte[] bArr, int i2, int i3) throws IOException {
        this.out.write(bArr, i2, i3);
        incCount(i3);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override // java.io.DataOutput
    public final void writeBoolean(boolean z2) throws IOException {
        this.out.write(z2 ? 1 : 0);
        incCount(1);
    }

    @Override // java.io.DataOutput
    public final void writeByte(int i2) throws IOException {
        this.out.write(i2);
        incCount(1);
    }

    @Override // java.io.DataOutput
    public final void writeShort(int i2) throws IOException {
        this.out.write((i2 >>> 8) & 255);
        this.out.write((i2 >>> 0) & 255);
        incCount(2);
    }

    @Override // java.io.DataOutput
    public final void writeChar(int i2) throws IOException {
        this.out.write((i2 >>> 8) & 255);
        this.out.write((i2 >>> 0) & 255);
        incCount(2);
    }

    @Override // java.io.DataOutput
    public final void writeInt(int i2) throws IOException {
        this.out.write((i2 >>> 24) & 255);
        this.out.write((i2 >>> 16) & 255);
        this.out.write((i2 >>> 8) & 255);
        this.out.write((i2 >>> 0) & 255);
        incCount(4);
    }

    @Override // java.io.DataOutput
    public final void writeLong(long j2) throws IOException {
        this.writeBuffer[0] = (byte) (j2 >>> 56);
        this.writeBuffer[1] = (byte) (j2 >>> 48);
        this.writeBuffer[2] = (byte) (j2 >>> 40);
        this.writeBuffer[3] = (byte) (j2 >>> 32);
        this.writeBuffer[4] = (byte) (j2 >>> 24);
        this.writeBuffer[5] = (byte) (j2 >>> 16);
        this.writeBuffer[6] = (byte) (j2 >>> 8);
        this.writeBuffer[7] = (byte) (j2 >>> 0);
        this.out.write(this.writeBuffer, 0, 8);
        incCount(8);
    }

    @Override // java.io.DataOutput
    public final void writeFloat(float f2) throws IOException {
        writeInt(Float.floatToIntBits(f2));
    }

    @Override // java.io.DataOutput
    public final void writeDouble(double d2) throws IOException {
        writeLong(Double.doubleToLongBits(d2));
    }

    @Override // java.io.DataOutput
    public final void writeBytes(String str) throws IOException {
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            this.out.write((byte) str.charAt(i2));
        }
        incCount(length);
    }

    @Override // java.io.DataOutput
    public final void writeChars(String str) throws IOException {
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            this.out.write((cCharAt >>> '\b') & 255);
            this.out.write((cCharAt >>> 0) & 255);
        }
        incCount(length * 2);
    }

    @Override // java.io.DataOutput
    public final void writeUTF(String str) throws IOException {
        writeUTF(str, this);
    }

    static int writeUTF(String str, DataOutput dataOutput) throws IOException {
        byte[] bArr;
        char cCharAt;
        int length = str.length();
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            char cCharAt2 = str.charAt(i3);
            if (cCharAt2 >= 1 && cCharAt2 <= 127) {
                i2++;
            } else if (cCharAt2 > 2047) {
                i2 += 3;
            } else {
                i2 += 2;
            }
        }
        if (i2 > 65535) {
            throw new UTFDataFormatException("encoded string too long: " + i2 + " bytes");
        }
        if (dataOutput instanceof DataOutputStream) {
            DataOutputStream dataOutputStream = (DataOutputStream) dataOutput;
            if (dataOutputStream.bytearr == null || dataOutputStream.bytearr.length < i2 + 2) {
                dataOutputStream.bytearr = new byte[(i2 * 2) + 2];
            }
            bArr = dataOutputStream.bytearr;
        } else {
            bArr = new byte[i2 + 2];
        }
        int i4 = 0 + 1;
        bArr[0] = (byte) ((i2 >>> 8) & 255);
        int i5 = i4 + 1;
        bArr[i4] = (byte) ((i2 >>> 0) & 255);
        int i6 = 0;
        while (i6 < length && (cCharAt = str.charAt(i6)) >= 1 && cCharAt <= 127) {
            int i7 = i5;
            i5++;
            bArr[i7] = (byte) cCharAt;
            i6++;
        }
        while (i6 < length) {
            char cCharAt3 = str.charAt(i6);
            if (cCharAt3 >= 1 && cCharAt3 <= 127) {
                int i8 = i5;
                i5++;
                bArr[i8] = (byte) cCharAt3;
            } else if (cCharAt3 > 2047) {
                int i9 = i5;
                int i10 = i5 + 1;
                bArr[i9] = (byte) (224 | ((cCharAt3 >> '\f') & 15));
                int i11 = i10 + 1;
                bArr[i10] = (byte) (128 | ((cCharAt3 >> 6) & 63));
                i5 = i11 + 1;
                bArr[i11] = (byte) (128 | ((cCharAt3 >> 0) & 63));
            } else {
                int i12 = i5;
                int i13 = i5 + 1;
                bArr[i12] = (byte) (192 | ((cCharAt3 >> 6) & 31));
                i5 = i13 + 1;
                bArr[i13] = (byte) (128 | ((cCharAt3 >> 0) & 63));
            }
            i6++;
        }
        dataOutput.write(bArr, 0, i2 + 2);
        return i2 + 2;
    }

    public final int size() {
        return this.written;
    }
}
