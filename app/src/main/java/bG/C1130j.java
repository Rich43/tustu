package bg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: bg.j, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bg/j.class */
class C1130j implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1121a f8079a;

    C1130j(C1121a c1121a) {
        this.f8079a = c1121a;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f8079a.m()) {
            this.f8079a.j();
            this.f8079a.l();
            this.f8079a.f8064f.dispose();
        }
    }
}
