package jdk.jfr.internal;

import jdk.jfr.internal.SecuritySupport;
import sun.misc.Unsafe;

/* loaded from: jfr.jar:jdk/jfr/internal/Options.class */
public final class Options {
    private static final long WAIT_INTERVAL = 1000;
    private static final long MIN_MAX_CHUNKSIZE = 1048576;
    private static final long DEFAULT_GLOBAL_BUFFER_COUNT = 20;
    private static final long DEFAULT_GLOBAL_BUFFER_SIZE = 524288;
    private static final long DEFAULT_MEMORY_SIZE = 10485760;
    private static long DEFAULT_THREAD_BUFFER_SIZE;
    private static final int DEFAULT_STACK_DEPTH = 64;
    private static final boolean DEFAULT_SAMPLE_THREADS = true;
    private static final long DEFAULT_MAX_CHUNK_SIZE = 12582912;
    private static long memorySize;
    private static long globalBufferSize;
    private static long globalBufferCount;
    private static long threadBufferSize;
    private static int stackDepth;
    private static boolean sampleThreads;
    private static long maxChunkSize;
    private static SecuritySupport.SafePath dumpPath;
    private static final JVM jvm = JVM.getJVM();
    private static final SecuritySupport.SafePath DEFAULT_DUMP_PATH = SecuritySupport.USER_HOME;

    static {
        long jPageSize = Unsafe.getUnsafe().pageSize();
        DEFAULT_THREAD_BUFFER_SIZE = jPageSize > 8192 ? jPageSize : 8192L;
        reset();
    }

    public static synchronized void setMaxChunkSize(long j2) {
        if (j2 < 1048576) {
            throw new IllegalArgumentException("Max chunk size must be at least 1048576");
        }
        jvm.setFileNotification(j2);
        maxChunkSize = j2;
    }

    public static synchronized long getMaxChunkSize() {
        return maxChunkSize;
    }

    public static synchronized void setMemorySize(long j2) throws IllegalArgumentException {
        jvm.setMemorySize(j2);
        memorySize = j2;
    }

    public static synchronized long getMemorySize() {
        return memorySize;
    }

    public static synchronized void setThreadBufferSize(long j2) throws IllegalStateException, IllegalArgumentException {
        jvm.setThreadBufferSize(j2);
        threadBufferSize = j2;
    }

    public static synchronized long getThreadBufferSize() {
        return threadBufferSize;
    }

    public static synchronized long getGlobalBufferSize() {
        return globalBufferSize;
    }

    public static synchronized void setGlobalBufferCount(long j2) throws IllegalStateException, IllegalArgumentException {
        jvm.setGlobalBufferCount(j2);
        globalBufferCount = j2;
    }

    public static synchronized long getGlobalBufferCount() {
        return globalBufferCount;
    }

    public static synchronized void setGlobalBufferSize(long j2) throws IllegalArgumentException {
        jvm.setGlobalBufferSize(j2);
        globalBufferSize = j2;
    }

    public static synchronized void setDumpPath(SecuritySupport.SafePath safePath) {
        dumpPath = safePath;
    }

    public static synchronized SecuritySupport.SafePath getDumpPath() {
        return dumpPath;
    }

    public static synchronized void setStackDepth(Integer num) throws IllegalStateException, IllegalArgumentException {
        jvm.setStackDepth(num.intValue());
        stackDepth = num.intValue();
    }

    public static synchronized int getStackDepth() {
        return stackDepth;
    }

    public static synchronized void setSampleThreads(Boolean bool) throws IllegalStateException {
        jvm.setSampleThreads(bool.booleanValue());
        sampleThreads = bool.booleanValue();
    }

    public static synchronized boolean getSampleThreads() {
        return sampleThreads;
    }

    private static synchronized void reset() throws IllegalStateException, IllegalArgumentException {
        setMaxChunkSize(DEFAULT_MAX_CHUNK_SIZE);
        setMemorySize(DEFAULT_MEMORY_SIZE);
        setGlobalBufferSize(524288L);
        setGlobalBufferCount(20L);
        setDumpPath(DEFAULT_DUMP_PATH);
        setSampleThreads(true);
        setStackDepth(64);
        setThreadBufferSize(DEFAULT_THREAD_BUFFER_SIZE);
    }

    static synchronized long getWaitInterval() {
        return 1000L;
    }

    static void ensureInitialized() {
    }
}
