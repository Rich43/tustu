package com.sun.tracing.dtrace;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:com/sun/tracing/dtrace/Attributes.class */
public @interface Attributes {
    StabilityLevel name() default StabilityLevel.PRIVATE;

    StabilityLevel data() default StabilityLevel.PRIVATE;

    DependencyClass dependency() default DependencyClass.UNKNOWN;
}
