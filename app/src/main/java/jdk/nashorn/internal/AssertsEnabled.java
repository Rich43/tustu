package jdk.nashorn.internal;

/* loaded from: nashorn.jar:jdk/nashorn/internal/AssertsEnabled.class */
public final class AssertsEnabled {
    private static boolean assertsEnabled;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AssertsEnabled.class.desiredAssertionStatus();
        assertsEnabled = false;
        if ($assertionsDisabled) {
            return;
        }
        assertsEnabled = true;
        if (1 == 0) {
            throw new AssertionError();
        }
    }

    public static boolean assertsEnabled() {
        return assertsEnabled;
    }
}
