package java.net;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import sun.net.ConnectionResetException;

/* loaded from: rt.jar:java/net/SocketOutputStream.class */
class SocketOutputStream extends FileOutputStream {
    private AbstractPlainSocketImpl impl;
    private byte[] temp;
    private Socket socket;
    private boolean closing;

    private native void socketWrite0(FileDescriptor fileDescriptor, byte[] bArr, int i2, int i3) throws IOException;

    private static native void init();

    static {
        init();
    }

    SocketOutputStream(AbstractPlainSocketImpl abstractPlainSocketImpl) throws IOException {
        super(abstractPlainSocketImpl.getFileDescriptor());
        this.impl = null;
        this.temp = new byte[1];
        this.socket = null;
        this.closing = false;
        this.impl = abstractPlainSocketImpl;
        this.socket = abstractPlainSocketImpl.getSocket();
    }

    @Override // java.io.FileOutputStream
    public final FileChannel getChannel() {
        return null;
    }

    private void socketWrite(byte[] bArr, int i2, int i3) throws IOException {
        if (i3 <= 0 || i2 < 0 || i3 > bArr.length - i2) {
            if (i3 == 0) {
                return;
            } else {
                throw new ArrayIndexOutOfBoundsException("len == " + i3 + " off == " + i2 + " buffer length == " + bArr.length);
            }
        }
        try {
            try {
                socketWrite0(this.impl.acquireFD(), bArr, i2, i3);
                this.impl.releaseFD();
            } catch (SocketException e2) {
                e = e2;
                if (e instanceof ConnectionResetException) {
                    this.impl.setConnectionResetPending();
                    e = new SocketException("Connection reset");
                }
                if (this.impl.isClosedOrPending()) {
                    throw new SocketException("Socket closed");
                }
                throw e;
            }
        } catch (Throwable th) {
            this.impl.releaseFD();
            throw th;
        }
    }

    @Override // java.io.FileOutputStream, java.io.OutputStream
    public void write(int i2) throws IOException {
        this.temp[0] = (byte) i2;
        socketWrite(this.temp, 0, 1);
    }

    @Override // java.io.FileOutputStream, java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        socketWrite(bArr, 0, bArr.length);
    }

    @Override // java.io.FileOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        socketWrite(bArr, i2, i3);
    }

    @Override // java.io.FileOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.closing) {
            return;
        }
        this.closing = true;
        if (this.socket != null) {
            if (!this.socket.isClosed()) {
                this.socket.close();
            }
        } else {
            this.impl.close();
        }
        this.closing = false;
    }

    @Override // java.io.FileOutputStream
    protected void finalize() {
    }
}
