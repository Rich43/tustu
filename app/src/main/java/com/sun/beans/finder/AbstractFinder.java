package com.sun.beans.finder;

import java.lang.reflect.Executable;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/* loaded from: rt.jar:com/sun/beans/finder/AbstractFinder.class */
abstract class AbstractFinder<T extends Executable> {
    private final Class<?>[] args;

    protected AbstractFinder(Class<?>[] clsArr) {
        this.args = clsArr;
    }

    protected boolean isValid(T t2) {
        return Modifier.isPublic(t2.getModifiers());
    }

    /* JADX WARN: Multi-variable type inference failed */
    final T find(T[] tArr) throws NoSuchMethodException {
        int length;
        HashMap map = new HashMap();
        T t2 = null;
        Class<?>[] clsArr = null;
        boolean z2 = false;
        for (T t3 : tArr) {
            if (isValid(t3)) {
                Class<?>[] parameterTypes = t3.getParameterTypes();
                if (parameterTypes.length == this.args.length) {
                    PrimitiveWrapperMap.replacePrimitivesWithWrappers(parameterTypes);
                    if (isAssignable(parameterTypes, this.args)) {
                        if (t2 == null) {
                            t2 = t3;
                            clsArr = parameterTypes;
                        } else {
                            boolean zIsAssignable = isAssignable(clsArr, parameterTypes);
                            boolean zIsAssignable2 = isAssignable(parameterTypes, clsArr);
                            if (zIsAssignable2 && zIsAssignable) {
                                zIsAssignable = !t3.isSynthetic();
                                zIsAssignable2 = !t2.isSynthetic();
                            }
                            if (zIsAssignable2 == zIsAssignable) {
                                z2 = true;
                            } else if (zIsAssignable) {
                                t2 = t3;
                                clsArr = parameterTypes;
                                z2 = false;
                            }
                        }
                    }
                }
                if (t3.isVarArgs() && (length = parameterTypes.length - 1) <= this.args.length) {
                    Class[] clsArr2 = new Class[this.args.length];
                    System.arraycopy(parameterTypes, 0, clsArr2, 0, length);
                    if (length < this.args.length) {
                        Class<?> componentType = parameterTypes[length].getComponentType();
                        if (componentType.isPrimitive()) {
                            componentType = PrimitiveWrapperMap.getType(componentType.getName());
                        }
                        for (int i2 = length; i2 < this.args.length; i2++) {
                            clsArr2[i2] = componentType;
                        }
                    }
                    map.put(t3, clsArr2);
                }
            }
        }
        for (T t4 : tArr) {
            Class<?>[] clsArr3 = (Class[]) map.get(t4);
            if (clsArr3 != null && isAssignable(clsArr3, this.args)) {
                if (t2 == null) {
                    t2 = t4;
                    clsArr = clsArr3;
                } else {
                    boolean zIsAssignable3 = isAssignable(clsArr, clsArr3);
                    boolean zIsAssignable4 = isAssignable(clsArr3, clsArr);
                    if (zIsAssignable4 && zIsAssignable3) {
                        zIsAssignable3 = !t4.isSynthetic();
                        zIsAssignable4 = !t2.isSynthetic();
                    }
                    if (zIsAssignable4 == zIsAssignable3) {
                        if (clsArr == map.get(t2)) {
                            z2 = true;
                        }
                    } else if (zIsAssignable3) {
                        t2 = t4;
                        clsArr = clsArr3;
                        z2 = false;
                    }
                }
            }
        }
        if (z2) {
            throw new NoSuchMethodException("Ambiguous methods are found");
        }
        if (t2 == null) {
            throw new NoSuchMethodException("Method is not found");
        }
        return t2;
    }

    private boolean isAssignable(Class<?>[] clsArr, Class<?>[] clsArr2) {
        for (int i2 = 0; i2 < this.args.length; i2++) {
            if (null != this.args[i2] && !clsArr[i2].isAssignableFrom(clsArr2[i2])) {
                return false;
            }
        }
        return true;
    }
}
