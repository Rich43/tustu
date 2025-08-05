package com.sun.xml.internal.ws.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:com/sun/xml/internal/ws/api/FeatureConstructor.class */
public @interface FeatureConstructor {
    String[] value() default {};
}
