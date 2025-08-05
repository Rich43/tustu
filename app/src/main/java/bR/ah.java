package br;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:br/ah.class */
class ah implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ ag f8438a;

    ah(ag agVar) {
        this.f8438a = agVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8438a.b();
        this.f8438a.f8428d.a("deactivated", Boolean.toString(this.f8438a.f8437m.isSelected()));
    }
}
