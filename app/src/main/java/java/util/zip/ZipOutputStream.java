package java.util.zip;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:java/util/zip/ZipOutputStream.class */
public class ZipOutputStream extends DeflaterOutputStream implements ZipConstants {
    private static final boolean inhibitZip64 = Boolean.parseBoolean((String) AccessController.doPrivileged(new GetPropertyAction("jdk.util.zip.inhibitZip64", "false")));
    private XEntry current;
    private Vector<XEntry> xentries;
    private HashSet<String> names;
    private CRC32 crc;
    private long written;
    private long locoff;
    private byte[] comment;
    private int method;
    private boolean finished;
    private boolean closed;
    private final ZipCoder zc;
    public static final int STORED = 0;
    public static final int DEFLATED = 8;

    /* loaded from: rt.jar:java/util/zip/ZipOutputStream$XEntry.class */
    private static class XEntry {
        final ZipEntry entry;
        final long offset;

        public XEntry(ZipEntry zipEntry, long j2) {
            this.entry = zipEntry;
            this.offset = j2;
        }
    }

    private static int version(ZipEntry zipEntry) throws ZipException {
        switch (zipEntry.method) {
            case 0:
                return 10;
            case 8:
                return 20;
            default:
                throw new ZipException("unsupported compression method");
        }
    }

    private void ensureOpen() throws IOException {
        if (this.closed) {
            throw new IOException("Stream closed");
        }
    }

    public ZipOutputStream(OutputStream outputStream) {
        this(outputStream, StandardCharsets.UTF_8);
    }

    public ZipOutputStream(OutputStream outputStream, Charset charset) {
        super(outputStream, new Deflater(-1, true));
        this.xentries = new Vector<>();
        this.names = new HashSet<>();
        this.crc = new CRC32();
        this.written = 0L;
        this.locoff = 0L;
        this.method = 8;
        this.closed = false;
        if (charset == null) {
            throw new NullPointerException("charset is null");
        }
        this.zc = ZipCoder.get(charset);
        this.usesDefaultDeflater = true;
    }

    public void setComment(String str) {
        if (str != null) {
            this.comment = this.zc.getBytes(str);
            if (this.comment.length > 65535) {
                throw new IllegalArgumentException("ZIP file comment too long.");
            }
        }
    }

    public void setMethod(int i2) {
        if (i2 != 8 && i2 != 0) {
            throw new IllegalArgumentException("invalid compression method");
        }
        this.method = i2;
    }

    public void setLevel(int i2) {
        this.def.setLevel(i2);
    }

    public void putNextEntry(ZipEntry zipEntry) throws IOException {
        ensureOpen();
        if (this.current != null) {
            closeEntry();
        }
        if (zipEntry.xdostime == -1) {
            zipEntry.setTime(System.currentTimeMillis());
        }
        if (zipEntry.method == -1) {
            zipEntry.method = this.method;
        }
        zipEntry.flag = 0;
        switch (zipEntry.method) {
            case 0:
                if (zipEntry.size == -1) {
                    zipEntry.size = zipEntry.csize;
                } else if (zipEntry.csize == -1) {
                    zipEntry.csize = zipEntry.size;
                } else if (zipEntry.size != zipEntry.csize) {
                    throw new ZipException("STORED entry where compressed != uncompressed size");
                }
                if (zipEntry.size == -1 || zipEntry.crc == -1) {
                    throw new ZipException("STORED entry missing size, compressed size, or crc-32");
                }
                break;
            case 8:
                if (zipEntry.size == -1 || zipEntry.csize == -1 || zipEntry.crc == -1) {
                    zipEntry.flag = 8;
                    break;
                }
                break;
            default:
                throw new ZipException("unsupported compression method");
        }
        if (!this.names.add(zipEntry.name)) {
            throw new ZipException("duplicate entry: " + zipEntry.name);
        }
        if (this.zc.isUTF8()) {
            zipEntry.flag |= 2048;
        }
        this.current = new XEntry(zipEntry, this.written);
        this.xentries.add(this.current);
        writeLOC(this.current);
    }

    public void closeEntry() throws IOException {
        ensureOpen();
        if (this.current != null) {
            ZipEntry zipEntry = this.current.entry;
            switch (zipEntry.method) {
                case 0:
                    if (zipEntry.size != this.written - this.locoff) {
                        throw new ZipException("invalid entry size (expected " + zipEntry.size + " but got " + (this.written - this.locoff) + " bytes)");
                    }
                    if (zipEntry.crc != this.crc.getValue()) {
                        throw new ZipException("invalid entry crc-32 (expected 0x" + Long.toHexString(zipEntry.crc) + " but got 0x" + Long.toHexString(this.crc.getValue()) + ")");
                    }
                    break;
                case 8:
                    this.def.finish();
                    while (!this.def.finished()) {
                        deflate();
                    }
                    if ((zipEntry.flag & 8) != 0) {
                        zipEntry.size = this.def.getBytesRead();
                        zipEntry.csize = this.def.getBytesWritten();
                        zipEntry.crc = this.crc.getValue();
                        writeEXT(zipEntry);
                    } else {
                        if (zipEntry.size != this.def.getBytesRead()) {
                            throw new ZipException("invalid entry size (expected " + zipEntry.size + " but got " + this.def.getBytesRead() + " bytes)");
                        }
                        if (zipEntry.csize != this.def.getBytesWritten()) {
                            throw new ZipException("invalid entry compressed size (expected " + zipEntry.csize + " but got " + this.def.getBytesWritten() + " bytes)");
                        }
                        if (zipEntry.crc != this.crc.getValue()) {
                            throw new ZipException("invalid entry CRC-32 (expected 0x" + Long.toHexString(zipEntry.crc) + " but got 0x" + Long.toHexString(this.crc.getValue()) + ")");
                        }
                    }
                    this.def.reset();
                    this.written += zipEntry.csize;
                    break;
                default:
                    throw new ZipException("invalid compression method");
            }
            this.crc.reset();
            this.current = null;
        }
    }

    @Override // java.util.zip.DeflaterOutputStream, java.io.FilterOutputStream, java.io.OutputStream
    public synchronized void write(byte[] bArr, int i2, int i3) throws IOException {
        ensureOpen();
        if (i2 < 0 || i3 < 0 || i2 > bArr.length - i3) {
            throw new IndexOutOfBoundsException();
        }
        if (i3 == 0) {
            return;
        }
        if (this.current == null) {
            throw new ZipException("no current ZIP entry");
        }
        ZipEntry zipEntry = this.current.entry;
        switch (zipEntry.method) {
            case 0:
                this.written += i3;
                if (this.written - this.locoff > zipEntry.size) {
                    throw new ZipException("attempt to write past end of STORED entry");
                }
                this.out.write(bArr, i2, i3);
                break;
            case 8:
                super.write(bArr, i2, i3);
                break;
            default:
                throw new ZipException("invalid compression method");
        }
        this.crc.update(bArr, i2, i3);
    }

    @Override // java.util.zip.DeflaterOutputStream
    public void finish() throws IOException {
        ensureOpen();
        if (this.finished) {
            return;
        }
        if (this.current != null) {
            closeEntry();
        }
        long j2 = this.written;
        Iterator<XEntry> it = this.xentries.iterator();
        while (it.hasNext()) {
            writeCEN(it.next());
        }
        writeEND(j2, this.written - j2);
        this.finished = true;
    }

    @Override // java.util.zip.DeflaterOutputStream, java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            super.close();
            this.closed = true;
        }
    }

    private void writeLOC(XEntry xEntry) throws IOException {
        ZipEntry zipEntry = xEntry.entry;
        int i2 = zipEntry.flag;
        boolean z2 = false;
        int extraLen = getExtraLen(zipEntry.extra);
        writeInt(67324752L);
        if ((i2 & 8) == 8) {
            writeShort(version(zipEntry));
            writeShort(i2);
            writeShort(zipEntry.method);
            writeInt(zipEntry.xdostime);
            writeInt(0L);
            writeInt(0L);
            writeInt(0L);
        } else {
            if (zipEntry.csize >= 4294967295L || zipEntry.size >= 4294967295L) {
                z2 = true;
                writeShort(45);
            } else {
                writeShort(version(zipEntry));
            }
            writeShort(i2);
            writeShort(zipEntry.method);
            writeInt(zipEntry.xdostime);
            writeInt(zipEntry.crc);
            if (z2) {
                writeInt(4294967295L);
                writeInt(4294967295L);
                extraLen += 20;
            } else {
                writeInt(zipEntry.csize);
                writeInt(zipEntry.size);
            }
        }
        byte[] bytes = this.zc.getBytes(zipEntry.name);
        writeShort(bytes.length);
        int i3 = 0;
        int i4 = 0;
        if (zipEntry.mtime != null) {
            i3 = 0 + 4;
            i4 = 0 | 1;
        }
        if (zipEntry.atime != null) {
            i3 += 4;
            i4 |= 2;
        }
        if (zipEntry.ctime != null) {
            i3 += 4;
            i4 |= 4;
        }
        if (i4 != 0) {
            extraLen += i3 + 5;
        }
        writeShort(extraLen);
        writeBytes(bytes, 0, bytes.length);
        if (z2) {
            writeShort(1);
            writeShort(16);
            writeLong(zipEntry.size);
            writeLong(zipEntry.csize);
        }
        if (i4 != 0) {
            writeShort(21589);
            writeShort(i3 + 1);
            writeByte(i4);
            if (zipEntry.mtime != null) {
                writeInt(ZipUtils.fileTimeToUnixTime(zipEntry.mtime));
            }
            if (zipEntry.atime != null) {
                writeInt(ZipUtils.fileTimeToUnixTime(zipEntry.atime));
            }
            if (zipEntry.ctime != null) {
                writeInt(ZipUtils.fileTimeToUnixTime(zipEntry.ctime));
            }
        }
        writeExtra(zipEntry.extra);
        this.locoff = this.written;
    }

    private void writeEXT(ZipEntry zipEntry) throws IOException {
        writeInt(134695760L);
        writeInt(zipEntry.crc);
        if (zipEntry.csize >= 4294967295L || zipEntry.size >= 4294967295L) {
            writeLong(zipEntry.csize);
            writeLong(zipEntry.size);
        } else {
            writeInt(zipEntry.csize);
            writeInt(zipEntry.size);
        }
    }

    private void writeCEN(XEntry xEntry) throws IOException {
        byte[] bytes;
        ZipEntry zipEntry = xEntry.entry;
        int i2 = zipEntry.flag;
        int iVersion = version(zipEntry);
        long j2 = zipEntry.csize;
        long j3 = zipEntry.size;
        long j4 = xEntry.offset;
        int i3 = 0;
        boolean z2 = false;
        if (zipEntry.csize >= 4294967295L) {
            j2 = 4294967295L;
            i3 = 0 + 8;
            z2 = true;
        }
        if (zipEntry.size >= 4294967295L) {
            j3 = 4294967295L;
            i3 += 8;
            z2 = true;
        }
        if (xEntry.offset >= 4294967295L) {
            j4 = 4294967295L;
            i3 += 8;
            z2 = true;
        }
        writeInt(33639248L);
        if (z2) {
            writeShort(45);
            writeShort(45);
        } else {
            writeShort(iVersion);
            writeShort(iVersion);
        }
        writeShort(i2);
        writeShort(zipEntry.method);
        writeInt(zipEntry.xdostime);
        writeInt(zipEntry.crc);
        writeInt(j2);
        writeInt(j3);
        byte[] bytes2 = this.zc.getBytes(zipEntry.name);
        writeShort(bytes2.length);
        int extraLen = getExtraLen(zipEntry.extra);
        if (z2) {
            extraLen += i3 + 4;
        }
        int i4 = 0;
        if (zipEntry.mtime != null) {
            extraLen += 4;
            i4 = 0 | 1;
        }
        if (zipEntry.atime != null) {
            i4 |= 2;
        }
        if (zipEntry.ctime != null) {
            i4 |= 4;
        }
        if (i4 != 0) {
            extraLen += 5;
        }
        writeShort(extraLen);
        if (zipEntry.comment != null) {
            bytes = this.zc.getBytes(zipEntry.comment);
            writeShort(Math.min(bytes.length, 65535));
        } else {
            bytes = null;
            writeShort(0);
        }
        writeShort(0);
        writeShort(0);
        writeInt(0L);
        writeInt(j4);
        writeBytes(bytes2, 0, bytes2.length);
        if (z2) {
            writeShort(1);
            writeShort(i3);
            if (j3 == 4294967295L) {
                writeLong(zipEntry.size);
            }
            if (j2 == 4294967295L) {
                writeLong(zipEntry.csize);
            }
            if (j4 == 4294967295L) {
                writeLong(xEntry.offset);
            }
        }
        if (i4 != 0) {
            writeShort(21589);
            if (zipEntry.mtime != null) {
                writeShort(5);
                writeByte(i4);
                writeInt(ZipUtils.fileTimeToUnixTime(zipEntry.mtime));
            } else {
                writeShort(1);
                writeByte(i4);
            }
        }
        writeExtra(zipEntry.extra);
        if (bytes != null) {
            writeBytes(bytes, 0, Math.min(bytes.length, 65535));
        }
    }

    private void writeEND(long j2, long j3) throws IOException {
        boolean z2 = false;
        long j4 = j3;
        long j5 = j2;
        if (j4 >= 4294967295L) {
            j4 = 4294967295L;
            z2 = true;
        }
        if (j5 >= 4294967295L) {
            j5 = 4294967295L;
            z2 = true;
        }
        int size = this.xentries.size();
        if (size >= 65535) {
            z2 |= !inhibitZip64;
            if (z2) {
                size = 65535;
            }
        }
        if (z2) {
            long j6 = this.written;
            writeInt(InternalZipConstants.ZIP64ENDCENDIRREC);
            writeLong(44L);
            writeShort(45);
            writeShort(45);
            writeInt(0L);
            writeInt(0L);
            writeLong(this.xentries.size());
            writeLong(this.xentries.size());
            writeLong(j3);
            writeLong(j2);
            writeInt(InternalZipConstants.ZIP64ENDCENDIRLOC);
            writeInt(0L);
            writeLong(j6);
            writeInt(1L);
        }
        writeInt(101010256L);
        writeShort(0);
        writeShort(0);
        writeShort(size);
        writeShort(size);
        writeInt(j4);
        writeInt(j5);
        if (this.comment != null) {
            writeShort(this.comment.length);
            writeBytes(this.comment, 0, this.comment.length);
        } else {
            writeShort(0);
        }
    }

    private int getExtraLen(byte[] bArr) {
        if (bArr == null) {
            return 0;
        }
        int i2 = 0;
        int length = bArr.length;
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 + 4 > length) {
                break;
            }
            int i5 = ZipUtils.get16(bArr, i4);
            int i6 = ZipUtils.get16(bArr, i4 + 2);
            if (i6 < 0 || i4 + 4 + i6 > length) {
                break;
            }
            if (i5 == 21589 || i5 == 1) {
                i2 += i6 + 4;
            }
            i3 = i4 + i6 + 4;
        }
        return length - i2;
    }

    private void writeExtra(byte[] bArr) throws IOException {
        int i2;
        if (bArr != null) {
            int length = bArr.length;
            int i3 = 0;
            while (true) {
                i2 = i3;
                if (i2 + 4 <= length) {
                    int i4 = ZipUtils.get16(bArr, i2);
                    int i5 = ZipUtils.get16(bArr, i2 + 2);
                    if (i5 < 0 || i2 + 4 + i5 > length) {
                        break;
                    }
                    if (i4 != 21589 && i4 != 1) {
                        writeBytes(bArr, i2, i5 + 4);
                    }
                    i3 = i2 + i5 + 4;
                } else {
                    if (i2 < length) {
                        writeBytes(bArr, i2, length - i2);
                        return;
                    }
                    return;
                }
            }
            writeBytes(bArr, i2, length - i2);
        }
    }

    private void writeByte(int i2) throws IOException {
        this.out.write(i2 & 255);
        this.written++;
    }

    private void writeShort(int i2) throws IOException {
        OutputStream outputStream = this.out;
        outputStream.write((i2 >>> 0) & 255);
        outputStream.write((i2 >>> 8) & 255);
        this.written += 2;
    }

    private void writeInt(long j2) throws IOException {
        OutputStream outputStream = this.out;
        outputStream.write((int) ((j2 >>> 0) & 255));
        outputStream.write((int) ((j2 >>> 8) & 255));
        outputStream.write((int) ((j2 >>> 16) & 255));
        outputStream.write((int) ((j2 >>> 24) & 255));
        this.written += 4;
    }

    private void writeLong(long j2) throws IOException {
        OutputStream outputStream = this.out;
        outputStream.write((int) ((j2 >>> 0) & 255));
        outputStream.write((int) ((j2 >>> 8) & 255));
        outputStream.write((int) ((j2 >>> 16) & 255));
        outputStream.write((int) ((j2 >>> 24) & 255));
        outputStream.write((int) ((j2 >>> 32) & 255));
        outputStream.write((int) ((j2 >>> 40) & 255));
        outputStream.write((int) ((j2 >>> 48) & 255));
        outputStream.write((int) ((j2 >>> 56) & 255));
        this.written += 8;
    }

    private void writeBytes(byte[] bArr, int i2, int i3) throws IOException {
        this.out.write(bArr, i2, i3);
        this.written += i3;
    }
}
