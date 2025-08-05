package javax.management;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

/* loaded from: rt.jar:javax/management/MBeanInfo.class */
public class MBeanInfo implements Cloneable, Serializable, DescriptorRead {
    static final long serialVersionUID = -6451021435135161911L;
    private transient Descriptor descriptor;
    private final String description;
    private final String className;
    private final MBeanAttributeInfo[] attributes;
    private final MBeanOperationInfo[] operations;
    private final MBeanConstructorInfo[] constructors;
    private final MBeanNotificationInfo[] notifications;
    private transient int hashCode;
    private final transient boolean arrayGettersSafe;
    private static final Map<Class<?>, Boolean> arrayGettersSafeMap = new WeakHashMap();

    public MBeanInfo(String str, String str2, MBeanAttributeInfo[] mBeanAttributeInfoArr, MBeanConstructorInfo[] mBeanConstructorInfoArr, MBeanOperationInfo[] mBeanOperationInfoArr, MBeanNotificationInfo[] mBeanNotificationInfoArr) throws IllegalArgumentException {
        this(str, str2, mBeanAttributeInfoArr, mBeanConstructorInfoArr, mBeanOperationInfoArr, mBeanNotificationInfoArr, null);
    }

    public MBeanInfo(String str, String str2, MBeanAttributeInfo[] mBeanAttributeInfoArr, MBeanConstructorInfo[] mBeanConstructorInfoArr, MBeanOperationInfo[] mBeanOperationInfoArr, MBeanNotificationInfo[] mBeanNotificationInfoArr, Descriptor descriptor) throws IllegalArgumentException {
        this.className = str;
        this.description = str2;
        this.attributes = mBeanAttributeInfoArr == null ? MBeanAttributeInfo.NO_ATTRIBUTES : mBeanAttributeInfoArr;
        this.operations = mBeanOperationInfoArr == null ? MBeanOperationInfo.NO_OPERATIONS : mBeanOperationInfoArr;
        this.constructors = mBeanConstructorInfoArr == null ? MBeanConstructorInfo.NO_CONSTRUCTORS : mBeanConstructorInfoArr;
        this.notifications = mBeanNotificationInfoArr == null ? MBeanNotificationInfo.NO_NOTIFICATIONS : mBeanNotificationInfoArr;
        this.descriptor = descriptor == null ? ImmutableDescriptor.EMPTY_DESCRIPTOR : descriptor;
        this.arrayGettersSafe = arrayGettersSafe(getClass(), MBeanInfo.class);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            return null;
        }
    }

    public String getClassName() {
        return this.className;
    }

    public String getDescription() {
        return this.description;
    }

    public MBeanAttributeInfo[] getAttributes() {
        MBeanAttributeInfo[] mBeanAttributeInfoArrNonNullAttributes = nonNullAttributes();
        if (mBeanAttributeInfoArrNonNullAttributes.length == 0) {
            return mBeanAttributeInfoArrNonNullAttributes;
        }
        return (MBeanAttributeInfo[]) mBeanAttributeInfoArrNonNullAttributes.clone();
    }

    private MBeanAttributeInfo[] fastGetAttributes() {
        if (this.arrayGettersSafe) {
            return nonNullAttributes();
        }
        return getAttributes();
    }

    private MBeanAttributeInfo[] nonNullAttributes() {
        return this.attributes == null ? MBeanAttributeInfo.NO_ATTRIBUTES : this.attributes;
    }

    public MBeanOperationInfo[] getOperations() {
        MBeanOperationInfo[] mBeanOperationInfoArrNonNullOperations = nonNullOperations();
        if (mBeanOperationInfoArrNonNullOperations.length == 0) {
            return mBeanOperationInfoArrNonNullOperations;
        }
        return (MBeanOperationInfo[]) mBeanOperationInfoArrNonNullOperations.clone();
    }

    private MBeanOperationInfo[] fastGetOperations() {
        if (this.arrayGettersSafe) {
            return nonNullOperations();
        }
        return getOperations();
    }

    private MBeanOperationInfo[] nonNullOperations() {
        return this.operations == null ? MBeanOperationInfo.NO_OPERATIONS : this.operations;
    }

    public MBeanConstructorInfo[] getConstructors() {
        MBeanConstructorInfo[] mBeanConstructorInfoArrNonNullConstructors = nonNullConstructors();
        if (mBeanConstructorInfoArrNonNullConstructors.length == 0) {
            return mBeanConstructorInfoArrNonNullConstructors;
        }
        return (MBeanConstructorInfo[]) mBeanConstructorInfoArrNonNullConstructors.clone();
    }

    private MBeanConstructorInfo[] fastGetConstructors() {
        if (this.arrayGettersSafe) {
            return nonNullConstructors();
        }
        return getConstructors();
    }

    private MBeanConstructorInfo[] nonNullConstructors() {
        return this.constructors == null ? MBeanConstructorInfo.NO_CONSTRUCTORS : this.constructors;
    }

    public MBeanNotificationInfo[] getNotifications() {
        MBeanNotificationInfo[] mBeanNotificationInfoArrNonNullNotifications = nonNullNotifications();
        if (mBeanNotificationInfoArrNonNullNotifications.length == 0) {
            return mBeanNotificationInfoArrNonNullNotifications;
        }
        return (MBeanNotificationInfo[]) mBeanNotificationInfoArrNonNullNotifications.clone();
    }

    private MBeanNotificationInfo[] fastGetNotifications() {
        if (this.arrayGettersSafe) {
            return nonNullNotifications();
        }
        return getNotifications();
    }

    private MBeanNotificationInfo[] nonNullNotifications() {
        return this.notifications == null ? MBeanNotificationInfo.NO_NOTIFICATIONS : this.notifications;
    }

    @Override // javax.management.DescriptorRead
    public Descriptor getDescriptor() {
        return (Descriptor) ImmutableDescriptor.nonNullDescriptor(this.descriptor).clone();
    }

    public String toString() {
        return getClass().getName() + "[description=" + getDescription() + ", attributes=" + ((Object) Arrays.asList(fastGetAttributes())) + ", constructors=" + ((Object) Arrays.asList(fastGetConstructors())) + ", operations=" + ((Object) Arrays.asList(fastGetOperations())) + ", notifications=" + ((Object) Arrays.asList(fastGetNotifications())) + ", descriptor=" + ((Object) getDescriptor()) + "]";
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MBeanInfo)) {
            return false;
        }
        MBeanInfo mBeanInfo = (MBeanInfo) obj;
        return isEqual(getClassName(), mBeanInfo.getClassName()) && isEqual(getDescription(), mBeanInfo.getDescription()) && getDescriptor().equals(mBeanInfo.getDescriptor()) && Arrays.equals(mBeanInfo.fastGetAttributes(), fastGetAttributes()) && Arrays.equals(mBeanInfo.fastGetOperations(), fastGetOperations()) && Arrays.equals(mBeanInfo.fastGetConstructors(), fastGetConstructors()) && Arrays.equals(mBeanInfo.fastGetNotifications(), fastGetNotifications());
    }

    public int hashCode() {
        if (this.hashCode != 0) {
            return this.hashCode;
        }
        this.hashCode = (((Objects.hash(getClassName(), getDescriptor()) ^ Arrays.hashCode(fastGetAttributes())) ^ Arrays.hashCode(fastGetOperations())) ^ Arrays.hashCode(fastGetConstructors())) ^ Arrays.hashCode(fastGetNotifications());
        return this.hashCode;
    }

    static boolean arrayGettersSafe(Class<?> cls, Class<?> cls2) {
        boolean zBooleanValue;
        if (cls == cls2) {
            return true;
        }
        synchronized (arrayGettersSafeMap) {
            Boolean bool = arrayGettersSafeMap.get(cls);
            if (bool == null) {
                try {
                    bool = (Boolean) AccessController.doPrivileged(new ArrayGettersSafeAction(cls, cls2));
                } catch (Exception e2) {
                    bool = false;
                }
                arrayGettersSafeMap.put(cls, bool);
                zBooleanValue = bool.booleanValue();
            } else {
                zBooleanValue = bool.booleanValue();
            }
        }
        return zBooleanValue;
    }

    /* loaded from: rt.jar:javax/management/MBeanInfo$ArrayGettersSafeAction.class */
    private static class ArrayGettersSafeAction implements PrivilegedAction<Boolean> {
        private final Class<?> subclass;
        private final Class<?> immutableClass;

        ArrayGettersSafeAction(Class<?> cls, Class<?> cls2) {
            this.subclass = cls;
            this.immutableClass = cls2;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        public Boolean run() throws SecurityException {
            for (Method method : this.immutableClass.getMethods()) {
                String name = method.getName();
                if (name.startsWith("get") && method.getParameterTypes().length == 0 && method.getReturnType().isArray()) {
                    try {
                        if (!this.subclass.getMethod(name, new Class[0]).equals(method)) {
                            return false;
                        }
                    } catch (NoSuchMethodException e2) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private static boolean isEqual(String str, String str2) {
        boolean zEquals;
        if (str == null) {
            zEquals = str2 == null;
        } else {
            zEquals = str.equals(str2);
        }
        return zEquals;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (this.descriptor.getClass() == ImmutableDescriptor.class) {
            objectOutputStream.write(1);
            String[] fieldNames = this.descriptor.getFieldNames();
            objectOutputStream.writeObject(fieldNames);
            objectOutputStream.writeObject(this.descriptor.getFieldValues(fieldNames));
            return;
        }
        objectOutputStream.write(0);
        objectOutputStream.writeObject(this.descriptor);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        switch (objectInputStream.read()) {
            case -1:
                this.descriptor = ImmutableDescriptor.EMPTY_DESCRIPTOR;
                return;
            case 0:
                this.descriptor = (Descriptor) objectInputStream.readObject();
                if (this.descriptor == null) {
                    this.descriptor = ImmutableDescriptor.EMPTY_DESCRIPTOR;
                    return;
                }
                return;
            case 1:
                String[] strArr = (String[]) objectInputStream.readObject();
                this.descriptor = strArr.length == 0 ? ImmutableDescriptor.EMPTY_DESCRIPTOR : new ImmutableDescriptor(strArr, (Object[]) objectInputStream.readObject());
                return;
            default:
                throw new StreamCorruptedException("Got unexpected byte.");
        }
    }
}
