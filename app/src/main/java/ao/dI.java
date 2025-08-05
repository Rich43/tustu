package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:ao/dI.class */
class dI implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5522a;

    dI(bP bPVar) {
        this.f5522a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5522a.h();
    }
}
