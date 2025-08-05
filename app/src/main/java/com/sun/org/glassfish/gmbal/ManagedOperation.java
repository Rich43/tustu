package com.sun.org.glassfish.gmbal;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:com/sun/org/glassfish/gmbal/ManagedOperation.class */
public @interface ManagedOperation {
    String id() default "";

    Impact impact() default Impact.UNKNOWN;
}
