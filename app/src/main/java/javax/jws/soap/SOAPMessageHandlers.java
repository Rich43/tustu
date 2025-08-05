package javax.jws.soap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
/* loaded from: rt.jar:javax/jws/soap/SOAPMessageHandlers.class */
public @interface SOAPMessageHandlers {
    SOAPMessageHandler[] value();
}
