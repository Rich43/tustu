package sun.reflect;

import java.lang.reflect.InvocationTargetException;

/* loaded from: rt.jar:sun/reflect/ConstructorAccessorImpl.class */
abstract class ConstructorAccessorImpl extends MagicAccessorImpl implements ConstructorAccessor {
    public abstract Object newInstance(Object[] objArr) throws InstantiationException, IllegalArgumentException, InvocationTargetException;

    ConstructorAccessorImpl() {
    }
}
