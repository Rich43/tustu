package com.sun.xml.internal.ws.dump;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.xml.ws.spi.WebServiceFeatureAnnotation;

@Target({ElementType.TYPE})
@WebServiceFeatureAnnotation(id = MessageDumpingFeature.ID, bean = MessageDumpingFeature.class)
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:com/sun/xml/internal/ws/dump/MessageDumping.class */
public @interface MessageDumping {
    boolean enabled() default true;

    String messageLoggingRoot() default "com.sun.xml.internal.ws.messagedump";

    String messageLoggingLevel() default "FINE";

    boolean storeMessages() default false;
}
