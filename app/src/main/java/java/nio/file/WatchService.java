package java.nio.file;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/* loaded from: rt.jar:java/nio/file/WatchService.class */
public interface WatchService extends Closeable {
    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close() throws IOException;

    WatchKey poll();

    WatchKey poll(long j2, TimeUnit timeUnit) throws InterruptedException;

    WatchKey take() throws InterruptedException;
}
