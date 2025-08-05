package sun.rmi.runtime;

import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import sun.security.action.GetIntegerAction;

/* loaded from: rt.jar:sun/rmi/runtime/RuntimeUtil.class */
public final class RuntimeUtil {
    private static final Log runtimeLog = Log.getLog("sun.rmi.runtime", (String) null, false);
    private static final int schedulerThreads = ((Integer) AccessController.doPrivileged(new GetIntegerAction("sun.rmi.runtime.schedulerThreads", 1))).intValue();
    private static final Permission GET_INSTANCE_PERMISSION = new RuntimePermission("sun.rmi.runtime.RuntimeUtil.getInstance");
    private static final RuntimeUtil instance = new RuntimeUtil();
    private final ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(schedulerThreads, new ThreadFactory() { // from class: sun.rmi.runtime.RuntimeUtil.1
        private final AtomicInteger count = new AtomicInteger(0);

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            try {
                return (Thread) AccessController.doPrivileged(new NewThreadAction(runnable, "Scheduler(" + this.count.getAndIncrement() + ")", true));
            } catch (Throwable th) {
                RuntimeUtil.runtimeLog.log(Level.WARNING, "scheduler thread factory throws", th);
                return null;
            }
        }
    });

    private RuntimeUtil() {
    }

    /* loaded from: rt.jar:sun/rmi/runtime/RuntimeUtil$GetInstanceAction.class */
    public static class GetInstanceAction implements PrivilegedAction<RuntimeUtil> {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public RuntimeUtil run2() {
            return RuntimeUtil.getInstance();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static RuntimeUtil getInstance() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(GET_INSTANCE_PERMISSION);
        }
        return instance;
    }

    public ScheduledThreadPoolExecutor getScheduler() {
        return this.scheduler;
    }
}
