package sun.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:sun/reflect/NativeConstructorAccessorImpl.class */
class NativeConstructorAccessorImpl extends ConstructorAccessorImpl {

    /* renamed from: c, reason: collision with root package name */
    private final Constructor<?> f13601c;
    private DelegatingConstructorAccessorImpl parent;
    private int numInvocations;

    private static native Object newInstance0(Constructor<?> constructor, Object[] objArr) throws InstantiationException, IllegalArgumentException, InvocationTargetException;

    NativeConstructorAccessorImpl(Constructor<?> constructor) {
        this.f13601c = constructor;
    }

    @Override // sun.reflect.ConstructorAccessorImpl, sun.reflect.ConstructorAccessor
    public Object newInstance(Object[] objArr) throws InstantiationException, IllegalArgumentException, InvocationTargetException {
        int i2 = this.numInvocations + 1;
        this.numInvocations = i2;
        if (i2 > ReflectionFactory.inflationThreshold() && !ReflectUtil.isVMAnonymousClass(this.f13601c.getDeclaringClass())) {
            this.parent.setDelegate((ConstructorAccessorImpl) new MethodAccessorGenerator().generateConstructor(this.f13601c.getDeclaringClass(), this.f13601c.getParameterTypes(), this.f13601c.getExceptionTypes(), this.f13601c.getModifiers()));
        }
        return newInstance0(this.f13601c, objArr);
    }

    void setParent(DelegatingConstructorAccessorImpl delegatingConstructorAccessorImpl) {
        this.parent = delegatingConstructorAccessorImpl;
    }
}
