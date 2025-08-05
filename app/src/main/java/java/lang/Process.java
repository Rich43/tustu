package java.lang;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

/* loaded from: rt.jar:java/lang/Process.class */
public abstract class Process {
    public abstract OutputStream getOutputStream();

    public abstract InputStream getInputStream();

    public abstract InputStream getErrorStream();

    public abstract int waitFor() throws InterruptedException;

    public abstract int exitValue();

    public abstract void destroy();

    public boolean waitFor(long j2, TimeUnit timeUnit) throws InterruptedException {
        long jNanoTime = System.nanoTime();
        long nanos = timeUnit.toNanos(j2);
        do {
            try {
                exitValue();
                return true;
            } catch (IllegalThreadStateException e2) {
                if (nanos > 0) {
                    Thread.sleep(Math.min(TimeUnit.NANOSECONDS.toMillis(nanos) + 1, 100L));
                }
                nanos = timeUnit.toNanos(j2) - (System.nanoTime() - jNanoTime);
            }
        } while (nanos > 0);
        return false;
    }

    public Process destroyForcibly() {
        destroy();
        return this;
    }

    public boolean isAlive() {
        try {
            exitValue();
            return false;
        } catch (IllegalThreadStateException e2) {
            return true;
        }
    }
}
