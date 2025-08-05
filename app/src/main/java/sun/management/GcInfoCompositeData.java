package sun.management;

import com.sun.management.GcInfo;
import java.io.InvalidObjectException;
import java.lang.management.MemoryUsage;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularData;

/* loaded from: rt.jar:sun/management/GcInfoCompositeData.class */
public class GcInfoCompositeData extends LazyCompositeData {
    private final GcInfo info;
    private final GcInfoBuilder builder;
    private final Object[] gcExtItemValues;
    private static final String ID = "id";
    private static final String DURATION = "duration";
    private static MappedMXBeanType memoryUsageMapType;
    private static OpenType[] baseGcInfoItemTypes;
    private static CompositeType baseGcInfoCompositeType;
    private static final long serialVersionUID = -5716428894085882742L;
    private static final String START_TIME = "startTime";
    private static final String END_TIME = "endTime";
    private static final String MEMORY_USAGE_BEFORE_GC = "memoryUsageBeforeGc";
    private static final String MEMORY_USAGE_AFTER_GC = "memoryUsageAfterGc";
    private static final String[] baseGcInfoItemNames = {"id", START_TIME, END_TIME, "duration", MEMORY_USAGE_BEFORE_GC, MEMORY_USAGE_AFTER_GC};

    public GcInfoCompositeData(GcInfo gcInfo, GcInfoBuilder gcInfoBuilder, Object[] objArr) {
        this.info = gcInfo;
        this.builder = gcInfoBuilder;
        this.gcExtItemValues = objArr;
    }

    public GcInfo getGcInfo() {
        return this.info;
    }

    public static CompositeData toCompositeData(final GcInfo gcInfo) {
        return new GcInfoCompositeData(gcInfo, (GcInfoBuilder) AccessController.doPrivileged(new PrivilegedAction<GcInfoBuilder>() { // from class: sun.management.GcInfoCompositeData.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public GcInfoBuilder run2() {
                try {
                    Field declaredField = Class.forName("com.sun.management.GcInfo").getDeclaredField("builder");
                    declaredField.setAccessible(true);
                    return (GcInfoBuilder) declaredField.get(gcInfo);
                } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e2) {
                    return null;
                }
            }
        }), (Object[]) AccessController.doPrivileged(new PrivilegedAction<Object[]>() { // from class: sun.management.GcInfoCompositeData.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object[] run2() {
                try {
                    Field declaredField = Class.forName("com.sun.management.GcInfo").getDeclaredField("extAttributes");
                    declaredField.setAccessible(true);
                    return (Object[]) declaredField.get(gcInfo);
                } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e2) {
                    return null;
                }
            }
        })).getCompositeData();
    }

    @Override // sun.management.LazyCompositeData
    protected CompositeData getCompositeData() {
        try {
            Object[] objArr = {new Long(this.info.getId()), new Long(this.info.getStartTime()), new Long(this.info.getEndTime()), new Long(this.info.getDuration()), memoryUsageMapType.toOpenTypeData(this.info.getMemoryUsageBeforeGc()), memoryUsageMapType.toOpenTypeData(this.info.getMemoryUsageAfterGc())};
            int gcExtItemCount = this.builder.getGcExtItemCount();
            if (gcExtItemCount == 0 && this.gcExtItemValues != null && this.gcExtItemValues.length != 0) {
                throw new AssertionError((Object) "Unexpected Gc Extension Item Values");
            }
            if (gcExtItemCount > 0 && (this.gcExtItemValues == null || gcExtItemCount != this.gcExtItemValues.length)) {
                throw new AssertionError((Object) "Unmatched Gc Extension Item Values");
            }
            Object[] objArr2 = new Object[objArr.length + gcExtItemCount];
            System.arraycopy(objArr, 0, objArr2, 0, objArr.length);
            if (gcExtItemCount > 0) {
                System.arraycopy(this.gcExtItemValues, 0, objArr2, objArr.length, gcExtItemCount);
            }
            try {
                return new CompositeDataSupport(this.builder.getGcInfoCompositeType(), this.builder.getItemNames(), objArr2);
            } catch (OpenDataException e2) {
                throw new AssertionError(e2);
            }
        } catch (OpenDataException e3) {
            throw new AssertionError(e3);
        }
    }

    static {
        try {
            memoryUsageMapType = MappedMXBeanType.getMappedType(GcInfo.class.getMethod("getMemoryUsageBeforeGc", new Class[0]).getGenericReturnType());
            baseGcInfoItemTypes = null;
            baseGcInfoCompositeType = null;
        } catch (NoSuchMethodException | OpenDataException e2) {
            throw new AssertionError(e2);
        }
    }

    static String[] getBaseGcInfoItemNames() {
        return baseGcInfoItemNames;
    }

    static synchronized OpenType[] getBaseGcInfoItemTypes() {
        if (baseGcInfoItemTypes == null) {
            OpenType<?> openType = memoryUsageMapType.getOpenType();
            baseGcInfoItemTypes = new OpenType[]{SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, openType, openType};
        }
        return baseGcInfoItemTypes;
    }

    public static long getId(CompositeData compositeData) {
        return getLong(compositeData, "id");
    }

    public static long getStartTime(CompositeData compositeData) {
        return getLong(compositeData, START_TIME);
    }

    public static long getEndTime(CompositeData compositeData) {
        return getLong(compositeData, END_TIME);
    }

    public static Map<String, MemoryUsage> getMemoryUsageBeforeGc(CompositeData compositeData) {
        try {
            return cast(memoryUsageMapType.toJavaTypeData((TabularData) compositeData.get(MEMORY_USAGE_BEFORE_GC)));
        } catch (InvalidObjectException | OpenDataException e2) {
            throw new AssertionError(e2);
        }
    }

    public static Map<String, MemoryUsage> cast(Object obj) {
        return (Map) obj;
    }

    public static Map<String, MemoryUsage> getMemoryUsageAfterGc(CompositeData compositeData) {
        try {
            return cast(memoryUsageMapType.toJavaTypeData((TabularData) compositeData.get(MEMORY_USAGE_AFTER_GC)));
        } catch (InvalidObjectException | OpenDataException e2) {
            throw new AssertionError(e2);
        }
    }

    public static void validateCompositeData(CompositeData compositeData) {
        if (compositeData == null) {
            throw new NullPointerException("Null CompositeData");
        }
        if (!isTypeMatched(getBaseGcInfoCompositeType(), compositeData.getCompositeType())) {
            throw new IllegalArgumentException("Unexpected composite type for GcInfo");
        }
    }

    static synchronized CompositeType getBaseGcInfoCompositeType() {
        if (baseGcInfoCompositeType == null) {
            try {
                baseGcInfoCompositeType = new CompositeType("sun.management.BaseGcInfoCompositeType", "CompositeType for Base GcInfo", getBaseGcInfoItemNames(), getBaseGcInfoItemNames(), getBaseGcInfoItemTypes());
            } catch (OpenDataException e2) {
                throw Util.newException(e2);
            }
        }
        return baseGcInfoCompositeType;
    }
}
