package sun.reflect;

import java.lang.reflect.InvocationTargetException;

/* loaded from: rt.jar:sun/reflect/MethodAccessorImpl.class */
abstract class MethodAccessorImpl extends MagicAccessorImpl implements MethodAccessor {
    public abstract Object invoke(Object obj, Object[] objArr) throws IllegalArgumentException, InvocationTargetException;

    MethodAccessorImpl() {
    }
}
