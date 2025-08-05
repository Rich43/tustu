package defpackage;

import W.C0188n;
import ao.C0804hg;
import bH.C;
import i.InterfaceC1742b;
import javax.swing.SwingUtilities;

/* loaded from: TunerStudioMS.jar:c.class */
class c implements InterfaceC1742b {

    /* renamed from: a, reason: collision with root package name */
    boolean f9253a = false;

    /* renamed from: b, reason: collision with root package name */
    boolean f9254b = false;

    /* renamed from: c, reason: collision with root package name */
    C0188n f9255c = null;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ b f9256d;

    c(b bVar) {
        this.f9256d = bVar;
    }

    @Override // i.InterfaceC1742b
    public void a() {
    }

    @Override // i.InterfaceC1742b
    public void a(double d2) {
    }

    @Override // i.InterfaceC1742b
    public void a(C0188n c0188n) {
        this.f9255c = c0188n;
        if (this.f9253a) {
            return;
        }
        C.c("Setting index to: " + (c0188n.d() - 1));
        C0804hg.a().c(c0188n.d() - 1);
        SwingUtilities.invokeLater(new d(this));
        this.f9253a = true;
    }

    @Override // i.InterfaceC1742b
    public void b(C0188n c0188n) {
    }

    @Override // i.InterfaceC1742b
    public void b() {
        if (this.f9255c == null || this.f9254b || this.f9255c.d() <= 200) {
            return;
        }
        this.f9254b = true;
        C.c("Setting index: " + (this.f9255c.d() - 1));
        C0804hg.a().e();
        C0804hg.a().c(this.f9255c.d() - 1);
    }
}
