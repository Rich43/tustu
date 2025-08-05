package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/* loaded from: TunerStudioMS.jar:ao/eC.class */
class eC implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ JButton f5580a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0737eu f5581b;

    eC(C0737eu c0737eu, JButton jButton) {
        this.f5581b = c0737eu;
        this.f5580a = jButton;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5581b.j(this.f5580a);
    }
}
