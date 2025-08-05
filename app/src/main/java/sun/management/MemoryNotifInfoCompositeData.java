package sun.management;

import java.lang.management.MemoryNotificationInfo;
import java.lang.management.MemoryUsage;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;

/* loaded from: rt.jar:sun/management/MemoryNotifInfoCompositeData.class */
public class MemoryNotifInfoCompositeData extends LazyCompositeData {
    private final MemoryNotificationInfo memoryNotifInfo;
    private static final CompositeType memoryNotifInfoCompositeType;
    private static final String POOL_NAME = "poolName";
    private static final String USAGE = "usage";
    private static final String COUNT = "count";
    private static final String[] memoryNotifInfoItemNames;
    private static final long serialVersionUID = -1805123446483771291L;

    private MemoryNotifInfoCompositeData(MemoryNotificationInfo memoryNotificationInfo) {
        this.memoryNotifInfo = memoryNotificationInfo;
    }

    public MemoryNotificationInfo getMemoryNotifInfo() {
        return this.memoryNotifInfo;
    }

    public static CompositeData toCompositeData(MemoryNotificationInfo memoryNotificationInfo) {
        return new MemoryNotifInfoCompositeData(memoryNotificationInfo).getCompositeData();
    }

    @Override // sun.management.LazyCompositeData
    protected CompositeData getCompositeData() {
        try {
            return new CompositeDataSupport(memoryNotifInfoCompositeType, memoryNotifInfoItemNames, new Object[]{this.memoryNotifInfo.getPoolName(), MemoryUsageCompositeData.toCompositeData(this.memoryNotifInfo.getUsage()), new Long(this.memoryNotifInfo.getCount())});
        } catch (OpenDataException e2) {
            throw new AssertionError(e2);
        }
    }

    static {
        try {
            memoryNotifInfoCompositeType = (CompositeType) MappedMXBeanType.toOpenType(MemoryNotificationInfo.class);
            memoryNotifInfoItemNames = new String[]{POOL_NAME, USAGE, "count"};
        } catch (OpenDataException e2) {
            throw new AssertionError(e2);
        }
    }

    public static String getPoolName(CompositeData compositeData) {
        String string = getString(compositeData, POOL_NAME);
        if (string == null) {
            throw new IllegalArgumentException("Invalid composite data: Attribute poolName has null value");
        }
        return string;
    }

    public static MemoryUsage getUsage(CompositeData compositeData) {
        return MemoryUsage.from((CompositeData) compositeData.get(USAGE));
    }

    public static long getCount(CompositeData compositeData) {
        return getLong(compositeData, "count");
    }

    public static void validateCompositeData(CompositeData compositeData) {
        if (compositeData == null) {
            throw new NullPointerException("Null CompositeData");
        }
        if (!isTypeMatched(memoryNotifInfoCompositeType, compositeData.getCompositeType())) {
            throw new IllegalArgumentException("Unexpected composite type for MemoryNotificationInfo");
        }
    }
}
