package javax.management.openmbean;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import javax.management.Descriptor;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;

/* loaded from: rt.jar:javax/management/openmbean/OpenMBeanInfoSupport.class */
public class OpenMBeanInfoSupport extends MBeanInfo implements OpenMBeanInfo {
    static final long serialVersionUID = 4349395935420511492L;
    private transient Integer myHashCode;
    private transient String myToString;

    public OpenMBeanInfoSupport(String str, String str2, OpenMBeanAttributeInfo[] openMBeanAttributeInfoArr, OpenMBeanConstructorInfo[] openMBeanConstructorInfoArr, OpenMBeanOperationInfo[] openMBeanOperationInfoArr, MBeanNotificationInfo[] mBeanNotificationInfoArr) {
        this(str, str2, openMBeanAttributeInfoArr, openMBeanConstructorInfoArr, openMBeanOperationInfoArr, mBeanNotificationInfoArr, (Descriptor) null);
    }

    public OpenMBeanInfoSupport(String str, String str2, OpenMBeanAttributeInfo[] openMBeanAttributeInfoArr, OpenMBeanConstructorInfo[] openMBeanConstructorInfoArr, OpenMBeanOperationInfo[] openMBeanOperationInfoArr, MBeanNotificationInfo[] mBeanNotificationInfoArr, Descriptor descriptor) {
        super(str, str2, attributeArray(openMBeanAttributeInfoArr), constructorArray(openMBeanConstructorInfoArr), operationArray(openMBeanOperationInfoArr), mBeanNotificationInfoArr == null ? null : (MBeanNotificationInfo[]) mBeanNotificationInfoArr.clone(), descriptor);
        this.myHashCode = null;
        this.myToString = null;
    }

    private static MBeanAttributeInfo[] attributeArray(OpenMBeanAttributeInfo[] openMBeanAttributeInfoArr) {
        if (openMBeanAttributeInfoArr == null) {
            return null;
        }
        MBeanAttributeInfo[] mBeanAttributeInfoArr = new MBeanAttributeInfo[openMBeanAttributeInfoArr.length];
        System.arraycopy(openMBeanAttributeInfoArr, 0, mBeanAttributeInfoArr, 0, openMBeanAttributeInfoArr.length);
        return mBeanAttributeInfoArr;
    }

    private static MBeanConstructorInfo[] constructorArray(OpenMBeanConstructorInfo[] openMBeanConstructorInfoArr) {
        if (openMBeanConstructorInfoArr == null) {
            return null;
        }
        MBeanConstructorInfo[] mBeanConstructorInfoArr = new MBeanConstructorInfo[openMBeanConstructorInfoArr.length];
        System.arraycopy(openMBeanConstructorInfoArr, 0, mBeanConstructorInfoArr, 0, openMBeanConstructorInfoArr.length);
        return mBeanConstructorInfoArr;
    }

    private static MBeanOperationInfo[] operationArray(OpenMBeanOperationInfo[] openMBeanOperationInfoArr) {
        if (openMBeanOperationInfoArr == null) {
            return null;
        }
        MBeanOperationInfo[] mBeanOperationInfoArr = new MBeanOperationInfo[openMBeanOperationInfoArr.length];
        System.arraycopy(openMBeanOperationInfoArr, 0, mBeanOperationInfoArr, 0, openMBeanOperationInfoArr.length);
        return mBeanOperationInfoArr;
    }

    @Override // javax.management.MBeanInfo, javax.management.openmbean.OpenMBeanInfo
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            OpenMBeanInfo openMBeanInfo = (OpenMBeanInfo) obj;
            if (!Objects.equals(getClassName(), openMBeanInfo.getClassName()) || !sameArrayContents(getAttributes(), openMBeanInfo.getAttributes()) || !sameArrayContents(getConstructors(), openMBeanInfo.getConstructors()) || !sameArrayContents(getOperations(), openMBeanInfo.getOperations()) || !sameArrayContents(getNotifications(), openMBeanInfo.getNotifications())) {
                return false;
            }
            return true;
        } catch (ClassCastException e2) {
            return false;
        }
    }

    private static <T> boolean sameArrayContents(T[] tArr, T[] tArr2) {
        return new HashSet(Arrays.asList(tArr)).equals(new HashSet(Arrays.asList(tArr2)));
    }

    @Override // javax.management.MBeanInfo, javax.management.openmbean.OpenMBeanInfo
    public int hashCode() {
        if (this.myHashCode == null) {
            int iHashCode = 0;
            if (getClassName() != null) {
                iHashCode = 0 + getClassName().hashCode();
            }
            this.myHashCode = Integer.valueOf(iHashCode + arraySetHash(getAttributes()) + arraySetHash(getConstructors()) + arraySetHash(getOperations()) + arraySetHash(getNotifications()));
        }
        return this.myHashCode.intValue();
    }

    private static <T> int arraySetHash(T[] tArr) {
        return new HashSet(Arrays.asList(tArr)).hashCode();
    }

    @Override // javax.management.MBeanInfo, javax.management.openmbean.OpenMBeanInfo
    public String toString() {
        if (this.myToString == null) {
            this.myToString = getClass().getName() + "(mbean_class_name=" + getClassName() + ",attributes=" + Arrays.asList(getAttributes()).toString() + ",constructors=" + Arrays.asList(getConstructors()).toString() + ",operations=" + Arrays.asList(getOperations()).toString() + ",notifications=" + Arrays.asList(getNotifications()).toString() + ",descriptor=" + ((Object) getDescriptor()) + ")";
        }
        return this.myToString;
    }
}
