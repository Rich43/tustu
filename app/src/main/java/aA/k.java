package aA;

/* loaded from: TunerStudioMS.jar:aA/k.class */
class k extends Thread {

    /* renamed from: a, reason: collision with root package name */
    String f2268a;

    /* renamed from: b, reason: collision with root package name */
    String f2269b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ h f2270c;

    k(h hVar, String str, String str2) {
        this.f2270c = hVar;
        this.f2268a = str;
        this.f2269b = str2;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.f2270c.b(this.f2268a, this.f2269b);
    }
}
