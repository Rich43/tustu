package java.beans;

import com.sun.javafx.fxml.BeanAdapter;
import java.lang.ref.Reference;
import java.lang.reflect.Method;

/* loaded from: rt.jar:java/beans/IndexedPropertyDescriptor.class */
public class IndexedPropertyDescriptor extends PropertyDescriptor {
    private Reference<? extends Class<?>> indexedPropertyTypeRef;
    private final MethodRef indexedReadMethodRef;
    private final MethodRef indexedWriteMethodRef;
    private String indexedReadMethodName;
    private String indexedWriteMethodName;

    public IndexedPropertyDescriptor(String str, Class<?> cls) throws IntrospectionException {
        this(str, cls, "get" + NameGenerator.capitalize(str), "set" + NameGenerator.capitalize(str), "get" + NameGenerator.capitalize(str), "set" + NameGenerator.capitalize(str));
    }

    public IndexedPropertyDescriptor(String str, Class<?> cls, String str2, String str3, String str4, String str5) throws IntrospectionException {
        super(str, cls, str2, str3);
        this.indexedReadMethodRef = new MethodRef();
        this.indexedWriteMethodRef = new MethodRef();
        this.indexedReadMethodName = str4;
        if (str4 != null && getIndexedReadMethod() == null) {
            throw new IntrospectionException("Method not found: " + str4);
        }
        this.indexedWriteMethodName = str5;
        if (str5 != null && getIndexedWriteMethod() == null) {
            throw new IntrospectionException("Method not found: " + str5);
        }
        findIndexedPropertyType(getIndexedReadMethod(), getIndexedWriteMethod());
    }

    public IndexedPropertyDescriptor(String str, Method method, Method method2, Method method3, Method method4) throws IntrospectionException {
        super(str, method, method2);
        this.indexedReadMethodRef = new MethodRef();
        this.indexedWriteMethodRef = new MethodRef();
        setIndexedReadMethod0(method3);
        setIndexedWriteMethod0(method4);
        setIndexedPropertyType(findIndexedPropertyType(method3, method4));
    }

    IndexedPropertyDescriptor(Class<?> cls, String str, Method method, Method method2, Method method3, Method method4) throws IntrospectionException {
        super(cls, str, method, method2);
        this.indexedReadMethodRef = new MethodRef();
        this.indexedWriteMethodRef = new MethodRef();
        setIndexedReadMethod0(method3);
        setIndexedWriteMethod0(method4);
        setIndexedPropertyType(findIndexedPropertyType(method3, method4));
    }

    public synchronized Method getIndexedReadMethod() {
        Method methodFindMethod = this.indexedReadMethodRef.get();
        if (methodFindMethod == null) {
            Class<?> class0 = getClass0();
            if (class0 != null) {
                if (this.indexedReadMethodName == null && !this.indexedReadMethodRef.isSet()) {
                    return null;
                }
                String str = "get" + getBaseName();
                if (this.indexedReadMethodName == null) {
                    Class<?> indexedPropertyType0 = getIndexedPropertyType0();
                    if (indexedPropertyType0 == Boolean.TYPE || indexedPropertyType0 == null) {
                        this.indexedReadMethodName = BeanAdapter.IS_PREFIX + getBaseName();
                    } else {
                        this.indexedReadMethodName = str;
                    }
                }
                Class[] clsArr = {Integer.TYPE};
                methodFindMethod = Introspector.findMethod(class0, this.indexedReadMethodName, 1, clsArr);
                if (methodFindMethod == null && !this.indexedReadMethodName.equals(str)) {
                    this.indexedReadMethodName = str;
                    methodFindMethod = Introspector.findMethod(class0, this.indexedReadMethodName, 1, clsArr);
                }
                setIndexedReadMethod0(methodFindMethod);
            } else {
                return null;
            }
        }
        return methodFindMethod;
    }

    public synchronized void setIndexedReadMethod(Method method) throws IntrospectionException {
        setIndexedPropertyType(findIndexedPropertyType(method, this.indexedWriteMethodRef.get()));
        setIndexedReadMethod0(method);
    }

    private void setIndexedReadMethod0(Method method) {
        this.indexedReadMethodRef.set(method);
        if (method == null) {
            this.indexedReadMethodName = null;
            return;
        }
        setClass0(method.getDeclaringClass());
        this.indexedReadMethodName = method.getName();
        setTransient((Transient) method.getAnnotation(Transient.class));
    }

    public synchronized Method getIndexedWriteMethod() {
        Method methodFindMethod = this.indexedWriteMethodRef.get();
        if (methodFindMethod == null) {
            Class<?> class0 = getClass0();
            if (class0 != null) {
                if (this.indexedWriteMethodName == null && !this.indexedWriteMethodRef.isSet()) {
                    return null;
                }
                Class<?> indexedPropertyType0 = getIndexedPropertyType0();
                if (indexedPropertyType0 == null) {
                    try {
                        indexedPropertyType0 = findIndexedPropertyType(getIndexedReadMethod(), null);
                        setIndexedPropertyType(indexedPropertyType0);
                    } catch (IntrospectionException e2) {
                        Class<?> propertyType = getPropertyType();
                        if (propertyType.isArray()) {
                            indexedPropertyType0 = propertyType.getComponentType();
                        }
                    }
                }
                if (this.indexedWriteMethodName == null) {
                    this.indexedWriteMethodName = "set" + getBaseName();
                }
                methodFindMethod = Introspector.findMethod(class0, this.indexedWriteMethodName, 2, indexedPropertyType0 == null ? null : new Class[]{Integer.TYPE, indexedPropertyType0});
                if (methodFindMethod != null && !methodFindMethod.getReturnType().equals(Void.TYPE)) {
                    methodFindMethod = null;
                }
                setIndexedWriteMethod0(methodFindMethod);
            } else {
                return null;
            }
        }
        return methodFindMethod;
    }

    public synchronized void setIndexedWriteMethod(Method method) throws IntrospectionException {
        setIndexedPropertyType(findIndexedPropertyType(getIndexedReadMethod(), method));
        setIndexedWriteMethod0(method);
    }

    private void setIndexedWriteMethod0(Method method) {
        this.indexedWriteMethodRef.set(method);
        if (method == null) {
            this.indexedWriteMethodName = null;
            return;
        }
        setClass0(method.getDeclaringClass());
        this.indexedWriteMethodName = method.getName();
        setTransient((Transient) method.getAnnotation(Transient.class));
    }

    public synchronized Class<?> getIndexedPropertyType() {
        Class<?> indexedPropertyType0 = getIndexedPropertyType0();
        if (indexedPropertyType0 == null) {
            try {
                indexedPropertyType0 = findIndexedPropertyType(getIndexedReadMethod(), getIndexedWriteMethod());
                setIndexedPropertyType(indexedPropertyType0);
            } catch (IntrospectionException e2) {
            }
        }
        return indexedPropertyType0;
    }

    private void setIndexedPropertyType(Class<?> cls) {
        this.indexedPropertyTypeRef = getWeakReference(cls);
    }

    private Class<?> getIndexedPropertyType0() {
        if (this.indexedPropertyTypeRef != null) {
            return this.indexedPropertyTypeRef.get();
        }
        return null;
    }

    private Class<?> findIndexedPropertyType(Method method, Method method2) throws IntrospectionException {
        Class<?> returnType = null;
        if (method != null) {
            Class<?>[] parameterTypes = getParameterTypes(getClass0(), method);
            if (parameterTypes.length != 1) {
                throw new IntrospectionException("bad indexed read method arg count");
            }
            if (parameterTypes[0] != Integer.TYPE) {
                throw new IntrospectionException("non int index to indexed read method");
            }
            returnType = getReturnType(getClass0(), method);
            if (returnType == Void.TYPE) {
                throw new IntrospectionException("indexed read method returns void");
            }
        }
        if (method2 != null) {
            Class<?>[] parameterTypes2 = getParameterTypes(getClass0(), method2);
            if (parameterTypes2.length != 2) {
                throw new IntrospectionException("bad indexed write method arg count");
            }
            if (parameterTypes2[0] != Integer.TYPE) {
                throw new IntrospectionException("non int index to indexed write method");
            }
            if (returnType == null || parameterTypes2[1].isAssignableFrom(returnType)) {
                returnType = parameterTypes2[1];
            } else if (!returnType.isAssignableFrom(parameterTypes2[1])) {
                throw new IntrospectionException("type mismatch between indexed read and indexed write methods: " + getName());
            }
        }
        Class<?> propertyType = getPropertyType();
        if (propertyType != null && (!propertyType.isArray() || propertyType.getComponentType() != returnType)) {
            throw new IntrospectionException("type mismatch between indexed and non-indexed methods: " + getName());
        }
        return returnType;
    }

    @Override // java.beans.PropertyDescriptor
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && (obj instanceof IndexedPropertyDescriptor)) {
            IndexedPropertyDescriptor indexedPropertyDescriptor = (IndexedPropertyDescriptor) obj;
            Method indexedReadMethod = indexedPropertyDescriptor.getIndexedReadMethod();
            Method indexedWriteMethod = indexedPropertyDescriptor.getIndexedWriteMethod();
            if (!compareMethods(getIndexedReadMethod(), indexedReadMethod) || !compareMethods(getIndexedWriteMethod(), indexedWriteMethod) || getIndexedPropertyType() != indexedPropertyDescriptor.getIndexedPropertyType()) {
                return false;
            }
            return super.equals(obj);
        }
        return false;
    }

    IndexedPropertyDescriptor(PropertyDescriptor propertyDescriptor, PropertyDescriptor propertyDescriptor2) {
        super(propertyDescriptor, propertyDescriptor2);
        this.indexedReadMethodRef = new MethodRef();
        this.indexedWriteMethodRef = new MethodRef();
        Method indexedReadMethod = null;
        Method indexedWriteMethod = null;
        if (propertyDescriptor instanceof IndexedPropertyDescriptor) {
            IndexedPropertyDescriptor indexedPropertyDescriptor = (IndexedPropertyDescriptor) propertyDescriptor;
            indexedReadMethod = indexedPropertyDescriptor.getIndexedReadMethod();
            indexedWriteMethod = indexedPropertyDescriptor.getIndexedWriteMethod();
        }
        if (propertyDescriptor2 instanceof IndexedPropertyDescriptor) {
            IndexedPropertyDescriptor indexedPropertyDescriptor2 = (IndexedPropertyDescriptor) propertyDescriptor2;
            Method indexedReadMethod2 = indexedPropertyDescriptor2.getIndexedReadMethod();
            indexedReadMethod = isAssignable(indexedReadMethod, indexedReadMethod2) ? indexedReadMethod2 : indexedReadMethod;
            Method indexedWriteMethod2 = indexedPropertyDescriptor2.getIndexedWriteMethod();
            if (isAssignable(indexedWriteMethod, indexedWriteMethod2)) {
                indexedWriteMethod = indexedWriteMethod2;
            }
        }
        if (indexedReadMethod != null) {
            try {
                setIndexedReadMethod(indexedReadMethod);
            } catch (IntrospectionException e2) {
                throw new AssertionError(e2);
            }
        }
        if (indexedWriteMethod != null) {
            setIndexedWriteMethod(indexedWriteMethod);
        }
    }

    IndexedPropertyDescriptor(IndexedPropertyDescriptor indexedPropertyDescriptor) {
        super(indexedPropertyDescriptor);
        this.indexedReadMethodRef = new MethodRef();
        this.indexedWriteMethodRef = new MethodRef();
        this.indexedReadMethodRef.set(indexedPropertyDescriptor.indexedReadMethodRef.get());
        this.indexedWriteMethodRef.set(indexedPropertyDescriptor.indexedWriteMethodRef.get());
        this.indexedPropertyTypeRef = indexedPropertyDescriptor.indexedPropertyTypeRef;
        this.indexedWriteMethodName = indexedPropertyDescriptor.indexedWriteMethodName;
        this.indexedReadMethodName = indexedPropertyDescriptor.indexedReadMethodName;
    }

    @Override // java.beans.PropertyDescriptor
    void updateGenericsFor(Class<?> cls) {
        super.updateGenericsFor(cls);
        try {
            setIndexedPropertyType(findIndexedPropertyType(this.indexedReadMethodRef.get(), this.indexedWriteMethodRef.get()));
        } catch (IntrospectionException e2) {
            setIndexedPropertyType(null);
        }
    }

    @Override // java.beans.PropertyDescriptor
    public int hashCode() {
        return (37 * ((37 * ((37 * super.hashCode()) + (this.indexedWriteMethodName == null ? 0 : this.indexedWriteMethodName.hashCode()))) + (this.indexedReadMethodName == null ? 0 : this.indexedReadMethodName.hashCode()))) + (getIndexedPropertyType() == null ? 0 : getIndexedPropertyType().hashCode());
    }

    @Override // java.beans.PropertyDescriptor, java.beans.FeatureDescriptor
    void appendTo(StringBuilder sb) {
        super.appendTo(sb);
        appendTo(sb, "indexedPropertyType", (Reference<?>) this.indexedPropertyTypeRef);
        appendTo(sb, "indexedReadMethod", this.indexedReadMethodRef.get());
        appendTo(sb, "indexedWriteMethod", this.indexedWriteMethodRef.get());
    }
}
