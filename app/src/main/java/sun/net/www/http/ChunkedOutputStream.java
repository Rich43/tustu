package sun.net.www.http;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/* loaded from: rt.jar:sun/net/www/http/ChunkedOutputStream.class */
public class ChunkedOutputStream extends PrintStream {
    static final int DEFAULT_CHUNK_SIZE = 4096;
    private static final byte[] CRLF = {13, 10};
    private static final int CRLF_SIZE = CRLF.length;
    private static final byte[] FOOTER = CRLF;
    private static final int FOOTER_SIZE = CRLF_SIZE;
    private static final byte[] EMPTY_CHUNK_HEADER = getHeader(0);
    private static final int EMPTY_CHUNK_HEADER_SIZE = getHeaderSize(0);
    private byte[] buf;
    private int size;
    private int count;
    private int spaceInCurrentChunk;
    private PrintStream out;
    private int preferredChunkDataSize;
    private int preferedHeaderSize;
    private int preferredChunkGrossSize;
    private byte[] completeHeader;

    private static int getHeaderSize(int i2) {
        return Integer.toHexString(i2).length() + CRLF_SIZE;
    }

    private static byte[] getHeader(int i2) {
        try {
            byte[] bytes = Integer.toHexString(i2).getBytes("US-ASCII");
            byte[] bArr = new byte[getHeaderSize(i2)];
            for (int i3 = 0; i3 < bytes.length; i3++) {
                bArr[i3] = bytes[i3];
            }
            bArr[bytes.length] = CRLF[0];
            bArr[bytes.length + 1] = CRLF[1];
            return bArr;
        } catch (UnsupportedEncodingException e2) {
            throw new InternalError(e2.getMessage(), e2);
        }
    }

    public ChunkedOutputStream(PrintStream printStream) {
        this(printStream, 4096);
    }

    public ChunkedOutputStream(PrintStream printStream, int i2) {
        super(printStream);
        this.out = printStream;
        i2 = i2 <= 0 ? 4096 : i2;
        if (i2 > 0) {
            int headerSize = (i2 - getHeaderSize(i2)) - FOOTER_SIZE;
            i2 = getHeaderSize(headerSize + 1) < getHeaderSize(i2) ? headerSize + 1 : headerSize;
        }
        if (i2 > 0) {
            this.preferredChunkDataSize = i2;
        } else {
            this.preferredChunkDataSize = (4096 - getHeaderSize(4096)) - FOOTER_SIZE;
        }
        this.preferedHeaderSize = getHeaderSize(this.preferredChunkDataSize);
        this.preferredChunkGrossSize = this.preferedHeaderSize + this.preferredChunkDataSize + FOOTER_SIZE;
        this.completeHeader = getHeader(this.preferredChunkDataSize);
        this.buf = new byte[this.preferredChunkGrossSize];
        reset();
    }

    private void flush(boolean z2) {
        if (this.spaceInCurrentChunk == 0) {
            this.out.write(this.buf, 0, this.preferredChunkGrossSize);
            this.out.flush();
            reset();
            return;
        }
        if (z2) {
            if (this.size > 0) {
                int headerSize = this.preferedHeaderSize - getHeaderSize(this.size);
                System.arraycopy(getHeader(this.size), 0, this.buf, headerSize, getHeaderSize(this.size));
                byte[] bArr = this.buf;
                int i2 = this.count;
                this.count = i2 + 1;
                bArr[i2] = FOOTER[0];
                byte[] bArr2 = this.buf;
                int i3 = this.count;
                this.count = i3 + 1;
                bArr2[i3] = FOOTER[1];
                this.out.write(this.buf, headerSize, this.count - headerSize);
            } else {
                this.out.write(EMPTY_CHUNK_HEADER, 0, EMPTY_CHUNK_HEADER_SIZE);
            }
            this.out.flush();
            reset();
        }
    }

    @Override // java.io.PrintStream
    public boolean checkError() {
        return this.out.checkError();
    }

    private void ensureOpen() {
        if (this.out == null) {
            setError();
        }
    }

    @Override // java.io.PrintStream, java.io.FilterOutputStream, java.io.OutputStream
    public synchronized void write(byte[] bArr, int i2, int i3) {
        ensureOpen();
        if (i2 < 0 || i2 > bArr.length || i3 < 0 || i2 + i3 > bArr.length || i2 + i3 < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (i3 == 0) {
            return;
        }
        int i4 = i3;
        int i5 = i2;
        do {
            if (i4 >= this.spaceInCurrentChunk) {
                for (int i6 = 0; i6 < this.completeHeader.length; i6++) {
                    this.buf[i6] = this.completeHeader[i6];
                }
                System.arraycopy(bArr, i5, this.buf, this.count, this.spaceInCurrentChunk);
                i5 += this.spaceInCurrentChunk;
                i4 -= this.spaceInCurrentChunk;
                this.count += this.spaceInCurrentChunk;
                byte[] bArr2 = this.buf;
                int i7 = this.count;
                this.count = i7 + 1;
                bArr2[i7] = FOOTER[0];
                byte[] bArr3 = this.buf;
                int i8 = this.count;
                this.count = i8 + 1;
                bArr3[i8] = FOOTER[1];
                this.spaceInCurrentChunk = 0;
                flush(false);
                if (checkError()) {
                    return;
                }
            } else {
                System.arraycopy(bArr, i5, this.buf, this.count, i4);
                this.count += i4;
                this.size += i4;
                this.spaceInCurrentChunk -= i4;
                i4 = 0;
            }
        } while (i4 > 0);
    }

    @Override // java.io.PrintStream, java.io.FilterOutputStream, java.io.OutputStream
    public synchronized void write(int i2) {
        write(new byte[]{(byte) i2}, 0, 1);
    }

    public synchronized void reset() {
        this.count = this.preferedHeaderSize;
        this.size = 0;
        this.spaceInCurrentChunk = this.preferredChunkDataSize;
    }

    public int size() {
        return this.size;
    }

    @Override // java.io.PrintStream, java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() {
        ensureOpen();
        if (this.size > 0) {
            flush(true);
        }
        flush(true);
        this.out = null;
    }

    @Override // java.io.PrintStream, java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public synchronized void flush() {
        ensureOpen();
        if (this.size > 0) {
            flush(true);
        }
    }
}
