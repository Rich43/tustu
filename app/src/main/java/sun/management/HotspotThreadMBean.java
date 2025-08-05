package sun.management;

import java.util.List;
import java.util.Map;
import sun.management.counter.Counter;

/* loaded from: rt.jar:sun/management/HotspotThreadMBean.class */
public interface HotspotThreadMBean {
    int getInternalThreadCount();

    Map<String, Long> getInternalThreadCpuTimes();

    List<Counter> getInternalThreadingCounters();
}
