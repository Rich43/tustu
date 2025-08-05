package ao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:ao/bU.class */
class bU implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5386a;

    bU(bP bPVar) {
        this.f5386a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (C0804hg.a().r() == null || this.f5386a.f5374t == null) {
            com.efiAnalytics.ui.bV.d("No Log File loaded.", C0645bi.a().b());
        } else {
            C0636b.a().a(C0804hg.a().r(), this.f5386a.f5374t, C0645bi.a().b());
        }
    }
}
