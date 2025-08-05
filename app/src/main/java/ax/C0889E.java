package ax;

/* renamed from: ax.E, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/E.class */
class C0889E implements ab {

    /* renamed from: a, reason: collision with root package name */
    InterfaceC0888D f6324a;

    public C0889E(InterfaceC0888D interfaceC0888D) {
        this.f6324a = null;
        this.f6324a = interfaceC0888D;
    }

    public InterfaceC0888D a() {
        return this.f6324a;
    }

    @Override // ax.ab
    public double b(S s2) throws an {
        if ((this.f6324a instanceof am) && s2.b() == 1 && !((am) this.f6324a).b()) {
            throw new an(this.f6324a.a());
        }
        return this.f6324a.b(s2);
    }

    public String toString() {
        return this.f6324a.a();
    }
}
