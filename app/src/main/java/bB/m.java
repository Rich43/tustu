package bB;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:bB/m.class */
class m implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ l f6564a;

    m(l lVar) {
        this.f6564a = lVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f6564a.c();
        this.f6564a.f6558b.f();
        this.f6564a.f6558b.setEnabled(false);
        this.f6564a.f6561d.setEnabled(false);
        this.f6564a.f6562e.setEnabled(false);
    }
}
