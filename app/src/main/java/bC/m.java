package bC;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:bC/m.class */
class m implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f6601a;

    m(k kVar) {
        this.f6601a = kVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f6601a.c();
        this.f6601a.f6593d.g();
        this.f6601a.f6593d.setEnabled(false);
        this.f6601a.f6596f.setEnabled(false);
    }
}
