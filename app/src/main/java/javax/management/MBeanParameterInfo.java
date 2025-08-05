package javax.management;

import java.util.Objects;

/* loaded from: rt.jar:javax/management/MBeanParameterInfo.class */
public class MBeanParameterInfo extends MBeanFeatureInfo implements Cloneable {
    static final long serialVersionUID = 7432616882776782338L;
    static final MBeanParameterInfo[] NO_PARAMS = new MBeanParameterInfo[0];
    private final String type;

    public MBeanParameterInfo(String str, String str2, String str3) {
        this(str, str2, str3, (Descriptor) null);
    }

    public MBeanParameterInfo(String str, String str2, String str3, Descriptor descriptor) {
        super(str, str3, descriptor);
        this.type = str2;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            return null;
        }
    }

    public String getType() {
        return this.type;
    }

    public String toString() {
        return getClass().getName() + "[description=" + getDescription() + ", name=" + getName() + ", type=" + getType() + ", descriptor=" + ((Object) getDescriptor()) + "]";
    }

    @Override // javax.management.MBeanFeatureInfo
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MBeanParameterInfo)) {
            return false;
        }
        MBeanParameterInfo mBeanParameterInfo = (MBeanParameterInfo) obj;
        return Objects.equals(mBeanParameterInfo.getName(), getName()) && Objects.equals(mBeanParameterInfo.getType(), getType()) && Objects.equals(mBeanParameterInfo.getDescription(), getDescription()) && Objects.equals(mBeanParameterInfo.getDescriptor(), getDescriptor());
    }

    @Override // javax.management.MBeanFeatureInfo
    public int hashCode() {
        return Objects.hash(getName(), getType());
    }
}
