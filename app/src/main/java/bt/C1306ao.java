package bt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: bt.ao, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/ao.class */
class C1306ao implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1303al f8889a;

    C1306ao(C1303al c1303al) {
        this.f8889a = c1303al;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8889a.f8871z.a(C1303al.f8850g, "");
        this.f8889a.f8861p.e(this.f8889a.f8862q.b());
        this.f8889a.f8861p.repaint();
    }
}
