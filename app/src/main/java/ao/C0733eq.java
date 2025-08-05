package ao;

/* renamed from: ao.eq, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/eq.class */
class C0733eq implements InterfaceC0797h {

    /* renamed from: a, reason: collision with root package name */
    int f5655a = 0;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0729em f5656b;

    public C0733eq(C0729em c0729em) {
        this.f5656b = c0729em;
    }

    @Override // ao.InterfaceC0797h
    public void a(String str) {
        if (this.f5656b.f5643d != null) {
            this.f5656b.f5641b.a("singleGraph", this.f5656b.f5643d.a(str), this.f5655a);
        }
    }

    @Override // ao.InterfaceC0797h
    public void b(String str) {
        if (this.f5656b.f5643d != null) {
            this.f5656b.f5641b.a("singleGraph", this.f5656b.f5643d.a(str), this.f5655a + 1);
        }
    }

    @Override // ao.InterfaceC0797h
    public void c(String str) {
        if (this.f5656b.f5643d != null) {
            this.f5656b.f5641b.a("singleGraph", this.f5656b.f5643d.a(str), this.f5655a + 2);
            if (this.f5656b.f5646g.f() == null || this.f5656b.f5643d.a(this.f5656b.f5646g.f()) == null) {
                return;
            }
            this.f5656b.f5641b.a("singleGraph", this.f5656b.f5643d.a(this.f5656b.f5646g.f()), this.f5655a + 3);
        }
    }
}
