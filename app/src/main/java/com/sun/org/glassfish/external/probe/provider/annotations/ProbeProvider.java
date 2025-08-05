package com.sun.org.glassfish.external.probe.provider.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: rt.jar:com/sun/org/glassfish/external/probe/provider/annotations/ProbeProvider.class */
public @interface ProbeProvider {
    String providerName() default "";

    String moduleProviderName() default "";

    String moduleName() default "";

    String probeProviderName() default "";
}
