package com.sun.corba.se.impl.orbutil;

import java.lang.reflect.Array;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/ObjectUtility.class */
public final class ObjectUtility {
    private ObjectUtility() {
    }

    public static Object concatenateArrays(Object obj, Object obj2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        Class<?> componentType = obj.getClass().getComponentType();
        Class<?> componentType2 = obj2.getClass().getComponentType();
        int length = Array.getLength(obj);
        int length2 = Array.getLength(obj2);
        if (componentType == null || componentType2 == null) {
            throw new IllegalStateException("Arguments must be arrays");
        }
        if (!componentType.equals(componentType2)) {
            throw new IllegalStateException("Arguments must be arrays with the same component type");
        }
        Object objNewInstance = Array.newInstance(componentType, length + length2);
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3++) {
            int i4 = i2;
            i2++;
            Array.set(objNewInstance, i4, Array.get(obj, i3));
        }
        for (int i5 = 0; i5 < length2; i5++) {
            int i6 = i2;
            i2++;
            Array.set(objNewInstance, i6, Array.get(obj2, i5));
        }
        return objNewInstance;
    }
}
