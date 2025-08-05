package r;

import com.efiAnalytics.ui.bV;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import s.C1818g;

/* renamed from: r.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:r/h.class */
class C1805h implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1799b f13437a;

    C1805h(C1799b c1799b) {
        this.f13437a = c1799b;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String strB = bV.b((Component) actionEvent.getSource(), C1818g.b("Select Other Dashboard Layout"), new String[]{"dash"}, "", "./" + aE.a.f2352l);
        if (strB != null) {
            this.f13437a.f13425d.setText(strB);
            this.f13437a.f13422a = new File(strB);
            this.f13437a.b(this.f13437a.f13422a);
        }
    }
}
