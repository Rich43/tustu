package jdk.jfr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE})
@Label("Content Type")
@MetadataDefinition
@Retention(RetentionPolicy.RUNTIME)
@Description("Semantic meaning of a value")
/* loaded from: jfr.jar:jdk/jfr/ContentType.class */
public @interface ContentType {
}
