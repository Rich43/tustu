package sun.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/* loaded from: rt.jar:sun/reflect/BootstrapConstructorAccessorImpl.class */
class BootstrapConstructorAccessorImpl extends ConstructorAccessorImpl {
    private final Constructor<?> constructor;

    BootstrapConstructorAccessorImpl(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    @Override // sun.reflect.ConstructorAccessorImpl, sun.reflect.ConstructorAccessor
    public Object newInstance(Object[] objArr) throws IllegalArgumentException, InvocationTargetException {
        try {
            return UnsafeFieldAccessorImpl.unsafe.allocateInstance(this.constructor.getDeclaringClass());
        } catch (InstantiationException e2) {
            throw new InvocationTargetException(e2);
        }
    }
}
