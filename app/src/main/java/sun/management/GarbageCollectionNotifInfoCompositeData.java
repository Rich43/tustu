package sun.management;

import com.sun.management.GarbageCollectionNotificationInfo;
import com.sun.management.GcInfo;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;

/* loaded from: rt.jar:sun/management/GarbageCollectionNotifInfoCompositeData.class */
public class GarbageCollectionNotifInfoCompositeData extends LazyCompositeData {
    private final GarbageCollectionNotificationInfo gcNotifInfo;
    private static final String GC_NAME = "gcName";
    private static final String GC_ACTION = "gcAction";
    private static final String GC_CAUSE = "gcCause";
    private static final String GC_INFO = "gcInfo";
    private static final String[] gcNotifInfoItemNames = {GC_NAME, GC_ACTION, GC_CAUSE, GC_INFO};
    private static HashMap<GcInfoBuilder, CompositeType> compositeTypeByBuilder = new HashMap<>();
    private static CompositeType baseGcNotifInfoCompositeType = null;
    private static final long serialVersionUID = -1805123446483771292L;

    public GarbageCollectionNotifInfoCompositeData(GarbageCollectionNotificationInfo garbageCollectionNotificationInfo) {
        this.gcNotifInfo = garbageCollectionNotificationInfo;
    }

    public GarbageCollectionNotificationInfo getGarbageCollectionNotifInfo() {
        return this.gcNotifInfo;
    }

    public static CompositeData toCompositeData(GarbageCollectionNotificationInfo garbageCollectionNotificationInfo) {
        return new GarbageCollectionNotifInfoCompositeData(garbageCollectionNotificationInfo).getCompositeData();
    }

    private CompositeType getCompositeTypeByBuilder() {
        CompositeType compositeType;
        GcInfoBuilder gcInfoBuilder = (GcInfoBuilder) AccessController.doPrivileged(new PrivilegedAction<GcInfoBuilder>() { // from class: sun.management.GarbageCollectionNotifInfoCompositeData.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public GcInfoBuilder run() {
                try {
                    Field declaredField = Class.forName("com.sun.management.GcInfo").getDeclaredField("builder");
                    declaredField.setAccessible(true);
                    return (GcInfoBuilder) declaredField.get(GarbageCollectionNotifInfoCompositeData.this.gcNotifInfo.getGcInfo());
                } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e2) {
                    return null;
                }
            }
        });
        synchronized (compositeTypeByBuilder) {
            compositeType = compositeTypeByBuilder.get(gcInfoBuilder);
            if (compositeType == null) {
                try {
                    compositeType = new CompositeType("sun.management.GarbageCollectionNotifInfoCompositeType", "CompositeType for GC notification info", gcNotifInfoItemNames, gcNotifInfoItemNames, new OpenType[]{SimpleType.STRING, SimpleType.STRING, SimpleType.STRING, gcInfoBuilder.getGcInfoCompositeType()});
                    compositeTypeByBuilder.put(gcInfoBuilder, compositeType);
                } catch (OpenDataException e2) {
                    throw Util.newException(e2);
                }
            }
        }
        return compositeType;
    }

    @Override // sun.management.LazyCompositeData
    protected CompositeData getCompositeData() {
        try {
            return new CompositeDataSupport(getCompositeTypeByBuilder(), gcNotifInfoItemNames, new Object[]{this.gcNotifInfo.getGcName(), this.gcNotifInfo.getGcAction(), this.gcNotifInfo.getGcCause(), GcInfoCompositeData.toCompositeData(this.gcNotifInfo.getGcInfo())});
        } catch (OpenDataException e2) {
            throw new AssertionError(e2);
        }
    }

    public static String getGcName(CompositeData compositeData) {
        String string = getString(compositeData, GC_NAME);
        if (string == null) {
            throw new IllegalArgumentException("Invalid composite data: Attribute gcName has null value");
        }
        return string;
    }

    public static String getGcAction(CompositeData compositeData) {
        String string = getString(compositeData, GC_ACTION);
        if (string == null) {
            throw new IllegalArgumentException("Invalid composite data: Attribute gcAction has null value");
        }
        return string;
    }

    public static String getGcCause(CompositeData compositeData) {
        String string = getString(compositeData, GC_CAUSE);
        if (string == null) {
            throw new IllegalArgumentException("Invalid composite data: Attribute gcCause has null value");
        }
        return string;
    }

    public static GcInfo getGcInfo(CompositeData compositeData) {
        return GcInfo.from((CompositeData) compositeData.get(GC_INFO));
    }

    public static void validateCompositeData(CompositeData compositeData) {
        if (compositeData == null) {
            throw new NullPointerException("Null CompositeData");
        }
        if (!isTypeMatched(getBaseGcNotifInfoCompositeType(), compositeData.getCompositeType())) {
            throw new IllegalArgumentException("Unexpected composite type for GarbageCollectionNotificationInfo");
        }
    }

    private static synchronized CompositeType getBaseGcNotifInfoCompositeType() {
        if (baseGcNotifInfoCompositeType == null) {
            try {
                baseGcNotifInfoCompositeType = new CompositeType("sun.management.BaseGarbageCollectionNotifInfoCompositeType", "CompositeType for Base GarbageCollectionNotificationInfo", gcNotifInfoItemNames, gcNotifInfoItemNames, new OpenType[]{SimpleType.STRING, SimpleType.STRING, SimpleType.STRING, GcInfoCompositeData.getBaseGcInfoCompositeType()});
            } catch (OpenDataException e2) {
                throw Util.newException(e2);
            }
        }
        return baseGcNotifInfoCompositeType;
    }
}
