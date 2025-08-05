package o;

import d.InterfaceC1711c;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: o.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:o/d.class */
class C1768d implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    InterfaceC1711c f12951a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1765a f12952b;

    public C1768d(C1765a c1765a, InterfaceC1711c interfaceC1711c) {
        this.f12952b = c1765a;
        this.f12951a = interfaceC1711c;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f12952b.a(this.f12952b.a(this.f12951a));
    }
}
