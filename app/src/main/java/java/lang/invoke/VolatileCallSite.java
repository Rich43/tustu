package java.lang.invoke;

/* loaded from: rt.jar:java/lang/invoke/VolatileCallSite.class */
public class VolatileCallSite extends CallSite {
    public VolatileCallSite(MethodType methodType) {
        super(methodType);
    }

    public VolatileCallSite(MethodHandle methodHandle) {
        super(methodHandle);
    }

    @Override // java.lang.invoke.CallSite
    public final MethodHandle getTarget() {
        return getTargetVolatile();
    }

    @Override // java.lang.invoke.CallSite
    public void setTarget(MethodHandle methodHandle) {
        checkTargetChange(getTargetVolatile(), methodHandle);
        setTargetVolatile(methodHandle);
    }

    @Override // java.lang.invoke.CallSite
    public final MethodHandle dynamicInvoker() {
        return makeDynamicInvoker();
    }
}
