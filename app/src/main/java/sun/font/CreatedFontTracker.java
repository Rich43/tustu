package sun.font;

import java.io.File;
import java.io.OutputStream;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import sun.awt.AppContext;
import sun.misc.ThreadGroupUtils;

/* loaded from: rt.jar:sun/font/CreatedFontTracker.class */
public class CreatedFontTracker {
    public static final int MAX_FILE_SIZE = 33554432;
    public static final int MAX_TOTAL_BYTES = 335544320;
    static CreatedFontTracker tracker;
    int numBytes = 0;

    public static synchronized CreatedFontTracker getTracker() {
        if (tracker == null) {
            tracker = new CreatedFontTracker();
        }
        return tracker;
    }

    private CreatedFontTracker() {
    }

    public synchronized int getNumBytes() {
        return this.numBytes;
    }

    public synchronized void addBytes(int i2) {
        this.numBytes += i2;
    }

    public synchronized void subBytes(int i2) {
        this.numBytes -= i2;
    }

    private static synchronized Semaphore getCS() {
        AppContext appContext = AppContext.getAppContext();
        Semaphore semaphore = (Semaphore) appContext.get(CreatedFontTracker.class);
        if (semaphore == null) {
            semaphore = new Semaphore(5, true);
            appContext.put(CreatedFontTracker.class, semaphore);
        }
        return semaphore;
    }

    public boolean acquirePermit() throws InterruptedException {
        return getCS().tryAcquire(120L, TimeUnit.SECONDS);
    }

    public void releasePermit() {
        getCS().release();
    }

    public void add(File file) {
        TempFileDeletionHook.add(file);
    }

    public void set(File file, OutputStream outputStream) {
        TempFileDeletionHook.set(file, outputStream);
    }

    public void remove(File file) {
        TempFileDeletionHook.remove(file);
    }

    /* loaded from: rt.jar:sun/font/CreatedFontTracker$TempFileDeletionHook.class */
    private static class TempFileDeletionHook {
        private static HashMap<File, OutputStream> files = new HashMap<>();

        /* renamed from: t, reason: collision with root package name */
        private static Thread f13550t = null;

        static void init() {
            if (f13550t == null) {
                AccessController.doPrivileged(() -> {
                    f13550t = new Thread(ThreadGroupUtils.getRootThreadGroup(), TempFileDeletionHook::runHooks);
                    f13550t.setContextClassLoader(null);
                    Runtime.getRuntime().addShutdownHook(f13550t);
                    return null;
                });
            }
        }

        private TempFileDeletionHook() {
        }

        static synchronized void add(File file) {
            init();
            files.put(file, null);
        }

        static synchronized void set(File file, OutputStream outputStream) {
            files.put(file, outputStream);
        }

        static synchronized void remove(File file) {
            files.remove(file);
        }

        static synchronized void runHooks() {
            if (files.isEmpty()) {
                return;
            }
            for (Map.Entry<File, OutputStream> entry : files.entrySet()) {
                try {
                    if (entry.getValue() != null) {
                        entry.getValue().close();
                    }
                } catch (Exception e2) {
                }
                entry.getKey().delete();
            }
        }
    }
}
