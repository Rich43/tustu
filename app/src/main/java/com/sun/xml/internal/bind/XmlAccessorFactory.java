package com.sun.xml.internal.bind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:com/sun/xml/internal/bind/XmlAccessorFactory.class */
public @interface XmlAccessorFactory {
    Class<? extends AccessorFactory> value();
}
