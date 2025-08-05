package javax.jws;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:javax/jws/WebParam.class */
public @interface WebParam {

    /* loaded from: rt.jar:javax/jws/WebParam$Mode.class */
    public enum Mode {
        IN,
        OUT,
        INOUT
    }

    String name() default "";

    String partName() default "";

    String targetNamespace() default "";

    Mode mode() default Mode.IN;

    boolean header() default false;
}
