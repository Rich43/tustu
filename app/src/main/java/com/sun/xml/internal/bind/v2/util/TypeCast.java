package com.sun.xml.internal.bind.v2.util;

import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/util/TypeCast.class */
public class TypeCast {
    /* JADX WARN: Multi-variable type inference failed */
    public static <K, V> Map<K, V> checkedCast(Map<?, ?> map, Class<K> keyType, Class<V> valueType) {
        if (map == 0) {
            return null;
        }
        for (Map.Entry e2 : map.entrySet()) {
            if (!keyType.isInstance(e2.getKey())) {
                throw new ClassCastException(e2.getKey().getClass().toString());
            }
            if (!valueType.isInstance(e2.getValue())) {
                throw new ClassCastException(e2.getValue().getClass().toString());
            }
        }
        return map;
    }
}
