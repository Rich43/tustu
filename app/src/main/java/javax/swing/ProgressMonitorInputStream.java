package javax.swing;

import java.awt.Component;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;

/* loaded from: rt.jar:javax/swing/ProgressMonitorInputStream.class */
public class ProgressMonitorInputStream extends FilterInputStream {
    private ProgressMonitor monitor;
    private int nread;
    private int size;

    public ProgressMonitorInputStream(Component component, Object obj, InputStream inputStream) {
        super(inputStream);
        this.nread = 0;
        this.size = 0;
        try {
            this.size = inputStream.available();
        } catch (IOException e2) {
            this.size = 0;
        }
        this.monitor = new ProgressMonitor(component, obj, null, 0, this.size);
    }

    public ProgressMonitor getProgressMonitor() {
        return this.monitor;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int i2 = this.in.read();
        if (i2 >= 0) {
            ProgressMonitor progressMonitor = this.monitor;
            int i3 = this.nread + 1;
            this.nread = i3;
            progressMonitor.setProgress(i3);
        }
        if (this.monitor.isCanceled()) {
            InterruptedIOException interruptedIOException = new InterruptedIOException("progress");
            interruptedIOException.bytesTransferred = this.nread;
            throw interruptedIOException;
        }
        return i2;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr) throws IOException {
        int i2 = this.in.read(bArr);
        if (i2 > 0) {
            ProgressMonitor progressMonitor = this.monitor;
            int i3 = this.nread + i2;
            this.nread = i3;
            progressMonitor.setProgress(i3);
        }
        if (this.monitor.isCanceled()) {
            InterruptedIOException interruptedIOException = new InterruptedIOException("progress");
            interruptedIOException.bytesTransferred = this.nread;
            throw interruptedIOException;
        }
        return i2;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        int i4 = this.in.read(bArr, i2, i3);
        if (i4 > 0) {
            ProgressMonitor progressMonitor = this.monitor;
            int i5 = this.nread + i4;
            this.nread = i5;
            progressMonitor.setProgress(i5);
        }
        if (this.monitor.isCanceled()) {
            InterruptedIOException interruptedIOException = new InterruptedIOException("progress");
            interruptedIOException.bytesTransferred = this.nread;
            throw interruptedIOException;
        }
        return i4;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long j2) throws IOException {
        long jSkip = this.in.skip(j2);
        if (jSkip > 0) {
            ProgressMonitor progressMonitor = this.monitor;
            int i2 = (int) (this.nread + jSkip);
            this.nread = i2;
            progressMonitor.setProgress(i2);
        }
        return jSkip;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.in.close();
        this.monitor.close();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void reset() throws IOException {
        this.in.reset();
        this.nread = this.size - this.in.available();
        this.monitor.setProgress(this.nread);
    }
}
