package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:ao/cH.class */
class cH implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5463a;

    cH(bP bPVar) {
        this.f5463a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        com.efiAnalytics.ui.aN.a(this.f5463a.x());
    }
}
