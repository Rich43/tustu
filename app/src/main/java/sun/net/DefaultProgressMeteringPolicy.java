package sun.net;

import java.net.URL;

/* compiled from: ProgressMonitor.java */
/* loaded from: rt.jar:sun/net/DefaultProgressMeteringPolicy.class */
class DefaultProgressMeteringPolicy implements ProgressMeteringPolicy {
    DefaultProgressMeteringPolicy() {
    }

    @Override // sun.net.ProgressMeteringPolicy
    public boolean shouldMeterInput(URL url, String str) {
        return false;
    }

    @Override // sun.net.ProgressMeteringPolicy
    public int getProgressUpdateThreshold() {
        return 8192;
    }
}
