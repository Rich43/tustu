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
/* loaded from: jfr.jar:jdk/jfr/Period.class */
public @interface Period {
    public static final String NAME = "period";

    String value() default "everyChunk";
}
