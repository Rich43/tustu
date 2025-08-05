package jdk.internal.dynalink.linker;

/* loaded from: nashorn.jar:jdk/internal/dynalink/linker/GuardingTypeConverterFactory.class */
public interface GuardingTypeConverterFactory {
    GuardedTypeConversion convertToType(Class<?> cls, Class<?> cls2) throws Exception;
}
