package java.io;

import java.nio.channels.FileChannel;
import sun.nio.ch.FileChannelImpl;

/* loaded from: rt.jar:java/io/FileInputStream.class */
public class FileInputStream extends InputStream {
    private final FileDescriptor fd;
    private final String path;
    private FileChannel channel;
    private final Object closeLock;
    private volatile boolean closed;

    private native void open0(String str) throws FileNotFoundException;

    private native int read0() throws IOException;

    private native int readBytes(byte[] bArr, int i2, int i3) throws IOException;

    private native long skip0(long j2) throws IOException;

    private native int available0() throws IOException;

    private static native void initIDs();

    /* JADX INFO: Access modifiers changed from: private */
    public native void close0() throws IOException;

    public FileInputStream(String str) throws FileNotFoundException {
        this(str != null ? new File(str) : null);
    }

    public FileInputStream(File file) throws FileNotFoundException {
        this.channel = null;
        this.closeLock = new Object();
        this.closed = false;
        String path = file != null ? file.getPath() : null;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(path);
        }
        if (path == null) {
            throw new NullPointerException();
        }
        if (file.isInvalid()) {
            throw new FileNotFoundException("Invalid file path");
        }
        this.fd = new FileDescriptor();
        this.fd.attach(this);
        this.path = path;
        open(path);
    }

    public FileInputStream(FileDescriptor fileDescriptor) {
        this.channel = null;
        this.closeLock = new Object();
        this.closed = false;
        SecurityManager securityManager = System.getSecurityManager();
        if (fileDescriptor == null) {
            throw new NullPointerException();
        }
        if (securityManager != null) {
            securityManager.checkRead(fileDescriptor);
        }
        this.fd = fileDescriptor;
        this.path = null;
        this.fd.attach(this);
    }

    private void open(String str) throws FileNotFoundException {
        open0(str);
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        return read0();
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr) throws IOException {
        return readBytes(bArr, 0, bArr.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        return readBytes(bArr, i2, i3);
    }

    @Override // java.io.InputStream
    public long skip(long j2) throws IOException {
        return skip0(j2);
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return available0();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        synchronized (this.closeLock) {
            if (this.closed) {
                return;
            }
            this.closed = true;
            if (this.channel != null) {
                this.channel.close();
            }
            this.fd.closeAll(new Closeable() { // from class: java.io.FileInputStream.1
                @Override // java.io.Closeable, java.lang.AutoCloseable
                public void close() throws IOException {
                    FileInputStream.this.close0();
                }
            });
        }
    }

    public final FileDescriptor getFD() throws IOException {
        if (this.fd != null) {
            return this.fd;
        }
        throw new IOException();
    }

    public FileChannel getChannel() {
        FileChannel fileChannel;
        synchronized (this) {
            if (this.channel == null) {
                this.channel = FileChannelImpl.open(this.fd, this.path, true, false, this);
            }
            fileChannel = this.channel;
        }
        return fileChannel;
    }

    static {
        initIDs();
    }

    protected void finalize() throws IOException {
        if (this.fd != null && this.fd != FileDescriptor.in) {
            close();
        }
    }
}
