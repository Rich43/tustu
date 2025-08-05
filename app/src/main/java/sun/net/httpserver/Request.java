package sun.net.httpserver;

import com.sun.net.httpserver.Headers;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/* loaded from: rt.jar:sun/net/httpserver/Request.class */
class Request {
    static final int BUF_LEN = 2048;
    static final byte CR = 13;
    static final byte LF = 10;
    private SocketChannel chan;
    private InputStream is;
    private OutputStream os;
    int pos;
    StringBuffer lineBuf;
    char[] buf = new char[2048];
    Headers hdrs = null;
    private String startLine = readLine();

    Request(InputStream inputStream, OutputStream outputStream) throws IOException {
        this.is = inputStream;
        this.os = outputStream;
        do {
            if (this.startLine == null || this.startLine == null) {
                return;
            }
        } while (this.startLine.equals(""));
    }

    public InputStream inputStream() {
        return this.is;
    }

    public OutputStream outputStream() {
        return this.os;
    }

    public String readLine() throws IOException {
        boolean z2 = false;
        boolean z3 = false;
        this.pos = 0;
        this.lineBuf = new StringBuffer();
        while (!z3) {
            int i2 = this.is.read();
            if (i2 == -1) {
                return null;
            }
            if (z2) {
                if (i2 == 10) {
                    z3 = true;
                } else {
                    z2 = false;
                    consume(13);
                    consume(i2);
                }
            } else if (i2 == 13) {
                z2 = true;
            } else {
                consume(i2);
            }
        }
        this.lineBuf.append(this.buf, 0, this.pos);
        return new String(this.lineBuf);
    }

    private void consume(int i2) {
        if (this.pos == 2048) {
            this.lineBuf.append(this.buf);
            this.pos = 0;
        }
        char[] cArr = this.buf;
        int i3 = this.pos;
        this.pos = i3 + 1;
        cArr[i3] = (char) i2;
    }

    public String requestLine() {
        return this.startLine;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:104:0x013a A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0126  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    com.sun.net.httpserver.Headers headers() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 517
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.httpserver.Request.headers():com.sun.net.httpserver.Headers");
    }

    /* loaded from: rt.jar:sun/net/httpserver/Request$ReadStream.class */
    static class ReadStream extends InputStream {
        SocketChannel channel;
        byte[] one;
        private boolean closed;
        ByteBuffer markBuf;
        boolean marked;
        boolean reset;
        int readlimit;
        static long readTimeout;
        ServerImpl server;
        static final int BUFSIZE = 8192;
        static final /* synthetic */ boolean $assertionsDisabled;
        private boolean eof = false;
        ByteBuffer chanbuf = ByteBuffer.allocate(8192);

        static {
            $assertionsDisabled = !Request.class.desiredAssertionStatus();
        }

        public ReadStream(ServerImpl serverImpl, SocketChannel socketChannel) throws IOException {
            this.closed = false;
            this.channel = socketChannel;
            this.server = serverImpl;
            this.chanbuf.clear();
            this.one = new byte[1];
            this.reset = false;
            this.marked = false;
            this.closed = false;
        }

        @Override // java.io.InputStream
        public synchronized int read(byte[] bArr) throws IOException {
            return read(bArr, 0, bArr.length);
        }

        @Override // java.io.InputStream
        public synchronized int read() throws IOException {
            if (read(this.one, 0, 1) == 1) {
                return this.one[0] & 255;
            }
            return -1;
        }

        @Override // java.io.InputStream
        public synchronized int read(byte[] bArr, int i2, int i3) throws IOException {
            int i4;
            if (this.closed) {
                throw new IOException("Stream closed");
            }
            if (this.eof) {
                return -1;
            }
            if (!$assertionsDisabled && !this.channel.isBlocking()) {
                throw new AssertionError();
            }
            if (i2 < 0 || i3 < 0 || i3 > bArr.length - i2) {
                throw new IndexOutOfBoundsException();
            }
            if (this.reset) {
                int iRemaining = this.markBuf.remaining();
                i4 = iRemaining > i3 ? i3 : iRemaining;
                this.markBuf.get(bArr, i2, i4);
                if (iRemaining == i4) {
                    this.reset = false;
                }
            } else {
                this.chanbuf.clear();
                if (i3 < 8192) {
                    this.chanbuf.limit(i3);
                }
                do {
                    i4 = this.channel.read(this.chanbuf);
                } while (i4 == 0);
                if (i4 == -1) {
                    this.eof = true;
                    return -1;
                }
                this.chanbuf.flip();
                this.chanbuf.get(bArr, i2, i4);
                if (this.marked) {
                    try {
                        this.markBuf.put(bArr, i2, i4);
                    } catch (BufferOverflowException e2) {
                        this.marked = false;
                    }
                }
            }
            return i4;
        }

        @Override // java.io.InputStream
        public boolean markSupported() {
            return true;
        }

        @Override // java.io.InputStream
        public synchronized int available() throws IOException {
            if (this.closed) {
                throw new IOException("Stream is closed");
            }
            if (this.eof) {
                return -1;
            }
            if (this.reset) {
                return this.markBuf.remaining();
            }
            return this.chanbuf.remaining();
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.closed) {
                return;
            }
            this.channel.close();
            this.closed = true;
        }

        @Override // java.io.InputStream
        public synchronized void mark(int i2) {
            if (this.closed) {
                return;
            }
            this.readlimit = i2;
            this.markBuf = ByteBuffer.allocate(i2);
            this.marked = true;
            this.reset = false;
        }

        @Override // java.io.InputStream
        public synchronized void reset() throws IOException {
            if (this.closed) {
                return;
            }
            if (!this.marked) {
                throw new IOException("Stream not marked");
            }
            this.marked = false;
            this.reset = true;
            this.markBuf.flip();
        }
    }

    /* loaded from: rt.jar:sun/net/httpserver/Request$WriteStream.class */
    static class WriteStream extends OutputStream {
        SocketChannel channel;
        ByteBuffer buf;
        SelectionKey key;
        boolean closed;
        byte[] one;
        ServerImpl server;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Request.class.desiredAssertionStatus();
        }

        public WriteStream(ServerImpl serverImpl, SocketChannel socketChannel) throws IOException {
            this.channel = socketChannel;
            this.server = serverImpl;
            if (!$assertionsDisabled && !socketChannel.isBlocking()) {
                throw new AssertionError();
            }
            this.closed = false;
            this.one = new byte[1];
            this.buf = ByteBuffer.allocate(4096);
        }

        @Override // java.io.OutputStream
        public synchronized void write(int i2) throws IOException {
            this.one[0] = (byte) i2;
            write(this.one, 0, 1);
        }

        @Override // java.io.OutputStream
        public synchronized void write(byte[] bArr) throws IOException {
            write(bArr, 0, bArr.length);
        }

        @Override // java.io.OutputStream
        public synchronized void write(byte[] bArr, int i2, int i3) throws IOException {
            int i4 = i3;
            if (this.closed) {
                throw new IOException("stream is closed");
            }
            int iCapacity = this.buf.capacity();
            if (iCapacity < i3) {
                this.buf = ByteBuffer.allocate(2 * (iCapacity + (i3 - iCapacity)));
            }
            this.buf.clear();
            this.buf.put(bArr, i2, i3);
            this.buf.flip();
            do {
                int iWrite = this.channel.write(this.buf);
                if (iWrite < i4) {
                    i4 -= iWrite;
                } else {
                    return;
                }
            } while (i4 != 0);
        }

        @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.closed) {
                return;
            }
            this.channel.close();
            this.closed = true;
        }
    }
}
