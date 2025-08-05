package ao;

import W.C0188n;
import java.awt.Rectangle;

/* renamed from: ao.gs, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/gs.class */
class RunnableC0789gs implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0188n f5988a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ String f5989b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ fX f5990c;

    RunnableC0789gs(fX fXVar, C0188n c0188n, String str) {
        this.f5990c = fXVar;
        this.f5988a = c0188n;
        this.f5989b = str;
    }

    @Override // java.lang.Runnable
    public void run() throws IllegalArgumentException {
        this.f5990c.f5773O.a(this.f5988a.d() + "");
        this.f5990c.f5773O.b(this.f5989b);
        this.f5990c.f5773O.a();
        this.f5990c.b().C();
        this.f5990c.a(C0804hg.a().p());
        this.f5990c.f5783W.a();
        Rectangle bounds = this.f5990c.f5736l.getBounds();
        this.f5990c.f5736l.setBounds(bounds.f12372x, bounds.f12373y, bounds.width, bounds.height);
    }
}
