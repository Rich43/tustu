package com.sun.xml.internal.ws.api.databinding;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/databinding/MetadataReader.class */
public interface MetadataReader {
    Annotation[] getAnnotations(Method method);

    Annotation[][] getParameterAnnotations(Method method);

    <A extends Annotation> A getAnnotation(Class<A> cls, Method method);

    <A extends Annotation> A getAnnotation(Class<A> cls, Class<?> cls2);

    Annotation[] getAnnotations(Class<?> cls);

    void getProperties(Map<String, Object> map, Class<?> cls);

    void getProperties(Map<String, Object> map, Method method);

    void getProperties(Map<String, Object> map, Method method, int i2);
}
