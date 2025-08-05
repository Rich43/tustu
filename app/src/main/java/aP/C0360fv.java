package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;

/* renamed from: aP.fv, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/fv.class */
class C0360fv implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3392a;

    C0360fv(C0308dx c0308dx) {
        this.f3392a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            this.f3392a.f3264c.a(actionEvent.getActionCommand(), ((JComponent) actionEvent.getSource()).getName(), this.f3392a.f3270h);
        } catch (Exception e2) {
            bH.C.a("Error showing dialog:\n" + e2.getMessage(), e2, this.f3392a.f3270h);
        }
    }
}
