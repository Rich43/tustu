package java.lang.management;

import javax.management.openmbean.CompositeData;
import sun.management.LockInfoCompositeData;

/* loaded from: rt.jar:java/lang/management/LockInfo.class */
public class LockInfo {
    private String className;
    private int identityHashCode;

    public LockInfo(String str, int i2) {
        if (str == null) {
            throw new NullPointerException("Parameter className cannot be null");
        }
        this.className = str;
        this.identityHashCode = i2;
    }

    LockInfo(Object obj) {
        this.className = obj.getClass().getName();
        this.identityHashCode = System.identityHashCode(obj);
    }

    public String getClassName() {
        return this.className;
    }

    public int getIdentityHashCode() {
        return this.identityHashCode;
    }

    public static LockInfo from(CompositeData compositeData) {
        if (compositeData == null) {
            return null;
        }
        if (compositeData instanceof LockInfoCompositeData) {
            return ((LockInfoCompositeData) compositeData).getLockInfo();
        }
        return LockInfoCompositeData.toLockInfo(compositeData);
    }

    public String toString() {
        return this.className + '@' + Integer.toHexString(this.identityHashCode);
    }
}
