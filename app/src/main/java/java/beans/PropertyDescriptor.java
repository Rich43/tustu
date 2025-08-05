package java.beans;

import com.sun.javafx.fxml.BeanAdapter;
import java.lang.ref.Reference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:java/beans/PropertyDescriptor.class */
public class PropertyDescriptor extends FeatureDescriptor {
    private Reference<? extends Class<?>> propertyTypeRef;
    private final MethodRef readMethodRef;
    private final MethodRef writeMethodRef;
    private Reference<? extends Class<?>> propertyEditorClassRef;
    private boolean bound;
    private boolean constrained;
    private String baseName;
    private String writeMethodName;
    private String readMethodName;

    public PropertyDescriptor(String str, Class<?> cls) throws IntrospectionException {
        this(str, cls, BeanAdapter.IS_PREFIX + NameGenerator.capitalize(str), "set" + NameGenerator.capitalize(str));
    }

    public PropertyDescriptor(String str, Class<?> cls, String str2, String str3) throws IntrospectionException {
        this.readMethodRef = new MethodRef();
        this.writeMethodRef = new MethodRef();
        if (cls == null) {
            throw new IntrospectionException("Target Bean class is null");
        }
        if (str == null || str.length() == 0) {
            throw new IntrospectionException("bad property name");
        }
        if ("".equals(str2) || "".equals(str3)) {
            throw new IntrospectionException("read or write method name should not be the empty string");
        }
        setName(str);
        setClass0(cls);
        this.readMethodName = str2;
        if (str2 != null && getReadMethod() == null) {
            throw new IntrospectionException("Method not found: " + str2);
        }
        this.writeMethodName = str3;
        if (str3 != null && getWriteMethod() == null) {
            throw new IntrospectionException("Method not found: " + str3);
        }
        Class[] clsArr = {PropertyChangeListener.class};
        this.bound = null != Introspector.findMethod(cls, "addPropertyChangeListener", clsArr.length, clsArr);
    }

    public PropertyDescriptor(String str, Method method, Method method2) throws IntrospectionException {
        this.readMethodRef = new MethodRef();
        this.writeMethodRef = new MethodRef();
        if (str == null || str.length() == 0) {
            throw new IntrospectionException("bad property name");
        }
        setName(str);
        setReadMethod(method);
        setWriteMethod(method2);
    }

    PropertyDescriptor(Class<?> cls, String str, Method method, Method method2) throws IntrospectionException {
        this.readMethodRef = new MethodRef();
        this.writeMethodRef = new MethodRef();
        if (cls == null) {
            throw new IntrospectionException("Target Bean class is null");
        }
        setClass0(cls);
        setName(Introspector.decapitalize(str));
        setReadMethod(method);
        setWriteMethod(method2);
        this.baseName = str;
    }

    public synchronized Class<?> getPropertyType() {
        Class<?> propertyType0 = getPropertyType0();
        if (propertyType0 == null) {
            try {
                propertyType0 = findPropertyType(getReadMethod(), getWriteMethod());
                setPropertyType(propertyType0);
            } catch (IntrospectionException e2) {
            }
        }
        return propertyType0;
    }

    private void setPropertyType(Class<?> cls) {
        this.propertyTypeRef = getWeakReference(cls);
    }

    private Class<?> getPropertyType0() {
        if (this.propertyTypeRef != null) {
            return this.propertyTypeRef.get();
        }
        return null;
    }

    public synchronized Method getReadMethod() {
        Method methodFindMethod = this.readMethodRef.get();
        if (methodFindMethod == null) {
            Class<?> class0 = getClass0();
            if (class0 == null) {
                return null;
            }
            if (this.readMethodName == null && !this.readMethodRef.isSet()) {
                return null;
            }
            String str = "get" + getBaseName();
            if (this.readMethodName == null) {
                Class<?> propertyType0 = getPropertyType0();
                if (propertyType0 == Boolean.TYPE || propertyType0 == null) {
                    this.readMethodName = BeanAdapter.IS_PREFIX + getBaseName();
                } else {
                    this.readMethodName = str;
                }
            }
            methodFindMethod = Introspector.findMethod(class0, this.readMethodName, 0);
            if (methodFindMethod == null && !this.readMethodName.equals(str)) {
                this.readMethodName = str;
                methodFindMethod = Introspector.findMethod(class0, this.readMethodName, 0);
            }
            try {
                setReadMethod(methodFindMethod);
            } catch (IntrospectionException e2) {
            }
        }
        return methodFindMethod;
    }

    public synchronized void setReadMethod(Method method) throws IntrospectionException {
        this.readMethodRef.set(method);
        if (method == null) {
            this.readMethodName = null;
            return;
        }
        setPropertyType(findPropertyType(method, this.writeMethodRef.get()));
        setClass0(method.getDeclaringClass());
        this.readMethodName = method.getName();
        setTransient((Transient) method.getAnnotation(Transient.class));
    }

    public synchronized Method getWriteMethod() {
        Method methodFindMethod = this.writeMethodRef.get();
        if (methodFindMethod == null) {
            Class<?> class0 = getClass0();
            if (class0 == null) {
                return null;
            }
            if (this.writeMethodName == null && !this.writeMethodRef.isSet()) {
                return null;
            }
            Class<?> propertyType0 = getPropertyType0();
            if (propertyType0 == null) {
                try {
                    propertyType0 = findPropertyType(getReadMethod(), null);
                    setPropertyType(propertyType0);
                } catch (IntrospectionException e2) {
                    return null;
                }
            }
            if (this.writeMethodName == null) {
                this.writeMethodName = "set" + getBaseName();
            }
            methodFindMethod = Introspector.findMethod(class0, this.writeMethodName, 1, propertyType0 == null ? null : new Class[]{propertyType0});
            if (methodFindMethod != null && !methodFindMethod.getReturnType().equals(Void.TYPE)) {
                methodFindMethod = null;
            }
            try {
                setWriteMethod(methodFindMethod);
            } catch (IntrospectionException e3) {
            }
        }
        return methodFindMethod;
    }

    public synchronized void setWriteMethod(Method method) throws IntrospectionException {
        this.writeMethodRef.set(method);
        if (method == null) {
            this.writeMethodName = null;
            return;
        }
        setPropertyType(findPropertyType(getReadMethod(), method));
        setClass0(method.getDeclaringClass());
        this.writeMethodName = method.getName();
        setTransient((Transient) method.getAnnotation(Transient.class));
    }

    @Override // java.beans.FeatureDescriptor
    void setClass0(Class<?> cls) {
        if (getClass0() != null && cls.isAssignableFrom(getClass0())) {
            return;
        }
        super.setClass0(cls);
    }

    public boolean isBound() {
        return this.bound;
    }

    public void setBound(boolean z2) {
        this.bound = z2;
    }

    public boolean isConstrained() {
        return this.constrained;
    }

    public void setConstrained(boolean z2) {
        this.constrained = z2;
    }

    public void setPropertyEditorClass(Class<?> cls) {
        this.propertyEditorClassRef = getWeakReference(cls);
    }

    public Class<?> getPropertyEditorClass() {
        if (this.propertyEditorClassRef != null) {
            return this.propertyEditorClassRef.get();
        }
        return null;
    }

    public PropertyEditor createPropertyEditor(Object obj) {
        Object objNewInstance = null;
        Class<?> propertyEditorClass = getPropertyEditorClass();
        if (propertyEditorClass != null && PropertyEditor.class.isAssignableFrom(propertyEditorClass) && ReflectUtil.isPackageAccessible(propertyEditorClass)) {
            Constructor<?> constructor = null;
            if (obj != null) {
                try {
                    constructor = propertyEditorClass.getConstructor(Object.class);
                } catch (Exception e2) {
                }
            }
            try {
                if (constructor == null) {
                    objNewInstance = propertyEditorClass.newInstance();
                } else {
                    objNewInstance = constructor.newInstance(obj);
                }
            } catch (Exception e3) {
            }
        }
        return (PropertyEditor) objNewInstance;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && (obj instanceof PropertyDescriptor)) {
            PropertyDescriptor propertyDescriptor = (PropertyDescriptor) obj;
            Method readMethod = propertyDescriptor.getReadMethod();
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (compareMethods(getReadMethod(), readMethod) && compareMethods(getWriteMethod(), writeMethod) && getPropertyType() == propertyDescriptor.getPropertyType() && getPropertyEditorClass() == propertyDescriptor.getPropertyEditorClass() && this.bound == propertyDescriptor.isBound() && this.constrained == propertyDescriptor.isConstrained() && this.writeMethodName == propertyDescriptor.writeMethodName && this.readMethodName == propertyDescriptor.readMethodName) {
                return true;
            }
            return false;
        }
        return false;
    }

    boolean compareMethods(Method method, Method method2) {
        if ((method == null) != (method2 == null)) {
            return false;
        }
        if (method != null && method2 != null && !method.equals(method2)) {
            return false;
        }
        return true;
    }

    PropertyDescriptor(PropertyDescriptor propertyDescriptor, PropertyDescriptor propertyDescriptor2) {
        super(propertyDescriptor, propertyDescriptor2);
        this.readMethodRef = new MethodRef();
        this.writeMethodRef = new MethodRef();
        if (propertyDescriptor2.baseName != null) {
            this.baseName = propertyDescriptor2.baseName;
        } else {
            this.baseName = propertyDescriptor.baseName;
        }
        if (propertyDescriptor2.readMethodName != null) {
            this.readMethodName = propertyDescriptor2.readMethodName;
        } else {
            this.readMethodName = propertyDescriptor.readMethodName;
        }
        if (propertyDescriptor2.writeMethodName != null) {
            this.writeMethodName = propertyDescriptor2.writeMethodName;
        } else {
            this.writeMethodName = propertyDescriptor.writeMethodName;
        }
        if (propertyDescriptor2.propertyTypeRef != null) {
            this.propertyTypeRef = propertyDescriptor2.propertyTypeRef;
        } else {
            this.propertyTypeRef = propertyDescriptor.propertyTypeRef;
        }
        Method readMethod = propertyDescriptor.getReadMethod();
        Method readMethod2 = propertyDescriptor2.getReadMethod();
        try {
            if (isAssignable(readMethod, readMethod2)) {
                setReadMethod(readMethod2);
            } else {
                setReadMethod(readMethod);
            }
        } catch (IntrospectionException e2) {
        }
        if (readMethod != null && readMethod2 != null && readMethod.getDeclaringClass() == readMethod2.getDeclaringClass() && getReturnType(getClass0(), readMethod) == Boolean.TYPE && getReturnType(getClass0(), readMethod2) == Boolean.TYPE && readMethod.getName().indexOf(BeanAdapter.IS_PREFIX) == 0 && readMethod2.getName().indexOf("get") == 0) {
            try {
                setReadMethod(readMethod);
            } catch (IntrospectionException e3) {
            }
        }
        Method writeMethod = propertyDescriptor.getWriteMethod();
        Method writeMethod2 = propertyDescriptor2.getWriteMethod();
        try {
            if (writeMethod2 != null) {
                setWriteMethod(writeMethod2);
            } else {
                setWriteMethod(writeMethod);
            }
        } catch (IntrospectionException e4) {
        }
        if (propertyDescriptor2.getPropertyEditorClass() != null) {
            setPropertyEditorClass(propertyDescriptor2.getPropertyEditorClass());
        } else {
            setPropertyEditorClass(propertyDescriptor.getPropertyEditorClass());
        }
        this.bound = propertyDescriptor.bound | propertyDescriptor2.bound;
        this.constrained = propertyDescriptor.constrained | propertyDescriptor2.constrained;
    }

    PropertyDescriptor(PropertyDescriptor propertyDescriptor) {
        super(propertyDescriptor);
        this.readMethodRef = new MethodRef();
        this.writeMethodRef = new MethodRef();
        this.propertyTypeRef = propertyDescriptor.propertyTypeRef;
        this.readMethodRef.set(propertyDescriptor.readMethodRef.get());
        this.writeMethodRef.set(propertyDescriptor.writeMethodRef.get());
        this.propertyEditorClassRef = propertyDescriptor.propertyEditorClassRef;
        this.writeMethodName = propertyDescriptor.writeMethodName;
        this.readMethodName = propertyDescriptor.readMethodName;
        this.baseName = propertyDescriptor.baseName;
        this.bound = propertyDescriptor.bound;
        this.constrained = propertyDescriptor.constrained;
    }

    void updateGenericsFor(Class<?> cls) {
        setClass0(cls);
        try {
            setPropertyType(findPropertyType(this.readMethodRef.get(), this.writeMethodRef.get()));
        } catch (IntrospectionException e2) {
            setPropertyType(null);
        }
    }

    private Class<?> findPropertyType(Method method, Method method2) throws IntrospectionException {
        Class<?> returnType = null;
        if (method != null) {
            try {
                if (getParameterTypes(getClass0(), method).length != 0) {
                    throw new IntrospectionException("bad read method arg count: " + ((Object) method));
                }
                returnType = getReturnType(getClass0(), method);
                if (returnType == Void.TYPE) {
                    throw new IntrospectionException("read method " + method.getName() + " returns void");
                }
            } catch (IntrospectionException e2) {
                throw e2;
            }
        }
        if (method2 != null) {
            Class<?>[] parameterTypes = getParameterTypes(getClass0(), method2);
            if (parameterTypes.length != 1) {
                throw new IntrospectionException("bad write method arg count: " + ((Object) method2));
            }
            if (returnType != null && !parameterTypes[0].isAssignableFrom(returnType)) {
                throw new IntrospectionException("type mismatch between read and write methods");
            }
            returnType = parameterTypes[0];
        }
        return returnType;
    }

    public int hashCode() {
        return (37 * ((37 * ((37 * ((37 * ((37 * ((37 * ((37 * ((37 * ((37 * 7) + (getPropertyType() == null ? 0 : getPropertyType().hashCode()))) + (getReadMethod() == null ? 0 : getReadMethod().hashCode()))) + (getWriteMethod() == null ? 0 : getWriteMethod().hashCode()))) + (getPropertyEditorClass() == null ? 0 : getPropertyEditorClass().hashCode()))) + (this.writeMethodName == null ? 0 : this.writeMethodName.hashCode()))) + (this.readMethodName == null ? 0 : this.readMethodName.hashCode()))) + getName().hashCode())) + (!this.bound ? 0 : 1))) + (!this.constrained ? 0 : 1);
    }

    String getBaseName() {
        if (this.baseName == null) {
            this.baseName = NameGenerator.capitalize(getName());
        }
        return this.baseName;
    }

    @Override // java.beans.FeatureDescriptor
    void appendTo(StringBuilder sb) {
        appendTo(sb, "bound", this.bound);
        appendTo(sb, "constrained", this.constrained);
        appendTo(sb, "propertyEditorClass", (Reference<?>) this.propertyEditorClassRef);
        appendTo(sb, "propertyType", (Reference<?>) this.propertyTypeRef);
        appendTo(sb, "readMethod", this.readMethodRef.get());
        appendTo(sb, "writeMethod", this.writeMethodRef.get());
    }

    boolean isAssignable(Method method, Method method2) {
        if (method == null) {
            return true;
        }
        if (method2 == null) {
            return false;
        }
        if (!method.getName().equals(method2.getName())) {
            return true;
        }
        if (!method.getDeclaringClass().isAssignableFrom(method2.getDeclaringClass()) || !getReturnType(getClass0(), method).isAssignableFrom(getReturnType(getClass0(), method2))) {
            return false;
        }
        Class<?>[] parameterTypes = getParameterTypes(getClass0(), method);
        Class<?>[] parameterTypes2 = getParameterTypes(getClass0(), method2);
        if (parameterTypes.length != parameterTypes2.length) {
            return true;
        }
        for (int i2 = 0; i2 < parameterTypes.length; i2++) {
            if (!parameterTypes[i2].isAssignableFrom(parameterTypes2[i2])) {
                return false;
            }
        }
        return true;
    }
}
