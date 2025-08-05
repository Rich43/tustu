package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aP/bI.class */
class bI implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bD f2979a;

    bI(bD bDVar) {
        this.f2979a = bDVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f2979a.dispose();
    }
}
