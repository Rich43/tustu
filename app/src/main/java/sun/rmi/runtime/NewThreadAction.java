package sun.rmi.runtime;

import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:sun/rmi/runtime/NewThreadAction.class */
public final class NewThreadAction implements PrivilegedAction<Thread> {
    static final ThreadGroup systemThreadGroup = (ThreadGroup) AccessController.doPrivileged(new PrivilegedAction<ThreadGroup>() { // from class: sun.rmi.runtime.NewThreadAction.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public ThreadGroup run2() {
            ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
            while (true) {
                ThreadGroup threadGroup2 = threadGroup;
                ThreadGroup parent = threadGroup2.getParent();
                if (parent != null) {
                    threadGroup = parent;
                } else {
                    return threadGroup2;
                }
            }
        }
    });
    static final ThreadGroup userThreadGroup = (ThreadGroup) AccessController.doPrivileged(new PrivilegedAction<ThreadGroup>() { // from class: sun.rmi.runtime.NewThreadAction.2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public ThreadGroup run2() {
            return new ThreadGroup(NewThreadAction.systemThreadGroup, "RMI Runtime");
        }
    });
    private final ThreadGroup group;
    private final Runnable runnable;
    private final String name;
    private final boolean daemon;

    NewThreadAction(ThreadGroup threadGroup, Runnable runnable, String str, boolean z2) {
        this.group = threadGroup;
        this.runnable = runnable;
        this.name = str;
        this.daemon = z2;
    }

    public NewThreadAction(Runnable runnable, String str, boolean z2) {
        this(systemThreadGroup, runnable, str, z2);
    }

    public NewThreadAction(Runnable runnable, String str, boolean z2, boolean z3) {
        this(z3 ? userThreadGroup : systemThreadGroup, runnable, str, z2);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.security.PrivilegedAction
    /* renamed from: run */
    public Thread run2() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
        }
        Thread thread = new Thread(this.group, this.runnable, "RMI " + this.name);
        thread.setContextClassLoader(ClassLoader.getSystemClassLoader());
        thread.setDaemon(this.daemon);
        return thread;
    }
}
