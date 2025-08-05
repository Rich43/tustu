package jdk.jfr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@MetadataDefinition
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: jfr.jar:jdk/jfr/Name.class */
public @interface Name {
    String value();
}
