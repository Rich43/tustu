package javax.management;

import com.sun.javafx.fxml.BeanAdapter;
import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.jmx.mbeanserver.Introspector;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.Objects;

/* loaded from: rt.jar:javax/management/MBeanAttributeInfo.class */
public class MBeanAttributeInfo extends MBeanFeatureInfo implements Cloneable {
    private static final long serialVersionUID;
    static final MBeanAttributeInfo[] NO_ATTRIBUTES;
    private final String attributeType;
    private final boolean isWrite;
    private final boolean isRead;
    private final boolean is;

    static {
        long j2 = 8644704819898565848L;
        try {
            if ("1.0".equals((String) AccessController.doPrivileged(new GetPropertyAction("jmx.serial.form")))) {
                j2 = 7043855487133450673L;
            }
        } catch (Exception e2) {
        }
        serialVersionUID = j2;
        NO_ATTRIBUTES = new MBeanAttributeInfo[0];
    }

    public MBeanAttributeInfo(String str, String str2, String str3, boolean z2, boolean z3, boolean z4) {
        this(str, str2, str3, z2, z3, z4, (Descriptor) null);
    }

    public MBeanAttributeInfo(String str, String str2, String str3, boolean z2, boolean z3, boolean z4, Descriptor descriptor) {
        super(str, str3, descriptor);
        this.attributeType = str2;
        this.isRead = z2;
        this.isWrite = z3;
        if (z4 && !z2) {
            throw new IllegalArgumentException("Cannot have an \"is\" getter for a non-readable attribute");
        }
        if (z4 && !str2.equals(Constants.BOOLEAN_CLASS) && !str2.equals("boolean")) {
            throw new IllegalArgumentException("Cannot have an \"is\" getter for a non-boolean attribute");
        }
        this.is = z4;
    }

    public MBeanAttributeInfo(String str, String str2, Method method, Method method2) throws IntrospectionException {
        this(str, attributeType(method, method2), str2, method != null, method2 != null, isIs(method), ImmutableDescriptor.union(Introspector.descriptorForElement(method), Introspector.descriptorForElement(method2)));
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            return null;
        }
    }

    public String getType() {
        return this.attributeType;
    }

    public boolean isReadable() {
        return this.isRead;
    }

    public boolean isWritable() {
        return this.isWrite;
    }

    public boolean isIs() {
        return this.is;
    }

    public String toString() {
        String str;
        if (isReadable()) {
            if (isWritable()) {
                str = "read/write";
            } else {
                str = "read-only";
            }
        } else if (isWritable()) {
            str = "write-only";
        } else {
            str = "no-access";
        }
        return getClass().getName() + "[description=" + getDescription() + ", name=" + getName() + ", type=" + getType() + ", " + str + ", " + (isIs() ? "isIs, " : "") + "descriptor=" + ((Object) getDescriptor()) + "]";
    }

    @Override // javax.management.MBeanFeatureInfo
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MBeanAttributeInfo)) {
            return false;
        }
        MBeanAttributeInfo mBeanAttributeInfo = (MBeanAttributeInfo) obj;
        return Objects.equals(mBeanAttributeInfo.getName(), getName()) && Objects.equals(mBeanAttributeInfo.getType(), getType()) && Objects.equals(mBeanAttributeInfo.getDescription(), getDescription()) && Objects.equals(mBeanAttributeInfo.getDescriptor(), getDescriptor()) && mBeanAttributeInfo.isReadable() == isReadable() && mBeanAttributeInfo.isWritable() == isWritable() && mBeanAttributeInfo.isIs() == isIs();
    }

    @Override // javax.management.MBeanFeatureInfo
    public int hashCode() {
        return Objects.hash(getName(), getType());
    }

    private static boolean isIs(Method method) {
        return method != null && method.getName().startsWith(BeanAdapter.IS_PREFIX) && (method.getReturnType().equals(Boolean.TYPE) || method.getReturnType().equals(Boolean.class));
    }

    private static String attributeType(Method method, Method method2) throws IntrospectionException {
        Class<?> returnType = null;
        if (method != null) {
            if (method.getParameterTypes().length != 0) {
                throw new IntrospectionException("bad getter arg count");
            }
            returnType = method.getReturnType();
            if (returnType == Void.TYPE) {
                throw new IntrospectionException("getter " + method.getName() + " returns void");
            }
        }
        if (method2 != null) {
            Class<?>[] parameterTypes = method2.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new IntrospectionException("bad setter arg count");
            }
            if (returnType == null) {
                returnType = parameterTypes[0];
            } else if (returnType != parameterTypes[0]) {
                throw new IntrospectionException("type mismatch between getter and setter");
            }
        }
        if (returnType == null) {
            throw new IntrospectionException("getter and setter cannot both be null");
        }
        return returnType.getName();
    }
}
