package aP;

import s.C1818g;
import s.InterfaceC1817f;
import x.C1891a;

/* loaded from: TunerStudioMS.jar:aP/gI.class */
class gI extends C1891a implements InterfaceC1817f {

    /* renamed from: c, reason: collision with root package name */
    String f3416c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ C0308dx f3417d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public gI(C0308dx c0308dx, String str, boolean z2) {
        super(str, z2);
        this.f3417d = c0308dx;
        this.f3416c = null;
        this.f3416c = str;
        super.setText(C1818g.b(str));
        addMouseListener(new gP(c0308dx, this));
    }

    @Override // x.C1891a
    public boolean b() {
        return false;
    }

    @Override // s.InterfaceC1817f
    public void a(String str) {
        super.setText(str);
        C1818g.b(this.f3416c, str);
    }

    @Override // x.C1891a, s.InterfaceC1817f
    public String a() {
        return this.f3416c;
    }
}
