package javax.management.openmbean;

import java.util.Arrays;
import javax.management.Descriptor;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanParameterInfo;

/* loaded from: rt.jar:javax/management/openmbean/OpenMBeanConstructorInfoSupport.class */
public class OpenMBeanConstructorInfoSupport extends MBeanConstructorInfo implements OpenMBeanConstructorInfo {
    static final long serialVersionUID = -4400441579007477003L;
    private transient Integer myHashCode;
    private transient String myToString;

    public OpenMBeanConstructorInfoSupport(String str, String str2, OpenMBeanParameterInfo[] openMBeanParameterInfoArr) {
        this(str, str2, openMBeanParameterInfoArr, (Descriptor) null);
    }

    public OpenMBeanConstructorInfoSupport(String str, String str2, OpenMBeanParameterInfo[] openMBeanParameterInfoArr, Descriptor descriptor) {
        super(str, str2, arrayCopyCast(openMBeanParameterInfoArr), descriptor);
        this.myHashCode = null;
        this.myToString = null;
        if (str == null || str.trim().equals("")) {
            throw new IllegalArgumentException("Argument name cannot be null or empty");
        }
        if (str2 == null || str2.trim().equals("")) {
            throw new IllegalArgumentException("Argument description cannot be null or empty");
        }
    }

    private static MBeanParameterInfo[] arrayCopyCast(OpenMBeanParameterInfo[] openMBeanParameterInfoArr) {
        if (openMBeanParameterInfoArr == null) {
            return null;
        }
        MBeanParameterInfo[] mBeanParameterInfoArr = new MBeanParameterInfo[openMBeanParameterInfoArr.length];
        System.arraycopy(openMBeanParameterInfoArr, 0, mBeanParameterInfoArr, 0, openMBeanParameterInfoArr.length);
        return mBeanParameterInfoArr;
    }

    @Override // javax.management.MBeanConstructorInfo, javax.management.MBeanFeatureInfo
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            OpenMBeanConstructorInfo openMBeanConstructorInfo = (OpenMBeanConstructorInfo) obj;
            if (!getName().equals(openMBeanConstructorInfo.getName()) || !Arrays.equals(getSignature(), openMBeanConstructorInfo.getSignature())) {
                return false;
            }
            return true;
        } catch (ClassCastException e2) {
            return false;
        }
    }

    @Override // javax.management.MBeanConstructorInfo, javax.management.MBeanFeatureInfo
    public int hashCode() {
        if (this.myHashCode == null) {
            this.myHashCode = Integer.valueOf(0 + getName().hashCode() + Arrays.asList(getSignature()).hashCode());
        }
        return this.myHashCode.intValue();
    }

    @Override // javax.management.MBeanConstructorInfo
    public String toString() {
        if (this.myToString == null) {
            this.myToString = getClass().getName() + "(name=" + getName() + ",signature=" + Arrays.asList(getSignature()).toString() + ",descriptor=" + ((Object) getDescriptor()) + ")";
        }
        return this.myToString;
    }
}
