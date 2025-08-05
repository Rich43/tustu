package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/* loaded from: TunerStudioMS.jar:ao/eJ.class */
class eJ implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0737eu f5588a;

    eJ(C0737eu c0737eu) {
        this.f5588a = c0737eu;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5588a.d((JButton) actionEvent.getSource());
    }
}
