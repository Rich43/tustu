package com.sun.media.sound;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:com/sun/media/sound/RIFFReader.class */
public final class RIFFReader extends InputStream {
    private final RIFFReader root;
    private final String fourcc;
    private String riff_type;
    private long ckSize;
    private InputStream stream;
    private long avail;
    private long filepointer = 0;
    private RIFFReader lastiterator = null;

    public RIFFReader(InputStream inputStream) throws IOException {
        int i2;
        this.riff_type = null;
        this.ckSize = 2147483647L;
        this.avail = 2147483647L;
        if (inputStream instanceof RIFFReader) {
            this.root = ((RIFFReader) inputStream).root;
        } else {
            this.root = this;
        }
        this.stream = inputStream;
        do {
            i2 = read();
            if (i2 == -1) {
                this.fourcc = "";
                this.riff_type = null;
                this.avail = 0L;
                return;
            }
        } while (i2 == 0);
        byte[] bArr = new byte[4];
        bArr[0] = (byte) i2;
        readFully(bArr, 1, 3);
        this.fourcc = new String(bArr, "ascii");
        this.ckSize = readUnsignedInt();
        this.avail = this.ckSize;
        if (getFormat().equals("RIFF") || getFormat().equals("LIST")) {
            if (this.avail > 2147483647L) {
                throw new RIFFInvalidDataException("Chunk size too big");
            }
            byte[] bArr2 = new byte[4];
            readFully(bArr2);
            this.riff_type = new String(bArr2, "ascii");
        }
    }

    public long getFilePointer() throws IOException {
        return this.root.filepointer;
    }

    public boolean hasNextChunk() throws IOException {
        if (this.lastiterator != null) {
            this.lastiterator.finish();
        }
        return this.avail != 0;
    }

    public RIFFReader nextChunk() throws IOException {
        if (this.lastiterator != null) {
            this.lastiterator.finish();
        }
        if (this.avail == 0) {
            return null;
        }
        this.lastiterator = new RIFFReader(this);
        return this.lastiterator;
    }

    public String getFormat() {
        return this.fourcc;
    }

    public String getType() {
        return this.riff_type;
    }

    public long getSize() {
        return this.ckSize;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.avail == 0) {
            return -1;
        }
        int i2 = this.stream.read();
        if (i2 == -1) {
            this.avail = 0L;
            return -1;
        }
        this.avail--;
        this.filepointer++;
        return i2;
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        if (this.avail == 0) {
            return -1;
        }
        if (i3 > this.avail) {
            int i4 = this.stream.read(bArr, i2, (int) this.avail);
            if (i4 != -1) {
                this.filepointer += i4;
            }
            this.avail = 0L;
            return i4;
        }
        int i5 = this.stream.read(bArr, i2, i3);
        if (i5 == -1) {
            this.avail = 0L;
            return -1;
        }
        this.avail -= i5;
        this.filepointer += i5;
        return i5;
    }

    public final void readFully(byte[] bArr) throws IOException {
        readFully(bArr, 0, bArr.length);
    }

    public final void readFully(byte[] bArr, int i2, int i3) throws IOException {
        if (i3 < 0) {
            throw new IndexOutOfBoundsException();
        }
        while (i3 > 0) {
            int i4 = read(bArr, i2, i3);
            if (i4 < 0) {
                throw new EOFException();
            }
            if (i4 == 0) {
                Thread.yield();
            }
            i2 += i4;
            i3 -= i4;
        }
    }

    @Override // java.io.InputStream
    public long skip(long j2) throws IOException {
        if (j2 <= 0 || this.avail == 0) {
            return 0L;
        }
        long jMin = Math.min(j2, this.avail);
        while (true) {
            if (jMin <= 0) {
                break;
            }
            long jMin2 = Math.min(this.stream.skip(jMin), jMin);
            if (jMin2 == 0) {
                Thread.yield();
                if (this.stream.read() == -1) {
                    this.avail = 0L;
                    break;
                }
                jMin2 = 1;
            }
            jMin -= jMin2;
            this.avail -= jMin2;
            this.filepointer += jMin2;
        }
        return j2 - jMin;
    }

    @Override // java.io.InputStream
    public int available() {
        return (int) this.avail;
    }

    public void finish() throws IOException {
        if (this.avail != 0) {
            skip(this.avail);
        }
    }

    public String readString(int i2) throws IOException {
        try {
            byte[] bArr = new byte[i2];
            readFully(bArr);
            for (int i3 = 0; i3 < bArr.length; i3++) {
                if (bArr[i3] == 0) {
                    return new String(bArr, 0, i3, "ascii");
                }
            }
            return new String(bArr, "ascii");
        } catch (OutOfMemoryError e2) {
            throw new IOException("Length too big", e2);
        }
    }

    public byte readByte() throws IOException {
        int i2 = read();
        if (i2 < 0) {
            throw new EOFException();
        }
        return (byte) i2;
    }

    public short readShort() throws IOException {
        int i2 = read();
        int i3 = read();
        if (i2 < 0) {
            throw new EOFException();
        }
        if (i3 < 0) {
            throw new EOFException();
        }
        return (short) (i2 | (i3 << 8));
    }

    public int readInt() throws IOException {
        int i2 = read();
        int i3 = read();
        int i4 = read();
        int i5 = read();
        if (i2 < 0) {
            throw new EOFException();
        }
        if (i3 < 0) {
            throw new EOFException();
        }
        if (i4 < 0) {
            throw new EOFException();
        }
        if (i5 < 0) {
            throw new EOFException();
        }
        return (i2 + (i3 << 8)) | (i4 << 16) | (i5 << 24);
    }

    public long readLong() throws IOException {
        long j2 = read();
        long j3 = read();
        long j4 = read();
        long j5 = read();
        long j6 = read();
        long j7 = read();
        long j8 = read();
        long j9 = read();
        if (j2 < 0) {
            throw new EOFException();
        }
        if (j3 < 0) {
            throw new EOFException();
        }
        if (j4 < 0) {
            throw new EOFException();
        }
        if (j5 < 0) {
            throw new EOFException();
        }
        if (j6 < 0) {
            throw new EOFException();
        }
        if (j7 < 0) {
            throw new EOFException();
        }
        if (j8 < 0) {
            throw new EOFException();
        }
        if (j9 < 0) {
            throw new EOFException();
        }
        return j2 | (j3 << 8) | (j4 << 16) | (j5 << 24) | (j6 << 32) | (j7 << 40) | (j8 << 48) | (j9 << 56);
    }

    public int readUnsignedByte() throws IOException {
        int i2 = read();
        if (i2 < 0) {
            throw new EOFException();
        }
        return i2;
    }

    public int readUnsignedShort() throws IOException {
        int i2 = read();
        int i3 = read();
        if (i2 < 0) {
            throw new EOFException();
        }
        if (i3 < 0) {
            throw new EOFException();
        }
        return i2 | (i3 << 8);
    }

    public long readUnsignedInt() throws IOException {
        long j2 = read();
        long j3 = read();
        long j4 = read();
        long j5 = read();
        if (j2 < 0) {
            throw new EOFException();
        }
        if (j3 < 0) {
            throw new EOFException();
        }
        if (j4 < 0) {
            throw new EOFException();
        }
        if (j5 < 0) {
            throw new EOFException();
        }
        return (j2 + (j3 << 8)) | (j4 << 16) | (j5 << 24);
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        finish();
        if (this == this.root) {
            this.stream.close();
        }
        this.stream = null;
    }
}
