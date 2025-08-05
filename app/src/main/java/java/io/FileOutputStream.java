package java.io;

import java.nio.channels.FileChannel;
import sun.nio.ch.FileChannelImpl;

/* loaded from: rt.jar:java/io/FileOutputStream.class */
public class FileOutputStream extends OutputStream {
    private final FileDescriptor fd;
    private final boolean append;
    private FileChannel channel;
    private final String path;
    private final Object closeLock;
    private volatile boolean closed;

    private native void open0(String str, boolean z2) throws FileNotFoundException;

    private native void write(int i2, boolean z2) throws IOException;

    private native void writeBytes(byte[] bArr, int i2, int i3, boolean z2) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public native void close0() throws IOException;

    private static native void initIDs();

    public FileOutputStream(String str) throws FileNotFoundException {
        this(str != null ? new File(str) : null, false);
    }

    public FileOutputStream(String str, boolean z2) throws FileNotFoundException {
        this(str != null ? new File(str) : null, z2);
    }

    public FileOutputStream(File file) throws FileNotFoundException {
        this(file, false);
    }

    public FileOutputStream(File file, boolean z2) throws FileNotFoundException {
        this.closeLock = new Object();
        this.closed = false;
        String path = file != null ? file.getPath() : null;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(path);
        }
        if (path == null) {
            throw new NullPointerException();
        }
        if (file.isInvalid()) {
            throw new FileNotFoundException("Invalid file path");
        }
        this.fd = new FileDescriptor();
        this.fd.attach(this);
        this.append = z2;
        this.path = path;
        open(path, z2);
    }

    public FileOutputStream(FileDescriptor fileDescriptor) {
        this.closeLock = new Object();
        this.closed = false;
        SecurityManager securityManager = System.getSecurityManager();
        if (fileDescriptor == null) {
            throw new NullPointerException();
        }
        if (securityManager != null) {
            securityManager.checkWrite(fileDescriptor);
        }
        this.fd = fileDescriptor;
        this.append = false;
        this.path = null;
        this.fd.attach(this);
    }

    private void open(String str, boolean z2) throws FileNotFoundException {
        open0(str, z2);
    }

    @Override // java.io.OutputStream
    public void write(int i2) throws IOException {
        write(i2, this.append);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        writeBytes(bArr, 0, bArr.length, this.append);
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        writeBytes(bArr, i2, i3, this.append);
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        synchronized (this.closeLock) {
            if (this.closed) {
                return;
            }
            this.closed = true;
            if (this.channel != null) {
                this.channel.close();
            }
            this.fd.closeAll(new Closeable() { // from class: java.io.FileOutputStream.1
                @Override // java.io.Closeable, java.lang.AutoCloseable
                public void close() throws IOException {
                    FileOutputStream.this.close0();
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
                this.channel = FileChannelImpl.open(this.fd, this.path, false, true, this.append, this);
            }
            fileChannel = this.channel;
        }
        return fileChannel;
    }

    protected void finalize() throws IOException {
        if (this.fd != null) {
            if (this.fd == FileDescriptor.out || this.fd == FileDescriptor.err) {
                flush();
            } else {
                close();
            }
        }
    }

    static {
        initIDs();
    }
}
