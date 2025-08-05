package com.sun.org.glassfish.external.arc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: rt.jar:com/sun/org/glassfish/external/arc/Taxonomy.class */
public @interface Taxonomy {
    Stability stability() default Stability.UNSPECIFIED;

    String description() default "";
}
