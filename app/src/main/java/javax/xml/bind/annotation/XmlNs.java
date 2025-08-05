package javax.xml.bind.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:javax/xml/bind/annotation/XmlNs.class */
public @interface XmlNs {
    String prefix();

    String namespaceURI();
}
