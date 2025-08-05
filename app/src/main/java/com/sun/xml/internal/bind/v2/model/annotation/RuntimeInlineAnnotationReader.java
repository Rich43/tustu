package com.sun.xml.internal.bind.v2.model.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/annotation/RuntimeInlineAnnotationReader.class */
public final class RuntimeInlineAnnotationReader extends AbstractInlineAnnotationReaderImpl<Type, Class, Field, Method> implements RuntimeAnnotationReader {
    private final Map<Class<? extends Annotation>, Map<Package, Annotation>> packageCache = new HashMap();

    @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader
    public /* bridge */ /* synthetic */ boolean hasMethodAnnotation(Class cls, Method method) {
        return hasMethodAnnotation2((Class<? extends Annotation>) cls, method);
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader
    public /* bridge */ /* synthetic */ boolean hasClassAnnotation(Class cls, Class cls2) {
        return hasClassAnnotation2(cls, (Class<? extends Annotation>) cls2);
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader
    public /* bridge */ /* synthetic */ boolean hasFieldAnnotation(Class cls, Field field) {
        return hasFieldAnnotation2((Class<? extends Annotation>) cls, field);
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader
    public <A extends Annotation> A getFieldAnnotation(Class<A> cls, Field field, Locatable locatable) {
        return (A) LocatableAnnotation.create(field.getAnnotation(cls), locatable);
    }

    /* renamed from: hasFieldAnnotation, reason: avoid collision after fix types in other method */
    public boolean hasFieldAnnotation2(Class<? extends Annotation> annotationType, Field field) {
        return field.isAnnotationPresent(annotationType);
    }

    /* renamed from: hasClassAnnotation, reason: avoid collision after fix types in other method */
    public boolean hasClassAnnotation2(Class clazz, Class<? extends Annotation> annotationType) {
        return clazz.isAnnotationPresent(annotationType);
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader
    public Annotation[] getAllFieldAnnotations(Field field, Locatable srcPos) {
        Annotation[] r2 = field.getAnnotations();
        for (int i2 = 0; i2 < r2.length; i2++) {
            r2[i2] = LocatableAnnotation.create(r2[i2], srcPos);
        }
        return r2;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader
    public <A extends Annotation> A getMethodAnnotation(Class<A> cls, Method method, Locatable locatable) {
        return (A) LocatableAnnotation.create(method.getAnnotation(cls), locatable);
    }

    /* renamed from: hasMethodAnnotation, reason: avoid collision after fix types in other method */
    public boolean hasMethodAnnotation2(Class<? extends Annotation> annotation, Method method) {
        return method.isAnnotationPresent(annotation);
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader
    public Annotation[] getAllMethodAnnotations(Method method, Locatable srcPos) {
        Annotation[] r2 = method.getAnnotations();
        for (int i2 = 0; i2 < r2.length; i2++) {
            r2[i2] = LocatableAnnotation.create(r2[i2], srcPos);
        }
        return r2;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader
    public <A extends Annotation> A getMethodParameterAnnotation(Class<A> cls, Method method, int i2, Locatable locatable) {
        for (Annotation annotation : method.getParameterAnnotations()[i2]) {
            if (annotation.annotationType() == cls) {
                return (A) LocatableAnnotation.create(annotation, locatable);
            }
        }
        return null;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader
    public <A extends Annotation> A getClassAnnotation(Class<A> cls, Class cls2, Locatable locatable) {
        return (A) LocatableAnnotation.create(cls2.getAnnotation(cls), locatable);
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader
    public <A extends Annotation> A getPackageAnnotation(Class<A> cls, Class cls2, Locatable locatable) {
        Package r0 = cls2.getPackage();
        if (r0 == null) {
            return null;
        }
        Map<Package, Annotation> map = this.packageCache.get(cls);
        if (map == null) {
            map = new HashMap();
            this.packageCache.put(cls, map);
        }
        if (map.containsKey(r0)) {
            return (A) map.get(r0);
        }
        A a2 = (A) LocatableAnnotation.create(r0.getAnnotation(cls), locatable);
        map.put(r0, a2);
        return a2;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader
    /* renamed from: getClassValue, reason: merged with bridge method [inline-methods] */
    public Type getClassValue2(Annotation a2, String name) {
        try {
            return (Class) a2.annotationType().getMethod(name, new Class[0]).invoke(a2, new Object[0]);
        } catch (IllegalAccessException e2) {
            throw new IllegalAccessError(e2.getMessage());
        } catch (NoSuchMethodException e3) {
            throw new NoSuchMethodError(e3.getMessage());
        } catch (InvocationTargetException e4) {
            throw new InternalError(Messages.CLASS_NOT_FOUND.format(a2.annotationType(), e4.getMessage()));
        }
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader
    /* renamed from: getClassArrayValue, reason: merged with bridge method [inline-methods] */
    public Type[] getClassArrayValue2(Annotation a2, String name) {
        try {
            return (Class[]) a2.annotationType().getMethod(name, new Class[0]).invoke(a2, new Object[0]);
        } catch (IllegalAccessException e2) {
            throw new IllegalAccessError(e2.getMessage());
        } catch (NoSuchMethodException e3) {
            throw new NoSuchMethodError(e3.getMessage());
        } catch (InvocationTargetException e4) {
            throw new InternalError(e4.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.xml.internal.bind.v2.model.annotation.AbstractInlineAnnotationReaderImpl
    public String fullName(Method m2) {
        return m2.getDeclaringClass().getName() + '#' + m2.getName();
    }
}
