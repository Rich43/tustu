package sun.management;

import java.lang.management.MemoryUsage;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;

/* loaded from: rt.jar:sun/management/MemoryUsageCompositeData.class */
public class MemoryUsageCompositeData extends LazyCompositeData {
    private final MemoryUsage usage;
    private static final CompositeType memoryUsageCompositeType;
    private static final String INIT = "init";
    private static final String USED = "used";
    private static final String COMMITTED = "committed";
    private static final String MAX = "max";
    private static final String[] memoryUsageItemNames;
    private static final long serialVersionUID = -8504291541083874143L;

    private MemoryUsageCompositeData(MemoryUsage memoryUsage) {
        this.usage = memoryUsage;
    }

    public MemoryUsage getMemoryUsage() {
        return this.usage;
    }

    public static CompositeData toCompositeData(MemoryUsage memoryUsage) {
        return new MemoryUsageCompositeData(memoryUsage).getCompositeData();
    }

    @Override // sun.management.LazyCompositeData
    protected CompositeData getCompositeData() {
        try {
            return new CompositeDataSupport(memoryUsageCompositeType, memoryUsageItemNames, new Object[]{new Long(this.usage.getInit()), new Long(this.usage.getUsed()), new Long(this.usage.getCommitted()), new Long(this.usage.getMax())});
        } catch (OpenDataException e2) {
            throw new AssertionError(e2);
        }
    }

    static {
        try {
            memoryUsageCompositeType = (CompositeType) MappedMXBeanType.toOpenType(MemoryUsage.class);
            memoryUsageItemNames = new String[]{INIT, USED, COMMITTED, MAX};
        } catch (OpenDataException e2) {
            throw new AssertionError(e2);
        }
    }

    static CompositeType getMemoryUsageCompositeType() {
        return memoryUsageCompositeType;
    }

    public static long getInit(CompositeData compositeData) {
        return getLong(compositeData, INIT);
    }

    public static long getUsed(CompositeData compositeData) {
        return getLong(compositeData, USED);
    }

    public static long getCommitted(CompositeData compositeData) {
        return getLong(compositeData, COMMITTED);
    }

    public static long getMax(CompositeData compositeData) {
        return getLong(compositeData, MAX);
    }

    public static void validateCompositeData(CompositeData compositeData) {
        if (compositeData == null) {
            throw new NullPointerException("Null CompositeData");
        }
        if (!isTypeMatched(memoryUsageCompositeType, compositeData.getCompositeType())) {
            throw new IllegalArgumentException("Unexpected composite type for MemoryUsage");
        }
    }
}
