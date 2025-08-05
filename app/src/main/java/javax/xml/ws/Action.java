package javax.xml.ws;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:javax/xml/ws/Action.class */
public @interface Action {
    String input() default "";

    String output() default "";

    FaultAction[] fault() default {};
}
