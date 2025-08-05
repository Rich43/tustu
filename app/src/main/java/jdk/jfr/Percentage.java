package jdk.jfr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Label("Percentage")
@MetadataDefinition
@ContentType
@Retention(RetentionPolicy.RUNTIME)
@Description("Percentage, represented as a number between 0 and 1")
/* loaded from: jfr.jar:jdk/jfr/Percentage.class */
public @interface Percentage {
}
