package java.util.zip;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;

/* loaded from: rt.jar:java/util/zip/GZIPInputStream.class */
public class GZIPInputStream extends InflaterInputStream {
    protected CRC32 crc;
    protected boolean eos;
    private boolean closed;
    public static final int GZIP_MAGIC = 35615;
    private static final int FTEXT = 1;
    private static final int FHCRC = 2;
    private static final int FEXTRA = 4;
    private static final int FNAME = 8;
    private static final int FCOMMENT = 16;
    private byte[] tmpbuf;

    private void ensureOpen() throws IOException {
        if (this.closed) {
            throw new IOException("Stream closed");
        }
    }

    public GZIPInputStream(InputStream inputStream, int i2) throws IOException {
        super(inputStream, new Inflater(true), i2);
        this.crc = new CRC32();
        this.closed = false;
        this.tmpbuf = new byte[128];
        this.usesDefaultInflater = true;
        readHeader(inputStream);
    }

    public GZIPInputStream(InputStream inputStream) throws IOException {
        this(inputStream, 512);
    }

    @Override // java.util.zip.InflaterInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        ensureOpen();
        if (this.eos) {
            return -1;
        }
        int i4 = super.read(bArr, i2, i3);
        if (i4 == -1) {
            if (readTrailer()) {
                this.eos = true;
            } else {
                return read(bArr, i2, i3);
            }
        } else {
            this.crc.update(bArr, i2, i4);
        }
        return i4;
    }

    @Override // java.util.zip.InflaterInputStream, java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.closed) {
            super.close();
            this.eos = true;
            this.closed = true;
        }
    }

    private int readHeader(InputStream inputStream) throws IOException {
        CheckedInputStream checkedInputStream = new CheckedInputStream(inputStream, this.crc);
        this.crc.reset();
        if (readUShort(checkedInputStream) != 35615) {
            throw new ZipException("Not in GZIP format");
        }
        if (readUByte(checkedInputStream) != 8) {
            throw new ZipException("Unsupported compression method");
        }
        int uByte = readUByte(checkedInputStream);
        skipBytes(checkedInputStream, 6);
        int i2 = 10;
        if ((uByte & 4) == 4) {
            int uShort = readUShort(checkedInputStream);
            skipBytes(checkedInputStream, uShort);
            i2 = 10 + uShort + 2;
        }
        if ((uByte & 8) == 8) {
            do {
                i2++;
            } while (readUByte(checkedInputStream) != 0);
        }
        if ((uByte & 16) == 16) {
            do {
                i2++;
            } while (readUByte(checkedInputStream) != 0);
        }
        if ((uByte & 2) == 2) {
            if (readUShort(checkedInputStream) != (((int) this.crc.getValue()) & 65535)) {
                throw new ZipException("Corrupt GZIP header");
            }
            i2 += 2;
        }
        this.crc.reset();
        return i2;
    }

    private boolean readTrailer() throws IOException {
        InputStream sequenceInputStream = this.in;
        int remaining = this.inf.getRemaining();
        if (remaining > 0) {
            sequenceInputStream = new SequenceInputStream(new ByteArrayInputStream(this.buf, this.len - remaining, remaining), new FilterInputStream(sequenceInputStream) { // from class: java.util.zip.GZIPInputStream.1
                @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
                public void close() throws IOException {
                }
            });
        }
        if (readUInt(sequenceInputStream) != this.crc.getValue() || readUInt(sequenceInputStream) != (this.inf.getBytesWritten() & 4294967295L)) {
            throw new ZipException("Corrupt GZIP trailer");
        }
        if (this.in.available() > 0 || remaining > 26) {
            try {
                int header = 8 + readHeader(sequenceInputStream);
                this.inf.reset();
                if (remaining > header) {
                    this.inf.setInput(this.buf, (this.len - remaining) + header, remaining - header);
                    return false;
                }
                return false;
            } catch (IOException e2) {
                return true;
            }
        }
        return true;
    }

    private long readUInt(InputStream inputStream) throws IOException {
        return (readUShort(inputStream) << 16) | readUShort(inputStream);
    }

    private int readUShort(InputStream inputStream) throws IOException {
        return (readUByte(inputStream) << 8) | readUByte(inputStream);
    }

    private int readUByte(InputStream inputStream) throws IOException {
        int i2 = inputStream.read();
        if (i2 == -1) {
            throw new EOFException();
        }
        if (i2 < -1 || i2 > 255) {
            throw new IOException(this.in.getClass().getName() + ".read() returned value out of range -1..255: " + i2);
        }
        return i2;
    }

    private void skipBytes(InputStream inputStream, int i2) throws IOException {
        while (i2 > 0) {
            int i3 = inputStream.read(this.tmpbuf, 0, i2 < this.tmpbuf.length ? i2 : this.tmpbuf.length);
            if (i3 == -1) {
                throw new EOFException();
            }
            i2 -= i3;
        }
    }
}
