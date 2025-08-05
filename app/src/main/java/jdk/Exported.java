package jdk;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.PACKAGE})
@Exported
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:jdk/Exported.class */
public @interface Exported {
    boolean value() default true;
}
