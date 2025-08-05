package com.sun.xml.internal.bind.v2.runtime;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/RuntimeUtil.class */
public class RuntimeUtil {
    public static final Map<Class, Class> boxToPrimitive;
    public static final Map<Class, Class> primitiveToBox;

    /* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/RuntimeUtil$ToStringAdapter.class */
    public static final class ToStringAdapter extends XmlAdapter<String, Object> {
        @Override // javax.xml.bind.annotation.adapters.XmlAdapter
        public Object unmarshal(String s2) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // javax.xml.bind.annotation.adapters.XmlAdapter
        public String marshal(Object o2) {
            if (o2 == null) {
                return null;
            }
            return o2.toString();
        }
    }

    static {
        Map<Class, Class> b2 = new HashMap<>();
        b2.put(Byte.TYPE, Byte.class);
        b2.put(Short.TYPE, Short.class);
        b2.put(Integer.TYPE, Integer.class);
        b2.put(Long.TYPE, Long.class);
        b2.put(Character.TYPE, Character.class);
        b2.put(Boolean.TYPE, Boolean.class);
        b2.put(Float.TYPE, Float.class);
        b2.put(Double.TYPE, Double.class);
        b2.put(Void.TYPE, Void.class);
        primitiveToBox = Collections.unmodifiableMap(b2);
        Map<Class, Class> p2 = new HashMap<>();
        for (Map.Entry<Class, Class> e2 : b2.entrySet()) {
            p2.put(e2.getValue(), e2.getKey());
        }
        boxToPrimitive = Collections.unmodifiableMap(p2);
    }

    private static String getTypeName(Object o2) {
        return o2.getClass().getName();
    }
}
