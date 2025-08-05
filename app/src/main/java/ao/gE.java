package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:ao/gE.class */
class gE implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fX f5897a;

    gE(fX fXVar) {
        this.f5897a = fXVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5897a.f5736l.h().b(((Integer) this.f5897a.f5730f.getSelectedItem()).intValue());
        this.f5897a.f5736l.k();
        this.f5897a.m();
    }
}
