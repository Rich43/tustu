package p;

import com.efiAnalytics.ui.bV;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: p.r, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:p/r.class */
class C1792r implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1790p f13243a;

    C1792r(C1790p c1790p) {
        this.f13243a = c1790p;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (bV.a(this.f13243a.a("Are you sure your want to delete the selected Action Trigger?"), (Component) this.f13243a.f13237c, true)) {
            this.f13243a.e();
        }
    }
}
