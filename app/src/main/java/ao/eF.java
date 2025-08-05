package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/* loaded from: TunerStudioMS.jar:ao/eF.class */
class eF implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0737eu f5584a;

    eF(C0737eu c0737eu) {
        this.f5584a = c0737eu;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5584a.e((JButton) actionEvent.getSource());
    }
}
