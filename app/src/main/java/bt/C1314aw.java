package bt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: bt.aw, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/aw.class */
class C1314aw implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1303al f8897a;

    C1314aw(C1303al c1303al) {
        this.f8897a = c1303al;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8897a.f8871z.a(C1303al.f8858o, "");
        this.f8897a.f8861p.l(this.f8897a.f8862q.a());
        this.f8897a.f8861p.repaint();
    }
}
