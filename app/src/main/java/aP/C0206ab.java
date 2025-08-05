package aP;

/* renamed from: aP.ab, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ab.class */
class C0206ab extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0205aa f2882a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C0206ab(C0205aa c0205aa) {
        super("PortScan");
        this.f2882a = c0205aa;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.f2882a.a();
    }
}
