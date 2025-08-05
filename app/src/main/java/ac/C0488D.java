package ac;

import bH.W;

/* renamed from: ac.D, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ac/D.class */
public class C0488D extends s implements InterfaceC0489a {

    /* renamed from: j, reason: collision with root package name */
    private InterfaceC0487C f4176j = null;

    /* renamed from: k, reason: collision with root package name */
    private int f4177k = 0;

    public C0488D() {
        this.f4258i = new byte[34];
    }

    public void a(InterfaceC0487C interfaceC0487C) {
        this.f4176j = interfaceC0487C;
    }

    @Override // ac.InterfaceC0489a
    public String c() {
        return this.f4177k == 7 ? this.f4176j.a() != 0.0d ? "Active" : "Inactive" : this.f4177k == 6 ? this.f4176j.a() != 0.0d ? "High" : "Low" : this.f4177k == 4 ? this.f4176j.a() != 0.0d ? "On" : "Off" : this.f4177k == 5 ? this.f4176j.a() != 0.0d ? "Yes" : "No" : W.c(this.f4176j.a(), i());
    }

    @Override // ac.InterfaceC0489a
    public String a() {
        return new String(super.d());
    }

    @Override // ac.InterfaceC0489a
    public String b() {
        return new String(super.e());
    }

    public double j() {
        return this.f4176j.a();
    }

    public int k() {
        return this.f4177k;
    }
}
