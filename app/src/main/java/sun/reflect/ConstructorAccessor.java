package sun.reflect;

import java.lang.reflect.InvocationTargetException;

/* loaded from: rt.jar:sun/reflect/ConstructorAccessor.class */
public interface ConstructorAccessor {
    Object newInstance(Object[] objArr) throws InstantiationException, IllegalArgumentException, InvocationTargetException;
}
