package sun.net;

import java.net.URL;

/* loaded from: rt.jar:sun/net/ProgressMeteringPolicy.class */
public interface ProgressMeteringPolicy {
    boolean shouldMeterInput(URL url, String str);

    int getProgressUpdateThreshold();
}
