package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import com.sun.xml.internal.ws.model.RuntimeModelerException;

/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/Util.class */
class Util {
    Util() {
    }

    static String nullSafe(String value) {
        return value == null ? "" : value;
    }

    static <T> T nullSafe(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    static <T extends Enum> T nullSafe(Enum r3, T t2) {
        return r3 == null ? t2 : (T) Enum.valueOf(t2.getClass(), r3.toString());
    }

    public static Class<?> findClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e2) {
            throw new RuntimeModelerException("runtime.modeler.external.metadata.generic", e2);
        }
    }
}
