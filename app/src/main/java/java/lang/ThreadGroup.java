package java.lang;

import java.io.PrintStream;
import java.lang.Thread;
import java.util.Arrays;
import sun.misc.VM;

/* loaded from: rt.jar:java/lang/ThreadGroup.class */
public class ThreadGroup implements Thread.UncaughtExceptionHandler {
    private final ThreadGroup parent;
    String name;
    int maxPriority;
    boolean destroyed;
    boolean daemon;
    boolean vmAllowSuspension;
    int nUnstartedThreads;
    int nthreads;
    Thread[] threads;
    int ngroups;
    ThreadGroup[] groups;

    private ThreadGroup() {
        this.nUnstartedThreads = 0;
        this.name = "system";
        this.maxPriority = 10;
        this.parent = null;
    }

    public ThreadGroup(String str) {
        this(Thread.currentThread().getThreadGroup(), str);
    }

    public ThreadGroup(ThreadGroup threadGroup, String str) {
        this(checkParentAccess(threadGroup), threadGroup, str);
    }

    private ThreadGroup(Void r4, ThreadGroup threadGroup, String str) {
        this.nUnstartedThreads = 0;
        this.name = str;
        this.maxPriority = threadGroup.maxPriority;
        this.daemon = threadGroup.daemon;
        this.vmAllowSuspension = threadGroup.vmAllowSuspension;
        this.parent = threadGroup;
        threadGroup.add(this);
    }

    private static Void checkParentAccess(ThreadGroup threadGroup) {
        threadGroup.checkAccess();
        return null;
    }

    public final String getName() {
        return this.name;
    }

    public final ThreadGroup getParent() {
        if (this.parent != null) {
            this.parent.checkAccess();
        }
        return this.parent;
    }

    public final int getMaxPriority() {
        return this.maxPriority;
    }

    public final boolean isDaemon() {
        return this.daemon;
    }

    public synchronized boolean isDestroyed() {
        return this.destroyed;
    }

    public final void setDaemon(boolean z2) {
        checkAccess();
        this.daemon = z2;
    }

    public final void setMaxPriority(int i2) {
        ThreadGroup[] threadGroupArr;
        synchronized (this) {
            checkAccess();
            if (i2 < 1 || i2 > 10) {
                return;
            }
            this.maxPriority = this.parent != null ? Math.min(i2, this.parent.maxPriority) : i2;
            int i3 = this.ngroups;
            if (this.groups != null) {
                threadGroupArr = (ThreadGroup[]) Arrays.copyOf(this.groups, i3);
            } else {
                threadGroupArr = null;
            }
            for (int i4 = 0; i4 < i3; i4++) {
                threadGroupArr[i4].setMaxPriority(i2);
            }
        }
    }

    public final boolean parentOf(ThreadGroup threadGroup) {
        while (threadGroup != null) {
            if (threadGroup != this) {
                threadGroup = threadGroup.parent;
            } else {
                return true;
            }
        }
        return false;
    }

    public final void checkAccess() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkAccess(this);
        }
    }

    public int activeCount() {
        ThreadGroup[] threadGroupArr;
        synchronized (this) {
            if (this.destroyed) {
                return 0;
            }
            int iActiveCount = this.nthreads;
            int i2 = this.ngroups;
            if (this.groups != null) {
                threadGroupArr = (ThreadGroup[]) Arrays.copyOf(this.groups, i2);
            } else {
                threadGroupArr = null;
            }
            for (int i3 = 0; i3 < i2; i3++) {
                iActiveCount += threadGroupArr[i3].activeCount();
            }
            return iActiveCount;
        }
    }

    public int enumerate(Thread[] threadArr) {
        checkAccess();
        return enumerate(threadArr, 0, true);
    }

    public int enumerate(Thread[] threadArr, boolean z2) {
        checkAccess();
        return enumerate(threadArr, 0, z2);
    }

    private int enumerate(Thread[] threadArr, int i2, boolean z2) {
        int i3 = 0;
        ThreadGroup[] threadGroupArr = null;
        synchronized (this) {
            if (this.destroyed) {
                return 0;
            }
            int length = this.nthreads;
            if (length > threadArr.length - i2) {
                length = threadArr.length - i2;
            }
            for (int i4 = 0; i4 < length; i4++) {
                if (this.threads[i4].isAlive()) {
                    int i5 = i2;
                    i2++;
                    threadArr[i5] = this.threads[i4];
                }
            }
            if (z2) {
                i3 = this.ngroups;
                if (this.groups != null) {
                    threadGroupArr = (ThreadGroup[]) Arrays.copyOf(this.groups, i3);
                } else {
                    threadGroupArr = null;
                }
            }
            if (z2) {
                for (int i6 = 0; i6 < i3; i6++) {
                    i2 = threadGroupArr[i6].enumerate(threadArr, i2, true);
                }
            }
            return i2;
        }
    }

    public int activeGroupCount() {
        ThreadGroup[] threadGroupArr;
        synchronized (this) {
            if (this.destroyed) {
                return 0;
            }
            int i2 = this.ngroups;
            if (this.groups != null) {
                threadGroupArr = (ThreadGroup[]) Arrays.copyOf(this.groups, i2);
            } else {
                threadGroupArr = null;
            }
            int iActiveGroupCount = i2;
            for (int i3 = 0; i3 < i2; i3++) {
                iActiveGroupCount += threadGroupArr[i3].activeGroupCount();
            }
            return iActiveGroupCount;
        }
    }

    public int enumerate(ThreadGroup[] threadGroupArr) {
        checkAccess();
        return enumerate(threadGroupArr, 0, true);
    }

    public int enumerate(ThreadGroup[] threadGroupArr, boolean z2) {
        checkAccess();
        return enumerate(threadGroupArr, 0, z2);
    }

    private int enumerate(ThreadGroup[] threadGroupArr, int i2, boolean z2) {
        int i3 = 0;
        ThreadGroup[] threadGroupArr2 = null;
        synchronized (this) {
            if (this.destroyed) {
                return 0;
            }
            int length = this.ngroups;
            if (length > threadGroupArr.length - i2) {
                length = threadGroupArr.length - i2;
            }
            if (length > 0) {
                System.arraycopy(this.groups, 0, threadGroupArr, i2, length);
                i2 += length;
            }
            if (z2) {
                i3 = this.ngroups;
                if (this.groups != null) {
                    threadGroupArr2 = (ThreadGroup[]) Arrays.copyOf(this.groups, i3);
                } else {
                    threadGroupArr2 = null;
                }
            }
            if (z2) {
                for (int i4 = 0; i4 < i3; i4++) {
                    i2 = threadGroupArr2[i4].enumerate(threadGroupArr, i2, true);
                }
            }
            return i2;
        }
    }

    @Deprecated
    public final void stop() {
        if (stopOrSuspend(false)) {
            Thread.currentThread().stop();
        }
    }

    public final void interrupt() {
        int i2;
        ThreadGroup[] threadGroupArr;
        synchronized (this) {
            checkAccess();
            for (int i3 = 0; i3 < this.nthreads; i3++) {
                this.threads[i3].interrupt();
            }
            i2 = this.ngroups;
            if (this.groups != null) {
                threadGroupArr = (ThreadGroup[]) Arrays.copyOf(this.groups, i2);
            } else {
                threadGroupArr = null;
            }
        }
        for (int i4 = 0; i4 < i2; i4++) {
            threadGroupArr[i4].interrupt();
        }
    }

    @Deprecated
    public final void suspend() {
        if (stopOrSuspend(true)) {
            Thread.currentThread().suspend();
        }
    }

    private boolean stopOrSuspend(boolean z2) {
        int i2;
        boolean z3 = false;
        Thread threadCurrentThread = Thread.currentThread();
        ThreadGroup[] threadGroupArr = null;
        synchronized (this) {
            checkAccess();
            for (int i3 = 0; i3 < this.nthreads; i3++) {
                if (this.threads[i3] == threadCurrentThread) {
                    z3 = true;
                } else if (z2) {
                    this.threads[i3].suspend();
                } else {
                    this.threads[i3].stop();
                }
            }
            i2 = this.ngroups;
            if (this.groups != null) {
                threadGroupArr = (ThreadGroup[]) Arrays.copyOf(this.groups, i2);
            }
        }
        for (int i4 = 0; i4 < i2; i4++) {
            z3 = threadGroupArr[i4].stopOrSuspend(z2) || z3;
        }
        return z3;
    }

    @Deprecated
    public final void resume() {
        int i2;
        ThreadGroup[] threadGroupArr;
        synchronized (this) {
            checkAccess();
            for (int i3 = 0; i3 < this.nthreads; i3++) {
                this.threads[i3].resume();
            }
            i2 = this.ngroups;
            if (this.groups != null) {
                threadGroupArr = (ThreadGroup[]) Arrays.copyOf(this.groups, i2);
            } else {
                threadGroupArr = null;
            }
        }
        for (int i4 = 0; i4 < i2; i4++) {
            threadGroupArr[i4].resume();
        }
    }

    public final void destroy() {
        int i2;
        ThreadGroup[] threadGroupArr;
        synchronized (this) {
            checkAccess();
            if (this.destroyed || this.nthreads > 0) {
                throw new IllegalThreadStateException();
            }
            i2 = this.ngroups;
            if (this.groups != null) {
                threadGroupArr = (ThreadGroup[]) Arrays.copyOf(this.groups, i2);
            } else {
                threadGroupArr = null;
            }
            if (this.parent != null) {
                this.destroyed = true;
                this.ngroups = 0;
                this.groups = null;
                this.nthreads = 0;
                this.threads = null;
            }
        }
        for (int i3 = 0; i3 < i2; i3++) {
            threadGroupArr[i3].destroy();
        }
        if (this.parent != null) {
            this.parent.remove(this);
        }
    }

    private final void add(ThreadGroup threadGroup) {
        synchronized (this) {
            if (this.destroyed) {
                throw new IllegalThreadStateException();
            }
            if (this.groups == null) {
                this.groups = new ThreadGroup[4];
            } else if (this.ngroups == this.groups.length) {
                this.groups = (ThreadGroup[]) Arrays.copyOf(this.groups, this.ngroups * 2);
            }
            this.groups[this.ngroups] = threadGroup;
            this.ngroups++;
        }
    }

    private void remove(ThreadGroup threadGroup) {
        synchronized (this) {
            if (this.destroyed) {
                return;
            }
            int i2 = 0;
            while (true) {
                if (i2 >= this.ngroups) {
                    break;
                }
                if (this.groups[i2] != threadGroup) {
                    i2++;
                } else {
                    this.ngroups--;
                    System.arraycopy(this.groups, i2 + 1, this.groups, i2, this.ngroups - i2);
                    this.groups[this.ngroups] = null;
                    break;
                }
            }
            if (this.nthreads == 0) {
                notifyAll();
            }
            if (this.daemon && this.nthreads == 0 && this.nUnstartedThreads == 0 && this.ngroups == 0) {
                destroy();
            }
        }
    }

    void addUnstarted() {
        synchronized (this) {
            if (this.destroyed) {
                throw new IllegalThreadStateException();
            }
            this.nUnstartedThreads++;
        }
    }

    void add(Thread thread) {
        synchronized (this) {
            if (this.destroyed) {
                throw new IllegalThreadStateException();
            }
            if (this.threads == null) {
                this.threads = new Thread[4];
            } else if (this.nthreads == this.threads.length) {
                this.threads = (Thread[]) Arrays.copyOf(this.threads, this.nthreads * 2);
            }
            this.threads[this.nthreads] = thread;
            this.nthreads++;
            this.nUnstartedThreads--;
        }
    }

    void threadStartFailed(Thread thread) {
        synchronized (this) {
            remove(thread);
            this.nUnstartedThreads++;
        }
    }

    void threadTerminated(Thread thread) {
        synchronized (this) {
            remove(thread);
            if (this.nthreads == 0) {
                notifyAll();
            }
            if (this.daemon && this.nthreads == 0 && this.nUnstartedThreads == 0 && this.ngroups == 0) {
                destroy();
            }
        }
    }

    private void remove(Thread thread) {
        synchronized (this) {
            if (this.destroyed) {
                return;
            }
            int i2 = 0;
            while (true) {
                if (i2 >= this.nthreads) {
                    break;
                }
                if (this.threads[i2] != thread) {
                    i2++;
                } else {
                    int i3 = this.nthreads - 1;
                    this.nthreads = i3;
                    System.arraycopy(this.threads, i2 + 1, this.threads, i2, i3 - i2);
                    this.threads[this.nthreads] = null;
                    break;
                }
            }
        }
    }

    public void list() {
        list(System.out, 0);
    }

    void list(PrintStream printStream, int i2) {
        int i3;
        int i4;
        ThreadGroup[] threadGroupArr;
        synchronized (this) {
            for (int i5 = 0; i5 < i2; i5++) {
                printStream.print(" ");
            }
            printStream.println(this);
            i3 = i2 + 4;
            for (int i6 = 0; i6 < this.nthreads; i6++) {
                for (int i7 = 0; i7 < i3; i7++) {
                    printStream.print(" ");
                }
                printStream.println(this.threads[i6]);
            }
            i4 = this.ngroups;
            if (this.groups != null) {
                threadGroupArr = (ThreadGroup[]) Arrays.copyOf(this.groups, i4);
            } else {
                threadGroupArr = null;
            }
        }
        for (int i8 = 0; i8 < i4; i8++) {
            threadGroupArr[i8].list(printStream, i3);
        }
    }

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public void uncaughtException(Thread thread, Throwable th) {
        if (this.parent != null) {
            this.parent.uncaughtException(thread, th);
            return;
        }
        Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (defaultUncaughtExceptionHandler != null) {
            defaultUncaughtExceptionHandler.uncaughtException(thread, th);
        } else if (!(th instanceof ThreadDeath)) {
            System.err.print("Exception in thread \"" + thread.getName() + "\" ");
            th.printStackTrace(System.err);
        }
    }

    @Deprecated
    public boolean allowThreadSuspension(boolean z2) {
        this.vmAllowSuspension = z2;
        if (!z2) {
            VM.unsuspendSomeThreads();
            return true;
        }
        return true;
    }

    public String toString() {
        return getClass().getName() + "[name=" + getName() + ",maxpri=" + this.maxPriority + "]";
    }
}
