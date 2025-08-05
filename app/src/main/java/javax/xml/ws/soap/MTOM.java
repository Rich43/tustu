package javax.xml.ws.soap;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.xml.ws.spi.WebServiceFeatureAnnotation;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@WebServiceFeatureAnnotation(id = MTOMFeature.ID, bean = MTOMFeature.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: rt.jar:javax/xml/ws/soap/MTOM.class */
public @interface MTOM {
    boolean enabled() default true;

    int threshold() default 0;
}
