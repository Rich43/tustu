package com.sun.xml.internal.org.jvnet.mimepull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/DataHead.class */
final class DataHead {
    volatile Chunk head;
    volatile Chunk tail;
    DataFile dataFile;
    private final MIMEPart part;
    boolean readOnce;
    volatile long inMemory;
    private Throwable consumedAt;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DataHead.class.desiredAssertionStatus();
    }

    DataHead(MIMEPart part) {
        this.part = part;
    }

    void addBody(ByteBuffer buf) {
        synchronized (this) {
            this.inMemory += buf.limit();
        }
        if (this.tail != null) {
            this.tail = this.tail.createNext(this, buf);
            return;
        }
        Chunk chunk = new Chunk(new MemoryData(buf, this.part.msg.config));
        this.tail = chunk;
        this.head = chunk;
    }

    void doneParsing() {
    }

    /* JADX WARN: Finally extract failed */
    void moveTo(File f2) {
        if (this.dataFile != null) {
            this.dataFile.renameTo(f2);
            return;
        }
        try {
            OutputStream os = new FileOutputStream(f2);
            try {
                InputStream in = readOnce();
                byte[] buf = new byte[8192];
                while (true) {
                    int len = in.read(buf);
                    if (len == -1) {
                        break;
                    } else {
                        os.write(buf, 0, len);
                    }
                }
                if (os != null) {
                    os.close();
                }
            } catch (Throwable th) {
                if (os != null) {
                    os.close();
                }
                throw th;
            }
        } catch (IOException ioe) {
            throw new MIMEParsingException(ioe);
        }
    }

    void close() {
        this.tail = null;
        this.head = null;
        if (this.dataFile != null) {
            this.dataFile.close();
        }
    }

    public InputStream read() {
        if (this.readOnce) {
            throw new IllegalStateException("readOnce() is called before, read() cannot be called later.");
        }
        while (this.tail == null) {
            if (!this.part.msg.makeProgress()) {
                throw new IllegalStateException("No such MIME Part: " + ((Object) this.part));
            }
        }
        if (this.head == null) {
            throw new IllegalStateException("Already read. Probably readOnce() is called before.");
        }
        return new ReadMultiStream();
    }

    private boolean unconsumed() {
        if (this.consumedAt != null) {
            AssertionError error = new AssertionError((Object) "readOnce() is already called before. See the nested exception from where it's called.");
            error.initCause(this.consumedAt);
            throw error;
        }
        this.consumedAt = new Exception().fillInStackTrace();
        return true;
    }

    public InputStream readOnce() {
        if (!$assertionsDisabled && !unconsumed()) {
            throw new AssertionError();
        }
        if (this.readOnce) {
            throw new IllegalStateException("readOnce() is called before. It can only be called once.");
        }
        this.readOnce = true;
        while (this.tail == null) {
            if (!this.part.msg.makeProgress() && this.tail == null) {
                throw new IllegalStateException("No such Part: " + ((Object) this.part));
            }
        }
        InputStream in = new ReadOnceStream();
        this.head = null;
        return in;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/DataHead$ReadMultiStream.class */
    class ReadMultiStream extends InputStream {
        Chunk current;
        int offset;
        int len;
        byte[] buf;
        boolean closed;

        public ReadMultiStream() {
            this.current = DataHead.this.head;
            this.len = this.current.data.size();
            this.buf = this.current.data.read();
        }

        @Override // java.io.InputStream
        public int read(byte[] b2, int off, int sz) throws IOException {
            if (!fetch()) {
                return -1;
            }
            int sz2 = Math.min(sz, this.len - this.offset);
            System.arraycopy(this.buf, this.offset, b2, off, sz2);
            this.offset += sz2;
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

        void adjustInMemoryUsage() {
        }

        private boolean fetch() throws IOException {
            if (this.closed) {
                throw new IOException("Stream already closed");
            }
            if (this.current == null) {
                return false;
            }
            while (this.offset == this.len) {
                while (!DataHead.this.part.parsed && this.current.next == null) {
                    DataHead.this.part.msg.makeProgress();
                }
                this.current = this.current.next;
                if (this.current == null) {
                    return false;
                }
                adjustInMemoryUsage();
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
            this.closed = true;
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/DataHead$ReadOnceStream.class */
    final class ReadOnceStream extends ReadMultiStream {
        ReadOnceStream() {
            super();
        }

        @Override // com.sun.xml.internal.org.jvnet.mimepull.DataHead.ReadMultiStream
        void adjustInMemoryUsage() {
            synchronized (DataHead.this) {
                DataHead.this.inMemory -= this.current.data.size();
            }
        }
    }
}
