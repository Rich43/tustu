package sun.management;

import java.util.List;
import sun.management.counter.Counter;

/* loaded from: rt.jar:sun/management/HotspotMemoryMBean.class */
public interface HotspotMemoryMBean {
    List<Counter> getInternalMemoryCounters();
}
