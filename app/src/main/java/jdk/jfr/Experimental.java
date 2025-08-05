package jdk.jfr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Label("Experimental")
@MetadataDefinition
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Description("Element is not to be shown to a user by default")
/* loaded from: jfr.jar:jdk/jfr/Experimental.class */
public @interface Experimental {
}
