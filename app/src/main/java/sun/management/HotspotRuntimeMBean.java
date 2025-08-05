package sun.management;

import java.util.List;
import sun.management.counter.Counter;

/* loaded from: rt.jar:sun/management/HotspotRuntimeMBean.class */
public interface HotspotRuntimeMBean {
    long getSafepointCount();

    long getTotalSafepointTime();

    long getSafepointSyncTime();

    List<Counter> getInternalRuntimeCounters();
}
