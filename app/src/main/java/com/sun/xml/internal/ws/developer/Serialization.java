package com.sun.xml.internal.ws.developer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.xml.ws.spi.WebServiceFeatureAnnotation;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@WebServiceFeatureAnnotation(id = SerializationFeature.ID, bean = SerializationFeature.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: rt.jar:com/sun/xml/internal/ws/developer/Serialization.class */
public @interface Serialization {
    String encoding() default "";
}
