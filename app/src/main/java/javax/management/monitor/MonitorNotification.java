package javax.management.monitor;

import javax.management.Notification;
import javax.management.ObjectName;

/* loaded from: rt.jar:javax/management/monitor/MonitorNotification.class */
public class MonitorNotification extends Notification {
    public static final String OBSERVED_OBJECT_ERROR = "jmx.monitor.error.mbean";
    public static final String OBSERVED_ATTRIBUTE_ERROR = "jmx.monitor.error.attribute";
    public static final String OBSERVED_ATTRIBUTE_TYPE_ERROR = "jmx.monitor.error.type";
    public static final String THRESHOLD_ERROR = "jmx.monitor.error.threshold";
    public static final String RUNTIME_ERROR = "jmx.monitor.error.runtime";
    public static final String THRESHOLD_VALUE_EXCEEDED = "jmx.monitor.counter.threshold";
    public static final String THRESHOLD_HIGH_VALUE_EXCEEDED = "jmx.monitor.gauge.high";
    public static final String THRESHOLD_LOW_VALUE_EXCEEDED = "jmx.monitor.gauge.low";
    public static final String STRING_TO_COMPARE_VALUE_MATCHED = "jmx.monitor.string.matches";
    public static final String STRING_TO_COMPARE_VALUE_DIFFERED = "jmx.monitor.string.differs";
    private static final long serialVersionUID = -4608189663661929204L;
    private ObjectName observedObject;
    private String observedAttribute;
    private Object derivedGauge;
    private Object trigger;

    MonitorNotification(String str, Object obj, long j2, long j3, String str2, ObjectName objectName, String str3, Object obj2, Object obj3) {
        super(str, obj, j2, j3, str2);
        this.observedObject = null;
        this.observedAttribute = null;
        this.derivedGauge = null;
        this.trigger = null;
        this.observedObject = objectName;
        this.observedAttribute = str3;
        this.derivedGauge = obj2;
        this.trigger = obj3;
    }

    public ObjectName getObservedObject() {
        return this.observedObject;
    }

    public String getObservedAttribute() {
        return this.observedAttribute;
    }

    public Object getDerivedGauge() {
        return this.derivedGauge;
    }

    public Object getTrigger() {
        return this.trigger;
    }
}
