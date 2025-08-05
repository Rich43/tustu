package com.oracle.webservices.internal.api.message;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/* loaded from: rt.jar:com/oracle/webservices/internal/api/message/PropertySet.class */
public interface PropertySet {

    @Inherited
    @Target({ElementType.FIELD, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: rt.jar:com/oracle/webservices/internal/api/message/PropertySet$Property.class */
    public @interface Property {
        String[] value();
    }

    boolean containsKey(Object obj);

    Object get(Object obj);

    Object put(String str, Object obj);

    boolean supports(Object obj);

    Object remove(Object obj);

    @Deprecated
    Map<String, Object> createMapView();

    Map<String, Object> asMap();
}
