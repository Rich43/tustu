package jdk.jfr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Label("Data Amount")
@MetadataDefinition
@ContentType
@Retention(RetentionPolicy.RUNTIME)
@Description("Amount of data")
/* loaded from: jfr.jar:jdk/jfr/DataAmount.class */
public @interface DataAmount {
    public static final String BITS = "BITS";
    public static final String BYTES = "BYTES";

    String value() default "BYTES";
}
