package aP;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;

/* renamed from: aP.bs, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/bs.class */
class C0250bs implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0240bi f3095a;

    C0250bs(C0240bi c0240bi) {
        this.f3095a = c0240bi;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        Frame frameA = com.efiAnalytics.ui.bV.a((Component) actionEvent.getSource());
        try {
            C0338f.a().a(actionEvent.getActionCommand(), ((JComponent) actionEvent.getSource()).getName(), frameA);
        } catch (Exception e2) {
            bH.C.a("Error showing dialog:\n" + e2.getMessage(), e2, frameA);
        }
    }
}
