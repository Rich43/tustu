package com.sun.xml.internal.bind.v2.model.annotation;

import java.lang.annotation.Annotation;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/annotation/AnnotationSource.class */
public interface AnnotationSource {
    <A extends Annotation> A readAnnotation(Class<A> cls);

    boolean hasAnnotation(Class<? extends Annotation> cls);
}
