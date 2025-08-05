package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:ao/cE.class */
class cE implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5459a;

    cE(bP bPVar) {
        this.f5459a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        com.efiAnalytics.ui.aN.a(this.f5459a.x());
    }
}
