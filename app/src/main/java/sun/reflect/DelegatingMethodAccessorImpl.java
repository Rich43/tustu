package sun.reflect;

import java.lang.reflect.InvocationTargetException;

/* loaded from: rt.jar:sun/reflect/DelegatingMethodAccessorImpl.class */
class DelegatingMethodAccessorImpl extends MethodAccessorImpl {
    private MethodAccessorImpl delegate;

    DelegatingMethodAccessorImpl(MethodAccessorImpl methodAccessorImpl) {
        setDelegate(methodAccessorImpl);
    }

    @Override // sun.reflect.MethodAccessorImpl, sun.reflect.MethodAccessor
    public Object invoke(Object obj, Object[] objArr) throws IllegalArgumentException, InvocationTargetException {
        return this.delegate.invoke(obj, objArr);
    }

    void setDelegate(MethodAccessorImpl methodAccessorImpl) {
        this.delegate = methodAccessorImpl;
    }
}
