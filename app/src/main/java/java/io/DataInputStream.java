package java.io;

/* loaded from: rt.jar:java/io/DataInputStream.class */
public class DataInputStream extends FilterInputStream implements DataInput {
    private byte[] bytearr;
    private char[] chararr;
    private byte[] readBuffer;
    private char[] lineBuffer;

    public DataInputStream(InputStream inputStream) {
        super(inputStream);
        this.bytearr = new byte[80];
        this.chararr = new char[80];
        this.readBuffer = new byte[8];
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public final int read(byte[] bArr) throws IOException {
        return this.in.read(bArr, 0, bArr.length);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public final int read(byte[] bArr, int i2, int i3) throws IOException {
        return this.in.read(bArr, i2, i3);
    }

    @Override // java.io.DataInput
    public final void readFully(byte[] bArr) throws IOException {
        readFully(bArr, 0, bArr.length);
    }

    @Override // java.io.DataInput
    public final void readFully(byte[] bArr, int i2, int i3) throws IOException {
        if (i3 < 0) {
            throw new IndexOutOfBoundsException();
        }
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 < i3) {
                int i6 = this.in.read(bArr, i2 + i5, i3 - i5);
                if (i6 < 0) {
                    throw new EOFException();
                }
                i4 = i5 + i6;
            } else {
                return;
            }
        }
    }

    @Override // java.io.DataInput
    public final int skipBytes(int i2) throws IOException {
        int iSkip;
        int i3 = 0;
        while (i3 < i2 && (iSkip = (int) this.in.skip(i2 - i3)) > 0) {
            i3 += iSkip;
        }
        return i3;
    }

    @Override // java.io.DataInput
    public final boolean readBoolean() throws IOException {
        int i2 = this.in.read();
        if (i2 < 0) {
            throw new EOFException();
        }
        return i2 != 0;
    }

    @Override // java.io.DataInput
    public final byte readByte() throws IOException {
        int i2 = this.in.read();
        if (i2 < 0) {
            throw new EOFException();
        }
        return (byte) i2;
    }

    @Override // java.io.DataInput
    public final int readUnsignedByte() throws IOException {
        int i2 = this.in.read();
        if (i2 < 0) {
            throw new EOFException();
        }
        return i2;
    }

    @Override // java.io.DataInput
    public final short readShort() throws IOException {
        int i2 = this.in.read();
        int i3 = this.in.read();
        if ((i2 | i3) < 0) {
            throw new EOFException();
        }
        return (short) ((i2 << 8) + (i3 << 0));
    }

    @Override // java.io.DataInput
    public final int readUnsignedShort() throws IOException {
        int i2 = this.in.read();
        int i3 = this.in.read();
        if ((i2 | i3) < 0) {
            throw new EOFException();
        }
        return (i2 << 8) + (i3 << 0);
    }

    @Override // java.io.DataInput
    public final char readChar() throws IOException {
        int i2 = this.in.read();
        int i3 = this.in.read();
        if ((i2 | i3) < 0) {
            throw new EOFException();
        }
        return (char) ((i2 << 8) + (i3 << 0));
    }

    @Override // java.io.DataInput
    public final int readInt() throws IOException {
        int i2 = this.in.read();
        int i3 = this.in.read();
        int i4 = this.in.read();
        int i5 = this.in.read();
        if ((i2 | i3 | i4 | i5) < 0) {
            throw new EOFException();
        }
        return (i2 << 24) + (i3 << 16) + (i4 << 8) + (i5 << 0);
    }

    @Override // java.io.DataInput
    public final long readLong() throws IOException {
        readFully(this.readBuffer, 0, 8);
        return (this.readBuffer[0] << 56) + ((this.readBuffer[1] & 255) << 48) + ((this.readBuffer[2] & 255) << 40) + ((this.readBuffer[3] & 255) << 32) + ((this.readBuffer[4] & 255) << 24) + ((this.readBuffer[5] & 255) << 16) + ((this.readBuffer[6] & 255) << 8) + ((this.readBuffer[7] & 255) << 0);
    }

    @Override // java.io.DataInput
    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    @Override // java.io.DataInput
    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @Override // java.io.DataInput
    @Deprecated
    public final String readLine() throws IOException {
        int i2;
        char[] cArr = this.lineBuffer;
        if (cArr == null) {
            char[] cArr2 = new char[128];
            this.lineBuffer = cArr2;
            cArr = cArr2;
        }
        int length = cArr.length;
        int i3 = 0;
        while (true) {
            i2 = this.in.read();
            switch (i2) {
                case -1:
                case 10:
                    break;
                case 13:
                    int i4 = this.in.read();
                    if (i4 != 10 && i4 != -1) {
                        if (!(this.in instanceof PushbackInputStream)) {
                            this.in = new PushbackInputStream(this.in);
                        }
                        ((PushbackInputStream) this.in).unread(i4);
                        break;
                    }
                    break;
                default:
                    length--;
                    if (length < 0) {
                        cArr = new char[i3 + 128];
                        length = (cArr.length - i3) - 1;
                        System.arraycopy(this.lineBuffer, 0, cArr, 0, i3);
                        this.lineBuffer = cArr;
                    }
                    int i5 = i3;
                    i3++;
                    cArr[i5] = (char) i2;
            }
        }
        if (i2 == -1 && i3 == 0) {
            return null;
        }
        return String.copyValueOf(cArr, 0, i3);
    }

    @Override // java.io.DataInput
    public final String readUTF() throws IOException {
        return readUTF(this);
    }

    public static final String readUTF(DataInput dataInput) throws IOException {
        byte[] bArr;
        char[] cArr;
        int i2;
        int unsignedShort = dataInput.readUnsignedShort();
        if (dataInput instanceof DataInputStream) {
            DataInputStream dataInputStream = (DataInputStream) dataInput;
            if (dataInputStream.bytearr.length < unsignedShort) {
                dataInputStream.bytearr = new byte[unsignedShort * 2];
                dataInputStream.chararr = new char[unsignedShort * 2];
            }
            cArr = dataInputStream.chararr;
            bArr = dataInputStream.bytearr;
        } else {
            bArr = new byte[unsignedShort];
            cArr = new char[unsignedShort];
        }
        int i3 = 0;
        int i4 = 0;
        dataInput.readFully(bArr, 0, unsignedShort);
        while (i3 < unsignedShort && (i2 = bArr[i3] & 255) <= 127) {
            i3++;
            int i5 = i4;
            i4++;
            cArr[i5] = (char) i2;
        }
        while (i3 < unsignedShort) {
            int i6 = bArr[i3] & 255;
            switch (i6 >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    i3++;
                    int i7 = i4;
                    i4++;
                    cArr[i7] = (char) i6;
                    break;
                case 8:
                case 9:
                case 10:
                case 11:
                default:
                    throw new UTFDataFormatException("malformed input around byte " + i3);
                case 12:
                case 13:
                    i3 += 2;
                    if (i3 > unsignedShort) {
                        throw new UTFDataFormatException("malformed input: partial character at end");
                    }
                    byte b2 = bArr[i3 - 1];
                    if ((b2 & 192) != 128) {
                        throw new UTFDataFormatException("malformed input around byte " + i3);
                    }
                    int i8 = i4;
                    i4++;
                    cArr[i8] = (char) (((i6 & 31) << 6) | (b2 & 63));
                    break;
                case 14:
                    i3 += 3;
                    if (i3 > unsignedShort) {
                        throw new UTFDataFormatException("malformed input: partial character at end");
                    }
                    byte b3 = bArr[i3 - 2];
                    byte b4 = bArr[i3 - 1];
                    if ((b3 & 192) != 128 || (b4 & 192) != 128) {
                        throw new UTFDataFormatException("malformed input around byte " + (i3 - 1));
                    }
                    int i9 = i4;
                    i4++;
                    cArr[i9] = (char) (((i6 & 15) << 12) | ((b3 & 63) << 6) | ((b4 & 63) << 0));
                    break;
                    break;
            }
        }
        return new String(cArr, 0, i4);
    }
}
