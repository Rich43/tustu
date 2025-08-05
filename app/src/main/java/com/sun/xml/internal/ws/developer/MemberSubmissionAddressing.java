package com.sun.xml.internal.ws.developer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.xml.ws.spi.WebServiceFeatureAnnotation;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@WebServiceFeatureAnnotation(id = MemberSubmissionAddressingFeature.ID, bean = MemberSubmissionAddressingFeature.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: rt.jar:com/sun/xml/internal/ws/developer/MemberSubmissionAddressing.class */
public @interface MemberSubmissionAddressing {

    /* loaded from: rt.jar:com/sun/xml/internal/ws/developer/MemberSubmissionAddressing$Validation.class */
    public enum Validation {
        LAX,
        STRICT
    }

    boolean enabled() default true;

    boolean required() default false;

    Validation validation() default Validation.LAX;
}
