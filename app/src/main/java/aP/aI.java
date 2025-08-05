package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aP/aI.class */
class aI implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aF f2818a;

    aI(aF aFVar) {
        this.f2818a = aFVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.f2818a.e()) {
            this.f2818a.close();
        }
    }
}
