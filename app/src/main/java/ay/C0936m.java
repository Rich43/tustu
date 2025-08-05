package ay;

/* renamed from: ay.m, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ay/m.class */
class C0936m extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0935l f6462a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C0936m(C0935l c0935l, String str) {
        super(str);
        this.f6462a = c0935l;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.f6462a.c();
    }
}
