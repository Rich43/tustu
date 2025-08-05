package sun.net.httpserver;

import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsParameters;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLParameters;

/* loaded from: rt.jar:sun/net/httpserver/SSLStreams.class */
class SSLStreams {
    SSLContext sslctx;
    SocketChannel chan;
    ServerImpl server;
    SSLEngine engine;
    EngineWrapper wrapper;
    OutputStream os;
    InputStream is;
    Lock handshaking = new ReentrantLock();
    int app_buf_size;
    int packet_buf_size;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: rt.jar:sun/net/httpserver/SSLStreams$BufType.class */
    enum BufType {
        PACKET,
        APPLICATION
    }

    static {
        $assertionsDisabled = !SSLStreams.class.desiredAssertionStatus();
    }

    SSLStreams(ServerImpl serverImpl, SSLContext sSLContext, SocketChannel socketChannel) throws IOException {
        this.server = serverImpl;
        this.sslctx = sSLContext;
        this.chan = socketChannel;
        InetSocketAddress inetSocketAddress = (InetSocketAddress) socketChannel.socket().getRemoteSocketAddress();
        this.engine = sSLContext.createSSLEngine(inetSocketAddress.getHostName(), inetSocketAddress.getPort());
        this.engine.setUseClientMode(false);
        configureEngine(serverImpl.getHttpsConfigurator(), inetSocketAddress);
        this.wrapper = new EngineWrapper(socketChannel, this.engine);
    }

    private void configureEngine(HttpsConfigurator httpsConfigurator, InetSocketAddress inetSocketAddress) {
        if (httpsConfigurator != null) {
            Parameters parameters = new Parameters(httpsConfigurator, inetSocketAddress);
            httpsConfigurator.configure(parameters);
            SSLParameters sSLParameters = parameters.getSSLParameters();
            if (sSLParameters != null) {
                this.engine.setSSLParameters(sSLParameters);
                return;
            }
            if (parameters.getCipherSuites() != null) {
                try {
                    this.engine.setEnabledCipherSuites(parameters.getCipherSuites());
                } catch (IllegalArgumentException e2) {
                }
            }
            this.engine.setNeedClientAuth(parameters.getNeedClientAuth());
            this.engine.setWantClientAuth(parameters.getWantClientAuth());
            if (parameters.getProtocols() != null) {
                try {
                    this.engine.setEnabledProtocols(parameters.getProtocols());
                } catch (IllegalArgumentException e3) {
                }
            }
        }
    }

    /* loaded from: rt.jar:sun/net/httpserver/SSLStreams$Parameters.class */
    class Parameters extends HttpsParameters {
        InetSocketAddress addr;
        HttpsConfigurator cfg;
        SSLParameters params;

        Parameters(HttpsConfigurator httpsConfigurator, InetSocketAddress inetSocketAddress) {
            this.addr = inetSocketAddress;
            this.cfg = httpsConfigurator;
        }

        @Override // com.sun.net.httpserver.HttpsParameters
        public InetSocketAddress getClientAddress() {
            return this.addr;
        }

        @Override // com.sun.net.httpserver.HttpsParameters
        public HttpsConfigurator getHttpsConfigurator() {
            return this.cfg;
        }

        @Override // com.sun.net.httpserver.HttpsParameters
        public void setSSLParameters(SSLParameters sSLParameters) {
            this.params = sSLParameters;
        }

        SSLParameters getSSLParameters() {
            return this.params;
        }
    }

    void close() throws IOException {
        this.wrapper.close();
    }

    InputStream getInputStream() throws IOException {
        if (this.is == null) {
            this.is = new InputStream();
        }
        return this.is;
    }

    OutputStream getOutputStream() throws IOException {
        if (this.os == null) {
            this.os = new OutputStream();
        }
        return this.os;
    }

    SSLEngine getSSLEngine() {
        return this.engine;
    }

    void beginHandshake() throws SSLException {
        this.engine.beginHandshake();
    }

    /* loaded from: rt.jar:sun/net/httpserver/SSLStreams$WrapperResult.class */
    class WrapperResult {
        SSLEngineResult result;
        ByteBuffer buf;

        WrapperResult() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ByteBuffer allocate(BufType bufType) {
        return allocate(bufType, -1);
    }

    private ByteBuffer allocate(BufType bufType, int i2) {
        int i3;
        ByteBuffer byteBufferAllocate;
        if (!$assertionsDisabled && this.engine == null) {
            throw new AssertionError();
        }
        synchronized (this) {
            if (bufType == BufType.PACKET) {
                if (this.packet_buf_size == 0) {
                    this.packet_buf_size = this.engine.getSession().getPacketBufferSize();
                }
                if (i2 > this.packet_buf_size) {
                    this.packet_buf_size = i2;
                }
                i3 = this.packet_buf_size;
            } else {
                if (this.app_buf_size == 0) {
                    this.app_buf_size = this.engine.getSession().getApplicationBufferSize();
                }
                if (i2 > this.app_buf_size) {
                    this.app_buf_size = i2;
                }
                i3 = this.app_buf_size;
            }
            byteBufferAllocate = ByteBuffer.allocate(i3);
        }
        return byteBufferAllocate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ByteBuffer realloc(ByteBuffer byteBuffer, boolean z2, BufType bufType) {
        ByteBuffer byteBufferAllocate;
        synchronized (this) {
            byteBufferAllocate = allocate(bufType, 2 * byteBuffer.capacity());
            if (z2) {
                byteBuffer.flip();
            }
            byteBufferAllocate.put(byteBuffer);
        }
        return byteBufferAllocate;
    }

    /* loaded from: rt.jar:sun/net/httpserver/SSLStreams$EngineWrapper.class */
    class EngineWrapper {
        SocketChannel chan;
        SSLEngine engine;
        ByteBuffer unwrap_src;
        ByteBuffer wrap_dst;
        int u_remaining;
        static final /* synthetic */ boolean $assertionsDisabled;
        boolean closed = false;
        Object wrapLock = new Object();
        Object unwrapLock = new Object();

        static {
            $assertionsDisabled = !SSLStreams.class.desiredAssertionStatus();
        }

        EngineWrapper(SocketChannel socketChannel, SSLEngine sSLEngine) throws IOException {
            this.chan = socketChannel;
            this.engine = sSLEngine;
            this.unwrap_src = SSLStreams.this.allocate(BufType.PACKET);
            this.wrap_dst = SSLStreams.this.allocate(BufType.PACKET);
        }

        void close() throws IOException {
        }

        WrapperResult wrapAndSend(ByteBuffer byteBuffer) throws IOException {
            return wrapAndSendX(byteBuffer, false);
        }

        WrapperResult wrapAndSendX(ByteBuffer byteBuffer, boolean z2) throws IOException {
            SSLEngineResult.Status status;
            if (this.closed && !z2) {
                throw new IOException("Engine is closed");
            }
            WrapperResult wrapperResult = SSLStreams.this.new WrapperResult();
            synchronized (this.wrapLock) {
                this.wrap_dst.clear();
                do {
                    wrapperResult.result = this.engine.wrap(byteBuffer, this.wrap_dst);
                    status = wrapperResult.result.getStatus();
                    if (status == SSLEngineResult.Status.BUFFER_OVERFLOW) {
                        this.wrap_dst = SSLStreams.this.realloc(this.wrap_dst, true, BufType.PACKET);
                    }
                } while (status == SSLEngineResult.Status.BUFFER_OVERFLOW);
                if (status == SSLEngineResult.Status.CLOSED && !z2) {
                    this.closed = true;
                    return wrapperResult;
                }
                if (wrapperResult.result.bytesProduced() > 0) {
                    this.wrap_dst.flip();
                    int iRemaining = this.wrap_dst.remaining();
                    if (!$assertionsDisabled && iRemaining != wrapperResult.result.bytesProduced()) {
                        throw new AssertionError();
                    }
                    while (iRemaining > 0) {
                        iRemaining -= this.chan.write(this.wrap_dst);
                    }
                }
                return wrapperResult;
            }
        }

        WrapperResult recvAndUnwrap(ByteBuffer byteBuffer) throws IOException {
            boolean z2;
            int i2;
            SSLEngineResult.Status status;
            SSLEngineResult.Status status2 = SSLEngineResult.Status.OK;
            WrapperResult wrapperResult = SSLStreams.this.new WrapperResult();
            wrapperResult.buf = byteBuffer;
            if (this.closed) {
                throw new IOException("Engine is closed");
            }
            if (this.u_remaining > 0) {
                this.unwrap_src.compact();
                this.unwrap_src.flip();
                z2 = false;
            } else {
                this.unwrap_src.clear();
                z2 = true;
            }
            synchronized (this.unwrapLock) {
                do {
                    if (z2) {
                        do {
                            i2 = this.chan.read(this.unwrap_src);
                        } while (i2 == 0);
                        if (i2 == -1) {
                            throw new IOException("connection closed for reading");
                        }
                        this.unwrap_src.flip();
                    }
                    wrapperResult.result = this.engine.unwrap(this.unwrap_src, wrapperResult.buf);
                    status = wrapperResult.result.getStatus();
                    if (status == SSLEngineResult.Status.BUFFER_UNDERFLOW) {
                        if (this.unwrap_src.limit() == this.unwrap_src.capacity()) {
                            this.unwrap_src = SSLStreams.this.realloc(this.unwrap_src, false, BufType.PACKET);
                        } else {
                            this.unwrap_src.position(this.unwrap_src.limit());
                            this.unwrap_src.limit(this.unwrap_src.capacity());
                        }
                        z2 = true;
                    } else if (status == SSLEngineResult.Status.BUFFER_OVERFLOW) {
                        wrapperResult.buf = SSLStreams.this.realloc(wrapperResult.buf, true, BufType.APPLICATION);
                        z2 = false;
                    } else if (status == SSLEngineResult.Status.CLOSED) {
                        this.closed = true;
                        wrapperResult.buf.flip();
                        return wrapperResult;
                    }
                } while (status != SSLEngineResult.Status.OK);
                this.u_remaining = this.unwrap_src.remaining();
                return wrapperResult;
            }
        }
    }

    public WrapperResult sendData(ByteBuffer byteBuffer) throws IOException {
        WrapperResult wrapperResultWrapAndSend = null;
        while (byteBuffer.remaining() > 0) {
            wrapperResultWrapAndSend = this.wrapper.wrapAndSend(byteBuffer);
            if (wrapperResultWrapAndSend.result.getStatus() == SSLEngineResult.Status.CLOSED) {
                doClosure();
                return wrapperResultWrapAndSend;
            }
            SSLEngineResult.HandshakeStatus handshakeStatus = wrapperResultWrapAndSend.result.getHandshakeStatus();
            if (handshakeStatus != SSLEngineResult.HandshakeStatus.FINISHED && handshakeStatus != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
                doHandshake(handshakeStatus);
            }
        }
        return wrapperResultWrapAndSend;
    }

    public WrapperResult recvData(ByteBuffer byteBuffer) throws IOException {
        WrapperResult wrapperResultRecvAndUnwrap = null;
        if (!$assertionsDisabled && byteBuffer.position() != 0) {
            throw new AssertionError();
        }
        while (byteBuffer.position() == 0) {
            wrapperResultRecvAndUnwrap = this.wrapper.recvAndUnwrap(byteBuffer);
            byteBuffer = wrapperResultRecvAndUnwrap.buf != byteBuffer ? wrapperResultRecvAndUnwrap.buf : byteBuffer;
            if (wrapperResultRecvAndUnwrap.result.getStatus() == SSLEngineResult.Status.CLOSED) {
                doClosure();
                return wrapperResultRecvAndUnwrap;
            }
            SSLEngineResult.HandshakeStatus handshakeStatus = wrapperResultRecvAndUnwrap.result.getHandshakeStatus();
            if (handshakeStatus != SSLEngineResult.HandshakeStatus.FINISHED && handshakeStatus != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
                doHandshake(handshakeStatus);
            }
        }
        byteBuffer.flip();
        return wrapperResultRecvAndUnwrap;
    }

    void doClosure() throws IOException {
        try {
            this.handshaking.lock();
            ByteBuffer byteBufferAllocate = allocate(BufType.APPLICATION);
            while (true) {
                byteBufferAllocate.clear();
                byteBufferAllocate.flip();
                WrapperResult wrapperResultWrapAndSendX = this.wrapper.wrapAndSendX(byteBufferAllocate, true);
                SSLEngineResult.HandshakeStatus handshakeStatus = wrapperResultWrapAndSendX.result.getHandshakeStatus();
                SSLEngineResult.Status status = wrapperResultWrapAndSendX.result.getStatus();
                if (status == SSLEngineResult.Status.CLOSED || (status == SSLEngineResult.Status.OK && handshakeStatus == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING)) {
                    break;
                }
            }
        } finally {
            this.handshaking.unlock();
        }
    }

    void doHandshake(SSLEngineResult.HandshakeStatus handshakeStatus) throws IOException {
        try {
            this.handshaking.lock();
            ByteBuffer byteBufferAllocate = allocate(BufType.APPLICATION);
            while (handshakeStatus != SSLEngineResult.HandshakeStatus.FINISHED && handshakeStatus != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {
                WrapperResult wrapperResultRecvAndUnwrap = null;
                switch (handshakeStatus) {
                    case NEED_TASK:
                        while (true) {
                            Runnable delegatedTask = this.engine.getDelegatedTask();
                            if (delegatedTask != null) {
                                delegatedTask.run();
                            } else {
                                byteBufferAllocate.clear();
                                byteBufferAllocate.flip();
                                wrapperResultRecvAndUnwrap = this.wrapper.wrapAndSend(byteBufferAllocate);
                                handshakeStatus = wrapperResultRecvAndUnwrap.result.getHandshakeStatus();
                            }
                        }
                    case NEED_WRAP:
                        byteBufferAllocate.clear();
                        byteBufferAllocate.flip();
                        wrapperResultRecvAndUnwrap = this.wrapper.wrapAndSend(byteBufferAllocate);
                        handshakeStatus = wrapperResultRecvAndUnwrap.result.getHandshakeStatus();
                    case NEED_UNWRAP:
                        byteBufferAllocate.clear();
                        wrapperResultRecvAndUnwrap = this.wrapper.recvAndUnwrap(byteBufferAllocate);
                        if (wrapperResultRecvAndUnwrap.buf != byteBufferAllocate) {
                            byteBufferAllocate = wrapperResultRecvAndUnwrap.buf;
                        }
                        if (!$assertionsDisabled && byteBufferAllocate.position() != 0) {
                            throw new AssertionError();
                        }
                        handshakeStatus = wrapperResultRecvAndUnwrap.result.getHandshakeStatus();
                        break;
                    default:
                        handshakeStatus = wrapperResultRecvAndUnwrap.result.getHandshakeStatus();
                }
            }
        } finally {
            this.handshaking.unlock();
        }
    }

    /* loaded from: rt.jar:sun/net/httpserver/SSLStreams$InputStream.class */
    class InputStream extends java.io.InputStream {
        ByteBuffer bbuf;
        boolean closed = false;
        boolean eof = false;
        boolean needData = true;
        byte[] single = new byte[1];

        InputStream() {
            this.bbuf = SSLStreams.this.allocate(BufType.APPLICATION);
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            if (this.closed) {
                throw new IOException("SSL stream is closed");
            }
            if (this.eof) {
                return -1;
            }
            int iRemaining = 0;
            if (!this.needData) {
                iRemaining = this.bbuf.remaining();
                this.needData = iRemaining == 0;
            }
            if (this.needData) {
                this.bbuf.clear();
                WrapperResult wrapperResultRecvData = SSLStreams.this.recvData(this.bbuf);
                this.bbuf = wrapperResultRecvData.buf == this.bbuf ? this.bbuf : wrapperResultRecvData.buf;
                int iRemaining2 = this.bbuf.remaining();
                iRemaining = iRemaining2;
                if (iRemaining2 == 0) {
                    this.eof = true;
                    return -1;
                }
                this.needData = false;
            }
            if (i3 > iRemaining) {
                i3 = iRemaining;
            }
            this.bbuf.get(bArr, i2, i3);
            return i3;
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            return this.bbuf.remaining();
        }

        @Override // java.io.InputStream
        public boolean markSupported() {
            return false;
        }

        @Override // java.io.InputStream
        public void reset() throws IOException {
            throw new IOException("mark/reset not supported");
        }

        @Override // java.io.InputStream
        public long skip(long j2) throws IOException {
            int iRemaining = (int) j2;
            if (this.closed) {
                throw new IOException("SSL stream is closed");
            }
            if (this.eof) {
                return 0L;
            }
            while (iRemaining > 0) {
                if (this.bbuf.remaining() >= iRemaining) {
                    this.bbuf.position(this.bbuf.position() + iRemaining);
                    return iRemaining;
                }
                iRemaining -= this.bbuf.remaining();
                this.bbuf.clear();
                WrapperResult wrapperResultRecvData = SSLStreams.this.recvData(this.bbuf);
                this.bbuf = wrapperResultRecvData.buf == this.bbuf ? this.bbuf : wrapperResultRecvData.buf;
            }
            return iRemaining;
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.eof = true;
            SSLStreams.this.engine.closeInbound();
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr) throws IOException {
            return read(bArr, 0, bArr.length);
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            if (this.eof || read(this.single, 0, 1) <= 0) {
                return -1;
            }
            return this.single[0] & 255;
        }
    }

    /* loaded from: rt.jar:sun/net/httpserver/SSLStreams$OutputStream.class */
    class OutputStream extends java.io.OutputStream {
        ByteBuffer buf;
        boolean closed = false;
        byte[] single = new byte[1];
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !SSLStreams.class.desiredAssertionStatus();
        }

        OutputStream() {
            this.buf = SSLStreams.this.allocate(BufType.APPLICATION);
        }

        @Override // java.io.OutputStream
        public void write(int i2) throws IOException {
            this.single[0] = (byte) i2;
            write(this.single, 0, 1);
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr) throws IOException {
            write(bArr, 0, bArr.length);
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr, int i2, int i3) throws IOException {
            if (this.closed) {
                throw new IOException("output stream is closed");
            }
            while (i3 > 0) {
                int iCapacity = i3 > this.buf.capacity() ? this.buf.capacity() : i3;
                this.buf.clear();
                this.buf.put(bArr, i2, iCapacity);
                i3 -= iCapacity;
                i2 += iCapacity;
                this.buf.flip();
                if (SSLStreams.this.sendData(this.buf).result.getStatus() == SSLEngineResult.Status.CLOSED) {
                    this.closed = true;
                    if (i3 > 0) {
                        throw new IOException("output stream is closed");
                    }
                }
            }
        }

        @Override // java.io.OutputStream, java.io.Flushable
        public void flush() throws IOException {
        }

        @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            WrapperResult wrapperResultWrapAndSend = null;
            SSLStreams.this.engine.closeOutbound();
            this.closed = true;
            SSLEngineResult.HandshakeStatus handshakeStatus = SSLEngineResult.HandshakeStatus.NEED_WRAP;
            this.buf.clear();
            while (handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_WRAP) {
                wrapperResultWrapAndSend = SSLStreams.this.wrapper.wrapAndSend(this.buf);
                handshakeStatus = wrapperResultWrapAndSend.result.getHandshakeStatus();
            }
            if (!$assertionsDisabled && wrapperResultWrapAndSend.result.getStatus() != SSLEngineResult.Status.CLOSED) {
                throw new AssertionError();
            }
        }
    }
}
