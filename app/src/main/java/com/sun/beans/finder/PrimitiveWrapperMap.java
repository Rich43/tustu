package com.sun.beans.finder;

import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:com/sun/beans/finder/PrimitiveWrapperMap.class */
public final class PrimitiveWrapperMap {
    private static final Map<String, Class<?>> map = new HashMap(9);

    static void replacePrimitivesWithWrappers(Class<?>[] clsArr) {
        for (int i2 = 0; i2 < clsArr.length; i2++) {
            if (clsArr[i2] != null && clsArr[i2].isPrimitive()) {
                clsArr[i2] = getType(clsArr[i2].getName());
            }
        }
    }

    public static Class<?> getType(String str) {
        return map.get(str);
    }

    static {
        map.put(Boolean.TYPE.getName(), Boolean.class);
        map.put(Character.TYPE.getName(), Character.class);
        map.put(Byte.TYPE.getName(), Byte.class);
        map.put(Short.TYPE.getName(), Short.class);
        map.put(Integer.TYPE.getName(), Integer.class);
        map.put(Long.TYPE.getName(), Long.class);
        map.put(Float.TYPE.getName(), Float.class);
        map.put(Double.TYPE.getName(), Double.class);
        map.put(Void.TYPE.getName(), Void.class);
    }

    private PrimitiveWrapperMap() {
    }
}
