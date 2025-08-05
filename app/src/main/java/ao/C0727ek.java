package ao;

/* renamed from: ao.ek, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ek.class */
class C0727ek implements InterfaceC0797h {

    /* renamed from: a, reason: collision with root package name */
    int f5637a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0718eb f5638b;

    public C0727ek(C0718eb c0718eb, int i2) {
        this.f5638b = c0718eb;
        this.f5637a = 0;
        this.f5637a = i2 * 2;
    }

    @Override // ao.InterfaceC0797h
    public void a(String str) {
        if (this.f5638b.f5619e != null) {
            this.f5638b.f5617c.a("singleGraph", this.f5638b.f5619e.a(str), this.f5637a);
        }
    }

    @Override // ao.InterfaceC0797h
    public void b(String str) {
        if (this.f5638b.f5619e != null) {
            this.f5638b.f5617c.a("singleGraph", this.f5638b.f5619e.a(str), this.f5637a + 1);
        }
    }

    @Override // ao.InterfaceC0797h
    public void c(String str) {
        if (this.f5638b.f5619e != null) {
            this.f5638b.f5617c.a("singleGraph", this.f5638b.f5619e.a(str), this.f5637a + 2);
        }
    }
}
