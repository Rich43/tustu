package sun.net.www.http;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.net.ProgressSource;
import sun.net.www.MeteredStream;

/* loaded from: rt.jar:sun/net/www/http/KeepAliveStream.class */
public class KeepAliveStream extends MeteredStream implements Hurryable {
    HttpClient hc;
    boolean hurried;
    protected boolean queuedForCleanup;
    private static final KeepAliveStreamCleaner queue = new KeepAliveStreamCleaner();
    private static Thread cleanerThread;

    public KeepAliveStream(InputStream inputStream, ProgressSource progressSource, long j2, HttpClient httpClient) {
        super(inputStream, progressSource, j2);
        this.queuedForCleanup = false;
        this.hc = httpClient;
    }

    @Override // sun.net.www.MeteredStream, java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        long j2;
        if (this.closed || this.queuedForCleanup) {
            return;
        }
        try {
            if (this.expected > this.count) {
                if (this.expected - this.count <= available()) {
                    do {
                        j2 = this.expected - this.count;
                        if (j2 <= 0) {
                            break;
                        }
                    } while (skip(Math.min(j2, available())) > 0);
                } else if (this.expected <= KeepAliveStreamCleaner.MAX_DATA_REMAINING && !this.hurried) {
                    queueForCleanup(new KeepAliveCleanerEntry(this, this.hc));
                } else {
                    this.hc.closeServer();
                }
            }
            if (!this.closed && !this.hurried && !this.queuedForCleanup) {
                this.hc.finished();
            }
        } finally {
            if (this.pi != null) {
                this.pi.finishTracking();
            }
            if (!this.queuedForCleanup) {
                this.in = null;
                this.hc = null;
                this.closed = true;
            }
        }
    }

    @Override // sun.net.www.MeteredStream, java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // sun.net.www.MeteredStream, java.io.FilterInputStream, java.io.InputStream
    public void mark(int i2) {
    }

    @Override // sun.net.www.MeteredStream, java.io.FilterInputStream, java.io.InputStream
    public void reset() throws IOException {
        throw new IOException("mark/reset not supported");
    }

    @Override // sun.net.www.http.Hurryable
    public synchronized boolean hurry() {
        try {
            if (this.closed || this.count >= this.expected || this.in.available() < this.expected - this.count) {
                return false;
            }
            byte[] bArr = new byte[(int) (this.expected - this.count)];
            new DataInputStream(this.in).readFully(bArr);
            this.in = new ByteArrayInputStream(bArr);
            this.hurried = true;
            return true;
        } catch (IOException e2) {
            return false;
        }
    }

    private static void queueForCleanup(KeepAliveCleanerEntry keepAliveCleanerEntry) {
        synchronized (queue) {
            if (!keepAliveCleanerEntry.getQueuedForCleanup()) {
                if (!queue.offer(keepAliveCleanerEntry)) {
                    keepAliveCleanerEntry.getHttpClient().closeServer();
                    return;
                } else {
                    keepAliveCleanerEntry.setQueuedForCleanup();
                    queue.notifyAll();
                }
            }
            boolean z2 = cleanerThread == null;
            if (!z2 && !cleanerThread.isAlive()) {
                z2 = true;
            }
            if (z2) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.net.www.http.KeepAliveStream.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    public Void run() {
                        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
                        while (true) {
                            ThreadGroup parent = threadGroup.getParent();
                            if (parent == null) {
                                Thread unused = KeepAliveStream.cleanerThread = new Thread(threadGroup, KeepAliveStream.queue, "Keep-Alive-SocketCleaner");
                                KeepAliveStream.cleanerThread.setDaemon(true);
                                KeepAliveStream.cleanerThread.setPriority(8);
                                KeepAliveStream.cleanerThread.setContextClassLoader(null);
                                KeepAliveStream.cleanerThread.start();
                                return null;
                            }
                            threadGroup = parent;
                        }
                    }
                });
            }
        }
    }

    protected long remainingToRead() {
        return this.expected - this.count;
    }

    protected void setClosed() {
        this.in = null;
        this.hc = null;
        this.closed = true;
    }
}
