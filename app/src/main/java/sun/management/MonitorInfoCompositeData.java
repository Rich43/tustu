package sun.management;

import java.lang.management.MonitorInfo;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;

/* loaded from: rt.jar:sun/management/MonitorInfoCompositeData.class */
public class MonitorInfoCompositeData extends LazyCompositeData {
    private final MonitorInfo lock;
    private static final CompositeType monitorInfoCompositeType;
    private static final String[] monitorInfoItemNames;
    private static final String CLASS_NAME = "className";
    private static final String IDENTITY_HASH_CODE = "identityHashCode";
    private static final String LOCKED_STACK_FRAME = "lockedStackFrame";
    private static final String LOCKED_STACK_DEPTH = "lockedStackDepth";
    private static final long serialVersionUID = -5825215591822908529L;

    private MonitorInfoCompositeData(MonitorInfo monitorInfo) {
        this.lock = monitorInfo;
    }

    public MonitorInfo getMonitorInfo() {
        return this.lock;
    }

    public static CompositeData toCompositeData(MonitorInfo monitorInfo) {
        return new MonitorInfoCompositeData(monitorInfo).getCompositeData();
    }

    @Override // sun.management.LazyCompositeData
    protected CompositeData getCompositeData() {
        int length = monitorInfoItemNames.length;
        Object[] objArr = new Object[length];
        CompositeData compositeData = LockInfoCompositeData.toCompositeData(this.lock);
        for (int i2 = 0; i2 < length; i2++) {
            String str = monitorInfoItemNames[i2];
            if (str.equals(LOCKED_STACK_FRAME)) {
                StackTraceElement lockedStackFrame = this.lock.getLockedStackFrame();
                objArr[i2] = lockedStackFrame != null ? StackTraceElementCompositeData.toCompositeData(lockedStackFrame) : null;
            } else if (str.equals(LOCKED_STACK_DEPTH)) {
                objArr[i2] = new Integer(this.lock.getLockedStackDepth());
            } else {
                objArr[i2] = compositeData.get(str);
            }
        }
        try {
            return new CompositeDataSupport(monitorInfoCompositeType, monitorInfoItemNames, objArr);
        } catch (OpenDataException e2) {
            throw new AssertionError(e2);
        }
    }

    static {
        try {
            monitorInfoCompositeType = (CompositeType) MappedMXBeanType.toOpenType(MonitorInfo.class);
            monitorInfoItemNames = (String[]) monitorInfoCompositeType.keySet().toArray(new String[0]);
        } catch (OpenDataException e2) {
            throw new AssertionError(e2);
        }
    }

    static CompositeType getMonitorInfoCompositeType() {
        return monitorInfoCompositeType;
    }

    public static String getClassName(CompositeData compositeData) {
        return getString(compositeData, CLASS_NAME);
    }

    public static int getIdentityHashCode(CompositeData compositeData) {
        return getInt(compositeData, IDENTITY_HASH_CODE);
    }

    public static StackTraceElement getLockedStackFrame(CompositeData compositeData) {
        CompositeData compositeData2 = (CompositeData) compositeData.get(LOCKED_STACK_FRAME);
        if (compositeData2 != null) {
            return StackTraceElementCompositeData.from(compositeData2);
        }
        return null;
    }

    public static int getLockedStackDepth(CompositeData compositeData) {
        return getInt(compositeData, LOCKED_STACK_DEPTH);
    }

    public static void validateCompositeData(CompositeData compositeData) {
        if (compositeData == null) {
            throw new NullPointerException("Null CompositeData");
        }
        if (!isTypeMatched(monitorInfoCompositeType, compositeData.getCompositeType())) {
            throw new IllegalArgumentException("Unexpected composite type for MonitorInfo");
        }
    }
}
