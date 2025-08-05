package sun.misc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.security.AccessControlContext;
import java.util.Map;
import sun.nio.ch.Interruptible;
import sun.reflect.ConstantPool;
import sun.reflect.annotation.AnnotationType;

/* loaded from: rt.jar:sun/misc/JavaLangAccess.class */
public interface JavaLangAccess {
    ConstantPool getConstantPool(Class<?> cls);

    boolean casAnnotationType(Class<?> cls, AnnotationType annotationType, AnnotationType annotationType2);

    AnnotationType getAnnotationType(Class<?> cls);

    Map<Class<? extends Annotation>, Annotation> getDeclaredAnnotationMap(Class<?> cls);

    byte[] getRawClassAnnotations(Class<?> cls);

    byte[] getRawClassTypeAnnotations(Class<?> cls);

    byte[] getRawExecutableTypeAnnotations(Executable executable);

    <E extends Enum<E>> E[] getEnumConstantsShared(Class<E> cls);

    void blockedOn(Thread thread, Interruptible interruptible);

    void registerShutdownHook(int i2, boolean z2, Runnable runnable);

    int getStackTraceDepth(Throwable th);

    StackTraceElement getStackTraceElement(Throwable th, int i2);

    String newStringUnsafe(char[] cArr);

    Thread newThreadWithAcc(Runnable runnable, AccessControlContext accessControlContext);

    void invokeFinalize(Object obj) throws Throwable;
}
