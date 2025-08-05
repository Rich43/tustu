package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import s.C1818g;

/* renamed from: aP.hw, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/hw.class */
class C0415hw implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0413hu f3621a;

    C0415hw(C0413hu c0413hu) {
        this.f3621a = c0413hu;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C0338f.a().k();
        com.efiAnalytics.ui.bV.d(C1818g.b("MegaLogViewer locations cleared, the proper location will be used on next launch."), this.f3621a.f3618a);
    }
}
