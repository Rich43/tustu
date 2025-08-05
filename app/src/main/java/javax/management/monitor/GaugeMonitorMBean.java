package javax.management.monitor;

import javax.management.ObjectName;

/* loaded from: rt.jar:javax/management/monitor/GaugeMonitorMBean.class */
public interface GaugeMonitorMBean extends MonitorMBean {
    @Deprecated
    Number getDerivedGauge();

    @Deprecated
    long getDerivedGaugeTimeStamp();

    Number getDerivedGauge(ObjectName objectName);

    long getDerivedGaugeTimeStamp(ObjectName objectName);

    Number getHighThreshold();

    Number getLowThreshold();

    void setThresholds(Number number, Number number2) throws IllegalArgumentException;

    boolean getNotifyHigh();

    void setNotifyHigh(boolean z2);

    boolean getNotifyLow();

    void setNotifyLow(boolean z2);

    boolean getDifferenceMode();

    void setDifferenceMode(boolean z2);
}
