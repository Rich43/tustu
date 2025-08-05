package sun.management;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryPoolMXBean;

/* loaded from: rt.jar:sun/management/ManagementFactory.class */
class ManagementFactory {
    private ManagementFactory() {
    }

    private static MemoryPoolMXBean createMemoryPool(String str, boolean z2, long j2, long j3) {
        return new MemoryPoolImpl(str, z2, j2, j3);
    }

    private static MemoryManagerMXBean createMemoryManager(String str) {
        return new MemoryManagerImpl(str);
    }

    private static GarbageCollectorMXBean createGarbageCollector(String str, String str2) {
        return new GarbageCollectorImpl(str);
    }
}
