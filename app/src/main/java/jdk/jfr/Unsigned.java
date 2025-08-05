package jdk.jfr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Label("Unsigned Value")
@MetadataDefinition
@ContentType
@Retention(RetentionPolicy.RUNTIME)
@Description("Value should be interpreted as unsigned data type")
/* loaded from: jfr.jar:jdk/jfr/Unsigned.class */
public @interface Unsigned {
}
