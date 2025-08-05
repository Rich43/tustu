package com.sun.beans.decoder;

import com.sun.beans.finder.ConstructorFinder;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/* loaded from: rt.jar:com/sun/beans/decoder/NewElementHandler.class */
class NewElementHandler extends ElementHandler {
    private List<Object> arguments = new ArrayList();
    private ValueObject value = ValueObjectImpl.VOID;
    private Class<?> type;

    NewElementHandler() {
    }

    @Override // com.sun.beans.decoder.ElementHandler
    public void addAttribute(String str, String str2) {
        if (str.equals(Constants.ATTRNAME_CLASS)) {
            this.type = getOwner().findClass(str2);
        } else {
            super.addAttribute(str, str2);
        }
    }

    @Override // com.sun.beans.decoder.ElementHandler
    protected final void addArgument(Object obj) {
        if (this.arguments == null) {
            throw new IllegalStateException("Could not add argument to evaluated element");
        }
        this.arguments.add(obj);
    }

    @Override // com.sun.beans.decoder.ElementHandler
    protected final Object getContextBean() {
        return this.type != null ? this.type : super.getContextBean();
    }

    @Override // com.sun.beans.decoder.ElementHandler
    protected final ValueObject getValueObject() {
        try {
        } catch (Exception e2) {
            getOwner().handleException(e2);
        } finally {
            this.arguments = null;
        }
        if (this.arguments != null) {
            this.value = getValueObject(this.type, this.arguments.toArray());
        }
        return this.value;
    }

    ValueObject getValueObject(Class<?> cls, Object[] objArr) throws Exception {
        if (cls == null) {
            throw new IllegalArgumentException("Class name is not set");
        }
        Constructor<?> constructorFindConstructor = ConstructorFinder.findConstructor(cls, getArgumentTypes(objArr));
        if (constructorFindConstructor.isVarArgs()) {
            objArr = getArguments(objArr, constructorFindConstructor.getParameterTypes());
        }
        return ValueObjectImpl.create(constructorFindConstructor.newInstance(objArr));
    }

    static Class<?>[] getArgumentTypes(Object[] objArr) {
        Class<?>[] clsArr = new Class[objArr.length];
        for (int i2 = 0; i2 < objArr.length; i2++) {
            if (objArr[i2] != null) {
                clsArr[i2] = objArr[i2].getClass();
            }
        }
        return clsArr;
    }

    static Object[] getArguments(Object[] objArr, Class<?>[] clsArr) throws NegativeArraySizeException {
        int length = clsArr.length - 1;
        if (clsArr.length == objArr.length) {
            Object obj = objArr[length];
            if (obj == null) {
                return objArr;
            }
            if (clsArr[length].isAssignableFrom(obj.getClass())) {
                return objArr;
            }
        }
        int length2 = objArr.length - length;
        Object objNewInstance = Array.newInstance(clsArr[length].getComponentType(), length2);
        System.arraycopy(objArr, length, objNewInstance, 0, length2);
        Object[] objArr2 = new Object[clsArr.length];
        System.arraycopy(objArr, 0, objArr2, 0, length);
        objArr2[length] = objNewInstance;
        return objArr2;
    }
}
