package sun.nio.fs;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/* loaded from: rt.jar:sun/nio/fs/AbstractWatchService.class */
abstract class AbstractWatchService implements WatchService {
    private volatile boolean closed;
    private final LinkedBlockingDeque<WatchKey> pendingKeys = new LinkedBlockingDeque<>();
    private final WatchKey CLOSE_KEY = new AbstractWatchKey(null, null) { // from class: sun.nio.fs.AbstractWatchService.1
        @Override // java.nio.file.WatchKey
        public boolean isValid() {
            return true;
        }

        @Override // java.nio.file.WatchKey
        public void cancel() {
        }
    };
    private final Object closeLock = new Object();

    abstract WatchKey register(Path path, WatchEvent.Kind<?>[] kindArr, WatchEvent.Modifier... modifierArr) throws IOException;

    abstract void implClose() throws IOException;

    protected AbstractWatchService() {
    }

    final void enqueueKey(WatchKey watchKey) {
        this.pendingKeys.offer(watchKey);
    }

    private void checkOpen() {
        if (this.closed) {
            throw new ClosedWatchServiceException();
        }
    }

    private void checkKey(WatchKey watchKey) {
        if (watchKey == this.CLOSE_KEY) {
            enqueueKey(watchKey);
        }
        checkOpen();
    }

    @Override // java.nio.file.WatchService
    public final WatchKey poll() {
        checkOpen();
        WatchKey watchKeyPoll = this.pendingKeys.poll();
        checkKey(watchKeyPoll);
        return watchKeyPoll;
    }

    @Override // java.nio.file.WatchService
    public final WatchKey poll(long j2, TimeUnit timeUnit) throws InterruptedException {
        checkOpen();
        WatchKey watchKeyPoll2 = this.pendingKeys.poll2(j2, timeUnit);
        checkKey(watchKeyPoll2);
        return watchKeyPoll2;
    }

    @Override // java.nio.file.WatchService
    public final WatchKey take() throws InterruptedException {
        checkOpen();
        WatchKey watchKeyTake2 = this.pendingKeys.take2();
        checkKey(watchKeyTake2);
        return watchKeyTake2;
    }

    final boolean isOpen() {
        return !this.closed;
    }

    final Object closeLock() {
        return this.closeLock;
    }

    @Override // java.nio.file.WatchService, java.io.Closeable, java.lang.AutoCloseable
    public final void close() throws IOException {
        synchronized (this.closeLock) {
            if (this.closed) {
                return;
            }
            this.closed = true;
            implClose();
            this.pendingKeys.clear();
            this.pendingKeys.offer(this.CLOSE_KEY);
        }
    }
}
