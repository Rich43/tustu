package com.sun.xml.internal.org.jvnet.mimepull;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/ChunkInputStream.class */
final class ChunkInputStream extends InputStream {
    Chunk current;
    int offset;
    int len;
    final MIMEMessage msg;
    final MIMEPart part;
    byte[] buf;

    public ChunkInputStream(MIMEMessage msg, MIMEPart part, Chunk startPos) {
        this.current = startPos;
        this.len = this.current.data.size();
        this.buf = this.current.data.read();
        this.msg = msg;
        this.part = part;
    }

    @Override // java.io.InputStream
    public int read(byte[] b2, int off, int sz) throws IOException {
        if (!fetch()) {
            return -1;
        }
        int sz2 = Math.min(sz, this.len - this.offset);
        System.arraycopy(this.buf, this.offset, b2, off, sz2);
        return sz2;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (!fetch()) {
            return -1;
        }
        byte[] bArr = this.buf;
        int i2 = this.offset;
        this.offset = i2 + 1;
        return bArr[i2] & 255;
    }

    private boolean fetch() {
        if (this.current == null) {
            throw new IllegalStateException("Stream already closed");
        }
        while (this.offset == this.len) {
            while (!this.part.parsed && this.current.next == null) {
                this.msg.makeProgress();
            }
            this.current = this.current.next;
            if (this.current == null) {
                return false;
            }
            this.offset = 0;
            this.buf = this.current.data.read();
            this.len = this.current.data.size();
        }
        return true;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        this.current = null;
    }
}
