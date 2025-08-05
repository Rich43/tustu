package sun.tracing.dtrace;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashSet;

/* compiled from: Activation.java */
/* loaded from: rt.jar:sun/tracing/dtrace/SystemResource.class */
class SystemResource extends WeakReference<Activation> {
    private long handle;
    private static ReferenceQueue<Activation> referenceQueue;
    static HashSet<SystemResource> resources;

    static {
        ReferenceQueue<Activation> referenceQueue2 = new ReferenceQueue<>();
        referenceQueue = referenceQueue2;
        referenceQueue = referenceQueue2;
        resources = new HashSet<>();
    }

    SystemResource(Activation activation, long j2) {
        super(activation, referenceQueue);
        this.handle = j2;
        flush();
        resources.add(this);
    }

    void dispose() {
        JVM.dispose(this.handle);
        resources.remove(this);
        this.handle = 0L;
    }

    static void flush() {
        while (true) {
            SystemResource systemResource = (SystemResource) referenceQueue.poll();
            if (systemResource == null) {
                return;
            }
            if (systemResource.handle != 0) {
                systemResource.dispose();
            }
        }
    }
}
