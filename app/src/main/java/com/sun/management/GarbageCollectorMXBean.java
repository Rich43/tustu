package com.sun.management;

import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/management/GarbageCollectorMXBean.class */
public interface GarbageCollectorMXBean extends java.lang.management.GarbageCollectorMXBean {
    GcInfo getLastGcInfo();
}
