package jdk.management.jfr;

import java.util.TimerTask;

/* loaded from: jfr.jar:jdk/management/jfr/StreamCleanupTask.class */
final class StreamCleanupTask extends TimerTask {
    private final Stream stream;
    private final StreamManager manager;

    StreamCleanupTask(StreamManager streamManager, Stream stream) {
        this.stream = stream;
        this.manager = streamManager;
    }

    @Override // java.util.TimerTask, java.lang.Runnable
    public void run() {
        long lastTouched = this.stream.getLastTouched();
        if (System.currentTimeMillis() - lastTouched >= StreamManager.TIME_OUT) {
            this.manager.destroy(this.stream);
        } else {
            this.manager.scheduleAbort(this.stream, lastTouched + StreamManager.TIME_OUT);
        }
    }
}
