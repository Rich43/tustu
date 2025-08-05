package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: ao.cz, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/cz.class */
class C0689cz implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5510a;

    C0689cz(bP bPVar) {
        this.f5510a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        h.i.c("forceOpenGL", Boolean.toString(false));
        h.i.c("disableD3d", Boolean.toString(false));
        this.f5510a.D();
    }
}
