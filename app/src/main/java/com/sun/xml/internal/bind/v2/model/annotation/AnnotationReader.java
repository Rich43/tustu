package com.sun.xml.internal.bind.v2.model.annotation;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.bind.v2.model.core.ErrorHandler;
import java.lang.annotation.Annotation;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/annotation/AnnotationReader.class */
public interface AnnotationReader<T, C, F, M> {
    void setErrorHandler(ErrorHandler errorHandler);

    <A extends Annotation> A getFieldAnnotation(Class<A> cls, F f2, Locatable locatable);

    boolean hasFieldAnnotation(Class<? extends Annotation> cls, F f2);

    boolean hasClassAnnotation(C c2, Class<? extends Annotation> cls);

    Annotation[] getAllFieldAnnotations(F f2, Locatable locatable);

    <A extends Annotation> A getMethodAnnotation(Class<A> cls, M m2, M m3, Locatable locatable);

    boolean hasMethodAnnotation(Class<? extends Annotation> cls, String str, M m2, M m3, Locatable locatable);

    Annotation[] getAllMethodAnnotations(M m2, Locatable locatable);

    <A extends Annotation> A getMethodAnnotation(Class<A> cls, M m2, Locatable locatable);

    boolean hasMethodAnnotation(Class<? extends Annotation> cls, M m2);

    @Nullable
    <A extends Annotation> A getMethodParameterAnnotation(Class<A> cls, M m2, int i2, Locatable locatable);

    @Nullable
    <A extends Annotation> A getClassAnnotation(Class<A> cls, C c2, Locatable locatable);

    @Nullable
    <A extends Annotation> A getPackageAnnotation(Class<A> cls, C c2, Locatable locatable);

    T getClassValue(Annotation annotation, String str);

    T[] getClassArrayValue(Annotation annotation, String str);
}
