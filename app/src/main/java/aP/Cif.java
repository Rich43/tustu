package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: aP.if, reason: invalid class name */
/* loaded from: TunerStudioMS.jar:aP/if.class */
class Cif implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0423id f3729a;

    Cif(C0423id c0423id) {
        this.f3729a = c0423id;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f3729a.a(actionEvent.getActionCommand());
    }
}
