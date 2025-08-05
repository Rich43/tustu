package jdk.jfr.internal.instrument;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: jfr.jar:jdk/jfr/internal/instrument/JIInstrumentationTarget.class */
@interface JIInstrumentationTarget {
    String value();
}
