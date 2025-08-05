package com.oracle.webservices.internal.api.databinding;

import com.oracle.webservices.internal.api.databinding.Databinding;
import java.util.Map;

/* loaded from: rt.jar:com/oracle/webservices/internal/api/databinding/DatabindingFactory.class */
public abstract class DatabindingFactory {
    static final String ImplClass = "com.sun.xml.internal.ws.db.DatabindingFactoryImpl";

    public abstract Databinding.Builder createBuilder(Class<?> cls, Class<?> cls2);

    public abstract Map<String, Object> properties();

    public static DatabindingFactory newInstance() {
        try {
            Class<?> cls = Class.forName(ImplClass);
            return convertIfNecessary(cls);
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private static DatabindingFactory convertIfNecessary(Class<?> cls) throws IllegalAccessException, InstantiationException {
        return (DatabindingFactory) cls.newInstance();
    }
}
