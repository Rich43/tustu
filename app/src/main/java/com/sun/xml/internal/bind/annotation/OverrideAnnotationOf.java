package com.sun.xml.internal.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:com/sun/xml/internal/bind/annotation/OverrideAnnotationOf.class */
public @interface OverrideAnnotationOf {
    String value() default "content";
}
