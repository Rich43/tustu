package aP;

/* renamed from: aP.an, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/an.class */
class C0218an extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0207ac f2919a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C0218an(C0207ac c0207ac) {
        super("PortScanB");
        this.f2919a = c0207ac;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.f2919a.b();
    }
}
