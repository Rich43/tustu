package com.sun.management;

import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/management/UnixOperatingSystemMXBean.class */
public interface UnixOperatingSystemMXBean extends OperatingSystemMXBean {
    long getOpenFileDescriptorCount();

    long getMaxFileDescriptorCount();
}
