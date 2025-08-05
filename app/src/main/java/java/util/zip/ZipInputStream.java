package java.util.zip;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/* loaded from: rt.jar:java/util/zip/ZipInputStream.class */
public class ZipInputStream extends InflaterInputStream implements ZipConstants {
    private ZipEntry entry;
    private int flag;
    private CRC32 crc;
    private long remaining;
    private byte[] tmpbuf;
    private static final int STORED = 0;
    private static final int DEFLATED = 8;
    private boolean closed;
    private boolean entryEOF;
    private ZipCoder zc;

    /* renamed from: b, reason: collision with root package name */
    private byte[] f12624b;

    private void ensureOpen() throws IOException {
        if (this.closed) {
            throw new IOException("Stream closed");
        }
    }

    public ZipInputStream(InputStream inputStream) {
        this(inputStream, StandardCharsets.UTF_8);
    }

    public ZipInputStream(InputStream inputStream, Charset charset) {
        super(new PushbackInputStream(inputStream, 512), new Inflater(true), 512);
        this.crc = new CRC32();
        this.tmpbuf = new byte[512];
        this.closed = false;
        this.entryEOF = false;
        this.f12624b = new byte[256];
        this.usesDefaultInflater = true;
        if (inputStream == null) {
            throw new NullPointerException("in is null");
        }
        if (charset == null) {
            throw new NullPointerException("charset is null");
        }
        this.zc = ZipCoder.get(charset);
    }

    public ZipEntry getNextEntry() throws IOException {
        ensureOpen();
        if (this.entry != null) {
            closeEntry();
        }
        this.crc.reset();
        this.inf.reset();
        ZipEntry loc = readLOC();
        this.entry = loc;
        if (loc == null) {
            return null;
        }
        if (this.entry.method == 0) {
            this.remaining = this.entry.size;
        }
        this.entryEOF = false;
        return this.entry;
    }

    public void closeEntry() throws IOException {
        ensureOpen();
        while (read(this.tmpbuf, 0, this.tmpbuf.length) != -1) {
        }
        this.entryEOF = true;
    }

    @Override // java.util.zip.InflaterInputStream, java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        ensureOpen();
        if (this.entryEOF) {
            return 0;
        }
        return 1;
    }

    @Override // java.util.zip.InflaterInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        ensureOpen();
        if (i2 < 0 || i3 < 0 || i2 > bArr.length - i3) {
            throw new IndexOutOfBoundsException();
        }
        if (i3 == 0) {
            return 0;
        }
        if (this.entry == null) {
            return -1;
        }
        switch (this.entry.method) {
            case 0:
                if (this.remaining <= 0) {
                    this.entryEOF = true;
                    this.entry = null;
                    return -1;
                }
                if (i3 > this.remaining) {
                    i3 = (int) this.remaining;
                }
                int i4 = this.in.read(bArr, i2, i3);
                if (i4 == -1) {
                    throw new ZipException("unexpected EOF");
                }
                this.crc.update(bArr, i2, i4);
                this.remaining -= i4;
                if (this.remaining == 0 && this.entry.crc != this.crc.getValue()) {
                    throw new ZipException("invalid entry CRC (expected 0x" + Long.toHexString(this.entry.crc) + " but got 0x" + Long.toHexString(this.crc.getValue()) + ")");
                }
                return i4;
            case 8:
                int i5 = super.read(bArr, i2, i3);
                if (i5 == -1) {
                    readEnd(this.entry);
                    this.entryEOF = true;
                    this.entry = null;
                } else {
                    this.crc.update(bArr, i2, i5);
                }
                return i5;
            default:
                throw new ZipException("invalid compression method");
        }
    }

    @Override // java.util.zip.InflaterInputStream, java.io.FilterInputStream, java.io.InputStream
    public long skip(long j2) throws IOException {
        int i2;
        if (j2 < 0) {
            throw new IllegalArgumentException("negative skip length");
        }
        ensureOpen();
        int iMin = (int) Math.min(j2, 2147483647L);
        int i3 = 0;
        while (true) {
            i2 = i3;
            if (i2 >= iMin) {
                break;
            }
            int length = iMin - i2;
            if (length > this.tmpbuf.length) {
                length = this.tmpbuf.length;
            }
            int i4 = read(this.tmpbuf, 0, length);
            if (i4 == -1) {
                this.entryEOF = true;
                break;
            }
            i3 = i2 + i4;
        }
        return i2;
    }

    @Override // java.util.zip.InflaterInputStream, java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            super.close();
            this.closed = true;
        }
    }

    private ZipEntry readLOC() throws IOException {
        String string;
        try {
            readFully(this.tmpbuf, 0, 30);
            if (ZipUtils.get32(this.tmpbuf, 0) != 67324752) {
                return null;
            }
            this.flag = ZipUtils.get16(this.tmpbuf, 6);
            int i2 = ZipUtils.get16(this.tmpbuf, 26);
            int length = this.f12624b.length;
            if (i2 > length) {
                do {
                    length *= 2;
                } while (i2 > length);
                this.f12624b = new byte[length];
            }
            readFully(this.f12624b, 0, i2);
            if ((this.flag & 2048) != 0) {
                string = this.zc.toStringUTF8(this.f12624b, i2);
            } else {
                string = this.zc.toString(this.f12624b, i2);
            }
            ZipEntry zipEntryCreateZipEntry = createZipEntry(string);
            if ((this.flag & 1) == 1) {
                throw new ZipException("encrypted ZIP entry not supported");
            }
            zipEntryCreateZipEntry.method = ZipUtils.get16(this.tmpbuf, 8);
            zipEntryCreateZipEntry.xdostime = ZipUtils.get32(this.tmpbuf, 10);
            if ((this.flag & 8) == 8) {
                if (zipEntryCreateZipEntry.method != 8) {
                    throw new ZipException("only DEFLATED entries can have EXT descriptor");
                }
            } else {
                zipEntryCreateZipEntry.crc = ZipUtils.get32(this.tmpbuf, 14);
                zipEntryCreateZipEntry.csize = ZipUtils.get32(this.tmpbuf, 18);
                zipEntryCreateZipEntry.size = ZipUtils.get32(this.tmpbuf, 22);
            }
            int i3 = ZipUtils.get16(this.tmpbuf, 28);
            if (i3 > 0) {
                byte[] bArr = new byte[i3];
                readFully(bArr, 0, i3);
                zipEntryCreateZipEntry.setExtra0(bArr, zipEntryCreateZipEntry.csize == 4294967295L || zipEntryCreateZipEntry.size == 4294967295L);
            }
            return zipEntryCreateZipEntry;
        } catch (EOFException e2) {
            return null;
        }
    }

    protected ZipEntry createZipEntry(String str) {
        return new ZipEntry(str);
    }

    private void readEnd(ZipEntry zipEntry) throws IOException {
        int remaining = this.inf.getRemaining();
        if (remaining > 0) {
            ((PushbackInputStream) this.in).unread(this.buf, this.len - remaining, remaining);
        }
        if ((this.flag & 8) == 8) {
            if (this.inf.getBytesWritten() > 4294967295L || this.inf.getBytesRead() > 4294967295L) {
                readFully(this.tmpbuf, 0, 24);
                long j2 = ZipUtils.get32(this.tmpbuf, 0);
                if (j2 != 134695760) {
                    zipEntry.crc = j2;
                    zipEntry.csize = ZipUtils.get64(this.tmpbuf, 4);
                    zipEntry.size = ZipUtils.get64(this.tmpbuf, 12);
                    ((PushbackInputStream) this.in).unread(this.tmpbuf, 19, 4);
                } else {
                    zipEntry.crc = ZipUtils.get32(this.tmpbuf, 4);
                    zipEntry.csize = ZipUtils.get64(this.tmpbuf, 8);
                    zipEntry.size = ZipUtils.get64(this.tmpbuf, 16);
                }
            } else {
                readFully(this.tmpbuf, 0, 16);
                long j3 = ZipUtils.get32(this.tmpbuf, 0);
                if (j3 != 134695760) {
                    zipEntry.crc = j3;
                    zipEntry.csize = ZipUtils.get32(this.tmpbuf, 4);
                    zipEntry.size = ZipUtils.get32(this.tmpbuf, 8);
                    ((PushbackInputStream) this.in).unread(this.tmpbuf, 11, 4);
                } else {
                    zipEntry.crc = ZipUtils.get32(this.tmpbuf, 4);
                    zipEntry.csize = ZipUtils.get32(this.tmpbuf, 8);
                    zipEntry.size = ZipUtils.get32(this.tmpbuf, 12);
                }
            }
        }
        if (zipEntry.size != this.inf.getBytesWritten()) {
            throw new ZipException("invalid entry size (expected " + zipEntry.size + " but got " + this.inf.getBytesWritten() + " bytes)");
        }
        if (zipEntry.csize != this.inf.getBytesRead()) {
            throw new ZipException("invalid entry compressed size (expected " + zipEntry.csize + " but got " + this.inf.getBytesRead() + " bytes)");
        }
        if (zipEntry.crc != this.crc.getValue()) {
            throw new ZipException("invalid entry CRC (expected 0x" + Long.toHexString(zipEntry.crc) + " but got 0x" + Long.toHexString(this.crc.getValue()) + ")");
        }
    }

    private void readFully(byte[] bArr, int i2, int i3) throws IOException {
        while (i3 > 0) {
            int i4 = this.in.read(bArr, i2, i3);
            if (i4 == -1) {
                throw new EOFException();
            }
            i2 += i4;
            i3 -= i4;
        }
    }
}
