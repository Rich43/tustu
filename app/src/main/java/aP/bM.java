package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aP/bM.class */
class bM implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bJ f2985a;

    bM(bJ bJVar) {
        this.f2985a = bJVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f2985a.dispose();
    }
}
