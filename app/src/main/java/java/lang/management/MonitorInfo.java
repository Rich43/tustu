package java.lang.management;

import javax.management.openmbean.CompositeData;
import sun.management.MonitorInfoCompositeData;

/* loaded from: rt.jar:java/lang/management/MonitorInfo.class */
public class MonitorInfo extends LockInfo {
    private int stackDepth;
    private StackTraceElement stackFrame;

    public MonitorInfo(String str, int i2, int i3, StackTraceElement stackTraceElement) {
        super(str, i2);
        if (i3 >= 0 && stackTraceElement == null) {
            throw new IllegalArgumentException("Parameter stackDepth is " + i3 + " but stackFrame is null");
        }
        if (i3 < 0 && stackTraceElement != null) {
            throw new IllegalArgumentException("Parameter stackDepth is " + i3 + " but stackFrame is not null");
        }
        this.stackDepth = i3;
        this.stackFrame = stackTraceElement;
    }

    public int getLockedStackDepth() {
        return this.stackDepth;
    }

    public StackTraceElement getLockedStackFrame() {
        return this.stackFrame;
    }

    public static MonitorInfo from(CompositeData compositeData) {
        if (compositeData == null) {
            return null;
        }
        if (compositeData instanceof MonitorInfoCompositeData) {
            return ((MonitorInfoCompositeData) compositeData).getMonitorInfo();
        }
        MonitorInfoCompositeData.validateCompositeData(compositeData);
        return new MonitorInfo(MonitorInfoCompositeData.getClassName(compositeData), MonitorInfoCompositeData.getIdentityHashCode(compositeData), MonitorInfoCompositeData.getLockedStackDepth(compositeData), MonitorInfoCompositeData.getLockedStackFrame(compositeData));
    }
}
