package aP;

/* renamed from: aP.dq, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/dq.class */
class C0301dq extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0293dh f3245a;

    C0301dq(C0293dh c0293dh) {
        this.f3245a = c0293dh;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(120000L);
            bH.C.c("Application close timeout. Initiating forced close request.");
            bH.C.g();
            bH.C.c("Application close timeout. All threads stacks printed. Exiting.");
            Runtime.getRuntime().halt(3);
        } catch (InterruptedException e2) {
        }
    }
}
