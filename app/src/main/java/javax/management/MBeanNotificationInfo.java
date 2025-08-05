package javax.management;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: rt.jar:javax/management/MBeanNotificationInfo.class */
public class MBeanNotificationInfo extends MBeanFeatureInfo implements Cloneable {
    static final long serialVersionUID = -3888371564530107064L;
    private static final String[] NO_TYPES = new String[0];
    static final MBeanNotificationInfo[] NO_NOTIFICATIONS = new MBeanNotificationInfo[0];
    private String[] types;
    private final transient boolean arrayGettersSafe;

    public MBeanNotificationInfo(String[] strArr, String str, String str2) {
        this(strArr, str, str2, null);
    }

    public MBeanNotificationInfo(String[] strArr, String str, String str2, Descriptor descriptor) {
        super(str, str2, descriptor);
        this.types = (strArr == null || strArr.length <= 0) ? NO_TYPES : (String[]) strArr.clone();
        this.arrayGettersSafe = MBeanInfo.arrayGettersSafe(getClass(), MBeanNotificationInfo.class);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            return null;
        }
    }

    public String[] getNotifTypes() {
        if (this.types.length == 0) {
            return NO_TYPES;
        }
        return (String[]) this.types.clone();
    }

    private String[] fastGetNotifTypes() {
        if (this.arrayGettersSafe) {
            return this.types;
        }
        return getNotifTypes();
    }

    public String toString() {
        return getClass().getName() + "[description=" + getDescription() + ", name=" + getName() + ", notifTypes=" + ((Object) Arrays.asList(fastGetNotifTypes())) + ", descriptor=" + ((Object) getDescriptor()) + "]";
    }

    @Override // javax.management.MBeanFeatureInfo
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MBeanNotificationInfo)) {
            return false;
        }
        MBeanNotificationInfo mBeanNotificationInfo = (MBeanNotificationInfo) obj;
        return Objects.equals(mBeanNotificationInfo.getName(), getName()) && Objects.equals(mBeanNotificationInfo.getDescription(), getDescription()) && Objects.equals(mBeanNotificationInfo.getDescriptor(), getDescriptor()) && Arrays.equals(mBeanNotificationInfo.fastGetNotifTypes(), fastGetNotifTypes());
    }

    @Override // javax.management.MBeanFeatureInfo
    public int hashCode() {
        int iHashCode = getName().hashCode();
        for (int i2 = 0; i2 < this.types.length; i2++) {
            iHashCode ^= this.types[i2].hashCode();
        }
        return iHashCode;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        String[] strArr = (String[]) objectInputStream.readFields().get("types", (Object) null);
        this.types = (strArr == null || strArr.length == 0) ? NO_TYPES : (String[]) strArr.clone();
    }
}
