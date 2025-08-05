package sun.management;

import java.lang.management.LockInfo;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;

/* loaded from: rt.jar:sun/management/LockInfoCompositeData.class */
public class LockInfoCompositeData extends LazyCompositeData {
    private final LockInfo lock;
    private static final CompositeType lockInfoCompositeType;
    private static final String CLASS_NAME = "className";
    private static final String IDENTITY_HASH_CODE = "identityHashCode";
    private static final String[] lockInfoItemNames;
    private static final long serialVersionUID = -6374759159749014052L;

    private LockInfoCompositeData(LockInfo lockInfo) {
        this.lock = lockInfo;
    }

    public LockInfo getLockInfo() {
        return this.lock;
    }

    public static CompositeData toCompositeData(LockInfo lockInfo) {
        if (lockInfo == null) {
            return null;
        }
        return new LockInfoCompositeData(lockInfo).getCompositeData();
    }

    @Override // sun.management.LazyCompositeData
    protected CompositeData getCompositeData() {
        try {
            return new CompositeDataSupport(lockInfoCompositeType, lockInfoItemNames, new Object[]{new String(this.lock.getClassName()), new Integer(this.lock.getIdentityHashCode())});
        } catch (OpenDataException e2) {
            throw Util.newException(e2);
        }
    }

    static {
        try {
            lockInfoCompositeType = (CompositeType) MappedMXBeanType.toOpenType(LockInfo.class);
            lockInfoItemNames = new String[]{CLASS_NAME, IDENTITY_HASH_CODE};
        } catch (OpenDataException e2) {
            throw Util.newException(e2);
        }
    }

    static CompositeType getLockInfoCompositeType() {
        return lockInfoCompositeType;
    }

    public static LockInfo toLockInfo(CompositeData compositeData) {
        if (compositeData == null) {
            throw new NullPointerException("Null CompositeData");
        }
        if (!isTypeMatched(lockInfoCompositeType, compositeData.getCompositeType())) {
            throw new IllegalArgumentException("Unexpected composite type for LockInfo");
        }
        return new LockInfo(getString(compositeData, CLASS_NAME), getInt(compositeData, IDENTITY_HASH_CODE));
    }
}
