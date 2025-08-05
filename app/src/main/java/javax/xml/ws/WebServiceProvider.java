package javax.xml.ws;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: rt.jar:javax/xml/ws/WebServiceProvider.class */
public @interface WebServiceProvider {
    String wsdlLocation() default "";

    String serviceName() default "";

    String targetNamespace() default "";

    String portName() default "";
}
