package java.lang.invoke;

/* loaded from: rt.jar:java/lang/invoke/SwitchPoint.class */
public class SwitchPoint {
    private static final MethodHandle K_true = MethodHandles.constant(Boolean.TYPE, true);
    private static final MethodHandle K_false = MethodHandles.constant(Boolean.TYPE, false);
    private final MutableCallSite mcs = new MutableCallSite(K_true);
    private final MethodHandle mcsInvoker = this.mcs.dynamicInvoker();

    public boolean hasBeenInvalidated() {
        return this.mcs.getTarget() != K_true;
    }

    public MethodHandle guardWithTest(MethodHandle methodHandle, MethodHandle methodHandle2) {
        if (this.mcs.getTarget() == K_false) {
            return methodHandle2;
        }
        return MethodHandles.guardWithTest(this.mcsInvoker, methodHandle, methodHandle2);
    }

    public static void invalidateAll(SwitchPoint[] switchPointArr) {
        SwitchPoint switchPoint;
        if (switchPointArr.length == 0) {
            return;
        }
        MutableCallSite[] mutableCallSiteArr = new MutableCallSite[switchPointArr.length];
        for (int i2 = 0; i2 < switchPointArr.length && (switchPoint = switchPointArr[i2]) != null; i2++) {
            mutableCallSiteArr[i2] = switchPoint.mcs;
            switchPoint.mcs.setTarget(K_false);
        }
        MutableCallSite.syncAll(mutableCallSiteArr);
    }
}
