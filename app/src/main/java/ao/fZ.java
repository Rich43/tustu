package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:ao/fZ.class */
class fZ implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fX f5793a;

    fZ(fX fXVar) {
        this.f5793a = fXVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5793a.f5736l.b(((Integer) this.f5793a.f5731g.getSelectedItem()).intValue());
        this.f5793a.f5736l.k();
        this.f5793a.m();
    }
}
