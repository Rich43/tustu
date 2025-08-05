package javafx.beans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: jfxrt.jar:javafx/beans/NamedArg.class */
public @interface NamedArg {
    String value();

    String defaultValue() default "";
}
