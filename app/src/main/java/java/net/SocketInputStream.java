package java.net;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import sun.net.ConnectionResetException;

/* loaded from: rt.jar:java/net/SocketInputStream.class */
class SocketInputStream extends FileInputStream {
    private boolean eof;
    private AbstractPlainSocketImpl impl;
    private byte[] temp;
    private Socket socket;
    private boolean closing;

    private native int socketRead0(FileDescriptor fileDescriptor, byte[] bArr, int i2, int i3, int i4) throws IOException;

    private static native void init();

    static {
        init();
    }

    SocketInputStream(AbstractPlainSocketImpl abstractPlainSocketImpl) throws IOException {
        super(abstractPlainSocketImpl.getFileDescriptor());
        this.impl = null;
        this.socket = null;
        this.closing = false;
        this.impl = abstractPlainSocketImpl;
        this.socket = abstractPlainSocketImpl.getSocket();
    }

    @Override // java.io.FileInputStream
    public final FileChannel getChannel() {
        return null;
    }

    private int socketRead(FileDescriptor fileDescriptor, byte[] bArr, int i2, int i3, int i4) throws IOException {
        return socketRead0(fileDescriptor, bArr, i2, i3, i4);
    }

    @Override // java.io.FileInputStream, java.io.InputStream
    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    @Override // java.io.FileInputStream, java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        return read(bArr, i2, i3, this.impl.getTimeout());
    }

    int read(byte[] bArr, int i2, int i3, int i4) throws IOException {
        int iSocketRead;
        if (this.eof) {
            return -1;
        }
        if (this.impl.isConnectionReset()) {
            throw new SocketException("Connection reset");
        }
        if (i3 <= 0 || i2 < 0 || i3 > bArr.length - i2) {
            if (i3 == 0) {
                return 0;
            }
            throw new ArrayIndexOutOfBoundsException("length == " + i3 + " off == " + i2 + " buffer length == " + bArr.length);
        }
        boolean z2 = false;
        FileDescriptor fileDescriptorAcquireFD = this.impl.acquireFD();
        try {
            iSocketRead = socketRead(fileDescriptorAcquireFD, bArr, i2, i3, i4);
        } catch (ConnectionResetException e2) {
            z2 = true;
            this.impl.releaseFD();
        } catch (Throwable th) {
            this.impl.releaseFD();
            throw th;
        }
        if (iSocketRead > 0) {
            this.impl.releaseFD();
            return iSocketRead;
        }
        this.impl.releaseFD();
        if (z2) {
            this.impl.setConnectionResetPending();
            this.impl.acquireFD();
            try {
                int iSocketRead2 = socketRead(fileDescriptorAcquireFD, bArr, i2, i3, i4);
                if (iSocketRead2 > 0) {
                    this.impl.releaseFD();
                    return iSocketRead2;
                }
                this.impl.releaseFD();
            } catch (ConnectionResetException e3) {
                this.impl.releaseFD();
            } catch (Throwable th2) {
                this.impl.releaseFD();
                throw th2;
            }
        }
        if (this.impl.isClosedOrPending()) {
            throw new SocketException("Socket closed");
        }
        if (this.impl.isConnectionResetPending()) {
            this.impl.setConnectionReset();
        }
        if (this.impl.isConnectionReset()) {
            throw new SocketException("Connection reset");
        }
        this.eof = true;
        return -1;
    }

    @Override // java.io.FileInputStream, java.io.InputStream
    public int read() throws IOException {
        if (this.eof) {
            return -1;
        }
        this.temp = new byte[1];
        if (read(this.temp, 0, 1) <= 0) {
            return -1;
        }
        return this.temp[0] & 255;
    }

    @Override // java.io.FileInputStream, java.io.InputStream
    public long skip(long j2) throws IOException {
        int i2;
        if (j2 <= 0) {
            return 0L;
        }
        long j3 = j2;
        int iMin = (int) Math.min(1024L, j3);
        byte[] bArr = new byte[iMin];
        while (j3 > 0 && (i2 = read(bArr, 0, (int) Math.min(iMin, j3))) >= 0) {
            j3 -= i2;
        }
        return j2 - j3;
    }

    @Override // java.io.FileInputStream, java.io.InputStream
    public int available() throws IOException {
        return this.impl.available();
    }

    @Override // java.io.FileInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
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

    void setEOF(boolean z2) {
        this.eof = z2;
    }

    @Override // java.io.FileInputStream
    protected void finalize() {
    }
}
