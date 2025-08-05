package bg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: bg.i, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bg/i.class */
class C1129i implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1121a f8078a;

    C1129i(C1121a c1121a) {
        this.f8078a = c1121a;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8078a.l();
        this.f8078a.f8064f.dispose();
    }
}
