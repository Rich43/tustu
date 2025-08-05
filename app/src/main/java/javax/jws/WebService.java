package javax.jws;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:javax/jws/WebService.class */
public @interface WebService {
    String name() default "";

    String targetNamespace() default "";

    String serviceName() default "";

    String portName() default "";

    String wsdlLocation() default "";

    String endpointInterface() default "";
}
