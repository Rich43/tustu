package javax.management.monitor;

import javax.management.ObjectName;

/* loaded from: rt.jar:javax/management/monitor/MonitorMBean.class */
public interface MonitorMBean {
    void start();

    void stop();

    void addObservedObject(ObjectName objectName) throws IllegalArgumentException;

    void removeObservedObject(ObjectName objectName);

    boolean containsObservedObject(ObjectName objectName);

    ObjectName[] getObservedObjects();

    @Deprecated
    ObjectName getObservedObject();

    @Deprecated
    void setObservedObject(ObjectName objectName);

    String getObservedAttribute();

    void setObservedAttribute(String str);

    long getGranularityPeriod();

    void setGranularityPeriod(long j2) throws IllegalArgumentException;

    boolean isActive();
}
