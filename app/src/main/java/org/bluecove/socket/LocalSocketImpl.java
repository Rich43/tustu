package org.bluecove.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.UnknownServiceException;

/* loaded from: bluecove-bluez-2.1.1.jar:org/bluecove/socket/LocalSocketImpl.class */
class LocalSocketImpl extends SocketImpl {
    private int socket = -1;
    private boolean bound;
    private boolean connected;
    private boolean closed;
    private SocketAddress endpoint;
    private InputStream in;
    private OutputStream out;

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluecove/socket/LocalSocketImpl$LocalSocketOptions.class */
    public interface LocalSocketOptions {
        public static final int SO_LINGER = 1;
        public static final int SO_PASSCRED = 2;
        public static final int SO_SNDBUF = 3;
        public static final int SO_RCVBUF = 4;
        public static final int SO_RCVTIMEO = 5;
        public static final int SO_SNDTIMEO = 6;
    }

    private native int nativeCreate(boolean z2) throws IOException;

    private native void nativeConnect(int i2, String str, boolean z2, int i3);

    private native void nativeBind(int i2, String str, boolean z2);

    private native void nativeListen(int i2, int i3);

    private native int nativeAccept(int i2) throws IOException;

    private native void nativeClose(int i2) throws IOException;

    private native void nativeShutdown(int i2, boolean z2) throws IOException;

    private native void nativeUnlink(String str);

    /* JADX INFO: Access modifiers changed from: private */
    public native int nativeAvailable(int i2) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public native int nativeRead(int i2, byte[] bArr, int i3, int i4) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public native void nativeWrite(int i2, byte[] bArr, int i3, int i4) throws IOException;

    private native void nativeReadCredentials(int i2, int[] iArr) throws IOException;

    private static native void nativeReadProcessCredentials(int[] iArr);

    private native void nativeSetOption(int i2, int i3, int i4) throws SocketException;

    private native int nativeGetOption(int i2, int i3) throws SocketException;

    LocalSocketImpl() {
    }

    @Override // java.net.SocketImpl
    protected void accept(SocketImpl s2) throws IOException {
        if (!(s2 instanceof LocalSocketImpl)) {
            throw new UnknownServiceException();
        }
        ((LocalSocketImpl) s2).socket = nativeAccept(this.socket);
        ((LocalSocketImpl) s2).connected = true;
        ((LocalSocketImpl) s2).endpoint = this.endpoint;
    }

    @Override // java.net.SocketImpl
    protected int available() throws IOException {
        return nativeAvailable(this.socket);
    }

    @Override // java.net.SocketImpl
    protected void bind(InetAddress host, int port) throws IOException {
        throw new UnknownServiceException();
    }

    protected void bind(SocketAddress endpoint) throws IOException {
        if (!(endpoint instanceof LocalSocketAddress)) {
            throw new IllegalArgumentException("Unsupported address type");
        }
        nativeBind(this.socket, ((LocalSocketAddress) endpoint).getName(), ((LocalSocketAddress) endpoint).isAbstractNamespace());
        this.bound = true;
        this.endpoint = endpoint;
    }

    @Override // java.net.SocketImpl
    protected void listen(int backlog) throws IOException {
        nativeListen(this.socket, backlog);
    }

    @Override // java.net.SocketImpl
    protected void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            nativeClose(this.socket);
        }
        this.bound = false;
        this.endpoint = null;
    }

    void unlink(String path) {
        nativeUnlink(path);
    }

    @Override // java.net.SocketImpl
    protected void connect(String host, int port) throws IOException {
        throw new UnknownServiceException();
    }

    @Override // java.net.SocketImpl
    protected void connect(InetAddress address, int port) throws IOException {
        throw new UnknownServiceException();
    }

    @Override // java.net.SocketImpl
    protected void connect(SocketAddress address, int timeout) throws IOException {
        if (!(address instanceof LocalSocketAddress)) {
            throw new IllegalArgumentException("Unsupported address type");
        }
        nativeConnect(this.socket, ((LocalSocketAddress) address).getName(), ((LocalSocketAddress) address).isAbstractNamespace(), timeout);
        this.connected = true;
        this.bound = true;
    }

    public SocketAddress getSocketAddress() {
        return this.endpoint;
    }

    @Override // java.net.SocketImpl
    protected void create(boolean stream) throws IOException {
        this.socket = nativeCreate(stream);
    }

    @Override // java.net.SocketImpl
    protected InputStream getInputStream() throws IOException {
        if (this.in == null) {
            this.in = new LocalSocketInputStream();
        }
        return this.in;
    }

    @Override // java.net.SocketImpl
    protected OutputStream getOutputStream() throws IOException {
        if (this.out == null) {
            this.out = new LocalSocketOutputStream();
        }
        return this.out;
    }

    @Override // java.net.SocketImpl
    protected void shutdownInput() throws IOException {
        nativeShutdown(this.socket, true);
    }

    @Override // java.net.SocketImpl
    protected void shutdownOutput() throws IOException {
        nativeShutdown(this.socket, false);
    }

    @Override // java.net.SocketImpl
    protected void sendUrgentData(int data) throws IOException {
    }

    @Override // java.net.SocketOptions
    public Object getOption(int optID) throws SocketException {
        int rc = nativeGetOption(this.socket, optID);
        return Integer.valueOf(rc);
    }

    @Override // java.net.SocketOptions
    public void setOption(int optID, Object value) throws SocketException {
        int nativeValue;
        if (value instanceof Boolean) {
            nativeValue = ((Boolean) value).booleanValue() ? 1 : -1;
        } else if (value instanceof Integer) {
            nativeValue = ((Integer) value).intValue();
        } else {
            throw new IllegalArgumentException();
        }
        nativeSetOption(this.socket, optID, nativeValue);
    }

    protected void finalize() throws IOException {
        if (this.socket > 0) {
            close();
        }
    }

    public boolean isCurrentThreadInterruptedCallback() {
        return Thread.interrupted();
    }

    boolean isClosed() {
        return this.closed;
    }

    boolean isConnected() {
        return this.connected;
    }

    boolean isBound() {
        return this.bound;
    }

    Credentials readPeerCredentials() throws IOException {
        int[] ucred = new int[3];
        nativeReadCredentials(this.socket, ucred);
        return new Credentials(ucred[0], ucred[2], ucred[2]);
    }

    static Credentials readProcessCredentials() {
        int[] ucred = new int[3];
        nativeReadProcessCredentials(ucred);
        return new Credentials(ucred[0], ucred[2], ucred[2]);
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluecove/socket/LocalSocketImpl$LocalSocketInputStream.class */
    private class LocalSocketInputStream extends InputStream {
        private LocalSocketInputStream() {
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            return LocalSocketImpl.this.nativeAvailable(LocalSocketImpl.this.socket);
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            byte[] data = new byte[1];
            int size = LocalSocketImpl.this.nativeRead(LocalSocketImpl.this.socket, data, 0, 1);
            if (size == -1) {
                return -1;
            }
            return 255 & data[0];
        }

        @Override // java.io.InputStream
        public int read(byte[] b2, int off, int len) throws IOException {
            if (off >= 0 && len >= 0 && off + len <= b2.length) {
                return LocalSocketImpl.this.nativeRead(LocalSocketImpl.this.socket, b2, off, len);
            }
            throw new IndexOutOfBoundsException();
        }
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluecove/socket/LocalSocketImpl$LocalSocketOutputStream.class */
    private class LocalSocketOutputStream extends OutputStream {
        private LocalSocketOutputStream() {
        }

        @Override // java.io.OutputStream
        public void write(int b2) throws IOException {
            byte[] buf = {(byte) (b2 & 255)};
            LocalSocketImpl.this.nativeWrite(LocalSocketImpl.this.socket, buf, 0, 1);
        }

        @Override // java.io.OutputStream
        public void write(byte[] buf) throws IOException {
            if (buf != null) {
                LocalSocketImpl.this.nativeWrite(LocalSocketImpl.this.socket, buf, 0, buf.length);
                return;
            }
            throw new NullPointerException();
        }

        @Override // java.io.OutputStream
        public void write(byte[] buf, int off, int len) throws IOException {
            if (off >= 0 && len >= 0 && off + len <= buf.length) {
                LocalSocketImpl.this.nativeWrite(LocalSocketImpl.this.socket, buf, off, len);
                return;
            }
            throw new IndexOutOfBoundsException();
        }
    }
}
