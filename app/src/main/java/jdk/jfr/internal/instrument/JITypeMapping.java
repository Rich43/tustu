package jdk.jfr.internal.instrument;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
/* loaded from: jfr.jar:jdk/jfr/internal/instrument/JITypeMapping.class */
@interface JITypeMapping {
    String from();

    String to();
}
