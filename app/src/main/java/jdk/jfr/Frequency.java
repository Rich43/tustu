package jdk.jfr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Label("Frequency")
@MetadataDefinition
@ContentType
@Retention(RetentionPolicy.RUNTIME)
@Description("Measure of how often something occurs, in Hertz")
/* loaded from: jfr.jar:jdk/jfr/Frequency.class */
public @interface Frequency {
}
