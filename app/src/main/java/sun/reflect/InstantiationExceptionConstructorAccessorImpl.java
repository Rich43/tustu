package sun.reflect;

import java.lang.reflect.InvocationTargetException;

/* loaded from: rt.jar:sun/reflect/InstantiationExceptionConstructorAccessorImpl.class */
class InstantiationExceptionConstructorAccessorImpl extends ConstructorAccessorImpl {
    private final String message;

    InstantiationExceptionConstructorAccessorImpl(String str) {
        this.message = str;
    }

    @Override // sun.reflect.ConstructorAccessorImpl, sun.reflect.ConstructorAccessor
    public Object newInstance(Object[] objArr) throws InstantiationException, IllegalArgumentException, InvocationTargetException {
        if (this.message == null) {
            throw new InstantiationException();
        }
        throw new InstantiationException(this.message);
    }
}
