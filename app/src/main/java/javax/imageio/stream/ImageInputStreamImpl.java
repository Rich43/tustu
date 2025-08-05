package javax.imageio.stream;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Stack;
import javax.imageio.IIOException;
import javax.swing.JInternalFrame;

/* loaded from: rt.jar:javax/imageio/stream/ImageInputStreamImpl.class */
public abstract class ImageInputStreamImpl implements ImageInputStream {
    private static final int BYTE_BUF_LENGTH = 8192;
    protected long streamPos;
    protected int bitOffset;
    private Stack markByteStack = new Stack();
    private Stack markBitStack = new Stack();
    private boolean isClosed = false;
    byte[] byteBuf = new byte[8192];
    protected ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
    protected long flushedPos = 0;

    @Override // javax.imageio.stream.ImageInputStream
    public abstract int read() throws IOException;

    @Override // javax.imageio.stream.ImageInputStream
    public abstract int read(byte[] bArr, int i2, int i3) throws IOException;

    protected final void checkClosed() throws IOException {
        if (this.isClosed) {
            throw new IOException(JInternalFrame.IS_CLOSED_PROPERTY);
        }
    }

    @Override // javax.imageio.stream.ImageInputStream
    public void setByteOrder(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
    }

    @Override // javax.imageio.stream.ImageInputStream
    public ByteOrder getByteOrder() {
        return this.byteOrder;
    }

    @Override // javax.imageio.stream.ImageInputStream
    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    @Override // javax.imageio.stream.ImageInputStream
    public void readBytes(IIOByteBuffer iIOByteBuffer, int i2) throws IOException {
        if (i2 < 0) {
            throw new IndexOutOfBoundsException("len < 0!");
        }
        if (iIOByteBuffer == null) {
            throw new NullPointerException("buf == null!");
        }
        byte[] bArr = new byte[i2];
        int i3 = read(bArr, 0, i2);
        iIOByteBuffer.setData(bArr);
        iIOByteBuffer.setOffset(0);
        iIOByteBuffer.setLength(i3);
    }

    @Override // javax.imageio.stream.ImageInputStream, java.io.DataInput
    public boolean readBoolean() throws IOException {
        int i2 = read();
        if (i2 < 0) {
            throw new EOFException();
        }
        return i2 != 0;
    }

    @Override // javax.imageio.stream.ImageInputStream, java.io.DataInput
    public byte readByte() throws IOException {
        int i2 = read();
        if (i2 < 0) {
            throw new EOFException();
        }
        return (byte) i2;
    }

    @Override // javax.imageio.stream.ImageInputStream, java.io.DataInput
    public int readUnsignedByte() throws IOException {
        int i2 = read();
        if (i2 < 0) {
            throw new EOFException();
        }
        return i2;
    }

    @Override // javax.imageio.stream.ImageInputStream, java.io.DataInput
    public short readShort() throws IOException {
        if (read(this.byteBuf, 0, 2) != 2) {
            throw new EOFException();
        }
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            return (short) (((this.byteBuf[0] & 255) << 8) | ((this.byteBuf[1] & 255) << 0));
        }
        return (short) (((this.byteBuf[1] & 255) << 8) | ((this.byteBuf[0] & 255) << 0));
    }

    @Override // javax.imageio.stream.ImageInputStream, java.io.DataInput
    public int readUnsignedShort() throws IOException {
        return readShort() & 65535;
    }

    @Override // javax.imageio.stream.ImageInputStream, java.io.DataInput
    public char readChar() throws IOException {
        return (char) readShort();
    }

    @Override // javax.imageio.stream.ImageInputStream, java.io.DataInput
    public int readInt() throws IOException {
        if (read(this.byteBuf, 0, 4) != 4) {
            throw new EOFException();
        }
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            return ((this.byteBuf[0] & 255) << 24) | ((this.byteBuf[1] & 255) << 16) | ((this.byteBuf[2] & 255) << 8) | ((this.byteBuf[3] & 255) << 0);
        }
        return ((this.byteBuf[3] & 255) << 24) | ((this.byteBuf[2] & 255) << 16) | ((this.byteBuf[1] & 255) << 8) | ((this.byteBuf[0] & 255) << 0);
    }

    @Override // javax.imageio.stream.ImageInputStream
    public long readUnsignedInt() throws IOException {
        return readInt() & 4294967295L;
    }

    @Override // javax.imageio.stream.ImageInputStream, java.io.DataInput
    public long readLong() throws IOException {
        int i2 = readInt();
        int i3 = readInt();
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            return (i2 << 32) + (i3 & 4294967295L);
        }
        return (i3 << 32) + (i2 & 4294967295L);
    }

    @Override // javax.imageio.stream.ImageInputStream, java.io.DataInput
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    @Override // javax.imageio.stream.ImageInputStream, java.io.DataInput
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @Override // javax.imageio.stream.ImageInputStream, java.io.DataInput
    public String readLine() throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        int i2 = -1;
        boolean z2 = false;
        while (!z2) {
            int i3 = read();
            i2 = i3;
            switch (i3) {
                case -1:
                case 10:
                    z2 = true;
                    break;
                case 13:
                    z2 = true;
                    long streamPosition = getStreamPosition();
                    if (read() == 10) {
                        break;
                    } else {
                        seek(streamPosition);
                        break;
                    }
                default:
                    stringBuffer.append((char) i2);
                    break;
            }
        }
        if (i2 == -1 && stringBuffer.length() == 0) {
            return null;
        }
        return stringBuffer.toString();
    }

    @Override // javax.imageio.stream.ImageInputStream, java.io.DataInput
    public String readUTF() throws IOException {
        this.bitOffset = 0;
        ByteOrder byteOrder = getByteOrder();
        setByteOrder(ByteOrder.BIG_ENDIAN);
        try {
            String utf = DataInputStream.readUTF(this);
            setByteOrder(byteOrder);
            return utf;
        } catch (IOException e2) {
            setByteOrder(byteOrder);
            throw e2;
        }
    }

    @Override // javax.imageio.stream.ImageInputStream, java.io.DataInput
    public void readFully(byte[] bArr, int i2, int i3) throws IOException {
        if (i2 < 0 || i3 < 0 || i2 + i3 > bArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > b.length!");
        }
        while (i3 > 0) {
            int i4 = read(bArr, i2, i3);
            if (i4 == -1) {
                throw new EOFException();
            }
            i2 += i4;
            i3 -= i4;
        }
    }

    @Override // javax.imageio.stream.ImageInputStream, java.io.DataInput
    public void readFully(byte[] bArr) throws IOException {
        readFully(bArr, 0, bArr.length);
    }

    @Override // javax.imageio.stream.ImageInputStream
    public void readFully(short[] sArr, int i2, int i3) throws IOException {
        if (i2 < 0 || i3 < 0 || i2 + i3 > sArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > s.length!");
        }
        while (i3 > 0) {
            int iMin = Math.min(i3, this.byteBuf.length / 2);
            readFully(this.byteBuf, 0, iMin * 2);
            toShorts(this.byteBuf, sArr, i2, iMin);
            i2 += iMin;
            i3 -= iMin;
        }
    }

    @Override // javax.imageio.stream.ImageInputStream
    public void readFully(char[] cArr, int i2, int i3) throws IOException {
        if (i2 < 0 || i3 < 0 || i2 + i3 > cArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > c.length!");
        }
        while (i3 > 0) {
            int iMin = Math.min(i3, this.byteBuf.length / 2);
            readFully(this.byteBuf, 0, iMin * 2);
            toChars(this.byteBuf, cArr, i2, iMin);
            i2 += iMin;
            i3 -= iMin;
        }
    }

    @Override // javax.imageio.stream.ImageInputStream
    public void readFully(int[] iArr, int i2, int i3) throws IOException {
        if (i2 < 0 || i3 < 0 || i2 + i3 > iArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > i.length!");
        }
        while (i3 > 0) {
            int iMin = Math.min(i3, this.byteBuf.length / 4);
            readFully(this.byteBuf, 0, iMin * 4);
            toInts(this.byteBuf, iArr, i2, iMin);
            i2 += iMin;
            i3 -= iMin;
        }
    }

    @Override // javax.imageio.stream.ImageInputStream
    public void readFully(long[] jArr, int i2, int i3) throws IOException {
        if (i2 < 0 || i3 < 0 || i2 + i3 > jArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > l.length!");
        }
        while (i3 > 0) {
            int iMin = Math.min(i3, this.byteBuf.length / 8);
            readFully(this.byteBuf, 0, iMin * 8);
            toLongs(this.byteBuf, jArr, i2, iMin);
            i2 += iMin;
            i3 -= iMin;
        }
    }

    @Override // javax.imageio.stream.ImageInputStream
    public void readFully(float[] fArr, int i2, int i3) throws IOException {
        if (i2 < 0 || i3 < 0 || i2 + i3 > fArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > f.length!");
        }
        while (i3 > 0) {
            int iMin = Math.min(i3, this.byteBuf.length / 4);
            readFully(this.byteBuf, 0, iMin * 4);
            toFloats(this.byteBuf, fArr, i2, iMin);
            i2 += iMin;
            i3 -= iMin;
        }
    }

    @Override // javax.imageio.stream.ImageInputStream
    public void readFully(double[] dArr, int i2, int i3) throws IOException {
        if (i2 < 0 || i3 < 0 || i2 + i3 > dArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException("off < 0 || len < 0 || off + len > d.length!");
        }
        while (i3 > 0) {
            int iMin = Math.min(i3, this.byteBuf.length / 8);
            readFully(this.byteBuf, 0, iMin * 8);
            toDoubles(this.byteBuf, dArr, i2, iMin);
            i2 += iMin;
            i3 -= iMin;
        }
    }

    private void toShorts(byte[] bArr, short[] sArr, int i2, int i3) {
        int i4 = 0;
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int i5 = 0; i5 < i3; i5++) {
                sArr[i2 + i5] = (short) ((bArr[i4] << 8) | (bArr[i4 + 1] & 255));
                i4 += 2;
            }
            return;
        }
        for (int i6 = 0; i6 < i3; i6++) {
            sArr[i2 + i6] = (short) ((bArr[i4 + 1] << 8) | (bArr[i4] & 255));
            i4 += 2;
        }
    }

    private void toChars(byte[] bArr, char[] cArr, int i2, int i3) {
        int i4 = 0;
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int i5 = 0; i5 < i3; i5++) {
                cArr[i2 + i5] = (char) ((bArr[i4] << 8) | (bArr[i4 + 1] & 255));
                i4 += 2;
            }
            return;
        }
        for (int i6 = 0; i6 < i3; i6++) {
            cArr[i2 + i6] = (char) ((bArr[i4 + 1] << 8) | (bArr[i4] & 255));
            i4 += 2;
        }
    }

    private void toInts(byte[] bArr, int[] iArr, int i2, int i3) {
        int i4 = 0;
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int i5 = 0; i5 < i3; i5++) {
                iArr[i2 + i5] = (bArr[i4] << 24) | ((bArr[i4 + 1] & 255) << 16) | ((bArr[i4 + 2] & 255) << 8) | (bArr[i4 + 3] & 255);
                i4 += 4;
            }
            return;
        }
        for (int i6 = 0; i6 < i3; i6++) {
            iArr[i2 + i6] = (bArr[i4 + 3] << 24) | ((bArr[i4 + 2] & 255) << 16) | ((bArr[i4 + 1] & 255) << 8) | (bArr[i4] & 255);
            i4 += 4;
        }
    }

    private void toLongs(byte[] bArr, long[] jArr, int i2, int i3) {
        int i4 = 0;
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int i5 = 0; i5 < i3; i5++) {
                byte b2 = bArr[i4];
                int i6 = bArr[i4 + 1] & 255;
                int i7 = bArr[i4 + 2] & 255;
                int i8 = bArr[i4 + 3] & 255;
                byte b3 = bArr[i4 + 4];
                int i9 = bArr[i4 + 5] & 255;
                int i10 = bArr[i4 + 6] & 255;
                jArr[i2 + i5] = (((((b2 << 24) | (i6 << 16)) | (i7 << 8)) | i8) << 32) | (((b3 << 24) | (i9 << 16) | (i10 << 8) | (bArr[i4 + 7] & 255)) & 4294967295L);
                i4 += 8;
            }
            return;
        }
        for (int i11 = 0; i11 < i3; i11++) {
            byte b4 = bArr[i4 + 7];
            int i12 = bArr[i4 + 6] & 255;
            int i13 = bArr[i4 + 5] & 255;
            int i14 = bArr[i4 + 4] & 255;
            byte b5 = bArr[i4 + 3];
            int i15 = bArr[i4 + 2] & 255;
            int i16 = bArr[i4 + 1] & 255;
            jArr[i2 + i11] = (((((b4 << 24) | (i12 << 16)) | (i13 << 8)) | i14) << 32) | (((b5 << 24) | (i15 << 16) | (i16 << 8) | (bArr[i4] & 255)) & 4294967295L);
            i4 += 8;
        }
    }

    private void toFloats(byte[] bArr, float[] fArr, int i2, int i3) {
        int i4 = 0;
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int i5 = 0; i5 < i3; i5++) {
                fArr[i2 + i5] = Float.intBitsToFloat((bArr[i4] << 24) | ((bArr[i4 + 1] & 255) << 16) | ((bArr[i4 + 2] & 255) << 8) | (bArr[i4 + 3] & 255));
                i4 += 4;
            }
            return;
        }
        for (int i6 = 0; i6 < i3; i6++) {
            fArr[i2 + i6] = Float.intBitsToFloat((bArr[i4 + 3] << 24) | ((bArr[i4 + 2] & 255) << 16) | ((bArr[i4 + 1] & 255) << 8) | (bArr[i4 + 0] & 255));
            i4 += 4;
        }
    }

    private void toDoubles(byte[] bArr, double[] dArr, int i2, int i3) {
        int i4 = 0;
        if (this.byteOrder == ByteOrder.BIG_ENDIAN) {
            for (int i5 = 0; i5 < i3; i5++) {
                byte b2 = bArr[i4];
                int i6 = bArr[i4 + 1] & 255;
                int i7 = bArr[i4 + 2] & 255;
                int i8 = bArr[i4 + 3] & 255;
                byte b3 = bArr[i4 + 4];
                int i9 = bArr[i4 + 5] & 255;
                int i10 = bArr[i4 + 6] & 255;
                dArr[i2 + i5] = Double.longBitsToDouble((((((b2 << 24) | (i6 << 16)) | (i7 << 8)) | i8) << 32) | (((b3 << 24) | (i9 << 16) | (i10 << 8) | (bArr[i4 + 7] & 255)) & 4294967295L));
                i4 += 8;
            }
            return;
        }
        for (int i11 = 0; i11 < i3; i11++) {
            byte b4 = bArr[i4 + 7];
            int i12 = bArr[i4 + 6] & 255;
            int i13 = bArr[i4 + 5] & 255;
            int i14 = bArr[i4 + 4] & 255;
            byte b5 = bArr[i4 + 3];
            int i15 = bArr[i4 + 2] & 255;
            int i16 = bArr[i4 + 1] & 255;
            dArr[i2 + i11] = Double.longBitsToDouble((((((b4 << 24) | (i12 << 16)) | (i13 << 8)) | i14) << 32) | (((b5 << 24) | (i15 << 16) | (i16 << 8) | (bArr[i4] & 255)) & 4294967295L));
            i4 += 8;
        }
    }

    @Override // javax.imageio.stream.ImageInputStream
    public long getStreamPosition() throws IOException {
        checkClosed();
        return this.streamPos;
    }

    @Override // javax.imageio.stream.ImageInputStream
    public int getBitOffset() throws IOException {
        checkClosed();
        return this.bitOffset;
    }

    @Override // javax.imageio.stream.ImageInputStream
    public void setBitOffset(int i2) throws IOException {
        checkClosed();
        if (i2 < 0 || i2 > 7) {
            throw new IllegalArgumentException("bitOffset must be betwwen 0 and 7!");
        }
        this.bitOffset = i2;
    }

    @Override // javax.imageio.stream.ImageInputStream
    public int readBit() throws IOException {
        checkClosed();
        int i2 = (this.bitOffset + 1) & 7;
        int i3 = read();
        if (i3 == -1) {
            throw new EOFException();
        }
        if (i2 != 0) {
            seek(getStreamPosition() - 1);
            i3 >>= 8 - i2;
        }
        this.bitOffset = i2;
        return i3 & 1;
    }

    @Override // javax.imageio.stream.ImageInputStream
    public long readBits(int i2) throws IOException {
        checkClosed();
        if (i2 < 0 || i2 > 64) {
            throw new IllegalArgumentException();
        }
        if (i2 == 0) {
            return 0L;
        }
        int i3 = i2 + this.bitOffset;
        int i4 = (this.bitOffset + i2) & 7;
        long j2 = 0;
        while (i3 > 0) {
            int i5 = read();
            if (i5 == -1) {
                throw new EOFException();
            }
            j2 = (j2 << 8) | i5;
            i3 -= 8;
        }
        if (i4 != 0) {
            seek(getStreamPosition() - 1);
        }
        this.bitOffset = i4;
        return (j2 >>> (-i3)) & ((-1) >>> (64 - i2));
    }

    @Override // javax.imageio.stream.ImageInputStream
    public long length() {
        return -1L;
    }

    @Override // javax.imageio.stream.ImageInputStream, java.io.DataInput
    public int skipBytes(int i2) throws IOException {
        long streamPosition = getStreamPosition();
        seek(streamPosition + i2);
        return (int) (getStreamPosition() - streamPosition);
    }

    @Override // javax.imageio.stream.ImageInputStream
    public long skipBytes(long j2) throws IOException {
        long streamPosition = getStreamPosition();
        seek(streamPosition + j2);
        return getStreamPosition() - streamPosition;
    }

    @Override // javax.imageio.stream.ImageInputStream
    public void seek(long j2) throws IOException {
        checkClosed();
        if (j2 < this.flushedPos) {
            throw new IndexOutOfBoundsException("pos < flushedPos!");
        }
        this.streamPos = j2;
        this.bitOffset = 0;
    }

    @Override // javax.imageio.stream.ImageInputStream
    public void mark() {
        try {
            this.markByteStack.push(Long.valueOf(getStreamPosition()));
            this.markBitStack.push(Integer.valueOf(getBitOffset()));
        } catch (IOException e2) {
        }
    }

    @Override // javax.imageio.stream.ImageInputStream
    public void reset() throws IOException {
        if (this.markByteStack.empty()) {
            return;
        }
        long jLongValue = ((Long) this.markByteStack.pop()).longValue();
        if (jLongValue < this.flushedPos) {
            throw new IIOException("Previous marked position has been discarded!");
        }
        seek(jLongValue);
        setBitOffset(((Integer) this.markBitStack.pop()).intValue());
    }

    @Override // javax.imageio.stream.ImageInputStream, javax.imageio.stream.ImageOutputStream
    public void flushBefore(long j2) throws IOException {
        checkClosed();
        if (j2 < this.flushedPos) {
            throw new IndexOutOfBoundsException("pos < flushedPos!");
        }
        if (j2 > getStreamPosition()) {
            throw new IndexOutOfBoundsException("pos > getStreamPosition()!");
        }
        this.flushedPos = j2;
    }

    @Override // javax.imageio.stream.ImageInputStream
    public void flush() throws IOException {
        flushBefore(getStreamPosition());
    }

    @Override // javax.imageio.stream.ImageInputStream
    public long getFlushedPosition() {
        return this.flushedPos;
    }

    @Override // javax.imageio.stream.ImageInputStream
    public boolean isCached() {
        return false;
    }

    @Override // javax.imageio.stream.ImageInputStream
    public boolean isCachedMemory() {
        return false;
    }

    @Override // javax.imageio.stream.ImageInputStream
    public boolean isCachedFile() {
        return false;
    }

    @Override // javax.imageio.stream.ImageInputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        checkClosed();
        this.isClosed = true;
    }

    protected void finalize() throws Throwable {
        if (!this.isClosed) {
            try {
                close();
            } catch (IOException e2) {
            }
        }
        super.finalize();
    }
}
