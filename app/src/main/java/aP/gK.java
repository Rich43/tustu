package aP;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aP/gK.class */
class gK implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ gJ f3419a;

    gK(gJ gJVar) {
        this.f3419a = gJVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        C0338f.a().a((Window) cZ.a().c(), actionEvent.getActionCommand());
    }
}
