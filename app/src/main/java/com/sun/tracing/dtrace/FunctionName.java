package com.sun.tracing.dtrace;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:com/sun/tracing/dtrace/FunctionName.class */
public @interface FunctionName {
    String value();
}
