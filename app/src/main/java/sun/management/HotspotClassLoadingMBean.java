package sun.management;

import java.util.List;
import sun.management.counter.Counter;

/* loaded from: rt.jar:sun/management/HotspotClassLoadingMBean.class */
public interface HotspotClassLoadingMBean {
    long getLoadedClassSize();

    long getUnloadedClassSize();

    long getClassLoadingTime();

    long getMethodDataSize();

    long getInitializedClassCount();

    long getClassInitializationTime();

    long getClassVerificationTime();

    List<Counter> getInternalClassLoadingCounters();
}
