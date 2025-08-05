package com.sun.webkit;

/* loaded from: jfxrt.jar:com/sun/webkit/MainThread.class */
final class MainThread {
    /* JADX INFO: Access modifiers changed from: private */
    public static native void twkScheduleDispatchFunctions();

    static native void twkSetShutdown(boolean z2);

    MainThread() {
    }

    private static void fwkScheduleDispatchFunctions() {
        Invoker.getInvoker().postOnEventThread(() -> {
            twkScheduleDispatchFunctions();
        });
    }
}
