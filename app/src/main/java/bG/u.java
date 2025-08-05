package bG;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:bG/u.class */
class u implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ q f6980a;

    u(q qVar) {
        this.f6980a = qVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f6980a.f6974a.i();
        this.f6980a.f6974a.repaint();
    }
}
