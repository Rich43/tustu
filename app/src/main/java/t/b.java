package T;

import G.InterfaceC0042ab;

/* loaded from: TunerStudioMS.jar:T/b.class */
class b implements InterfaceC0042ab {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ a f1861a;

    b(a aVar) {
        this.f1861a = aVar;
    }

    @Override // G.InterfaceC0042ab
    public void a(String str, int i2, int i3, int[] iArr) {
        this.f1861a.c().a(new d(this.f1861a, str, i2, i3, iArr.length));
    }
}
