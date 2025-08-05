package java.lang.management;

import java.lang.Thread;
import javax.management.openmbean.CompositeData;
import org.icepdf.core.util.PdfOps;
import sun.management.ManagementFactoryHelper;
import sun.management.ThreadInfoCompositeData;

/* loaded from: rt.jar:java/lang/management/ThreadInfo.class */
public class ThreadInfo {
    private String threadName;
    private long threadId;
    private long blockedTime;
    private long blockedCount;
    private long waitedTime;
    private long waitedCount;
    private LockInfo lock;
    private String lockName;
    private long lockOwnerId;
    private String lockOwnerName;
    private boolean inNative;
    private boolean suspended;
    private Thread.State threadState;
    private StackTraceElement[] stackTrace;
    private MonitorInfo[] lockedMonitors;
    private LockInfo[] lockedSynchronizers;
    private static MonitorInfo[] EMPTY_MONITORS;
    private static LockInfo[] EMPTY_SYNCS;
    private static final int MAX_FRAMES = 8;
    private static final StackTraceElement[] NO_STACK_TRACE;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ThreadInfo.class.desiredAssertionStatus();
        EMPTY_MONITORS = new MonitorInfo[0];
        EMPTY_SYNCS = new LockInfo[0];
        NO_STACK_TRACE = new StackTraceElement[0];
    }

    private ThreadInfo(Thread thread, int i2, Object obj, Thread thread2, long j2, long j3, long j4, long j5, StackTraceElement[] stackTraceElementArr) {
        initialize(thread, i2, obj, thread2, j2, j3, j4, j5, stackTraceElementArr, EMPTY_MONITORS, EMPTY_SYNCS);
    }

    private ThreadInfo(Thread thread, int i2, Object obj, Thread thread2, long j2, long j3, long j4, long j5, StackTraceElement[] stackTraceElementArr, Object[] objArr, int[] iArr, Object[] objArr2) {
        MonitorInfo[] monitorInfoArr;
        LockInfo[] lockInfoArr;
        int length = objArr == null ? 0 : objArr.length;
        if (length == 0) {
            monitorInfoArr = EMPTY_MONITORS;
        } else {
            monitorInfoArr = new MonitorInfo[length];
            for (int i3 = 0; i3 < length; i3++) {
                Object obj2 = objArr[i3];
                String name = obj2.getClass().getName();
                int iIdentityHashCode = System.identityHashCode(obj2);
                int i4 = iArr[i3];
                monitorInfoArr[i3] = new MonitorInfo(name, iIdentityHashCode, i4, i4 >= 0 ? stackTraceElementArr[i4] : null);
            }
        }
        int length2 = objArr2 == null ? 0 : objArr2.length;
        if (length2 == 0) {
            lockInfoArr = EMPTY_SYNCS;
        } else {
            lockInfoArr = new LockInfo[length2];
            for (int i5 = 0; i5 < length2; i5++) {
                Object obj3 = objArr2[i5];
                lockInfoArr[i5] = new LockInfo(obj3.getClass().getName(), System.identityHashCode(obj3));
            }
        }
        initialize(thread, i2, obj, thread2, j2, j3, j4, j5, stackTraceElementArr, monitorInfoArr, lockInfoArr);
    }

    private void initialize(Thread thread, int i2, Object obj, Thread thread2, long j2, long j3, long j4, long j5, StackTraceElement[] stackTraceElementArr, MonitorInfo[] monitorInfoArr, LockInfo[] lockInfoArr) {
        this.threadId = thread.getId();
        this.threadName = thread.getName();
        this.threadState = ManagementFactoryHelper.toThreadState(i2);
        this.suspended = ManagementFactoryHelper.isThreadSuspended(i2);
        this.inNative = ManagementFactoryHelper.isThreadRunningNative(i2);
        this.blockedCount = j2;
        this.blockedTime = j3;
        this.waitedCount = j4;
        this.waitedTime = j5;
        if (obj == null) {
            this.lock = null;
            this.lockName = null;
        } else {
            this.lock = new LockInfo(obj);
            this.lockName = this.lock.getClassName() + '@' + Integer.toHexString(this.lock.getIdentityHashCode());
        }
        if (thread2 == null) {
            this.lockOwnerId = -1L;
            this.lockOwnerName = null;
        } else {
            this.lockOwnerId = thread2.getId();
            this.lockOwnerName = thread2.getName();
        }
        if (stackTraceElementArr == null) {
            this.stackTrace = NO_STACK_TRACE;
        } else {
            this.stackTrace = stackTraceElementArr;
        }
        this.lockedMonitors = monitorInfoArr;
        this.lockedSynchronizers = lockInfoArr;
    }

    private ThreadInfo(CompositeData compositeData) throws NumberFormatException {
        ThreadInfoCompositeData threadInfoCompositeData = ThreadInfoCompositeData.getInstance(compositeData);
        this.threadId = threadInfoCompositeData.threadId();
        this.threadName = threadInfoCompositeData.threadName();
        this.blockedTime = threadInfoCompositeData.blockedTime();
        this.blockedCount = threadInfoCompositeData.blockedCount();
        this.waitedTime = threadInfoCompositeData.waitedTime();
        this.waitedCount = threadInfoCompositeData.waitedCount();
        this.lockName = threadInfoCompositeData.lockName();
        this.lockOwnerId = threadInfoCompositeData.lockOwnerId();
        this.lockOwnerName = threadInfoCompositeData.lockOwnerName();
        this.threadState = threadInfoCompositeData.threadState();
        this.suspended = threadInfoCompositeData.suspended();
        this.inNative = threadInfoCompositeData.inNative();
        this.stackTrace = threadInfoCompositeData.stackTrace();
        if (threadInfoCompositeData.isCurrentVersion()) {
            this.lock = threadInfoCompositeData.lockInfo();
            this.lockedMonitors = threadInfoCompositeData.lockedMonitors();
            this.lockedSynchronizers = threadInfoCompositeData.lockedSynchronizers();
            return;
        }
        if (this.lockName != null) {
            String[] strArrSplit = this.lockName.split("@");
            if (strArrSplit.length == 2) {
                this.lock = new LockInfo(strArrSplit[0], Integer.parseInt(strArrSplit[1], 16));
            } else {
                if (!$assertionsDisabled && strArrSplit.length != 2) {
                    throw new AssertionError();
                }
                this.lock = null;
            }
        } else {
            this.lock = null;
        }
        this.lockedMonitors = EMPTY_MONITORS;
        this.lockedSynchronizers = EMPTY_SYNCS;
    }

    public long getThreadId() {
        return this.threadId;
    }

    public String getThreadName() {
        return this.threadName;
    }

    public Thread.State getThreadState() {
        return this.threadState;
    }

    public long getBlockedTime() {
        return this.blockedTime;
    }

    public long getBlockedCount() {
        return this.blockedCount;
    }

    public long getWaitedTime() {
        return this.waitedTime;
    }

    public long getWaitedCount() {
        return this.waitedCount;
    }

    public LockInfo getLockInfo() {
        return this.lock;
    }

    public String getLockName() {
        return this.lockName;
    }

    public long getLockOwnerId() {
        return this.lockOwnerId;
    }

    public String getLockOwnerName() {
        return this.lockOwnerName;
    }

    public StackTraceElement[] getStackTrace() {
        return this.stackTrace;
    }

    public boolean isSuspended() {
        return this.suspended;
    }

    public boolean isInNative() {
        return this.inNative;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(PdfOps.DOUBLE_QUOTE__TOKEN + getThreadName() + "\" Id=" + getThreadId() + " " + ((Object) getThreadState()));
        if (getLockName() != null) {
            sb.append(" on " + getLockName());
        }
        if (getLockOwnerName() != null) {
            sb.append(" owned by \"" + getLockOwnerName() + "\" Id=" + getLockOwnerId());
        }
        if (isSuspended()) {
            sb.append(" (suspended)");
        }
        if (isInNative()) {
            sb.append(" (in native)");
        }
        sb.append('\n');
        int i2 = 0;
        while (i2 < this.stackTrace.length && i2 < 8) {
            sb.append("\tat " + this.stackTrace[i2].toString());
            sb.append('\n');
            if (i2 == 0 && getLockInfo() != null) {
                switch (getThreadState()) {
                    case BLOCKED:
                        sb.append("\t-  blocked on " + ((Object) getLockInfo()));
                        sb.append('\n');
                        break;
                    case WAITING:
                        sb.append("\t-  waiting on " + ((Object) getLockInfo()));
                        sb.append('\n');
                        break;
                    case TIMED_WAITING:
                        sb.append("\t-  waiting on " + ((Object) getLockInfo()));
                        sb.append('\n');
                        break;
                }
            }
            for (MonitorInfo monitorInfo : this.lockedMonitors) {
                if (monitorInfo.getLockedStackDepth() == i2) {
                    sb.append("\t-  locked " + ((Object) monitorInfo));
                    sb.append('\n');
                }
            }
            i2++;
        }
        if (i2 < this.stackTrace.length) {
            sb.append("\t...");
            sb.append('\n');
        }
        LockInfo[] lockedSynchronizers = getLockedSynchronizers();
        if (lockedSynchronizers.length > 0) {
            sb.append("\n\tNumber of locked synchronizers = " + lockedSynchronizers.length);
            sb.append('\n');
            for (LockInfo lockInfo : lockedSynchronizers) {
                sb.append("\t- " + ((Object) lockInfo));
                sb.append('\n');
            }
        }
        sb.append('\n');
        return sb.toString();
    }

    public static ThreadInfo from(CompositeData compositeData) {
        if (compositeData == null) {
            return null;
        }
        if (compositeData instanceof ThreadInfoCompositeData) {
            return ((ThreadInfoCompositeData) compositeData).getThreadInfo();
        }
        return new ThreadInfo(compositeData);
    }

    public MonitorInfo[] getLockedMonitors() {
        return this.lockedMonitors;
    }

    public LockInfo[] getLockedSynchronizers() {
        return this.lockedSynchronizers;
    }
}
