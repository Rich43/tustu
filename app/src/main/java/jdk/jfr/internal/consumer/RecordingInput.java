package jdk.jfr.internal.consumer;

import java.io.DataInput;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import net.lingala.zip4j.util.InternalZipConstants;
import org.apache.commons.net.ftp.FTP;

/* loaded from: jfr.jar:jdk/jfr/internal/consumer/RecordingInput.class */
public final class RecordingInput implements DataInput, AutoCloseable {
    public static final byte STRING_ENCODING_NULL = 0;
    public static final byte STRING_ENCODING_EMPTY_STRING = 1;
    public static final byte STRING_ENCODING_CONSTANT_POOL = 2;
    public static final byte STRING_ENCODING_UTF8_BYTE_ARRAY = 3;
    public static final byte STRING_ENCODING_CHAR_ARRAY = 4;
    public static final byte STRING_ENCODING_LATIN1_BYTE_ARRAY = 5;
    private static final int DEFAULT_BLOCK_SIZE = 16777216;
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final Charset LATIN1 = Charset.forName(FTP.DEFAULT_CONTROL_ENCODING);
    private final RandomAccessFile file;
    private final long size;
    private Block currentBlock;
    private Block previousBlock;
    private long position;
    private final int blockSize;

    /* loaded from: jfr.jar:jdk/jfr/internal/consumer/RecordingInput$Block.class */
    private static final class Block {
        private byte[] bytes;
        private long blockPosition;

        private Block() {
            this.bytes = new byte[0];
        }

        boolean contains(long j2) {
            return j2 >= this.blockPosition && j2 < this.blockPosition + ((long) this.bytes.length);
        }

        public void read(RandomAccessFile randomAccessFile, int i2) throws IOException {
            this.blockPosition = randomAccessFile.getFilePointer();
            if (i2 != this.bytes.length) {
                this.bytes = new byte[i2];
            }
            randomAccessFile.readFully(this.bytes);
        }

        public byte get(long j2) {
            return this.bytes[(int) (j2 - this.blockPosition)];
        }
    }

    private RecordingInput(File file, int i2) throws IOException {
        this.currentBlock = new Block();
        this.previousBlock = new Block();
        this.size = file.length();
        this.blockSize = i2;
        this.file = new RandomAccessFile(file, InternalZipConstants.READ_MODE);
        if (this.size < 8) {
            throw new IOException("Not a valid Flight Recorder file. File length is only " + this.size + " bytes.");
        }
    }

    public RecordingInput(File file) throws IOException {
        this(file, 16777216);
    }

    @Override // java.io.DataInput
    public final byte readByte() throws IOException {
        if (!this.currentBlock.contains(this.position)) {
            position(this.position);
        }
        Block block = this.currentBlock;
        long j2 = this.position;
        this.position = j2 + 1;
        return block.get(j2);
    }

    @Override // java.io.DataInput
    public final void readFully(byte[] bArr, int i2, int i3) throws IOException {
        for (int i4 = 0; i4 < i3; i4++) {
            bArr[i4 + i2] = readByte();
        }
    }

    @Override // java.io.DataInput
    public final void readFully(byte[] bArr) throws IOException {
        readFully(bArr, 0, bArr.length);
    }

    public final short readRawShort() throws IOException {
        return (short) ((readByte() & 255) + (readByte() << 8));
    }

    @Override // java.io.DataInput
    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(readRawLong());
    }

    @Override // java.io.DataInput
    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readRawInt());
    }

    public final int readRawInt() throws IOException {
        byte b2 = readByte();
        byte b3 = readByte();
        return (readByte() & 255) + ((readByte() & 255) << 8) + ((b3 & 255) << 16) + (b2 << 24);
    }

    public final long readRawLong() throws IOException {
        byte b2 = readByte();
        byte b3 = readByte();
        byte b4 = readByte();
        byte b5 = readByte();
        byte b6 = readByte();
        return (readByte() & 255) + ((readByte() & 255) << 8) + ((readByte() & 255) << 16) + ((b6 & 255) << 24) + ((b5 & 255) << 32) + ((b4 & 255) << 40) + ((b3 & 255) << 48) + (b2 << 56);
    }

    public final long position() throws IOException {
        return this.position;
    }

    public final void position(long j2) throws IOException {
        if (!this.currentBlock.contains(j2)) {
            if (!this.previousBlock.contains(j2)) {
                if (j2 > size()) {
                    throw new EOFException("Trying to read at " + j2 + ", but file is only " + size() + " bytes.");
                }
                long jTrimToFileSize = trimToFileSize(calculateBlockStart(j2));
                this.file.seek(jTrimToFileSize);
                this.previousBlock.read(this.file, (int) Math.min(size() - jTrimToFileSize, this.blockSize));
            }
            Block block = this.currentBlock;
            this.currentBlock = this.previousBlock;
            this.previousBlock = block;
        }
        this.position = j2;
    }

    private final long trimToFileSize(long j2) throws IOException {
        return Math.min(size(), Math.max(0L, j2));
    }

    private final long calculateBlockStart(long j2) {
        if (this.currentBlock.contains(j2 - this.blockSize)) {
            return this.currentBlock.blockPosition + this.currentBlock.bytes.length;
        }
        if (this.currentBlock.contains(j2 + this.blockSize)) {
            return this.currentBlock.blockPosition - this.blockSize;
        }
        return j2 - (this.blockSize / 2);
    }

    public final long size() throws IOException {
        return this.size;
    }

    @Override // java.lang.AutoCloseable
    public final void close() throws IOException {
        this.file.close();
    }

    @Override // java.io.DataInput
    public final int skipBytes(int i2) throws IOException {
        long jPosition = position();
        position(jPosition + i2);
        return (int) (position() - jPosition);
    }

    @Override // java.io.DataInput
    public final boolean readBoolean() throws IOException {
        return readByte() != 0;
    }

    @Override // java.io.DataInput
    public int readUnsignedByte() throws IOException {
        return readByte() & 255;
    }

    @Override // java.io.DataInput
    public int readUnsignedShort() throws IOException {
        return readShort() & 65535;
    }

    @Override // java.io.DataInput
    public final String readLine() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // java.io.DataInput
    public String readUTF() throws IOException {
        return readEncodedString(readByte());
    }

    public String readEncodedString(byte b2) throws IOException {
        if (b2 == 0) {
            return null;
        }
        if (b2 == 1) {
            return "";
        }
        int i2 = readInt();
        require(i2, "String size %d exceeds available data");
        if (b2 == 4) {
            char[] cArr = new char[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                cArr[i3] = readChar();
            }
            return new String(cArr);
        }
        byte[] bArr = new byte[i2];
        readFully(bArr);
        if (b2 == 3) {
            return new String(bArr, UTF8);
        }
        if (b2 == 5) {
            return new String(bArr, LATIN1);
        }
        throw new IOException("Unknown string encoding " + ((int) b2));
    }

    @Override // java.io.DataInput
    public char readChar() throws IOException {
        return (char) readLong();
    }

    @Override // java.io.DataInput
    public short readShort() throws IOException {
        return (short) readLong();
    }

    @Override // java.io.DataInput
    public int readInt() throws IOException {
        return (int) readLong();
    }

    @Override // java.io.DataInput
    public long readLong() throws IOException {
        byte b2 = readByte();
        long j2 = b2 & 127;
        if (b2 >= 0) {
            return j2;
        }
        byte b3 = readByte();
        long j3 = j2 + ((b3 & 127) << 7);
        if (b3 >= 0) {
            return j3;
        }
        byte b4 = readByte();
        long j4 = j3 + ((b4 & 127) << 14);
        if (b4 >= 0) {
            return j4;
        }
        byte b5 = readByte();
        long j5 = j4 + ((b5 & 127) << 21);
        if (b5 >= 0) {
            return j5;
        }
        byte b6 = readByte();
        long j6 = j5 + ((b6 & 127) << 28);
        if (b6 >= 0) {
            return j6;
        }
        byte b7 = readByte();
        long j7 = j6 + ((b7 & 127) << 35);
        if (b7 >= 0) {
            return j7;
        }
        byte b8 = readByte();
        long j8 = j7 + ((b8 & 127) << 42);
        if (b8 >= 0) {
            return j8;
        }
        byte b9 = readByte();
        long j9 = j8 + ((b9 & 127) << 49);
        if (b9 >= 0) {
            return j9;
        }
        return j9 + ((readByte() & 255) << 56);
    }

    public void require(int i2, String str) throws IOException {
        if (this.position + i2 > this.size) {
            throw new IOException(String.format(str, Integer.valueOf(i2)));
        }
    }
}
