package jdk.jfr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Label("Timestamp")
@MetadataDefinition
@ContentType
@Retention(RetentionPolicy.RUNTIME)
@Description("A point in time")
/* loaded from: jfr.jar:jdk/jfr/Timestamp.class */
public @interface Timestamp {
    public static final String MILLISECONDS_SINCE_EPOCH = "MILLISECONDS_SINCE_EPOCH";
    public static final String TICKS = "TICKS";

    String value() default "MILLISECONDS_SINCE_EPOCH";
}
