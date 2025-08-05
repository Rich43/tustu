package sun.reflect.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.lang.annotation.Repeatable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import sun.misc.JavaLangAccess;
import sun.misc.SharedSecrets;

/* loaded from: rt.jar:sun/reflect/annotation/AnnotationSupport.class */
public final class AnnotationSupport {
    private static final JavaLangAccess LANG_ACCESS = SharedSecrets.getJavaLangAccess();

    public static <A extends Annotation> A[] getDirectlyAndIndirectlyPresent(Map<Class<? extends Annotation>, Annotation> map, Class<A> cls) {
        ArrayList arrayList = new ArrayList();
        Annotation annotation = map.get(cls);
        if (annotation != null) {
            arrayList.add(annotation);
        }
        Annotation[] indirectlyPresent = getIndirectlyPresent(map, cls);
        if (indirectlyPresent != null && indirectlyPresent.length != 0) {
            arrayList.addAll(annotation == null || containerBeforeContainee(map, cls) ? 0 : 1, Arrays.asList(indirectlyPresent));
        }
        return (A[]) ((Annotation[]) arrayList.toArray((Annotation[]) Array.newInstance((Class<?>) cls, arrayList.size())));
    }

    private static <A extends Annotation> A[] getIndirectlyPresent(Map<Class<? extends Annotation>, Annotation> map, Class<A> cls) {
        Annotation annotation;
        Repeatable repeatable = (Repeatable) cls.getDeclaredAnnotation(Repeatable.class);
        if (repeatable == null || (annotation = map.get(repeatable.value())) == null) {
            return null;
        }
        A[] aArr = (A[]) getValueArray(annotation);
        checkTypes(aArr, annotation, cls);
        return aArr;
    }

    private static <A extends Annotation> boolean containerBeforeContainee(Map<Class<? extends Annotation>, Annotation> map, Class<A> cls) {
        Class<? extends Annotation> clsValue = ((Repeatable) cls.getDeclaredAnnotation(Repeatable.class)).value();
        for (Class<? extends Annotation> cls2 : map.keySet()) {
            if (cls2 == clsValue) {
                return true;
            }
            if (cls2 == cls) {
                return false;
            }
        }
        return false;
    }

    public static <A extends Annotation> A[] getAssociatedAnnotations(Map<Class<? extends Annotation>, Annotation> map, Class<?> cls, Class<A> cls2) {
        Objects.requireNonNull(cls);
        Annotation[] directlyAndIndirectlyPresent = getDirectlyAndIndirectlyPresent(map, cls2);
        if (AnnotationType.getInstance(cls2).isInherited()) {
            Class<? super Object> superclass = cls.getSuperclass();
            while (true) {
                Class<? super Object> cls3 = superclass;
                if (directlyAndIndirectlyPresent.length != 0 || cls3 == null) {
                    break;
                }
                directlyAndIndirectlyPresent = getDirectlyAndIndirectlyPresent(LANG_ACCESS.getDeclaredAnnotationMap(cls3), cls2);
                superclass = cls3.getSuperclass();
            }
        }
        return (A[]) directlyAndIndirectlyPresent;
    }

    private static <A extends Annotation> A[] getValueArray(Annotation annotation) {
        try {
            AnnotationType annotationType = AnnotationType.getInstance(annotation.annotationType());
            if (annotationType == null) {
                throw invalidContainerException(annotation, null);
            }
            Method method = annotationType.members().get("value");
            if (method == null) {
                throw invalidContainerException(annotation, null);
            }
            method.setAccessible(true);
            return (A[]) ((Annotation[]) method.invoke(annotation, new Object[0]));
        } catch (ClassCastException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e2) {
            throw invalidContainerException(annotation, e2);
        }
    }

    private static AnnotationFormatError invalidContainerException(Annotation annotation, Throwable th) {
        return new AnnotationFormatError(((Object) annotation) + " is an invalid container for repeating annotations", th);
    }

    private static <A extends Annotation> void checkTypes(A[] aArr, Annotation annotation, Class<A> cls) {
        for (A a2 : aArr) {
            if (!cls.isInstance(a2)) {
                throw new AnnotationFormatError(String.format("%s is an invalid container for repeating annotations of type: %s", annotation, cls));
            }
        }
    }
}
