package o;

import d.InterfaceC1711c;
import d.k;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import s.C1818g;

/* renamed from: o.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:o/e.class */
class C1769e extends bA.e implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    InterfaceC1711c f12953a;

    /* renamed from: b, reason: collision with root package name */
    k f12954b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1765a f12955c;

    public C1769e(C1765a c1765a, InterfaceC1711c interfaceC1711c, k kVar) {
        this.f12955c = c1765a;
        this.f12953a = interfaceC1711c;
        this.f12954b = kVar;
        super.setText(C1818g.b(interfaceC1711c.b()));
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f12955c.a(this.f12955c.a(this.f12953a));
    }
}
