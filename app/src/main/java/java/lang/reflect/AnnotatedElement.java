package java.lang.reflect;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import sun.reflect.annotation.AnnotationSupport;
import sun.reflect.annotation.AnnotationType;

/* loaded from: rt.jar:java/lang/reflect/AnnotatedElement.class */
public interface AnnotatedElement {
    <T extends Annotation> T getAnnotation(Class<T> cls);

    Annotation[] getAnnotations();

    Annotation[] getDeclaredAnnotations();

    default boolean isAnnotationPresent(Class<? extends Annotation> cls) {
        return getAnnotation(cls) != null;
    }

    default <T extends Annotation> T[] getAnnotationsByType(Class<T> cls) {
        Class superclass;
        Annotation[] declaredAnnotationsByType = getDeclaredAnnotationsByType(cls);
        if (declaredAnnotationsByType.length == 0 && (this instanceof Class) && AnnotationType.getInstance(cls).isInherited() && (superclass = ((Class) this).getSuperclass()) != null) {
            declaredAnnotationsByType = superclass.getAnnotationsByType(cls);
        }
        return (T[]) declaredAnnotationsByType;
    }

    default <T extends Annotation> T getDeclaredAnnotation(Class<T> cls) {
        Objects.requireNonNull(cls);
        for (Annotation annotation : getDeclaredAnnotations()) {
            if (cls.equals(annotation.annotationType())) {
                return cls.cast(annotation);
            }
        }
        return null;
    }

    default <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> cls) {
        Objects.requireNonNull(cls);
        return (T[]) AnnotationSupport.getDirectlyAndIndirectlyPresent((Map) Arrays.stream(getDeclaredAnnotations()).collect(Collectors.toMap((v0) -> {
            return v0.annotationType();
        }, Function.identity(), (annotation, annotation2) -> {
            return annotation;
        }, LinkedHashMap::new)), cls);
    }
}
