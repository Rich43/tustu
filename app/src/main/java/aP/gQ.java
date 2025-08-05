package aP;

import s.C1818g;
import s.InterfaceC1817f;

/* loaded from: TunerStudioMS.jar:aP/gQ.class */
class gQ extends bA.c implements InterfaceC1817f {

    /* renamed from: a, reason: collision with root package name */
    String f3434a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0308dx f3435b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public gQ(C0308dx c0308dx, String str, boolean z2, boolean z3) {
        super(C1818g.b(str), z2, z3);
        this.f3435b = c0308dx;
        this.f3434a = null;
        this.f3434a = str;
        addMouseListener(new gP(c0308dx, this));
    }

    @Override // s.InterfaceC1817f
    public void a(String str) {
        super.setText(str);
        C1818g.b(this.f3434a, str);
    }

    @Override // s.InterfaceC1817f
    public String a() {
        return this.f3434a;
    }
}
