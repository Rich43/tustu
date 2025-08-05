package sun.misc;

import java.lang.Thread;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: rt.jar:sun/misc/InnocuousThread.class */
public final class InnocuousThread extends Thread {
    private static final Unsafe UNSAFE;
    private static final long THREAD_LOCALS;
    private static final long INHERITABLE_THREAD_LOCALS;
    private static final ThreadGroup INNOCUOUSTHREADGROUP;
    private static final AccessControlContext ACC;
    private static final long INHERITEDACCESSCONTROLCONTEXT;
    private static final long CONTEXTCLASSLOADER;
    private static final AtomicInteger threadNumber = new AtomicInteger(1);
    private volatile boolean hasRun;

    static {
        ThreadGroup threadGroup;
        try {
            ACC = new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, null)});
            UNSAFE = Unsafe.getUnsafe();
            THREAD_LOCALS = UNSAFE.objectFieldOffset(Thread.class.getDeclaredField("threadLocals"));
            INHERITABLE_THREAD_LOCALS = UNSAFE.objectFieldOffset(Thread.class.getDeclaredField("inheritableThreadLocals"));
            INHERITEDACCESSCONTROLCONTEXT = UNSAFE.objectFieldOffset(Thread.class.getDeclaredField("inheritedAccessControlContext"));
            CONTEXTCLASSLOADER = UNSAFE.objectFieldOffset(Thread.class.getDeclaredField("contextClassLoader"));
            long jObjectFieldOffset = UNSAFE.objectFieldOffset(Thread.class.getDeclaredField("group"));
            long jObjectFieldOffset2 = UNSAFE.objectFieldOffset(ThreadGroup.class.getDeclaredField("parent"));
            ThreadGroup threadGroup2 = (ThreadGroup) UNSAFE.getObject(Thread.currentThread(), jObjectFieldOffset);
            while (threadGroup2 != null && (threadGroup = (ThreadGroup) UNSAFE.getObject(threadGroup2, jObjectFieldOffset2)) != null) {
                threadGroup2 = threadGroup;
            }
            final ThreadGroup threadGroup3 = threadGroup2;
            INNOCUOUSTHREADGROUP = (ThreadGroup) AccessController.doPrivileged(new PrivilegedAction<ThreadGroup>() { // from class: sun.misc.InnocuousThread.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public ThreadGroup run2() {
                    return new ThreadGroup(threadGroup3, "InnocuousThreadGroup");
                }
            });
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }

    private static String newName() {
        return "InnocuousThread-" + threadNumber.getAndIncrement();
    }

    public static Thread newSystemThread(Runnable runnable) {
        return newSystemThread(newName(), runnable);
    }

    public static Thread newSystemThread(final String str, final Runnable runnable) {
        return (Thread) AccessController.doPrivileged(new PrivilegedAction<Thread>() { // from class: sun.misc.InnocuousThread.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Thread run2() {
                return new InnocuousThread(InnocuousThread.INNOCUOUSTHREADGROUP, runnable, str, null);
            }
        });
    }

    public InnocuousThread(Runnable runnable) {
        super(INNOCUOUSTHREADGROUP, runnable, newName());
        UNSAFE.putOrderedObject(this, INHERITEDACCESSCONTROLCONTEXT, ACC);
        eraseThreadLocals();
    }

    private InnocuousThread(ThreadGroup threadGroup, Runnable runnable, String str, ClassLoader classLoader) {
        super(threadGroup, runnable, str, 0L);
        UNSAFE.putOrderedObject(this, INHERITEDACCESSCONTROLCONTEXT, ACC);
        UNSAFE.putOrderedObject(this, CONTEXTCLASSLOADER, classLoader);
        eraseThreadLocals();
    }

    @Override // java.lang.Thread
    public ClassLoader getContextClassLoader() {
        return ClassLoader.getSystemClassLoader();
    }

    @Override // java.lang.Thread
    public void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
    }

    @Override // java.lang.Thread
    public void setContextClassLoader(ClassLoader classLoader) {
        throw new SecurityException("setContextClassLoader");
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        if (Thread.currentThread() == this && !this.hasRun) {
            this.hasRun = true;
            super.run();
        }
    }

    public void eraseThreadLocals() {
        UNSAFE.putObject(this, THREAD_LOCALS, (Object) null);
        UNSAFE.putObject(this, INHERITABLE_THREAD_LOCALS, (Object) null);
    }
}
