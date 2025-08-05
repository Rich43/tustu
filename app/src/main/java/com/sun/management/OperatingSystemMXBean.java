package com.sun.management;

import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/management/OperatingSystemMXBean.class */
public interface OperatingSystemMXBean extends java.lang.management.OperatingSystemMXBean {
    long getCommittedVirtualMemorySize();

    long getTotalSwapSpaceSize();

    long getFreeSwapSpaceSize();

    long getProcessCpuTime();

    long getFreePhysicalMemorySize();

    long getTotalPhysicalMemorySize();

    double getSystemCpuLoad();

    double getProcessCpuLoad();
}
