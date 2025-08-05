package javax.jws.soap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:javax/jws/soap/SOAPBinding.class */
public @interface SOAPBinding {

    /* loaded from: rt.jar:javax/jws/soap/SOAPBinding$ParameterStyle.class */
    public enum ParameterStyle {
        BARE,
        WRAPPED
    }

    /* loaded from: rt.jar:javax/jws/soap/SOAPBinding$Style.class */
    public enum Style {
        DOCUMENT,
        RPC
    }

    /* loaded from: rt.jar:javax/jws/soap/SOAPBinding$Use.class */
    public enum Use {
        LITERAL,
        ENCODED
    }

    Style style() default Style.DOCUMENT;

    Use use() default Use.LITERAL;

    ParameterStyle parameterStyle() default ParameterStyle.WRAPPED;
}
