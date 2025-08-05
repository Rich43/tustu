package ao;

import W.C0184j;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: ao.ab, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ab.class */
final class C0611ab implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0184j f5170a;

    C0611ab(C0184j c0184j) {
        this.f5170a = c0184j;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5170a.i(0);
        C0645bi.a().c().i();
        C0645bi.a().c().repaint();
    }
}
