package sun.net.httpserver;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:sun/net/httpserver/ChunkedOutputStream.class */
class ChunkedOutputStream extends FilterOutputStream {
    private boolean closed;
    static final int CHUNK_SIZE = 4096;
    static final int OFFSET = 6;
    private int pos;
    private int count;
    private byte[] buf;

    /* renamed from: t, reason: collision with root package name */
    ExchangeImpl f13579t;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ChunkedOutputStream.class.desiredAssertionStatus();
    }

    ChunkedOutputStream(ExchangeImpl exchangeImpl, OutputStream outputStream) {
        super(outputStream);
        this.closed = false;
        this.pos = 6;
        this.count = 0;
        this.buf = new byte[4104];
        this.f13579t = exchangeImpl;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i2) throws IOException {
        if (this.closed) {
            throw new StreamClosedException();
        }
        byte[] bArr = this.buf;
        int i3 = this.pos;
        this.pos = i3 + 1;
        bArr[i3] = (byte) i2;
        this.count++;
        if (this.count == 4096) {
            writeChunk();
        }
        if (!$assertionsDisabled && this.count >= 4096) {
            throw new AssertionError();
        }
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        if (this.closed) {
            throw new StreamClosedException();
        }
        int i4 = 4096 - this.count;
        if (i3 > i4) {
            System.arraycopy(bArr, i2, this.buf, this.pos, i4);
            this.count = 4096;
            writeChunk();
            i3 -= i4;
            i2 += i4;
            while (i3 >= 4096) {
                System.arraycopy(bArr, i2, this.buf, 6, 4096);
                i3 -= 4096;
                i2 += 4096;
                this.count = 4096;
                writeChunk();
            }
        }
        if (i3 > 0) {
            System.arraycopy(bArr, i2, this.buf, this.pos, i3);
            this.count += i3;
            this.pos += i3;
        }
        if (this.count == 4096) {
            writeChunk();
        }
    }

    private void writeChunk() throws IOException {
        char[] charArray = Integer.toHexString(this.count).toCharArray();
        int length = charArray.length;
        int i2 = 4 - length;
        int i3 = 0;
        while (i3 < length) {
            this.buf[i2 + i3] = (byte) charArray[i3];
            i3++;
        }
        int i4 = i3;
        int i5 = i3 + 1;
        this.buf[i2 + i4] = 13;
        int i6 = i5 + 1;
        this.buf[i2 + i5] = 10;
        int i7 = i6 + 1;
        this.buf[i2 + i6 + this.count] = 13;
        this.buf[i2 + i7 + this.count] = 10;
        this.out.write(this.buf, i2, i7 + 1 + this.count);
        this.count = 0;
        this.pos = 6;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closed) {
            return;
        }
        flush();
        try {
            writeChunk();
            this.out.flush();
            LeftOverInputStream originalInputStream = this.f13579t.getOriginalInputStream();
            if (!originalInputStream.isClosed()) {
                originalInputStream.close();
            }
        } catch (IOException e2) {
        } finally {
            this.closed = true;
        }
        this.f13579t.getHttpContext().getServerImpl().addEvent(new WriteFinishedEvent(this.f13579t));
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        if (this.closed) {
            throw new StreamClosedException();
        }
        if (this.count > 0) {
            writeChunk();
        }
        this.out.flush();
    }
}
