package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aP/gC.class */
class gC implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0308dx f3403a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ gB f3404b;

    gC(gB gBVar, C0308dx c0308dx) {
        this.f3404b = gBVar;
        this.f3403a = c0308dx;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f3404b.d((String) this.f3404b.f3400a.getSelectedItem());
    }
}
