package sun.management;

import com.sun.management.ThreadMXBean;
import java.lang.management.ThreadInfo;
import java.util.Arrays;
import java.util.Objects;
import javax.management.ObjectName;

/* loaded from: rt.jar:sun/management/ThreadImpl.class */
class ThreadImpl implements ThreadMXBean {
    private final VMManagement jvm;
    private boolean contentionMonitoringEnabled = false;
    private boolean cpuTimeEnabled;
    private boolean allocatedMemoryEnabled;

    private static native Thread[] getThreads();

    private static native void getThreadInfo1(long[] jArr, int i2, ThreadInfo[] threadInfoArr);

    private static native long getThreadTotalCpuTime0(long j2);

    private static native void getThreadTotalCpuTime1(long[] jArr, long[] jArr2);

    private static native long getThreadUserCpuTime0(long j2);

    private static native void getThreadUserCpuTime1(long[] jArr, long[] jArr2);

    private static native long getThreadAllocatedMemory0(long j2);

    private static native void getThreadAllocatedMemory1(long[] jArr, long[] jArr2);

    private static native void setThreadCpuTimeEnabled0(boolean z2);

    private static native void setThreadAllocatedMemoryEnabled0(boolean z2);

    private static native void setThreadContentionMonitoringEnabled0(boolean z2);

    private static native Thread[] findMonitorDeadlockedThreads0();

    private static native Thread[] findDeadlockedThreads0();

    private static native void resetPeakThreadCount0();

    private static native ThreadInfo[] dumpThreads0(long[] jArr, boolean z2, boolean z3, int i2);

    private static native void resetContentionTimes0(long j2);

    ThreadImpl(VMManagement vMManagement) {
        this.jvm = vMManagement;
        this.cpuTimeEnabled = this.jvm.isThreadCpuTimeEnabled();
        this.allocatedMemoryEnabled = this.jvm.isThreadAllocatedMemoryEnabled();
    }

    @Override // java.lang.management.ThreadMXBean
    public int getThreadCount() {
        return this.jvm.getLiveThreadCount();
    }

    @Override // java.lang.management.ThreadMXBean
    public int getPeakThreadCount() {
        return this.jvm.getPeakThreadCount();
    }

    @Override // java.lang.management.ThreadMXBean
    public long getTotalStartedThreadCount() {
        return this.jvm.getTotalThreadCount();
    }

    @Override // java.lang.management.ThreadMXBean
    public int getDaemonThreadCount() {
        return this.jvm.getDaemonThreadCount();
    }

    @Override // java.lang.management.ThreadMXBean
    public boolean isThreadContentionMonitoringSupported() {
        return this.jvm.isThreadContentionMonitoringSupported();
    }

    @Override // java.lang.management.ThreadMXBean
    public synchronized boolean isThreadContentionMonitoringEnabled() {
        if (!isThreadContentionMonitoringSupported()) {
            throw new UnsupportedOperationException("Thread contention monitoring is not supported.");
        }
        return this.contentionMonitoringEnabled;
    }

    @Override // java.lang.management.ThreadMXBean
    public boolean isThreadCpuTimeSupported() {
        return this.jvm.isOtherThreadCpuTimeSupported();
    }

    @Override // java.lang.management.ThreadMXBean
    public boolean isCurrentThreadCpuTimeSupported() {
        return this.jvm.isCurrentThreadCpuTimeSupported();
    }

    @Override // com.sun.management.ThreadMXBean
    public boolean isThreadAllocatedMemorySupported() {
        return this.jvm.isThreadAllocatedMemorySupported();
    }

    @Override // java.lang.management.ThreadMXBean
    public boolean isThreadCpuTimeEnabled() {
        if (!isThreadCpuTimeSupported() && !isCurrentThreadCpuTimeSupported()) {
            throw new UnsupportedOperationException("Thread CPU time measurement is not supported");
        }
        return this.cpuTimeEnabled;
    }

    private void ensureThreadAllocatedMemorySupported() {
        if (!isThreadAllocatedMemorySupported()) {
            throw new UnsupportedOperationException("Thread allocated memory measurement is not supported.");
        }
    }

    @Override // com.sun.management.ThreadMXBean
    public boolean isThreadAllocatedMemoryEnabled() {
        ensureThreadAllocatedMemorySupported();
        return this.allocatedMemoryEnabled;
    }

    @Override // java.lang.management.ThreadMXBean
    public long[] getAllThreadIds() throws SecurityException {
        Util.checkMonitorAccess();
        Thread[] threads = getThreads();
        int length = threads.length;
        long[] jArr = new long[length];
        for (int i2 = 0; i2 < length; i2++) {
            jArr[i2] = threads[i2].getId();
        }
        return jArr;
    }

    @Override // java.lang.management.ThreadMXBean
    public ThreadInfo getThreadInfo(long j2) {
        return getThreadInfo(new long[]{j2}, 0)[0];
    }

    @Override // java.lang.management.ThreadMXBean
    public ThreadInfo getThreadInfo(long j2, int i2) {
        return getThreadInfo(new long[]{j2}, i2)[0];
    }

    @Override // java.lang.management.ThreadMXBean
    public ThreadInfo[] getThreadInfo(long[] jArr) {
        return getThreadInfo(jArr, 0);
    }

    private void verifyThreadId(long j2) {
        if (j2 <= 0) {
            throw new IllegalArgumentException("Invalid thread ID parameter: " + j2);
        }
    }

    private void verifyThreadIds(long[] jArr) {
        Objects.requireNonNull(jArr);
        for (long j2 : jArr) {
            verifyThreadId(j2);
        }
    }

    @Override // java.lang.management.ThreadMXBean
    public ThreadInfo[] getThreadInfo(long[] jArr, int i2) throws SecurityException {
        verifyThreadIds(jArr);
        if (i2 < 0) {
            throw new IllegalArgumentException("Invalid maxDepth parameter: " + i2);
        }
        if (jArr.length == 0) {
            return new ThreadInfo[0];
        }
        Util.checkMonitorAccess();
        ThreadInfo[] threadInfoArr = new ThreadInfo[jArr.length];
        if (i2 == Integer.MAX_VALUE) {
            getThreadInfo1(jArr, -1, threadInfoArr);
        } else {
            getThreadInfo1(jArr, i2, threadInfoArr);
        }
        return threadInfoArr;
    }

    @Override // java.lang.management.ThreadMXBean
    public void setThreadContentionMonitoringEnabled(boolean z2) throws SecurityException {
        if (!isThreadContentionMonitoringSupported()) {
            throw new UnsupportedOperationException("Thread contention monitoring is not supported");
        }
        Util.checkControlAccess();
        synchronized (this) {
            if (this.contentionMonitoringEnabled != z2) {
                if (z2) {
                    resetContentionTimes0(0L);
                }
                setThreadContentionMonitoringEnabled0(z2);
                this.contentionMonitoringEnabled = z2;
            }
        }
    }

    private boolean verifyCurrentThreadCpuTime() {
        if (!isCurrentThreadCpuTimeSupported()) {
            throw new UnsupportedOperationException("Current thread CPU time measurement is not supported.");
        }
        return isThreadCpuTimeEnabled();
    }

    @Override // java.lang.management.ThreadMXBean
    public long getCurrentThreadCpuTime() {
        if (verifyCurrentThreadCpuTime()) {
            return getThreadTotalCpuTime0(0L);
        }
        return -1L;
    }

    @Override // java.lang.management.ThreadMXBean
    public long getThreadCpuTime(long j2) {
        return getThreadCpuTime(new long[]{j2})[0];
    }

    private boolean verifyThreadCpuTime(long[] jArr) {
        verifyThreadIds(jArr);
        if (!isThreadCpuTimeSupported() && !isCurrentThreadCpuTimeSupported()) {
            throw new UnsupportedOperationException("Thread CPU time measurement is not supported.");
        }
        if (!isThreadCpuTimeSupported()) {
            for (long j2 : jArr) {
                if (j2 != Thread.currentThread().getId()) {
                    throw new UnsupportedOperationException("Thread CPU time measurement is only supported for the current thread.");
                }
            }
        }
        return isThreadCpuTimeEnabled();
    }

    @Override // com.sun.management.ThreadMXBean
    public long[] getThreadCpuTime(long[] jArr) {
        boolean zVerifyThreadCpuTime = verifyThreadCpuTime(jArr);
        int length = jArr.length;
        long[] jArr2 = new long[length];
        Arrays.fill(jArr2, -1L);
        if (zVerifyThreadCpuTime) {
            if (length == 1) {
                long j2 = jArr[0];
                if (j2 == Thread.currentThread().getId()) {
                    j2 = 0;
                }
                jArr2[0] = getThreadTotalCpuTime0(j2);
            } else {
                getThreadTotalCpuTime1(jArr, jArr2);
            }
        }
        return jArr2;
    }

    @Override // java.lang.management.ThreadMXBean
    public long getCurrentThreadUserTime() {
        if (verifyCurrentThreadCpuTime()) {
            return getThreadUserCpuTime0(0L);
        }
        return -1L;
    }

    @Override // java.lang.management.ThreadMXBean
    public long getThreadUserTime(long j2) {
        return getThreadUserTime(new long[]{j2})[0];
    }

    @Override // com.sun.management.ThreadMXBean
    public long[] getThreadUserTime(long[] jArr) {
        boolean zVerifyThreadCpuTime = verifyThreadCpuTime(jArr);
        int length = jArr.length;
        long[] jArr2 = new long[length];
        Arrays.fill(jArr2, -1L);
        if (zVerifyThreadCpuTime) {
            if (length == 1) {
                long j2 = jArr[0];
                if (j2 == Thread.currentThread().getId()) {
                    j2 = 0;
                }
                jArr2[0] = getThreadUserCpuTime0(j2);
            } else {
                getThreadUserCpuTime1(jArr, jArr2);
            }
        }
        return jArr2;
    }

    @Override // java.lang.management.ThreadMXBean
    public void setThreadCpuTimeEnabled(boolean z2) throws SecurityException {
        if (!isThreadCpuTimeSupported() && !isCurrentThreadCpuTimeSupported()) {
            throw new UnsupportedOperationException("Thread CPU time measurement is not supported");
        }
        Util.checkControlAccess();
        synchronized (this) {
            if (this.cpuTimeEnabled != z2) {
                setThreadCpuTimeEnabled0(z2);
                this.cpuTimeEnabled = z2;
            }
        }
    }

    @Override // com.sun.management.ThreadMXBean
    public long getCurrentThreadAllocatedBytes() {
        if (isThreadAllocatedMemoryEnabled()) {
            return getThreadAllocatedMemory0(0L);
        }
        return -1L;
    }

    private boolean verifyThreadAllocatedMemory(long j2) {
        verifyThreadId(j2);
        return isThreadAllocatedMemoryEnabled();
    }

    @Override // com.sun.management.ThreadMXBean
    public long getThreadAllocatedBytes(long j2) {
        if (verifyThreadAllocatedMemory(j2)) {
            return getThreadAllocatedMemory0(Thread.currentThread().getId() == j2 ? 0L : j2);
        }
        return -1L;
    }

    private boolean verifyThreadAllocatedMemory(long[] jArr) {
        verifyThreadIds(jArr);
        return isThreadAllocatedMemoryEnabled();
    }

    @Override // com.sun.management.ThreadMXBean
    public long[] getThreadAllocatedBytes(long[] jArr) {
        Objects.requireNonNull(jArr);
        if (jArr.length == 1) {
            return new long[]{getThreadAllocatedBytes(jArr[0])};
        }
        boolean zVerifyThreadAllocatedMemory = verifyThreadAllocatedMemory(jArr);
        long[] jArr2 = new long[jArr.length];
        Arrays.fill(jArr2, -1L);
        if (zVerifyThreadAllocatedMemory) {
            getThreadAllocatedMemory1(jArr, jArr2);
        }
        return jArr2;
    }

    @Override // com.sun.management.ThreadMXBean
    public void setThreadAllocatedMemoryEnabled(boolean z2) throws SecurityException {
        ensureThreadAllocatedMemorySupported();
        Util.checkControlAccess();
        synchronized (this) {
            if (this.allocatedMemoryEnabled != z2) {
                setThreadAllocatedMemoryEnabled0(z2);
                this.allocatedMemoryEnabled = z2;
            }
        }
    }

    @Override // java.lang.management.ThreadMXBean
    public long[] findMonitorDeadlockedThreads() throws SecurityException {
        Util.checkMonitorAccess();
        Thread[] threadArrFindMonitorDeadlockedThreads0 = findMonitorDeadlockedThreads0();
        if (threadArrFindMonitorDeadlockedThreads0 == null) {
            return null;
        }
        long[] jArr = new long[threadArrFindMonitorDeadlockedThreads0.length];
        for (int i2 = 0; i2 < threadArrFindMonitorDeadlockedThreads0.length; i2++) {
            jArr[i2] = threadArrFindMonitorDeadlockedThreads0[i2].getId();
        }
        return jArr;
    }

    @Override // java.lang.management.ThreadMXBean
    public long[] findDeadlockedThreads() throws SecurityException {
        if (!isSynchronizerUsageSupported()) {
            throw new UnsupportedOperationException("Monitoring of Synchronizer Usage is not supported.");
        }
        Util.checkMonitorAccess();
        Thread[] threadArrFindDeadlockedThreads0 = findDeadlockedThreads0();
        if (threadArrFindDeadlockedThreads0 == null) {
            return null;
        }
        long[] jArr = new long[threadArrFindDeadlockedThreads0.length];
        for (int i2 = 0; i2 < threadArrFindDeadlockedThreads0.length; i2++) {
            jArr[i2] = threadArrFindDeadlockedThreads0[i2].getId();
        }
        return jArr;
    }

    @Override // java.lang.management.ThreadMXBean
    public void resetPeakThreadCount() throws SecurityException {
        Util.checkControlAccess();
        resetPeakThreadCount0();
    }

    @Override // java.lang.management.ThreadMXBean
    public boolean isObjectMonitorUsageSupported() {
        return this.jvm.isObjectMonitorUsageSupported();
    }

    @Override // java.lang.management.ThreadMXBean
    public boolean isSynchronizerUsageSupported() {
        return this.jvm.isSynchronizerUsageSupported();
    }

    private void verifyDumpThreads(boolean z2, boolean z3) throws SecurityException {
        if (z2 && !isObjectMonitorUsageSupported()) {
            throw new UnsupportedOperationException("Monitoring of Object Monitor Usage is not supported.");
        }
        if (z3 && !isSynchronizerUsageSupported()) {
            throw new UnsupportedOperationException("Monitoring of Synchronizer Usage is not supported.");
        }
        Util.checkMonitorAccess();
    }

    @Override // java.lang.management.ThreadMXBean
    public ThreadInfo[] getThreadInfo(long[] jArr, boolean z2, boolean z3) {
        return dumpThreads0(jArr, z2, z3, Integer.MAX_VALUE);
    }

    @Override // com.sun.management.ThreadMXBean
    public ThreadInfo[] getThreadInfo(long[] jArr, boolean z2, boolean z3, int i2) throws SecurityException {
        if (i2 < 0) {
            throw new IllegalArgumentException("Invalid maxDepth parameter: " + i2);
        }
        verifyThreadIds(jArr);
        if (jArr.length == 0) {
            return new ThreadInfo[0];
        }
        verifyDumpThreads(z2, z3);
        return dumpThreads0(jArr, z2, z3, i2);
    }

    @Override // java.lang.management.ThreadMXBean
    public ThreadInfo[] dumpAllThreads(boolean z2, boolean z3) {
        return dumpAllThreads(z2, z3, Integer.MAX_VALUE);
    }

    @Override // com.sun.management.ThreadMXBean
    public ThreadInfo[] dumpAllThreads(boolean z2, boolean z3, int i2) throws SecurityException {
        if (i2 < 0) {
            throw new IllegalArgumentException("Invalid maxDepth parameter: " + i2);
        }
        verifyDumpThreads(z2, z3);
        return dumpThreads0(null, z2, z3, i2);
    }

    @Override // java.lang.management.PlatformManagedObject
    public ObjectName getObjectName() {
        return Util.newObjectName(java.lang.management.ManagementFactory.THREAD_MXBEAN_NAME);
    }
}
