package aP;

import s.C1818g;
import s.InterfaceC1817f;

/* loaded from: TunerStudioMS.jar:aP/gS.class */
class gS extends bA.e implements InterfaceC1817f {

    /* renamed from: a, reason: collision with root package name */
    String f3438a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0308dx f3439b;

    public gS(C0308dx c0308dx, String str, boolean z2) {
        this.f3439b = c0308dx;
        this.f3438a = null;
        super.setText(C1818g.b(str));
        super.a(z2);
        this.f3438a = str;
        addMouseListener(new gP(c0308dx, this));
    }

    @Override // s.InterfaceC1817f
    public void a(String str) {
        super.setText(str);
        C1818g.b(this.f3438a, str);
    }

    @Override // s.InterfaceC1817f
    public String a() {
        return this.f3438a;
    }
}
