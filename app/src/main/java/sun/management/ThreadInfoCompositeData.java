package sun.management;

import java.lang.Thread;
import java.lang.management.LockInfo;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;

/* loaded from: rt.jar:sun/management/ThreadInfoCompositeData.class */
public class ThreadInfoCompositeData extends LazyCompositeData {
    private final ThreadInfo threadInfo;
    private final CompositeData cdata;
    private final boolean currentVersion;
    private static final String STACK_TRACE = "stackTrace";
    private static final CompositeType threadInfoCompositeType;
    private static final CompositeType threadInfoV5CompositeType;
    private static final CompositeType lockInfoCompositeType;
    private static final long serialVersionUID = 2464378539119753175L;
    private static final String THREAD_ID = "threadId";
    private static final String THREAD_NAME = "threadName";
    private static final String THREAD_STATE = "threadState";
    private static final String BLOCKED_TIME = "blockedTime";
    private static final String BLOCKED_COUNT = "blockedCount";
    private static final String WAITED_TIME = "waitedTime";
    private static final String WAITED_COUNT = "waitedCount";
    private static final String LOCK_INFO = "lockInfo";
    private static final String LOCK_NAME = "lockName";
    private static final String LOCK_OWNER_ID = "lockOwnerId";
    private static final String LOCK_OWNER_NAME = "lockOwnerName";
    private static final String SUSPENDED = "suspended";
    private static final String IN_NATIVE = "inNative";
    private static final String LOCKED_MONITORS = "lockedMonitors";
    private static final String LOCKED_SYNCS = "lockedSynchronizers";
    private static final String[] threadInfoItemNames = {THREAD_ID, THREAD_NAME, THREAD_STATE, BLOCKED_TIME, BLOCKED_COUNT, WAITED_TIME, WAITED_COUNT, LOCK_INFO, LOCK_NAME, LOCK_OWNER_ID, LOCK_OWNER_NAME, "stackTrace", SUSPENDED, IN_NATIVE, LOCKED_MONITORS, LOCKED_SYNCS};
    private static final String[] threadInfoV6Attributes = {LOCK_INFO, LOCKED_MONITORS, LOCKED_SYNCS};

    private ThreadInfoCompositeData(ThreadInfo threadInfo) {
        this.threadInfo = threadInfo;
        this.currentVersion = true;
        this.cdata = null;
    }

    private ThreadInfoCompositeData(CompositeData compositeData) {
        this.threadInfo = null;
        this.currentVersion = isCurrentVersion(compositeData);
        this.cdata = compositeData;
    }

    public ThreadInfo getThreadInfo() {
        return this.threadInfo;
    }

    public boolean isCurrentVersion() {
        return this.currentVersion;
    }

    public static ThreadInfoCompositeData getInstance(CompositeData compositeData) {
        validateCompositeData(compositeData);
        return new ThreadInfoCompositeData(compositeData);
    }

    public static CompositeData toCompositeData(ThreadInfo threadInfo) {
        return new ThreadInfoCompositeData(threadInfo).getCompositeData();
    }

    @Override // sun.management.LazyCompositeData
    protected CompositeData getCompositeData() {
        StackTraceElement[] stackTrace = this.threadInfo.getStackTrace();
        CompositeData[] compositeDataArr = new CompositeData[stackTrace.length];
        for (int i2 = 0; i2 < stackTrace.length; i2++) {
            compositeDataArr[i2] = StackTraceElementCompositeData.toCompositeData(stackTrace[i2]);
        }
        CompositeData compositeData = LockInfoCompositeData.toCompositeData(this.threadInfo.getLockInfo());
        LockInfo[] lockedSynchronizers = this.threadInfo.getLockedSynchronizers();
        CompositeData[] compositeDataArr2 = new CompositeData[lockedSynchronizers.length];
        for (int i3 = 0; i3 < lockedSynchronizers.length; i3++) {
            compositeDataArr2[i3] = LockInfoCompositeData.toCompositeData(lockedSynchronizers[i3]);
        }
        MonitorInfo[] lockedMonitors = this.threadInfo.getLockedMonitors();
        CompositeData[] compositeDataArr3 = new CompositeData[lockedMonitors.length];
        for (int i4 = 0; i4 < lockedMonitors.length; i4++) {
            compositeDataArr3[i4] = MonitorInfoCompositeData.toCompositeData(lockedMonitors[i4]);
        }
        try {
            return new CompositeDataSupport(threadInfoCompositeType, threadInfoItemNames, new Object[]{new Long(this.threadInfo.getThreadId()), this.threadInfo.getThreadName(), this.threadInfo.getThreadState().name(), new Long(this.threadInfo.getBlockedTime()), new Long(this.threadInfo.getBlockedCount()), new Long(this.threadInfo.getWaitedTime()), new Long(this.threadInfo.getWaitedCount()), compositeData, this.threadInfo.getLockName(), new Long(this.threadInfo.getLockOwnerId()), this.threadInfo.getLockOwnerName(), compositeDataArr, new Boolean(this.threadInfo.isSuspended()), new Boolean(this.threadInfo.isInNative()), compositeDataArr3, compositeDataArr2});
        } catch (OpenDataException e2) {
            throw new AssertionError(e2);
        }
    }

    static {
        try {
            threadInfoCompositeType = (CompositeType) MappedMXBeanType.toOpenType(ThreadInfo.class);
            String[] strArr = (String[]) threadInfoCompositeType.keySet().toArray(new String[0]);
            int length = threadInfoItemNames.length - threadInfoV6Attributes.length;
            String[] strArr2 = new String[length];
            String[] strArr3 = new String[length];
            OpenType[] openTypeArr = new OpenType[length];
            int i2 = 0;
            for (String str : strArr) {
                if (isV5Attribute(str)) {
                    strArr2[i2] = str;
                    strArr3[i2] = threadInfoCompositeType.getDescription(str);
                    openTypeArr[i2] = threadInfoCompositeType.getType(str);
                    i2++;
                }
            }
            threadInfoV5CompositeType = new CompositeType("java.lang.management.ThreadInfo", "J2SE 5.0 java.lang.management.ThreadInfo", strArr2, strArr3, openTypeArr);
            Object obj = new Object();
            lockInfoCompositeType = LockInfoCompositeData.toCompositeData(new LockInfo(obj.getClass().getName(), System.identityHashCode(obj))).getCompositeType();
        } catch (OpenDataException e2) {
            throw new AssertionError(e2);
        }
    }

    private static boolean isV5Attribute(String str) {
        for (String str2 : threadInfoV6Attributes) {
            if (str.equals(str2)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isCurrentVersion(CompositeData compositeData) {
        if (compositeData == null) {
            throw new NullPointerException("Null CompositeData");
        }
        return isTypeMatched(threadInfoCompositeType, compositeData.getCompositeType());
    }

    public long threadId() {
        return getLong(this.cdata, THREAD_ID);
    }

    public String threadName() {
        String string = getString(this.cdata, THREAD_NAME);
        if (string == null) {
            throw new IllegalArgumentException("Invalid composite data: Attribute threadName has null value");
        }
        return string;
    }

    public Thread.State threadState() {
        return Thread.State.valueOf(getString(this.cdata, THREAD_STATE));
    }

    public long blockedTime() {
        return getLong(this.cdata, BLOCKED_TIME);
    }

    public long blockedCount() {
        return getLong(this.cdata, BLOCKED_COUNT);
    }

    public long waitedTime() {
        return getLong(this.cdata, WAITED_TIME);
    }

    public long waitedCount() {
        return getLong(this.cdata, WAITED_COUNT);
    }

    public String lockName() {
        return getString(this.cdata, LOCK_NAME);
    }

    public long lockOwnerId() {
        return getLong(this.cdata, LOCK_OWNER_ID);
    }

    public String lockOwnerName() {
        return getString(this.cdata, LOCK_OWNER_NAME);
    }

    public boolean suspended() {
        return getBoolean(this.cdata, SUSPENDED);
    }

    public boolean inNative() {
        return getBoolean(this.cdata, IN_NATIVE);
    }

    public StackTraceElement[] stackTrace() {
        CompositeData[] compositeDataArr = (CompositeData[]) this.cdata.get("stackTrace");
        StackTraceElement[] stackTraceElementArr = new StackTraceElement[compositeDataArr.length];
        for (int i2 = 0; i2 < compositeDataArr.length; i2++) {
            stackTraceElementArr[i2] = StackTraceElementCompositeData.from(compositeDataArr[i2]);
        }
        return stackTraceElementArr;
    }

    public LockInfo lockInfo() {
        return LockInfo.from((CompositeData) this.cdata.get(LOCK_INFO));
    }

    public MonitorInfo[] lockedMonitors() {
        CompositeData[] compositeDataArr = (CompositeData[]) this.cdata.get(LOCKED_MONITORS);
        MonitorInfo[] monitorInfoArr = new MonitorInfo[compositeDataArr.length];
        for (int i2 = 0; i2 < compositeDataArr.length; i2++) {
            monitorInfoArr[i2] = MonitorInfo.from(compositeDataArr[i2]);
        }
        return monitorInfoArr;
    }

    public LockInfo[] lockedSynchronizers() {
        CompositeData[] compositeDataArr = (CompositeData[]) this.cdata.get(LOCKED_SYNCS);
        LockInfo[] lockInfoArr = new LockInfo[compositeDataArr.length];
        for (int i2 = 0; i2 < compositeDataArr.length; i2++) {
            lockInfoArr[i2] = LockInfo.from(compositeDataArr[i2]);
        }
        return lockInfoArr;
    }

    public static void validateCompositeData(CompositeData compositeData) {
        if (compositeData == null) {
            throw new NullPointerException("Null CompositeData");
        }
        CompositeType compositeType = compositeData.getCompositeType();
        boolean z2 = true;
        if (!isTypeMatched(threadInfoCompositeType, compositeType)) {
            z2 = false;
            if (!isTypeMatched(threadInfoV5CompositeType, compositeType)) {
                throw new IllegalArgumentException("Unexpected composite type for ThreadInfo");
            }
        }
        CompositeData[] compositeDataArr = (CompositeData[]) compositeData.get("stackTrace");
        if (compositeDataArr == null) {
            throw new IllegalArgumentException("StackTraceElement[] is missing");
        }
        if (compositeDataArr.length > 0) {
            StackTraceElementCompositeData.validateCompositeData(compositeDataArr[0]);
        }
        if (z2) {
            CompositeData compositeData2 = (CompositeData) compositeData.get(LOCK_INFO);
            if (compositeData2 != null && !isTypeMatched(lockInfoCompositeType, compositeData2.getCompositeType())) {
                throw new IllegalArgumentException("Unexpected composite type for \"lockInfo\" attribute.");
            }
            CompositeData[] compositeDataArr2 = (CompositeData[]) compositeData.get(LOCKED_MONITORS);
            if (compositeDataArr2 == null) {
                throw new IllegalArgumentException("MonitorInfo[] is null");
            }
            if (compositeDataArr2.length > 0) {
                MonitorInfoCompositeData.validateCompositeData(compositeDataArr2[0]);
            }
            CompositeData[] compositeDataArr3 = (CompositeData[]) compositeData.get(LOCKED_SYNCS);
            if (compositeDataArr3 == null) {
                throw new IllegalArgumentException("LockInfo[] is null");
            }
            if (compositeDataArr3.length > 0 && !isTypeMatched(lockInfoCompositeType, compositeDataArr3[0].getCompositeType())) {
                throw new IllegalArgumentException("Unexpected composite type for \"lockedSynchronizers\" attribute.");
            }
        }
    }
}
