package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: ao.am, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/am.class */
class C0622am implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0620ak f5224a;

    C0622am(C0620ak c0620ak) {
        this.f5224a = c0620ak;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C0636b.a().a(com.efiAnalytics.ui.bV.a(this.f5224a.getParent()));
    }
}
