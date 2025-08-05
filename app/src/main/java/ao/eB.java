package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/* loaded from: TunerStudioMS.jar:ao/eB.class */
class eB implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ JButton f5578a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0737eu f5579b;

    eB(C0737eu c0737eu, JButton jButton) {
        this.f5579b = c0737eu;
        this.f5578a = jButton;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5579b.i(this.f5578a);
    }
}
