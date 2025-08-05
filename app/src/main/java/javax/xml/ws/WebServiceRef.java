package javax.xml.ws;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: rt.jar:javax/xml/ws/WebServiceRef.class */
public @interface WebServiceRef {
    String name() default "";

    Class<?> type() default Object.class;

    String mappedName() default "";

    Class<? extends Service> value() default Service.class;

    String wsdlLocation() default "";

    String lookup() default "";
}
