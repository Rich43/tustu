package bC;

/* loaded from: TunerStudioMS.jar:bC/o.class */
class o implements a {

    /* renamed from: a, reason: collision with root package name */
    boolean f6603a = false;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ k f6604b;

    o(k kVar) {
        this.f6604b = kVar;
    }

    @Override // bC.a
    public void a() {
        this.f6603a = true;
        this.f6604b.f6596f.setEnabled(true);
    }

    @Override // bC.a
    public void b() {
        this.f6604b.f6596f.setEnabled(false);
    }

    public boolean c() {
        return this.f6603a;
    }
}
