package bt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: bt.ap, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/ap.class */
class C1307ap implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1303al f8890a;

    C1307ap(C1303al c1303al) {
        this.f8890a = c1303al;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8890a.f8871z.a(C1303al.f8851h, "");
        this.f8890a.f8861p.d(this.f8890a.f8862q.c());
        this.f8890a.f8861p.repaint();
    }
}
