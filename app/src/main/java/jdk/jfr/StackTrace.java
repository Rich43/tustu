package jdk.jfr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@MetadataDefinition
@Inherited
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: jfr.jar:jdk/jfr/StackTrace.class */
public @interface StackTrace {
    public static final String NAME = "stackTrace";

    boolean value() default true;
}
