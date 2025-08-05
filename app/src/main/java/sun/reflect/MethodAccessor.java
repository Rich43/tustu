package sun.reflect;

import java.lang.reflect.InvocationTargetException;

/* loaded from: rt.jar:sun/reflect/MethodAccessor.class */
public interface MethodAccessor {
    Object invoke(Object obj, Object[] objArr) throws IllegalArgumentException, InvocationTargetException;
}
