package sun.misc;

/* loaded from: rt.jar:sun/misc/ThreadGroupUtils.class */
public final class ThreadGroupUtils {
    private ThreadGroupUtils() {
    }

    public static ThreadGroup getRootThreadGroup() {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup parent = threadGroup.getParent();
        while (true) {
            ThreadGroup threadGroup2 = parent;
            if (threadGroup2 != null) {
                threadGroup = threadGroup2;
                parent = threadGroup.getParent();
            } else {
                return threadGroup;
            }
        }
    }
}
