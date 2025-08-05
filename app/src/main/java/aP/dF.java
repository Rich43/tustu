package aP;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/dF.class */
class dF implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3184a;

    dF(C0308dx c0308dx) {
        this.f3184a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        Component component = (gS) actionEvent.getSource();
        if (com.efiAnalytics.ui.bV.a(C1818g.b("This COM Port is currently disabled from Detect Scans because it has caused issues on past attempts to access it.") + "\n\n" + C1818g.b("Are you sure you want to reactivate com port:") + component.getName(), component, true)) {
            Q.a().b(component.getName());
            component.getParent().remove(component);
        }
    }
}
