package javax.xml.ws.spi;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.xml.ws.WebServiceFeature;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: rt.jar:javax/xml/ws/spi/WebServiceFeatureAnnotation.class */
public @interface WebServiceFeatureAnnotation {
    String id();

    Class<? extends WebServiceFeature> bean();
}
