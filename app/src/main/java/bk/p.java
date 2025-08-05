package bk;

import aI.w;
import com.efiAnalytics.ui.bV;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JToggleButton;

/* loaded from: TunerStudioMS.jar:bk/p.class */
class p implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ o f8228a;

    p(o oVar) {
        this.f8228a = oVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JToggleButton jToggleButton = (JToggleButton) actionEvent.getSource();
        try {
            if (jToggleButton.isSelected()) {
                this.f8228a.b();
            } else {
                this.f8228a.c();
            }
        } catch (w e2) {
            bV.d(e2.getMessage(), jToggleButton);
        }
    }
}
