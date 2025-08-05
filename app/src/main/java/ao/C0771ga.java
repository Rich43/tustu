package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: ao.ga, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ga.class */
class C0771ga implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ fX f5970a;

    C0771ga(fX fXVar) {
        this.f5970a = fXVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5970a.f5736l.h().a(((Integer) this.f5970a.f5732h.getSelectedItem()).intValue());
        this.f5970a.f5736l.k();
        this.f5970a.m();
    }
}
