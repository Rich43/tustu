package java.lang;

import java.lang.ThreadLocal;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import jdk.internal.misc.TerminatingThreadLocal;
import sun.misc.Contended;
import sun.misc.VM;
import sun.nio.ch.Interruptible;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/lang/Thread.class */
public class Thread implements Runnable {
    private volatile String name;
    private int priority;
    private Thread threadQ;
    private long eetop;
    private boolean single_step;
    private Runnable target;
    private ThreadGroup group;
    private ClassLoader contextClassLoader;
    private AccessControlContext inheritedAccessControlContext;
    private static int threadInitNumber;
    private long stackSize;
    private long nativeParkEventPointer;
    private long tid;
    private static long threadSeqNumber;
    volatile Object parkBlocker;
    private volatile Interruptible blocker;
    public static final int MIN_PRIORITY = 1;
    public static final int NORM_PRIORITY = 5;
    public static final int MAX_PRIORITY = 10;
    private static final StackTraceElement[] EMPTY_STACK_TRACE;
    private static final RuntimePermission SUBCLASS_IMPLEMENTATION_PERMISSION;
    private volatile UncaughtExceptionHandler uncaughtExceptionHandler;
    private static volatile UncaughtExceptionHandler defaultUncaughtExceptionHandler;

    @Contended("tlr")
    long threadLocalRandomSeed;

    @Contended("tlr")
    int threadLocalRandomProbe;

    @Contended("tlr")
    int threadLocalRandomSecondarySeed;
    private boolean daemon = false;
    private boolean stillborn = false;
    ThreadLocal.ThreadLocalMap threadLocals = null;
    ThreadLocal.ThreadLocalMap inheritableThreadLocals = null;
    private volatile int threadStatus = 0;
    private final Object blockerLock = new Object();

    /* loaded from: rt.jar:java/lang/Thread$State.class */
    public enum State {
        NEW,
        RUNNABLE,
        BLOCKED,
        WAITING,
        TIMED_WAITING,
        TERMINATED
    }

    @FunctionalInterface
    /* loaded from: rt.jar:java/lang/Thread$UncaughtExceptionHandler.class */
    public interface UncaughtExceptionHandler {
        void uncaughtException(Thread thread, Throwable th);
    }

    private static native void registerNatives();

    public static native Thread currentThread();

    public static native void yield();

    public static native void sleep(long j2) throws InterruptedException;

    private native void start0();

    private native boolean isInterrupted(boolean z2);

    public final native boolean isAlive();

    @Deprecated
    public native int countStackFrames();

    public static native boolean holdsLock(Object obj);

    private static native StackTraceElement[][] dumpThreads(Thread[] threadArr);

    private static native Thread[] getThreads();

    private native void setPriority0(int i2);

    private native void stop0(Object obj);

    private native void suspend0();

    private native void resume0();

    private native void interrupt0();

    private native void setNativeName(String str);

    static {
        registerNatives();
        EMPTY_STACK_TRACE = new StackTraceElement[0];
        SUBCLASS_IMPLEMENTATION_PERMISSION = new RuntimePermission("enableContextClassLoaderOverride");
    }

    private static synchronized int nextThreadNum() {
        int i2 = threadInitNumber;
        threadInitNumber = i2 + 1;
        return i2;
    }

    private static synchronized long nextThreadID() {
        long j2 = threadSeqNumber + 1;
        threadSeqNumber = j2;
        return j2;
    }

    void blockedOn(Interruptible interruptible) {
        synchronized (this.blockerLock) {
            this.blocker = interruptible;
        }
    }

    public static void sleep(long j2, int i2) throws InterruptedException {
        if (j2 < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }
        if (i2 < 0 || i2 > 999999) {
            throw new IllegalArgumentException("nanosecond timeout value out of range");
        }
        if (i2 >= 500000 || (i2 != 0 && j2 == 0)) {
            j2++;
        }
        sleep(j2);
    }

    private void init(ThreadGroup threadGroup, Runnable runnable, String str, long j2) {
        init(threadGroup, runnable, str, j2, null, true);
    }

    private void init(ThreadGroup threadGroup, Runnable runnable, String str, long j2, AccessControlContext accessControlContext, boolean z2) {
        if (str == null) {
            throw new NullPointerException("name cannot be null");
        }
        this.name = str;
        Thread threadCurrentThread = currentThread();
        SecurityManager securityManager = System.getSecurityManager();
        if (threadGroup == null) {
            if (securityManager != null) {
                threadGroup = securityManager.getThreadGroup();
            }
            if (threadGroup == null) {
                threadGroup = threadCurrentThread.getThreadGroup();
            }
        }
        threadGroup.checkAccess();
        if (securityManager != null && isCCLOverridden(getClass())) {
            securityManager.checkPermission(SUBCLASS_IMPLEMENTATION_PERMISSION);
        }
        threadGroup.addUnstarted();
        this.group = threadGroup;
        this.daemon = threadCurrentThread.isDaemon();
        this.priority = threadCurrentThread.getPriority();
        if (securityManager == null || isCCLOverridden(threadCurrentThread.getClass())) {
            this.contextClassLoader = threadCurrentThread.getContextClassLoader();
        } else {
            this.contextClassLoader = threadCurrentThread.contextClassLoader;
        }
        this.inheritedAccessControlContext = accessControlContext != null ? accessControlContext : AccessController.getContext();
        this.target = runnable;
        setPriority(this.priority);
        if (z2 && threadCurrentThread.inheritableThreadLocals != null) {
            this.inheritableThreadLocals = ThreadLocal.createInheritedMap(threadCurrentThread.inheritableThreadLocals);
        }
        this.stackSize = j2;
        this.tid = nextThreadID();
    }

    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public Thread() {
        init(null, null, "Thread-" + nextThreadNum(), 0L);
    }

    public Thread(Runnable runnable) {
        init(null, runnable, "Thread-" + nextThreadNum(), 0L);
    }

    Thread(Runnable runnable, AccessControlContext accessControlContext) {
        init(null, runnable, "Thread-" + nextThreadNum(), 0L, accessControlContext, false);
    }

    public Thread(ThreadGroup threadGroup, Runnable runnable) {
        init(threadGroup, runnable, "Thread-" + nextThreadNum(), 0L);
    }

    public Thread(String str) {
        init(null, null, str, 0L);
    }

    public Thread(ThreadGroup threadGroup, String str) {
        init(threadGroup, null, str, 0L);
    }

    public Thread(Runnable runnable, String str) {
        init(null, runnable, str, 0L);
    }

    public Thread(ThreadGroup threadGroup, Runnable runnable, String str) {
        init(threadGroup, runnable, str, 0L);
    }

    public Thread(ThreadGroup threadGroup, Runnable runnable, String str, long j2) {
        init(threadGroup, runnable, str, j2);
    }

    public synchronized void start() {
        if (this.threadStatus != 0) {
            throw new IllegalThreadStateException();
        }
        this.group.add(this);
        boolean z2 = false;
        try {
            start0();
            z2 = true;
            if (1 == 0) {
                try {
                    this.group.threadStartFailed(this);
                } catch (Throwable th) {
                }
            }
        } catch (Throwable th2) {
            if (!z2) {
                try {
                    this.group.threadStartFailed(this);
                } catch (Throwable th3) {
                    throw th2;
                }
            }
            throw th2;
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.target != null) {
            this.target.run();
        }
    }

    private void exit() {
        if (this.threadLocals != null && TerminatingThreadLocal.REGISTRY.isPresent()) {
            TerminatingThreadLocal.threadTerminated();
        }
        if (this.group != null) {
            this.group.threadTerminated(this);
            this.group = null;
        }
        this.target = null;
        this.threadLocals = null;
        this.inheritableThreadLocals = null;
        this.inheritedAccessControlContext = null;
        this.blocker = null;
        this.uncaughtExceptionHandler = null;
    }

    @Deprecated
    public final void stop() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            checkAccess();
            if (this != currentThread()) {
                securityManager.checkPermission(SecurityConstants.STOP_THREAD_PERMISSION);
            }
        }
        if (this.threadStatus != 0) {
            resume();
        }
        stop0(new ThreadDeath());
    }

    @Deprecated
    public final synchronized void stop(Throwable th) {
        throw new UnsupportedOperationException();
    }

    public void interrupt() {
        if (this != currentThread()) {
            checkAccess();
        }
        synchronized (this.blockerLock) {
            Interruptible interruptible = this.blocker;
            if (interruptible != null) {
                interrupt0();
                interruptible.interrupt(this);
            } else {
                interrupt0();
            }
        }
    }

    public static boolean interrupted() {
        return currentThread().isInterrupted(true);
    }

    public boolean isInterrupted() {
        return isInterrupted(false);
    }

    @Deprecated
    public void destroy() {
        throw new NoSuchMethodError();
    }

    @Deprecated
    public final void suspend() {
        checkAccess();
        suspend0();
    }

    @Deprecated
    public final void resume() {
        checkAccess();
        resume0();
    }

    public final void setPriority(int i2) {
        checkAccess();
        if (i2 > 10 || i2 < 1) {
            throw new IllegalArgumentException();
        }
        ThreadGroup threadGroup = getThreadGroup();
        if (threadGroup != null) {
            if (i2 > threadGroup.getMaxPriority()) {
                i2 = threadGroup.getMaxPriority();
            }
            int i3 = i2;
            this.priority = i3;
            setPriority0(i3);
        }
    }

    public final int getPriority() {
        return this.priority;
    }

    public final synchronized void setName(String str) {
        checkAccess();
        if (str == null) {
            throw new NullPointerException("name cannot be null");
        }
        this.name = str;
        if (this.threadStatus != 0) {
            setNativeName(str);
        }
    }

    public final String getName() {
        return this.name;
    }

    public final ThreadGroup getThreadGroup() {
        return this.group;
    }

    public static int activeCount() {
        return currentThread().getThreadGroup().activeCount();
    }

    public static int enumerate(Thread[] threadArr) {
        return currentThread().getThreadGroup().enumerate(threadArr);
    }

    public final synchronized void join(long j2) throws InterruptedException {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long jCurrentTimeMillis2 = 0;
        if (j2 < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }
        if (j2 == 0) {
            while (isAlive()) {
                wait(0L);
            }
            return;
        }
        while (isAlive()) {
            long j3 = j2 - jCurrentTimeMillis2;
            if (j3 > 0) {
                wait(j3);
                jCurrentTimeMillis2 = System.currentTimeMillis() - jCurrentTimeMillis;
            } else {
                return;
            }
        }
    }

    public final synchronized void join(long j2, int i2) throws InterruptedException {
        if (j2 < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }
        if (i2 < 0 || i2 > 999999) {
            throw new IllegalArgumentException("nanosecond timeout value out of range");
        }
        if (i2 >= 500000 || (i2 != 0 && j2 == 0)) {
            j2++;
        }
        join(j2);
    }

    public final void join() throws InterruptedException {
        join(0L);
    }

    public static void dumpStack() {
        new Exception("Stack trace").printStackTrace();
    }

    public final void setDaemon(boolean z2) {
        checkAccess();
        if (isAlive()) {
            throw new IllegalThreadStateException();
        }
        this.daemon = z2;
    }

    public final boolean isDaemon() {
        return this.daemon;
    }

    public final void checkAccess() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkAccess(this);
        }
    }

    public String toString() {
        ThreadGroup threadGroup = getThreadGroup();
        if (threadGroup != null) {
            return "Thread[" + getName() + "," + getPriority() + "," + threadGroup.getName() + "]";
        }
        return "Thread[" + getName() + "," + getPriority() + ",]";
    }

    @CallerSensitive
    public ClassLoader getContextClassLoader() {
        if (this.contextClassLoader == null) {
            return null;
        }
        if (System.getSecurityManager() != null) {
            ClassLoader.checkClassLoaderPermission(this.contextClassLoader, Reflection.getCallerClass());
        }
        return this.contextClassLoader;
    }

    public void setContextClassLoader(ClassLoader classLoader) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("setContextClassLoader"));
        }
        this.contextClassLoader = classLoader;
    }

    public StackTraceElement[] getStackTrace() {
        if (this != currentThread()) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(SecurityConstants.GET_STACK_TRACE_PERMISSION);
            }
            if (!isAlive()) {
                return EMPTY_STACK_TRACE;
            }
            StackTraceElement[] stackTraceElementArr = dumpThreads(new Thread[]{this})[0];
            if (stackTraceElementArr == null) {
                stackTraceElementArr = EMPTY_STACK_TRACE;
            }
            return stackTraceElementArr;
        }
        return new Exception().getStackTrace();
    }

    public static Map<Thread, StackTraceElement[]> getAllStackTraces() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SecurityConstants.GET_STACK_TRACE_PERMISSION);
            securityManager.checkPermission(SecurityConstants.MODIFY_THREADGROUP_PERMISSION);
        }
        Thread[] threads = getThreads();
        StackTraceElement[][] stackTraceElementArrDumpThreads = dumpThreads(threads);
        HashMap map = new HashMap(threads.length);
        for (int i2 = 0; i2 < threads.length; i2++) {
            StackTraceElement[] stackTraceElementArr = stackTraceElementArrDumpThreads[i2];
            if (stackTraceElementArr != null) {
                map.put(threads[i2], stackTraceElementArr);
            }
        }
        return map;
    }

    /* loaded from: rt.jar:java/lang/Thread$Caches.class */
    private static class Caches {
        static final ConcurrentMap<WeakClassKey, Boolean> subclassAudits = new ConcurrentHashMap();
        static final ReferenceQueue<Class<?>> subclassAuditsQueue = new ReferenceQueue<>();

        private Caches() {
        }
    }

    private static boolean isCCLOverridden(Class<?> cls) {
        if (cls == Thread.class) {
            return false;
        }
        processQueue(Caches.subclassAuditsQueue, Caches.subclassAudits);
        WeakClassKey weakClassKey = new WeakClassKey(cls, Caches.subclassAuditsQueue);
        Boolean boolValueOf = Caches.subclassAudits.get(weakClassKey);
        if (boolValueOf == null) {
            boolValueOf = Boolean.valueOf(auditSubclass(cls));
            Caches.subclassAudits.putIfAbsent(weakClassKey, boolValueOf);
        }
        return boolValueOf.booleanValue();
    }

    private static boolean auditSubclass(final Class<?> cls) {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: java.lang.Thread.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Boolean run() throws SecurityException {
                Class superclass = cls;
                while (true) {
                    Class cls2 = superclass;
                    if (cls2 != Thread.class) {
                        try {
                            cls2.getDeclaredMethod("getContextClassLoader", new Class[0]);
                            return Boolean.TRUE;
                        } catch (NoSuchMethodException e2) {
                            try {
                                cls2.getDeclaredMethod("setContextClassLoader", ClassLoader.class);
                                return Boolean.TRUE;
                            } catch (NoSuchMethodException e3) {
                                superclass = cls2.getSuperclass();
                            }
                        }
                    } else {
                        return Boolean.FALSE;
                    }
                }
            }
        })).booleanValue();
    }

    public long getId() {
        return this.tid;
    }

    public State getState() {
        return VM.toThreadState(this.threadStatus);
    }

    public static void setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("setDefaultUncaughtExceptionHandler"));
        }
        defaultUncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    public static UncaughtExceptionHandler getDefaultUncaughtExceptionHandler() {
        return defaultUncaughtExceptionHandler;
    }

    public UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return this.uncaughtExceptionHandler != null ? this.uncaughtExceptionHandler : this.group;
    }

    public void setUncaughtExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler) {
        checkAccess();
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    private void dispatchUncaughtException(Throwable th) {
        getUncaughtExceptionHandler().uncaughtException(this, th);
    }

    static void processQueue(ReferenceQueue<Class<?>> referenceQueue, ConcurrentMap<? extends WeakReference<Class<?>>, ?> concurrentMap) {
        while (true) {
            Reference<? extends Class<?>> referencePoll = referenceQueue.poll();
            if (referencePoll != null) {
                concurrentMap.remove(referencePoll);
            } else {
                return;
            }
        }
    }

    /* loaded from: rt.jar:java/lang/Thread$WeakClassKey.class */
    static class WeakClassKey extends WeakReference<Class<?>> {
        private final int hash;

        WeakClassKey(Class<?> cls, ReferenceQueue<Class<?>> referenceQueue) {
            super(cls, referenceQueue);
            this.hash = System.identityHashCode(cls);
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            Class<?> cls;
            if (obj == this) {
                return true;
            }
            return (obj instanceof WeakClassKey) && (cls = get()) != null && cls == ((WeakClassKey) obj).get();
        }
    }
}
