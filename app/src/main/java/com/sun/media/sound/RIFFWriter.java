package com.sun.media.sound;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:com/sun/media/sound/RIFFWriter.class */
public final class RIFFWriter extends OutputStream {
    private int chunktype;
    private RandomAccessWriter raf;
    private final long chunksizepointer;
    private final long startpointer;
    private RIFFWriter childchunk;
    private boolean open;
    private boolean writeoverride;

    /* loaded from: rt.jar:com/sun/media/sound/RIFFWriter$RandomAccessWriter.class */
    private interface RandomAccessWriter {
        void seek(long j2) throws IOException;

        long getPointer() throws IOException;

        void close() throws IOException;

        void write(int i2) throws IOException;

        void write(byte[] bArr, int i2, int i3) throws IOException;

        void write(byte[] bArr) throws IOException;

        long length() throws IOException;

        void setLength(long j2) throws IOException;
    }

    /* loaded from: rt.jar:com/sun/media/sound/RIFFWriter$RandomAccessFileWriter.class */
    private static class RandomAccessFileWriter implements RandomAccessWriter {
        RandomAccessFile raf;

        RandomAccessFileWriter(File file) throws FileNotFoundException {
            this.raf = new RandomAccessFile(file, InternalZipConstants.WRITE_MODE);
        }

        RandomAccessFileWriter(String str) throws FileNotFoundException {
            this.raf = new RandomAccessFile(str, InternalZipConstants.WRITE_MODE);
        }

        @Override // com.sun.media.sound.RIFFWriter.RandomAccessWriter
        public void seek(long j2) throws IOException {
            this.raf.seek(j2);
        }

        @Override // com.sun.media.sound.RIFFWriter.RandomAccessWriter
        public long getPointer() throws IOException {
            return this.raf.getFilePointer();
        }

        @Override // com.sun.media.sound.RIFFWriter.RandomAccessWriter
        public void close() throws IOException {
            this.raf.close();
        }

        @Override // com.sun.media.sound.RIFFWriter.RandomAccessWriter
        public void write(int i2) throws IOException {
            this.raf.write(i2);
        }

        @Override // com.sun.media.sound.RIFFWriter.RandomAccessWriter
        public void write(byte[] bArr, int i2, int i3) throws IOException {
            this.raf.write(bArr, i2, i3);
        }

        @Override // com.sun.media.sound.RIFFWriter.RandomAccessWriter
        public void write(byte[] bArr) throws IOException {
            this.raf.write(bArr);
        }

        @Override // com.sun.media.sound.RIFFWriter.RandomAccessWriter
        public long length() throws IOException {
            return this.raf.length();
        }

        @Override // com.sun.media.sound.RIFFWriter.RandomAccessWriter
        public void setLength(long j2) throws IOException {
            this.raf.setLength(j2);
        }
    }

    /* loaded from: rt.jar:com/sun/media/sound/RIFFWriter$RandomAccessByteWriter.class */
    private static class RandomAccessByteWriter implements RandomAccessWriter {
        byte[] buff = new byte[32];
        int length = 0;
        int pos = 0;

        /* renamed from: s, reason: collision with root package name */
        byte[] f11979s;
        final OutputStream stream;

        RandomAccessByteWriter(OutputStream outputStream) {
            this.stream = outputStream;
        }

        @Override // com.sun.media.sound.RIFFWriter.RandomAccessWriter
        public void seek(long j2) throws IOException {
            this.pos = (int) j2;
        }

        @Override // com.sun.media.sound.RIFFWriter.RandomAccessWriter
        public long getPointer() throws IOException {
            return this.pos;
        }

        @Override // com.sun.media.sound.RIFFWriter.RandomAccessWriter
        public void close() throws IOException {
            this.stream.write(this.buff, 0, this.length);
            this.stream.close();
        }

        @Override // com.sun.media.sound.RIFFWriter.RandomAccessWriter
        public void write(int i2) throws IOException {
            if (this.f11979s == null) {
                this.f11979s = new byte[1];
            }
            this.f11979s[0] = (byte) i2;
            write(this.f11979s, 0, 1);
        }

        @Override // com.sun.media.sound.RIFFWriter.RandomAccessWriter
        public void write(byte[] bArr, int i2, int i3) throws IOException {
            int i4 = this.pos + i3;
            if (i4 > this.length) {
                setLength(i4);
            }
            int i5 = i2 + i3;
            for (int i6 = i2; i6 < i5; i6++) {
                byte[] bArr2 = this.buff;
                int i7 = this.pos;
                this.pos = i7 + 1;
                bArr2[i7] = bArr[i6];
            }
        }

        @Override // com.sun.media.sound.RIFFWriter.RandomAccessWriter
        public void write(byte[] bArr) throws IOException {
            write(bArr, 0, bArr.length);
        }

        @Override // com.sun.media.sound.RIFFWriter.RandomAccessWriter
        public long length() throws IOException {
            return this.length;
        }

        @Override // com.sun.media.sound.RIFFWriter.RandomAccessWriter
        public void setLength(long j2) throws IOException {
            this.length = (int) j2;
            if (this.length > this.buff.length) {
                byte[] bArr = new byte[Math.max(this.buff.length << 1, this.length)];
                System.arraycopy(this.buff, 0, bArr, 0, this.buff.length);
                this.buff = bArr;
            }
        }
    }

    public RIFFWriter(String str, String str2) throws IOException {
        this(new RandomAccessFileWriter(str), str2, 0);
    }

    public RIFFWriter(File file, String str) throws IOException {
        this(new RandomAccessFileWriter(file), str, 0);
    }

    public RIFFWriter(OutputStream outputStream, String str) throws IOException {
        this(new RandomAccessByteWriter(outputStream), str, 0);
    }

    private RIFFWriter(RandomAccessWriter randomAccessWriter, String str, int i2) throws IOException {
        this.chunktype = 0;
        this.childchunk = null;
        this.open = true;
        this.writeoverride = false;
        if (i2 == 0 && randomAccessWriter.length() != 0) {
            randomAccessWriter.setLength(0L);
        }
        this.raf = randomAccessWriter;
        if (randomAccessWriter.getPointer() % 2 != 0) {
            randomAccessWriter.write(0);
        }
        if (i2 == 0) {
            randomAccessWriter.write("RIFF".getBytes("ascii"));
        } else if (i2 == 1) {
            randomAccessWriter.write("LIST".getBytes("ascii"));
        } else {
            randomAccessWriter.write((str + "    ").substring(0, 4).getBytes("ascii"));
        }
        this.chunksizepointer = randomAccessWriter.getPointer();
        this.chunktype = 2;
        writeUnsignedInt(0L);
        this.chunktype = i2;
        this.startpointer = randomAccessWriter.getPointer();
        if (i2 != 2) {
            randomAccessWriter.write((str + "    ").substring(0, 4).getBytes("ascii"));
        }
    }

    public void seek(long j2) throws IOException {
        this.raf.seek(j2);
    }

    public long getFilePointer() throws IOException {
        return this.raf.getPointer();
    }

    public void setWriteOverride(boolean z2) {
        this.writeoverride = z2;
    }

    public boolean getWriteOverride() {
        return this.writeoverride;
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.open) {
            return;
        }
        if (this.childchunk != null) {
            this.childchunk.close();
            this.childchunk = null;
        }
        int i2 = this.chunktype;
        long pointer = this.raf.getPointer();
        this.raf.seek(this.chunksizepointer);
        this.chunktype = 2;
        writeUnsignedInt(pointer - this.startpointer);
        if (i2 == 0) {
            this.raf.close();
        } else {
            this.raf.seek(pointer);
        }
        this.open = false;
        this.raf = null;
    }

    @Override // java.io.OutputStream
    public void write(int i2) throws IOException {
        if (!this.writeoverride) {
            if (this.chunktype != 2) {
                throw new IllegalArgumentException("Only chunks can write bytes!");
            }
            if (this.childchunk != null) {
                this.childchunk.close();
                this.childchunk = null;
            }
        }
        this.raf.write(i2);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        if (!this.writeoverride) {
            if (this.chunktype != 2) {
                throw new IllegalArgumentException("Only chunks can write bytes!");
            }
            if (this.childchunk != null) {
                this.childchunk.close();
                this.childchunk = null;
            }
        }
        this.raf.write(bArr, i2, i3);
    }

    public RIFFWriter writeList(String str) throws IOException {
        if (this.chunktype == 2) {
            throw new IllegalArgumentException("Only LIST and RIFF can write lists!");
        }
        if (this.childchunk != null) {
            this.childchunk.close();
            this.childchunk = null;
        }
        this.childchunk = new RIFFWriter(this.raf, str, 1);
        return this.childchunk;
    }

    public RIFFWriter writeChunk(String str) throws IOException {
        if (this.chunktype == 2) {
            throw new IllegalArgumentException("Only LIST and RIFF can write chunks!");
        }
        if (this.childchunk != null) {
            this.childchunk.close();
            this.childchunk = null;
        }
        this.childchunk = new RIFFWriter(this.raf, str, 2);
        return this.childchunk;
    }

    public void writeString(String str) throws IOException {
        write(str.getBytes());
    }

    public void writeString(String str, int i2) throws IOException {
        byte[] bytes = str.getBytes();
        if (bytes.length > i2) {
            write(bytes, 0, i2);
            return;
        }
        write(bytes);
        for (int length = bytes.length; length < i2; length++) {
            write(0);
        }
    }

    public void writeByte(int i2) throws IOException {
        write(i2);
    }

    public void writeShort(short s2) throws IOException {
        write((s2 >>> 0) & 255);
        write((s2 >>> 8) & 255);
    }

    public void writeInt(int i2) throws IOException {
        write((i2 >>> 0) & 255);
        write((i2 >>> 8) & 255);
        write((i2 >>> 16) & 255);
        write((i2 >>> 24) & 255);
    }

    public void writeLong(long j2) throws IOException {
        write(((int) (j2 >>> 0)) & 255);
        write(((int) (j2 >>> 8)) & 255);
        write(((int) (j2 >>> 16)) & 255);
        write(((int) (j2 >>> 24)) & 255);
        write(((int) (j2 >>> 32)) & 255);
        write(((int) (j2 >>> 40)) & 255);
        write(((int) (j2 >>> 48)) & 255);
        write(((int) (j2 >>> 56)) & 255);
    }

    public void writeUnsignedByte(int i2) throws IOException {
        writeByte((byte) i2);
    }

    public void writeUnsignedShort(int i2) throws IOException {
        writeShort((short) i2);
    }

    public void writeUnsignedInt(long j2) throws IOException {
        writeInt((int) j2);
    }
}
