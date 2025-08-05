package com.sun.management;

import java.lang.management.ThreadInfo;
import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/management/ThreadMXBean.class */
public interface ThreadMXBean extends java.lang.management.ThreadMXBean {
    long[] getThreadCpuTime(long[] jArr);

    long[] getThreadUserTime(long[] jArr);

    long getThreadAllocatedBytes(long j2);

    long[] getThreadAllocatedBytes(long[] jArr);

    boolean isThreadAllocatedMemorySupported();

    boolean isThreadAllocatedMemoryEnabled();

    void setThreadAllocatedMemoryEnabled(boolean z2);

    default long getCurrentThreadAllocatedBytes() {
        return getThreadAllocatedBytes(Thread.currentThread().getId());
    }

    default ThreadInfo[] getThreadInfo(long[] jArr, boolean z2, boolean z3, int i2) {
        throw new UnsupportedOperationException();
    }

    default ThreadInfo[] dumpAllThreads(boolean z2, boolean z3, int i2) {
        throw new UnsupportedOperationException();
    }
}
