package t;

import com.efiAnalytics.apps.ts.dashboard.Gauge;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: t.D, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/D.class */
class C1825D implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1875w f13689a;

    C1825D(C1875w c1875w) {
        this.f13689a = c1875w;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (((JCheckBoxMenuItem) actionEvent.getSource()).getState()) {
            this.f13689a.f13910a.d(Gauge.f9324G);
        }
    }
}
