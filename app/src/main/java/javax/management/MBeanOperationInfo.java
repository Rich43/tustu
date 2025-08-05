package javax.management;

import com.sun.jmx.mbeanserver.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: rt.jar:javax/management/MBeanOperationInfo.class */
public class MBeanOperationInfo extends MBeanFeatureInfo implements Cloneable {
    static final long serialVersionUID = -6178860474881375330L;
    static final MBeanOperationInfo[] NO_OPERATIONS;
    public static final int INFO = 0;
    public static final int ACTION = 1;
    public static final int ACTION_INFO = 2;
    public static final int UNKNOWN = 3;
    private final String type;
    private final MBeanParameterInfo[] signature;
    private final int impact;
    private final transient boolean arrayGettersSafe;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !MBeanOperationInfo.class.desiredAssertionStatus();
        NO_OPERATIONS = new MBeanOperationInfo[0];
    }

    public MBeanOperationInfo(String str, Method method) {
        this(method.getName(), str, methodSignature(method), method.getReturnType().getName(), 3, Introspector.descriptorForElement(method));
    }

    public MBeanOperationInfo(String str, String str2, MBeanParameterInfo[] mBeanParameterInfoArr, String str3, int i2) {
        this(str, str2, mBeanParameterInfoArr, str3, i2, (Descriptor) null);
    }

    public MBeanOperationInfo(String str, String str2, MBeanParameterInfo[] mBeanParameterInfoArr, String str3, int i2, Descriptor descriptor) {
        MBeanParameterInfo[] mBeanParameterInfoArr2;
        super(str, str2, descriptor);
        if (mBeanParameterInfoArr == null || mBeanParameterInfoArr.length == 0) {
            mBeanParameterInfoArr2 = MBeanParameterInfo.NO_PARAMS;
        } else {
            mBeanParameterInfoArr2 = (MBeanParameterInfo[]) mBeanParameterInfoArr.clone();
        }
        this.signature = mBeanParameterInfoArr2;
        this.type = str3;
        this.impact = i2;
        this.arrayGettersSafe = MBeanInfo.arrayGettersSafe(getClass(), MBeanOperationInfo.class);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            return null;
        }
    }

    public String getReturnType() {
        return this.type;
    }

    public MBeanParameterInfo[] getSignature() {
        if (this.signature == null) {
            return MBeanParameterInfo.NO_PARAMS;
        }
        if (this.signature.length == 0) {
            return this.signature;
        }
        return (MBeanParameterInfo[]) this.signature.clone();
    }

    private MBeanParameterInfo[] fastGetSignature() {
        if (this.arrayGettersSafe) {
            if (this.signature == null) {
                return MBeanParameterInfo.NO_PARAMS;
            }
            return this.signature;
        }
        return getSignature();
    }

    public int getImpact() {
        return this.impact;
    }

    public String toString() {
        String str;
        switch (getImpact()) {
            case 0:
                str = "info";
                break;
            case 1:
                str = "action";
                break;
            case 2:
                str = "action/info";
                break;
            case 3:
                str = "unknown";
                break;
            default:
                str = "(" + getImpact() + ")";
                break;
        }
        return getClass().getName() + "[description=" + getDescription() + ", name=" + getName() + ", returnType=" + getReturnType() + ", signature=" + ((Object) Arrays.asList(fastGetSignature())) + ", impact=" + str + ", descriptor=" + ((Object) getDescriptor()) + "]";
    }

    @Override // javax.management.MBeanFeatureInfo
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MBeanOperationInfo)) {
            return false;
        }
        MBeanOperationInfo mBeanOperationInfo = (MBeanOperationInfo) obj;
        return Objects.equals(mBeanOperationInfo.getName(), getName()) && Objects.equals(mBeanOperationInfo.getReturnType(), getReturnType()) && Objects.equals(mBeanOperationInfo.getDescription(), getDescription()) && mBeanOperationInfo.getImpact() == getImpact() && Arrays.equals(mBeanOperationInfo.fastGetSignature(), fastGetSignature()) && Objects.equals(mBeanOperationInfo.getDescriptor(), getDescriptor());
    }

    @Override // javax.management.MBeanFeatureInfo
    public int hashCode() {
        return Objects.hash(getName(), getReturnType());
    }

    private static MBeanParameterInfo[] methodSignature(Method method) {
        return parameters(method.getParameterTypes(), method.getParameterAnnotations());
    }

    static MBeanParameterInfo[] parameters(Class<?>[] clsArr, Annotation[][] annotationArr) {
        MBeanParameterInfo[] mBeanParameterInfoArr = new MBeanParameterInfo[clsArr.length];
        if (!$assertionsDisabled && clsArr.length != annotationArr.length) {
            throw new AssertionError();
        }
        for (int i2 = 0; i2 < clsArr.length; i2++) {
            mBeanParameterInfoArr[i2] = new MBeanParameterInfo("p" + (i2 + 1), clsArr[i2].getName(), "", Introspector.descriptorForAnnotations(annotationArr[i2]));
        }
        return mBeanParameterInfoArr;
    }
}
