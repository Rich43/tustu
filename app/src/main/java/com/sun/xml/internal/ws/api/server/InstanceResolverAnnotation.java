package com.sun.xml.internal.ws.api.server;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/InstanceResolverAnnotation.class */
public @interface InstanceResolverAnnotation {
    Class<? extends InstanceResolver> value();
}
