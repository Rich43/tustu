package aP;

import G.C0129l;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: aP.al, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/al.class */
class C0216al implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0207ac f2915a;

    C0216al(C0207ac c0207ac) {
        this.f2915a = c0207ac;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f2915a.j();
        this.f2915a.i();
        try {
            this.f2915a.f2891i.C().d();
        } catch (C0129l e2) {
            bH.C.d("Unable to go online. Error: " + e2.getLocalizedMessage());
        }
    }
}
