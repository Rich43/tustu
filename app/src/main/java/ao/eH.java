package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/* loaded from: TunerStudioMS.jar:ao/eH.class */
class eH implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0737eu f5586a;

    eH(C0737eu c0737eu) {
        this.f5586a = c0737eu;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5586a.f((JButton) actionEvent.getSource());
    }
}
