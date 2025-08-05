package com.sun.management;

import java.io.IOException;
import java.lang.management.PlatformManagedObject;
import java.util.List;
import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/management/HotSpotDiagnosticMXBean.class */
public interface HotSpotDiagnosticMXBean extends PlatformManagedObject {
    void dumpHeap(String str, boolean z2) throws IOException;

    List<VMOption> getDiagnosticOptions();

    VMOption getVMOption(String str);

    void setVMOption(String str, String str2);
}
