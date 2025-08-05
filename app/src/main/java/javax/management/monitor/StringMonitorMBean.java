package javax.management.monitor;

import javax.management.ObjectName;

/* loaded from: rt.jar:javax/management/monitor/StringMonitorMBean.class */
public interface StringMonitorMBean extends MonitorMBean {
    @Deprecated
    String getDerivedGauge();

    @Deprecated
    long getDerivedGaugeTimeStamp();

    String getDerivedGauge(ObjectName objectName);

    long getDerivedGaugeTimeStamp(ObjectName objectName);

    String getStringToCompare();

    void setStringToCompare(String str) throws IllegalArgumentException;

    boolean getNotifyMatch();

    void setNotifyMatch(boolean z2);

    boolean getNotifyDiffer();

    void setNotifyDiffer(boolean z2);
}
