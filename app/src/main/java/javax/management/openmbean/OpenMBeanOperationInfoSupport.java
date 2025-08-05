package javax.management.openmbean;

import java.util.Arrays;
import javax.management.Descriptor;
import javax.management.ImmutableDescriptor;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;

/* loaded from: rt.jar:javax/management/openmbean/OpenMBeanOperationInfoSupport.class */
public class OpenMBeanOperationInfoSupport extends MBeanOperationInfo implements OpenMBeanOperationInfo {
    static final long serialVersionUID = 4996859732565369366L;
    private OpenType<?> returnOpenType;
    private transient Integer myHashCode;
    private transient String myToString;

    public OpenMBeanOperationInfoSupport(String str, String str2, OpenMBeanParameterInfo[] openMBeanParameterInfoArr, OpenType<?> openType, int i2) {
        this(str, str2, openMBeanParameterInfoArr, openType, i2, (Descriptor) null);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public OpenMBeanOperationInfoSupport(String str, String str2, OpenMBeanParameterInfo[] openMBeanParameterInfoArr, OpenType<?> openType, int i2, Descriptor descriptor) {
        MBeanParameterInfo[] mBeanParameterInfoArrArrayCopyCast = arrayCopyCast(openMBeanParameterInfoArr);
        String className = openType == null ? null : openType.getClassName();
        Descriptor[] descriptorArr = new Descriptor[2];
        descriptorArr[0] = descriptor;
        descriptorArr[1] = openType == null ? null : openType.getDescriptor();
        super(str, str2, mBeanParameterInfoArrArrayCopyCast, className, i2, ImmutableDescriptor.union(descriptorArr));
        this.myHashCode = null;
        this.myToString = null;
        if (str == null || str.trim().equals("")) {
            throw new IllegalArgumentException("Argument name cannot be null or empty");
        }
        if (str2 == null || str2.trim().equals("")) {
            throw new IllegalArgumentException("Argument description cannot be null or empty");
        }
        if (openType == null) {
            throw new IllegalArgumentException("Argument returnOpenType cannot be null");
        }
        if (i2 != 1 && i2 != 2 && i2 != 0 && i2 != 3) {
            throw new IllegalArgumentException("Argument impact can only be one of ACTION, ACTION_INFO, INFO, or UNKNOWN: " + i2);
        }
        this.returnOpenType = openType;
    }

    private static MBeanParameterInfo[] arrayCopyCast(OpenMBeanParameterInfo[] openMBeanParameterInfoArr) {
        if (openMBeanParameterInfoArr == null) {
            return null;
        }
        MBeanParameterInfo[] mBeanParameterInfoArr = new MBeanParameterInfo[openMBeanParameterInfoArr.length];
        System.arraycopy(openMBeanParameterInfoArr, 0, mBeanParameterInfoArr, 0, openMBeanParameterInfoArr.length);
        return mBeanParameterInfoArr;
    }

    private static OpenMBeanParameterInfo[] arrayCopyCast(MBeanParameterInfo[] mBeanParameterInfoArr) {
        if (mBeanParameterInfoArr == null) {
            return null;
        }
        OpenMBeanParameterInfo[] openMBeanParameterInfoArr = new OpenMBeanParameterInfo[mBeanParameterInfoArr.length];
        System.arraycopy(mBeanParameterInfoArr, 0, openMBeanParameterInfoArr, 0, mBeanParameterInfoArr.length);
        return openMBeanParameterInfoArr;
    }

    @Override // javax.management.openmbean.OpenMBeanOperationInfo
    public OpenType<?> getReturnOpenType() {
        return this.returnOpenType;
    }

    @Override // javax.management.MBeanOperationInfo, javax.management.MBeanFeatureInfo
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            OpenMBeanOperationInfo openMBeanOperationInfo = (OpenMBeanOperationInfo) obj;
            if (!getName().equals(openMBeanOperationInfo.getName()) || !Arrays.equals(getSignature(), openMBeanOperationInfo.getSignature()) || !getReturnOpenType().equals(openMBeanOperationInfo.getReturnOpenType()) || getImpact() != openMBeanOperationInfo.getImpact()) {
                return false;
            }
            return true;
        } catch (ClassCastException e2) {
            return false;
        }
    }

    @Override // javax.management.MBeanOperationInfo, javax.management.MBeanFeatureInfo
    public int hashCode() {
        if (this.myHashCode == null) {
            this.myHashCode = Integer.valueOf(0 + getName().hashCode() + Arrays.asList(getSignature()).hashCode() + getReturnOpenType().hashCode() + getImpact());
        }
        return this.myHashCode.intValue();
    }

    @Override // javax.management.MBeanOperationInfo
    public String toString() {
        if (this.myToString == null) {
            this.myToString = getClass().getName() + "(name=" + getName() + ",signature=" + Arrays.asList(getSignature()).toString() + ",return=" + getReturnOpenType().toString() + ",impact=" + getImpact() + ",descriptor=" + ((Object) getDescriptor()) + ")";
        }
        return this.myToString;
    }

    private Object readResolve() {
        if (getDescriptor().getFieldNames().length == 0) {
            return new OpenMBeanOperationInfoSupport(this.name, this.description, arrayCopyCast(getSignature()), this.returnOpenType, getImpact());
        }
        return this;
    }
}
