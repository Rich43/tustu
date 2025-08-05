package sun.reflect;

import java.lang.reflect.InvocationTargetException;

/* loaded from: rt.jar:sun/reflect/DelegatingConstructorAccessorImpl.class */
class DelegatingConstructorAccessorImpl extends ConstructorAccessorImpl {
    private ConstructorAccessorImpl delegate;

    DelegatingConstructorAccessorImpl(ConstructorAccessorImpl constructorAccessorImpl) {
        setDelegate(constructorAccessorImpl);
    }

    @Override // sun.reflect.ConstructorAccessorImpl, sun.reflect.ConstructorAccessor
    public Object newInstance(Object[] objArr) throws InstantiationException, IllegalArgumentException, InvocationTargetException {
        return this.delegate.newInstance(objArr);
    }

    void setDelegate(ConstructorAccessorImpl constructorAccessorImpl) {
        this.delegate = constructorAccessorImpl;
    }
}
