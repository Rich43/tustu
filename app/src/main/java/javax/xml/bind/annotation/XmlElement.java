package javax.xml.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:javax/xml/bind/annotation/XmlElement.class */
public @interface XmlElement {

    /* loaded from: rt.jar:javax/xml/bind/annotation/XmlElement$DEFAULT.class */
    public static final class DEFAULT {
    }

    String name() default "##default";

    boolean nillable() default false;

    boolean required() default false;

    String namespace() default "##default";

    String defaultValue() default "��";

    Class type() default DEFAULT.class;
}
