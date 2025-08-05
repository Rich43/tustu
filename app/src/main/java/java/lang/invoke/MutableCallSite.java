package java.lang.invoke;

import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: rt.jar:java/lang/invoke/MutableCallSite.class */
public class MutableCallSite extends CallSite {
    private static final AtomicInteger STORE_BARRIER = new AtomicInteger();

    public MutableCallSite(MethodType methodType) {
        super(methodType);
    }

    public MutableCallSite(MethodHandle methodHandle) {
        super(methodHandle);
    }

    @Override // java.lang.invoke.CallSite
    public final MethodHandle getTarget() {
        return this.target;
    }

    @Override // java.lang.invoke.CallSite
    public void setTarget(MethodHandle methodHandle) {
        checkTargetChange(this.target, methodHandle);
        setTargetNormal(methodHandle);
    }

    @Override // java.lang.invoke.CallSite
    public final MethodHandle dynamicInvoker() {
        return makeDynamicInvoker();
    }

    public static void syncAll(MutableCallSite[] mutableCallSiteArr) {
        if (mutableCallSiteArr.length == 0) {
            return;
        }
        STORE_BARRIER.lazySet(0);
        for (MutableCallSite mutableCallSite : mutableCallSiteArr) {
            mutableCallSite.getClass();
        }
    }
}
