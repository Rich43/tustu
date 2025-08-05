package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aP/aH.class */
class aH implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aF f2817a;

    aH(aF aFVar) {
        this.f2817a = aFVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f2817a.close();
    }
}
