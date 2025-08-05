package jdk.jfr.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jdk.jfr.MetadataDefinition;

@Target({ElementType.TYPE})
@MetadataDefinition
@Inherited
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: jfr.jar:jdk/jfr/internal/Cutoff.class */
public @interface Cutoff {
    public static final String NAME = "cutoff";
    public static final String INIFITY = "infinity";

    String value() default "inifity";
}
