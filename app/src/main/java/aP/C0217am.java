package aP;

import z.C1900d;

/* renamed from: aP.am, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/am.class */
class C0217am {

    /* renamed from: b, reason: collision with root package name */
    private C1900d f2916b;

    /* renamed from: c, reason: collision with root package name */
    private String f2917c = null;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0207ac f2918a;

    C0217am(C0207ac c0207ac, C1900d c1900d) {
        this.f2918a = c0207ac;
        this.f2916b = null;
        this.f2916b = c1900d;
    }

    public String toString() {
        return b() != null ? b() : a() == null ? "INVALID" : a().b();
    }

    public C1900d a() {
        return this.f2916b;
    }

    public String b() {
        return this.f2917c;
    }

    public void a(String str) {
        this.f2917c = str;
    }

    public boolean equals(Object obj) {
        return obj instanceof C0217am ? this.f2916b.equals(((C0217am) obj).a()) : obj instanceof String ? this.f2916b.a().equals((String) obj) : super.equals(obj);
    }
}
