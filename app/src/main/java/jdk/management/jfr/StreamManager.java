package jdk.management.jfr;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

/* loaded from: jfr.jar:jdk/management/jfr/StreamManager.class */
final class StreamManager {
    public static final int DEFAULT_BLOCK_SIZE = 50000;
    private final Map<Long, Stream> streams = new HashMap();
    private Timer timer;
    public static final long TIME_OUT = TimeUnit.MINUTES.toMillis(2);
    private static long idCounter = 0;

    StreamManager() {
    }

    public synchronized Stream getStream(long j2) {
        Stream stream = this.streams.get(Long.valueOf(j2));
        if (stream == null) {
            throw new IllegalArgumentException("Unknown stream identifier " + j2);
        }
        return stream;
    }

    public synchronized Stream create(InputStream inputStream, int i2) {
        idCounter++;
        Stream stream = new Stream(inputStream, idCounter, i2);
        this.streams.put(Long.valueOf(stream.getId()), stream);
        scheduleAbort(stream, System.currentTimeMillis() + TIME_OUT);
        return stream;
    }

    public synchronized void destroy(Stream stream) {
        try {
            stream.close();
        } catch (IOException e2) {
        }
        this.streams.remove(Long.valueOf(stream.getId()));
        if (this.streams.isEmpty()) {
            this.timer.cancel();
            this.timer = null;
        }
    }

    public synchronized void scheduleAbort(Stream stream, long j2) {
        if (this.timer == null) {
            this.timer = new Timer(true);
        }
        this.timer.schedule(new StreamCleanupTask(this, stream), new Date(j2 + TIME_OUT));
    }
}
