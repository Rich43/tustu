package bk;

/* renamed from: bk.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bk/c.class */
class C1178c extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f8181a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1176a f8182b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C1178c(C1176a c1176a) {
        super("RTC Refresh");
        this.f8182b = c1176a;
        this.f8181a = true;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f8181a) {
            this.f8182b.c();
        }
    }

    public void a() {
        this.f8181a = false;
    }
}
