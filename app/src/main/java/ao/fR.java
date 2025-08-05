package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:ao/fR.class */
class fR implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0764fu f5716a;

    fR(C0764fu c0764fu) {
        this.f5716a = c0764fu;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f5716a.d(actionEvent.getActionCommand());
    }
}
