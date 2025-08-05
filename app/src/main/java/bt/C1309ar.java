package bt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: bt.ar, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/ar.class */
class C1309ar implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1303al f8892a;

    C1309ar(C1303al c1303al) {
        this.f8892a = c1303al;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8892a.f8871z.a(C1303al.f8847d, "");
        this.f8892a.f8861p.c(this.f8892a.f8862q.h());
        this.f8892a.f8861p.repaint();
    }
}
