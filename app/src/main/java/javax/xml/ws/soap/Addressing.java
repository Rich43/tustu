package javax.xml.ws.soap;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.xml.ws.soap.AddressingFeature;
import javax.xml.ws.spi.WebServiceFeatureAnnotation;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@WebServiceFeatureAnnotation(id = AddressingFeature.ID, bean = AddressingFeature.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: rt.jar:javax/xml/ws/soap/Addressing.class */
public @interface Addressing {
    boolean enabled() default true;

    boolean required() default false;

    AddressingFeature.Responses responses() default AddressingFeature.Responses.ALL;
}
