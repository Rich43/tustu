package jdk.jfr;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Label("Memory Address")
@MetadataDefinition
@ContentType
@Retention(RetentionPolicy.RUNTIME)
@Description("Represents a physical memory address")
/* loaded from: jfr.jar:jdk/jfr/MemoryAddress.class */
public @interface MemoryAddress {
}
