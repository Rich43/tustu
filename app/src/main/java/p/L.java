package p;

import com.efiAnalytics.ui.bV;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:p/L.class */
class L implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ J f13172a;

    L(J j2) {
        this.f13172a = j2;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (bV.a(this.f13172a.a("Are you sure your want to delete the selected User Action?"), (Component) this.f13172a.f13166c, true)) {
            this.f13172a.e();
        }
    }
}
