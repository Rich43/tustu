package javax.xml.bind.annotation.adapters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PACKAGE, ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:javax/xml/bind/annotation/adapters/XmlJavaTypeAdapter.class */
public @interface XmlJavaTypeAdapter {

    /* loaded from: rt.jar:javax/xml/bind/annotation/adapters/XmlJavaTypeAdapter$DEFAULT.class */
    public static final class DEFAULT {
    }

    Class<? extends XmlAdapter> value();

    Class type() default DEFAULT.class;
}
