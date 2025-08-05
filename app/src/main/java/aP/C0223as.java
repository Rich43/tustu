package aP;

/* renamed from: aP.as, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/as.class */
class C0223as {

    /* renamed from: b, reason: collision with root package name */
    private String f2929b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f2930c = null;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0220ap f2931a;

    C0223as(C0220ap c0220ap) {
        this.f2931a = c0220ap;
    }

    public String toString() {
        return a();
    }

    public boolean equals(Object obj) {
        return obj instanceof String ? obj.equals(this.f2929b) : obj instanceof C0223as ? ((C0223as) obj).f2929b.equals(this.f2929b) : super.equals(obj);
    }

    public String a() {
        return this.f2930c;
    }

    public void a(String str) {
        this.f2930c = str;
    }

    public String b() {
        return this.f2929b;
    }

    public void b(String str) {
        this.f2929b = str;
    }
}
