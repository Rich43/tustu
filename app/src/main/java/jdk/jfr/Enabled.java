package jdk.jfr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Inherited
@MetadataDefinition
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: jfr.jar:jdk/jfr/Enabled.class */
public @interface Enabled {
    public static final String NAME = "enabled";

    boolean value() default true;
}
