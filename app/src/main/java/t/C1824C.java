package t;

import com.efiAnalytics.apps.ts.dashboard.Gauge;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: t.C, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/C.class */
class C1824C implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1875w f13688a;

    C1824C(C1875w c1875w) {
        this.f13688a = c1875w;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (((JCheckBoxMenuItem) actionEvent.getSource()).getState()) {
            this.f13688a.f13910a.d(Gauge.f9323F);
        }
    }
}
