package bs;

import com.efiAnalytics.ui.bV;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:bs/n.class */
class n implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f8624a;

    n(k kVar) {
        this.f8624a = kVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f8624a.f8604a.f(actionEvent.getActionCommand());
        try {
            this.f8624a.f8605b.c();
        } catch (V.g e2) {
            bV.d(e2.getMessage(), this.f8624a.f8614k);
        }
        this.f8624a.f8617n.a("targetLambdaTableName", this.f8624a.f8604a.f());
    }
}
