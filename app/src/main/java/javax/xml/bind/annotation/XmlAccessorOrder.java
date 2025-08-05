package javax.xml.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Target({ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:javax/xml/bind/annotation/XmlAccessorOrder.class */
public @interface XmlAccessorOrder {
    XmlAccessOrder value() default XmlAccessOrder.UNDEFINED;
}
