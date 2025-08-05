package aP;

import s.C1818g;
import s.InterfaceC1817f;
import x.C1891a;

/* loaded from: TunerStudioMS.jar:aP/gR.class */
public class gR extends C1891a implements InterfaceC1817f {

    /* renamed from: c, reason: collision with root package name */
    String f3436c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ C0308dx f3437d;

    public gR(C0308dx c0308dx, String str, boolean z2) {
        this.f3437d = c0308dx;
        this.f3436c = null;
        this.f3436c = str;
        super.setText(C1818g.b(str));
        super.a(z2);
        addMouseListener(new gP(c0308dx, this));
    }

    public gR(C0308dx c0308dx, String str) {
        this(c0308dx, str, true);
    }

    @Override // s.InterfaceC1817f
    public void a(String str) {
        super.setText(str);
        C1818g.b(this.f3436c, str);
    }

    @Override // x.C1891a, s.InterfaceC1817f
    public String a() {
        return this.f3436c;
    }
}
