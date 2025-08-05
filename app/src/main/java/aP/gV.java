package aP;

import java.awt.CardLayout;

/* loaded from: TunerStudioMS.jar:aP/gV.class */
class gV implements bA.a {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3448a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ gU f3449b;

    gV(gU gUVar, C0308dx c0308dx) {
        this.f3449b = gUVar;
        this.f3448a = c0308dx;
    }

    @Override // bA.a
    public void a(String str) {
        ((CardLayout) this.f3449b.f3443c.getLayout()).show(this.f3449b.f3443c, str);
    }
}
