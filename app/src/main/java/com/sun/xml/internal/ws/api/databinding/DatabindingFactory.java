package com.sun.xml.internal.ws.api.databinding;

import com.sun.xml.internal.ws.db.DatabindingFactoryImpl;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/databinding/DatabindingFactory.class */
public abstract class DatabindingFactory extends com.oracle.webservices.internal.api.databinding.DatabindingFactory {
    static final String ImplClass = DatabindingFactoryImpl.class.getName();

    public abstract com.oracle.webservices.internal.api.databinding.Databinding createRuntime(DatabindingConfig databindingConfig);

    @Override // com.oracle.webservices.internal.api.databinding.DatabindingFactory
    public abstract Map<String, Object> properties();

    public static DatabindingFactory newInstance() {
        try {
            Class<?> cls = Class.forName(ImplClass);
            return (DatabindingFactory) cls.newInstance();
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }
}
