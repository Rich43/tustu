package com.sun.xml.internal.ws.developer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.xml.ws.spi.WebServiceFeatureAnnotation;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@WebServiceFeatureAnnotation(id = StreamingAttachmentFeature.ID, bean = StreamingAttachmentFeature.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: rt.jar:com/sun/xml/internal/ws/developer/StreamingAttachment.class */
public @interface StreamingAttachment {
    String dir() default "";

    boolean parseEagerly() default false;

    long memoryThreshold() default 1048576;
}
