package jdk.jfr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Label("Timespan")
@MetadataDefinition
@ContentType
@Retention(RetentionPolicy.RUNTIME)
@Description("A duration, measured in nanoseconds by default")
/* loaded from: jfr.jar:jdk/jfr/Timespan.class */
public @interface Timespan {
    public static final String TICKS = "TICKS";
    public static final String SECONDS = "SECONDS";
    public static final String MILLISECONDS = "MILLISECONDS";
    public static final String NANOSECONDS = "NANOSECONDS";
    public static final String MICROSECONDS = "MICROSECONDS";

    String value() default "NANOSECONDS";
}
