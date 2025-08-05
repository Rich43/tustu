package java.lang.invoke;

/* loaded from: rt.jar:java/lang/invoke/ConstantCallSite.class */
public class ConstantCallSite extends CallSite {
    private final boolean isFrozen;

    public ConstantCallSite(MethodHandle methodHandle) {
        super(methodHandle);
        this.isFrozen = true;
    }

    protected ConstantCallSite(MethodType methodType, MethodHandle methodHandle) throws Throwable {
        super(methodType, methodHandle);
        this.isFrozen = true;
    }

    @Override // java.lang.invoke.CallSite
    public final MethodHandle getTarget() {
        if (this.isFrozen) {
            return this.target;
        }
        throw new IllegalStateException();
    }

    @Override // java.lang.invoke.CallSite
    public final void setTarget(MethodHandle methodHandle) {
        throw new UnsupportedOperationException();
    }

    @Override // java.lang.invoke.CallSite
    public final MethodHandle dynamicInvoker() {
        return getTarget();
    }
}
