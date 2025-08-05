package aP;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import r.C1798a;

/* renamed from: aP.az, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/az.class */
class C0230az implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0224at f2962a;

    C0230az(C0224at c0224at) {
        this.f2962a = c0224at;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String strA = com.efiAnalytics.ui.bV.a(new File(this.f2962a.f2933b.getText()).getParent(), (Component) actionEvent.getSource());
        if (strA != null) {
            this.f2962a.f(strA);
            C1798a.a().b(C1798a.f13366aV, this.f2962a.b());
        }
    }
}
