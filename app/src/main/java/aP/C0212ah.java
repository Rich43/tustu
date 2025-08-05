package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: aP.ah, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ah.class */
class C0212ah implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0207ac f2910a;

    C0212ah(C0207ac c0207ac) {
        this.f2910a = c0207ac;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f2910a.f2889g.setEnabled(false);
        this.f2910a.c();
    }
}
