package com.sun.xml.internal.ws.model;

import com.sun.xml.internal.ws.api.databinding.MetadataReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/ReflectAnnotationReader.class */
public class ReflectAnnotationReader implements MetadataReader {
    @Override // com.sun.xml.internal.ws.api.databinding.MetadataReader
    public Annotation[] getAnnotations(Method m2) {
        return m2.getAnnotations();
    }

    @Override // com.sun.xml.internal.ws.api.databinding.MetadataReader
    public Annotation[][] getParameterAnnotations(final Method method) {
        return (Annotation[][]) AccessController.doPrivileged(new PrivilegedAction<Annotation[][]>() { // from class: com.sun.xml.internal.ws.model.ReflectAnnotationReader.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Annotation[][] run2() {
                return method.getParameterAnnotations();
            }
        });
    }

    @Override // com.sun.xml.internal.ws.api.databinding.MetadataReader
    public <A extends Annotation> A getAnnotation(final Class<A> annType, final Method m2) {
        return (A) AccessController.doPrivileged(new PrivilegedAction<A>() { // from class: com.sun.xml.internal.ws.model.ReflectAnnotationReader.2
            /* JADX WARN: Incorrect return type in method signature: ()TA; */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Annotation run2() {
                return m2.getAnnotation(annType);
            }
        });
    }

    @Override // com.sun.xml.internal.ws.api.databinding.MetadataReader
    public <A extends Annotation> A getAnnotation(final Class<A> annType, final Class<?> cls) {
        return (A) AccessController.doPrivileged(new PrivilegedAction<A>() { // from class: com.sun.xml.internal.ws.model.ReflectAnnotationReader.3
            /* JADX WARN: Incorrect return type in method signature: ()TA; */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Annotation run2() {
                return cls.getAnnotation(annType);
            }
        });
    }

    @Override // com.sun.xml.internal.ws.api.databinding.MetadataReader
    public Annotation[] getAnnotations(final Class<?> cls) {
        return (Annotation[]) AccessController.doPrivileged(new PrivilegedAction<Annotation[]>() { // from class: com.sun.xml.internal.ws.model.ReflectAnnotationReader.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Annotation[] run2() {
                return cls.getAnnotations();
            }
        });
    }

    @Override // com.sun.xml.internal.ws.api.databinding.MetadataReader
    public void getProperties(Map<String, Object> prop, Class<?> cls) {
    }

    @Override // com.sun.xml.internal.ws.api.databinding.MetadataReader
    public void getProperties(Map<String, Object> prop, Method method) {
    }

    @Override // com.sun.xml.internal.ws.api.databinding.MetadataReader
    public void getProperties(Map<String, Object> prop, Method method, int pos) {
    }
}
