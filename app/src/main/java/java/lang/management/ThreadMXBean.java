package java.lang.management;

/* loaded from: rt.jar:java/lang/management/ThreadMXBean.class */
public interface ThreadMXBean extends PlatformManagedObject {
    int getThreadCount();

    int getPeakThreadCount();

    long getTotalStartedThreadCount();

    int getDaemonThreadCount();

    long[] getAllThreadIds();

    ThreadInfo getThreadInfo(long j2);

    ThreadInfo[] getThreadInfo(long[] jArr);

    ThreadInfo getThreadInfo(long j2, int i2);

    ThreadInfo[] getThreadInfo(long[] jArr, int i2);

    boolean isThreadContentionMonitoringSupported();

    boolean isThreadContentionMonitoringEnabled();

    void setThreadContentionMonitoringEnabled(boolean z2);

    long getCurrentThreadCpuTime();

    long getCurrentThreadUserTime();

    long getThreadCpuTime(long j2);

    long getThreadUserTime(long j2);

    boolean isThreadCpuTimeSupported();

    boolean isCurrentThreadCpuTimeSupported();

    boolean isThreadCpuTimeEnabled();

    void setThreadCpuTimeEnabled(boolean z2);

    long[] findMonitorDeadlockedThreads();

    void resetPeakThreadCount();

    long[] findDeadlockedThreads();

    boolean isObjectMonitorUsageSupported();

    boolean isSynchronizerUsageSupported();

    ThreadInfo[] getThreadInfo(long[] jArr, boolean z2, boolean z3);

    ThreadInfo[] dumpAllThreads(boolean z2, boolean z3);
}
