package javax.management.monitor;

import javax.management.ObjectName;

/* loaded from: rt.jar:javax/management/monitor/CounterMonitorMBean.class */
public interface CounterMonitorMBean extends MonitorMBean {
    @Deprecated
    Number getDerivedGauge();

    @Deprecated
    long getDerivedGaugeTimeStamp();

    @Deprecated
    Number getThreshold();

    @Deprecated
    void setThreshold(Number number) throws IllegalArgumentException;

    Number getDerivedGauge(ObjectName objectName);

    long getDerivedGaugeTimeStamp(ObjectName objectName);

    Number getThreshold(ObjectName objectName);

    Number getInitThreshold();

    void setInitThreshold(Number number) throws IllegalArgumentException;

    Number getOffset();

    void setOffset(Number number) throws IllegalArgumentException;

    Number getModulus();

    void setModulus(Number number) throws IllegalArgumentException;

    boolean getNotify();

    void setNotify(boolean z2);

    boolean getDifferenceMode();

    void setDifferenceMode(boolean z2);
}
