package bx;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:bx/n.class */
class n implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ m f9212a;

    n(m mVar) {
        this.f9212a = mVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f9212a.c()) {
            this.f9212a.b();
        }
    }
}
