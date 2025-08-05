package sun.tracing.dtrace;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import sun.tracing.ProbeSkeleton;

/* loaded from: rt.jar:sun/tracing/dtrace/DTraceProbe.class */
class DTraceProbe extends ProbeSkeleton {
    private Object proxy;
    private Method declared_method;
    private Method implementing_method;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DTraceProbe.class.desiredAssertionStatus();
    }

    DTraceProbe(Object obj, Method method) {
        super(method.getParameterTypes());
        this.proxy = obj;
        this.declared_method = method;
        try {
            this.implementing_method = obj.getClass().getMethod(method.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException e2) {
            throw new RuntimeException("Internal error, wrong proxy class");
        }
    }

    @Override // sun.tracing.ProbeSkeleton, com.sun.tracing.Probe
    public boolean isEnabled() {
        return JVM.isEnabled(this.implementing_method);
    }

    @Override // sun.tracing.ProbeSkeleton
    public void uncheckedTrigger(Object[] objArr) throws IllegalArgumentException {
        try {
            this.implementing_method.invoke(this.proxy, objArr);
        } catch (IllegalAccessException e2) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        } catch (InvocationTargetException e3) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        }
    }

    String getProbeName() {
        return DTraceProvider.getProbeName(this.declared_method);
    }

    String getFunctionName() {
        return DTraceProvider.getFunctionName(this.declared_method);
    }

    Method getMethod() {
        return this.implementing_method;
    }

    Class<?>[] getParameterTypes() {
        return this.parameters;
    }
}
