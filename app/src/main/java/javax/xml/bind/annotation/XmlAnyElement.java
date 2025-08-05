package javax.xml.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:javax/xml/bind/annotation/XmlAnyElement.class */
public @interface XmlAnyElement {
    boolean lax() default false;

    Class<? extends DomHandler> value() default W3CDomHandler.class;
}
