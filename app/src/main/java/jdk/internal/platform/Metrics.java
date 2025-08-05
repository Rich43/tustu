package jdk.internal.platform;

/* loaded from: rt.jar:jdk/internal/platform/Metrics.class */
public interface Metrics {
    String getProvider();

    long getCpuUsage();

    long[] getPerCpuUsage();

    long getCpuUserUsage();

    long getCpuSystemUsage();

    long getCpuPeriod();

    long getCpuQuota();

    long getCpuShares();

    long getCpuNumPeriods();

    long getCpuNumThrottled();

    long getCpuThrottledTime();

    long getEffectiveCpuCount();

    int[] getCpuSetCpus();

    int[] getEffectiveCpuSetCpus();

    int[] getCpuSetMems();

    int[] getEffectiveCpuSetMems();

    long getMemoryFailCount();

    long getMemoryLimit();

    long getMemoryUsage();

    long getTcpMemoryUsage();

    long getMemoryAndSwapLimit();

    long getMemoryAndSwapUsage();

    long getMemorySoftLimit();

    long getBlkIOServiceCount();

    long getBlkIOServiced();

    static Metrics systemMetrics() {
        return SystemMetrics.instance();
    }
}
