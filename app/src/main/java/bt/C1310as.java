package bt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: bt.as, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/as.class */
class C1310as implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1303al f8893a;

    C1310as(C1303al c1303al) {
        this.f8893a = c1303al;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8893a.f8871z.a(C1303al.f8848e, "");
        this.f8893a.f8861p.b(this.f8893a.f8862q.i());
        this.f8893a.f8861p.repaint();
    }
}
