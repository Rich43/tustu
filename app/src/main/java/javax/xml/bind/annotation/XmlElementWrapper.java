package javax.xml.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:javax/xml/bind/annotation/XmlElementWrapper.class */
public @interface XmlElementWrapper {
    String name() default "##default";

    String namespace() default "##default";

    boolean nillable() default false;

    boolean required() default false;
}
