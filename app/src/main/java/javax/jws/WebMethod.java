package javax.jws;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:javax/jws/WebMethod.class */
public @interface WebMethod {
    String operationName() default "";

    String action() default "";

    boolean exclude() default false;
}
