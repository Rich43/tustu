package sun.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:sun/reflect/NativeMethodAccessorImpl.class */
class NativeMethodAccessorImpl extends MethodAccessorImpl {
    private final Method method;
    private DelegatingMethodAccessorImpl parent;
    private int numInvocations;

    private static native Object invoke0(Method method, Object obj, Object[] objArr);

    NativeMethodAccessorImpl(Method method) {
        this.method = method;
    }

    @Override // sun.reflect.MethodAccessorImpl, sun.reflect.MethodAccessor
    public Object invoke(Object obj, Object[] objArr) throws IllegalArgumentException, InvocationTargetException {
        int i2 = this.numInvocations + 1;
        this.numInvocations = i2;
        if (i2 > ReflectionFactory.inflationThreshold() && !ReflectUtil.isVMAnonymousClass(this.method.getDeclaringClass())) {
            this.parent.setDelegate((MethodAccessorImpl) new MethodAccessorGenerator().generateMethod(this.method.getDeclaringClass(), this.method.getName(), this.method.getParameterTypes(), this.method.getReturnType(), this.method.getExceptionTypes(), this.method.getModifiers()));
        }
        return invoke0(this.method, obj, objArr);
    }

    void setParent(DelegatingMethodAccessorImpl delegatingMethodAccessorImpl) {
        this.parent = delegatingMethodAccessorImpl;
    }
}
