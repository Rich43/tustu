package javax.management;

import com.sun.jmx.mbeanserver.Introspector;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: rt.jar:javax/management/MBeanConstructorInfo.class */
public class MBeanConstructorInfo extends MBeanFeatureInfo implements Cloneable {
    static final long serialVersionUID = 4433990064191844427L;
    static final MBeanConstructorInfo[] NO_CONSTRUCTORS = new MBeanConstructorInfo[0];
    private final transient boolean arrayGettersSafe;
    private final MBeanParameterInfo[] signature;

    public MBeanConstructorInfo(String str, Constructor<?> constructor) {
        this(constructor.getName(), str, constructorSignature(constructor), Introspector.descriptorForElement(constructor));
    }

    public MBeanConstructorInfo(String str, String str2, MBeanParameterInfo[] mBeanParameterInfoArr) {
        this(str, str2, mBeanParameterInfoArr, null);
    }

    public MBeanConstructorInfo(String str, String str2, MBeanParameterInfo[] mBeanParameterInfoArr, Descriptor descriptor) {
        MBeanParameterInfo[] mBeanParameterInfoArr2;
        super(str, str2, descriptor);
        if (mBeanParameterInfoArr == null || mBeanParameterInfoArr.length == 0) {
            mBeanParameterInfoArr2 = MBeanParameterInfo.NO_PARAMS;
        } else {
            mBeanParameterInfoArr2 = (MBeanParameterInfo[]) mBeanParameterInfoArr.clone();
        }
        this.signature = mBeanParameterInfoArr2;
        this.arrayGettersSafe = MBeanInfo.arrayGettersSafe(getClass(), MBeanConstructorInfo.class);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            return null;
        }
    }

    public MBeanParameterInfo[] getSignature() {
        if (this.signature.length == 0) {
            return this.signature;
        }
        return (MBeanParameterInfo[]) this.signature.clone();
    }

    private MBeanParameterInfo[] fastGetSignature() {
        if (this.arrayGettersSafe) {
            return this.signature;
        }
        return getSignature();
    }

    public String toString() {
        return getClass().getName() + "[description=" + getDescription() + ", name=" + getName() + ", signature=" + ((Object) Arrays.asList(fastGetSignature())) + ", descriptor=" + ((Object) getDescriptor()) + "]";
    }

    @Override // javax.management.MBeanFeatureInfo
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MBeanConstructorInfo)) {
            return false;
        }
        MBeanConstructorInfo mBeanConstructorInfo = (MBeanConstructorInfo) obj;
        return Objects.equals(mBeanConstructorInfo.getName(), getName()) && Objects.equals(mBeanConstructorInfo.getDescription(), getDescription()) && Arrays.equals(mBeanConstructorInfo.fastGetSignature(), fastGetSignature()) && Objects.equals(mBeanConstructorInfo.getDescriptor(), getDescriptor());
    }

    @Override // javax.management.MBeanFeatureInfo
    public int hashCode() {
        return Objects.hash(getName()) ^ Arrays.hashCode(fastGetSignature());
    }

    private static MBeanParameterInfo[] constructorSignature(Constructor<?> constructor) {
        return MBeanOperationInfo.parameters(constructor.getParameterTypes(), constructor.getParameterAnnotations());
    }
}
